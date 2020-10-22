package com.wjh.dao;

import com.wjh.bean.SingleFile;

import java.sql.SQLException;
import java.util.List;

public interface SingleFileMapper {
    int insertFileRecord(int sender,int receiver,String path,String time,boolean status) throws SQLException;

    List<SingleFile> queryUnreadMessage(long id) throws  SQLException;
}
