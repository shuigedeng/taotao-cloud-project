/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.shell.jcommand;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;
import java.nio.file.Files;
import java.nio.file.Path;
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
