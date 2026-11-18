# Library Management System (Java)

Console-based Library Management System built using core Java and OOP concepts.

## Features
- Admin login (username: `admin`, password: `admin123`)
- Add / remove / view / search books (by title, author, category)
- Register and list members
- Issue and return books (max 3 books per member, 14-day loan period)
- Automatic fine calculation (Rs. 2 per day after due date)
- View issued books, overdue books, and full transaction history
- Data saved automatically to `library.dat` (object serialization)

## Concepts Used
Classes & Objects, Encapsulation, Collections (Map, List), Java Streams & Lambdas,
Serialization (file handling), java.time API, Exception handling, Switch expressions.

## Classes
| File | Purpose |
|------|---------|
| Book.java | Book details and copy tracking |
| Member.java | Member details and fines paid |
| Transaction.java | Issue/return record with fine logic |
| Library.java | Core business logic and persistence |
| Main.java | Menu-driven user interface |

## How to Run (Java 14+)
```
javac *.java
java Main
```
