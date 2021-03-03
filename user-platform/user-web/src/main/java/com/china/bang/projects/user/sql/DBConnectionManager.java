package com.china.bang.projects.user.sql;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Objects;

public class DBConnectionManager {

    private static DataSource dataSource;
    private final static String JDNI_NAMING="jdbc/derby/jdniDerby";
    private final static String derbyDbUrl = "jdbc:derby:/db/user-platform;create=true";

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException throwables) {
            getConnectionFromSPI();
           throw new RuntimeException(throwables.getCause());
        }
    }

    public void releaseConnection(Connection connection)
    {
        if(!Objects.isNull(connection)){
            try {
                connection.close();
            } catch (SQLException throwables) {
                throw new RuntimeException(throwables.getCause());
            }
            connection = null;
        }
    }

    private static Connection getConnctionFromJndi()  {

        Context initCtx = null;
        try {
            initCtx = new InitialContext();
            DataSource dataSource = (DataSource)initCtx.lookup(JDNI_NAMING);
            return dataSource.getConnection();
        } catch (NamingException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> Connection getConnectionFromSPI()
    {
        try {

            Connection connection = DriverManager.getConnection(derbyDbUrl);
            if(Objects.isNull(connection))
            {
                System.out.println("Connected to database #1");
            }

            return connection;
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables.getCause());
        }
    }

    public static final String DROP_USERS_TABLE_DDL_SQL = "DROP TABLE users";

    public static final String CREATE_USERS_TABLE_DDL_SQL = "CREATE TABLE users(" +
            "id INT NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
            "name VARCHAR(16) NOT NULL, " +
            "password VARCHAR(64) NOT NULL, " +
            "email VARCHAR(64) NOT NULL, " +
            "phoneNumber VARCHAR(64) NOT NULL" +
            ")";

    public static final String INSERT_USER_DML_SQL = "INSERT INTO users(name,password,email,phoneNumber) VALUES " +
            "('A','******','a@gmail.com','1') , " +
            "('B','******','b@gmail.com','2') , " +
            "('C','******','c@gmail.com','3') , " +
            "('D','******','d@gmail.com','4') , " +
            "('E','******','e@gmail.com','5')";
}
