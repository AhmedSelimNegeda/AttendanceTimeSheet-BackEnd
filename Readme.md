# Spring Boot, MySQL, JPA, Hibernate Rest API Tutorial

Build Restful CRUD API for a simple Tkafol-Taking application using Spring Boot, Mysql, JPA and Hibernate.

## Requirements

1. Java - 1.8.x

2. Maven - 3.x.x

3. Mysql - 5.x.x

## Steps to Setup

**1. Clone the application**

```bash
git clone https://github.com/mohamed0123/TkafolBackend.git
```

**2. Create Mysql database**
```bash
create database tkafol
```

**3. Change mysql username and password as per your installation**

+ open `src/main/resources/application.properties`

+ change `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

**4. Build and run the app using maven**

```bash
mvn package -Dmaven.test.skip=true
java -jar target/tkafol-1.0.0.jar
```

Alternatively, you can run the app without packaging it using -

```bash
mvn spring-boot:run
```

The app will start running at <http://localhost:8080>."# AttendanceTimeSheet-BackEnd" 
