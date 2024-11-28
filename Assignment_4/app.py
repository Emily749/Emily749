from flask import Flask
from models import db, User, Child, Session, Attendance, Note

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///charity_club.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

# Initialize the database after the app is created
db.init_app(app)

@app.route('/')
def home():
    return "Hello, Charity Club Manager!"

if __name__ == '__main__':
    with app.app_context():
        db.create_all()  # Create tables if they don't exist
    app.run(debug=True)
    
@login_manager.user_loader
def load_user(user_id):
    return User.query.get(int(user_id))

@app.route('/')
def home():
    return render_template('home.html')

@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']
        user = User.query.filter_by(username=username).first()
        if user and bcrypt.check_password_hash(user.password_hash, password):
            login_user(user)
            flash('Login successful!', 'success')
            return redirect(url_for('dashboard'))
        else:
            flash('Invalid username or password', 'danger')
    return render_template('login.html')

@app.route('/logout')
@login_required
def logout():
    logout_user()
    flash('You have been logged out.', 'info')
    return redirect(url_for('login'))

@app.route('/dashboard')
@login_required
def dashboard():
    sessions = Session.query.all()
    return render_template('dashboard.html', sessions=sessions)

@app.route('/add_attendee/<int:session_id>', methods=['GET', 'POST'])
@login_required
def add_attendee(session_id):
    if request.method == 'POST':
        child_id = request.form['child_id']
        child = Child.query.get(child_id)
        session = Session.query.get(session_id)
        if not Attendance.query.filter_by(child_id=child_id, session_id=session_id).first():
            attendance = Attendance(child_id=child.id, session_id=session.id)
            db.session.add(attendance)
            db.session.commit()
            flash(f'{child.name} has been added to the session.', 'success')
        else:
            flash('Child already added to this session.', 'info')
    children = Child.query.all()
    return render_template('add_attendee.html', session_id=session_id, children=children)

@app.route('/view_session/<int:session_id>')
@login_required
def view_session(session_id):
    session = Session.query.get(session_id)
    attendees = Attendance.query.filter_by(session_id=session_id).all()
    return render_template('view_session.html', session=session, attendees=attendees)

@app.route('/export_csv')
@login_required
def export_csv():
    children = Child.query.all()
    data = [
        {
            'Name': child.name,
            'Age': child.age,
            'Guardian Contact': child.guardian_contact,
            'Special Requirements': child.special_requirements
        } for child in children
    ]
    df = pd.DataFrame(data)
    csv_path = 'data/children_export.csv'
    df.to_csv(csv_path, index=False)
    return send_file(csv_path, as_attachment=True)

if __name__ == '__main__':
    db.create_all()
    app.run(debug=True)
