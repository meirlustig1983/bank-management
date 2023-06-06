# Bank Management

Bank management is a some type of sand-box repository to try a new technologies

## List of technologies

* Java JDK-17
* Gradle 7.6.1
* Spring Boot 3.0.6
* Spring Web Services
* Spring Data JPA
* Spring Web
* Rest Repositories
* Lombok 1.18.26
* Junit
* Git CI-CD
* PostgreSQL
* Docker

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
* [BM-015: Use 'Version' from gradle file in dockerfile and 'Git' CICD workflow](https://github.com/meirlustig1983/bank-management/pull/15)
* [BM-016: Add auto increment method for 'Version' in gradle file](https://github.com/meirlustig1983/bank-management/pull/16)



## Docker Compose Example

      bank-management:
        image: mlustig/bank-management:latest
        environment:
            SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/postgres
            SPRING_DATASOURCE_USERNAME: postgres
            SPRING_DATASOURCE_PASSWORD: postgres
        ports:
            - "8080:8080"
        depends_on:
            postgres:
                condition: service_healthy
