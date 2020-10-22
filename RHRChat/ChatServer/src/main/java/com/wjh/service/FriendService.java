package com.wjh.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wjh.bean.User;
import com.wjh.constant.MsgType;
import com.wjh.dao.impl.FriendMapperImpl;
import com.wjh.util.CacheUtil;
import com.wjh.util.JsonUtil;
import io.netty.channel.Channel;

import javax.json.Json;
import java.sql.SQLException;

public class FriendService {
    private User user;
    public FriendService(User user){
        this.user = user;
    }

    /**
     *更新好友列表
     */
    public void renewFriendOnline(ObjectNode objectNode){
        //打印日志
        System.out.println("[server]:更新好友上线");
        String mates = objectNode.get("mates").asText();
        String family = objectNode.get("family").asText();
        String define = objectNode.get("define").asText();
        if(!mates.equals("")){
            String[] split = mates.split(",");
            for(int i = 0;i<split.length;i++){
                String[] split1 = split[i].split("#");
                int id = Integer.parseInt(split1[0]);
                Channel channel = CacheUtil.getChannelById(id);
                //缓存中不存在，代表不在线
                if(channel != null) {
                    ObjectNode friendOnline = JsonUtil.getObjectNode();
                    //向好友通知user上线了
                    friendOnline.put("msgType", MsgType.FRIEND_ONLINE_ADVICE);
                    friendOnline.put("friend_id", user.getId());
                    //客户端需要建立好友列表的集合
                    channel.writeAndFlush(friendOnline.toString());
                }
            }
        }
        if(!family.equals("")){
            String[] split = family.split(",");
            for(int i = 0;i<split.length;i++){
                String[] split1 = split[i].split("#");
                int id = Integer.parseInt(split1[0]);
                Channel channel = CacheUtil.getChannelById(id);
                //缓存中不存在，代表不在线
                if(channel != null) {
                    ObjectNode friendOnline = JsonUtil.getObjectNode();
                    //向好友通知user上线了
                    friendOnline.put("msgType", MsgType.FRIEND_ONLINE_ADVICE);
                    friendOnline.put("friend_id", user.getId());
                    //客户端需要建立好友列表的集合
                    channel.writeAndFlush(friendOnline.toString());
                }
            }
        }
        if(!define.equals("")){
            String[] split = define.split(",");
            for(int i = 0;i<split.length;i++){
                String[] split1 = split[i].split("#");
                int id = Integer.parseInt(split1[0]);
                Channel channel = CacheUtil.getChannelById(id);
                //缓存中不存在，代表不在线
                if(channel != null) {
                    ObjectNode friendOnline = JsonUtil.getObjectNode();
                    //向好友通知user上线了
                    friendOnline.put("msgType", MsgType.FRIEND_ONLINE_ADVICE);
                    friendOnline.put("friend_id", user.getId());
                    //客户端需要建立好友列表的集合
                    channel.writeAndFlush(friendOnline.toString());
                }
            }
        }
    }

    /**
     * 好友下线通知
     */
    public void renewFriendDisOnline() throws SQLException {
        //打印日志
        System.out.println("[server]:更新好友下线");
        //先获取好友列表
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode = new FriendMapperImpl().getFriendList(objectNode, (int) user.getId());
        String mates = objectNode.get("mates").asText();
        String family = objectNode.get("family").asText();
        String define = objectNode.get("define").asText();
        if(!mates.equals("")){
            String[] split = mates.split(",");
            for(int i = 0;i<split.length;i++){
                String[] split1 = split[i].split("#");
                int id = Integer.parseInt(split1[0]);
                Channel channel = CacheUtil.getChannelById(id);
                //缓存中不存在，代表不在线
                if(channel != null) {
                    ObjectNode friendOnline = JsonUtil.getObjectNode();
                    //向好友通知user上线了
                    friendOnline.put("msgType", MsgType.FRIEND_DISONLINE_ADVICE);
                    friendOnline.put("friend_id", user.getId());
                    //客户端需要建立好友列表的集合
                    channel.writeAndFlush(friendOnline.toString());
                }
            }
        }
        if(!family.equals("")){
            String[] split = family.split(",");
            for(int i = 0;i<split.length;i++){
                String[] split1 = split[i].split("#");
                int id = Integer.parseInt(split1[0]);
                Channel channel = CacheUtil.getChannelById(id);
                //缓存中不存在，代表不在线
                if(channel != null) {
                    ObjectNode friendOnline = JsonUtil.getObjectNode();
                    //向好友通知user上线了
                    friendOnline.put("msgType", MsgType.FRIEND_DISONLINE_ADVICE);
                    friendOnline.put("friend_id", user.getId());
                    //客户端需要建立好友列表的集合
                    channel.writeAndFlush(friendOnline.toString());
                }
            }
        }
        if(!define.equals("")){
            String[] split = define.split(",");
            for(int i = 0;i<split.length;i++){
                String[] split1 = split[i].split("#");
                int id = Integer.parseInt(split1[0]);
                Channel channel = CacheUtil.getChannelById(id);
                //缓存中不存在，代表不在线
                if(channel != null) {
                    ObjectNode friendOnline = JsonUtil.getObjectNode();
                    //向好友通知user上线了
                    friendOnline.put("msgType", MsgType.FRIEND_DISONLINE_ADVICE);
                    friendOnline.put("friend_id", user.getId());
                    //客户端需要建立好友列表的集合
                    channel.writeAndFlush(friendOnline.toString());
                }
            }
        }
    }
}
