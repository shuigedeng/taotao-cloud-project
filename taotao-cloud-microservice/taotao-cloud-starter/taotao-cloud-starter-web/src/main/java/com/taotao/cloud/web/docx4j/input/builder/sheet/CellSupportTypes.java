package com.taotao.cloud.web.docx4j.input.builder.sheet;

import com.taotao.cloud.web.docx4j.input.constants.DatetimeConstants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.boot.actuate.autoconfigure.cloudfoundry.AccessLevel;

/**
 * 导入单元格支持数据类型
 */
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
enum CellSupportTypes {
    /**
     * 整型
     */
    BYTE(Byte.class, byte.class) {
        @Override
        Object parse(String text, CellMeta meta) {
            return Byte.parseByte(text);
        }
    },
    SHORT(Short.class, short.class) {
        @Override
        Object parse(String text, CellMeta meta) {
            return Short.parseShort(text);
        }
    },
    INTEGER(Integer.class, int.class) {
        @Override
        Object parse(String text, CellMeta meta) {
            return Integer.parseInt(text);
        }
    },
    LONG(Long.class, long.class) {
        @Override
        Object parse(String text, CellMeta meta) {
            return Long.parseLong(text);
        }
    },
    BIG_INTEGER(BigInteger.class) {
        @Override
        Object parse(String text, CellMeta meta) {
            return new BigInteger(text);
        }
    },
    /**
     * 小数
     */
    FLOAT(Float.class, float.class) {
        @Override
        Object parse(String text, CellMeta meta) {
            return Float.parseFloat(text);
        }
    },
    DOUBLE(Double.class, double.class) {
        @Override
        Object parse(String text, CellMeta meta) {
            return Double.parseDouble(text);
        }
    },
    BIG_DECIMAL(BigDecimal.class) {
        @Override
        Object parse(String text, CellMeta meta) {
            return new BigDecimal(text);
        }
    },
    /**
     * 布尔
     */
    BOOLEAN(Boolean.class, boolean.class) {
        @Override
        Object parse(String text, CellMeta meta) {
            return Boolean.parseBoolean(text);
        }
    },
    /**
     * 字符串
     */
    CHAR(Character.class, char.class) {
        @Override
        Object parse(String text, CellMeta meta) {
            // 空字符串
            if (Objects.isNull(text) || text.isEmpty()) {
                return '\0';
            }

            // 只返回一个
            return text.charAt(0);
        }
    },
    STRING(String.class) {
        @Override
        Object parse(String text, CellMeta meta) {
            // 空字符串处理为null
            if (Objects.nonNull(text) && text.isEmpty()) {
                return null;
            }

            return text;
        }
    },
    /**
     * 日期时间
     */
    LOCAL_TIME(LocalTime.class) {
        @Override
        Object parse(String text, CellMeta meta) {
            return
                Optional.of(meta.dateTimeFormat())
                    .filter(it -> !it.isEmpty())
                    .map(it -> LocalTime.parse(text, DateTimeFormatter.ofPattern(it)))
                    .orElseGet(() -> LocalTime.parse(text, DatetimeConstants.DTF_XLS_HH_MM_SS));
        }
    },
    LOCAL_DATE(LocalDate.class) {
        @Override
        Object parse(String text, CellMeta meta) {
            return
                Optional.of(meta.dateTimeFormat())
                    .filter(it -> !it.isEmpty())
                    .map(it -> LocalDate.parse(text, DateTimeFormatter.ofPattern(it)))
                    .orElseGet(() -> {
                        try {
                            return LocalDate.parse(text, DatetimeConstants.DTF_XLS_YYYY_MM_DD);
                        } catch (Exception ignore) {
                        }

                        return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-M-d"));
                    });
        }
    },
    LOCAL_DATE_TIME(LocalDateTime.class) {
        @Override
        Object parse(String text, CellMeta meta) {
            return
                Optional.of(meta.dateTimeFormat())
                    .filter(it -> !it.isEmpty())
                    .map(it -> LocalDateTime.parse(text, DateTimeFormatter.ofPattern(it)))
                    .orElseGet(() -> {
                        try {
                            return LocalDateTime.parse(text, DatetimeConstants.DTF_XLS_YYYY_MM_DD_HH_MM_SS);
                        } catch (Exception ignore) {
                        }

                        return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"));
                    });
        }
    },
    DATE(Date.class) {
        /**
         * 将字符解析为{@link Date}
         * @param text 字符串
         * @param pattern 日期模式
         * @return {@link Date}
         */
        private Date toDate(String text, String pattern) {
            return
                Date.from(
                    LocalDateTime.parse(text, DateTimeFormatter.ofPattern(pattern))
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                );
        }

        @Override
        Object parse(String text, CellMeta meta) {
            return
                Optional.of(meta.dateTimeFormat())
                    .filter(it -> !it.isEmpty())
                    .map(it -> this.toDate(text, it))
                    .orElseGet(() -> {
                        try {
                            return this.toDate(text, DatetimeConstants.XLS_YYYY_MM_DD);
                        } catch (Exception ignore) {
                        }

                        try {
                            return this.toDate(text, "yyyy-M-d");
                        } catch (Exception ignore) {
                        }

                        try {
                            return this.toDate(text, DatetimeConstants.XLS_YYYY_MM_DD_HH_MM_SS);
                        } catch (Exception ignore) {
                        }

                        return this.toDate(text, "yyyy-M-d H:m:s");
                    });
        }
    };

    Set<Class<?>> classes;

    CellSupportTypes(Class<?>... classes) {
        this.classes = new HashSet<>(Arrays.asList(classes));
    }

    /**
     * 类型解析
     * @param text 字符串
     * @param meta 单元格元信息
     * @return 对应类型
     */
    abstract Object parse(String text, CellMeta meta);

    /**
     * 类型转换
     * @param text 单元格字符串
     * @param meta 单元格元信息
     * @return {@link CellResult}
     */
    CellResult doConvert(String text, CellMeta meta) {
        try {
            // 若为空单元格直接返回null
            return CellResult.ok(Objects.isNull(text) || text.isEmpty() ? null : this.parse(text, meta));
        } catch (Exception ignore) {
            return
                CellResult.fail(
                    String.format(
                        "%s%s",
                        meta.name(),
                        Optional.of(meta.message()).filter(it -> !it.isEmpty()).orElse("数据格式错误")
                    )
                );
        }
    }

    /**
     * 类型转换
     * @param target 目标类型
     * @param text   单元格字符串
     * @param meta   单元格元信息
     * @return {@link CellResult}
     */
    static CellResult convert(Class<?> target, String text, CellMeta meta) {
        if (text.isEmpty()) {
            return CellResult.ok(null);
        }

        return
            Stream.of(CellSupportTypes.values())
                .filter(it -> it.classes.contains(target))
                .findFirst()
                .map(it -> it.doConvert(text, meta))
                .orElseThrow(() ->
                    new SpreadSheetImportException(String.format("not support cell type %s", target.getName()))
                );
    }

    static class CellResult {
        /**
         * 是否解析成功
         */
        boolean isOk;
        /**
         * 解析实例
         */
        Object value;
        /**
         * 解析失败信息
         */
        String message;

        CellResult(Object value) {
            this.value = value;
            this.isOk = true;
        }

        CellResult(String message) {
            this.message = message;
            this.isOk = false;
        }

        static CellResult ok(Object value) {
            return new CellResult(value);
        }

        static CellResult fail(String message) {
            return new CellResult(message);
        }
    }
}
