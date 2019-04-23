package com.liam.tracker;

import javax.swing.*;
import java.awt.*;

public class Window {
    public static JFrame frame;
    public Window(int width, int height, String title, Main main){
        main.setPreferredSize(new Dimension(width, height));
        main.setMaximumSize(new Dimension(width, height));
        main.setMinimumSize(new Dimension(width, height));

        frame = new JFrame(title);
        frame.add(main);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
        main.start();
    }
}
