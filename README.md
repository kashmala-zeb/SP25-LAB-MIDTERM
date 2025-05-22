Smart Travel Booking System

This project is a simple Java Swing desktop application called Smart Travel that allows users to book tickets for different transport types (Bus, Train, Airline) and select a payment method. It demonstrates the use of design patterns like Factory, Strategy, and Observer, along with basic Swing UI programming.

---

Project Overview

- User Interface: Java Swing GUI with fields for traveler name, transport type, payment method, and an output area showing booking details.
- Design Patterns:
  - Factory Pattern for creating transport providers and payment strategies.
  - Strategy Pattern for payment methods.
  - Observer Pattern for booking notifications (email and admin logger).
- Domain: Travelers can book tickets with specific transport providers and payment methods.
- Output: Booking results displayed in the text area and logged to console via observers.

---


---

 How to Run the Application

Prerequisites

- Java Development Kit (JDK) installed (Java 8 or above recommended)
- An IDE like IntelliJ IDEA, Eclipse, or you can use command-line

Steps

Using an IDE

1. Clone or download this repository to your local machine.
2. Open the project folder in your favorite IDE.
3. Locate the `BookingApp.java` file.
4. Run the `BookingApp` class as a Java Application.
5. The GUI window titled "Smart Travel - Book a Ticket" will open.
6. Enter traveler name, select transport type and payment method.
7. Click Book Ticket to see booking confirmation and logs.

Using Command Line

1. Open a terminal/command prompt.
2. Navigate to the folder containing the source files.
3. Compile the Java source files:
   ```bash
   javac BookingApp.java


