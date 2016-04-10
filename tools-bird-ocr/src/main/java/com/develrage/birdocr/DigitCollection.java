package com.develrage.birdocr;

import com.develrage.birdocr.Recognition.OcrMap;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class DigitCollection {
    private static final Logger log = Logger.getLogger(DigitCollection.class);
    public OcrMap ocrmap;
    public int minWidth;
    public int minHeight;
    public int maxWidth;
    public int maxHeight;
    public int treshold = 80;
    public ArrayList<DigitMap> digitmaps = new ArrayList<DigitMap>();

    public static DigitCollection getInstanceFromJson(String json)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper mapperIn = new ObjectMapper();
        mapperIn.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        DigitCollection dc;

        dc = mapperIn.readValue(json, DigitCollection.class);

        return dc;
    }

    public static String jsonFromDigitMap(DigitMap digitmap)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

        return mapper.writeValueAsString(digitmap);

    }

    public static String jsonFromDigitCollection(DigitCollection digitcollection)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

        return mapper.writeValueAsString(digitcollection);
    }

    public int getTreshold() {
        return treshold;
    }

    public void setTreshold(int treshold) {
        this.treshold = treshold;
    }

    /*
     * calc row integer from row binary code (RBC)
     *
     * calc diffs between RBC-s
     *
     * search for RBC-diff matches
     *
     * if fails, diff given map to ref map by RBC-s use log2(x) as below:
     *
     * x=diff(given-RBC, ref-RBC) if log2(x)%1==0 then diff is only one pixel,
     * else diff is more than one pixel
     *
     * diff all RBC, define max pixel difference, sort matches by pixel
     * difference
     */
    public int findDigit(DigitMap givendm, int treshold) {
        int digit = -1;

        for (DigitMap refdm : digitmaps) {
            int match = 100;

            if (refdm.colvalues.length != givendm.colvalues.length) {
                match -= 10;
            }
            for (int i = 0; i < Math.min(givendm.colvalues.length,
                    refdm.colvalues.length); i++) {
                if (refdm.colvalues[i] != givendm.colvalues[i]) {
                    int x = Math.abs(refdm.colvalues[i] - givendm.colvalues[i]);
                    double difflog = Math.log(x) / Math.log(2);
                    if (difflog % 1 == 0) {
                        match -= 1;
                    } else {
                        match -= 10;
                    }
                }
            }
            log.trace(" " + refdm.digit + ":" + match);

            if (match > treshold) {
                digit = refdm.digit;
                treshold = match;
            }
        }

        return digit;
    }

    public int findDigit(DigitMap givendm) {
        return findDigit(givendm, treshold);
    }

    public void calcMetaData() {
        minWidth = maxWidth = minHeight = maxHeight = -1;

        for (DigitMap dm : digitmaps) {
            if (minWidth == -1 || minWidth > dm.width)
                minWidth = dm.width;
            if (maxWidth == -1 || maxWidth < dm.width)
                maxWidth = dm.width;
            if (minHeight == -1 || minHeight > dm.height)
                minHeight = dm.height;
            if (maxHeight == -1 || maxHeight < dm.height)
                maxHeight = dm.height;
        }
    }

    public String jsonFromDigitMap(int digit) throws JsonProcessingException {
        return jsonFromDigitMap(getDigitMap(digit));
    }

    public String jsonFromDigitCollection() throws JsonProcessingException {
        return jsonFromDigitCollection(this);
    }

    public OcrMap getOcrmap() {
        return ocrmap;
    }

    public void setOcrmap(OcrMap ocrmap) {
        this.ocrmap = ocrmap;
    }

    public DigitMap getDigitMap(int digit) {
        DigitMap dm = null;
        for (DigitMap m : this.digitmaps) {
            if (m.digit == digit) {
                dm = m;
                break;
            }
        }

        return dm;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public ArrayList<DigitMap> getDigitmaps() {
        return digitmaps;
    }

    public void setDigitmaps(ArrayList<DigitMap> digitmaps) {
        this.digitmaps = digitmaps;
    }
}
