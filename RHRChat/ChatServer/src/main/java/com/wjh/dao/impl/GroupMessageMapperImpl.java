package com.wjh.dao.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wjh.bean.GroupMessage;
import com.wjh.constant.MsgType;
import com.wjh.dao.GroupMessageMapper;
import com.wjh.util.C3p0Util;
import com.wjh.util.CacheUtil;
import com.wjh.util.JsonUtil;
import io.netty.channel.Channel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupMessageMapperImpl implements GroupMessageMapper {
    private Connection connection;

    @Override
    public List<GroupMessage> queryUnreadMessageByGroupAndUser(int groupId, long id) throws SQLException {
        //打印日志
        System.out.println("[server]:查询群聊的未读消息的数据库访问层");
        //获取一个可用的连接
        connection = C3p0Util.getConnection();
        ArrayList<GroupMessage> groupMessages = new ArrayList<>(100);
        //创建sql语句
        String sql = "select * from group_message where group_id = ? and receiver = ? and status = false ";
        //创建preparedStatement执行sql语句
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,groupId);
        preparedStatement.setInt(2,(int)id);
        //执行查询语句，返回结果集
        ResultSet resultSet = preparedStatement.executeQuery();
        //遍历结果集
        while (resultSet.next()){
            //创建实例
            GroupMessage groupMessage = new GroupMessage();
            groupMessage.setGroupId(resultSet.getLong("group_id"));
            groupMessage.setSender(resultSet.getLong("sender"));
            groupMessage.setReceiver(resultSet.getLong("receiver"));
            groupMessage.setMessage(resultSet.getString("message"));
            groupMessage.setTime(resultSet.getTimestamp("time"));
            //添加到集合中
            groupMessages.add(groupMessage);
        }
        //更改消息状态
        updateStatus(groupId,(int)id);
        C3p0Util.close(resultSet,preparedStatement,connection);
        return groupMessages;
    }

    private void updateStatus(int group_id,int receiver) throws SQLException {
        System.out.println("[RHR server]:更改群消息状态");
        connection = C3p0Util.getConnection();
        String sql = "update group_message set status = true where group_id = ?  and receiver = ? and status = false";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,group_id);
        preparedStatement.setInt(2,receiver);
        preparedStatement.executeUpdate();
    }

    /**
     * 插入群聊的消息记录
     * @param group_id
     * @param sender
     * @param receiver
     * @param message
     * @param time
     * @param b
     */
    @Override
    public void insertMessageRecord(int group_id, int sender, long receiver, String message, String time, boolean b) throws SQLException {
        //打印日志
        System.out.println("[server]:在数据库访问层向数据库中插入群消息的记录");
        //获取连接
        connection = C3p0Util.getConnection();
        //sql语句
        String sql = "insert into group_message values(?,?,?,?,?,?)";
        //创建可执行的prepareStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,group_id);
        preparedStatement.setInt(2,sender);
        preparedStatement.setInt(3,(int)receiver);
        preparedStatement.setString(4,message);
        preparedStatement.setString(5,time);
        preparedStatement.setBoolean(6,b);
        //执行sql语句
        preparedStatement.executeUpdate();
    }
}
