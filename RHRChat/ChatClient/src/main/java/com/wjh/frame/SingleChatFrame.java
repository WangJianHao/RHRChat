package com.wjh.frame;

import com.wjh.bean.User;
import com.wjh.service.OnlineService;
import com.wjh.service.SendService;
import com.wjh.util.TimeUtil;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * 单聊窗口
 * @author Mr. Wang 
 * @// TODO: 2020/10/18  
 */
public class SingleChatFrame extends JFrame{
    private JLabel sendPS;
    private JButton send,exit;//发送消息按钮和退出按钮
    private JTextArea chatWindow,sendWindow;//聊天窗口和发送窗口
    private JPanel chatPanel,sendPanel;//为窗口添加若干功能
    private JButton singleFileButton;
    private static final int width = 600;
    private static final int height = 600;
    private User friend;
    private User user;
    public SingleChatFrame(User user,User friend){
        super("与"+friend.getUserName()+"的会话");
        this.user = user;
        this.friend = friend;
        init();
        addComponent();
        addListener();
        showFrame();
    }
    private void init(){
        chatPanel = new JPanel();
        sendPanel = new JPanel();
        chatWindow = new JTextArea(12,36);
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
        singleFileButton = new JButton("文件发送");
    }
    private void addComponent(){
        this.setLayout(null);
        this.add(chatPanel);
        this.add(sendPanel);
        this.add(sendPS);
        this.add(send);
        this.add(exit);
        this.add(singleFileButton);

        //设置组件的位置和大小
        chatPanel.setBounds(0,20,500,300);
        sendPanel.setBounds(0,350,500,150);
        sendPS.setBounds(0,320,100,30);
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
        singleFileButton.setBounds(100,320, 100,30);

    }
    private synchronized void addListener(){
        //发送按钮的事件
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = sendWindow.getText();
                sendWindow.setText("");
                new OnlineService().chatWithFriend(user,friend,msg);
                //将自己发送的消息添加到聊天窗口上
                chatWindow.append(user.getUserName()+" "+TimeUtil.getCurrentTime()+"：\n");
                chatWindow.append(msg+"\n");
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        singleFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SingleFileFrame(user,friend);
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
    public synchronized void showRecvMsg(String msg,String time){
        chatWindow.append(friend.getUserName()+" "+time+"：\n");
        chatWindow.append(msg+"\n");
    }
}
