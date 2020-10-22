package com.wjh.frame;

import com.wjh.netty.NettyClient;
import com.wjh.service.SendService;
import com.wjh.util.ImagePanel;
import io.netty.channel.Channel;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

/**
 * 注册界面
 * @author Mr. Wang
 * @// TODO: 2020/10/15
 */
public class RegisterFrame extends JFrame {
        //防止全透明化
        JPanel jPanel = new ImagePanel();
        //输入邮箱组件
        private JLabel emailLabel;
        private JTextField emailText;
        //输入用户名组件
        private JLabel userNameLabel;
        private JTextField userNameText;
        //输入密码组件
        private JLabel passwordLabel;
        private JPasswordField passwordText;
        //按钮
        private JButton registerButton;//注册按钮
        private JButton backButton;//返回按钮
        //业务层实例化对象
        private SendService sendService;
        //界面的大小设置
        private static final int WIDTH = 450;
        private static final int HEIGHT = 350;
        public RegisterFrame(){
            init();
            addComponent();
            addListener();
            showFrame();
        }


        /**
         * 初始化各组件
         */
        private void init(){
            emailLabel = new JLabel("邮箱：");
            passwordLabel = new JLabel("密码：");
            emailText = new JTextField();
            passwordText = new JPasswordField();
            userNameLabel = new JLabel("用户名：");
            userNameText = new JTextField();

            registerButton = new JButton("注册");
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
            emailLabel.setBounds(50,20,100,50);
            emailText.setBounds(150,20,200,50);
            passwordLabel.setBounds(50,100,100,50);
            passwordText.setBounds(150,100,200,50);
            userNameLabel.setBounds(50,180,100,50);
            userNameText.setBounds(150,180,200,50);
            backButton.setBounds(0,250,225,50);
            registerButton.setBounds(225,250,225,50);
            //添加到界面上
            jPanel.add(emailLabel);
            jPanel.add(passwordLabel);
            jPanel.add(emailText);
            jPanel.add(passwordText);
            jPanel.add(userNameLabel);
            jPanel.add(userNameText);
            jPanel.add(backButton);
            jPanel.add(registerButton);
        }

        /**
         * 给各组件添加事件
         */
        private void addListener(){
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //注册功能的事件
            registerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        sendService.register(emailText.getText(),userNameText.getText(),passwordText.getText());
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null,e1.getMessage());
                    }
                }
            });
            //返回按钮的事件
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    RegisterFrame.this.dispose();
                    NettyClient.loginFrame.setVisible(true);
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
