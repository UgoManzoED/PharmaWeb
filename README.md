*[Read this in English](README.md) | [Leggi in Italiano](README.it.md)*

# PharmaWeb - Online Pharmacy E-Commerce Platform

**University of Salerno** **Course:** Web Software Technologies - A.Y. 2024/2025  
**Professor:** Simone ROMANO  

**Team:** 
* Ugo Manzo (Matricola: 0512119071) - *Coordinator / Backend & Database* - [GitHub](https://github.com/UgoManzoED)
* Davide Pio Lazzarini (Matricola: 0512119112) - *Frontend & UI/UX* - [GitHub](https://github.com/davidelazz)

> A comprehensive e-commerce platform designed to facilitate the online sale of over-the-counter drugs, supplements, and personal care products, ensuring a modern and secure user experience.

![Project Status](https://img.shields.io/badge/Status-Active-success)
![Backend](https://img.shields.io/badge/Backend-Java%20EE-orange)
![Database](https://img.shields.io/badge/Database-MySQL-blue)
![Frontend](https://img.shields.io/badge/Frontend-Vanilla%20JS-yellow)

---

## Table of Contents
* [About the Project](#about-the-project)
* [Architecture & Security](#architecture--security)
* [Key Features](#key-features)
* [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Test Credentials](#test-credentials)
* [Documentation](#documentation)

---

## About the Project
**PharmaWeb** is a full-stack web application developed to digitalize the retail operations of a pharmacy. The system provides a seamless shopping experience for customers and a robust management dashboard for administrators, bridging the gap between inventory control and user engagement through features like dynamic cataloging, asynchronous cart management, and a loyalty point system.

## Architecture & Security
The project strictly adheres to the **Model-View-Controller (MVC)** architectural pattern, ensuring a clean separation of concerns between business logic, data access (via the DAO pattern), and presentation.

* **Data Integrity & Performance:** Utilizes SQL Views for complex calculation logic and database indexes to optimize query performance.
* **CSRF Protection:** Implements a centralized filter with automatic token rotation on every POST request.
* **Authentication:** Password hashing using the **BCrypt** algorithm (via jBcrypt).
* **Injection Prevention:** Systematic use of `PreparedStatement` to mitigate SQL Injection vulnerabilities.
* **View Security:** JSP pages are protected and isolated within the `/WEB-INF/` directory to prevent direct URL access.

## Key Features

### Client Area
**Dynamic Catalog:** Browse products by category and perform advanced text searches.\
**Cart & Wishlist:** Asynchronous operations via AJAX/Fetch API with database persistence for authenticated users.\
**Advanced Checkout:** Secure transaction processing with historical price tracking and automatic loyalty point calculation (1 point per €20 spent).\
**Personal Dashboard:** Dedicated area for users to manage shipping addresses, payment methods, and view order history.

### Administrator Area
**Catalog Management (CRUD):** Add, edit, and delete products with support for real image uploads.\
**Order Monitoring:** Global view of platform sales with advanced filtering by date and customer email.\
**User Management:** Complete roster of registered users and role assignment capabilities.

## Built With
* **Backend:** [Java 17 LTS](https://www.oracle.com/java/), Jakarta EE 10 (Servlet, JSP, JSPF, Tag Files), [Apache Tomcat 10.1](https://tomcat.apache.org/), Maven.
* **Database:** [MySQL 8.0](https://www.mysql.com/), JDBC (`DriverManager`).
* **Frontend:** HTML5, CSS3 (Custom Grid System & Responsive Design), Vanilla JavaScript, AJAX/Fetch API.
* **Libraries:** JSTL 2.0, FontAwesome 6, Google Gson.

---

## Getting Started
Follow these instructions to set up the local development environment.

### Prerequisites
* Java JDK 17
* Apache Tomcat 10.1
* MySQL Server 8.0
* Eclipse IDE for Enterprise Java and Web Developers (Recommended)

### Installation

1. **Database Setup:**
   * Execute the `BaseDati/schema.sql` script to generate the database structure.
   * Execute the `BaseDati/population.sql` script to populate the database with test data and categories.

2. **Backend Configuration:**
   * Open `DriverManagerConnectionPool.java` and update the connection credentials to match your local MySQL instance.

3. **Deployment:**
   * Import the project into Eclipse as an "Existing Maven Project".
   * Bind the project to your local Apache Tomcat 10.1 Server runtime environment.
   * Start the server in *Debug* mode and access the application at: `http://localhost:8080/PharmaWeb/`

---

## Test Credentials
Use the following credentials to explore the different access levels of the platform:

| Role | Email | Password |
| :--- | :--- | :--- |
| **Administrator** | `admin@pharmaweb.it` | `Password123!` |
| **Customer** | `mario.rossi@email.com` | `Password123!` |

---

## Documentation
The complete **Website Design Document (WDD)** detailing the architectural choices and UI/UX design is available in the `Deliverables/` folder, alongside the generated **Javadoc** for the entire backend source code.
