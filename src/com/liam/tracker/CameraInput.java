package com.liam.tracker;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import java.awt.image.BufferedImage;

class CameraInput {
    static Webcam webcam;
    static void initialize(){
        webcam = Webcam.getWebcams().get(0);
        webcam.setCustomViewSizes(WebcamResolution.HD.getSize());
        webcam.setViewSize(WebcamResolution.HD.getSize());
        webcam.open();
    }

    static BufferedImage getInput(){
        return webcam.getImage();
    }
}
