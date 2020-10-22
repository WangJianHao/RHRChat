package com.wjh.dao.impl;

import com.wjh.bean.User;
import org.junit.Test;

import java.sql.SQLException;


public class UserMapperImplTest {

    @Test
    public void getUserByIdAndPassword() {
        try {
            User user = new UserMapperImpl().getUserByIdAndPassword(0, "123456");
            System.out.println(user);
            user = new UserMapperImpl().getUserByIdAndPassword(1, "123456");
            System.out.println(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}