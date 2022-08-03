package com.taotao.cloud.sys.biz.api.controller.tools.classloader;

import org.apache.commons.lang3.StringUtils;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.api.OutputSinkFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

/**
 * 反编译 class 文件
 */
@Service
public class DeCompileService {

    /**
     * 反编译 class 文件
     * @param classFile class文件
     * @return
     */
    public String deCompile(File classFile){
        HashMap<String, String> options = new HashMap<String, String>();
        /**
         * @see com.reader.util.MiscConstants.Version.getVersion() Currently,
         *      the cfr version is wrong. so disable show cfr version.
         */
        options.put("showversion", "false");

        StringBuilder result = new StringBuilder(8192);
        OutputSinkFactory outputSinkFactory = new CustomOutputSinkFactory(result);

        CfrDriver driver = new CfrDriver.Builder().withOptions(options).withOutputSink(outputSinkFactory).build();
        List<String> toAnalyse = new ArrayList<String>();
        toAnalyse.add(classFile.getAbsolutePath());
        driver.analyse(toAnalyse);

        return result.toString();
    }

    public static final class CustomOutputSinkFactory implements OutputSinkFactory{
        private StringBuilder result;

        public CustomOutputSinkFactory(StringBuilder result) {
            this.result = result;
        }

        @Override
        public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
            return Arrays.asList(SinkClass.STRING, SinkClass.DECOMPILED, SinkClass.DECOMPILED_MULTIVER,
                    SinkClass.EXCEPTION_MESSAGE);
        }

        @Override
        public <T> Sink<T> getSink(final SinkType sinkType, SinkClass sinkClass) {
            return new Sink<T>() {
                @Override
                public void write(T sinkable) {
                    // skip message like: Analysing type demo.MathGame
                    if (sinkType == SinkType.PROGRESS) {
                        return;
                    }
                    result.append(sinkable);
                }
            };
        }
    }
}
