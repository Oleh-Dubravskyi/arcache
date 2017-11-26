package com.dubravsky.arcache;

import com.dubravsky.arcache.utils.ResourcesUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public class CompressionTest {

    private Arcache arcache;

    @BeforeMethod
    public void init() throws IOException, URISyntaxException {
        arcache = Arcache.builder().limitSize(10_000_000).build();
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

        assertTrue(arcache.dataSize() > 0);
        assertTrue(arcache.dataSize() < longText.length() / 3);
    }

}
