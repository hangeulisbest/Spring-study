package com.springstudy.demo.ch1.IoC;

import course1.관계설정책임분리.ConnectionMaker;
import course1.초난감DAO.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    private ConnectionMaker connectionMaker;

    /**
     * this.connectionMaker = new DConnectionMaker() 와 다르게 의존관계에서 벗어남 -> 결합도를 낮추는 것이 중요함
     * 전략 패턴
     *
     * @param connectionMaker
     */
    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement("SELECT * FROM USER WHERE ID = ?");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        rs.close();
        ps.close();
        c.close();

        return user;
    }

}
