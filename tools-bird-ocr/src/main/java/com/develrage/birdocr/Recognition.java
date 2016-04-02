package com.develrage.birdocr;

import com.develrage.birdocr.helpers.Helper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

// TODO: library use, do not log, do not catch but throw exceptions!
public class Recognition {
    OcrMap useMap;
    DigitCollection useCollection;

    public Recognition(OcrMap ocrmap) throws JsonParseException,
            JsonMappingException, IOException {
        String json = Helper.getFileContent("global_resources/ocrmaps/" + ocrmap + ".json");
        useCollection = DigitCollection.getInstanceFromJson(json);
        useMap = useCollection.getOcrmap();
    }

    public Recognition(OcrMap ocrMap, String mapPath) throws IOException {
        String json = Helper.getFileContent(mapPath);
        useCollection = DigitCollection.getInstanceFromJson(json);
        useMap = useCollection.getOcrmap();
    }

    public Recognition(OcrMap ocrmap, BufferedImage image, int[] digitsOrder) {
        useMap = ocrmap;
        useCollection = this.createMap(image, digitsOrder);
    }

    public boolean isNumPixel(Color color) {
        // TODO: This is hardcoded. Make Reco. params!
        boolean judge = false;
        int greyscale;

        // WHTHICK
        switch (useMap) {
            case TANKA:
                greyscale = 175;
                judge = (color.getRed() > greyscale && color.getGreen() > greyscale && color
                        .getBlue() > greyscale);
                break;
            case TANKP:
                greyscale = 155;
                judge = (color.getRed() > greyscale && color.getGreen() > greyscale && color
                        .getBlue() > greyscale);
                break;
            case WHTHICK:
                judge = (color.getRed() > 200 && color.getGreen() > 200 && color
                        .getBlue() > 200);
                break;

            default:
                judge = false;
                break;
        }

        return judge;
    }

    private DigitCollection createMap(BufferedImage image, int[] digitsOrder) {

        System.out.println(image.getColorModel());

        DigitMap[] digitmaps = extractDigitMapsFromImage(image);

        // create and return DigitCollection
        DigitCollection dc = new DigitCollection();
        dc.setOcrmap(useMap);
        for (int dmi = 0; dmi < digitmaps.length; dmi++) {
            digitmaps[dmi].setDigit(digitsOrder[dmi]);
            dc.digitmaps.add(digitmaps[dmi]);

            // draw
            System.out.println();
            System.out.println("Digit: " + digitsOrder[dmi] + " comes next...");
            System.out.println(getDigitStringFromDigitMap(digitmaps[dmi]));
        }
        dc.calcMetaData();
        return dc;
    }

    public DigitMap[] extractDigitMapsFromImage(BufferedImage image) {
        // rectangle holder
        ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();
        Rectangle tempRect = null;

        // get top left / get rectangle
        for (int x = 0; x < image.getWidth(); x++) {
            boolean stillNum = false;
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));

                if (isNumPixel(color)) {
                    stillNum = true;
                    if (tempRect == null) {
                        tempRect = new Rectangle(-1, -1, -1, -1);
                        tempRect.x = x;
                    } else {
                        if (tempRect.y == -1 || y < tempRect.y) {
                            tempRect.y = y;
                        }
                        if (y > tempRect.height) {
                            tempRect.height = y;
                        }
                        if (x > tempRect.width) {
                            tempRect.width = x;
                        }
                    }
                } // isNumPixel
            } // for y
            if (tempRect != null && !stillNum) {
                tempRect.width = tempRect.width - tempRect.x + 1; // +1 coz its
                // not coord
                // but size!
                tempRect.height = tempRect.height - tempRect.y + 1;
                rectangles.add(tempRect);
                tempRect = null;
            }
        } // for x

        // draw and build maps
        DigitMap[] digitmaps = new DigitMap[rectangles.size()];
        for (int recti = 0; recti < rectangles.size(); recti++) {
            Rectangle r = rectangles.get(recti);

            // digitmap
            DigitMap dm = new DigitMap(r.height, r.width);

            for (int y = r.y; y < r.y + r.height; y++) {
                for (int x = r.x; x < r.x + r.width; x++) {
                    Color color = new Color(image.getRGB(x, y));
                    int dx = x - r.x;
                    int dy = y - r.y;

                    if (isNumPixel(color)) {
                        dm.bitmap[dy][dx] = 1;
                    } else {
                        dm.bitmap[dy][dx] = 0;
                    }
                } // for x
            } // for y
            dm.calcMetadata();
            digitmaps[recti] = dm;
        } // for numbers
        return digitmaps;
    }

    public String getDigitStringFromDigitMap(DigitMap digitmap) {
        String ss = "";
        for (int y = 0; y < digitmap.height; y++) {
            String s = "";
            for (int x = 0; x < digitmap.width; x++) {

                if (digitmap.bitmap[y][x] == 1) {
                    s += "X";
                } else {
                    if (x % 2 == 0) {
                        s += "_";
                    } else {
                        s += " ";
                    }
                }
            } // for x
            ss += "\n" + s;
        } // for y
        return ss;
    }

    public DigitCollection getDigitCollection() {
        return this.useCollection;
    }

    public enum OcrMap {
        AUTO, WHTHICK, WHTHIN, TANKP, TANKA, TANK
    }
}
