# GoSafe Backend

Backend service for **GoSafe – Intelligent Ride Safety Mediation Platform**, a project focused on improving ride safety through route analysis, safety scoring, authentication, and emergency assistance.

The backend is being developed using **Java and Spring Boot** and will provide REST APIs for the GoSafe frontend application.

> 🚧 **Project Status:** In Development  
> The backend is currently under active development. Some features and modules are incomplete and will be added progressively.

---

## 🚀 About GoSafe

GoSafe is an intelligent ride safety platform designed to help users plan safer journeys by analyzing route-related factors and providing safety information.

The system is intended to provide features such as:

- User authentication and authorization
- Journey planning
- Route analysis
- Route safety scoring
- Interactive map integration
- Emergency SOS functionality
- Captain/driver information
- User and journey management
- Real-time safety-related features

---

## 🛠️ Tech Stack

### Backend
- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- REST APIs
- Maven

### Database
- PostgreSQL
- MongoDB *(planned/used for specific modules)*

### Security
- JWT Authentication
- Spring Security

### Other Technologies
- Redis
- Google Maps API
- OpenStreetMap
- OSRM
- Spring AI *(planned/under development)*

---

## 📂 Project Structure

```text
gosafe-backend/
│
├── .mvn/
│   └── wrapper/
│
├── src/
│   └── main/
│       ├── java/
│       └── resources/
│
├── .gitignore
├── .gitattributes
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
