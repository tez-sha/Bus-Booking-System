# Bus Booking System

This is a web application for managing bus bookings. It provides functionalities for users to search for available buses, make bookings, and manage their bookings.

## Technologies Used

- **Frontend**: React.js
- **Backend**: Spring Boot 2.7.18
- **Database**: MySQL 8.0
  

## Features

- User Authentication: Users can sign up, log in, and log out.
- Bus Search: Users can search for available buses based on various criteria such as origin, destination, date, etc.
- Booking Management: Users can make bookings for available buses and manage their bookings.
- Admin Panel: Administrators can manage buses, bookings, users, etc.
# Bus Booking System Backend

Welcome to the Bus Booking System Backend! This project provides the backend APIs for managing bus bookings, routes, users, and other related functionalities.

## Table of Contents

- [Introduction](#introduction)
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Setup Instructions](#setup-instructions)
- [API Documentation](#api-documentation)
- [Contributing](#contributing)
- [License](#license)

## Introduction

The Bus Booking System Backend is a crucial component of the Bus Booking System project. It provides RESTful APIs that handle various operations such as creating, updating, and deleting bookings, managing routes, handling user authentication, and more.

## Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- PostgreSQL (or your preferred database)
- Maven

## Features

- **User Management:** APIs for user registration, login, and authentication.
- **Booking Management:** APIs for creating, updating, and canceling bus bookings.
- **Route Management:** APIs for managing routes, including adding new routes, updating route details, and deleting routes.
- **Authorization and Authentication:** Integration with Spring Security for secure user authentication and authorization.
- **Error Handling:** Comprehensive error handling for API requests, including appropriate HTTP status codes and error messages.
- **Documentation:** Detailed API documentation to help developers understand and use the backend APIs effectively.

## Setup Instructions

1. **Clone the Repository:**

    ```bash
    git clone <repository-url>
    ```

2. **Navigate to the Project Directory:**

    ```bash
    cd BusBookingSystemBackend
    ```

3. **Configure Database:**

    Configure your database settings in the `application.properties` file.

4. **Build and Run the Application:**

    ```bash
    mvn spring-boot:run
    ```

    The application will start running on `http://localhost:8080`.

## API Documentation

You can find the detailed API documentation in the `apidoc.txt` file in the project repository. Additionally, you can import the provided Postman collection `CDACBusBookingSystem.postman_collection.json` to explore and test the APIs interactively.

## Contributing

Contributions are welcome! If you'd like to contribute to the project, please follow these steps:

1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/YourFeature`).
3. Commit your changes (`git commit -m 'Add YourFeature'`).
4. Push to the branch (`git push origin feature/YourFeature`).
5. Open a pull request.

Please ensure that your pull request follows the project's coding conventions and includes relevant tests if applicable.

## License

This project is licensed under the [MIT License](LICENSE).

