# Time Tracking System

This is a web-based time tracking application, developed as a practice project within IT application development training, aimed at deepening practical skills in development. It showcases the practical application of modern Java enterprise technologies and key software development principles.

The project repository is available at [this link](https://github.com/Lena435545/time-tracking-system).
## Project Overview

This project demonstrates the development of a complete enterprise web application using Spring Boot. It implements a time tracking system for employees with comprehensive user management, time logging functionality, and professional PDF report generation.

### Learning Objectives and Competencies

The project illustrates the following development competencies:
- **Backend Development**: Spring Boot Framework with MVC architecture
- **Database Design**: JPA/Hibernate Entity-Relationship modeling
- **Security Concepts**: Spring Security implementation
- **Frontend Integration**: Thymeleaf Template Engine
- **Reporting System**: PDF generation with iText
- **Software Architecture**: Layered Architecture Pattern
- **Quality Assurance**: Input validation and error handling

## Technology Stack

### Backend Technologies
- **Spring Boot 3.x** - Main framework for dependency injection and auto-configuration
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data access layer with repository pattern
- **Hibernate** - Object-Relational Mapping (ORM)
- **Bean Validation** - Input validation with annotations

### Frontend Technologies
- **Thymeleaf** - Server-side template engine
- **HTML5/CSS3** - Modern web standards
- **Responsive Design** - Mobile-optimized user interface

### Additional Libraries
- **iText PDF** - Professional PDF report generation
- **Lombok** - Code reduction through annotation processing
- **Maven** - Build management and dependency resolution

## System Architecture

```
src/main/java/com/manicheva/TimeTrackingSystem/
├── controllers/              # Presentation Layer (MVC Controllers)
│   ├── AuthController.java          # Authentication endpoints
│   ├── HomeController.java          # Dashboard and home page
│   ├── ReportController.java        # PDF report downloads
│   └── TimeEntryController.java     # Time tracking CRUD operations
│
├── models/                  # Data Model Layer (JPA Entities)
│   ├── Account.java                # User accounts with credentials
│   ├── Role.java                   # User roles (Enum)
│   ├── TimeEntry.java             # Time tracking entries
│   └── User.java                  # User profile information
│
├── services/               # Business Logic Layer
│   ├── AccountDetailsService.java  # Spring Security UserDetailsService
│   ├── PdfService.java            # PDF generation and formatting
│   ├── RegistrationService.java   # User registration logic
│   └── TimeEntryService.java      # Time tracking business logic
│
├── repositories/           # Data Access Layer
│   └── [JPA Repository Interfaces]
│
├── security/              # Security Configuration
├── utils/                # Validation and helper classes
└── config/               # Application Configuration

src/main/resources/
├── templates/            # Thymeleaf HTML Templates
│   ├── auth/                    # Login/Registration
│   ├── home/                    # Dashboard
│   ├── reports/                 # Report generation
│   └── time_entries/            # Time tracking interface
└── static/css/          # Stylesheets
```

## Database Model

### Entity-Relationship Design

**User Entity**
- User profile information (firstName, lastName, email, department)
- 1:1 relationship with Account
- 1:n relationship with TimeEntry

**Account Entity**
- Authentication credentials (username, password)
- Role-based access control
- BCrypt password encryption

**TimeEntry Entity**
- Time tracking records with start/end times
- Activity descriptions
- Automatic audit timestamps

## Installation and Setup

### Prerequisites
- Java 17+
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)
- Git

### Local Installation

1. **Clone the repository**
```bash
git clone [repository-url]
cd TimeTrackingSystem
```

2. **Install dependencies**
```bash
mvn clean install
```

3. **Configure database**
```properties
# application.properties
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/time_track
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

4. **Start the application**
```bash
mvn spring-boot:run
```

5. **Access via browser**
```
http://localhost:8080
```

## Functional Description

### Authentication and Security
- **Secure Registration**: Email-based user accounts
- **Spring Security Integration**: Form-based authentication
- **Role-based Authorization**: ADMIN/USER roles
- **Password Encryption**: BCrypt hashing

### Time Tracking
- **Start/Stop Functionality**: Simple timestamp recording
- **Activity Management**: Descriptions for work activities
- **Today's View**: Current time entries with live updates
- **Historical Views**: Date and range-based filtering

### CRUD Operations
- **Create**: Add new time entries
- **Read**: Display and filter time entries
- **Update**: Edit existing entries
- **Delete**: Remove entries with confirmation dialog

### PDF Reporting System
- **Daily Reports**: Detailed daily breakdowns
- **IHK Weekly Reports**: Official apprenticeship reports
- **Automatic Calculations**: Working time calculations
- **Professional Formatting**: Print-ready PDF output

## API Endpoints

### Authentication
```http
GET  /auth/login              # Login form
POST /auth/login              # Login processing
GET  /auth/registration       # Registration form
POST /auth/registration       # Process registration
GET  /auth/access_denied      # Access denied page
POST /auth/logout             # Logout function
```

### Time Tracking
```http
GET  /time_entries/today             # Today's entries
POST /time_entries/start             # Start time tracking
POST /time_entries/end/{id}          # End time tracking
GET  /time_entries/by_date           # Entries by date
GET  /time_entries/by_range          # Entries by date range
GET  /time_entries/edit/{id}         # Edit form
POST /time_entries/edit/{id}         # Update entry
POST /time_entries/delete/{id}       # Delete entry
```

### Report Generation
```http
GET /reports                 # Reports dashboard
GET /reports/daily          # Daily report (PDF)
GET /reports/ihk            # IHK weekly report (PDF)
```

## Testing and Quality Assurance

### Run unit tests
```bash
mvn test
```

### Integration tests
```bash
mvn verify
```

### Code coverage report
```bash
mvn jacoco:report
```

### Package application
```bash
mvn clean package
java -jar target/TimeTrackingSystem-*.jar
```

## Development Guidelines

### Code Conventions
- **Java Naming Conventions**: CamelCase for methods and variables
- **Package Structure**: Domain-based packaging
- **Dependency Injection**: Constructor-based injection preferred
- **Exception Handling**: Checked exceptions with specific messages

### Best Practices
- **Single Responsibility Principle**: One responsibility per class
- **Repository Pattern**: Abstract data access through interfaces
- **Service Layer**: Separate business logic from controllers
- **Input Validation**: Bean validation at all input levels

## Deployment

### Docker Deployment (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/TimeTrackingSystem-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Heroku Deployment
```bash
heroku create timetracking-ihk-demo
git push heroku main
heroku open
```

## Examination-Relevant Aspects

### Demonstrated Competencies
1. **Software Development**: Complete application development
2. **Database Design**: Normalized database structure
3. **Web Technologies**: Modern web application development
4. **Security Concepts**: Authentication/Authorization
5. **Documentation**: Comprehensive technical documentation
6. **Testing**: Unit and integration testing

### Advanced Features
- **Internationalization**: Preparation for multi-language support
- **Responsive Design**: Mobile-friendly interface
- **PDF Integration**: Professional document generation
- **Error Handling**: Comprehensive error management

## License

This project is released under the MIT License and is intended solely as a demonstration to deepen development skills in web development

```
MIT License

Copyright (c) 2025 Manicheva Lena

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```

## Contact and Support

**Developer**: Manicheva Lena

**Training Occupation**: IT Specialist in Application Development

---

