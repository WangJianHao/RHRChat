package com.wjh.frame;

import com.wjh.bean.ChatGroup;
import com.wjh.bean.User;
import com.wjh.netty.NettyClient;
import com.wjh.service.OnlineService;
import com.wjh.service.SendService;
import com.wjh.util.ImagePanel;
import javafx.scene.shape.Circle;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * 好友列表界面
 */
public class FriendListFrame extends JFrame {
    public static Hashtable<Integer,User> friendList = new Hashtable<>(100);
    public static HashMap<Integer, ChatGroup> groups;
    private JPanel jPanel;
    //个人信息部分
    private JPanel personPanel;
    private JLabel userNameLabel;
    private  JLabel emailLaybel;
    //好友列表部分
    public  String mates;
    public  String family;
    public  String define;
    private JPanel friendListPanel ;
    private JTree friendListTree;
    private JTree groupListTree;
    //更改密码按钮
    private JButton changePassword;
    //界面的大小设置
    private static final int WIDTH = 400;
    private static final int HEIGHT = 800;
    //客户端的个人信息
    public User user;

    public FriendListFrame(User user,String mates,String family,String define,HashMap<Integer, ChatGroup> groups){
        this.setTitle(user.getUserName()+"的好友列表");
        this.user = user;
        this.mates = mates;
        this.family = family;
        this.define = define;
        FriendListFrame.groups = groups;
        init();
        addComponent();
        addListener();
        showFrame();
        Circle circle = new Circle();
        jPanel.getGraphics().setColor(Color.BLACK);
        jPanel.getGraphics().drawLine(0,200,400,200);
    }
    /**
     * 初始化各组件
     */
    private void init(){
        jPanel = new JPanel();
        personPanel =  new ImagePanel();
        friendListPanel = new ImagePanel();
        userNameLabel = new JLabel(user.getUserName());
        emailLaybel = new JLabel(user.getEmail());
        Font dialog = new Font("Dialog", 0, 18);
        userNameLabel.setFont(dialog);
        emailLaybel.setFont(dialog);
        changePassword = new JButton("更改密码");
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
        //个人信息部分界面的设置
        personPanel.setLayout(null);
        ImagePanel imagePanel =
                new ImagePanel(200,200,new ImageIcon("ChatClient/icon.png").getImage());
        imagePanel.setBounds(0,0,200,200);
        personPanel.add(imagePanel);
        personPanel.setBounds(0,0,400,200);
        friendListPanel.setBounds(0,200,400,600);
        userNameLabel.setBounds(250,100,100,30);
        emailLaybel.setBounds(250,120,100,30);
        personPanel.add(userNameLabel);
        personPanel.add(emailLaybel);
        //好友列表界面的设置
        // 创建没有父节点和子节点、但允许有子节点的树节点，并使用指定的用户对象对它进行初始化。
        // public DefaultMutableTreeNode(Object userObject)
        DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("同学");
        if(!mates.equals("")) {
            //要求用户名不能有,和#
            String[] split = mates.split(",");
            for (int i = 0; i < split.length;i++){
                //返回来的格式为0#root,1#user这种形式
                String[] split1 = split[i].split("#");
                User user = new User();
                user.setId(Integer.parseInt(split1[0]));
                user.setUserName(split1[1]);
                friendList.put(Integer.parseInt(split1[0]),user);
                node1.add(new DefaultMutableTreeNode(user));
            }
        }
        DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("家人");
       if(!family.equals("")){
           //要求用户名不能有,和#
           String[] split = family.split(",");
           for (int i = 0; i < split.length;i++){
               //返回来的格式为0#root,1#user这种形式
               String[] split1 = split[i].split("#");
               User user = new User();
               user.setId(Integer.parseInt(split1[0]));
               user.setUserName(split1[1]);
               friendList.put(Integer.parseInt(split1[0]),user);
               node2.add(new DefaultMutableTreeNode(user));
           }
       }
        DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("默认分组");
        if(!define.equals("")){
            //要求用户名不能有,和#
            String[] split = define.split(",");
            for (int i = 0; i < split.length;i++){
                //返回来的格式为0#root,1#user这种形式
                String[] split1 = split[i].split("#");
                User user = new User();
                user.setId(Integer.parseInt(split1[0]));
                user.setUserName(split1[1]);
                friendList.put(Integer.parseInt(split1[0]),user);
                node3.add(new DefaultMutableTreeNode(user));
            }
        }
        DefaultMutableTreeNode group = new DefaultMutableTreeNode("群聊");
        Iterator<Map.Entry<Integer, ChatGroup>> iterator = groups.entrySet().iterator();
        while (iterator.hasNext()){
            ChatGroup value = iterator.next().getValue();
            group.add(new DefaultMutableTreeNode(value));
        }
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("好友列表");
        top.add(node1);
        top.add(node2);
        top.add(node3);
        friendListTree = new JTree(top);
        groupListTree = new JTree(group);
        friendListPanel.setLayout(null);
        groupListTree.setLayout(null);
        friendListTree.setBounds(0,0,400,250);
        groupListTree.setBounds(0,250,400,250);
        friendListPanel.add(friendListTree);
        friendListPanel.add(groupListTree);
        changePassword.setBounds(0,500,100,50);
        friendListPanel.add(changePassword);
        //添加到界面上
        jPanel.add(personPanel);
        jPanel.add(friendListPanel);

    }

    /**
     * 给各组件添加事件
     */
    private void addListener(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 添加选择事件
        friendListTree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) friendListTree
                        .getLastSelectedPathComponent();

                if (node == null)
                    return;

                Object object = node.getUserObject();
                if (node.isLeaf()) {
                    User user = (User) object;
                    if(NettyClient.singleChats.containsKey(user)){
                        NettyClient.singleChats.get(user).setVisible(true);
                    }else {
                        SingleChatFrame singleChatFrame = new SingleChatFrame(FriendListFrame.this.user, user);
                        NettyClient.singleChats.put(user, singleChatFrame);
                    }
                }

            }
        });
        groupListTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) groupListTree
                        .getLastSelectedPathComponent();

                if (node == null)
                    return;

                Object object = node.getUserObject();
                if (node.isLeaf()) {
                    ChatGroup group = (ChatGroup) object;
                    //自己和选择的群
                    if(NettyClient.groupChats.containsKey(group)){
                        NettyClient.groupChats.get(group).setVisible(true);
                    }else {
                        GroupChatFrame singleChatFrame = new GroupChatFrame(FriendListFrame.this.user, group);
                        NettyClient.groupChats.put(group, singleChatFrame);
                    }
                }

            }
        });
        //更改密码的业务逻辑处理
        changePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NettyClient.changePasswordFrame = new ChangePasswordFrame();
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
