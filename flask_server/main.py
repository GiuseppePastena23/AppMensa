from flask import Flask, jsonify
from flask_mysqldb import MySQL

app = Flask(__name__)
app.config['MYSQL_HOST'] = '172.26.173.12'
app.config['MYSQL_USER'] = 'android'
app.config['MYSQL_PASSWORD'] = 'android'
app.config['MYSQL_DB'] = 'mensadb'
mysql = MySQL(app)

@app.route('/')
def hello_world():
    return 'Hello, World!'

@app.route('/data', methods=['GET'])
def get_data():
    cur = mysql.connection.cursor()
    cur.execute('''SELECT * FROM users''')
    data = cur.fetchall()
    cur.close()
    return jsonify(data)

@app.route('/data/<int:id>', methods=['GET'])
def get_data_by_id(id):
    cur = mysql.connection.cursor()
    cur.execute('''SELECT * FROM users WHERE id = %s''', (id,))
    data = cur.fetchall()
    cur.close()
    return jsonify(data)

@app.route('/data', methods=['GET'])
def add_data():
    cur = mysql.connection.cursor()
    name = request.json['name']
    age = request.json['age']
    cur.execute('''INSERT INTO users (name, age) VALUES (%s, %s)''', (name, age))
    mysql.connection.commit()
    cur.close()
    return jsonify({'message': 'Data added successfully'})

@app.route('/data/<int:id>', methods=['GET'])
def update_data(id):
    cur = mysql.connection.cursor()
    name = request.json['name']
    age = request.json['age']
    cur.execute('''UPDATE users SET name = %s, age = %s WHERE id = %s''', (name, age, id))
    mysql.connection.commit()
    cur.close()
    return jsonify({'message': 'Data updated successfully'})