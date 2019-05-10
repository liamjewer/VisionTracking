package com.liam.tracker;

import com.github.sarxos.webcam.Webcam;

import java.awt.*;
import java.awt.image.BufferedImage;

class CameraInput {
    private static Webcam webcam;
    static void initialize(int camera){
        webcam = Webcam.getWebcams().get(camera);
        webcam.setViewSize(new Dimension(640, 480));
        webcam.open();
    }

    static BufferedImage getInput(){
        return webcam.getImage();
    }
}
