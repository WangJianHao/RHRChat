package com.wjh.frame;

import com.wjh.bean.ChatGroup;
import com.wjh.bean.User;
import com.wjh.service.OnlineService;
import com.wjh.util.TimeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 左边为接收消息的窗口和发送消息的窗口，右边维护一个panel存储当前群里的成员1122
 */
public class GroupChatFrame extends JFrame {
    private JLabel sendPS;
    private JButton send,exit;//发送消息按钮和退出按钮
    private JTextArea chatWindow,sendWindow;//聊天窗口和发送窗口
    private JPanel chatPanel,sendPanel;//为窗口添加若干功能
    private JButton groupFileButton;
    //在右侧显示群成员
    private JPanel groupMemeber;
    private static final int width = 600;
    private static final int height = 600;

    private User user;//自己
    private ChatGroup group;//这个群的聊天界面
    public GroupChatFrame(User user, ChatGroup group) {
        super("群"+group.getGroupId()+"的会话");
        this.user = user;
        this.group = group;
        init();
        addComponent();
        addListener();
        showFrame();
    }
    private void init(){
        chatPanel = new JPanel();
        sendPanel = new JPanel();
        chatWindow = new JTextArea(30,36);
        sendWindow = new JTextArea(5,36);
        sendWindow.setWrapStyleWord(true);
        sendWindow.setLineWrap(true);
        sendWindow.setAutoscrolls(true);
        chatWindow.setWrapStyleWord(true);
        chatWindow.setLineWrap(true);
        chatWindow.setAutoscrolls(true);
        chatWindow.setEditable(false);
        sendPS = new JLabel("发送窗口：");
        send = new JButton("发送");
        exit = new JButton("退出");
        groupMemeber= new JPanel();
        groupFileButton = new JButton("文件发送");
    }
    private void addComponent(){
        this.setLayout(null);
        this.add(chatPanel);
        this.add(sendPanel);
        this.add(sendPS);
        this.add(send);
        this.add(exit);
        this.add(groupFileButton);

        //设置组件的位置和大小
        chatPanel.setBounds(0,20,500,300);
        sendPanel.setBounds(0,350,500,150);
        sendPS.setBounds(0,320,300,30);
        chatWindow.setFont(new Font("DiaLog",0,15));
        sendWindow.setFont(new Font("DiaLog",0,15));
        JScrollPane chat = new JScrollPane(chatWindow);
        JScrollPane send = new JScrollPane(sendWindow);
        chat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        send.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        send.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chatPanel.add(chat);
        sendPanel.add(send);
        this.send.setBounds(420,510,80,40);
        exit.setBounds(20,510,80,40);
        groupFileButton.setBounds(100,320, 100,30);
        //初始化群成员
        groupMemeber.setLayout(null);
        groupMemeber.setBounds(500,300,100,300);
        int i = 0;
         if(group.getUser1() != -1 && group.getUser1() != user.getId()){
             //默认群里面的成员是自己的好友
             JLabel jLabel = new JLabel(FriendListFrame.friendList.get((int)group.getUser1()).toString());
             jLabel.setBounds(0,i*30,100,30);
             groupMemeber.add(jLabel);
             i++;
         }
        if(group.getUser2() != -1  && group.getUser2() != user.getId()){
            //默认群里面的成员是自己的好友
            JLabel jLabel = new JLabel(FriendListFrame.friendList.get((int)group.getUser2()).toString());
            jLabel.setBounds(0,i*30,100,30);
            groupMemeber.add(jLabel);
            i++;
        }
        if(group.getUser3() != -1  && group.getUser3() != user.getId()){
            //默认群里面的成员是自己的好友
            JLabel jLabel = new JLabel(FriendListFrame.friendList.get((int)group.getUser3()).toString());
            jLabel.setBounds(0,i*30,100,30);
            groupMemeber.add(jLabel);
            i++;
        }
        if(group.getUser4() != -1  && group.getUser4() != user.getId()){
            //默认群里面的成员是自己的好友
            JLabel jLabel = new JLabel(FriendListFrame.friendList.get((int)group.getUser4()).toString());
            jLabel.setBounds(0,i*30,100,30);
            groupMemeber.add(jLabel);
            i++;
        }
        if(group.getUser5() != -1  && group.getUser5() != user.getId()){
            //默认群里面的成员是自己的好友
            JLabel jLabel = new JLabel(FriendListFrame.friendList.get((int)group.getUser5()).toString());
            jLabel.setBounds(0,i*30,100,30);
            groupMemeber.add(jLabel);
        }
        this.add(groupMemeber);

    }
    private synchronized void addListener(){
        //发送按钮的事件
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = sendWindow.getText();
                sendWindow.setText("");
                new OnlineService().chatWithGroup(user,group,msg);
                //将自己发送的消息添加到聊天窗口上
                chatWindow.append(user.getUserName()+" "+TimeUtil.getCurrentTime()+"：\n\r");
                chatWindow.append(msg+"\n\r");
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        groupFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GroupFileFrame(user,group);
            }
        });
    }
    private void showFrame(){
        int screen_height = (int)this.getToolkit().getScreenSize().getHeight();
        int screen_width = (int)this.getToolkit().getScreenSize().getWidth();
        this.setLocation((screen_width-width)/2,(screen_height-height)/2);
        this.setSize(width,height);
        this.setVisible(true);
    }

    /**
     * 显示接收到的消息
     * @param msg
     */
    public synchronized void showRecvMsg(User user,String msg,String time){
        chatWindow.append(user.getUserName()+" "+time+"：\n\r");
        chatWindow.append(msg+"\n\r");
    }
}
