# LibrarySpringBoot

**LibrarySpringBoot** is a web application for managing a library system, built with Spring Boot. It supports user roles (admin, moderator, user) and provides a wide range of features for book management, reservations, and user access control.

## 📚 Features

### 🔐 User Roles

- **Admin**
  - Assign moderators.
  - Demote moderators to regular users.

- **Moderator**
  - Add, edit, and delete books.
  - Confirm book issuance and return.
  - Issue books directly without prior reservation.

- **User**
  - View and search for books.
  - Reserve books for later pickup.
  - Cannot reserve books if overdue reservations exist.

### 📘 Book Management

- **Add Books** (moderators only): Specify title, author, genre, and year.
- **Edit & Delete Books** (moderators only).
- **Book Listing**: All users can view the full list of available books.
- **Book Search**: Search books by title, author, and other filters.

### 🔄 Reservations & Lending

- Users can reserve books in advance.
- Moderators must confirm reservations before the book can be picked up.
- Users with overdue reservations are restricted from making new ones.
- Moderators can issue books directly without prior reservations.

## 🛠️ Technologies

- **Backend**: Java, Spring Boot, Spring MVC, Spring Security, Spring Data JPA
- **Frontend**: HTML, CSS
- **Database**: MySQL
- **Build Tool**: Maven

## 📂 Project Structure

```
LibrarySpringBoot/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── library/
│   │   │               ├── controller/
│   │   │               ├── model/
│   │   │               ├── repository/
│   │   │               ├── service/
│   │   │               └── LibrarySpringBootApplication.java
│   │   └── resources/
│   │       ├── templates/
│   │       ├── static/
│   │       └── application.properties
├── pom.xml
└── README.md
```

## 🔒 Security

- Spring Security ensures role-based access control.
- Admin, moderator, and user roles have distinct permissions for managing books and users.

## 📝 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.