package com.wjh.util;


import com.wjh.bean.User;
import io.netty.channel.Channel;

import java.util.Hashtable;

public class CacheUtil {
    private static final Hashtable<User,Channel> cacheUser = new Hashtable<>(100);
    private static final Hashtable<Channel,User> cacheChan = new Hashtable<>(100);
    /**
     * 通过ip返回对应的NettyController
     * @param id
     * @return
     */
    public static Channel getChannelById(int id) {
        User user = new User();
        user.setId(id);
        if(cacheUser.containsKey(user)) {
            Channel chan = cacheUser.get(user);
            return chan;
        }
        return null;
    }

    /**
     * 用户下线时或者更改用户在数据库中的信息时需要更改该用户对应的缓存
     * 为什么不直接清除，是因为本项目是用缓存来判断是否在线
     * @param user
     */
    public static void remove(User user){
        cacheUser.remove(user);
        System.out.println("[RHR server]:缓存已清除完全");
    }
    public static void remove(Channel channel){
        if(cacheChan.containsKey(channel)) {
            System.out.println("[RHR server]:清除缓存中");
            User user = cacheChan.get(channel);
            cacheChan.remove(channel);
            remove(user);
        }
    }

    /**
     * 判断缓存中是否存在
     * @param id
     * @return
     */
    public static boolean isOnline(int id) {
        User user = new User();
        user.setId(id);
        return cacheUser.containsKey(user);
    }

    /**
     * 添加缓存
     * @param user
     * @param channel
     */
    public static void addCache(User user, Channel channel) {
        System.out.println("[RHR server]:正将数据放入缓存");
        cacheUser.put(user,channel);
        cacheChan.put(channel,user);
        System.out.println("[RHR server]:已将数据放至缓存中");
    }


    /**
     * 根据通道获取对应的用户
     * @param channel
     * @return
     */
    public static User getUserByChannel(Channel channel) {
        if(cacheChan.containsKey(channel)) {
            User user = cacheChan.get(channel);
            return user;
        }
        return null;
    }
}
