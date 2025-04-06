# 🚗 Car Garage Management System – Java Swing Project

This is a full-featured desktop application developed in Java using Swing and JDBC for managing a car service and repair garage. It was built as part of a university assignment and includes modules for handling car records, customers, employees, services, payments, and admin login.

---

## 📌 Features

- ✅ **Admin login system**
- 🚗 Manage cars (Add, update, search)
- 👤 Manage customers and their service history
- 👨‍🔧 Employee salary and performance tracking
- 📦 Order and service management
- 💰 Payment insertion, update, and search
- 📊 Generate various reports:
  - Sales by employee
  - Service history
  - Service frequency
  - Service revenue

---

## 📁 Project Structure
## 📁 Project Structure

```
Talia-carage/
├── src/
│   └── main/
│       ├── [Java classes like CarInsert.java, LoginUI.java, etc.]
│       └── ...
├── bin/                         # Compiled .class files
├── images/                      # Screenshots and image resources
├── mysql-connector-j-9.1.0.jar  # JDBC connector
```
Technologies Used

- Java SE (Swing GUI)
- JDBC (Java Database Connectivity)
- MySQL (for data storage)
- Eclipse IDE

---

## 🚀 How to Run

1. Open the project in **Eclipse**
2. Make sure to connect the JDBC driver:
   - Add `mysql-connector-j-9.1.0.jar` to your classpath
3. Update your **DB connection** in `DBUtil.java`:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/your_db";
   private static final String USER = "root";
   private static final String PASS = "your_password";

   Created with  by Talia Geo
