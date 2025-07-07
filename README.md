# WebMotor

WebMotor is a web application designed for selling motorbikes. Built as an Eclipse project using Java, it follows the MVC (Model-View-Controller) architecture and is structured for easy management of users, products, orders, and administrative tasks.

## Features

- User registration, login, and profile management
- Product listing, search, and detailed product views
- Shopping cart and order management
- Admin dashboard for managing products, users, and orders
- Email notifications
- Role-based access control (Admin/User)
- Order and import management for inventory
- Bill and payment management

## Project Structure

```
WebMotor/
├── build/
│   └── classes/                # Compiled Java classes
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       ├── AdminController/   # Admin-related controllers
│       │       ├── Auxiliaries/       # Helper classes (Bill, Product, User, etc.)
│       │       ├── Entity/            # Entity classes (Account, Orders, etc.)
│       │       ├── Enums/             # Enum types (Roles, Payment methods, etc.)
│       │       ├── Interceptors/      # Request interceptors
│       │       ├── UserController/    # User-facing controllers
│       │       └── Utils/             # Utility classes
│       └── webapp/
│           ├── META-INF/
│           └── WEB-INF/
│               ├── web.xml            # Servlet configuration
│               ├── configs/
│               ├── lib/               # Libraries
│               ├── resources/
│               └── views/             # JSP/HTML views
```

## Technologies Used

- Java (Servlets, JSP)
- MVC architecture
- JDBC for database connectivity
- SQL Server
- Eclipse IDE
- HTML, CSS, JavaScript (for frontend)
- Email integration

## Getting Started

### Prerequisites

- Java JDK 8 or higher
- Eclipse IDE (or any compatible Java IDE)
- Apache Tomcat (or any Java EE compatible server)
- SQL Server (or compatible database)
- Maven (if using for dependency management)

### Setup Instructions

1. **Clone the repository:**

2. **Import the project into Eclipse:**
   - Open Eclipse.
   - Go to `File` > `Import` > `Existing Projects into Workspace`.
   - Select the cloned folder.

3. **Configure the database:**
   - Set up your SQL Server database.
   - Update database connection settings in the `JDBC` utility class.

4. **Add libraries:**
   - Place required JAR files in lib.

5. **Deploy to server:**
   - Right-click the project > `Run As` > `Run on Server`.
   - Or deploy the WAR file to your Tomcat server.

6. **Access the application:**
   - Open your browser and go to `http://localhost:8080/WebMotor/`.

## Folder Overview

- `AdminController/` - Handles admin operations (product, user, bill management)
- `UserController/` - Handles user operations (cart, order, profile, etc.)
- `Entity/` - Java classes mapping to database tables
- `Auxiliaries/` - Helper classes for business logic
- `Enums/` - Enum types for roles, payment, etc.
- `Interceptors/` - Request/response interceptors for security and tracking
- `Utils/` - Utility classes for sessions, database, etc.
- `webapp/WEB-INF/views/` - JSP/HTML files for UI