package com.develrage.birdocr.helpers;

import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

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
        String fileContent = null;
        File f = new File(path);
        Scanner sc = null;
        try {
            sc = new Scanner(f);
            fileContent = "";
            while (sc.hasNextLine()) {
                fileContent += sc.nextLine();
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            if (sc != null) {
                try {
                    sc.close();
                } catch (Exception e) {
                    log.warn("Unable to close Scanner", e);
                }
            }
        }

        return fileContent;
    }

    public static String getFileContentFromClasspath(Class clazz, String path) {
        InputStream input = clazz.getClassLoader().getResourceAsStream(path);

        String content = null;
        if (input != null) {
            try {
                content = getStringFromInputStream(input);
            } catch (IOException e) {
                log.error(String.format("Failed to read from InputStream %s/%s",
                        clazz.getName(), path), e);
            }

            return content;
        } else {
            throw new RuntimeException(String.format(
                    "Failed to open file from classpath %s/%s",
                    clazz.getName(), path));
        }
    }

    private static String getStringFromInputStream(InputStream input) throws IOException {
        String content = "";
        int nextByte;

        while ((nextByte = input.read()) != -1) {
            content += ((char) nextByte);
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
