package com.wjh.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class C3p0Util {
    //读取资源文件获取数据库配置
    private static ComboPooledDataSource dataSource =
            new ComboPooledDataSource("mysql");
    private static Connection connection = null;

    //初始化连接
    static {
        try {
            if(connection == null ) {
                connection = dataSource.getConnection();
            }
        } catch (SQLException e) {
            System.out.println("服务器处理错误：读取MySQL配置异常");
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        if(connection.isClosed()){
            connection = dataSource.getConnection();
        }
        return connection;
    }

    /**
     * 关闭资源
     * @param resultSet
     * @param statement
     * @param connection
     */
    public static void close(ResultSet resultSet, PreparedStatement statement,Connection connection){
        try {
            if(resultSet != null){
                resultSet.close();
            }
            if(statement != null){
                statement.close();
            }
            if(connection != null){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在不执行查询语句的情况下没有ResultSet，利用方法的重载关闭资源
     * @param statement
     * @param connection
     */
    public static void close(PreparedStatement statement,Connection connection){
        close(null,statement,connection);
    }

}
