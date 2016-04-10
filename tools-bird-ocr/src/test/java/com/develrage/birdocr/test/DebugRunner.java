package com.develrage.birdocr.test;

import com.develrage.birdocr.BirdOcrRunner;
import com.develrage.birdocr.Recognition;
import org.apache.log4j.Logger;

/**
 * Created by develrage
 */
public class DebugRunner {
    private static final Logger log = Logger.getLogger(DebugRunner.class);

    public static void main(String[] args) {
        BirdOcrRunner runner = new BirdOcrRunner();
//        runner.testMapCreation(OcrMap.TANKA, "global_resources/ocrmaps/TANKA.png", new int[]{1, 0, 2, 3, 4, 5, 6, 7, 8, 9}, "global_resources/ocrmaps/TANKA.json");
//        runner.testRecognition(OcrMap.TANKA, "global_resources/ocrmaps/TANKA.png");
        runner.testRecognition(Recognition.OcrMap.TANKP, "global_resources/ocrmaps/TANKPI.png");
    }
}
