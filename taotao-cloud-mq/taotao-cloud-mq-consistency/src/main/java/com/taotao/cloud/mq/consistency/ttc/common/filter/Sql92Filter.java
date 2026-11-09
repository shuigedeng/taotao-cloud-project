package com.taotao.cloud.mq.consistency.ttc.common.filter;

import com.taotao.cloud.mq.consistency.ttc.common.filter.MessageFilter;
import com.taotao.cloud.mq.consistency.ttc.common.message.MessageExt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL92 属性过滤器（对标 RocketMQ SqlFilter）.
 * <p>
 * 支持的 SQL92 表达式语法（子集）：
 * <ul>
 *   <li>{@code IS NULL / IS NOT NULL} — 属性存在性判断</li>
 *   <li>{@code = / <>} — 等于/不等于（字符串和数值）</li>
 *   <li>{@code > / < / >= / <=} — 数值比较</li>
 *   <li>{@code BETWEEN x AND y} — 区间判断（等价于 >= x AND <= y）</li>
 *   <li>{@code NOT BETWEEN x AND y} — 区间外判断</li>
 *   <li>{@code IN ('a', 'b')} — 集合包含（字符串）</li>
 *   <li>{@code AND / OR} — 逻辑组合</li>
 *   <li>{@code NOT} — 逻辑取反</li>
 *   <li>{@code ( )} — 括号分组</li>
 * </ul>
 * <p>
 * 属性来源：消息的 properties Map + 系统属性（TAGS, KEYS 等）。
 * <p>
 * 异常处理：表达式计算异常时，默认过滤该消息（返回 false），与 RocketMQ 行为一致。
 */
public class Sql92Filter implements MessageFilter {

    private final String expression;

    public Sql92Filter(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("SQL92 expression cannot be null or empty");
        }
        this.expression = expression.trim();
    }

    @Override
    public boolean match(MessageExt msg) {
        try {
            Map<String, String> properties = msg.getProperties();
            SqlContext ctx = new SqlContext(properties, msg);
            SqlParser parser = new SqlParser(expression, ctx);
            return parser.parse();
        } catch (Exception e) {
            // SQL92 过滤异常时，默认过滤该消息（与 RocketMQ 行为一致）
            return false;
        }
    }

    @Override
    public FilterType getFilterType() {
        return FilterType.SQL92;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    // ==================== 内部类：SQL 上下文 ====================

    /**
     * SQL 求值上下文，包含消息属性。
     */
    static class SqlContext {
        private final Map<String, String> properties;
        private final MessageExt message;

        SqlContext(Map<String, String> properties, MessageExt message) {
            this.properties = properties != null ? properties : java.util.Collections.emptyMap();
            this.message = message;
        }

        /**
         * 获取属性值（支持系统属性 TAGS, KEYS 等）。
         */
        String getPropertyValue(String name) {
            if (name == null) {
                return null;
            }
            String upperName = name.toUpperCase();
            // 系统属性
            if ("TAGS".equals(upperName)) {
                return message.getTags();
            } else if ("KEYS".equals(upperName)) {
                return message.getKeys();
            } else if ("TOPIC".equals(upperName)) {
                return message.getTopic();
            }
            // 用户自定义属性（不区分大小写）
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(name)) {
                    return entry.getValue();
                }
            }
            return null;
        }

        /**
         * 判断属性是否存在。
         */
        boolean hasProperty(String name) {
            return getPropertyValue(name) != null;
        }
    }

    // ==================== 内部类：SQL 词法分析器 ====================

    /**
     * SQL 词法单元类型。
     */
    enum TokenType {
        // 字面量
        STRING, NUMBER, IDENTIFIER,
        // 运算符
        EQ, NE, GT, GE, LT, LE,
        // 关键字
        IS, NULL, NOT, AND, OR, BETWEEN, IN,
        // 特殊
        LPAREN, RPAREN, COMMA,
        // 结束
        EOF
    }

    /**
     * SQL 词法单元。
     */
    static class Token {
        final TokenType type;
        final String value;

        Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return type + "(" + value + ")";
        }
    }

    // ==================== 内部类：SQL 词法分析器 ====================

    /**
     * 递归下降 SQL92 解析器。
     * <p>
     * 语法（BNF）：
     * <pre>
     *   expression ::= orExpr
     *   orExpr     ::= andExpr (OR andExpr)*
     *   andExpr    ::= unaryExpr (AND unaryExpr)*
     *   unaryExpr  ::= NOT unaryExpr | comparison
     *   comparison ::= value (IS [NOT] NULL | BETWEEN value AND value | NOT BETWEEN value AND value | IN '(' valueList ')' | (= | <> | > | < | >= | <=) value )
     *   value      ::= IDENTIFIER | STRING | NUMBER | '(' expression ')'
     *   valueList  ::= value (',' value)*
     * </pre>
     */
    static class SqlParser {
        private final String input;
        private final SqlContext context;
        private int pos;
        private Token currentToken;

        SqlParser(String input, SqlContext context) {
            this.input = input;
            this.context = context;
            this.pos = 0;
            advance();
        }

        /**
         * 解析并返回布尔结果。
         */
        boolean parse() {
            boolean result = parseOrExpr();
            if (currentToken.type != TokenType.EOF) {
                throw new SqlParseException("Unexpected token at position " + pos + ": " + currentToken);
            }
            return result;
        }

        // expression ::= orExpr
        // orExpr ::= andExpr (OR andExpr)*
        private boolean parseOrExpr() {
            boolean left = parseAndExpr();
            while (currentToken.type == TokenType.OR) {
                advance();
                boolean right = parseAndExpr();
                left = left || right;
            }
            return left;
        }

        // andExpr ::= unaryExpr (AND unaryExpr)*
        private boolean parseAndExpr() {
            boolean left = parseUnaryExpr();
            while (currentToken.type == TokenType.AND) {
                advance();
                boolean right = parseUnaryExpr();
                left = left && right;
            }
            return left;
        }

        // unaryExpr ::= NOT unaryExpr | comparison
        private boolean parseUnaryExpr() {
            if (currentToken.type == TokenType.NOT) {
                advance();
                return !parseUnaryExpr();
            }
            return parseComparison();
        }

        // comparison ::= value (postfixOps)?
        private boolean parseComparison() {
            // 括号分组
            if (currentToken.type == TokenType.LPAREN) {
                advance();
                boolean result = parseOrExpr();
                expect(TokenType.RPAREN);
                return result;
            }

            // 先解析左侧值
            SqlValue leftValue = parseValue();

            // IS [NOT] NULL
            if (currentToken.type == TokenType.IS) {
                advance();
                boolean not = false;
                if (currentToken.type == TokenType.NOT) {
                    not = true;
                    advance();
                }
                expect(TokenType.NULL);
                boolean isNull = leftValue.isNull();
                return not ? !isNull : isNull;
            }

            // NOT BETWEEN ... AND ...
            if (currentToken.type == TokenType.NOT) {
                Token saved = currentToken;
                advance();
                if (currentToken.type == TokenType.BETWEEN) {
                    advance();
                    SqlValue low = parseValue();
                    expect(TokenType.AND);
                    SqlValue high = parseValue();
                    boolean between = leftValue.compareTo(low) >= 0 && leftValue.compareTo(high) <= 0;
                    return !between;
                } else {
                    // 回退，作为逻辑 NOT
                    throw new SqlParseException("Expected BETWEEN after NOT");
                }
            }

            // BETWEEN ... AND ...
            if (currentToken.type == TokenType.BETWEEN) {
                advance();
                SqlValue low = parseValue();
                expect(TokenType.AND);
                SqlValue high = parseValue();
                return leftValue.compareTo(low) >= 0 && leftValue.compareTo(high) <= 0;
            }

            // IN '(' valueList ')'
            if (currentToken.type == TokenType.IN) {
                advance();
                expect(TokenType.LPAREN);
                List<SqlValue> values = new ArrayList<>();
                values.add(parseValue());
                while (currentToken.type == TokenType.COMMA) {
                    advance();
                    values.add(parseValue());
                }
                expect(TokenType.RPAREN);
                for (SqlValue v : values) {
                    if (leftValue.equalsValue(v)) {
                        return true;
                    }
                }
                return false;
            }

            // 比较运算符
            if (currentToken.type == TokenType.EQ) {
                advance();
                SqlValue rightValue = parseValue();
                return leftValue.equalsValue(rightValue);
            }
            if (currentToken.type == TokenType.NE) {
                advance();
                SqlValue rightValue = parseValue();
                return !leftValue.equalsValue(rightValue);
            }
            if (currentToken.type == TokenType.GT) {
                advance();
                SqlValue rightValue = parseValue();
                return leftValue.compareTo(rightValue) > 0;
            }
            if (currentToken.type == TokenType.GE) {
                advance();
                SqlValue rightValue = parseValue();
                return leftValue.compareTo(rightValue) >= 0;
            }
            if (currentToken.type == TokenType.LT) {
                advance();
                SqlValue rightValue = parseValue();
                return leftValue.compareTo(rightValue) < 0;
            }
            if (currentToken.type == TokenType.LE) {
                advance();
                SqlValue rightValue = parseValue();
                return leftValue.compareTo(rightValue) <= 0;
            }

            // 无运算符，视为布尔值
            return leftValue.toBoolean();
        }

        // value ::= IDENTIFIER | STRING | NUMBER | '(' expression ')'
        private SqlValue parseValue() {
            Token token = currentToken;

            if (token.type == TokenType.STRING) {
                advance();
                return new SqlValue(token.value, SqlValue.Type.STRING);
            }

            if (token.type == TokenType.NUMBER) {
                advance();
                try {
                    if (token.value.contains(".")) {
                        return new SqlValue(Double.parseDouble(token.value), SqlValue.Type.NUMBER);
                    } else {
                        return new SqlValue(Long.parseLong(token.value), SqlValue.Type.NUMBER);
                    }
                } catch (NumberFormatException e) {
                    return new SqlValue(token.value, SqlValue.Type.STRING);
                }
            }

            if (token.type == TokenType.IDENTIFIER) {
                advance();
                // 解析为属性引用
                String propName = token.value;
                String propValue = context.getPropertyValue(propName);
                if (propValue == null) {
                    return SqlValue.NULL;
                }
                // 尝试解析为数字
                try {
                    if (propValue.contains(".")) {
                        return new SqlValue(Double.parseDouble(propValue), SqlValue.Type.NUMBER);
                    } else {
                        return new SqlValue(Long.parseLong(propValue), SqlValue.Type.NUMBER);
                    }
                } catch (NumberFormatException e) {
                    return new SqlValue(propValue, SqlValue.Type.STRING);
                }
            }

            if (token.type == TokenType.LPAREN) {
                advance();
                SqlValue result = parseValue(); // 简化：递归解析
                expect(TokenType.RPAREN);
                return result;
            }

            throw new SqlParseException("Unexpected token in value: " + token);
        }

        private void expect(TokenType type) {
            if (currentToken.type != type) {
                throw new SqlParseException("Expected " + type + " but got " + currentToken
                        + " at position " + pos);
            }
            advance();
        }

        private void advance() {
            skipWhitespace();
            if (pos >= input.length()) {
                currentToken = new Token(TokenType.EOF, "");
                return;
            }

            char c = input.charAt(pos);

            // 字符串字面量
            if (c == '\'') {
                parseString();
                return;
            }

            // 数字
            if (Character.isDigit(c) || (c == '-' && pos + 1 < input.length() && Character.isDigit(input.charAt(pos + 1)))) {
                parseNumber();
                return;
            }

            // 标识符和关键字
            if (Character.isLetter(c) || c == '_') {
                parseIdentifierOrKeyword();
                return;
            }

            // 运算符和特殊字符
            switch (c) {
                case '(':
                    pos++;
                    currentToken = new Token(TokenType.LPAREN, "(");
                    return;
                case ')':
                    pos++;
                    currentToken = new Token(TokenType.RPAREN, ")");
                    return;
                case ',':
                    pos++;
                    currentToken = new Token(TokenType.COMMA, ",");
                    return;
                case '=':
                    pos++;
                    currentToken = new Token(TokenType.EQ, "=");
                    return;
                case '<':
                    pos++;
                    if (pos < input.length() && input.charAt(pos) == '>') {
                        pos++;
                        currentToken = new Token(TokenType.NE, "<>");
                    } else if (pos < input.length() && input.charAt(pos) == '=') {
                        pos++;
                        currentToken = new Token(TokenType.LE, "<=");
                    } else {
                        currentToken = new Token(TokenType.LT, "<");
                    }
                    return;
                case '>':
                    pos++;
                    if (pos < input.length() && input.charAt(pos) == '=') {
                        pos++;
                        currentToken = new Token(TokenType.GE, ">=");
                    } else {
                        currentToken = new Token(TokenType.GT, ">");
                    }
                    return;
                default:
                    throw new SqlParseException("Unexpected character: " + c + " at position " + pos);
            }
        }

        private void parseString() {
            pos++; // skip opening quote
            StringBuilder sb = new StringBuilder();
            while (pos < input.length() && input.charAt(pos) != '\'') {
                if (input.charAt(pos) == '\\' && pos + 1 < input.length()) {
                    pos++;
                    sb.append(input.charAt(pos));
                } else {
                    sb.append(input.charAt(pos));
                }
                pos++;
            }
            if (pos < input.length()) {
                pos++; // skip closing quote
            }
            currentToken = new Token(TokenType.STRING, sb.toString());
        }

        private void parseNumber() {
            int start = pos;
            if (input.charAt(pos) == '-') {
                pos++;
            }
            while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                pos++;
            }
            if (pos < input.length() && input.charAt(pos) == '.') {
                pos++;
                while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                    pos++;
                }
            }
            currentToken = new Token(TokenType.NUMBER, input.substring(start, pos));
        }

        private void parseIdentifierOrKeyword() {
            int start = pos;
            while (pos < input.length() && (Character.isLetterOrDigit(input.charAt(pos)) || input.charAt(pos) == '_')) {
                pos++;
            }
            String word = input.substring(start, pos).toUpperCase();

            // 关键字匹配
            switch (word) {
                case "IS":
                    currentToken = new Token(TokenType.IS, word);
                    return;
                case "NULL":
                    currentToken = new Token(TokenType.NULL, word);
                    return;
                case "NOT":
                    currentToken = new Token(TokenType.NOT, word);
                    return;
                case "AND":
                    currentToken = new Token(TokenType.AND, word);
                    return;
                case "OR":
                    currentToken = new Token(TokenType.OR, word);
                    return;
                case "BETWEEN":
                    currentToken = new Token(TokenType.BETWEEN, word);
                    return;
                case "IN":
                    currentToken = new Token(TokenType.IN, word);
                    return;
                default:
                    currentToken = new Token(TokenType.IDENTIFIER, input.substring(start, pos));
                    return;
            }
        }

        private void skipWhitespace() {
            while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
                pos++;
            }
        }
    }

    // ==================== 内部类：SQL 值 ====================

    /**
     * SQL 表达式中的值，支持字符串和数值类型。
     */
    static class SqlValue {
        enum Type { NULL, STRING, NUMBER }

        private final Object value;
        private final Type type;

        static final SqlValue NULL = new SqlValue((String) null, Type.NULL);

        SqlValue(String value, Type type) {
            this.value = value;
            this.type = type;
        }

        SqlValue(Number value, Type type) {
            this.value = value;
            this.type = type;
        }

        boolean isNull() {
            return type == Type.NULL;
        }

        boolean toBoolean() {
            if (type == Type.NULL) return false;
            if (type == Type.STRING) return !((String) value).isEmpty();
            if (type == Type.NUMBER) return ((Number) value).doubleValue() != 0;
            return false;
        }

        boolean equalsValue(SqlValue other) {
            if (this.type == Type.NULL || other.type == Type.NULL) {
                return this.type == other.type; // NULL == NULL
            }
            if (this.type != other.type) {
                // 尝试数值比较
                try {
                    double d1 = toDouble();
                    double d2 = other.toDouble();
                    return Double.compare(d1, d2) == 0;
                } catch (Exception e) {
                    return false;
                }
            }
            if (this.type == Type.STRING) {
                return this.value.equals(other.value);
            }
            if (this.type == Type.NUMBER) {
                return Double.compare(toDouble(), other.toDouble()) == 0;
            }
            return false;
        }

        int compareTo(SqlValue other) {
            if (this.type == Type.NULL || other.type == Type.NULL) {
                throw new SqlParseException("Cannot compare NULL values");
            }
            try {
                double d1 = toDouble();
                double d2 = other.toDouble();
                return Double.compare(d1, d2);
            } catch (Exception e) {
                // 字符串比较
                if (this.type == Type.STRING && other.type == Type.STRING) {
                    return ((String) this.value).compareTo((String) other.value);
                }
                throw new SqlParseException("Incomparable types: " + this.type + " and " + other.type);
            }
        }

        private double toDouble() {
            if (type == Type.NUMBER) {
                return ((Number) value).doubleValue();
            }
            if (type == Type.STRING) {
                return Double.parseDouble((String) value);
            }
            throw new SqlParseException("Cannot convert to number: " + value);
        }

        @Override
        public String toString() {
            if (type == Type.NULL) return "NULL";
            return value.toString();
        }
    }

    // ==================== 内部类：SQL 异常 ====================

    /**
     * SQL 解析异常。
     */
    static class SqlParseException extends RuntimeException {
        SqlParseException(String message) {
            super(message);
        }
    }
}
