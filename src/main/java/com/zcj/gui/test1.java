package com.zcj.gui;

import javax.swing.*;
import java.awt.*;

/**
 * @Since2017/7/13 ZhaCongJie@HF
 */
public class test1 {
    private  static void createAndShowGUI(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        SimpleJFram frame = new SimpleJFram();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("hello world~~");
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
    }

    static class SimpleJFram extends JFrame{
        private final static int DEFAULT_WIDTH = 400;
        private final static int DEFAULT_HEIGHT = 600;
        public SimpleJFram(){
            setSize(DEFAULT_HEIGHT,DEFAULT_WIDTH);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
