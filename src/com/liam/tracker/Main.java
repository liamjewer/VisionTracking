package com.liam.tracker;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Main extends Canvas implements Runnable{
    private boolean running = false;
    private Thread thread;

    public static void main(String[] args) {
        new Window(640, 480, "tracking", new Main());
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
        CameraInput.initialize(1);
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
        ArrayList<Point> points = ColourFinder.getPointsByColor(CameraInput.getInput(), 0, 10, 0.9);
        g.setColor(Color.WHITE);
        //for (Point point : points) g.drawRect((int) point.getX(), (int) point.getY(), 1, 1);
        Rectangle avg = Target.getAveragePoint(points);
        g.setColor(Color.GREEN);
        if (avg != null) {
            g2d.draw(avg);
            g.setColor(Color.GREEN);
            g2d.fillOval((int)avg.getCenterX() - 5, (int)avg.getCenterY() - 5, 10, 10);
            g.setColor(Color.BLACK);
            for(int i = 20; i < 60; i += 10)
                g2d.drawOval((int)avg.getCenterX() - i/2, (int)avg.getCenterY() - i/2, i, i);
        }

        g.dispose();
        bs.show();
    }

    private void tick() {
    }
    void start() {
        if(running)
            return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }
}
