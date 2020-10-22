package com.wjh.dao.impl;

import com.wjh.bean.SingleFile;
import com.wjh.dao.SingleFileMapper;
import com.wjh.util.C3p0Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SingleFileMapperImpl  implements SingleFileMapper {
    private Connection connection;
    @Override
    public int insertFileRecord(int sender, int receiver, String path, String time, boolean status) throws SQLException {
        System.out.println("[RHR server]:插入传输文件的记录");
        //获取连接
        connection = C3p0Util.getConnection();
        //sql语句
        String sql = "insert into single_file values(?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1,sender);
        preparedStatement.setInt(2,receiver);
        preparedStatement.setString(3,path);
        preparedStatement.setString(4,time);
        preparedStatement.setBoolean(5,status);
        int i = preparedStatement.executeUpdate();
        C3p0Util.close(preparedStatement,connection);
        return i;
    }

    @Override
    public List<SingleFile> queryUnreadMessage(long id) throws SQLException {
        System.out.println("[RHR server]:查询所有的未接收文件");
        connection = C3p0Util.getConnection();
        List<SingleFile> singleFiles = new ArrayList<>();
        String sql = "select * from single_file where receiver = ? and status = false ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            SingleFile singleFile = new SingleFile();
            singleFile.setSender(resultSet.getLong("sender"));
            singleFile.setReceiver(resultSet.getLong("receiver"));
            singleFile.setPath(resultSet.getString("path"));
            singleFile.setTime(resultSet.getTimestamp("time"));
            singleFiles.add(singleFile);
        }
        updateStatus(id);
        C3p0Util.close(resultSet,preparedStatement,connection);
        return singleFiles;
    }
    private void updateStatus(long id) throws SQLException{
        String sql = "update single_file set status = true where receiver = ? and status = false ";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1,id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }
}
