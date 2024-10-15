from flask import Flask, jsonify, request
from flask_mysqldb import MySQL
import MySQLdb
import random
import string
import hashlib
import asyncio
from concurrent.futures import ThreadPoolExecutor
import threading

# config vars
app = Flask(__name__)
app.config['MYSQL_HOST'] = 'localhost'
app.config['MYSQL_USER'] = 'android'
app.config['MYSQL_PASSWORD'] = 'android'
app.config['MYSQL_DB'] = 'mensadb'
mysql = MySQL(app)

tmp_code = "not yet generated"
tmp_code_refresh_time = 30
###

# REST API FUNCTIONS

#* hello world
@app.route('/')
def hello_world():
    return 'Hello, World!'

#* GET ALL USERS
@app.route('/data/', methods=['GET'])
def get_all_users():
    cur = None
    try:
        cur = mysql.connection.cursor()
        cur.execute('SELECT * FROM users')
        rows = cur.fetchall()

        if not rows:
            return jsonify({"error": "No data found"}), 404

        columns = [desc[0] for desc in cur.description]
        users_list = [dict(zip(columns, row)) for row in rows]

        return jsonify(users_list)
    except Exception as e:
        print(f"Error fetching all users: {e}")
        return jsonify({"status": "fail", "message": "Error fetching data"}), 500
    finally:
        if cur:
            cur.close()

#* SHOW TRANSACTIONS OF A USER
@app.route('/transactions/<int:id>', methods=["GET"])
def get_transactions(id):
    cur = None
    try:
        cur = mysql.connection.cursor()
        cur.execute('SELECT * FROM transazioni WHERE user_id = %s', (id,))
        rows = cur.fetchall()
        if not rows:
            return jsonify({"error": "No data found"}), 404

        columns = [desc[0] for desc in cur.description]
        transactions_list = [dict(zip(columns, row)) for row in rows]

        return jsonify(transactions_list)
    except Exception as e:
        print(f"Error fetching transactions: {e}")
        return jsonify({"status": "fail", "message": "Error fetching data"}), 500
    finally:
        if cur:
            cur.close()


@app.route('/addCredit', methods=['POST'])
def add_credit():
    try:
        user_id = request.json.get('id')
        value = request.json.get('value')

        if not user_id or not value:
            return jsonify({"status": "fail", "message": "User ID or value is missing"}), 400

        if create_new_transaction(user_id, value):
            return jsonify({"status": "success", "message": "Credit updated successfully"}), 200
        else:
            return jsonify({"status": "fail", "message": "Error creating transaction"}), 500

    except Exception as e:
        print(f"Error occurred while adding credit: {e}")
        return jsonify({"status": "fail", "message": "An error occurred while adding credit"}), 500

def create_new_transaction(user_id, value):
    cur = None
    try:
        cur = mysql.connection.cursor()
        cur.execute(
            'INSERT INTO transazioni (user_id, importo, modalita) VALUES (%s, %s, %s)',
            (user_id, value, "debug")
        )
        mysql.connection.commit()
        print("Successfully created new transaction")
        return True  
    except Exception as e:
        print(f"Error creating transaction: {e}")
        return False 
    finally:
        if cur:
            cur.close()



@app.route('/data/<int:id>', methods=['GET'])
def get_data_by_id(id):
    cur = None
    try:
        cur = mysql.connection.cursor()
        cur.execute('SELECT * FROM users WHERE id = %s', (id,))
        data = cur.fetchall()

        if not data:
            return jsonify({"error": "No user found with that ID"}), 404

        return jsonify(data)
    except Exception as e:
        print(f"Error fetching data by ID: {e}")
        return jsonify({"status": "fail", "message": "Error fetching data"}), 500
    finally:
        if cur:
            cur.close()

@app.route('/login', methods=['POST'])
def login():
    cur = None
    try:
        email = request.json.get('email')
        password = request.json.get('password')

        if not email or not password:
            return jsonify({"status": "fail", "message": "Email or password missing"}), 400

        cur = mysql.connection.cursor(MySQLdb.cursors.DictCursor)
        cur.execute('SELECT * FROM users WHERE email = %s AND password = %s', (email, password))
        user = cur.fetchone()

        if user:
            return jsonify({"status": "success", "user": user}), 200
        else:
            return jsonify({"status": "fail", "message": "Invalid email or password"}), 401

    except Exception as e:
        print(f"Error in login: {e}")
        return jsonify({"status": "fail", "message": "An error occurred during login"}), 500
    finally:
        if cur:
            cur.close()

def generate_random_string():
    try:
        characters = string.ascii_letters + string.digits
        random_string = ''.join(random.choice(characters) for _ in range(32))
        return random_string
    except Exception as e:
        print(f"Error generating random string: {e}")
        return None

@app.route('/getTmpStr', methods=['GET'])
def get_tmp():
    return jsonify({"tmpCode": tmp_code})

async def modify_tmp_code():
    global tmp_code
    while True:
        tmp_code = generate_random_string()
        print(f"changed tmp_code to {tmp_code}")
        await asyncio.sleep(tmp_code_refresh_time)

async def tmp_code_changer():
    with ThreadPoolExecutor() as executor:
        loop = asyncio.get_running_loop()
        await loop.run_in_executor(executor, asyncio.run, modify_tmp_code())

def run_flask():
    app.run(host="0.0.0.0")

if __name__ == "__main__":
    flask_thread = threading.Thread(target=run_flask)
    flask_thread.start()
    asyncio.run(tmp_code_changer())
