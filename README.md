# # Pet Pedia
Pet-Pedia is a forum and information sharing platform for pet enthusiasts. 
Built using Spring Boot, Jakarta EE, and Thymeleaf, this project allows users to share posts, comment on them, and interact with the community.


## Table of Contents
- [Prerequisites]()
- [Installation]()
- [Building the Project]()
- [Running the Project]()
- [Technology Stack]()
- [Project Structure]()
- [Configuration]()
- [Usage]()
- [Contributing]()
- [License]()

## Prerequisites

Before you begin, ensure you have met the following requirements:
- Java Development Kit (JDK) 23 or later
- Maven 3.6 or later
- MySQL 5.7 or later
- Git

## Installation

### Clone the Repository
    git clone https://github.com/yourusername/pet-pedia.git
    cd pet-pedia

### Set Up MySQL Database
Create a new database called `pet_pedia` and update the `src/main/resources/application.properties` file with your database configuration.

    spring.datasource.url=jdbc:mysql://localhost:3306/pet_pedia
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword
    spring.jpa.hibernate.ddl-auto=update

### Install Dependencies
Use Maven to install the required dependencies:

    mvn clean install

## Building the Project
To build the project, use the following Maven command:
    mvn clean package
This will compile the project and package it as a JAR file in the `target` directory.

## Running the Project
Once the project is built, you can run it using the following command:
    java -jar target/pet-pedia-0.0.1-SNAPSHOT.jar
The application should now be running on [http://localhost:8080]().

## Technology Stack
- **Java 23**: The core language used for development.
- **Spring Boot**: Provides the framework for building the application.
- **Spring Data JPA**: Used for data persistence.
- **Spring MVC**: Handles the web layer.
- **Thymeleaf**: Template engine for rendering HTML.
- **Jakarta EE**: Provides necessary enterprise features.
- **Lombok**: Reduces boilerplate code.
- **MySQL**: The database used to store application data.

## Project Structure
pet-pedia
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── petpedia
│   │   │           ├── controllers
│   │   │           ├── model
│   │   │           ├── repository
│   │   │           └── service
│   │   └── resources
│   │       ├── static
│   │       ├── templates
│   │       └── application.properties
└── README.md

## Configuration
Configure the application by updating the `application.properties` file located in `src/main/resources/`.
Key configurations include database settings, server port, and Hibernate settings.
## Usage
1. **Access the Application**
   Open a browser and navigate to [http://localhost:8080]().
2. **User Registration**
    - Sign up or log in to create and interact with posts.

3. **Creating and Viewing Posts**
    - Create new posts and view community contributions.

4. **Profile Management**
    - Update profile pictures and personal information.

## Contributing
1. **Fork the Project**
   Fork the repository on GitHub to start making your changes.
2. **Clone Your Fork**
   git clone https://github.com/yourusername/pet-pedia.git
3. **Create a New Branch**
   git checkout -b feature-branch-name
4. **Make Changes**
Make your changes to the code and commit them.
5. **Push to GitHub (or GitLab)**
    git push origin feature-branch-name
6. **Create a Pull Request**
Submit your changes by creating a pull request to the `main` branch of the original repository.

## License
This project is licensed under the MIT License. See the [LICENSE]() file for details.