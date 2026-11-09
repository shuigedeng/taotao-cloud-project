package com.taotao.cloud.mq.consistency.ttc.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * README 定量主张自守卫。
 *
 * <p>判据：README 里写死的数字（模块数 / 类数 / 版本号 / 命令字个数 / 注解命中数）必须能从
 * 源码重算出来 —— README 改了就红，源码变了 README 没跟上也得红。</p>
 *
 * <p>口径约定（踩过的坑，勿随意改）：</p>
 * <ul>
 *   <li>类数只数 {@code src/main} 下的 {@code *.java}（测试类不算 README 说的「类」）；</li>
 *   <li>根 pom 的 version / module 数走 XML 解析：正则会抓到 parent 的 {@code <version>}，
 *       也躲不掉被 {@code <!-- -->} 注释掉的 {@code <module>}；CI-friendly 版本（{@code ${revision}}）
 *       读 {@code <revision>} 属性；</li>
 *   <li>README 里的数字靠「定位锚点 + 抠数正则」取，<b>抠不到就红</b>（不允许「没匹配上就跳过」，
 *       那正是空跑）；</li>
 *   <li>定位锚点用「结构树节点名 + 树形前缀」或「唯一匹配行」，<b>不拿数字本身当锚点</b>
 *       （短数字是长数字的子串，按数字定位会匹配到别人的行上）；结构树节点名后带终止条件，
 *       {@code z-mq-broker/} 不会误配 {@code z-mq-broker-admin/}；</li>
 *   <li>数注解/命令字引用前<b>先剥注释</b>：Javadoc 里出现的 {@code @GetMapping}、
 *       {@code DELETE_TOPIC(7)} 之类字样不是真引用（{@code MqProxyController} 的类注释里就有）；</li>
 *   <li>核对结果<b>一次全报</b>（收集齐所有不一致再抛），不是撞上第一条就停 ——
 *       撞停等于把尺子退化成「一次只说一句」，后面的漂移要改一次 README 才看得见。</li>
 * </ul>
 *
 * <p>安全红线：本守卫只断言<b>可重算的量</b>（数量 / 类名 / 版本号 / 常量名），
 * 绝不读取、断言或回显任何口令、真实主机名、真实 IP、Broker 真地址。</p>
 */
class ReadmeClaimsTest {

    /** 仓根：测试跑在模块目录，user.dir 不是仓根 —— 向上找同时有 README.md 与 pom.xml 的目录（最多 5 层）。 */
    private static Path repoRoot() {
        Path dir = Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
        for (int i = 0; i < 5 && dir != null; i++) {
            if (Files.isRegularFile(dir.resolve("README.md")) && Files.isRegularFile(dir.resolve("pom.xml"))) {
                return dir;
            }
            dir = dir.getParent();
        }
        throw new AssertionError("定位仓根失败：从 " + System.getProperty("user.dir")
                + " 向上 5 层内没有同时含 README.md 与 pom.xml 的目录");
    }

    /** README 全文（统一成 \n 换行，便于按行算偏移）。 */
    private static String readmeText() {
        try {
            String raw = new String(Files.readAllBytes(repoRoot().resolve("README.md")), StandardCharsets.UTF_8);
            return raw.replace("\r\n", "\n");
        } catch (IOException e) {
            throw new AssertionError("读取 README.md 失败", e);
        }
    }

    // ------------------------------------------------------------------ 源码重算

    /** 统计某模块 src/main 下的 *.java 文件数（只数 src/main，测试类不是 README 说的「类」）。 */
    private static int countMainJavaFiles(String moduleRelativePath) {
        Path base = repoRoot().resolve(moduleRelativePath);
        if (!Files.isDirectory(base)) {
            throw new AssertionError("源码目录不存在：" + moduleRelativePath);
        }
        try (Stream<Path> stream = Files.walk(base)) {
            return (int) stream.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".java"))
                    .count();
        } catch (IOException e) {
            throw new AssertionError("扫描 " + moduleRelativePath + " 失败", e);
        }
    }

    /** 根 pom 的 project 元素（XML 解析：注释掉的 <module> 天然不计入）。 */
    private static Element rootProject() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setExpandEntityReferences(false);
            Document doc = factory.newDocumentBuilder().parse(repoRoot().resolve("pom.xml").toFile());
            return doc.getDocumentElement();
        } catch (Exception e) {
            throw new AssertionError("解析根 pom.xml 失败", e);
        }
    }

    private static Element directChild(Element parent, String name) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && name.equals(node.getNodeName())) {
                return (Element) node;
            }
        }
        return null;
    }

    /** 根 pom 的版本号；CI-friendly（${revision}）时读 <revision> 属性。 */
    private static String rootPomVersion() {
        Element project = rootProject();
        Element version = directChild(project, "version");
        if (version == null) {
            throw new AssertionError("根 pom.xml 没有 <version>");
        }
        String raw = version.getTextContent().trim();
        if (raw.contains("${revision}")) {
            Element properties = directChild(project, "properties");
            Element revision = properties == null ? null : directChild(properties, "revision");
            if (revision == null) {
                throw new AssertionError("根 pom.xml 用 ${revision} 但没有 <revision> 属性");
            }
            return revision.getTextContent().trim();
        }
        return raw;
    }

    /** 根 pom <modules> 下实际参与构建的模块数（被注释掉的 module 不算）。 */
    private static int rootPomModuleCount() {
        Element modules = directChild(rootProject(), "modules");
        if (modules == null) {
            throw new AssertionError("根 pom.xml 没有 <modules>");
        }
        int count = 0;
        NodeList children = modules.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && "module".equals(node.getNodeName())) {
                count++;
            }
        }
        return count;
    }

    /** RequestCode.java 的仓内相对路径。 */
    private static Path requestCodeFile() {
        return repoRoot().resolve(
                "z-mq-remoting/src/main/java/com/zifang/z/mq/remoting/protocol/RequestCode.java");
    }

    /** RequestCode 里定义的命令字常量名（按源码顺序）。 */
    private static List<String> requestCodeConstants() {
        Path file = requestCodeFile();
        if (!Files.isRegularFile(file)) {
            throw new AssertionError("找不到 RequestCode.java：" + file);
        }
        String src = read(file);
        Matcher m = Pattern.compile("static\\s+final\\s+int\\s+(\\w+)\\s*=").matcher(src);
        List<String> names = new ArrayList<>();
        while (m.find()) {
            names.add(m.group(1));
        }
        if (names.isEmpty()) {
            throw new AssertionError("RequestCode.java 里没抠到命令字常量");
        }
        return names;
    }

    /** 扫描时跳过的目录（构建产物与前端依赖树，不是仓内源码）。 */
    private static final Set<String> SKIP_DIRS = new LinkedHashSet<>(
            Arrays.asList("target", "node_modules", "dist", "admin-dist", ".git", ".idea", "out"));

    /** 仓内全部 .java 文件（跳过构建产物 / node_modules）。 */
    private static List<Path> allJavaFiles() {
        List<Path> files = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(repoRoot())) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".java"))
                    .filter(p -> {
                        for (Path part : p) {
                            if (SKIP_DIRS.contains(part.toString())) {
                                return false;
                            }
                        }
                        return true;
                    })
                    .forEach(files::add);
        } catch (IOException e) {
            throw new AssertionError("扫描仓内 java 文件失败", e);
        }
        return files;
    }

    /** src/main 下的 .java 文件（按模块相对路径定位，不按目录名统计）。 */
    private static List<Path> srcMainJavaFiles() {
        List<Path> files = new ArrayList<>();
        for (Path p : allJavaFiles()) {
            String rel = repoRoot().relativize(p).toString().replace('\\', '/');
            if (rel.contains("/src/main/java/")) {
                files.add(p);
            }
        }
        return files;
    }

    /**
     * 剥掉注释（块注释 + 行注释），保留字符串字面量。
     * 数注解命中、数命令字引用之前必须先剥：Javadoc 里出现的
     * {@code @GetMapping}、{@code DELETE_TOPIC(7)} 之类字样不是真引用。
     */
    private static String stripComments(String src) {
        StringBuilder out = new StringBuilder(src.length());
        boolean inBlock = false;
        boolean inLine = false;
        boolean inString = false;
        boolean inChar = false;
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            char next = i + 1 < src.length() ? src.charAt(i + 1) : '\0';
            if (inBlock) {
                if (c == '*' && next == '/') {
                    inBlock = false;
                    i++;
                }
                continue;
            }
            if (inLine) {
                if (c == '\n') {
                    inLine = false;
                    out.append(c);
                }
                continue;
            }
            if (inString) {
                out.append(c);
                if (c == '\\' && next != '\0') {
                    out.append(next);
                    i++;
                } else if (c == '"') {
                    inString = false;
                }
                continue;
            }
            if (inChar) {
                out.append(c);
                if (c == '\\' && next != '\0') {
                    out.append(next);
                    i++;
                } else if (c == '\'') {
                    inChar = false;
                }
                continue;
            }
            if (c == '/' && next == '/') {
                inLine = true;
                i++;
                continue;
            }
            if (c == '/' && next == '*') {
                inBlock = true;
                i++;
                continue;
            }
            if (c == '"') {
                inString = true;
                out.append(c);
                continue;
            }
            if (c == '\'') {
                inChar = true;
                out.append(c);
                continue;
            }
            out.append(c);
        }
        return out.toString();
    }

    /** 读文件（UTF-8）。 */
    private static String read(Path file) {
        try {
            return new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new AssertionError("读取文件失败：" + file, e);
        }
    }

    /**
     * {@code RequestCode} 里「零引用」的命令字数：除定义文件外，src/main 里没有任何
     * {@code RequestCode.<NAME>} 真实引用（注释里的字样不算）。
     */
    private static int requestCodeZeroReferenceCount() {
        Path definition = requestCodeFile().toAbsolutePath().normalize();
        List<String> sources = new ArrayList<>();
        for (Path p : srcMainJavaFiles()) {
            if (p.toAbsolutePath().normalize().equals(definition)) {
                continue;
            }
            sources.add(stripComments(read(p)));
        }
        int zero = 0;
        for (String name : requestCodeConstants()) {
            Pattern ref = Pattern.compile("\\bRequestCode\\." + Pattern.quote(name) + "\\b");
            boolean used = false;
            for (String src : sources) {
                if (ref.matcher(src).find()) {
                    used = true;
                    break;
                }
            }
            if (!used) {
                zero++;
            }
        }
        return zero;
    }

    /**
     * {@code src/main} 里 {@code @RequestMapping} 的命中数（剥注释后数）。
     *
     * <p>口径只取 {@code src/main}：README 那句断言说的是<b>产品对外的 HTTP 面</b>，
     * 而本守卫自己住在 {@code src/test}，若按「全仓」数，它文件里那几处
     * {@code @RequestMapping} 字面量会被数进去 —— 那是自指，必然漂。</p>
     */
    private static int srcMainRequestMappingHitCount() {
        Pattern ann = Pattern.compile("@RequestMapping\\b");
        int hits = 0;
        for (Path p : srcMainJavaFiles()) {
            Matcher m = ann.matcher(stripComments(read(p)));
            while (m.find()) {
                hits++;
            }
        }
        return hits;
    }

    // ------------------------------------------------------------------ README 主张抽取

    /** 主张所在文本块：全文区间 + 文本。 */
    private static final class Block {
        final int start;
        final int end;
        final String text;

        Block(int start, int end, String text) {
            this.start = start;
            this.end = end;
            this.text = text;
        }
    }

    /** 主张定位：把 README 全文映射到一个唯一的文本块。 */
    private interface Anchor {
        Block locate(String readme, String label);
    }

    /** 一条定量主张：定位锚点 + 抠数正则 + 源码重算口径。 */
    private static final class Claim {
        final String label;
        final Anchor anchor;
        final Pattern number;
        final Supplier<String> expected;

        Claim(String label, Anchor anchor, Pattern number, Supplier<String> expected) {
            this.label = label;
            this.anchor = anchor;
            this.number = number;
            this.expected = expected;
        }
    }

    private static List<String> lines(String readme) {
        return Arrays.asList(readme.split("\n", -1));
    }

    private static int lineStart(String readme, int lineIndex) {
        int idx = 0;
        for (int i = 0; i < lineIndex; i++) {
            int nl = readme.indexOf('\n', idx);
            if (nl < 0) {
                return readme.length();
            }
            idx = nl + 1;
        }
        return idx;
    }

    /** 定位 README 中唯一匹配的行；找不到或不唯一都红。 */
    private static Block uniqueLine(String readme, Pattern anchor, String label) {
        List<String> all = lines(readme);
        List<Integer> hits = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (anchor.matcher(all.get(i)).find()) {
                hits.add(i);
            }
        }
        if (hits.isEmpty()) {
            throw new AssertionError("README 里找不到主张行：" + label + "（定位 " + anchor.pattern() + "）");
        }
        if (hits.size() > 1) {
            throw new AssertionError("README 里主张行定位不唯一（命中 " + hits.size() + " 行）：" + label);
        }
        int i = hits.get(0);
        int start = lineStart(readme, i);
        return new Block(start, start + all.get(i).length(), all.get(i));
    }

    /**
     * 定位 README 结构树里的节点行（树形前缀 + 节点名），并把它后面紧跟的续行（│ 开头）并入同一块。
     * 节点名带终止条件，避免 {@code z-mq-broker/} 误配 {@code z-mq-broker-admin/}。
     */
    private static Block treeNodeBlock(String readme, String nodeName, String label) {
        List<String> all = lines(readme);
        Pattern node = Pattern.compile("^\\s*(?:├──|└──)\\s*" + Pattern.quote(nodeName) + "(?![\\w-])");
        List<Integer> hits = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (node.matcher(all.get(i)).find()) {
                hits.add(i);
            }
        }
        if (hits.isEmpty()) {
            throw new AssertionError("README 结构树里找不到节点：" + label + "（" + nodeName + "）");
        }
        if (hits.size() > 1) {
            throw new AssertionError("README 结构树里节点定位不唯一（命中 " + hits.size() + " 处）：" + label);
        }
        int first = hits.get(0);
        int last = first;
        while (last + 1 < all.size() && all.get(last + 1).matches("^\\s*│.*$")) {
            last++;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = first; i <= last; i++) {
            if (i > first) {
                sb.append('\n');
            }
            sb.append(all.get(i));
        }
        int start = lineStart(readme, first);
        int end = lineStart(readme, last) + all.get(last).length();
        return new Block(start, end, sb.toString());
    }

    /** 从主张块里抠数字；抠不到就红（不跳过后续主张）。 */
    private static int[] locateNumber(Block block, Pattern number, String label) {
        Matcher m = number.matcher(block.text);
        if (!m.find()) {
            throw new AssertionError("README 主张里抠不到数字：" + label + "（正则 " + number.pattern() + "）");
        }
        return new int[]{block.start + m.start(1), block.start + m.end(1)};
    }

    // ------------------------------------------------------------------ 主张表

    private static List<Claim> claims() {
        List<Claim> list = new ArrayList<>();

        // 1. 基本信息表：Maven reactor 实际参与构建的模块数
        list.add(new Claim("reactor 模块数（基本信息表）",
                (readme, label) -> uniqueLine(readme, Pattern.compile("^\\|\\s*\\*\\*模块数\\*\\*\\s*\\|"), label),
                Pattern.compile("reactor \\*\\*(\\d+) 个\\*\\*"),
                () -> String.valueOf(rootPomModuleCount())));

        // 2. 项目结构树：pom.xml 节点里的「N 个 module」—— 主张 1 的**第二处表述**。
        //    少了这一条，结构树那行改了、基本信息表不改，就成了没人管的一句旧话。
        list.add(new Claim("reactor 模块数（项目结构树 pom.xml 节点）",
                (readme, label) -> treeNodeBlock(readme, "pom.xml", label),
                Pattern.compile("(\\d+) 个 module"),
                () -> String.valueOf(rootPomModuleCount())));

        // 3. 基本信息表：根 POM 版本号（CI-friendly ${revision}）
        list.add(new Claim("根 POM 版本号（基本信息表）",
                (readme, label) -> uniqueLine(readme, Pattern.compile("^\\|\\s*\\*\\*当前版本\\*\\*\\s*\\|"), label),
                Pattern.compile("`(\\d+\\.\\d+\\.\\d+)`"),
                ReadmeClaimsTest::rootPomVersion));

        // 4. 项目结构树：z-mq-common 的 main 类数
        list.add(new Claim("z-mq-common main 类数",
                (readme, label) -> treeNodeBlock(readme, "z-mq-common/", label),
                Pattern.compile("（(\\d+) 个 main 类）"),
                () -> String.valueOf(countMainJavaFiles("z-mq-common/src/main/java"))));

        // 5. 项目结构树：z-mq-spring-boot-starter 的 main 类数
        list.add(new Claim("z-mq-spring-boot-starter 类数",
                (readme, label) -> treeNodeBlock(readme, "z-mq-spring-boot-starter/", label),
                Pattern.compile("（(\\d+)）"),
                () -> String.valueOf(countMainJavaFiles("z-mq-spring-boot-starter/src/main/java"))));

        // 6. 协议命令字段落：RequestCode 的命令字常量总数
        list.add(new Claim("RequestCode 命令字常量总数",
                (readme, label) -> uniqueLine(readme, Pattern.compile("的 \\d+ 个常量"), label),
                Pattern.compile("的 (\\d+) 个常量"),
                () -> String.valueOf(requestCodeConstants().size())));

        // 7. 协议命令字段落：零引用命令字数（其余被接线的个数 = 常量总数 - 本数）
        list.add(new Claim("零引用命令字数",
                (readme, label) -> uniqueLine(readme, Pattern.compile("其余"), label),
                Pattern.compile("\\*\\*(\\d+) 个零引用\\*\\*"),
                () -> String.valueOf(requestCodeZeroReferenceCount())));

        // 8. 协议命令字段落：src/main 里 @RequestMapping 命中数（剥注释后数）
        list.add(new Claim("src/main @RequestMapping 命中数",
                (readme, label) -> uniqueLine(readme, Pattern.compile("里 `@RequestMapping`"), label),
                Pattern.compile("\\*\\*(\\d+) 命中\\*\\*"),
                () -> String.valueOf(srcMainRequestMappingHitCount())));

        return list;
    }

    /** 逐条对账：README 的数字 == 源码重算值。收集齐所有不一致再抛（一次全报）。 */
    private static void checkClaims(String readme, List<Claim> claims) {
        List<String> bad = new ArrayList<>();
        for (Claim claim : claims) {
            Block block = claim.anchor.locate(readme, claim.label);
            int[] span = locateNumber(block, claim.number, claim.label);
            String actual = readme.substring(span[0], span[1]);
            String expected = claim.expected.get();
            if (!expected.equals(actual)) {
                bad.add("[" + claim.label + "] README 写 " + actual + "，源码重算 " + expected);
            }
        }
        if (!bad.isEmpty()) {
            throw new AssertionError("README 的定量主张与盘面对不上（" + bad.size() + " 条）：\n  - "
                    + String.join("\n  - ", bad));
        }
    }

    /** 数字 +1（末段自增），用于合成「把某个数改错」的 README。 */
    private static String shiftNumber(String raw) {
        String[] parts = raw.split("\\.");
        int last = parts.length - 1;
        parts[last] = String.valueOf(Integer.parseInt(parts[last]) + 1);
        return String.join(".", parts);
    }

    // ------------------------------------------------------------------ 用例

    /**
     * 用例 A：真实 README 的所有主张与源码重算值逐条对账 —— 必须全绿。
     */
    @Test
    @DisplayName("README 的定量主张与源码重算值逐条对账")
    void readmeQuantitativeClaimsMustMatchSource() {
        checkClaims(readmeText(), claims());
    }

    /**
     * 用例 B（牙口）：每条主张都合成一个改错数字的 README，必须抛 AssertionError，
     * 且报错要指向真正对不上的那条主张。
     */
    @Test
    @DisplayName("尺子的牙口：把 README 的数改错，判定必须判红且指向那条主张")
    void mutatedReadmeMustBeRejected() {
        String readme = readmeText();
        List<Claim> claims = claims();
        // 基线必须先健康，否则「报错指向哪条主张」的判断没意义
        assertDoesNotThrow(() -> checkClaims(readme, claims), "基线 README 自身已漂移，先把 README 修回与源码一致");
        for (Claim claim : claims) {
            Block block = claim.anchor.locate(readme, claim.label);
            int[] span = locateNumber(block, claim.number, claim.label);
            String original = readme.substring(span[0], span[1]);
            String mutated = shiftNumber(original);
            assertNotEquals(original, mutated, "变异没有生效：" + claim.label);
            String broken = readme.substring(0, span[0]) + mutated + readme.substring(span[1]);
            AssertionError error = assertThrows(AssertionError.class, () -> checkClaims(broken, claims),
                    "主张被放过（README 写错但没红）：" + claim.label);
            assertTrue(error.getMessage().contains(claim.label),
                    "报错没指向真正对不上的主张：" + claim.label + " / " + error.getMessage());
        }
    }

    /**
     * 用例 C（不许冤枉健康页）：真实 README 不得被判红，
     * 且每条主张都必须真的定位到并抠出数字（防「空跑」）。
     */
    @Test
    @DisplayName("不许冤枉健康页：真实 README 不得被判红，且每条主张都抠得到数")
    void realReadmeMustNotBeFlagged() {
        String readme = readmeText();
        List<Claim> claims = claims();
        for (Claim claim : claims) {
            Block block = claim.anchor.locate(readme, claim.label);
            int[] span = locateNumber(block, claim.number, claim.label);
            assertTrue(span[1] > span[0], "主张没抠出数字（守卫空跑）：" + claim.label);
        }
        assertDoesNotThrow(() -> checkClaims(readme, claims), "真实 README 被误判为红");
    }
}
