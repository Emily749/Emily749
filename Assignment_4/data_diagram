```mermaid
erDiagram
    USER {
        int id PK
        string username
        string password
        string role
    }
    CHILD {
        int id PK
        string name
        int age
        string guardian_contact
        string special_requirements
        boolean is_on_waitlist
    }
    SESSION {
        int id PK
        string name
        datetime start_time
        datetime end_time
        string location
    }
    ATTENDANCE {
        int id PK
        int session_id FK
        int child_id FK
    }
    TIMETABLE {
        int id PK
        datetime date
        string description
    }
    NOTE {
        int id PK
        int session_id FK
        text content
        datetime created_at
    }

    %% Relationships
    USER ||--o{ SESSION : "creates or manages"
    CHILD ||--o{ ATTENDANCE : "is added to"
    SESSION ||--o{ ATTENDANCE : "has attendees"
    TIMETABLE ||--|| SESSION : "maps to"
    SESSION ||--o{ NOTE : "has notes"
