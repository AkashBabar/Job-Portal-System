# 🧑‍💼 Job Portal System

A full-stack job portal web application that streamlines job postings, applications, and user management for **Students**, **Companies**, and **Admins**. Built using **Spring Boot**, **Hibernate**, **JSP/Servlet**, and **PostgreSQL**, with secure JWT-based authentication and role-based access.

---

## 🚀 Features

### 👨‍🎓 Student Panel
- Secure student registration and login
- Browse and filter job listings by keyword, location, job type, skills, or salary
- View detailed job descriptions
- Apply to jobs instantly
- Track submitted applications

### 🏢 Company Panel
- Company registration and login
- Post new job vacancies
- View all applications received
- Manage posted jobs

### 🛡️ Admin Panel
- Manage and verify registered companies
- Monitor student registrations
- Approve or reject job posts
- Delete suspicious accounts or job listings

---

## 🛠️ Tech Stack

| Layer         | Technology                                      |
|---------------|--------------------------------------------------|
| Frontend      | JSP, HTML, CSS, Bootstrap                       |
| Backend       | Java, Spring Boot, Spring Security, Hibernate   |
| Database      | PostgreSQL                                       |
| Authentication| JWT (JSON Web Token)                             |
| Build Tool    | Maven                                            |

---

## 🔐 Security

- User authentication with Spring Security
- JWT-based session management
- Role-based authorization (STUDENT, COMPANY, ADMIN)
- Sensitive properties secured via `.gitignore`

---

## 🏗️ Project Structure

```
Job-Portal-System/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/jobportal/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── model/
│   │   │       └── config/
│   │   └── resources/
│   │       ├── templates/ (JSP views)
│   │       ├── static/ (CSS, JS, Images)
│   │       ├── application.properties <-- (Ignored by Git)
│   │       └── application-example.properties <-- (Shared config template)
├── pom.xml
└── README.md
```

---

## 📡 REST API Overview

| Method | Endpoint                     | Access Role | Description                          |
|--------|------------------------------|-------------|--------------------------------------|
| POST   | `/api/auth/register`         | Public      | Register a new Student or Company    |
| POST   | `/api/auth/login`            | Public      | Authenticate and receive JWT token   |
| POST   | `/api/jobs`                  | Company     | Create a new job listing             |
| GET    | `/api/jobs/search`           | Student     | Search jobs with filters             |
| GET    | `/api/admin/users`           | Admin       | View all registered users            |
| PUT    | `/api/admin/approve-job`     | Admin       | Approve job postings                 |
| DELETE | `/api/admin/delete-user/{id}`| Admin       | Delete any user                      |

---

## 📦 Getting Started

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/yourusername/job-portal-system.git
cd job-portal-system
```

### 2️⃣ Set Up the Database

- Create a PostgreSQL database:
  ```sql
  CREATE DATABASE jobportal;
  ```

### 3️⃣ Configure Application Properties

- Copy the example config file:
  ```bash
  cp src/main/resources/application-example.properties src/main/resources/application.properties
  ```

- Update `application.properties` with your credentials:
  ```properties
  spring.datasource.url=jdbc:postgresql://localhost:5432/jobportal
  spring.datasource.username=your_db_username
  spring.datasource.password=your_db_password
  jwt.secret=your_jwt_secret_key
  ```

> 🔐 **Important:** Never push your `application.properties` to GitHub. It's ignored via `.gitignore`.

### 4️⃣ Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

Open in browser: [http://localhost:8080](http://localhost:8080)

---

## 📝 application-example.properties (Sample)

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/jobportal
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA & Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT Secret
jwt.secret=your_jwt_secret_key
```

---

## 📂 .gitignore (Security Best Practice)

```gitignore
# Ignore sensitive configs
src/main/resources/application.properties
```

---

## 👨‍💻 Developed By

**Akash Babar**  
Java Developer | MCA, Mumbai University  
🔗 [LinkedIn](https://www.linkedin.com/in/akashbabar/)  


---

## 📃 License

This project is licensed under the [MIT License](LICENSE).

---

## 🙏 Acknowledgements

- Spring Boot & Spring Security
- Hibernate ORM
- PostgreSQL Community
- Stack Overflow & GitHub Community
