package com.liam.tracker;

import com.github.sarxos.webcam.WebcamResolution;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Main extends Canvas implements Runnable{
    private boolean running = false;
    private ArrayList<Blob> blobs = new ArrayList<>();

    public static void main(String[] args) {
        new Window(WebcamResolution.HD.getSize().width, WebcamResolution.HD.getSize().height, "tracking", new Main());
    }

    @Override
    public void run() {
        init();
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while(running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1){
                tick();
                updates++;
                delta--;
            }
            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                System.out.println("FPS: " + frames + " TICKS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }
    private void init() {
        CameraInput.initialize();
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D)g;

        //all graphical elements in here
        g.drawImage(CameraInput.getInput(), 0,0, null);
        g.drawString("Device Name: " + CameraInput.webcam.getName(), 10, 15);
        g.drawString("FPS: " + Math.round(CameraInput.webcam.getFPS()), 10, 30);
        ArrayList<Point> points = ColourFinder.getPointsByColor(CameraInput.getInput());
        g.setColor(Color.WHITE);
        //for (Point point : points) g.drawRect((int) point.getX(), (int) point.getY(), 1, 1);
        blobs = ColourFinder.blobify(points);
        if(blobs != null) {
            for (Blob b : blobs) {
                b.render(g2d);
            }
            blobs.clear();
        }

        g.dispose();
        bs.show();
    }

    private void tick() {}
    void start() {
        if(running)
            return;

        running = true;
        Thread thread = new Thread(this);
        thread.start();
    }
}
