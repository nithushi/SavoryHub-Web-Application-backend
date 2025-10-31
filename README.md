# SavoryHub API (Backend)

This is the complete backend REST API for **SavoryHub**, a full-stack food delivery and ordering web application. It is built using **Spring Boot**, **Spring Security**, and **MySQL**.

This API handles all business logic, data persistence, and security for the application, including user authentication, role-based authorization (USER vs. ADMIN), product management, and order processing.

-   **Frontend (React) Repository:** `[Link to your Frontend Repo]` (මෙහි ඔබේ frontend repository link එක දාන්න)

---

## ✨ Features

### User & Authentication (Public & User Roles)
* **JWT Authentication:** Secure Register and Login endpoints using JWT (JSON Web Tokens).
* **User Profile Management:**
    * Get user's personal details.
    * Update personal details (name, contact).
    * Change password (requires current password for verification).
    * Upload/Update user profile picture.
* **Address Management:** Auto-fill and update user's default shipping address from their profile.

### Product & Browsing (Public)
* Fetch all products, single products by ID, and products by category.
* Search products by name (case-insensitive).
* Serve static product images.

### E-Commerce (User Role)
* **Full Shopping Cart:** Add to cart, view cart, update quantities, and remove items. All cart data is persisted in the database.
* **Checkout:** Place a new order using items from the cart.
* **Order History:** View a list of all past orders for the logged-in user.

### 🔐 Admin Panel (Admin Role)
* **Role-Based Access:** All admin endpoints are protected and accessible only by users with the `ROLE_ADMIN` authority.
* **Dashboard Analytics:** A secure endpoint to fetch key statistics (Total Revenue, Total Orders, Total Users, Pending Orders).
* **Product Management (CRUD):**
    * Create, Read, Update, and Delete any product.
* **Order Management (CRUD):**
    * View all orders from all users.
    * Update the status of any order (e.g., "Pending", "Processing", "Delivered").
* **User Management (CRUD):**
    * View a list of all registered users.
    * Change any user's role (e.g., "USER" to "ADMIN").
    * Activate or Deactivate user accounts.

---

## 🛠️ Tech Stack

* **Framework:** Spring Boot 3
* **Security:** Spring Security 6 (with JWT Authentication & Method-Level Security)
* **Data:** Spring Data JPA (Hibernate)
* **Database:** MySQL
* **API:** RESTful APIs
* **Build Tool:** Maven
* **Utilities:** Lombok, `jjwt` (Java JWT library)

---

## 🏁 Getting Started

### Prerequisites
* JDK 17 (or newer)
* Maven 3.0+
* A running MySQL database instance

### How to Run

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/nithushi/SavoryHub-Web-Application-backend.git](https://github.com/nithushi/SavoryHub-Web-Application-backend.git)
    cd SavoryHub-Web-Application-backend
    ```

2.  **Create your `application.properties` file:**
    This project uses `.gitignore` to hide the `application.properties` file (which contains sensitive passwords). You must create your own.

    Create a new file at `src/main/resources/application.properties` and paste the following content, replacing the placeholders with your own MySQL database credentials.

    ```properties
    # MySQL Database Connection
    spring.datasource.url=jdbc:mysql://localhost:3306/savoryhub_db?createDatabaseIfNotExist=true
    spring.datasource.username=YOUR_MYSQL_USERNAME
    spring.datasource.password=YOUR_MYSQL_PASSWORD

    # JPA/Hibernate Settings
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true

    # Allow multipart (file uploads)
    spring.servlet.multipart.max-file-size=10MB
    spring.servlet.multipart.max-request-size=10MB
    ```

3.  **Check the JWT Secret Key:**
    The JWT secret key is currently hardcoded in `src/.../service/JwtService.java`. For production, this should be moved to the `application.properties` file and read via `@Value`.

4.  **Run the application:**
    You can run the application directly from your IDE (like IntelliJ IDEA) by running the `SavoryHubProjectApplication.java` file, or by using Maven in your terminal:

    ```bash
    mvn spring-boot:run
    ```
    The application will start on **`http://localhost:8080`**.

---

## 🔑 API Endpoints Summary

All routes are protected by default unless specified otherwise.

* **Auth (`/api/auth`)**: `PUBLIC`
    * `POST /register`
    * `POST /login`
* **Products (`/api/products`)**:
    * `GET /all`, `GET /{id}`, `GET /category/{name}`, `GET /search`: `PUBLIC`
    * `POST`, `PUT /{id}`, `DELETE /{id}`: `ADMIN Only`
* **Cart (`/api/cart`)**: `USER Role Required`
    * `GET /`
    * `POST /add`
    * `PUT /update`
    * `DELETE /remove/{id}`
* **Orders (`/api/orders`)**:
    * `POST /place`, `GET /my-orders`: `USER Role Required`
    * `GET /all`, `PUT /{id}/status`: `ADMIN Role Required`
* **User Management (`/api/user`)**:
    * `GET /me`, `PUT /update`, `POST /change-password`, `POST /profile-image`: `USER Role Required`
    * `GET /all`, `PUT /{id}/role`, `PUT /{id}/toggle-status`: `ADMIN Role Required`
* **Admin Stats (`/api/admin`)**:
    * `GET /stats`: `ADMIN Role Required`
* **Static Files (`/images/**`, `/uploads/profile-images/**`)**: `PUBLIC`
