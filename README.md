# MIMS
Micro Inventions Management System

## Spring Boot application

The active application is a Spring Boot 3 / Java 17 executable JAR with Thymeleaf pages, Spring Security, and Spring JDBC access to PostgreSQL. The legacy JSP files remain in the repository for reference and are not packaged in the JAR.

### PostgreSQL setup

The active Spring Boot application now uses PostgreSQL (integration tests target PostgreSQL 17).
Provision a PostgreSQL database named `mims` and a dedicated login/owner, then apply
the schema once to that empty database:

```sh
psql "$MIMS_PSQL_URL" -v ON_ERROR_STOP=1 -f src/main/resources/db/schema-postgresql.sql
```

`MIMS_PSQL_URL` is a standard PostgreSQL connection URI configured securely in your
shell. The application uses a JDBC URL instead:

```sh
export MIMS_DB_URL='jdbc:postgresql://localhost:5432/mims'
export MIMS_DB_USERNAME='mims'
# Set MIMS_DB_PASSWORD through your hosting provider's secret configuration.
```

For hosted databases use the provider's TLS configuration, for example
`jdbc:postgresql://db.example.com:5432/mims?sslmode=verify-full`, with its trusted CA
configured. `SERVER_PORT` defaults to `9090`; `MIMS_DB_POOL_SIZE` defaults to `10`.
Schema initialization is deliberately manual (`spring.sql.init.mode=never`).
The application role needs table CRUD and sequence USAGE/SELECT permissions if it
is different from the schema owner.

The schema has no default login. Before first use, create matching `userdetails`
and `logindetails` rows in a transaction using the same ID from
`nextval('mims_users_seq')`. Use `usertype='Admin'`, `status='Active'`, and a BCrypt
password hash generated securely for your administrator. Do not commit passwords
or hashes. Additional users can then be created through the authenticated API.

This is a fresh-database migration: no Oracle database or data export was supplied.
Existing Oracle schema scripts and legacy JSP/bean files are retained as historical
references and are not used by Spring Boot. Oracle `NUMBER(10)` columns become
`BIGINT`, `VARCHAR2` becomes `VARCHAR`, and ID allocation uses PostgreSQL sequences.
Existing column names and string date formats are preserved. PostgreSQL preserves
empty strings instead of converting them to NULL as Oracle does.

If importing data later, map Oracle empty/NULL values deliberately and reset every
sequence above the imported IDs before enabling writes. The shared users sequence
must exceed IDs in both `userdetails` and `logindetails`. The Oracle migration SQL
must not be run against PostgreSQL.

### Verification

Run `gradle clean build` with Gradle 8.14.x, Java 17+ and Docker available. Integration tests start
an isolated PostgreSQL 17 container and apply the production PostgreSQL schema;
they cover login/password upgrade, authorization, public request insertion,
sequence allocation, and domain CRUD. No production database is used by tests.

### Build and run

Install Gradle 8.14.x and Java 17+; Maven is not required. Dependencies are
resolved from Maven Central by Gradle. Docker is required for integration tests.
A Gradle wrapper is not included, so use the installed `gradle` command.

```sh
gradle clean build
java -jar build/libs/mims-2.0.0-SNAPSHOT.jar
```

For development after configuring PostgreSQL, use `gradle bootRun`.
The existing `pom.xml` remains available for older Maven-based workflows.

Open http://localhost:9090/MIMS/login. The JAR contains embedded Tomcat; deploy it as a Java process/service rather than copying it to Tomcat's `webapps` directory.

### Migration notes

- The former demo accounts have been removed. Sign in with an active account in `logindetails`.
- Existing non-inventor plaintext passwords and inventor legacy-encrypted passwords are accepted once; after successful login, the `logindetails.password` value is upgraded to BCrypt.
- New accounts and password resets use BCrypt. Keep the database `keys` column private; it contains legacy decryption material.
- Public invention requests remain available at `/MIMS/request`.
- Authenticated business endpoints are under `/MIMS/api`: domains, users, inventions, requests, reviews, queries, and opportunities. Updates are role-restricted; inventors can only manage their own inventions.
- The old schema has no database foreign-key constraints. The new services preserve its existing table and column names, including `opportuinities` and `opportuinity`.
- Use `src/main/resources/db/schema-postgresql.sql` for new deployments.

