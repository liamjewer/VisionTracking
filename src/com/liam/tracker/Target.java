package com.liam.tracker;

import java.awt.*;
import java.util.ArrayList;

public class Target {
    public static Rectangle getAveragePoint(ArrayList<Point> points){
        int totalX = 0;
        int amountX = 0;
        int totalY = 0;
        int amountY = 0;
        int averageX;
        int averageY;

        for (Point point : points) {
            totalX += point.x;
            amountX++;
        }
        for (Point point : points) {
            totalY += point.y;
            amountY++;
        }
        if (amountX != 0 && amountY != 0) {
            averageX = totalX / amountX;
            averageY = totalY / amountY;
            int size = (int) Math.sqrt(points.size());
            return new Rectangle(averageX - size/2, averageY - size/2, size, size);
        }else{
            return null;
        }
    }
}
