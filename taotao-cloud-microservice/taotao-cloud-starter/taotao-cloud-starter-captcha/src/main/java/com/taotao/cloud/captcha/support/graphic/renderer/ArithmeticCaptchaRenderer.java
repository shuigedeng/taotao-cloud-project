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
package com.taotao.cloud.captcha.support.graphic.renderer;

import cn.hutool.core.lang.RegexPool;
import com.taotao.cloud.captcha.support.core.definition.domain.Metadata;
import com.taotao.cloud.captcha.support.core.definition.enums.CaptchaCategory;
import com.taotao.cloud.captcha.support.core.provider.RandomProvider;
import com.taotao.cloud.captcha.support.graphic.definition.AbstractBaseGraphicRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.image.BufferedImage;

/**
 * <p>Description: 算数类型一般验证码 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 12:54:40
 */
@Component
public class ArithmeticCaptchaRenderer extends AbstractBaseGraphicRenderer {

    private static final Logger log = LoggerFactory.getLogger(ArithmeticCaptchaRenderer.class);

    private int complexity = 2;
    /**
     * 计算结果
     */
    private String computedResult;

    @Override
    public String getCategory() {
        return CaptchaCategory.ARITHMETIC.getConstant();
    }

    @Override
    protected String getBase64ImagePrefix() {
        return BASE64_PNG_IMAGE_PREFIX;
    }

    @Override
    protected String[] getDrawCharacters() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < complexity; i++) {
            builder.append(RandomProvider.randomInt(10));
            if (i < complexity - 1) {
                int type = RandomProvider.randomInt(1, 4);
                if (type == 1) {
                    builder.append("+");
                } else if (type == 2) {
                    builder.append("-");
                } else if (type == 3) {
                    builder.append("x");
                }
            }
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");

        try {
            computedResult = String.valueOf(engine.eval(builder.toString().replaceAll("x", "*")));
        } catch (ScriptException e) {
            log.error("Arithmetic png captcha eval expression error！", e);
        }

        builder.append("=?");

        String result = builder.toString();
        return result.split("(?!^)");
    }

    @Override
    public Metadata draw() {
        String[] drawContent = getDrawCharacters();
        BufferedImage bufferedImage = createArithmeticBufferedImage(drawContent);

        Metadata metadata = new Metadata();
        metadata.setGraphicImageBase64(toBase64(bufferedImage));
        metadata.setCharacters(this.computedResult);
        return metadata;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.complexity = this.getCaptchaProperties().getGraphics().getComplexity();
    }
}
