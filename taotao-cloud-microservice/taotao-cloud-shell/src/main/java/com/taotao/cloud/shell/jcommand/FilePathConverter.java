package com.taotao.cloud.shell.jcommand;

import java.nio.file.Files;
import java.nio.file.Paths;

public class FilePathConverter implements IStringConverter<Path> {

    @Override
    public Path convert(String filePath) {
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            return path;
        }
        throw new ParameterException(String.format("文件不存在，path:%s", filePath));
    }
}
