# Product Service

A production-style Spring Boot Product Catalog REST API designed as a hands-on **DevOps / Cloud Platform Engineering practice project**.

The application is intentionally simple from a business perspective, while the surrounding engineering architecture provides realistic opportunities to practice:

* Java / Spring Boot application deployment
* Maven builds
* Unit and integration testing
* PostgreSQL
* Testcontainers
* Docker
* GitHub Actions
* AWS IAM / OIDC
* Amazon ECR
* AWS RDS
* Kubernetes / Amazon EKS
* Helm
* Terraform
* Observability
* CI/CD
* Platform Engineering patterns

---

# 1. Project Objective

The objective of this project is to simulate how a Platform Engineer would build and operate a Spring Boot application from source code through production deployment.

The application developer is responsible for the Spring Boot application.

The DevOps / Platform Engineer is responsible for:

```text
Source Code
    ↓
Build
    ↓
Test
    ↓
Docker Image
    ↓
Amazon ECR
    ↓
Kubernetes / EKS
    ↓
AWS RDS
    ↓
Monitoring & Observability
```

The project is designed to progressively introduce production-grade DevOps practices around a realistic Java application.

---

# 2. Setup Prerequisites

Before building or running the Product Service, install and configure the following tools.

## 2.1 Required Software

| Tool           | Required Version | Purpose                                       |
| -------------- | ---------------- | --------------------------------------------- |
| Java           | 21               | Run and build the Spring Boot application     |
| Maven          | 3.9.16           | Build and test the application                |
| PostgreSQL     | 18.x             | Local application database                    |
| Docker Desktop | Latest/current   | Required for Docker builds and Testcontainers |
| Git            | Latest/current   | Source code management                        |
| GitHub CLI     | Optional         | GitHub repository management                  |
| AWS CLI        | v2               | AWS resource management                       |
| AWS Account    | Required         | ECR and future AWS infrastructure             |
| GitHub Account | Required         | Repository and GitHub Actions                 |

---

## 2.2 Java 21

The application requires Java 21.

Verify:

```bash
java -version
```

Expected:

```text
openjdk version "21.x.x"
```

The project is compiled using Java 21.

---

## 2.3 Maven 3.9.16

Verify:

```bash
mvn -version
```

Expected:

```text
Apache Maven 3.9.16
Java version: 21
```

Maven is used for:

```text
Compile
Test
Package
Dependency Management
```

Build the application:

```bash
mvn clean package
```

---

## 2.4 PostgreSQL 18

PostgreSQL is required for running the application locally.

Verify:

```bash
psql --version
```

Expected:

```text
psql (PostgreSQL) 18.x
```

Verify that PostgreSQL is running:

```bash
pg_isready
```

Expected:

```text
accepting connections
```

---

## 2.5 Local PostgreSQL Database

Create the application database:

```sql
CREATE DATABASE productdb;
```

Create the application user:

```sql
CREATE USER productuser WITH PASSWORD 'CHANGE_ME';
```

Grant access:

```sql
GRANT ALL PRIVILEGES ON DATABASE productdb TO productuser;
```

Connect to the database:

```bash
psql -h localhost -U productuser -d productdb
```

The application expects the following default configuration:

```text
Database: productdb
Username: productuser
Host: localhost
Port: 5432
```

The password should be provided through an environment variable/Secrets rather than committed to Git.

Example:

```bash
export DB_PASSWORD='your-password'
```
or

For CI Pipeline keep password in GitHub Secret

---

## 2.6 Docker Desktop

Docker Desktop is required for:

* Building application images
* Running containers
* Running Testcontainers integration tests

Verify Docker:

```bash
docker --version
```

Verify Docker is running:

```bash
docker info
```

The following command should also work:

```bash
docker run --rm hello-world
```

### Important

You do **not** need to manually start PostgreSQL or Ryuk containers for integration tests.

Testcontainers manages them automatically.

The integration test starts:

```text
Testcontainers
      |
      +--> PostgreSQL container
      |
      +--> Ryuk cleanup container
```

---

## 2.7 Testcontainers Requirements

Testcontainers requires access to a working Docker environment.

Before running integration tests, verify:

```bash
docker info
```

Then run:

```bash
mvn clean test
```

The integration test will automatically start a temporary PostgreSQL container.

You should see output similar to:

```text
Creating container for image: postgres:18
Container postgres:18 is starting
Container postgres:18 started
```

The test should finish with:

```text
Tests run: 2
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

---

## 2.8 Git

Verify:

```bash
git --version
```

Clone the project:

```bash
git clone <repository-url>
cd product-service
```

Check the repository:

```bash
git status
```

---

## 2.9 AWS CLI

AWS CLI is required for local AWS administration and troubleshooting.

Verify:

```bash
aws --version
```

Expected:

```text
aws-cli/2.x.x
```

Configure credentials if required for local AWS operations:

```bash
aws configure
```

Verify access:

```bash
aws sts get-caller-identity
```

For GitHub Actions, the project uses **GitHub OIDC** rather than storing long-lived AWS access keys.

---

## 2.10 AWS Requirements

The AWS environment used by the project requires:

```text
AWS Account
Region: ap-south-1
Amazon ECR repository: product-service
```

The current ECR repository is:

```text
product-service
```

The application image is pushed to Amazon ECR during CI.

The GitHub Actions workflow requires an IAM role that GitHub can assume through OIDC.

The role must trust the intended GitHub repository and provide the required ECR permissions.

---

## 2.11 GitHub Requirements

The project requires:

* GitHub repository
* GitHub Actions enabled
* GitHub Actions OIDC enabled through workflow permissions
* AWS IAM OIDC trust relationship
* IAM role for GitHub Actions
* Amazon ECR repository

The workflow uses:

```yaml
permissions:
  id-token: write
  contents: read
```

This allows GitHub Actions to request an OIDC token and authenticate with AWS.

---

## 2.12 Verify the Complete Local Environment

Run the following commands before starting development:

```bash
java -version
```

```bash
mvn -version
```

```bash
psql --version
```

```bash
docker --version
```

```bash
docker info
```

```bash
git --version
```

```bash
aws --version
```

Then verify PostgreSQL:

```bash
pg_isready
```

Finally run the complete test suite:

```bash
mvn clean test
```

---

## 2.13 Expected Environment

The complete local development environment should look like:

```text
Developer Machine
│
├── Java 21
│
├── Maven 3.9.16
│
├── PostgreSQL 18
│   └── productdb
│
├── Docker Desktop
│   └── Testcontainers
│       └── PostgreSQL 18
│
├── Git
│
└── AWS CLI
```

Application runtime:

```text
Spring Boot
     |
     v
localhost:8080
     |
     v
Local PostgreSQL
localhost:5432
```

Integration testing:

```text
Maven
  |
  v
JUnit
  |
  v
Testcontainers
  |
  v
Temporary PostgreSQL 18
```

---

## 2.14 Quick Prerequisites Checklist

Before starting the project, confirm:

* [ ] Java 21 installed
* [ ] Maven 3.9.16 installed
* [ ] PostgreSQL 18 installed
* [ ] `productdb` database created
* [ ] `productuser` created
* [ ] PostgreSQL running on port `5432`
* [ ] Docker Desktop installed
* [ ] Docker daemon running
* [ ] `docker run --rm hello-world` works
* [ ] Git installed
* [ ] AWS CLI v2 installed
* [ ] AWS account available
* [ ] ECR repository `product-service` available
* [ ] GitHub repository created
* [ ] GitHub Actions enabled
* [ ] AWS IAM OIDC trust configured
* [ ] GitHub Actions IAM role configured

Once these prerequisites are satisfied, the project can be built and tested with:

```bash
mvn clean test
```

and the application can be started with:

```bash
mvn spring-boot:run
```


# 3. Application Overview

The application is a **Product Catalog API**.

Application name:

```text
product-service
```

Technology stack:

| Component           | Technology                  |
| ------------------- | --------------------------- |
| Language            | Java 21                     |
| Framework           | Spring Boot 3.5.5           |
| Build Tool          | Maven 3.9.16                |
| API                 | REST                        |
| Database            | PostgreSQL                  |
| ORM                 | Spring Data JPA / Hibernate |
| Validation          | Jakarta Validation          |
| Testing             | JUnit 5 / Spring Boot Test  |
| Integration Testing | Testcontainers              |
| Containerization    | Docker                      |
| CI/CD               | GitHub Actions              |
| Container Registry  | Amazon ECR                  |
| Cloud               | AWS                         |
| Production Database | AWS RDS PostgreSQL          |
| Target Platform     | Amazon EKS                  |

---

# 4. High-Level Architecture

The application follows a standard layered Spring Boot architecture.

```text
                    Client
                      |
                      v
             REST API / Controller
                      |
                      v
                 Service Layer
                      |
                      v
               Repository Layer
                      |
                      v
                  PostgreSQL
```

The complete deployment architecture is intended to become:

```text
                         Internet / Client
                                |
                                v
                       Load Balancer / ALB
                                |
                                v
                         Amazon EKS
                                |
                    +-----------+-----------+
                    |                       |
                    v                       v
             product-service           Kubernetes
                Pod(s)                  Services
                    |
                    v
                AWS RDS
               PostgreSQL
```

CI/CD:

```text
Developer
    |
    v
GitHub Repository
    |
    v
GitHub Actions
    |
    +--> Maven Build
    |
    +--> Unit/Slice Tests
    |
    +--> Integration Tests
    |       |
    |       v
    |   Testcontainers
    |       |
    |       v
    |   PostgreSQL
    |
    +--> Docker Build
    |
    v
Amazon ECR
    |
    v
Amazon EKS
```

---

# 5. Repository Structure

The current Java application follows a standard Maven project structure.

```text
product-service/
│
├── .github/
│   └── workflows/
│       └── ci.yaml
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── productservice/
│   │   │               ├── ProductServiceApplication.java
│   │   │               │
│   │   │               ├── controller/
│   │   │               │   └── ProductController.java
│   │   │               │
│   │   │               ├── service/
│   │   │               │   └── ProductService.java
│   │   │               │
│   │   │               ├── repository/
│   │   │               │   └── ProductRepository.java
│   │   │               │
│   │   │               ├── entity/
│   │   │               │   └── Product.java
│   │   │               │
│   │   │               └── dto/
│   │   │                   └── ProductResponse.java
│   │   │
│   │   └── resources/
│   │       └── application.yml
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── productservice/
│                       ├── ProductControllerTest.java
│                       └── ProductServiceApplicationTests.java
│
├── Dockerfile
├── pom.xml
├── .gitignore
└── README.md
```

---

# 6. Java Application Design

The application uses a layered architecture.

## Controller

The controller exposes REST endpoints.

```text
HTTP Request
     |
     v
ProductController
```

The controller should be responsible for:

* HTTP request handling
* HTTP response handling
* Request validation
* Calling the service layer

It should not contain database logic.

---

# 7. Service Layer

The service layer contains application/business logic.

```text
ProductController
       |
       v
ProductService
       |
       v
ProductRepository
```

The service layer provides a separation between the REST API and database access.

This makes the application easier to:

* test
* maintain
* extend
* refactor

---

# 8. Repository Layer

The repository layer uses Spring Data JPA.

```text
ProductRepository
        |
        v
Spring Data JPA
        |
        v
Hibernate
        |
        v
PostgreSQL
```

The repository is responsible for persistence operations.

---

# 9. Database

The application uses PostgreSQL.

Local development:

```text
PostgreSQL
localhost:5432
```

Database:

```text
productdb
```

Application configuration uses environment variables:

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/productdb}
    username: ${DB_USERNAME:productuser}
    password: ${DB_PASSWORD:CHANGE_ME}
```

This allows the same application image to be used in different environments without changing the application code.

---

# 10. Configuration Strategy

The application follows environment-variable-based configuration.

For example:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
SERVER_PORT
```

Local:

```text
DB_URL=jdbc:postgresql://localhost:5432/productdb
```

CI integration tests:

```text
Testcontainers dynamically provides the database connection.
```

Production:

```text
DB_URL=<AWS RDS endpoint>
DB_USERNAME=<configured username>
DB_PASSWORD=<secret>
```

The goal is to keep environment-specific configuration outside the application image.

---

# 11. REST API

The primary API endpoint is:

```text
GET /api/products
```

Example:

```bash
curl http://localhost:8080/api/products
```

Example response:

```json
[
  {
    "id": 1,
    "name": "MacBook Air",
    "description": "Apple laptop",
    "price": 1199.99,
    "quantity": 10
  }
]
```

---

# 12. Actuator and Application Health

Spring Boot Actuator is enabled.

Exposed endpoints include:

```text
/actuator/health
/actuator/info
/actuator/metrics
/actuator/prometheus
```

Health probes are enabled for Kubernetes-oriented deployment.

The application therefore has the foundation for:

```text
Kubernetes
     |
     +--> Liveness Probe
     |
     +--> Readiness Probe
```

---

# 13. Maven Build

The project uses Maven.

Check Maven:

```bash
mvn -version
```

Build the application:

```bash
mvn clean package
```

Run tests:

```bash
mvn clean test
```

Skip tests when specifically required:

```bash
mvn package -DskipTests
```

Tests should normally be executed before producing the deployment artifact.

---

# 14. Testing Strategy

The project separates tests into two categories.

## 14.1 Controller / Slice Test

File:

```text
ProductControllerTest.java
```

This test uses:

```java
@WebMvcTest(ProductController.class)
```

The service layer is mocked.

Therefore:

```text
Controller Test
      |
      +--> Controller
      |
      +--> Mock Service
      |
      X
      |
   PostgreSQL
```

No real database is required.

This test is fast and focuses on HTTP/controller behavior.

---

# 15. Integration Test

File:

```text
ProductServiceApplicationTests.java
```

The integration test uses:

```java
@SpringBootTest
```

and:

```java
@Testcontainers
```

A PostgreSQL container is defined:

```java
@Container
static PostgreSQLContainer<?> postgres =
    new PostgreSQLContainer<>("postgres:18")
        .withDatabaseName("productdb")
        .withUsername("productuser")
        .withPassword("testpassword");
```

The database properties are dynamically injected:

```java
@DynamicPropertySource
static void configureDatabase(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
}
```

---

# 16. Why Testcontainers?

The application already has PostgreSQL installed locally.

That PostgreSQL instance is used when running the application.

Testcontainers serves a different purpose.

```text
Local PostgreSQL
       |
       +--> Run application locally


Testcontainers PostgreSQL
       |
       +--> Run integration tests
```

The integration test should not depend on:

* a developer's local database
* an external database
* a shared CI database
* manually created database infrastructure

Instead, the test creates its own temporary PostgreSQL environment.

---

# 17. Testcontainers Lifecycle

When the integration test starts:

```text
JUnit
  |
  v
Testcontainers
  |
  +--> Start PostgreSQL
  |
  +--> Start application context
  |
  +--> Connect to PostgreSQL
  |
  +--> Execute test
  |
  +--> Stop/remove containers
```

Testcontainers also starts its Ryuk resource-reaper container.

Ryuk is responsible for cleaning up Testcontainers resources.

The recent successful test confirmed:

```text
Testcontainers 1.21.3
Docker Server 29.8.0
PostgreSQL 18
```

and successfully created both the Ryuk and PostgreSQL containers.

---

# 18. Current Test Result

The integration test successfully:

1. Detected Docker.
2. Started Ryuk.
3. Started PostgreSQL 18.
4. Created the temporary database.
5. Started the Spring Boot application context.
6. Initialized Hibernate/JPA.
7. Established a database connection.
8. Completed the integration test.

The final result was:

```text
Tests run: 2
Failures: 0
Errors: 0
Skipped: 0

BUILD SUCCESS
```

This confirms that the current integration-test architecture is working.

---

# 19. CI Testing Strategy

GitHub Actions does not need a separately configured PostgreSQL service.

The CI pipeline executes:

```yaml
- name: Run tests
  run: mvn clean test --batch-mode
```

The integration test itself starts PostgreSQL through Testcontainers.

Therefore:

```text
GitHub Actions Runner
        |
        v
      Maven
        |
        v
      JUnit
        |
        v
   Testcontainers
        |
        v
  PostgreSQL Container
```

This makes the test environment self-contained.

---

# 20. GitHub Actions CI

The CI pipeline performs:

```text
Checkout
   ↓
AWS Authentication
   ↓
ECR Login
   ↓
Java Setup
   ↓
Maven Setup
   ↓
Run Tests
   ↓
Build Application
   ↓
Docker Build
   ↓
Push Docker Image
```

Current workflow structure:

```yaml
name: Product Service CI

on:
  workflow_dispatch:
    inputs:
      image_tag:
        description: "Docker image tag"
        default: "1.0"
        required: true
        type: string

permissions:
  id-token: write
  contents: read

jobs:
  build:
    runs-on: ubuntu-latest

    steps:

      - name: Checkout code
        uses: actions/checkout@v4

      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v4
        with:
          role-to-assume: arn:aws:iam::897722700244:role/gp-terraform-execution
          aws-region: ap-south-1

      - name: Login to Amazon ECR
        id: login-ecr
        uses: aws-actions/amazon-ecr-login@v2

      - name: Setup Java
        uses: actions/setup-java@v6
        with:
          distribution: temurin
          java-version: '21'
          cache: maven

      - name: Set up Maven
        uses: stCarolas/setup-maven@v5
        with:
          maven-version: 3.9.16

      - name: Check versions
        run: |
          java -version
          mvn -version

      - name: Run tests
        run: mvn clean test --batch-mode

      - name: Upload test reports
        if: failure()
        uses: actions/upload-artifact@v4
        with:
          name: surefire-reports
          path: '**/target/surefire-reports/'

      - name: Build the application
        run: mvn package -DskipTests --batch-mode

      - name: Build, tag, and push Docker image
        env:
          REGISTRY: ${{ steps.login-ecr.outputs.registry }}
          REPOSITORY: product-service
          IMAGE_TAG: ${{ inputs.image_tag }}
        run: |
          docker build -t $REGISTRY/$REPOSITORY:$IMAGE_TAG .
          docker push $REGISTRY/$REPOSITORY:$IMAGE_TAG
```

---

# 21. Why PostgreSQL Is Not Defined as a CI Service

We intentionally do not use:

```yaml
services:
  postgres:
    image: postgres:18
```

for this integration test.

Instead:

```text
GitHub Actions
      |
      v
Maven
      |
      v
Testcontainers
      |
      v
PostgreSQL
```

Advantages:

* Test owns its database lifecycle.
* No shared database.
* No hard-coded database port.
* No CI-specific database configuration.
* Same integration test can run locally and in CI.
* Database is automatically cleaned up.
* Tests are more reproducible.

---

# 22. Docker

The application is packaged as a Docker image.

The image is built after successful tests.

```text
Source Code
    |
    v
Maven Build
    |
    v
JAR
    |
    v
Docker Build
    |
    v
product-service:<tag>
```

The image is pushed to:

```text
Amazon ECR
```

Repository:

```text
product-service
```

AWS Region:

```text
ap-south-1
```

---

# 23. Amazon ECR

The CI workflow authenticates to ECR using:

```yaml
uses: aws-actions/amazon-ecr-login@v2
```

The image is tagged using:

```text
<registry>/product-service:<image_tag>
```

Example:

```text
897722700244.dkr.ecr.ap-south-1.amazonaws.com/product-service:1.0
```

The image is then pushed to ECR.

---

# 24. GitHub Actions AWS Authentication

The workflow uses GitHub Actions OIDC:

```yaml
permissions:
  id-token: write
  contents: read
```

AWS credentials are obtained through:

```yaml
aws-actions/configure-aws-credentials
```

The workflow assumes an AWS IAM role.

The intended security model is:

```text
GitHub Actions
      |
      | OIDC
      v
AWS IAM
      |
      v
Assume Role
      |
      v
AWS Resources
```

This avoids storing long-lived AWS access keys inside GitHub Secrets.

The IAM trust policy should restrict access to the intended GitHub repository.

For a production implementation, a dedicated CI/CD IAM role with least-privilege permissions should be preferred over a broad infrastructure execution role.

---

# 25. CI Failure Previously Encountered

The first integration test execution failed because the Spring Boot application attempted to connect to the default local PostgreSQL configuration:

```text
localhost:5432
```

The GitHub Actions runner did not have the expected PostgreSQL database.

This produced a Hibernate datasource/dialect initialization failure.

Instead of adding a permanent PostgreSQL server to CI, the project was changed to use Testcontainers.

This solved the architectural problem by making the integration test responsible for creating its own database.

---

# 26. Docker / Testcontainers Issue Previously Encountered

During local Testcontainers setup, Docker Desktop was running but Testcontainers initially could not establish a valid Docker environment.

The issue involved Docker API/client compatibility.

The environment was adjusted so that Testcontainers could successfully communicate with Docker Desktop.

The final successful execution confirmed:

```text
Found Docker environment with Docker accessed via Unix socket
Server Version: 29.8.0
API Version: 1.56
```

After this was resolved, PostgreSQL and Ryuk were successfully started by Testcontainers.

---

# 27. Development Environment

The local development environment is:

```text
macOS
Apple Silicon / ARM64
Docker Desktop
Java 21
Maven 3.9.16
PostgreSQL 18
```

Application runtime:

```text
Spring Boot
     |
     v
localhost:8080
     |
     v
localhost:5432
```

Integration tests:

```text
Spring Boot Test
      |
      v
Testcontainers
      |
      v
PostgreSQL container
```

---

# 28. Running the Application Locally

Start PostgreSQL locally.

Create/use:

```text
Database: productdb
Username: productuser
```

Then run:

```bash
mvn spring-boot:run
```

Test:

```bash
curl http://localhost:8080/api/products
```

---

# 29. Running Tests Locally

Run all tests:

```bash
mvn clean test
```

The test execution should create a temporary PostgreSQL container for the integration test.

No manual PostgreSQL container needs to be created for the integration test.

---

# 30. Test Separation

The project currently has:

```text
src/test/java/
│
├── ProductControllerTest.java
│
└── ProductServiceApplicationTests.java
```

Responsibilities:

### ProductControllerTest

Tests:

```text
HTTP endpoint
Controller
Response
```

Database:

```text
Not required
```

### ProductServiceApplicationTests

Tests:

```text
Spring Boot application context
JPA
Hibernate
Datasource
PostgreSQL connectivity
```

Database:

```text
Testcontainers PostgreSQL
```

---

# 31. Production Database Architecture

The production environment should not use Testcontainers.

Production should use:

```text
Amazon EKS
     |
     v
product-service
     |
     v
AWS RDS PostgreSQL
```

The RDS endpoint should be supplied through environment configuration.

Credentials should not be hard-coded.

A future production architecture can use:

```text
AWS Secrets Manager
        |
        v
Kubernetes
        |
        v
Environment Variables / Secret
        |
        v
Spring Boot
```

---

# 32. Target Kubernetes Architecture

The next deployment stage is Amazon EKS.

Expected architecture:

```text
                    Route 53
                       |
                       v
                     ALB
                       |
                       v
                Kubernetes Service
                       |
                       v
              product-service Pods
                 /          \
                /            \
               v              v
          Pod Replica 1   Pod Replica 2
                |
                v
          AWS RDS PostgreSQL
```

Kubernetes should manage:

* Pod replicas
* Service discovery
* Health probes
* Rolling deployments
* Resource requests/limits
* Secrets/configuration
* Horizontal scaling

---

# 33. Observability Roadmap

The application already exposes Spring Boot Actuator endpoints.

The platform engineering layer can later add:

```text
Spring Boot
     |
     +--> Actuator
     |
     +--> Prometheus metrics
     |
     +--> OpenTelemetry
     |
     +--> Logs
     |
     +--> Traces
```

Potential AWS observability stack:

```text
Prometheus
    |
    v
Amazon Managed Service for Prometheus

Grafana
    |
    v
Amazon Managed Grafana

OpenTelemetry
    |
    v
Tracing / Metrics

CloudWatch
    |
    v
Application Logs
```

---

# 34. Future CI/CD Pipeline

The current pipeline builds and pushes the application image.

The target pipeline is:

```text
Developer Push
      |
      v
GitHub
      |
      v
GitHub Actions
      |
      +--> Compile
      |
      +--> Unit Tests
      |
      +--> Integration Tests
      |        |
      |        v
      |   Testcontainers
      |        |
      |        v
      |   PostgreSQL
      |
      +--> Package JAR
      |
      +--> Docker Build
      |
      +--> Security Scan
      |
      +--> Push to ECR
      |
      v
   Deployment
      |
      v
     EKS
      |
      v
   Health Check
```

---

# 35. Platform Engineering Learning Objectives

This project is designed to provide hands-on practice with:

## Application Platform

* Java
* Spring Boot
* Maven
* REST APIs
* PostgreSQL
* JPA/Hibernate

## Testing

* JUnit
* Spring Boot testing
* MVC slice testing
* Integration testing
* Testcontainers

## Containers

* Docker
* Dockerfiles
* Image tagging
* Image registries
* Amazon ECR

## CI/CD

* GitHub Actions
* Maven caching
* Automated testing
* Docker builds
* ECR authentication
* AWS OIDC

## AWS

* IAM
* OIDC
* ECR
* RDS
* VPC
* Secrets Manager
* CloudWatch

## Kubernetes

* EKS
* Deployments
* Services
* Ingress
* ConfigMaps
* Secrets
* Probes
* Resource requests/limits
* Autoscaling

## Infrastructure as Code

* Terraform
* Reusable modules
* AWS infrastructure provisioning

## Observability

* Prometheus
* Grafana
* OpenTelemetry
* CloudWatch
* Distributed tracing

---

# 36. Current Project Status

| Component                | Status     |
| ------------------------ | ---------- |
| Spring Boot application  | Complete   |
| REST API                 | Complete   |
| PostgreSQL integration   | Complete   |
| Local development        | Working    |
| Controller test          | Working    |
| Integration test         | Working    |
| Testcontainers           | Working    |
| Docker                   | Working    |
| GitHub Actions           | Working    |
| AWS OIDC                 | Configured |
| ECR push                 | Configured |
| Kubernetes deployment    | Next stage |
| AWS RDS deployment       | Next stage |
| Helm                     | Next stage |
| Terraform infrastructure | Next stage |
| Observability            | Next stage |
| Production CI/CD         | Next stage |

---

# 37. Recommended Next Steps

The project should now progress in this order:

```text
1. Spring Boot Application
        ↓
2. Unit / Slice Tests
        ↓
3. Integration Tests + Testcontainers
        ↓
4. Docker Image
        ↓
5. GitHub Actions CI
        ↓
6. Amazon ECR
        ↓
7. Terraform AWS Infrastructure
        ↓
8. AWS RDS PostgreSQL
        ↓
9. Amazon EKS
        ↓
10. Helm Deployment
        ↓
11. GitHub Actions CD
        ↓
12. Health Checks
        ↓
13. Prometheus / Grafana
        ↓
14. OpenTelemetry
        ↓
15. Security Scanning
        ↓
16. Production-grade Platform
```

---

# 38. Key Design Principle

The most important architectural principle in this project is:

> **The application should not depend on the environment in which it runs.**

The same application artifact should work across:

```text
Developer Laptop
       |
       v
GitHub Actions
       |
       v
Docker
       |
       v
Kubernetes
       |
       v
Production
```

Environment-specific infrastructure should be provided externally through:

* Environment variables
* Kubernetes configuration
* AWS services
* Secrets
* Infrastructure as Code

The application remains portable while the platform provides the environment it needs.
