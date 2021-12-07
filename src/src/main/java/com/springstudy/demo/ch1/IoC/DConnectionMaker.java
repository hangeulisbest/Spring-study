package com.springstudy.demo.ch1.IoC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/test"
                , "root"
                , "root1234");
        return c;
    }

}
