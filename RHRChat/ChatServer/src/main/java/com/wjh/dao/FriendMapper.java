package com.wjh.dao;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.SQLException;

public interface FriendMapper {
    public ObjectNode getFriendList(ObjectNode objectNode,int id) throws SQLException;
}
