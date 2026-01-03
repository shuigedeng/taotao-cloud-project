package com.taotao.cloud.message.biz.channels.netty;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * ExceptionUtil
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class ExceptionUtil {

    public static String printStackTrace( Exception e ) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        printWriter.close();
        return writer.toString();
    }
}

