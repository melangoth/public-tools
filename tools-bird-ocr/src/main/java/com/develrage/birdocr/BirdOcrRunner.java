package com.develrage.birdocr;

import com.develrage.birdocr.Recognition.OcrMap;
import com.develrage.birdocr.helpers.Helper;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BirdOcrRunner {

    public static void main(String[] args) {
        BirdOcrRunner runner = new BirdOcrRunner();
        // runner.testMapCreation();
        runner.testRecognition();
    }

    public void testRecognition() {
        DigitMap[] maps = null;
        Recognition rec = null;

        try {
            rec = new Recognition(OcrMap.WHTHICK);
            File file = new File(
                    "global_resources/caps/thick/all_mod.png");
            FileInputStream fis = new FileInputStream(file);

            // given image
            BufferedImage image = ImageIO.read(fis);
            maps = rec.extractDigitMapsFromImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (maps != null && rec != null) {
            for (DigitMap map : maps) {
                System.out.println(rec.getDigitStringFromDigitMap(map));
                int digit = rec.getDigitCollection().findDigit(map);
                System.out.println("\nRecognized: " + digit);
            }
        } else {
            System.out.println("Something is null");
        }
    }

    public void testMapCreation() {
        // create map from image
        DigitCollection dc = null;
        Recognition rec;

        try {
            File file = new File(
                    "global_resources/caps/thick/all.png");
            FileInputStream fis = new FileInputStream(file);

            // given image
            BufferedImage image = ImageIO.read(fis);
            rec = new Recognition(OcrMap.WHTHICK, image, new int[]{4, 1, 8,
                    2, 9, 7, 6, 0, 3, 5});
            dc = rec.getDigitCollection();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create json from map pojo, and save to file
        String json = null;
        try {
            json = dc.jsonFromDigitCollection();
            System.out.println();
            System.out.println(json);

            Helper.putFileContent("ocrmaps/" + dc.ocrmap + ".json", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // create map pojo from json, and validate it by saving itself to file
    /*
	 * try { DigitCollection dcin =
	 * DigitCollection.getInstanceFromJson(json);
	 * Helper.putFileContent("ocrmaps/" + dc.ocrmap + "_2.json",
	 * dcin.jsonFromDigitCollection()); } catch (JsonParseException e) {
	 * e.printStackTrace(); } catch (JsonMappingException e) {
	 * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
	 */
    }
}
