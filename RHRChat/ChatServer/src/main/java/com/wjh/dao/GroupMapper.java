package com.wjh.dao;

import com.wjh.bean.ChatGroup;

import java.sql.SQLException;
import java.util.HashMap;

public interface GroupMapper {
    HashMap<Integer,ChatGroup> getGroupByUser(int id) throws SQLException;

    ChatGroup getGroupById(int group_id) throws SQLException;
}
