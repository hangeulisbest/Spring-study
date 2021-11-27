package course1.a;

import java.sql.*;

public class UserDao {
    public void add(User user) throws ClassNotFoundException, SQLException {
        // Class.forName 은 드라이브를 로드하기위해 사용했지만 자동등록되서 필요 없음
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306"
                , "root"
                , "root1234");
        PreparedStatement ps = c.prepareStatement(
          "INSERT INTO USER(ID , NAME , PASSWORD) VALUES(?,?,?)"
        );
        ps.setString(1,user.getId());
        ps.setString(2,user.getName());
        ps.setString(3,user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException , SQLException {
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306"
                , "root"
                , "root1234");

        PreparedStatement ps = c.prepareStatement("SELECT * FROM USER WHERE ID = ?");
        ps.setString(1,id);
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

    public static void main(String[] args) throws ClassNotFoundException, SQLException  {
        User user = new User();
        UserDao dao = new UserDao();
        dao.add(user);
        System.out.println("HELLO");
    }
}
