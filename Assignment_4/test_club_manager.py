# to run, run python -m unittest test_club_manager.py

import unittest
import urllib.parse

class ClubHandler:
    def __init__(self):
        self.sessions = [
            {"id": 1, "name": "SAMS", "date": "2025-01-26", "age_range": "0-6", "disability": "NA", "children": []},
            {"id": 2, "name": "Starships", "date": "2025-02-12", "age_range": "6-12", "disability": "NA", "children": []}
        ]

    def handle_add_session(self, session_name, session_date, age_range, disability, notes):
        if session_name and session_date:
            new_session = {
                "id": len(self.sessions) + 1,
                "name": session_name,
                "date": session_date,
                "age_range": age_range,
                "disability": disability,
                "notes": notes,
                "children": []
            }
            self.sessions.append(new_session)
            return new_session
        else:
            return None

    def remove_session(self, session_id):
        self.sessions = [s for s in self.sessions if s["id"] != session_id]
        
    def handle_add_child(self, session_id, child_name, child_age, child_disability, child_guardian):
        session = next((s for s in self.sessions if s["id"] == session_id), None)
        if session and child_name:
            session["children"].append({
                "name": child_name,
                "age": child_age,
                "disability": child_disability,
                "guardian": child_guardian
            })
            return True
        return False

    def remove_child(self, session_id, child_index):
        session = next((s for s in self.sessions if s["id"] == session_id), None)
        if session and 0 <= child_index < len(session["children"]):
            session["children"].pop(child_index)
            return True
        return False

    def filter_sessions(self, filters):
        filtered_sessions = self.sessions
        if "name" in filters:
            filtered_sessions = [s for s in filtered_sessions if filters["name"].lower() in s["name"].lower()]
        if "age_range" in filters:
            filtered_sessions = [s for s in filtered_sessions if filters["age_range"] in s["age_range"]]
        if "disability" in filters:
            filtered_sessions = [s for s in filtered_sessions if filters["disability"].lower() in s["disability"].lower()]
        return filtered_sessions

class TestClubHandler(unittest.TestCase):

    def setUp(self):
        self.club_handler = ClubHandler()

    def test_add_session(self):
        new_session = self.club_handler.handle_add_session("New Session", "2025-03-01", "12-18", "NA", "Some notes")
        self.assertIsNotNone(new_session)
        self.assertEqual(len(self.club_handler.sessions), 3)

    def test_remove_session(self):
        self.club_handler.remove_session(1)
        self.assertEqual(len(self.club_handler.sessions), 1)

    def test_add_child(self):
        session_id = 1
        success = self.club_handler.handle_add_child(session_id, "Child Name", "5", "None", "Guardian Name")
        session = next((s for s in self.club_handler.sessions if s["id"] == session_id), None)
        self.assertTrue(success)
        self.assertEqual(len(session["children"]), 1)

    def test_remove_child(self):
        session_id = 1
        self.club_handler.handle_add_child(session_id, "Child Name", "5", "None", "Guardian Name")
        success = self.club_handler.remove_child(session_id, 0)  # Removing the child at index 0
        session = next((s for s in self.club_handler.sessions if s["id"] == session_id), None)
        self.assertTrue(success)
        self.assertEqual(len(session["children"]), 0)  # The first session should have no children now

    def test_filter_sessions(self):
        filters = {"age_range": "6-12"}
        filtered_sessions = self.club_handler.filter_sessions(filters)   
        self.assertEqual(len(filtered_sessions), 1)
        self.assertEqual(filtered_sessions[0]["name"], "Starships")

if __name__ == "__main__":
    unittest.main()
