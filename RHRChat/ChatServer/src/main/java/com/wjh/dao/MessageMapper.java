package com.wjh.dao;

import com.wjh.bean.SingleMessage;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;

public interface MessageMapper {
    int insertRecord(int sender,int receiver,String message,String time,boolean status) throws SQLException;

   List<SingleMessage> getUnreadMessagesById(long id) throws SQLException;

   int updateMessageStatus(int id) throws SQLException;
}
