package com.wjh.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mysql.cj.util.TimeUtil;
import com.wjh.bean.User;
import com.wjh.constant.MsgType;
import com.wjh.dao.impl.FriendMapperImpl;
import com.wjh.dao.impl.GroupMapperImpl;
import com.wjh.dao.impl.MessageMapperImpl;
import com.wjh.dao.impl.UserMapperImpl;
import com.wjh.util.CacheUtil;
import com.wjh.util.EmailUtil;
import com.wjh.util.JsonUtil;
import io.netty.channel.Channel;
import org.apache.commons.mail.EmailException;


import java.sql.SQLException;

public class UserService {
    private UserMapperImpl userMapper;
    private Channel channel;
    //只有登录的时候需要这个ctx用来放入缓存中进行服务器与单个用户之间的通信
    public UserService(Channel channel){
        userMapper = new UserMapperImpl();
        this.channel = channel;
    }



    /**
     * 判断登录是否成功
     * @param id
     * @param password
     * @return
     * @throws SQLException
     */
    public String login(int id,String password) throws Exception {
        //封装好json数据报
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode.put("msgType", MsgType.LOGIN_ACK);
        //先根据缓存查找是否在线，返回登录错误的同时也要返回错误信息
        if(CacheUtil.isOnline(id)){
            //该用户已经在线
            objectNode.put("login_status", false);
            objectNode.put("status_msg", "该用户已在线！");
        }else {
            System.out.println("[server]:执行dao层查询+("+id+","+password+")");
            //如果缓存中没有再通过dao层查询数据库中是否存在
            User user = userMapper.getUserByIdAndPassword(id, password);
            System.out.println("[server]:"+user);
            if (user == null) {
                objectNode.put("login_status", false);
                objectNode.put("status_msg", "用户名不存在或密码错误！");
            } else {
                System.out.println("[server]:用户登录成功！");
                objectNode.put("login_status", true);
                objectNode.put("status_msg", "登录成功！");
                objectNode.put("id",user.getId());
                objectNode.put("userName",user.getUserName());
                objectNode.put("password",user.getPassword());
                objectNode.put("email",user.getEmail());
                System.out.println("[server]:执行缓存方法！");
                //用户登录成功后，放入缓存中
                CacheUtil.addCache(user,channel);
                //获取好友列表并返回
                objectNode = new FriendMapperImpl().getFriendList(objectNode,id);
                //获取群列表并返回
                objectNode = new GroupService().getGroupByUser((int)user.getId(),objectNode);
                //登录成功给好友通知
                new FriendService(user).renewFriendOnline(objectNode);
                //先返回登录信息
                Channel channelById = CacheUtil.getChannelById((int) user.getId());
                if(channelById != null) {
                    channelById.writeAndFlush(objectNode.toString());
                }
                //接收未读的私人聊天消息
                new MessageService().sendUnreadMessage(user);
                //接收未读的群聊天消息
                new GroupMessageService().sendUnreadMessage(user);
                //客户端需要维护一个群组的类，而且必须有一个缓存存储群组id和其对应的其他成员
                new FileService().sendUnreadMessage((int)user.getId());
                return "";
            }
        }
        return objectNode.toString();
    }

    /**
     * 注册方法，由客户端保证合法性
     * @param email
     * @param userName
     * @param password
     * @return
     */
    public String register(String email,String userName,String password) throws SQLException, EmailException {
        //打印日志
        System.out.println("[server]:准备进入数据访问层进行插入");
        //封装好json数据报
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode.put("msgType", MsgType.REGISTER_ACK);
        //业务层拿到user只负责将其发送邮件给注册邮箱
        User user = userMapper.insertUser(email,userName,password);
        if(user != null){
            StringBuffer str = new StringBuffer("账号：");
            str.append(user.getId());
            str.append("\n\r");
            str.append("密码：");
            str.append(user.getPassword());
            //发送邮件通知
            EmailUtil.toEmail(email,str.toString());
            System.out.println("[server]:邮件已经发送");
            objectNode.put("register_status",true);
            objectNode.put("status_msg","注册成功，账号已发送至对应邮箱！");
        }else{
            //代表插入失败
            objectNode.put("register_status",true);
            objectNode.put("status_message","注册失败！");
        }
        return objectNode.toString();
    }

    /**
     * 忘记密码方法，现根据id找到对应的email，然后将密码发送到对应的邮箱
     * @param id
     * @return
     */
    public String forgetPassword(int id) throws SQLException, EmailException {
        //打印日志
        System.out.println("[server]:正在进入数据访问层查询对应的邮箱");
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode.put("msgType",MsgType.FORGET_PASSWORD_ACK);
        User user = userMapper.getEmailAndPasswordById(id);
        if(user == null){
            //如果为null，代表数据库中没有对应的id
            objectNode.put("forgetPassword_status",false);
            objectNode.put("status_msg","此id不存在，请检查输入的id！");
        }else{
            //拿到了email
            EmailUtil.toEmail(user.getEmail(),user.getPassword());
            objectNode.put("forgetPassword_status",true);
            objectNode.put("status_msg","密码已发送至注册邮箱！");
        }
        return objectNode.toString();
    }

    /**
     * 更改密码的业务逻辑操作
     * @param id
     * @param oldPassword
     * @param newPassword
     * @return
     */
    public String changePassword(int id, String oldPassword, String newPassword) throws SQLException {
        ObjectNode objectNode = JsonUtil.getObjectNode();
        objectNode.put("msgType",MsgType.CHANGE_PASSWORD_ACK);
        int i = userMapper.updatePassword( id,  oldPassword,  newPassword);
        if(i == 1){
            objectNode.put("changePassword_status",true);
            objectNode.put("status_msg","更改密码成功，需要重新启动客户端！");
            //更改密码成功则需要清除缓存，客户端会自动关闭，在断连时服务端会清除其内存的
        }else{
            objectNode.put("changePassword_status",false);
            objectNode.put("status_msg","更改密码失败，请检查id和密码输入是否正确！");
        }
        return objectNode.toString();
    }


}
