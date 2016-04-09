package com.develrage.birdocr.helpers;

import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Helper {
    private static final Logger log = Logger.getLogger(Helper.class);

    public static String generateId(int length) {
        String s = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        String j = "";
        for (int i = 1; i <= length; i++) {
            j += s.charAt(rnd.nextInt(s.length()));
        }

        return j;
    }

    public static String getFileContent(String path) throws FileNotFoundException {
        if (path.toLowerCase().startsWith("classpath://")) {
            String temp = path.substring("classpath://".length());
            if (temp.contains("/")) {
                String classname = temp.substring(0, temp.indexOf("/"));
                String relative = temp.substring(temp.indexOf("/") + 1);

                try {
                    Class clazz = Class.forName(classname);
                    return getFileContentFromClasspath(clazz, relative);
                } catch (ClassNotFoundException e) {
                    log.warn(String.format("Failed to find class: %s", classname), e);
                    throw new FileNotFoundException(String.format("Invalid class name given in classpath: %s", path));
                }
            } else {
                throw new FileNotFoundException(String.format("Invalid classpath file given: %s", path));
            }
        } else {
            return getFileContentFromFilesystem(path);
        }
    }

    public static String getFileContentFromFilesystem(String path) throws FileNotFoundException {
        InputStream input = new FileInputStream(path);

        return getStringFromInputStream(input);
    }

    public static String getFileContentFromClasspath(Class clazz, String path) throws FileNotFoundException {
        //log.debug(String.format("Getting file from classpath %s/%s", clazz.getName(), path));
        InputStream input = clazz.getClassLoader().getResourceAsStream(path);

        if (input != null) {
            return getStringFromInputStream(input);
        } else {
            throw new FileNotFoundException(String.format(
                    "Failed to open file from classpath %s/%s",
                    clazz.getName(), path));
        }
    }

    private static String getStringFromInputStream(InputStream input) {
        String content;
        int nextByte;

        try {
            content = "";
            while ((nextByte = input.read()) != -1) {
                content += ((char) nextByte);
            }
        } catch (IOException e) {
            log.error("Failed to read from InputStream", e);
            content = null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.warn("Failed to close InputStream", e);
                }
            }
        }

        return content;
    }

    public static void putFileContent(String path, String content) {
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path), "utf-8"));
            writer.write(content);
        } catch (IOException e) {
            log.error("Error writing file", e);
        } finally {
            try {
                writer.close();
            } catch (Exception ex) {
                log.warn("Unable to close writer", ex);
            }
        }
    }

    public static String getTimeString(String format, Date date) {
        if (date == null)
            return "notSet";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date Now() {
        return new Date();
    }

    public static String padInt(int num, int padding) {
        String is = Integer.toString(num);
        if (is.length() < padding) {
            for (int i = 1; i <= padding - is.length(); i++) {
                is = "0" + is;
            }
        }
        return is;
    }

}
