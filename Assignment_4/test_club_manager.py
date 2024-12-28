# to run, run python -m unittest test_club_manager.py

import unittest
from http.server import HTTPServer
import threading
import http.client
import json
from urllib.parse import urlencode
from io import BytesIO
import club_manager.py

class TestClubHandler(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.server = HTTPServer(('localhost', 8000), club_handler_module.ClubHandler)
        cls.thread = threading.Thread(target=cls.server.serve_forever)
        cls.thread.daemon = True
        cls.thread.start()

    @classmethod
    def tearDownClass(cls):
        cls.server.shutdown()
        cls.thread.join()

    def make_request(self, method, path, body=None, headers={}):
        conn = http.client.HTTPConnection('localhost', 8000)
        conn.request(method, path, body, headers)
        response = conn.getresponse()
        return response.status, response.read().decode('utf-8')

    def test_home_page(self):
        status, body = self.make_request("GET", "/")
        self.assertEqual(status, 200)
        self.assertIn("Sessions", body)

    def test_view_sessions(self):
        status, body = self.make_request("GET", "/view_sessions")
        self.assertEqual(status, 200)
        self.assertIn("SAMS", body)
        self.assertIn("Starships", body)

    def test_add_session(self):
        new_session = {
            'session_name': 'New Session',
            'session_date': '2025-03-01',
            'age_range': '12-18',
            'disability': 'None',
            'notes': 'A new teen session'
        }
        body = urlencode(new_session)
        headers = {'Content-Type': 'application/x-www-form-urlencoded'}
        status, _ = self.make_request("POST", "/add_session", body, headers)

        self.assertEqual(status, 302)

        status, body = self.make_request("GET", "/view_sessions")
        self.assertEqual(status, 200)
        self.assertIn("New Session", body)

    def test_manage_children(self):
        session_id = 1
        new_child = {
            'child_name': 'Amy',
            'child_age': '6',
            'child_disability': 'Autism',
            'child_guardian': 'James'
        }
        body = urlencode(new_child)
        headers = {'Content-Type': 'application/x-www-form-urlencoded'}
        status, _ = self.make_request("POST", f"/manage_children/{session_id}", body, headers)

        self.assertEqual(status, 302)

        status, body = self.make_request("GET", f"/manage_children/{session_id}")
        self.assertEqual(status, 200)
        self.assertIn("Amy", body)

    def test_remove_session(self):
        session_id = 2
        status, _ = self.make_request("GET", f"/remove_session/{session_id}")
        self.assertEqual(status, 302)

        status, body = self.make_request("GET", "/view_sessions")
        self.assertEqual(status, 200)
        self.assertNotIn("Starships", body)

    def test_remove_child(self):
        session_id = 1 
        new_child = {
            'child_name': 'Josh',
            'child_age': '12',
            'child_disability': 'Blind',
            'child_guardian': 'Sarah'
        }
        body = urlencode(new_child)
        headers = {'Content-Type': 'application/x-www-form-urlencoded'}
        self.make_request("POST", f"/manage_children/{session_id}", body, headers)

        child_index = 0
        status, _ = self.make_request("GET", f"/remove_child/{session_id}/{child_index}")
        self.assertEqual(status, 302)

        status, body = self.make_request("GET", f"/manage_children/{session_id}")
        self.assertEqual(status, 200)
        self.assertNotIn("Josh", body)

if __name__ == "__main__":
    unittest.main()
