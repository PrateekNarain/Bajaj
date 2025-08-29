# Salary Finder (Java + H2)

This tiny Java program answers the question: *Find the highest salary credited **not** on the 1st day of any month and output the Salary, Employee Name, Age, and Department.*

It uses an in-memory H2 database so you don't need to install anything else. All tables and sample data (from the assignment PDF) are created on startup.

## Prerequisites
- Java 17+
- Maven 3.8+

## Build
```bash
mvn -v
mvn -q -e -DskipTests package
```
This produces a fat JAR at `target/salary-finder-1.0.0-shaded.jar`.

## Run
```bash
java -jar target/salary-finder-1.0.0-shaded.jar
```
Expected output (as of today's date) given the provided sample data:
```
SALARY,NAME,AGE,DEPARTMENT_NAME
74998.00,Emily Brown,32,Sales
```
> Age is computed at runtime from DOB, so it changes over time.

## Files
- `src/main/java/com/example/SalaryFinder.java` – all Java code (creates schema & data, runs the SQL, prints result)
- `pom.xml` – dependencies and build config

## Notes
- If you want to run against a real DB (MySQL/PostgreSQL), swap the JDBC URL/driver and reuse the same SQL (you may replace the `EXTRACT(DAY FROM ...)` with the database-specific function if needed).

