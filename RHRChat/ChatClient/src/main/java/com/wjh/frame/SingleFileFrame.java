package com.wjh.frame;

import com.wjh.bean.User;
import com.wjh.netty.NettyClient;
import com.wjh.service.OnlineService;
import com.wjh.util.TimeUtil;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SingleFileFrame extends JFrame {
    private JLabel chooseFileLabel;
    private JButton chooseFile;
    private JLabel filePathLabel;
    private JTextArea filePath;
    private JButton sendFile;
    private User user;
    private User friend;
    private static final int width = 630;
    private static final int height = 250;
    public SingleFileFrame(User user,User friend){
        this.user = user;
        this.friend = friend;
        init();
        addComponent();
        addListener();
        showFrame();
    }
    private void init(){
        chooseFileLabel = new JLabel("请选择文件：");
        chooseFile = new JButton("···");
        filePathLabel = new JLabel("文件路径：");
        filePath = new JTextArea();
        filePath.setEditable(false);
        sendFile = new JButton("确认发送");
    }
    private void addComponent(){
        this.setLayout(null);
        this.add(chooseFile);
        this.add(chooseFileLabel);
        this.add(filePath);
        this.add(filePathLabel);
        this.add(sendFile);

        chooseFileLabel.setBounds(0,10,100,50);
        chooseFile.setBounds(100,10,100,50);
        filePathLabel.setBounds(0,80,100,50);
        filePath.setBounds(100,80,490,50);
        sendFile.setBounds(490,150,100,50);
    }
    private void addListener(){
        sendFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(filePath.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"请选择文件");
                }
                SingleFileFrame.this.dispose();
                new OnlineService().singleFile(user,friend,filePath.getText(),TimeUtil.getCurrentTime());
            }
        });
        chooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = 0;
                File file = null;
                String path = null;
                JFileChooser fileChooser = new JFileChooser();
                FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
                System.out.println(fsv.getHomeDirectory());                //得到桌面路径
                fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
                fileChooser.setDialogTitle("请选择要上传的文件...");
                fileChooser.setApproveButtonText("确定");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                result = fileChooser.showOpenDialog(filePath);
                if (JFileChooser.APPROVE_OPTION == result) {
                    path=fileChooser.getSelectedFile().getPath();
                    filePath.setText(path);
                }
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

}
