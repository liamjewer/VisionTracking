package com.liam.tracker;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class ColourFinder {
    static ArrayList<Point> getPointsByColor(BufferedImage image, float min, float max, double sThreshold){
        int w = image.getWidth();
        int h = image.getHeight();

        ArrayList<Point> points = new ArrayList<>();

        for(int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int pixel = image.getRGB(x, y);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = (pixel) & 0xff;
                float[] hsb = new float[3];
                Color.RGBtoHSB(r, g, b, hsb);

                if (!(hsb[1] < 0.1 && hsb[2] > 0.9) && !(hsb[2] < 0.1)){
                    float deg = hsb[0]*360;
                    if ((deg >= min && deg <  max) && (hsb[1] > sThreshold)) points.add(new Point(x, y));
                }
            }
        }
        return points;
    }
}
