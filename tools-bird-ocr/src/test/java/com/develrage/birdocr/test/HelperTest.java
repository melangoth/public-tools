package com.develrage.birdocr.test;

import com.develrage.birdocr.helpers.Helper;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.FileNotFoundException;


/**
 * Created by develrage
 */
public class HelperTest {
    private static final Logger log = Logger.getLogger(HelperTest.class);

    @Test(expected = FileNotFoundException.class)
    public void test_getFileContent_FromFilesystem_wronPath() throws Exception {
        Helper.getFileContent("global_resources/test_unexists.txt");
    }

    @Test
    public void test_getFileContent_FromFilesystem() throws FileNotFoundException {
        String content = Helper.getFileContent("../global_resources/test.txt");
        log.debug(String.format("content: %s", content));
        assert content.equals("fs test file");
    }

    @Test
    public void test_getFileContent_FromClasspath() throws FileNotFoundException {
        String content = Helper.getFileContentFromClasspath(HelperTest.class, "testfile1.txt");

        log.debug(String.format("content: %s", content));
        assert content.equals("classpath test file 1");
    }

    @Test
    public void test_getFileContent_FromClasspath_subfolder() throws FileNotFoundException {
        String content = Helper.getFileContentFromClasspath(HelperTest.class, "subfolder/testfile2.txt");

        log.debug(String.format("content: %s", content));
        assert content.equals("classpath test file 2");
    }

    @Test(expected = FileNotFoundException.class)
    public void test_getFileContent_fromClasspath_wrongClass() throws FileNotFoundException {
        throw new FileNotFoundException("NOT IPLEMENTED TEST");
    }

    @Test(expected = FileNotFoundException.class)
    public void test_getFileContent_fromClasspath_wrongPath() throws FileNotFoundException {
        throw new FileNotFoundException("NOT IPLEMENTED TEST");
    }
}
