package com.wjh.frame;


import com.wjh.service.OnlineService;
import com.wjh.service.SendService;
import com.wjh.util.ImagePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 更改密码界面
 * @author Mr. Wang
 */
public class ChangePasswordFrame  extends JFrame {
    //防止全透明化
    JPanel jPanel = new ImagePanel();
    //输入用户名组件
    private JLabel idLabel;
    private JTextField idText;
    //输入密码组件
    private JLabel oldPasswordLabel;
    private JPasswordField oldPasswordText;
    private JLabel newPasswordLabel;
    private JPasswordField newPasswordText;
    //按钮
    private JButton confirmButton;//确认按钮
    private JButton backButton;//返回按钮
    //业务层实例化对象
    private SendService sendService;
    //界面的大小设置
    private static final int WIDTH = 450;
    private static final int HEIGHT = 350;
    public ChangePasswordFrame(){
        init();
        addComponent();
        addListener();
        showFrame();
    }


    /**
     * 初始化各组件
     */
    private void init(){
        idLabel = new JLabel("账号：");
        idText = new JTextField();
        oldPasswordLabel = new JLabel("旧密码：");
        oldPasswordText = new JPasswordField();
        newPasswordLabel = new JLabel("新密码：");
        newPasswordText = new JPasswordField();

        confirmButton = new JButton("确认");
        backButton = new JButton("返回");

        sendService = new SendService();
    }

    /**
     * 将组件添加到界面上
     */
    private void addComponent(){
        this.setLayout(null);
        this.add(jPanel);
        jPanel.setBounds(0,0,WIDTH,HEIGHT);
        jPanel.setLayout(null);

        //设置大小和位置
        idLabel.setBounds(50,20,100,50);
        idText.setBounds(150,20,200,50);
        oldPasswordLabel.setBounds(50,100,100,50);
        oldPasswordText.setBounds(150,100,200,50);
        newPasswordLabel.setBounds(50,180,100,50);
        newPasswordText.setBounds(150,180,200,50);
        backButton.setBounds(0,250,225,50);
        confirmButton.setBounds(225,250,225,50);
        //添加到界面上
        jPanel.add(idLabel);
        jPanel.add(idText);
        jPanel.add(backButton);
        jPanel.add(confirmButton);
        jPanel.add(oldPasswordLabel);
        jPanel.add(oldPasswordText);
        jPanel.add(newPasswordLabel);
        jPanel.add(newPasswordText);
    }

    /**
     * 给各组件添加事件
     */
    private void addListener(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //更改密码功能的事件
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new OnlineService().changePassword(idText.getText(),oldPasswordText.getText(),newPasswordText.getText());
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null,e1.getMessage());
                }
            }
        });
        //返回按钮的事件
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangePasswordFrame.this.dispose();
            }
        });
    }

    /**
     * 显示界面
     */
    private void showFrame(){
        int screen_height = (int)this.getToolkit().getScreenSize().getHeight();
        int screen_width = (int)this.getToolkit().getScreenSize().getWidth();
        this.setLocation((screen_width-WIDTH)/2,(screen_height-HEIGHT)/2);
        this.setSize(WIDTH,HEIGHT);
        this.setVisible(true);
    }
}
