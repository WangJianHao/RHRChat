package com.wjh.dao.impl;


import com.wjh.bean.ChatGroup;
import com.wjh.dao.GroupMapper;
import com.wjh.util.C3p0Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GroupMapperImpl implements GroupMapper {
    private Connection connection;

    /**
     * 根据id查询用户所在的群有哪些
     * @param id
     * @return
     */
    @Override
    public HashMap<Integer,ChatGroup> getGroupByUser(int id) throws SQLException {
        System.out.println("[server]:查询id所在的群的数据库访问层"+id);
        HashMap<Integer, ChatGroup> groups = new HashMap<>();
        connection = C3p0Util.getConnection();
        String sql = "select * from chat_group where user1 = ? or user2 = ? or user3 = ? or user4 = ? or user5 = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        preparedStatement.setInt(2,id);
        preparedStatement.setInt(3,id);
        preparedStatement.setInt(4,id);
        preparedStatement.setInt(5,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            ChatGroup chatGroup = new ChatGroup();
            chatGroup.setGroupId(resultSet.getLong("group_id"));
            chatGroup.setUser1(resultSet.getLong("user1"));
            chatGroup.setUser2(resultSet.getLong("user2"));
            if(resultSet.getString("user3") != null) {
                chatGroup.setUser3(resultSet.getLong("user3"));
            }
            if(resultSet.getString("user4") != null) {
                chatGroup.setUser4(resultSet.getLong("user4"));
            }
            if(resultSet.getString("user5") != null) {
                chatGroup.setUser5(resultSet.getLong("user5"));
            }
            groups.put((int)chatGroup.getGroupId(),chatGroup);
        }
        //释放资源
        C3p0Util.close(resultSet,preparedStatement,connection);
        System.out.println("[server]:数据库访问层执行结束");
        return groups;
    }

    /**
     * 根据群的id查询对应的群消息
     * @param group_id
     * @return
     */
    @Override
    public ChatGroup getGroupById(int group_id) throws SQLException {
        //打印日志
        System.out.println("[server]:根据群号获取对应的群  -dao层");
        //从连接池中获取连接
        connection = C3p0Util.getConnection();
        //返回的实例化对象
        ChatGroup chatGroup = null;
        //创建sql语句
        String sql = "select * from chat_group where group_id = ?";
        //创建可执行的preparedStatement
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,group_id);
        //获取返回的数据集
        ResultSet resultSet = preparedStatement.executeQuery();
        //遍历数据集
        while (resultSet.next()){
            chatGroup = new ChatGroup();
            //给对应的实例的属性赋值
            chatGroup.setGroupId(resultSet.getLong("group_id"));
            chatGroup.setUser1(resultSet.getLong("user1"));
            chatGroup.setUser2(resultSet.getLong("user2"));
            if(resultSet.getString("user3") != null) {
                chatGroup.setUser3(resultSet.getLong("user3"));
            }
            if(resultSet.getString("user4") != null) {
                chatGroup.setUser4(resultSet.getLong("user4"));
            }
            if(resultSet.getString("user5") != null) {
                chatGroup.setUser5(resultSet.getLong("user5"));
            }
        }

        return chatGroup;
    }

}
