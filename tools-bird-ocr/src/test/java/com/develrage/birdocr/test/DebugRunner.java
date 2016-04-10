package com.develrage.birdocr.test;

import com.develrage.birdocr.DigitCollection;
import com.develrage.birdocr.DigitMap;
import com.develrage.birdocr.ImageRectangle;
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
        runner.testRecognition(Recognition.OcrMap.TANKP, "global_resources/ocrmaps/TANKPI.png");
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
            // get maps for subject
            String digits = "";
            DigitCollection recMaps = rec.getDigitCollection();
            recMaps.setTreshold(90);

            // define boundaries
            int imgWidth = image.getWidth();
            int imgHeight = image.getHeight();
            int minWidth = recMaps.getMinWidth();
            int maxWidth = recMaps.getMaxWidth();
            int minHeight = recMaps.getMinHeight();
            int maxHeight = recMaps.getMaxHeight();
            int step = 1;

            // get first col of interest in image
            int x = rec.getFirstColOfInterestFromImageRectange(image, new ImageRectangle(0, 0, imgWidth, imgHeight));
            log.debug(String.format("Stepping by [%d] in image from [%d] to max [%d, %d]", step, x, imgWidth, imgHeight));

            // start inspecting image
            while (x < imgWidth - minWidth) {
                // get first row of interest in sub-image
                int y = rec.getFirstRowOfInterestFromImageRectange(image, new ImageRectangle(x, 0, imgWidth, imgHeight));
                boolean jump = false;
                while (y < imgHeight - minHeight) {
                    // on actual x,y coordinates, walk through maps
                    for (DigitMap dm : recMaps.getDigitmaps()) {
                        // if actual digit map fits in actual imagerectangle
                        if (imgWidth - x - dm.getWidth() >= 0 && imgHeight - y - dm.getHeight() >= 0) {
                            // define actual ImageRectangle
                            ImageRectangle ir = new ImageRectangle(x, y, dm.getWidth(), dm.getHeight());
                            log.trace(String.format("ImageRectangle [%d, %d, %d, %d]", ir.getX(), ir.getY(), ir.getWidth(), ir.getHeight()));

                            // check if found any digit
                            DigitMap tmpdm = rec.getDigitMapOfImageRectagle(image, ir);
                            int found = recMaps.findDigit(tmpdm);
                            if (found > -1) {
                                System.out.println(rec.getDigitStringFromDigitMap(tmpdm));
                                log.debug(String.format("Found digit: %d in [%d,%d]", found, dm.getWidth(), dm.getHeight()));
                                digits += Integer.toString(found);
                                x += tmpdm.getWidth();
                                y = 0;
                                jump = true;
                                break;
                            }
                        }
                    }
                    if (jump) break;
                    // step
                    y++;
                }

                // step
                if (!jump) x += step;
            }
            log.info("Found digits: " + digits);

        } else {
            log.warn("Image is null!");
        }
    }
}
