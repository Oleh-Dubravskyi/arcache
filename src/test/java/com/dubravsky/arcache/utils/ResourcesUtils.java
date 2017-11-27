package com.dubravsky.arcache.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ResourcesUtils {

    public static String readFromResources(String resourceName) throws IOException, URISyntaxException {
        Path path = Paths.get(ClassLoader.getSystemResource(resourceName).toURI());
        return Files.lines(path).collect(Collectors.joining("\n"));
    }

}
