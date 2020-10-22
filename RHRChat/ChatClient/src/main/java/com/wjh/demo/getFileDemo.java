package com.wjh.demo;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * 测试通过界面方式获取到操作系统的文件目录结构然后选择文件
 */
public class getFileDemo {
    public static void main(String[] args) {
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
         JFrame chatFrame = new JFrame();
        JTextArea jTextArea = new JTextArea();
        chatFrame.add(jTextArea);
        result = fileChooser.showOpenDialog(jTextArea);
         chatFrame.setSize(300,300);
        chatFrame.setVisible(true);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         if (JFileChooser.APPROVE_OPTION == result) {
                       path=fileChooser.getSelectedFile().getPath();
                       System.out.println("path: "+path);
               }
    }
}
