package com.liam.tracker;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

class ColourFinder {
    private static ArrayList<Blob> blobs = new ArrayList<>();
    private static ArrayList<Blob> toRemove = new ArrayList<>();
    private static boolean found = false;

    static ArrayList<Point> getPointsByColor(BufferedImage image){
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
                    if ((deg >= (float) 0 && deg < (float) 10) && (hsb[1] > 0.9)) points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    static ArrayList<Blob> blobify(ArrayList<Point> points){
        if(points.size() > 0) {
            blobs.add(new Blob(points.get(0).x, points.get(0).y));
            for (Point p : points) {
                for (Blob b : blobs) {
                    var cx = Math.max(Math.min(p.x, b.minX + (b.maxX - b.minX)), b.minX);
                    var cy = Math.max(Math.min(p.y, b.minY + (b.maxY - b.minY)), b.minY);
                    double dist = Math.sqrt((p.x-cx)*(p.x-cx) + (p.y-cy)*(p.y-cy));
                    if (dist < 25) {
                        found = true;
                        b.add(p.x, p.y);
                    }
                }
                if(!found){
                    blobs.add(new Blob(p.x, p.y));
                }else{
                    found = false;
                }
            }
            for (Blob b : blobs) {
                if(b.getArea() < 500){
                    toRemove.add(b);
                }
                for (Blob b2 : blobs) {
                    if(b != b2 && b.getBounds().intersects(b2.getBounds()) && !(toRemove.contains(b2) || toRemove.contains(b))){
                        toRemove.add(b2);
                    }
                }
            }
            for(Blob b : toRemove){
                blobs.remove(b);
            }
            toRemove.clear();
            System.out.println("Blobs found: " + blobs.size());
            return blobs;
        }else{
            return null;
        }
    }
}
