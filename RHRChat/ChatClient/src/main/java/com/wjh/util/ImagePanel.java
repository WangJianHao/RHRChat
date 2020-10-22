package com.wjh.util;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    int width;
    int height;
    Image image;
    public ImagePanel(int width,int height,Image image){
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public ImagePanel() {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image,0,0,width,height,this);

    }
}