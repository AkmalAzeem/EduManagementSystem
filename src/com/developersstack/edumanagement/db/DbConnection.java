package com.developersstack.edumanagement.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    // singleton => creational design pattern => *important*
    // rule 1
    private static DbConnection dbConnection;
    private Connection connection;

    //rule 2
    private  DbConnection() throws ClassNotFoundException, SQLException {
        // connection
        Class.forName("com.mysql.cj.jdbc.Driver");
        // Create a Connection
        connection =
                DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/lms3",
                        "root",
                        "Akstack#2003#123@az");
    }
    //rule 3
    public static DbConnection getInstance() throws SQLException, ClassNotFoundException {
        if (dbConnection==null){
            dbConnection = new DbConnection();
        }
        return dbConnection;
    }
    public Connection getConnection(){
        return connection;
    }

}
