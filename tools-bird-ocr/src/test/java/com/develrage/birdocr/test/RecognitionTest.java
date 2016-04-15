package com.develrage.birdocr.test;

import com.develrage.birdocr.Recognition;
import com.develrage.birdocr.helpers.Helper;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by develrage
 */
public class RecognitionTest {
    final static Logger log = Logger.getLogger(RecognitionTest.class);

    @Test
    public void test_initRecognition_pOcrmapPath() throws IOException {
        log.info("test_initRecognition_pOcrmapPath");
        Recognition rec = new Recognition("classpath://com.develrage.birdocr.test.RecognitionTest/ocrmaps/TANKP.json");
        assert rec.getOcrmapName().equals("TANKP");
    }

    @Test
    public void test_getStringFromImage_pClasspath() throws IOException {
        BufferedImage image = null;
        image = Helper.loadImage("classpath://com.develrage.birdocr.test.HelperTest/images/tankp_test.png");

        assert image != null;

        Recognition rec = new Recognition("classpath://com.develrage.birdocr.test.RecognitionTest/ocrmaps/TANKP.json");
        String digits = rec.getRecognisedStringFromImage(image);
        assert digits.equals("66100969486");
    }
}
