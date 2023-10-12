Microservices for to Online Shopping Application
We will cover :
- Service Discovery
- Centralized Configuration
- Distributed Tracing
- Event Driven Architecture
- Centralized Logging
- Circuit Breaker
- Secure Micro-service Using Key-cloak.

Video Parts -> InterProcessCommunication, Service Discovery using Netflix Eureka, Implementing Api gateway using Spring Cloud Gateway, Secure Microservices using Keycloak, Implementing Circuit Breaker, Implementing Distributed tracing, event driven architecture using Kafka, Dockerize Application and Monitoring micro services using Prometheus and Grafana.

Microservices-New is a comprehensive online shopping application project that incorporates features such as Spring Cloud Netflix Eureka, Keycloak, Spring Security, and Dockerization. It aims to provide a high-performance and scalable architecture by embracing the microservices architecture.
The project is composed of five different microservices:
1. Product-Service: Manages products within the system.
2. Order-Service: Handles order processing.
3. Inventory-Service: Tracks inventory status.
4. API-Gateway: Routes incoming requests and ensures security controls.
5. Discovery-Server: Eureka-based microservice responsible for service discovery.
This project offers a solution that caters to the fundamental functionalities required in a modern e-commerce application. Components like product management, order processing, inventory tracking, and security work seamlessly together through independent microservices.
Moreover, the project's Dockerization enables easy deployment and execution. Each microservice can be developed, deployed, and scaled independently.
The project is hosted on GitHub at https://github.com/seckinguzel/microservices-new. You can access the source code and related documentation through this link.
Microservices-New empowers developers to understand the complex business logic behind modern e-commerce applications and build similar projects using microservices architecture. With a robust infrastructure and scalability, it has the potential to deliver a flexible, high-performance, and secure online shopping experience.


Product Service
Create and view Products, acts as Product Catalog.

Order Service
Can order products

Inventory Service
Can check if product is in stock or not.

Notification Service
Can send notifications, after order is placed.

-Order Service, Inventory Service and Notification Service are going to interact with each other.
-Synchronous and Asynchronous Communication.

High Level Architecture

Logical Architecture (Each of the Services)

Inter Process Communication

Service Discovery Using Netflix Eureka

API Gateway
￼
Additional concerns will be added to the project.
-Routing based on Request Headers.
-Authentication.
-Security.
-Load Balancing.
-SSL Termination.

Circuit Breaker
￼￼
We used Resilience4j library for circuit breaker.

Distributed Tracing
￼
It helps to track the request from start to the finish.
