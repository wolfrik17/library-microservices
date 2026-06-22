# Library Management System - Microservices

## Descrierea proiectului
[Will add short description here]

## Arhitectură
* **user-service:** Manages users and authentication.
* **catalog-service:** Manages books, authors, and categories.
* **lending-service:** Manages borrowing and returning books.

## Setup Instructions
[Will add run instructions here]

## API Documentation
[Will add Postman / Swagger links here]

## Screenshots
[Will add UI screenshots here]

## Contribuții membrii echipei
* Aioanei Cristian-Alexandru - Backend, Infrastructure, and UI.

## ER Diagram (Data Model)
```mermaid
erDiagram
    USER ||--|| USER_PROFILE : "@OneToOne"
    USER ||--o{ LOAN : "@OneToMany"
    BOOK ||--o{ LOAN : "@OneToMany"
    PUBLISHER ||--o{ BOOK : "@OneToMany"
    BOOK }o--o{ CATEGORY : "@ManyToMany"
    BOOK }o--o{ AUTHOR : "@ManyToMany"

    USER {
        Long id
        String username
        String role
    }
    USER_PROFILE {
        Long id
        String email
        String address
    }
    BOOK {
        Long id
        String title
        String isbn
    }
    PUBLISHER {
        Long id
        String name
    }
    CATEGORY {
        Long id
        String name
    }
    AUTHOR {
        Long id
        String name
    }
    LOAN {
        Long id
        LocalDate loanDate
        LocalDate returnDate
    }