# Software Engineering – Firmware System (MOM-Based Demonstrator)

This repository contains the **Firmware System implementation** for the *Software Engineering* course at **HAW Hamburg**.  
It demonstrates **Message-Oriented Middleware (MOM)** communication using **Java Message Service (JMS)** and **ActiveMQ**.

---

## Overview
This project is part of the *Adaptive Cleaning & Perfume Dispensing* cluster (UC-9 & UC-10).  
It extends the previous RMI-based implementation to use **JMS** for asynchronous, message-based communication between software components.

### Key Concepts:
- **Message-Oriented Middleware (MOM)** via JMS and ActiveMQ
- **Observer**, **Decorator**, and **Factory** design patterns
- **Asynchronous communication** between robot control and firmware
- **Component-based modular structure**

---

## Development Process

The firmware system was developed following a complete software engineering workflow across six lab phases:

1. **Requirements Engineering (Labs 1–2):**  
   Conducted market and user analysis, derived system requirements, and created use case diagrams, activity diagrams, and traceability matrices.

2. **Design and Modeling (Labs 3–4):**  
   Performed domain modeling and designed class and sequence diagrams in **BlueJ** using *responsibility-driven design*.  
   Applied **Observer**, **Decorator**, and **Factory** design patterns to structure system logic.

3. **Implementation (Lab 5):**  
   Built a distributed **Client-Server demonstrator** using **Java RMI** with well-defined remote interfaces and serializable data objects.  
   Verified correct communication and state synchronization between client (App) and server (Robot Firmware).

4. **MOM-Based Extension (Lab 6):**  
   Re-implemented the system using **Message-Oriented Middleware (MOM)** via **JMS** and **Apache ActiveMQ** for asynchronous message exchange.  
   Tested and validated end-to-end message flow, callbacks, and dynamic behavior using console outputs and broker inspection.

Throughout all stages, **software testing**, **version control (GitLab)**, and **structured reporting** were used to ensure reliability and traceability.


## Repository Contents
| Folder/File | Description |
|--------------|-------------|
| `Lab6_mombased/` | Java source files for the MOM-based firmware system |
| `+libs/` | Required library JAR files (ActiveMQ, JMS, Log4j, etc.) |
| `SE-Lab-6_Final_Report-Firmware.md` | Markdown report for Lab 6 |
| `SE_Lab5-6_Tasks_Distributed_Use_Case_Demonstrators.pdf` | Lab task documentation |
| `ClassDiagram_MOM_Updated1.png`, `RMI_SequenceDiagram.png` | UML and sequence diagrams |

---

## Technologies Used
- **Java (JMS API)** – for implementing communication logic between firmware components  
- **Message-Oriented Middleware (MOM)** – asynchronous message exchange using JMS  
- **Apache ActiveMQ** – message broker for MOM communication  
- **BlueJ IDE** – for development, UML integration, and testing  
- **Software Testing** – verification of message flow, callbacks, and firmware behavior  
- **Design Patterns:** Observer, Factory, Decorator – for modular, extensible architecture


---

## How to Run
1. Install **Apache ActiveMQ** and start the broker.
2. Open the project in **BlueJ** or any Java IDE.
3. Add all JARs from the `+libs` folder to the classpath.
4. Run `Server` and `JMSClient` to simulate robot-firmware interaction.

---

## Contributors
- **Mir Md Redwon Sagor**
- **Faisal Ahammed**
- **Abrar Fahim**
- Team: Firmware (Partner B)

---

## License
This project is part of university coursework and provided for academic reference only.
