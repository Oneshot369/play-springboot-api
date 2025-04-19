# Capstone Weather App Backend
## Table of contents
- [About (start here)](#about)
- [Overview](#overview)
- [Technologies](#technologies)
- [Requirements](#requirements)
- [Documentation](#documentation)
  - [Logical Diagram](#logical-diagram)
  - [Physical Diagram](#physical-diagram)
  - [EER Diagram](#eer-diagram)
  - [UML's](#uml)
  - [API Design](#api-design)

## About
This is a project that is interconnected with another repository [The Frontend](https://github.com/Oneshot369/cap-frontend). This repository is for the backend. This is my capstone project, I have spent 100+ hours on this and is the culmination of my senior year at GCU, so please enjoy :). 
## Overview
As previously stated this is the backend, so all the documentation located here will be referenced to that. If you want to see the complete documentation for the front and backend, I suggest you visit my [project design document](https://1drv.ms/w/c/0f720a2bdf7ca902/Ect8zp6QuXVAjUKLloeDHOAB5pt0DCs30U6u0vzoaCLVQw?e=in1t7l) which holds the entirety of my documentation. 
### Running the project
If you are here to run this code yourself, please create a fork and the only text you need to change is in the application.properties files located under: `src/main/resources` (ensure that the property `cors.frontend.url` is the URL of your frontend, or your requests will be blocked).. For this go through and change your database/api credentials for your own. Then you can run the project by using `mvn clean install` and then running the java project. 
## Technologies
This table is only the technologies used in the backend. This includes languages, IDE's, API's, and AWS services for cloud hosing (cloud hosting is optional for this project, you can run this project locally with no problem).
| Name   | Justification | 
|--------|---------------|
| Visual Studio Code (1.94.0v)| Visual Studio Code is an IDE used for the development of both front and backend technologies|
| Java (17.0.9v) | Java is an object-oriented programming language used for the backend. |
|  Springboot (3.2.8v) |  Springboot will be the framework that we use to create our backend API in Java. |
|  Maven (3.9.6v) |  This is a dependency manager that we will use for the backend. |
|  OpenWeather API (2.5v) | This third-party API will be the tool we are using to gather weather information. |
| MailGun API (1.4.9v) |  This will be the third-party API we will use to send out email notifications. |
| MySQL Community (8.0.35v) | This will be the technology our database runs on for storing user information.  |
| AWS RDS (instance: db.t3.micro) | This is the AWS Service and instance class we will be using for hosting our Database.  |
| AWS Elastic Beanstalk(Corretto 17 on amazon linux 2023, platform 4.3.2) | This service will be used to host our instance of the backend on the AWS cloud. It will be on the Amazon Linux machine image. |

## Requirements
For this project I outlined 99 user stories for the requirements. As of 4/14/2025 they are all compleated with 181.5 work hours recorded to achieve all of them. For the entier list of them please visit the following document:

[Sprint backlog](https://1drv.ms/x/c/0f720a2bdf7ca902/EWoqQukujrhBr4GsGu1vBuUBKRZgiGI2Cv-mG9eX-f13pQ?e=XyQx0b)
## Documentation
As previously stated the [project design document](https://1drv.ms/w/c/0f720a2bdf7ca902/Ect8zp6QuXVAjUKLloeDHOAB5pt0DCs30U6u0vzoaCLVQw?e=in1t7l) has all of my documentation so I suggest viewing this if you want a complete overview. But in this section I will outline some notable sections of my documentation for the backend.
### Logical Diagram
![Logical diagram image](https://github.com/Oneshot369/play-springboot-api/blob/main/Share%20pics/logical.drawio.png?raw=true)
The logical solution design is meant to be our high-level view of how our software will use each other. We use an N-layered design with four layers: Presentation, Business logic, Data Access, and Data source. We have two separate apps: the frontend with Vue and the backend with springboot. While they are both in the presentation layer the Vue app is our primary presentation layer with springboot’s presentation layer just REST controllers that will be accessed by the Vue app. In our springboot app, we will have an MVC design pattern with model, views (JSON), and controllers. The Views will be what our Vue app consumes to get data from our springboot app.
### Physical Diagram
![Physical diagram image](https://github.com/Oneshot369/play-springboot-api/blob/main/Share%20pics/Physical.drawio.png?raw=true)
The Physical solution Design is for our hosting architecture we have three items we need to host a database, our API, and our frontend. We are using AWS for our hosting and CI/CD needs. For the database we will be using the Amazon RDS, the API will be hosted on Elastic Beanstalk, and finally, the frontend will be hosted in AWS Amplify. As for our CI/CD needs, AWS Amplify automatically handles this for us, and for the API we are using CodeBuild with a .yml file to build our code and CodePipeline is for managing the deployment. 
### EER Diagram
The users table has a ONE-TO-MANY relation to the locations table, and the location table has a ONE-TO-MANY relation to the constraints tables. In other words, one user can have many locations and one location has many constraints. The locations cannot exist without a user and a constraint cannot exist without a constraint. Each relationship has the rule that on delete it CASCADES. This means if you delete a user all locations get deleted for that user, and if you delete a location all constraints get deleted as well.

To copy a working version of the database please view the sql file in: `Share pics/Dump20241101.sql`
### UML
![UML picture](https://github.com/Oneshot369/play-springboot-api/blob/main/Share%20pics/ClassUML.drawio.png?raw=true)
We have three broad categories of UMLs: API controllers/services, Weather Models, and User Models / Entities. The API controller/services are for the handling of requests, emails, and calling the third-party APIs. The weather models will be the responses we get from our weather API, and these are what we then send to the frontend encased in a response object. Finally, the User Models and Entities are for the user's database CRUD and returning user data. 
### API Design
The API is documented using swagger documentation. This is generated dynamically by including swagger annotations in our controller classes to document each endpoint. There are three main groups of endpoints: user endpoints, weather endpoints, and base controllers. The base controller is very simplistic and only really provides a health check endpoint and an authorization test endpoint. Weather endpoints return weather data based on latitude and longitude. Finally, the user endpoints provide CRUD operations for our users plus a login feature.

To access this run the project then access the following endpoint: `{your URL here}:8080/swagger-ui/index.html#/`


## Frontend
To explore how this application interacts with its frontend counterpart, view the frontend repository [HERE](https://github.com/Oneshot369/cap-frontend). 


