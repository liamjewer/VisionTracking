package com.liam.tracker;

import java.awt.*;

public class Blob {
    int minX, maxX, minY, maxY;
    private int points;

    public Blob(int x, int y){
        minX = x;
        maxX = x;
        minY = y;
        maxY = y;
    }

    void add(int x, int y){
        maxX = Math.max(x, maxX);
        minX = Math.min(x, minX);
        maxY = Math.max(y, maxY);
        minY = Math.min(y, minY);
        points++;
    }

    void render(Graphics2D g){
        g.draw(getBounds());
    }

    double getArea(){
        return points;
    }

    Rectangle getBounds(){
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }
}
