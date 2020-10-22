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
 * 登录界面，目前主要是登录，注册，忘记密码
 * @author Mr. Wang
 */
public class LoginFrame extends JFrame {
    //防止全透明化
    JPanel jPanel = new ImagePanel();
    //输入账号组件
    private JLabel idLabel;
    private JTextField idText;
    //输入密码组件
    private JLabel passwordLabel;
    private JPasswordField passwordText;
    //按钮
    private JButton loginButton;//登录按钮
    private JButton registerButton;//注册按钮
    private JButton forgetButton;//忘记密码按钮
    //业务层实例化对象
    private SendService sendService;
    //界面的大小设置
    private static final int WIDTH = 450;
    private static final int HEIGHT = 300;
    public LoginFrame(){
        init();
        addComponent();
        addListener();
        showFrame();
    }

    private static void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }

    /**
     * 初始化各组件
     */
    private void init(){
        idLabel = new JLabel("账号：");
        passwordLabel = new JLabel("密码：");
        idText = new JTextField();
        passwordText = new JPasswordField();
        loginButton = new JButton("登录");
        forgetButton = new JButton("忘记密码");
        registerButton = new JButton("注册");

        sendService = new SendService();

        InitGlobalFont(new Font("Dialog", Font.PLAIN, 16));
        try {
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencySmallShadow;
            org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
            BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("set skin fail!");
        }
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
        passwordLabel.setBounds(50,100,100,50);
        passwordText.setBounds(150,100,200,50);
        loginButton.setBounds(175,150,100,50);
        registerButton.setBounds(0,200,225,50);
        forgetButton.setBounds(225,200,225,50);
        //添加到界面上
        jPanel.add(idLabel);
        jPanel.add(passwordLabel);
        jPanel.add(idText);
        jPanel.add(passwordText);
        jPanel.add(loginButton);
        jPanel.add(registerButton);
        jPanel.add(forgetButton);
    }

    /**
     * 给各组件添加事件
     */
    private void addListener(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //登录事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendService.login(idText.getText(),passwordText.getText());
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(null,e1.getMessage());
                }
            }
        });
        //注册事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame.this.dispose();
                NettyClient.registerFrame = new RegisterFrame();
            }
        });
        //忘记密码事件
        forgetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginFrame.this.dispose();
                NettyClient.forgetPasswordFrame = new ForgetPasswordFrame();
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
