![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![NetBeans](https://img.shields.io/badge/NetBeans-1B6AC6?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white)
![OOP](https://img.shields.io/badge/OOP-Object--Oriented-blue?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-success?style=for-the-badge)

# 🍰 GreenLeaf Bakery Management System

A Java-based GUI application developed to manage bakery operations, including customer orders, baker assignments, ingredient tracking, preparation reports, and invoice generation.

This project was developed as part of the **SE 1307 – Programming 02** module at **ICBT Kandy Campus**, with a focus on Object-Oriented Programming, file handling, exception handling, and user-friendly application design.

---

## 📌 Project Overview

GreenLeaf Bakery is a bakery business that manages cakes, pastries, breads, and custom orders for events such as birthdays and weddings.

The GreenLeaf Bakery Management System provides a digital solution for managing the bakery's daily operations. It allows customers and staff to manage order information, assign bakers, track preparation details, maintain ingredient records, and generate final invoices.

The application uses files to save and retrieve data, allowing information to persist between program executions.

---

## ✨ Main Features

### 👤 Customer Management
- Register customer details
- Update customer information
- Maintain customer records

### 🧾 Order Management
- Create new bakery orders
- Update order details
- Record cake, pastry, and bread orders
- Store size, flavors, instructions, and delivery dates
- Track assigned baker and order manager

### 👨‍🍳 Baker Management
- Manage baker information
- Record baker qualifications and specialization
- Assign bakers to orders
- Prepare bakery orders
- Generate preparation reports

### 📋 Preparation Reports
- Record completed work
- Track ingredients used
- Record preparation time
- Record final quality
- Submit preparation reports

### 🧂 Ingredient Management
- Store ingredient details
- Manage unit prices
- Track available quantities
- Update ingredient stock

### 💰 Invoice Management
- Calculate order costs
- Include ingredient, labor, and delivery costs
- Generate final invoices
- Store invoice information

### 🔐 User Access
- Staff login
- User authentication
- Role-based access, depending on the implemented system

---

## 🛠️ Technologies Used

| Technology | Purpose |
|------------|---------|
| Java | Main programming language |
| Java Swing / Java GUI | User interface |
| Object-Oriented Programming | Application structure and design |
| File Handling | Data persistence |
| Exception Handling | Error management |
| NetBeans IDE | Development environment |

> **Note:** Update the GUI technology if your project uses JavaFX instead of Java Swing.

---

## 🧠 Object-Oriented Programming Concepts

This project applies important Object-Oriented Programming concepts, including:

- **Class** – Used to define entities such as Customer, Order, Baker, and Invoice.
- **Object** – Used to create instances of classes.
- **Encapsulation** – Used to protect and manage class data through methods.
- **Abstraction** – Used to represent essential system functionality while hiding unnecessary implementation details.
- **Inheritance** – Used where common properties or behaviours are shared between classes.
- **Polymorphism** – Used where methods or behaviours can take different forms.

These concepts help make the application more organized, maintainable, and reusable.

---

## 📂 Main System Classes

The system is designed around the following main entities:

| Class | Responsibility |
|-------|----------------|
| Customer | Stores customer information |
| Order | Manages bakery order details |
| Baker | Manages baker information and order preparation |
| OrderManager | Assigns bakers and manages orders |
| PreparationReport | Stores preparation and quality details |
| Invoice | Calculates and generates order invoices |
| Ingredient | Manages ingredient details and stock |

---

## 💾 Data Persistence

The system uses **file-based storage** to save and retrieve information.

This allows data such as:

- Customer details
- User accounts
- Order information
- Baker information
- Ingredient records
- Preparation reports
- Invoice details

to remain available after the application is closed and reopened.

---

## 🖥️ Application Workflow

```text
Start Application
       │
       ▼
     Login
       │
       ▼
   Main Dashboard
       │
       ├── Customer Management
       │       └── Register / Update Customer
       │
       ├── Order Management
       │       └── Create / Update Order
       │
       ├── Baker Management
       │       └── Assign Baker to Order
       │
       ├── Preparation Report
       │       └── Record Work and Ingredients
       │
       ├── Ingredient Management
       │       └── Update Stock and Prices
       │
       └── Invoice Management
               └── Calculate Cost and Generate Invoice
```

---

## ⚙️ How to Run the Project

### 1. Clone the Repository

```bash
git clone https://github.com/YOUR-USERNAME/GreenLeaf-Bakery-Management-System.git
```

### 2. Open the Project

Open the project using **NetBeans IDE** or another compatible Java IDE.

### 3. Configure the Project

- Make sure Java JDK is installed.
- Open the project in the IDE.
- Check that the required source files and libraries are available.
- Ensure the data files are located in the correct project directory.

### 4. Run the Application

Run the main Java class or the project's main form.

> **Note:** Update the run instructions according to your actual project structure.

---

## 📁 Example Project Structure

```text
GreenLeaf-Bakery-Management-System/
│
├── src/
│   └── ...
│
├── data/
│   ├── customers.txt
│   ├── orders.txt
│   ├── bakers.txt
│   ├── ingredients.txt
│   ├── reports.txt
│   └── invoices.txt
│
├── README.md
└── ...
```

> **Note:** The actual folder and file names may differ depending on your implementation.

---

## 🎯 Project Objectives

- Develop a user-friendly bakery management application.
- Apply Object-Oriented Programming concepts in a practical system.
- Manage customer and bakery order information.
- Implement baker assignment and preparation tracking.
- Manage ingredients and invoice calculations.
- Use file handling for data persistence.
- Practice exception handling and software testing.

---

## 🧪 Testing

The project includes testing and documentation to verify the functionality of the developed system.

Testing areas may include:

- User login validation
- Customer registration
- Order creation
- Order updating
- Baker assignment
- Ingredient updates
- Invoice generation
- File data storage and retrieval
- Invalid input handling

---

## 🚀 Future Improvements

Possible future improvements include:

- 🗄️ MySQL database integration
- 🌐 Web-based version
- 📱 Mobile application
- 📊 Advanced sales reports
- 📦 Automatic ingredient stock alerts
- 💳 Online payment integration
- 📧 Email notifications
- ☁️ Cloud-based data storage
- 👥 Improved role-based access control

---

## 👨‍💻 Developer

**Udan Senarathna**

Software Engineering Student  
ICBT Kandy Campus

### Interests

- Software Development
- Web Development
- App Development
- Game Development
- Front-End Development
- Quality Assurance
- Project Management

---

## 📚 Academic Information

**Module:** SE 1307 – Programming 02  
**Course:** BSc (Hons) in Software Engineering  
**Institution:** ICBT Kandy Campus

---

## 📄 License

This project was developed for educational and learning purposes.
