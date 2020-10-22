package com.wjh.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wjh.bean.ChatGroup;
import com.wjh.dao.impl.GroupMapperImpl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GroupService {
    private GroupMapperImpl groupMapper = new GroupMapperImpl();

    public ObjectNode getGroupByUser(int id,ObjectNode objectNode) throws SQLException{
        System.out.println("[server]:查询群的业务层");
        HashMap<Integer, ChatGroup> groups = groupMapper.getGroupByUser(id);
        int i = 1;
        Iterator<Map.Entry<Integer, ChatGroup>> iterator = groups.entrySet().iterator();
        objectNode.put("count",groups.size());
        while (iterator.hasNext()){
            ChatGroup value = iterator.next().getValue();
            objectNode.put("group"+i,value.toString());
            i++;
        }
        return objectNode;
    }
}
