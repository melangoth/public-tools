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
    public void test_getFileContent_PfsWrongPath() throws FileNotFoundException {
        log.info("test_getFileContent_PfsWrongPath");
        Helper.getFileContent("global_resources/test_unexists.txt");
    }

    @Test
    public void test_getFileContent_Pfs() throws FileNotFoundException {
        log.info("test_getFileContent_Pfs");
        String content = Helper.getFileContent("../global_resources/test.txt");
        log.debug(String.format("content: %s", content));
        assert content.equals("fs test file");
    }

    @Test
    public void test_getFileContent_Pcp() throws FileNotFoundException {
        log.info("test_getFileContent_Pcp");
        String content = Helper.getFileContent("classpath://com.develrage.birdocr.test.HelperTest/testfile1.txt");

        log.debug(String.format("content: %s", content));
        assert content.equals("classpath test file 1");
    }

    @Test
    public void test_getFileContentFromClasspath_Pclass_Psubfolder() throws FileNotFoundException {
        log.info("test_getFileContentFromClasspath_Pclass_Psubfolder");
        String content = Helper.getFileContentFromClasspath(HelperTest.class, "subfolder/testfile2.txt");

        log.debug(String.format("content: %s", content));
        assert content.equals("classpath test file 2");
    }

    @Test(expected = Test.None.class)
    public void test_getFileContent_PprdClass_Ppath() throws FileNotFoundException {
        log.info("test_getFileContent_PprdClass_Ppath");
        Helper.getFileContent("classpath://com.develrage.birdocr.helpers.Helper/log4j.properties");
    }

    @Test(expected = FileNotFoundException.class)
    public void test_getFileContent_PcpWrongClass() throws FileNotFoundException {
        log.info("test_getFileContent_PcpWrongClass");
        Helper.getFileContent("classpath://com.develrage.birdocr.test.HelperTester/testfile1.txt");
    }

    @Test(expected = FileNotFoundException.class)
    public void test_getFileContent_PcpWrongPath() throws FileNotFoundException {
        log.info("test_getFileContent_PcpWrongPath");
        Helper.getFileContent("classpath://com.develrage.birdocr.test.HelperTesttestfile1.txt");
    }

    @Test(expected = FileNotFoundException.class)
    public void test_getFileContent_PcpNoPackage() throws FileNotFoundException {
        log.info("test_getFileContent_PcpNoPackage");
        Helper.getFileContent("classpath://HelperTest/testfile1.txt");
    }
}
