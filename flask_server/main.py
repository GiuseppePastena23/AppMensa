from flask import Flask, jsonify, request
from flask_mysqldb import MySQL
import MySQLdb
import random, string
import hashlib

app = Flask(__name__)
app.config['MYSQL_HOST'] = '172.26.173.12'
app.config['MYSQL_USER'] = 'android'
app.config['MYSQL_PASSWORD'] = 'android'
app.config['MYSQL_DB'] = 'mensadb'
mysql = MySQL(app)

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/data/', methods=['GET'])
def get_all_users():
    cur = mysql.connection.cursor()
    cur.execute('SELECT * FROM users')
    rows = cur.fetchall()
    
    if not rows:
        return jsonify({"error": "No data found"}), 404
    
    columns = [desc[0] for desc in cur.description]
    users_list = [dict(zip(columns, row)) for row in rows]
    
    cur.close()
    return jsonify(users_list)

@app.route('/data/<int:id>', methods=['GET'])
def get_data_by_id(id):
    cur = mysql.connection.cursor()
    cur.execute('''SELECT * FROM users WHERE id = %s''', (id,))
    data = cur.fetchall()
    cur.close()
    return jsonify(data)


@app.route('/data/<int:id>', methods=['GET'])
def update_data(id):
    cur = mysql.connection.cursor()
    name = request.json['name']
    age = request.json['age']
    cur.execute('''UPDATE users SET name = %s, age = %s WHERE id = %s''', (name, age, id))
    mysql.connection.commit()
    cur.close()
    return jsonify({'message': 'Data updated successfully'})

@app.route('/login', methods=['POST'])
def login():
    email = request.json.get('email')
    password = request.json.get('password')

    if not email or not password:
        return jsonify({"status": "fail", "message": "Email or password missing"}), 400
    cur = mysql.connection.cursor(MySQLdb.cursors.DictCursor)
    cur.execute('SELECT * FROM users WHERE email = %s', (email,))
    user = cur.fetchone()
    cur.close()

    # Check if the user exists and compare hashed passwords
    if user and user['password'] == password:
        return jsonify({"status": "success", "user": user}), 200
    else:
        return jsonify({"status": "fail", "message": "Invalid email or password"}), 401

@app.route('/qr?id=<int:id>', methods['GET'])
def get_qr(id):
    random_string = generate_code(generate_random_string())
    cur = mysql.connection.cursor()
    cur.execute('''UPDATE users SET tmp_code = %s WHERE id = %s''', (random_string, id))

def generate_random_string():
    characters = string.ascii_letters + string.digits
    random_string = ''.join(random.choice(characters) for _ in range(32))
    return random_string

if __name__ == "__main__":
    app.run(host="0.0.0.0")
