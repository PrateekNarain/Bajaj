
package com.example;

import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class SalaryFinder {
    public static void main(String[] args) {
        String url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);
            try (Statement st = conn.createStatement()) {
                // Create tables
                st.execute("CREATE TABLE DEPARTMENT (DEPARTMENT_ID INT PRIMARY KEY, DEPARTMENT_NAME VARCHAR(100));");
                st.execute("CREATE TABLE EMPLOYEE (" +
                        "EMP_ID INT PRIMARY KEY, FIRST_NAME VARCHAR(100), LAST_NAME VARCHAR(100), " +
                        "DOB DATE, GENDER VARCHAR(10), DEPARTMENT INT, " +
                        "FOREIGN KEY (DEPARTMENT) REFERENCES DEPARTMENT(DEPARTMENT_ID));");
                st.execute("CREATE TABLE PAYMENTS (" +
                        "PAYMENT_ID INT PRIMARY KEY, EMP_ID INT, AMOUNT DECIMAL(12,2), PAYMENT_TIME TIMESTAMP, " +
                        "FOREIGN KEY (EMP_ID) REFERENCES EMPLOYEE(EMP_ID));");

                // Insert DEPARTMENT data
                st.addBatch("INSERT INTO DEPARTMENT VALUES (1,'HR');");
                st.addBatch("INSERT INTO DEPARTMENT VALUES (2,'Finance');");
                st.addBatch("INSERT INTO DEPARTMENT VALUES (3,'Engineering');");
                st.addBatch("INSERT INTO DEPARTMENT VALUES (4,'Sales');");
                st.addBatch("INSERT INTO DEPARTMENT VALUES (5,'Marketing');");
                st.addBatch("INSERT INTO DEPARTMENT VALUES (6,'IT');");
                st.executeBatch();
                st.clearBatch();

                // Insert EMPLOYEE data
                st.addBatch("INSERT INTO EMPLOYEE VALUES (1,'John','Williams','1980-05-15','Male',3);");
                st.addBatch("INSERT INTO EMPLOYEE VALUES (2,'Sarah','Johnson','1990-07-20','Female',2);");
                st.addBatch("INSERT INTO EMPLOYEE VALUES (3,'Michael','Smith','1985-02-10','Male',3);");
                st.addBatch("INSERT INTO EMPLOYEE VALUES (4,'Emily','Brown','1992-11-30','Female',4);");
                st.addBatch("INSERT INTO EMPLOYEE VALUES (5,'David','Jones','1988-09-05','Male',5);");
                st.addBatch("INSERT INTO EMPLOYEE VALUES (6,'Olivia','Davis','1995-04-12','Female',1);");
                st.addBatch("INSERT INTO EMPLOYEE VALUES (7,'James','Wilson','1983-03-25','Male',6);");
                st.addBatch("INSERT INTO EMPLOYEE VALUES (8,'Sophia','Anderson','1991-08-17','Female',4);");
                st.addBatch("INSERT INTO EMPLOYEE VALUES (9,'Liam','Miller','1979-12-01','Male',1);");
                st.addBatch("INSERT INTO EMPLOYEE VALUES (10,'Emma','Taylor','1993-06-28','Female',5);");
                st.executeBatch();
                st.clearBatch();

                // Insert PAYMENTS data
                st.addBatch("INSERT INTO PAYMENTS VALUES (1,2,65784.00,'2025-01-01 13:44:12.824');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (2,4,62736.00,'2025-01-06 18:36:37.892');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (3,1,69437.00,'2025-01-01 10:19:21.563');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (4,3,67183.00,'2025-01-02 17:21:57.341');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (5,2,66273.00,'2025-02-01 11:49:15.764');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (6,5,71475.00,'2025-01-01 07:24:14.453');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (7,1,70837.00,'2025-02-03 19:11:31.553');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (8,6,69628.00,'2025-01-02 10:41:15.113');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (9,4,71876.00,'2025-02-01 12:16:47.807');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (10,3,70098.00,'2025-02-03 10:11:17.341');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (11,6,67827.00,'2025-02-02 19:21:27.753');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (12,5,69871.00,'2025-02-05 17:54:17.453');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (13,2,72984.00,'2025-03-05 09:37:35.974');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (14,1,67982.00,'2025-03-01 06:09:51.983');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (15,6,70198.00,'2025-03-02 10:34:35.753');");
                st.addBatch("INSERT INTO PAYMENTS VALUES (16,4,74998.00,'2025-03-02 09:27:26.162');");
                st.executeBatch();
                conn.commit();
            }

            String sql = "SELECT p.AMOUNT AS SALARY, e.FIRST_NAME, e.LAST_NAME, e.DOB, d.DEPARTMENT_NAME " +
                         "FROM PAYMENTS p " +
                         "JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
                         "JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
                         "WHERE EXTRACT(DAY FROM p.PAYMENT_TIME) <> 1 " +
                         "ORDER BY p.AMOUNT DESC " +
                         "FETCH FIRST 1 ROW ONLY";

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    double salary = rs.getDouble("SALARY");
                    String first = rs.getString("FIRST_NAME");
                    String last = rs.getString("LAST_NAME");
                    Date dobSql = rs.getDate("DOB");
                    String dept = rs.getString("DEPARTMENT_NAME");

                    LocalDate dob = dobSql.toLocalDate();
                    int age = Period.between(dob, LocalDate.now()).getYears();
                    String name = first + " " + last;

                    System.out.println("SALARY,NAME,AGE,DEPARTMENT_NAME");
                    System.out.printf("%.2f,%s,%d,%s%n", salary, name, age, dept);
                } else {
                    System.out.println("No qualifying payments found.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
