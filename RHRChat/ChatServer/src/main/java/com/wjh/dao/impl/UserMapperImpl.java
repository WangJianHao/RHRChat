package com.wjh.dao.impl;

import com.wjh.bean.User;
import com.wjh.dao.UserMapper;
import com.wjh.util.C3p0Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 针对user表的数据库操作接口的实现类
 */
public class UserMapperImpl implements UserMapper {
    //数据库访问层类维护一个连接来方便执行
    private Connection connection;


    /**
     * 通过id和密码查询user表中的元组
     * @param id
     * @param password
     * @return
     * @throws SQLException
     */
    @Override
    public User getUserByIdAndPassword(int id,String password) throws SQLException {
        //打印日志
        System.out.println("[server]:正在与数据库建立连接");
        //获取连接
        connection = C3p0Util.getConnection();
        //根据id和密码直接查询user表的sql语句
        String sql="select * from user where id = ? and password = ?";
        //返回的结果实例对象
        User user = null;
        //防止数据库注入异常，使用PreparedStatement先预编译，然后再传值
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        //传入数据
        preparedStatement.setInt(1,id);
        preparedStatement.setString(2,password);
        //执行查询
        ResultSet resultSet = preparedStatement.executeQuery();
        //遍历集合拿到结果
        while (resultSet.next()){
            user = new User();
            //通过实体类直接赋值
            user.setId(resultSet.getLong("id"));
            user.setUserName(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setEmail(resultSet.getString("email"));
        }
        //释放资源
        C3p0Util.close(resultSet,preparedStatement,connection);
        //返回结果
        return user;
    }

    /**
     * 插入新用户
     * @param email
     * @param userName
     * @param password
     * @return
     */
    @Override
    public User insertUser(String email, String userName, String password) throws SQLException {
        User user = null;
        //获取连接
        connection = C3p0Util.getConnection();
        //根据id和密码直接查询user表的sql语句
        //由数据库来保证邮箱的唯一性
        String sql="insert into user(username,password,email) values(?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,userName);
        preparedStatement.setString(2,password);
        preparedStatement.setString(3,email);
        int i = preparedStatement.executeUpdate();
        //如果i等于1的话就创建一个user实例给业务层
        if(i == 1){
            user = new User();
            user.setId(getId( email, password));
            user.setEmail(email);
            user.setUserName(userName);
            user.setPassword(password);
        }
        //释放资源
        C3p0Util.close(preparedStatement,connection);
        return user;
    }

    /**
     * 获取插入用户的id
     * @return
     */
    private int getId(String email, String password) throws SQLException {
        //获取连接
        connection = C3p0Util.getConnection();
        //根据id和密码直接查询user表的sql语句
        String sql="select id from user where email = ? and password = ?";
        //使用PreparedStatement防止数据库注入异常
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,email);
        preparedStatement.setString(2,password);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            return resultSet.getInt("id");
        }
        C3p0Util.close(resultSet,preparedStatement,connection);
        return -1;
    }

    /**
     * 根据id到数据库中查询对应的邮箱
     * @param id
     * @return
     */
    @Override
    public User getEmailAndPasswordById(int id) throws SQLException {
        User user = null;
        //打印日志
        System.out.println("[server]:正在数据访问层查询邮箱");
        //获取连接
        connection = C3p0Util.getConnection();
        //sql语句,根据id查询email字段
        String sql = "select email,password from user where id  = ?";
        //创建PreparedStatement语句执行查询
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            user = new User();
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
        }
        C3p0Util.close(resultSet,preparedStatement,connection);
        return user;
    }

    /**
     * 通过id查询用户的用户名
     * @param friendId
     * @return
     */
    @Override
    public String getNameById(int friendId)throws SQLException {
        //打印日志
        System.out.println("[server]:根据id查询好友的用户名");
        //获取连接
        connection = C3p0Util.getConnection();
        //sql语句,根据id查询email字段
        String sql = "select username from user where id  = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,friendId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            return resultSet.getString("username");
        }
        C3p0Util.close(resultSet,preparedStatement,connection);
        return null;
    }

    @Override
    public int updatePassword(int id, String oldPassword, String newPassword) throws SQLException {
        //打印日志
        System.out.println("[server]:更改密码操作的数据库访问层");
        //获取连接
        connection = C3p0Util.getConnection();
        //sql语句,根据id查询email字段
        String sql = "update user set password = ? where id = ? and password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,newPassword);
        preparedStatement.setInt(2,id);
        preparedStatement.setString(3,oldPassword);
        C3p0Util.close(preparedStatement,connection);
        return preparedStatement.executeUpdate();
    }
}
