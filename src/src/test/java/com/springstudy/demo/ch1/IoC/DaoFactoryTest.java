package com.springstudy.demo.ch1.IoC;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DaoFactoryTest {

    @Test
    public void id가_1인_데이터의_이름은_HELLO다() throws ClassNotFoundException, SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);
        assertEquals(dao.get("1").getName(),"hello");
    }

    @Test
    public void daoFactory에서_생성한_오브젝트는_동일하지_않다(){
        DaoFactory daoFactory = new DaoFactory();
        UserDao dao1 = daoFactory.userDao();
        UserDao dao2 = daoFactory.userDao();

        assertEquals(dao1==dao2,false);

    }

    @Test
    public void ApplicationContext에서_가져온_userDao는_싱글톤이다(){
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao1 = context.getBean("userDao", UserDao.class);
        UserDao dao2 = context.getBean("userDao", UserDao.class);

        assertEquals(dao1==dao2,true);
    }
}