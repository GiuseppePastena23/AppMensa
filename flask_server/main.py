from flask import Flask, jsonify, request, render_template
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.exc import SQLAlchemyError
import random
import string
import asyncio
import threading

# Configurazione dell'app Flask
app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql://android:android@localhost/mensadb'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

# Inizializzazione di SQLAlchemy
db = SQLAlchemy(app)

# Definizione dei modelli ORM
class User(db.Model):
    __tablename__ = 'users'
    id = db.Column(db.Integer, primary_key=True)
    nome = db.Column(db.String(50))
    cognome = db.Column(db.String(50))
    email = db.Column(db.String(100), unique=True, nullable=False)
    password = db.Column(db.String(255), nullable=False)
    telefono = db.Column(db.String(15))
    credito = db.Column(db.Float, default=0.0)
    status = db.Column(db.String(50))

class Transazione(db.Model):
    __tablename__ = 'transazioni'
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'), nullable=False)
    importo = db.Column(db.Float, nullable=False)
    modalita = db.Column(db.String(50))

@app.route('/admin')
def admin():
    return render_template('admin.html')

# Endpoint per ottenere tutti gli utenti
@app.route('/data/', methods=['GET'])
def get_all_users():
    try:
        users = User.query.limit(10).all();
        return jsonify([{
            'id': user.id,
            'nome': user.nome,
            'cognome': user.cognome,
            'email': user.email,
            'telefono': user.telefono,
            'credito': user.credito,
            'status': user.status
        } for user in users])
    except SQLAlchemyError as e:
        return jsonify({"error": str(e)}), 500

# Endpoint per ottenere le transazioni di un utente
@app.route('/transactions/<int:id>', methods=["GET"])
def get_transactions(id):
    try:
        transactions = Transazione.query.filter_by(user_id=id).all()
        if not transactions:
            return jsonify({"error": "No transactions found"}), 404
        return jsonify([{
            'id': tx.id,
            'user_id': tx.user_id,
            'importo': tx.importo,
            'modalita': tx.modalita
        } for tx in transactions])
    except SQLAlchemyError as e:
        return jsonify({"error": str(e)}), 500

# Endpoint per aggiungere una transazione
@app.route('/newTransaction', methods=['POST'])
def add_credit():
    try:
        user_id = request.json.get('id')
        value = request.json.get('value')
        

        if not user_id or not value:
            return jsonify({"error": "User ID or value is missing"}), 400

        if int(value) < 0:
            modalita = "pagamento alla mensa"
        else:
            modalita = "ricarica con carta di credito"



        transaction = Transazione(user_id=user_id, importo=value, modalita=modalita)
        user = User.query.get(user_id)

        if user:
            user.credito += float(value)
            db.session.add(transaction)
            db.session.commit()
            return jsonify({"message": "Credit updated successfully"}), 200
        else:
            return jsonify({"error": "User not found"}), 404

    except SQLAlchemyError as e:
        return jsonify({"error": str(e)}), 500

# Endpoint per il login
@app.route('/login', methods=['POST'])
def login():
    try:
        email = request.json.get('email')
        password = request.json.get('password')

        if not email or not password:
            return jsonify({"error": "Email or password missing"}), 400

        user = User.query.filter_by(email=email, password=password).first()
        if user:
            return jsonify({
                "id": user.id,
                "nome": user.nome,
                "email": user.email,
                "status": user.status
            }), 200
        else:
            return jsonify({"error": "Invalid email or password"}), 401

    except SQLAlchemyError as e:
        return jsonify({"error": str(e)}), 500

# Generazione di un codice temporaneo
tmp_code = "not yet generated"
tmp_code_refresh_time = 30

def generate_random_string():
    return ''.join(random.choice(string.ascii_letters + string.digits) for _ in range(32))




def run_flask():
    app.run(host="0.0.0.0")

if __name__ == "__main__":
    # Creare le tabelle se non esistono
    with app.app_context():
        db.create_all()
    # Iniziare i thread
    flask_thread = threading.Thread(target=run_flask)
    flask_thread.start()
