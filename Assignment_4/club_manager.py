import json
from http.server import SimpleHTTPRequestHandler, HTTPServer
import urllib.parse
from datetime import datetime

sessions = [
    {"id": 1, "name": "SAMS", "date": "2025-01-26", "age_range": "0-6", "disability": "NA", "children": []},
    {"id": 2, "name": "Starships", "date": "2025-02-12", "age_range": "6-12", "disability": "NA", "children": []}
]

class ClubHandler(SimpleHTTPRequestHandler):
    def do_GET(self):
        if self.path == "/":
            self.serve_home()
        elif self.path.startswith("/view_sessions"):
            self.serve_view_sessions()
        elif self.path == "/add_session":
            self.serve_add_session()
        elif self.path.startswith("/manage_children/"):
            session_id = self.path.split("/")[2]
            self.serve_manage_children(session_id)
        elif self.path.startswith("/remove_session/"):
            session_id = self.path.split("/")[2]
            self.remove_session(session_id)
        elif self.path.startswith("/remove_child/"):
            session_id, child_index = self.path.split("/")[2:4]
            self.remove_child(session_id, child_index)
        elif self.path.startswith("/static/"):
            super().do_GET()
        else:
            self.send_error(404, "Not Found")

    def do_POST(self):
        if self.path == "/add_session":
            self.handle_add_session()
        elif self.path.startswith("/manage_children/"):
            session_id = self.path.split("/")[2]
            self.handle_add_child(session_id)
        else:
            self.send_error(404, "Not Found")

    def serve_home(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
        with open("templates/home.html", "r") as f:
            home_html = f.read()
        self.wfile.write(home_html.encode())

    def serve_view_sessions(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
    
        session_html = "<h2>Sessions</h2>"
        session_html += """
        <form method="GET" action="/view_sessions">
            <label for="filter_name">Session Name:</label>
            <input type="text" id="filter_name" name="name">
            <label for="filter_age">Age Range:</label>
            <input type="text" id="filter_age" name="age_range">
            <label for="filter_disability">Disability:</label>
            <input type="text" id="filter_disability" name="disability">
            <button type="submit">Filter</button>
        </form>
        """
    
        query_string = urllib.parse.urlparse(self.path).query
        filters = urllib.parse.parse_qs(query_string)
    
        filtered_sessions = sessions
        if "name" in filters:
            filtered_sessions = [s for s in filtered_sessions if filters["name"][0].lower() in s["name"].lower()]
        if "age_range" in filters:
            filtered_sessions = [s for s in filtered_sessions if filters["age_range"][0] in s["age_range"]]
        if "disability" in filters:
            filtered_sessions = [s for s in filtered_sessions if filters["disability"][0].lower() in s["disability"].lower()]
    
        for session in filtered_sessions:
            session_html += f"""
            <div>
                <b>{session["name"]}</b> on {session["date"]} (Age: {session["age_range"]}, Disability: {session["disability"]})
                <p><strong>Notes:</strong> {session.get("notes", "No notes available")}</p>
                <a href="/manage_children/{session['id']}"><button>Manage Children</button></a>
                <a href="/remove_session/{session['id']}"><button>Remove Session</button></a>
            </div>
            """
    
        session_html += '<a href="/"><button>Back to Home</button></a>'
        self.wfile.write(session_html.encode())

    def serve_add_session(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
        session_html = """
        <h2>Add a New Session</h2>
        <form action="/add_session" method="POST">
            <label for="session_name">Session Name:</label>
            <input type="text" id="session_name" name="session_name" required><br><br>
            <label for="session_date">Session Date:</label>
            <input type="date" id="session_date" name="session_date" required><br><br>
            <label for="age_range">Age Range:</label>
            <input type="text" id="age_range" name="age_range" required><br><br>
            <label for="disability">Disability:</label>
            <input type="text" id="disability" name="disability"><br><br>
            <label for="notes">Notes:</label>
            <input type="text" id="notes" name="notes"><br><br>
            <button type="submit">Add Session</button>
        </form>
        <a href="/"><button>Back to Home</button></a>
        """
        self.wfile.write(session_html.encode())

    def handle_add_session(self):
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        form_data = urllib.parse.parse_qs(post_data.decode('utf-8'))

        session_name = form_data.get('session_name', [''])[0]
        session_date = form_data.get('session_date', [''])[0]
        age_range = form_data.get('age_range', [''])[0]
        disability = form_data.get('disability', [''])[0]
        notes = form_data.get('notes', [''])[0]

        if session_name and session_date:
            new_session = {
                "id": len(sessions) + 1,
                "name": session_name,
                "date": session_date,
                "age_range": age_range,
                "disability": disability,
                "notes": notes,
                "children": []
            }
            sessions.append(new_session)
            self.send_response(302)
            self.send_header('Location', '/view_sessions')
            self.end_headers()
        else:
            self.send_error(400, "Invalid input")

    def serve_manage_children(self, session_id):
        session = next((s for s in sessions if s["id"] == int(session_id)), None)
        if session:
            self.send_response(200)
            self.send_header('Content-type', 'text/html')
            self.end_headers()

            children_html = f"""
            <h2>Manage Children for {session["name"]}</h2>
            <form action="/manage_children/{session["id"]}" method="POST">
                <label for="child_name">Child Name:</label>
                <input type="text" id="child_name" name="child_name" required><br><br>
                <label for="child_age">Child Age:</label>
                <input type="text" id="child_age" name="child_age" required><br><br>
                <label for="child_disability">Child Disability:</label>
                <input type="text" id="child_disability" name="child_disability"><br><br>
                <label for="child_guardian">Child Guardian:</label>
                <input type="text" id="child_guardian" name="child_guardian"><br><br>
                <button type="submit">Add Child</button>
            </form>
            <h3>Children:</h3>
            <ul>
            """
            for index, child in enumerate(session["children"]):
                children_html += f"""
                <li>
                    {child['name']} (Age: {child['age']}, Disability: {child.get('disability', 'N/A')}, Guardian: {child['guardian']})
                    <a href="/remove_child/{session['id']}/{index}"><button>Remove</button></a>
                </li>
                """
            children_html += "</ul><a href='/view_sessions'><button>Back</button></a>"
            self.wfile.write(children_html.encode())
        else:
            self.send_error(404, "Session not found")

    def handle_add_child(self, session_id):
        content_length = int(self.headers['Content-Length'])
        post_data = self.rfile.read(content_length)
        form_data = urllib.parse.parse_qs(post_data.decode('utf-8'))

        child_name = form_data.get('child_name', [''])[0]
        child_age = form_data.get('child_age', [''])[0]
        child_disability = form_data.get('child_disability', [''])[0]
        child_guardian = form_data.get('child_guardian', [''])[0]

        session = next((s for s in sessions if s["id"] == int(session_id)), None)
        if session and child_name:
            session["children"].append({
                "name": child_name,
                "age": child_age,
                "disability": child_disability,
                "guardian": child_guardian
            })
            self.send_response(302)
            self.send_header('Location', f'/manage_children/{session_id}')
            self.end_headers()
        else:
            self.send_error(400, "Invalid input")

    def remove_child(self, session_id, child_index):
        session = next((s for s in sessions if s["id"] == int(session_id)), None)
        if session and 0 <= int(child_index) < len(session["children"]):
            session["children"].pop(int(child_index))
            self.send_response(302)
            self.send_header('Location', f'/manage_children/{session_id}')
            self.end_headers()
        else:
            self.send_error(404, "Child not found")

    def remove_session(self, session_id):
        global sessions
        sessions = [s for s in sessions if s["id"] != int(session_id)]
        self.send_response(302)
        self.send_header('Location', '/view_sessions')
        self.end_headers()


def run(server_class=HTTPServer, handler_class=ClubHandler, port=8000):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print(f'Starting server on port {port}')
    httpd.serve_forever()


if __name__ == "__main__":
    run()
