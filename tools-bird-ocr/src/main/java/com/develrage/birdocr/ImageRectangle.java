package com.develrage.birdocr;

import java.awt.*;

/**
 * Created by develrage
 */
public class ImageRectangle {
    private int x;
    private int y;
    private int width;
    private int height;

    public ImageRectangle(Rectangle awtRect) {
        x = awtRect.x;
        y = awtRect.y;
        width = awtRect.width;
        height = awtRect.height;
    }

    public ImageRectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
