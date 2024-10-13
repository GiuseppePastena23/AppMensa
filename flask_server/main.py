from flask import Flask, jsonify, request
from flask_mysqldb import MySQL
import MySQLdb
import random, string
import hashlib
import pandas as pd


app = Flask(__name__)
app.config['MYSQL_HOST'] = '172.26.173.12'
app.config['MYSQL_USER'] = 'android'
app.config['MYSQL_PASSWORD'] = 'android'
app.config['MYSQL_DB'] = 'mensadb'
mysql = MySQL(app)

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/transactions/<int:id>', methods=["GET"])
def get_transactions(id):
    try:
        cur = mysql.connection.cursor()
        cur.execute('''SELECT * FROM transazioni WHERE user_id = %s''', (id,))

        rows = cur.fetchall()
        if not rows:
            return jsonify({"error": "No data found"}), 404

        columns = [desc[0] for desc in cur.description]
        transactions_list = [dict(zip(columns, row)) for row in rows]

        cur.close()
        return jsonify(transactions_list)
    except Exception as e:
        print(f"Error fetching transactions: {e}")
        return jsonify({"status": "fail", "message": "Error fetching data"}), 500
    finally:
        if cur:
            cur.close() # TODO: add this line to all others functions



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


# TODO: add mode
def create_new_transaction(user_id, value):
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

@app.route('/data/', methods=['GET'])
def get_all_users():
    try:
        cur = mysql.connection.cursor()
        cur.execute('SELECT * FROM users')
        rows = cur.fetchall()

        if not rows:
            return jsonify({"error": "No data found"}), 404

        columns = [desc[0] for desc in cur.description]
        users_list = [dict(zip(columns, row)) for row in rows]

        cur.close()
        return jsonify(users_list)
    except Exception as e:
        print(f"Error fetching all users: {e}")
        return jsonify({"status": "fail", "message": "Error fetching data"}), 500


@app.route('/data/<int:id>', methods=['GET'])
def get_data_by_id(id):
    try:
        cur = mysql.connection.cursor()
        cur.execute('''SELECT * FROM users WHERE id = %s''', (id,))
        data = cur.fetchall()
        cur.close()

        if not data:
            return jsonify({"error": "No user found with that ID"}), 404

        return jsonify(data)
    except Exception as e:
        print(f"Error fetching data by ID: {e}")
        return jsonify({"status": "fail", "message": "Error fetching data"}), 500


@app.route('/login', methods=['POST'])
def login():
    try:
        email = request.json.get('email')
        password = request.json.get('password')

        # Validate input
        if not email or not password:
            return jsonify({"status": "fail", "message": "Email or password missing"}), 400

        # Update temporary code
        update_tmp_code(email)

        # Check for user credentials
        cur = mysql.connection.cursor(MySQLdb.cursors.DictCursor)
        cur.execute('SELECT * FROM users WHERE email = %s AND password = %s', (email, password))
        user = cur.fetchone()
        cur.close()

        if user:
            return jsonify({"status": "success", "user": user}), 200
        else:
            return jsonify({"status": "fail", "message": "Invalid email or password"}), 401

    except Exception as e:
        print(f"Error in login: {e}")
        return jsonify({"status": "fail", "message": "An error occurred during login"}), 500


def update_tmp_code(email):
    try:
        random_string = generate_random_string()
        cur = mysql.connection.cursor()
        cur.execute('''UPDATE users SET tmp_code = %s WHERE email = %s''', (random_string, email))
        mysql.connection.commit()
        cur.close()
    except Exception as e:
        print(f"Error updating temporary code: {e}")


def generate_random_string():
    try:
        characters = string.ascii_letters + string.digits
        random_string = ''.join(random.choice(characters) for _ in range(32))
        return random_string
    except Exception as e:
        print(f"Error generating random string: {e}")
        return None


"""def insert_data():
    try:
        # Connect to the database
        cur = mysql.connection.cursor()

        # Read the CSV file into a DataFrame
        df = pd.read_csv("generated_users.csv")

        # Prepare SQL insert statement
        sql = '''INSERT INTO users (cf, nome, cognome, email, password, status, telefono, credito) 
                 VALUES (%s, %s, %s, %s, %s, %s, %s, %s)'''

        # Iterate over the DataFrame and insert each row
        for index, row in df.iterrows():
            # Ensure to convert the DataFrame row into a tuple
            cur.execute(sql, tuple(row))

        # Commit the transaction
        mysql.connection.commit()
        print(f"{cur.rowcount} records inserted successfully into users table.")
    except Exception as e:
        print(f"Error during data insertion: {e}")
    finally:
        cur.close()
        print("MySQL cursor is closed.")

@app.route('/insert_users', methods=['GET'])
def insert_users():
    insert_data()  # You can specify how many records to insert
    return "Data insertion completed.", 200"""

    
if __name__ == "__main__":
    app.run(host="0.0.0.0")
    
