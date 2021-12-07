package course1.초난감DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 1.2.3 UserDao2 를 확장한 N사 DAO
 */
public class NUserDao2 extends UserDao2 {

    /**
     * N사 에서 따로 확장한 메서드
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/test"
                , "root"
                , "root1234");
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        NUserDao2 dao = new NUserDao2();

        User user = new User();
        user.setId("whiteShip3");
        user.setName("백기선");

        user.setPassword("married");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");

        // 선택적 구현!
        dao.hookMethod();
    }


}
