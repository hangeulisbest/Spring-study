package course1.관계설정책임분리;

import java.sql.SQLException;

public class UserDaoTest {
    /**
     * UserDao는 ConnectionMaker가 누군지 신경안써도 됨!
     * @param args
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao dao = new UserDao(connectionMaker);
        System.out.println(dao.get("1").getName());
    }
}
