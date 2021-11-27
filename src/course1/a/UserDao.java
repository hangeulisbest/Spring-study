package course1.a;

import java.sql.*;

/**
 * 초난감 DAO
 */
public class UserDao {
    public void add(User user) throws ClassNotFoundException, SQLException {
        // Class.forName 은 드라이브를 로드하기위해 사용했지만 자동등록되서 필요 없음
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/test"
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
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/test"
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
        UserDao dao = new UserDao();

        User user = new User();
        user.setId("whiteShip");
        user.setName("백기선");

        user.setPassword("married");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }
}
