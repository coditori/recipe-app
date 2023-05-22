# Recipe Search App

## Description
Recipe Search App is a Java application that allows users to search for recipes based on various criteria such as vegetarian, servings, and ingredients. It provides a convenient way for users to find recipes that match their preferences and dietary requirements.

## Table of Contents
- [Recipe Search App](#recipe-search-app)
  - [Description](#description)
  - [Table of Contents](#table-of-contents)
  - [Features](#features)
  - [Technologies Used](#technologies-used)
  - [Installation \& Running](#installation--running)
  - [API Documentation](#api-documentation)
  - [Tests](#tests)
  - [Deployment](#deployment)
  - [Known Issues](#known-issues)

## Features
- Search for recipes based on criteria such as vegetarian, servings, and ingredients.
- Retrieve detailed information about each recipe, including instructions.
- Handle cases where no recipes are found matching the search criteria.
- Provide a user-friendly and intuitive interface for searching recipes.

## Technologies Used
- Java
- Spring Boot
- Spring Data JPA
- MySQL
- Maven

## Installation & Running
1. Clone the repository: `git clone <repository-url>`
2. Navigate to the project directory: `cd recipe-app`
3. Build & run the project: `./gradlew bootRun`

## API Documentation
The application provides a RESTful API for searching recipes. Detailed documentation can be found at `http://localhost:8080/swagger-ui/index.html` when the application is running.

## Tests

To run the tests for the application, execute the following command:

```
./gradlew test
```

## Deployment

The application can be deployed to a server or cloud platform using the following steps:

1. Build the project: `./gradlew build -x test`
2. Deploy the generated demo.jar file to the target environment.
3. Start the application using the command: java -jar demo-0.0.1-SNAPSHOT.jar

## Known Issues
- When I added Spring Doc to Spring Boot 3, tests will not run. I excluded some Spring Doc classes but it seems that is not working. At the end I disabled swagger then tests are working now!