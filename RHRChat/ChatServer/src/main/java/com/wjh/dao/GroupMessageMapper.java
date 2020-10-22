package com.wjh.dao;

import com.wjh.bean.GroupMessage;

import java.sql.SQLException;
import java.util.List;

public interface GroupMessageMapper {

    List<GroupMessage> queryUnreadMessageByGroupAndUser(int groupId, long id) throws SQLException;

    void insertMessageRecord(int group_id, int sender, long receiver, String message, String time, boolean b) throws SQLException;
}
