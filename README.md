# MIMS
Micro Inventions Management System

## Spring Boot application

The active application is a Spring Boot 3 / Java 17 executable JAR with Thymeleaf pages, Spring Security, and Spring JDBC access to the original Oracle schema. The legacy JSP files remain in the repository for reference and are not packaged in the JAR.

### Oracle setup

The application does not create or alter application tables automatically. For a new Oracle schema, run [`WEB-INF/src/schema.sql`](WEB-INF/src/schema.sql). For an existing MIMS database, run [`WEB-INF/src/schema-migration-oracle.sql`](WEB-INF/src/schema-migration-oracle.sql) once as the MIMS schema owner; this adds ID sequences starting above the existing IDs.

If the migration stops with `ORA-01031: insufficient privileges`, the account connected in SQL Developer does not have permission to create sequences. Ask a DBA to grant that privilege to the MIMS schema (replace `MIMS` if your schema has a different name):

```sql
GRANT CREATE SEQUENCE TO MIMS;
```

The DBA runs the grant; reconnect to SQL Developer as the MIMS schema owner, open `WEB-INF/src/schema-migration-oracle.sql`, and choose **Run Script (F5)**. The sequences need to be owned by the same schema as the MIMS tables because the application refers to them without a schema prefix.

The connection defaults to the legacy local XE URL and `mims` username. Set the password as an environment variable rather than saving it in the repository:

```powershell
$env:MIMS_DB_PASSWORD = "your-database-password"
$env:MIMS_DB_URL = "jdbc:oracle:thin:@localhost:1521:xe"
$env:MIMS_DB_USERNAME = "mims"
```

You can also set `SERVER_PORT` (defaults to `9090`) and `MIMS_DB_POOL_SIZE` (defaults to `10`).

### Build and run

```powershell
mvn clean verify
java -jar target/mims-2.0.0-SNAPSHOT.jar
```

Open http://localhost:9090/MIMS/login. The JAR contains embedded Tomcat; deploy it as a Java process/service rather than copying it to Tomcat's `webapps` directory.

### Migration notes

- The former demo accounts have been removed. Sign in with an active account in `logindetails`.
- Existing non-inventor plaintext passwords and inventor legacy-encrypted passwords are accepted once; after successful login, the `logindetails.password` value is upgraded to BCrypt.
- New accounts and password resets use BCrypt. Keep the database `keys` column private; it contains legacy decryption material.
- Public invention requests remain available at `/MIMS/request`.
- Authenticated business endpoints are under `/MIMS/api`: domains, users, inventions, requests, reviews, queries, and opportunities. Updates are role-restricted; inventors can only manage their own inventions.
- The old schema has no database foreign-key constraints. The new services preserve its existing table and column names, including `opportuinities` and `opportuinity`.
- Oracle XE was the target schema for compatibility. Use the migration script before using create operations on an existing database.
