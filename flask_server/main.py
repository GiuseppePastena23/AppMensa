from flask import Flask, jsonify
from flask_mysqldb import MySQL
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

    hashed_password = hashlib.sha256(password.encode()).hexdigest()

    cur = mysql.connection.cursor()
    cur.execute('SELECT * FROM users WHERE email = %s', (email,))
    user = cur.fetchone()
    cur.close()

    if user and user['password'] == hashed_password:
        return jsonify({"status": "success", "user": user}), 200
    else:
        return jsonify({"status": "fail", "message": "Invalid email or password"}), 401

if __name__ == "__main__":
    app.run(host="0.0.0.0")
