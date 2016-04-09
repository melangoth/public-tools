package com.develrage.birdocr.test;

import com.develrage.birdocr.helpers.Helper;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;


/**
 * Created by develrage
 */
public class HelperTest {
    private static final Logger log = Logger.getLogger(HelperTest.class);

    @Test(expected = FileNotFoundException.class)
    public void test_getFileContent_fromFS_expectFNFE() throws Exception {
        Helper.getFileContent("global_resources/test_unexists.txt");
    }

    @Test
    public void test_getFileContent_fromFS_unexists() {
        String content = null;
        try {
            content = Helper.getFileContent("global_resources/test_unexists.txt");
        } catch (FileNotFoundException e) {
            log.trace("Exception:", e);
        }
        log.debug(String.format("content: %s", content));
        assert content == null;
    }

    @Test
    public void test_getFileContent_fromFS() {
        String content = null;
        try {
            content = Helper.getFileContent(new File("../global_resources/test.txt").getAbsolutePath());
        } catch (FileNotFoundException e) {
            log.trace("Exception:", e);
        }
        log.debug(String.format("content: %s", content));
        assert content.equals("fs test file");
    }

    @Test
    public void test_getFileContent_fromClasspath() {
        String content = Helper.getFileContent("bla", true);

        log.debug(String.format("content: %s", content));
        assert content.equals("bla");
    }
}
