package com.wjh.dao;

import com.wjh.bean.User;

import java.sql.SQLException;

/**
 * 针对user表的数据库操作接口
 */
public interface UserMapper {
    User getUserByIdAndPassword(int id,String password) throws SQLException;
    User insertUser(String email, String userName, String password) throws SQLException ;
    User getEmailAndPasswordById(int id) throws SQLException;

    String getNameById(int friendId) throws SQLException;

    int updatePassword(int id, String oldPassword, String newPassword) throws  SQLException;
}
