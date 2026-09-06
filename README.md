# Dog Registry API

A fully functional, reflection-free RESTful API for interacting with a police force dog registration registry. Built using **Java 25**, the **Micronaut 5.x Framework**, **Micronaut Data JDBC**, and **MapStruct**.

---

## Project Architecture & Layout

```text
com.polarissoftware.dogregistry/
│
├── controller/         # REST API Routing Endpoints & JSON Conversion Handlers
├── dto/                # Data Transfer Objects (Records) for API Inputs/Outputs
├── mapper/             # MapStruct Component Definitions for Entity <-> DTO bindings
├── model/              # Database-Backed Mapped Entities & Enumerations
└── repository/         # Micronaut Data JDBC Repositories (Pageable & Query Expressions)
```

---

## Design Assumptions

The following design decisions and assumptions were explicitly implemented:

1. **Supplier as Free Text** The supplier field is implemented as a free text input. If a separate table of suppliers is needed, this can be implemented with foreign key linking.
2. **Badge ID Uniqueness**: This is assumed not to be globally unique. If uniqueness is required in a production environment, this would typically be done via partial unique database indices (`CREATE UNIQUE INDEX idx_dog_badge_active ON dogs(badge_id) WHERE deleted = FALSE;`).
3. **Future Date Rejection**: Birth date, date acquired, and leaving date are all required to be present or past dates.
4. **Payload Exception Formatting**: Standard out-of-the-box Micronaut framework error wrappers are left intact for handling invalid enum conversions (e.g., passing 'Male1'). In a production system, these internal package descriptors should be hidden with some handler, which was omitted here to prioritize core business logic within the time box.
5. **Metadata Endpoints**: Certain fields accept enumerated values, such as gender, status and leaving reasons. Read-only endpoints are provided to list all valid values of these fields (e.g. `GET /api/dogs/metadata/statuses`)
6. **In Memory Database**: For demonstration purposes, this application utilises H2 in memory database storage. This should be changed to a real persistent database in production.

---

## Execution Instructions

### Run the Application Locally
```bash
./gradlew run
```

### Execute the Integrated Validation Test Suite
```bash
./gradlew test
```

### Sample Endpoint Matrix
* **Create Dog**: `POST /api/dogs`
* **List Dogs with Pagination**: `GET /api/dogs/dogs?page=0&size=10`
* **Search Dogs**: `GET /api/dogs/dogs?filter=%7B%22breed%22%3A%22Labrador%22%7D`
* **Get Single Dog**: `GET /api/dogs/{id}`
* **Update Dog**: `PUT /api/dogs/{id}`
* **Soft Delete Dog**: `DELETE /api/dogs/{id}`
* **List Valid Dog Statuses**: `GET /api/dogs/metadata/statuses`
* **List Valid Gender Options**: `GET /api/dogs/metadata/genders`
* **List Valid Leaving Reasons**: `GET /api/dogs/metadata/leaving-reasons`
