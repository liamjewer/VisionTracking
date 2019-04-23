package com.liam.tracker;

import com.github.sarxos.webcam.Webcam;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Main extends Canvas implements Runnable{
    private boolean running = false;
    private Thread thread;
    Webcam webcam;

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
        webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null){
            this.createBufferStrategy(3);
            return;
        }

        Graphics gr = bs.getDrawGraphics();

        gr.drawImage(webcam.getImage(), 0, 0, null);

        gr.setColor(Color.green);

        BufferedImage image = webcam.getImage();
        int w = image.getWidth();
        int h = image.getHeight();

        for(int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int pixel = image.getRGB(x, y);
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = (pixel) & 0xff;
                float hsb[] = new float[3];
                Color.RGBtoHSB(r, g, b, hsb);

                if      (hsb[1] < 0.1 && hsb[2] > 0.9) nearlyWhite(x, y, gr);
                else if (hsb[2] < 0.1) nearlyBlack(x, y, gr);
                else {
                    float deg = hsb[0]*360;
                    if ((deg >=   0 && deg <  10) && (hsb[1] > 0.6)) targetColor(x, y, gr);
                }
            }
        }

        // TODO: 2019-04-21  look through image to find "chunks" of blue and outline them with drawrect

        gr.dispose();
        bs.show();
    }

    private void targetColor(int x, int y, Graphics g) {
        g.fillRect(x, y, 1, 1);
    }
    private void nearlyBlack(int x, int y, Graphics g) {

    }
    private void nearlyWhite(int x, int y, Graphics g) {

    }

    private void tick() {

    }
    public void start() {
        if(running)
            return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }
}
