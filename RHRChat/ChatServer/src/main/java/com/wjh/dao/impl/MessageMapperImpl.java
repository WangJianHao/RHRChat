package com.wjh.dao.impl;

import com.wjh.bean.SingleMessage;
import com.wjh.dao.MessageMapper;
import com.wjh.util.C3p0Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MessageMapperImpl implements MessageMapper {
    private Connection connection;

    /**
     * 插入一条消息记录
     * @param sender
     * @param receiver
     * @param message
     * @param time
     * @param status
     * @return
     * @throws SQLException
     */
    @Override
    public int insertRecord(int sender, int receiver, String message, String time,boolean status) throws SQLException {
        //打印日志
        System.out.println("[server]:进入数据访问层执行插入消息发送记录");
        //获取数据库的连接
        connection = C3p0Util.getConnection();
        //sql语句
        String sql = "insert into single_message values (?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,sender);
        preparedStatement.setInt(2,receiver);
        preparedStatement.setString(3,message);
        preparedStatement.setString(4,time);
        preparedStatement.setBoolean(5,status);
        return preparedStatement.executeUpdate();
    }

    /**
     * 根据接收者的id查询所有其离线消息
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public List<SingleMessage> getUnreadMessagesById(long id) throws SQLException {
        System.out.println("[RHR server]:查询未读单聊消息的数据库访问层！");
        //获取连接
        connection = C3p0Util.getConnection();
        //返回值
        List<SingleMessage> messages = new ArrayList<>(100);
        //sql语句   需要的是确定接收者的未读的消息
        String sql = "select * from single_message where receiver = ? and status = false ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,(int)id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            SingleMessage message = new SingleMessage();
            message.setSender(resultSet.getInt("sender"));
            message.setReceiver(id);
            message.setMessage(resultSet.getString("message"));
            message.setTime(resultSet.getTimestamp("time"));
            messages.add(message);
        }
        C3p0Util.close(resultSet,preparedStatement,connection);
        System.out.println("[RHR server]:查询未读消息结束！");
        return messages;
    }

    /**
     * 更改消息的状态
     * @param id
     * @return
     * @throws SQLException
     */
    @Override
    public int updateMessageStatus(int id) throws SQLException {
        connection = C3p0Util.getConnection();
        String sql = "update single_message set status = true where receiver = ? and status = false ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        return  preparedStatement.executeUpdate();
    }
}
