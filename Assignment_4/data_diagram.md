```mermaid
erDiagram
    HOMEPAGE {
        int id 
        string title
        string theme_color
    }
    SESSION {
        int id 
        string name
        datetime date
        int age_range
        string disability
    }
    CHILD {
        int id 
        string name
        int age
        string guardian_contact
        string disability
    }
    SESSION_CHILD {
        int id
        int session_id 
        int child_id 
    }

    %% Relationships
    HOMEPAGE ||--o{ SESSION : "links to sessions"
    SESSION ||--o{ SESSION_CHILD : "has children"
    CHILD ||--o{ SESSION_CHILD : "attends sessions"
