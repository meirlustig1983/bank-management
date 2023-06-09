# Bank Management

Bank management is a some type of sand-box repository to try a new technologies

## List of technologies

* Java JDK-17
* Gradle 7.6.1
* Spring Boot 3.0.6
* Spring Web Services
* Spring Data JPA
* Spring Web
* Spring Actuator
* Rest Repositories
* Lombok 1.18.26
* Junit
* Git CI-CD Workflow
* PostgreSQL
* Docker
* Prometheus
* Metrics
* Kubernetes
* Helm Chart
* pgAdmin

## Branches

* [BM-001: Create a new java and gradle project](https://github.com/meirlustig1983/bank-management/pull/1)
* [BM-002: Spring Boot version update to the latest](https://github.com/meirlustig1983/bank-management/pull/2)
* [BM-003: Create model layer using Data-JPA and Repositories](https://github.com/meirlustig1983/bank-management/pull/3)
* [BM-004: Use facade design pattern in order to save and find data](https://github.com/meirlustig1983/bank-management/pull/4)
* [BM-005: Add service as a middle layer between controller and DB](https://github.com/meirlustig1983/bank-management/pull/5)
* [BM-006: Add controller (change the SpringBoot version back to 3.0.6)](https://github.com/meirlustig1983/bank-management/pull/6)
* [BM-007: Add exceptions handler](https://github.com/meirlustig1983/bank-management/pull/7)
* [BM-008: Add unit tests and IT for DataFacade](https://github.com/meirlustig1983/bank-management/pull/8)
* [BM-009: Add unit tests and IT for BankAccountService](https://github.com/meirlustig1983/bank-management/pull/9)
* [BM-010: Add unit tests for BankAccountMapper and TransactionMapper](https://github.com/meirlustig1983/bank-management/pull/10)
* [BM-011: Add IT for BankAccountController](https://github.com/meirlustig1983/bank-management/pull/11)
* [BM-012: Add test suite](https://github.com/meirlustig1983/bank-management/pull/12)
* [BM-013: Optimization tests coverage](https://github.com/meirlustig1983/bank-management/pull/13)
* [BM-014: Add 'Docker' support](https://github.com/meirlustig1983/bank-management/pull/14)
* [BM-015: Use 'Version' from gradle file in dockerfile and 'Git' CI/CD workflow](https://github.com/meirlustig1983/bank-management/pull/15)
* [BM-016: Add auto increment method for 'Version' in gradle file](https://github.com/meirlustig1983/bank-management/pull/16)
* [BM-017: Add support of 'main' and 'Pull Request' Git Workflows](https://github.com/meirlustig1983/bank-management/tree/main/.github/workflows)
* [BM-018: Add support of 'Spring Actuator'](https://github.com/meirlustig1983/bank-management/pull/89)
* [BM-019: Add support of 'Prometheus'](https://github.com/meirlustig1983/bank-management/pull/91)
* [BM-021: Update Dockerfile and docker-compose file](https://github.com/meirlustig1983/bank-management/pull/93)
* [BM-022: Add 'Docker' tasks to gradle build](https://github.com/meirlustig1983/bank-management/pull/95)
* [FIX-001: GitHub Workflows fix](https://github.com/meirlustig1983/bank-management/pull/97)
* [FIX-002: GitHub Workflows fix](https://github.com/meirlustig1983/bank-management/pull/98)
* [BM-023: Adding custom metrics](https://github.com/meirlustig1983/bank-management/pull/100)
* [BM-024: Add support of 'Grafana'](https://github.com/meirlustig1983/bank-management/pull/108)
* [FIX-003: Update activate and deactivate account methods](https://github.com/meirlustig1983/bank-management/pull/110)
* [BM-025: Add Kubernetes deployment files](https://github.com/meirlustig1983/bank-management/pull/112)
* [BM-026: Add Helm Chart deployment files](https://github.com/meirlustig1983/bank-management/pull/117)
* [BM-032: Add pgAdmin](https://github.com/meirlustig1983/bank-management/pull/122)
* [BM-031: Change Kubernetes Postgres kind to StatefulSet](https://github.com/meirlustig1983/bank-management/pull/124)

## Docker Compose Example

      bank-management:
        image: mlustig/bank-management:latest
        environment:
            SPRING_PROFILES_ACTIVE: dev
            SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/postgres
            SPRING_DATASOURCE_USERNAME: postgres
            SPRING_DATASOURCE_PASSWORD: postgres
        ports:
            - "8080:8080"
        depends_on:
            - postgres
