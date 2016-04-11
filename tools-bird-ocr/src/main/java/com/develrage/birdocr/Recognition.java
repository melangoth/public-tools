package com.develrage.birdocr;

import com.develrage.birdocr.helpers.Helper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

// TODO: library use, do not log, do not catch but throw exceptions!
public class Recognition {
    private static final Logger log = Logger.getLogger(Recognition.class);
    private OcrMap useMap;
    private DigitCollection useCollection;

    @Deprecated
    public Recognition(OcrMap ocrmap) throws JsonParseException,
            JsonMappingException, IOException {
        String json = Helper.getFileContent("global_resources/ocrmaps/" + ocrmap + ".json");
        useCollection = DigitCollection.getInstanceFromJson(json);
        useMap = useCollection.getOcrmap();
    }

    public Recognition(String mapPath) throws IOException {
        String json = Helper.getFileContent(mapPath);
        useCollection = DigitCollection.getInstanceFromJson(json);
        useMap = useCollection.getOcrmap();
    }

    @Deprecated
    public Recognition(OcrMap ocrMap, String mapPath) throws IOException {
        String json = Helper.getFileContent(mapPath);
        useCollection = DigitCollection.getInstanceFromJson(json);
        useMap = useCollection.getOcrmap();
    }

    @Deprecated
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

//        System.out.println(image.getColorModel());

        DigitMap[] digitmaps = extractDigitMapsFromImage(image);

        // create and return DigitCollection
        DigitCollection dc = new DigitCollection();
        dc.setOcrmap(useMap);
        for (int dmi = 0; dmi < digitmaps.length; dmi++) {
            digitmaps[dmi].setDigit(digitsOrder[dmi]);
            dc.digitmaps.add(digitmaps[dmi]);

            // draw
            log.debug("");
            log.debug("Digit: " + digitsOrder[dmi] + " comes next...");
            log.debug(getDigitStringFromDigitMap(digitmaps[dmi]));
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
            DigitMap dm = getDigitMapOfImageRectagle(image, new ImageRectangle(rectangles.get(recti)));
            digitmaps[recti] = dm;
        } // for numbers
        return digitmaps;
    }

    public DigitMap getDigitMapOfImageRectagle(BufferedImage image, ImageRectangle ir) {
        // digitmap
        int imgX = ir.getX();
        int imgY = ir.getY();
        int imgWidth = ir.getWidth();
        int imgHeight = ir.getHeight();

        DigitMap dm = new DigitMap(imgHeight, imgWidth);

        for (int y = imgY; y < imgY + imgHeight; y++) {
            for (int x = imgX; x < imgX + imgWidth; x++) {
                Color color = new Color(image.getRGB(x, y));
                int dx = x - imgX;
                int dy = y - imgY;

                if (isNumPixel(color)) {
                    dm.bitmap[dy][dx] = 1;
                } else {
                    dm.bitmap[dy][dx] = 0;
                }
            } // for x
        } // for y
        dm.calcMetadata();
        return dm;
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

    public int getFirstColOfInterestFromImageRectange(BufferedImage image, ImageRectangle imageRectangle) {
        int x;
        int y;

        for (x = imageRectangle.getX(); x < imageRectangle.getWidth(); x++) {
            boolean foundCOI = false;
            for (y = imageRectangle.getY(); y < imageRectangle.getHeight(); y++) {
                Color c = new Color(image.getRGB(x, y));
                if (isNumPixel(c)) {
                    foundCOI = true;
                    break;
                }
            }
            if (foundCOI) break;
        }

        return x;
    }

    public int getFirstRowOfInterestFromImageRectange(BufferedImage image, ImageRectangle imageRectangle) {
        int x;
        int y;

        for (y = imageRectangle.getY(); y < imageRectangle.getHeight(); y++) {
            boolean foundCOI = false;
            for (x = imageRectangle.getX(); x < imageRectangle.getWidth(); x++) {
                Color c = new Color(image.getRGB(x, y));
                if (isNumPixel(c)) {
                    foundCOI = true;
                    break;
                }
            }
            if (foundCOI) break;
        }

        return y;
    }

    public String getRecognisedStringFromImage(BufferedImage image) {
        String digits = "";
        DigitCollection recMaps = this.getDigitCollection();
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
        int x = this.getFirstColOfInterestFromImageRectange(image, new ImageRectangle(0, 0, imgWidth, imgHeight));
        log.debug(String.format("Stepping by [%d] in image from [%d] to max [%d, %d]", step, x, imgWidth, imgHeight));

        // start inspecting image
        while (x < imgWidth - minWidth) {
            // get first row of interest in sub-image
            int y = this.getFirstRowOfInterestFromImageRectange(image, new ImageRectangle(x, 0, imgWidth, imgHeight));
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
                        DigitMap tmpdm = this.getDigitMapOfImageRectagle(image, ir);
                        int found = recMaps.findDigit(tmpdm);
                        if (found > -1) {
                            System.out.println(this.getDigitStringFromDigitMap(tmpdm));
                            log.debug(String.format("Found digit: %d in [%d,%d] @ [%d,%d]", found, dm.getWidth(), dm.getHeight(), x, y));
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
        return digits;
    }

    public String getOcrmapName() {
        return this.useMap.toString();
    }

    public enum OcrMap {
        AUTO, WHTHICK, WHTHIN, TANKP, TANKA, TANK
    }
}
