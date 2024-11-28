from flask import Flask, render_template, request, redirect, url_for, flash
from models import db, User, Child, Session, Attendance
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///charity_club.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SECRET_KEY'] = 'your_secret_key'  # Used for flash messages and session management
db.init_app(app)

# Sample Data Creation Route (for testing)
@app.route('/init')
def init_db():
    db.create_all()
    return "Database Initialized!"

@app.route('/')
def home():
    return render_template('home.html')

@app.route('/sessions')
def sessions():
    all_sessions = Session.query.all()
    return render_template('sessions.html', sessions=all_sessions)

@app.route('/children')
def children():
    children_data = Child.query.all()
    return render_template('children.html', children=children_data)

@app.route('/add_child', methods=['GET', 'POST'])
def add_child():
    if request.method == 'POST':
        name = request.form['name']
        age = request.form['age']
        guardian_name = request.form['guardian_name']
        new_child = Child(name=name, age=age, guardian_name=guardian_name)
        db.session.add(new_child)
        db.session.commit()
        flash("Child added successfully!", "success")
        return redirect(url_for('children'))
    return render_template('add_child.html')

@app.route('/delete_child/<int:child_id>')
def delete_child(child_id):
    child = Child.query.get_or_404(child_id)
    db.session.delete(child)
    db.session.commit()
    flash("Child removed successfully!", "success")
    return redirect(url_for('children'))

if __name__ == '__main__':
    with app.app_context():
        db.create_all()
    app.run(debug=True)
