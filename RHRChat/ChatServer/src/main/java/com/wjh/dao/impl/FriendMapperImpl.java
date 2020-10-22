package com.wjh.dao.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wjh.dao.FriendMapper;
import com.wjh.service.UserService;
import com.wjh.util.C3p0Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendMapperImpl implements FriendMapper {
    private Connection connection;
    /**
     * 将好友列表放入返回的json消息中
     * @param objectNode
     * @return
     * @throws SQLException
     */
    @Override
    public ObjectNode getFriendList(ObjectNode objectNode,int id) throws SQLException {
        UserMapperImpl userMapper = new UserMapperImpl();
        //打印日志
        System.out.println("[server]:获取好友列表的数据库访问层");
        //获取连接
        connection = C3p0Util.getConnection();
        String sql = "SELECT friend_id AS friends, user_group AS my_group FROM friend WHERE user_id = ? UNION ALL SELECT user_id AS friends, friend_group AS my_group FROM friend WHERE friend_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,id);
        preparedStatement.setInt(2,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        StringBuffer mates = new StringBuffer();
        StringBuffer family = new StringBuffer();
        StringBuffer define = new StringBuffer();
        while (resultSet.next()){
            int friendId = resultSet.getInt("friends");
            switch (resultSet.getString("my_group")){
                case "mates":
                    mates.append(friendId);
                    mates.append("#");
                    mates.append(userMapper.getNameById(friendId));
                    mates.append(",");
                    break;
                case "family":
                    family.append(friendId);
                    family.append("#");
                    family.append(userMapper.getNameById(friendId));
                    family.append(",");
                    break;
                case "define":
                    define.append(friendId);
                    define.append("#");
                    define.append(userMapper.getNameById(friendId));
                    define.append(",");
                    break;
            }
        }
        objectNode.put("mates",mates.toString());
        objectNode.put("family",family.toString());
        objectNode.put("define",define.toString());
        System.out.println("[server]:已获取到好友列表");
        C3p0Util.close(resultSet,preparedStatement,connection);
        return objectNode;
    }
}
