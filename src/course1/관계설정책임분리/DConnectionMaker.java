package course1.관계설정책임분리;

import course1.초난감DAO.User;

import java.sql.*;

public class DConnectionMaker implements ConnectionMaker {

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/test"
                , "root"
                , "root1234");
        return c;
    }

}
