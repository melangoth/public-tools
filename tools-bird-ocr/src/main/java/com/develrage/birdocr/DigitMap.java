package com.develrage.birdocr;

public class DigitMap {
    public int digit;
    public int height;
    public int width;
    public int[][] bitmap;
    public int[] colvalues;

    public DigitMap() {
    }

    public DigitMap(int height, int width) {
        this.width = width;
        this.height = height;
        bitmap = new int[height][width];
    }

    public void calcMetadata() {
        colvalues = new int[this.width];

        for (int x = 0; x < width; x++) {
            int bint = 0;
            for (int y = 0; y < height; y++) {
                if (bitmap[y][x] == 1) {
                    bint += Math.pow(2, y + 1);
                }
            }
            colvalues[x] = bint;
        }
    }

    public int[][] getBitmap() {
        return bitmap;
    }

    public void setBitmap(int[][] bitmap) {
        this.bitmap = bitmap;
    }

    public int[] getColvalues() {
        return colvalues;
    }

    public void setColvalues(int[] colvalues) {
        this.colvalues = colvalues;
    }

    public int getDigit() {
        return digit;
    }

    public void setDigit(int digit) {
        this.digit = digit;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
