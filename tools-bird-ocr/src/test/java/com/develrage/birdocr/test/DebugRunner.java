package com.develrage.birdocr.test;

import com.develrage.birdocr.Recognition;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by develrage
 */
public class DebugRunner {
    private static final Logger log = Logger.getLogger(DebugRunner.class);

    public static void main(String[] args) {
        DebugRunner runner = new DebugRunner();
//        runner.testMapCreation(OcrMap.TANKA, "global_resources/ocrmaps/TANKA.png", new int[]{1, 0, 2, 3, 4, 5, 6, 7, 8, 9}, "global_resources/ocrmaps/TANKA.json");
//        runner.testRecognition(OcrMap.TANKA, "global_resources/ocrmaps/TANKA.png");
        runner.testRecognition(Recognition.OcrMap.TANKP, "global_resources/tankp_test.png");
    }

    public void testRecognition(Recognition.OcrMap mapEnum, String subjectSourcePath) {
        // holder for recognition map
        Recognition rec = null;
        BufferedImage image = null;

        try {
            // load map and image
            rec = new Recognition(mapEnum);
            File file = new File(
                    subjectSourcePath);
            FileInputStream fis = new FileInputStream(file);
            image = ImageIO.read(fis);
        } catch (FileNotFoundException e) {
            log.error("Failed to find image file", e);
        } catch (IOException e) {
            log.error("Failed to load image file", e);
        }

        // analyse image
        if (image != null) {
            // get digits from subject
            String digits = rec.getRecognisedStringFromImage(image);
            log.info("Found digits: " + digits);

        } else {
            log.warn("Image is null!");
        }
    }
}
