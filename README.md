

  <h1 align="center">Talent Share Portal</h1>
<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/rahul2011421/Talent-share-Portal-Application/blob/master/images/Login%20page.png">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>
</div>

<br />


<!-- ABOUT THE PROJECT -->
## About The Project

TSP is a mentorship platform that facilitates tech exploration by fostering an environment of continuous learning culture. It
implements user profiles for mentors who guide learners to explore tech stacks and gain new skills through
extensive training.


### Technology Stack

#### Backend
- **Language**: Java
- **Framework**: Spring Boot
- **Architecture**: Microservices
- **APIs**: REST APIs
- **Database**: MySQL
- **Testing**: JUnit, Mockito
- **IDE**: IntelliJ

#### Frontend
- **Languages**: HTML, CSS, TypeScript
- **Framework**: React.js
- **IDE**: Visual Studio Code

<br/>


<!-- GETTING STARTED -->
## Getting Started

This section provides instructions on how to set up your project locally. Follow these simple steps to get a local copy up and running:

### Prerequisites and Installation

- **IDE**: Use IntelliJ for backend and Visual Studio Code for frontend development
- **Database Tools**: MySQL Workbench and MySQL Server.


### Steps to Run the Application

Step 1: Clone the Repository
   ```sh
git clone https://github.com/rahul2011421/Talent-share-Portal-Application
   ```
Step 2: Setup IDE
   ```sh
Open the Backend folder in IntelliJ IDE.
Open the Frontend folder in Visual Studio Code IDE or if you are familiar with these IDEs, you can use the one you prefer.
   ```
   
Step 3: MySQL Connection configuration in Backend Code
   ```sh
datasource:
driver-class-name: com.mysql.cj.jdbc.Driver
url: jdbc:mysql://localhost:3306/REGISTER
username: TSP
password: TSP@123


   - REGISTER is the database name. First, create a database with this name in your MySQL.
   - Username: This is your MySQL login username. By default, it is root. Change it according to your MySQL login username.
   - Password: This is your MySQL login password. By default, it is root if you haven't changed it.

The above code snippet is located at: (Backend --> tsp-register-service --> src --> main --> resources --> application.yml)
There are four services in our application that require database configurations.
Update the database configurations for all these services:
   1. tsp-register-service
   2. tsp-user-service
   3. tsp-media-service
   4. tsp-mentorship-service
   ```

Step:4. Sequence Wise Running of Backend Services
  ```sh
1. eureka-server
2. tsp-gateway
3. notify-service
4. tsp-register-service
5. tsp-user-service
6. tsp-media-service
7. tsp-mentorship-service
8. tsp-report-service
  ```
Step:5. Steps for Frontend
   ```sh
  After opening the project in Visual Studio Code, install the dependencies before running the project. Open the terminal and run:
# =================
# | npm install    
# =================

After the dependencies are installed, you can run the application with:
# =================
# | npm run
# =================

#==================================================================================================================
#| This command will start the development server and open your default web browser to view the application.
#==================================================================================================================
   ```


<!-- KEY FEATURES -->
## Key Features

### User Management Module
- **Admin Dashboard**: Centralized management of user profiles, creation of mentors, mentees, and managers, and deactivation of inactive users.
- Admin functionalities for creating and managing user accounts, resetting passwords, and controlling profile details.

### Notification System
- **User Registration**: Automated email with login credentials.
- **Password Management**: Email notifications for password resets.
- **Session Notifications**: Notifications for session requests, approvals, and confirmations.

### Mentee Functionality
- **Mentor Search**: Mentees can search for mentors based on skills.
- **Session Scheduling**: Mentees can send session requests and schedule mentorship sessions.
- **Notifications**: Automated notifications for session requests, acceptances, and rejections.

### Manager Functionality
- **Approval Process**: Managers approve or reject mentorship requests and ensure alignment with organizational goals.
  
### Mentor Functionality
- **Session Requests**: Mentors can view and respond to mentorship session requests.
- **Profile Review**: Mentors can review mentee profiles before accepting or rejecting requests.
- **Availability**: Mentors can set their availability for scheduling sessions.



<!-- SYSTEM ARCHITECTURE -->
## System Architecture

### Backend
- Built with Java and Spring Boot.
- Consists of several microservices registered with Eureka Service for service discovery and load balancing via the API Gateway.
- Key services include Register Service, User Service, Media Service, Mentorship Service, Report Service, and Notification Service.

### Frontend
- Built with HTML, CSS, TypeScript, and React.js.
- The root component is the App Component, containing the Router for navigating between pages.




<!-- Author -->
## Author
**Rahul Yadav** - rahulyadavv2011421@gmail.com

Project Link: [https://github.com/rahul2011421/Talent-share-Portal-Application](https://github.com/rahul2011421/Talent-share-Portal-Application)

For more detailed information, please refer to the [Talent Share Portal High Level Design Document](https://github.com/rahul2011421/Talent-share-Portal-Application/blob/master/Talent%20Share%20Portal%20High%20Level%20Design%20Document.pdf).

