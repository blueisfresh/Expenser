# Expenser API

This is my Expenser API, built using Spring Boot. This project focuses on providing a secure and robust backend for tracking personal expenses.

---

## 🚀 Project Overview

This API provides endpoints for managing user authentication, categories, and expenses. It demonstrates a secure RESTful service using Spring Boot, Spring Data JPA, and a SQLite database, with JWT-based authentication.

**Key Features:**
*   **User Authentication & Authorization:**
    *   Secure user registration with password hashing (BCrypt) and complexity validation.
    *   User sign-in using JWT (JSON Web Tokens) for stateless authentication.
    *   Protected API endpoints accessible only with a valid JWT.
    *   User-specific data access (users can only manage their own expenses).
*   **Categories Management:**
    *   Pre-defined and global expense categories.
    *   CRUD (Create, Read, Update, Delete) operations for categories.
*   **Expense Tracking:**
    *   CRUD operations for individual expenses.
    *   Expenses are linked to users and categories.
    *   Filtering expenses by category and searching by title.
    *   Automatic `createdAt` and `updatedAt` timestamps for all entities.
*   **API Design:**
    *   Uses DTOs for clear API contracts and separation of concerns.
    *   Comprehensive data validation using Jakarta Bean Validation.
    *   Global exception handling for consistent error responses (e.g., 400 Bad Request for validation errors, 401 Unauthorized, 403 Forbidden, 404 Not Found).

**Technologies Used:**
*   **Java 17**
*   **Spring Boot 3.5.3**
    *   Spring Web (RESTful API)
    *   Spring Data JPA (Database access)
    *   Spring Validation (Request body validation)
    *   Spring Security (Authentication & Authorization)
*   **SQLite** (for easy local setup)
*   **Lombok** (for boilerplate code reduction)
*   **JJWT (Java JWT)** (for JSON Web Token creation and validation)

---

## ▶️ How to Run the Project Locally

Follow these steps to get the Expenser API up and running on your local machine.

### Prerequisites

Before you begin, ensure you have the following installed:
*   **Java Development Kit (JDK) 17 or higher**
*   **Apache Maven**
*   A Java IDE like **IntelliJ IDEA**, **Eclipse**, or **VS Code with Java Extensions** (recommended)

### Important Database Note for Local Testing:

For convenience during local development and to ensure repeatable tests, the `src/main/resources/application.properties` file is configured with `spring.jpa.hibernate.ddl-auto=update`. This setting will **automatically update or create the database schema** on application startup, and **not drop data**. This allows you to retain users and categories between runs.

*   **For production/persistent data:** This setting is **not recommended** as direct `update` can lead to issues with complex schema changes over time. For production, you would typically set this to `none` and use a schema migration tool like Flyway/Liquibase.

### Steps

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/blueisfresh/Expenser.git
    cd Expenser
    ```

2.  **Build the Project:**
    Open the project in your IDE. Your IDE should automatically detect the Maven project and download dependencies.
    Alternatively, using the command line:
    ```bash
    mvn clean install
    ```

3.  **Run the Application:**
    *   **From your IDE:** Locate the `ExpenserApplication.java` file (e.g., in `src/main/java/com/blueisfresh/expenser/ExpenserApplication.java`) and run its `main` method.
    *   **From the command line (after building):**
        ```bash
        java -jar target/Expenser-0.0.1-SNAPSHOT.jar # Adjust filename if necessary
        ```

4.  **Access the API:**
    The application will start on `http://localhost:8080` by default.

    *   **API Base URL:** `http://localhost:8080/api/`

### Initial Data Setup:
Before creating expenses, it's recommended to pre-populate some categories. You can do this by directly inserting data into the `tbl_category` table in your `expenser.db` SQLite database using a tool like [DB Browser for SQLite](https://sqlitebrowser.org/):

```sql
INSERT INTO tbl_category (created_at, name, updated_at, user_id) VALUES (STRFTIME('%s','now') * 1000, 'Groceries', STRFTIME('%s','now') * 1000, NULL);
INSERT INTO tbl_category (created_at, name, updated_at, user_id) VALUES (STRFTIME('%s','now') * 1000, 'Leisure', STRFTIME('%s','now') * 1000, NULL);
INSERT INTO tbl_category (created_at, name, updated_at, user_id) VALUES (STRFTIME('%s','now') * 1000, 'Electronics', STRFTIME('%s','now') * 1000, NULL);
INSERT INTO tbl_category (created_at, name, updated_at, user_id) VALUES (STRFTIME('%s','now') * 1000, 'Utilities', STRFTIME('%s','now') * 1000, NULL);
INSERT INTO tbl_category (created_at, name, updated_at, user_id) VALUES (STRFTIME('%s','now') * 1000, 'Clothing', STRFTIME('%s','now') * 1000, NULL);
INSERT INTO tbl_category (created_at, name, updated_at, user_id) VALUES (STRFTIME('%s','now') * 1000, 'Health', STRFTIME('%s','now') * 1000, NULL);
INSERT INTO tbl_category (created_at, name, updated_at, user_id) VALUES (STRFTIME('%s','now') * 1000, 'Others', STRFTIME('%s','now') * 1000, NULL);
```

---

## 📚 API Endpoints

This API is designed with JWT authentication. Endpoints requiring authentication will need a valid JWT in the `Authorization: Bearer <TOKEN>` header.

### Authentication Endpoints (`/api/auth`)

| Method | Endpoint        | Description                                  | Authentication | Request Body          | Response Body      |
| :----- | :-------------- | :------------------------------------------- | :------------- | :-------------------- | :----------------- |
| `POST` | `/signup`       | Register a new user with password hashing    | None           | `UserSignupDto`       | Success/Error Message (String/JSON) |
| `POST` | `/signin`       | Authenticate user and get JWT token          | None           | `UserSigninDto`       | JWT Token (String) |
| `GET`  | `/me/profile`   | Get authenticated user's profile information | JWT Required   | None                  | `UserProfileDto`   |

### Category Endpoints (`/api/category`)

| Method | Endpoint        | Description                         | Authentication | Request Body          | Response Body           |
| :----- | :-------------- | :---------------------------------- | :------------- | :-------------------- | :---------------------- |
| `GET`  | `/`             | Retrieve all categories (global/user) | JWT Required (if configured) | None | `List<Category>` |
| `GET`  | `/search?name={name}` | Search categories by name           | JWT Required (if configured) | None | `List<Category>` |
| `POST` | `/`             | Create a new category               | JWT Required (if configured) | `CategoryCreateDto` | `Category`              |
| `PUT`  | `/{id}`         | Update an existing category by ID   | JWT Required (if configured) | `CategoryCreateDto` | `Category`              |
| `DELETE`|`/{id}`         | Delete a category by ID             | JWT Required (if configured) | None | No Content (204)        |

### Expense Endpoints (`/api/expense`)

| Method | Endpoint              | Description                                   | Authentication | Request Body          | Response Body           |
| :----- | :-------------------- | :-------------------------------------------- | :------------- | :-------------------- | :---------------------- |
| `GET`  | `/`                   | Retrieve all expenses for the authenticated user | JWT Required   | None                  | `List<Expense>`         |
| `GET`  | `/{id}`               | Retrieve a single expense by ID (must be owned by user) | JWT Required | None | `Expense`               |
| `GET`  | `/by-category?name={name}` | Retrieve expenses by category name (owned by user) | JWT Required | None | `List<Expense>`         |
| `GET`  | `/search?term={term}` | Search expenses by title (owned by user)      | JWT Required   | None                  | `List<Expense>`         |
| `POST` | `/`                   | Create a new expense                          | JWT Required   | `ExpenseCreateDto`    | `Expense`               |
| `PUT`  | `/{id}`               | Update an existing expense by ID (must be owned by user) | JWT Required | `ExpenseCreateDto`    | `Expense`               |
| `DELETE`|`/{id}`               | Delete an expense by ID (must be owned by user) | JWT Required | None                  | No Content (204)        |

---

## 🌐 Project Page URL (for submission)

**Your GitHub Repository:**
[https://github.com/blueisfresh/Expenser.git](https://github.com/blueisfresh/Expenser.git)