package com.dubravsky.arcache;

import com.dubravsky.arcache.utils.ResourcesUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

public class CompressionTest {

    private Arcache arcache;

    @BeforeMethod
    public void init() throws IOException, URISyntaxException {
        arcache = Arcache.builder().capacityInBytes(10_000_000).build();
    }

    @DataProvider(name = "LongTexts")
    public static Object[][] longTexts() throws IOException, URISyntaxException {
        return new Object[][]{
                {ResourcesUtils.readFromResources("texts/long-text-01.txt")},
                {ResourcesUtils.readFromResources("texts/long-text-02.txt")},
                {ResourcesUtils.readFromResources("texts/long-text-03.txt")}
        };
    }

    @Test(dataProvider = "LongTexts")
    public void shouldCompressLongText(String longText) {
        arcache.put("key01", longText);

        assertTrue(arcache.sizeInBytes() > 0);
        assertTrue(arcache.sizeInBytes() < longText.length() / 3);
    }

    @DataProvider(name = "ShortTexts")
    public static Object[][] shortTexts() throws IOException, URISyntaxException {
        return new Object[][]{
                {ResourcesUtils.readFromResources("texts/short-text-01.txt")},
                {ResourcesUtils.readFromResources("texts/short-text-02.txt")}
        };
    }

    @Test(dataProvider = "ShortTexts")
    public void shouldNotCompressShortText(String shortText) {
        arcache.put("key01", shortText);

        assertTrue(arcache.sizeInBytes() > 0);
        assertEquals(arcache.sizeInBytes(), shortText.length());
    }

}
