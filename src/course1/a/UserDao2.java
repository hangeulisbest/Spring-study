package course1.a;

import java.sql.*;

/**
 *  1.2 UserDao 를 추상메서드로 변환하고 Connection 관심사를 분리함
 */
public abstract class UserDao2 {
    public void add(User user) throws ClassNotFoundException, SQLException {
        // Class.forName 은 드라이브를 로드하기위해 사용했지만 자동등록되서 필요 없음
        Connection c = getConnection();

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
        Connection c = getConnection();

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

    protected void hookMethod(){
        System.out.println("PROTECED 는 선택적으로 구현 할수 있다!");
    };

    /**
     * 서브 클래스에서 반드시 구현해야함 !
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;

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
