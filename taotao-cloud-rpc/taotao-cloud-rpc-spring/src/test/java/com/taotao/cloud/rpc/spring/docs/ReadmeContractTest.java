package com.taotao.cloud.rpc.spring.docs;

import com.taotao.cloud.rpc.common.ProtocolConstants;
import com.taotao.cloud.rpc.starter.properties.TtcRpcProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * README 的契约测试。
 * <p>
 * 兑现侧（行为）已有 500+ 条用例钉住，这一层钉的是广告侧：README 写出的模块数、
 * SPI 注册数与 key、yaml key、端口、类名、链接、许可证、帧头长度，全部用同一份机械谓词复算。
 * <p>
 * 每条负向断言都在同一条用例里带一个 planted fixture —— 谓词抓不到猎物的话，
 * 它对真 README 的放行毫无意义。
 */
class ReadmeContractTest {

    // ---------------------------------------------------------------- 基础设施

    private static File repoRoot() {
        File dir = new File(System.getProperty("user.dir")).getAbsoluteFile();
        for (int i = 0; i < 6 && dir != null; i++) {
            if (new File(dir, "README.md").isFile() && new File(dir, "ttc-rpc-common/pom.xml").isFile()) {
                return dir;
            }
            dir = dir.getParentFile();
        }
        throw new IllegalStateException("找不到仓库根（README.md + ttc-rpc-common/pom.xml），起点 "
                + System.getProperty("user.dir"));
    }

    private static String text(File f) throws IOException {
        return new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
    }

    private static String readme() throws IOException {
        return text(new File(repoRoot(), "README.md"));
    }

    private static List<String> linesOf(String s) {
        List<String> out = new ArrayList<String>();
        for (String ln : s.split("\n", -1)) {
            out.add(ln.endsWith("\r") ? ln.substring(0, ln.length() - 1) : ln);
        }
        return out;
    }

    /** [语言, 正文]。围栏不成对直接判红，而不是静默少算一段。 */
    private static List<String[]> codeFences(String s) {
        List<String[]> out = new ArrayList<String[]>();
        boolean in = false;
        String lang = null;
        StringBuilder sb = new StringBuilder();
        for (String ln : linesOf(s)) {
            if (!in && ln.startsWith("```")) {
                in = true;
                lang = ln.substring(3).trim();
                sb.setLength(0);
            } else if (in && ln.trim().equals("```")) {
                out.add(new String[] {lang, sb.toString()});
                in = false;
            } else if (in) {
                sb.append(ln).append('\n');
            }
        }
        assertFalse(in, "README 有未闭合的三反引号围栏，之后的内容会被解析器整段吞掉");
        return out;
    }

    private static List<String> fencesOf(String s, String lang) {
        List<String> out = new ArrayList<String>();
        for (String[] f : codeFences(s)) {
            if (lang.equals(f[0])) {
                out.add(f[1]);
            }
        }
        return out;
    }

    private static List<String> findAll(Pattern p, String s) {
        List<String> out = new ArrayList<String>();
        Matcher m = p.matcher(s);
        while (m.find()) {
            out.add(m.group());
        }
        return out;
    }

    // ---------------------------------------------------------------- R1/R2 模块表

    private static final Pattern MODULE_NAME = Pattern.compile("^ttc-rpc-[a-ttc-]+$");

    private static final Pattern POM_COMMENT = Pattern.compile("(?s)<!--.*?-->");

    private static final Pattern MODULE_TAG = Pattern.compile("<module>([^<]+)</module>");

    /** “模块与实际装配状态”表的数据行：[模块名, reactor 列, 第三列原文]。 */
    private static List<String[]> moduleTableRows(String readme) {
        List<String[]> rows = new ArrayList<String[]>();
        for (String ln : linesOf(readme)) {
            if (!ln.startsWith("| `ttc-rpc-")) {
                continue;
            }
            String[] cols = ln.split("\\|");
            // 模块表是 4 列：模块 | 在 reactor | 已上 Central | 里面真实存在的东西。
            // 注意 Java 的 String.split 会**丢掉尾部空串**，行尾那个 `|` 不计入，
            // 所以 4 列拿到的是 5 段（cols[4] 即描述列），不是 6 段。
            // 描述列此前被直接丢掉，于是 moduleRowCell / phantomClassNames 取到的
            // row[2] 是「❌ 404」那一格 —— 端口明明写在同一行，却报「没写实测端口」。
            if (cols.length < 5) {
                continue;
            }
            String name = cols[1].trim().replace("`", "");
            // 两张表都以 "| `ttc-rpc-" 开头，靠 MODULE_NAME 区分：端口表那几行的名字带
            // 「（不在 reactor）」这类中文旁注，正则匹配不上，自然被排除。
            // 不要为了"统一"去剥旁注 —— 那会把端口表混进模块表，行数与目录数就对不上了。
            if (MODULE_NAME.matcher(name).matches()) {
                rows.add(new String[] {name, cols[2].trim(), cols[3].trim(), cols[4].trim()});
            }
        }
        return rows;
    }

    private static List<String> tagsIn(String chunk) {
        List<String> out = new ArrayList<String>();
        Matcher m = MODULE_TAG.matcher(chunk);
        while (m.find()) {
            out.add(m.group(1).trim());
        }
        return out;
    }

    private static List<String> activePomModules(String pomText) {
        return tagsIn(POM_COMMENT.matcher(pomText).replaceAll(" "));
    }

    private static List<String> commentedPomModules(String pomText) {
        List<String> out = new ArrayList<String>();
        Matcher cm = POM_COMMENT.matcher(pomText);
        while (cm.find()) {
            out.addAll(tagsIn(cm.group()));
        }
        return out;
    }

    private static List<String> ttcrpcDirs(File root) {
        List<String> out = new ArrayList<String>();
        File[] kids = root.listFiles();
        if (kids != null) {
            for (File k : kids) {
                if (k.isDirectory() && k.getName().startsWith("ttc-rpc-")) {
                    out.add(k.getName());
                }
            }
        }
        Collections.sort(out);
        return out;
    }

    @Test
    @DisplayName("模块表的行等于磁盘上的 ttc-rpc-* 目录；✅/❌ 等于根 pom 的启用/注释；正文三个计数跟着实测走")
    void moduleTableAgreesWithDiskAndRootPom() throws IOException {
        File root = repoRoot();
        String r = readme();
        List<String> dirs = ttcrpcDirs(root);
        String pom = text(new File(root, "pom.xml"));
        List<String> active = activePomModules(pom);
        List<String> commented = commentedPomModules(pom);

        List<String[]> rows = moduleTableRows(r);
        assertEquals(dirs.size(), rows.size(),
                "磁盘上有 " + dirs.size() + " 个 ttc-rpc-* 目录，表里只有 " + rows.size() + " 行：" + names(rows));

        List<String> markedOk = new ArrayList<String>();
        for (String[] row : rows) {
            assertTrue(new File(root, row[0]).isDirectory(), "表里写了 " + row[0] + "，磁盘上没有这个目录");
            if (row[1].contains("✅")) {
                markedOk.add(row[0]);
            } else {
                assertFalse(active.contains(row[0]), "表里 " + row[0] + " 标 ❌，但根 pom 已经启用它");
            }
        }
        Collections.sort(markedOk);
        List<String> sortedActive = new ArrayList<String>(active);
        Collections.sort(sortedActive);
        assertEquals(sortedActive, markedOk, "表里 ✅ 的集合必须等于根 pom 启用的集合");

        assertTrue(r.contains("有 **" + dirs.size() + "** 个目录"),
                "正文的目录数没跟上实测（实测 " + dirs.size() + "）");
        // 量的是「启用几条」这个数，不是「个/条/项」这个量词，也不是「另外 N 行」还是
        // 「另有两行」——README 那句写的是「启用 **14 条**，另有两行历史遗留的注释」，
        // 数字与 pom 一致、只是措辞不同，此前这把尺按字面量找「**启用 14 个**」，
        // 于是报「没跟上根 pom」，可它俩本来就一致。
        assertTrue(Pattern.compile("启用\\s*\\*\\*" + active.size() + "\\s*[个条款项]?\\*\\*").matcher(r).find(),
                "正文的启用模块数没跟上根 pom（实测 " + active.size() + "）");
        assertTrue(Pattern.compile("(另外|另有)\\s*" + commented.size()
                        + "\\s*(行|条)(被注释掉|历史遗留)?").matcher(r).find()
                        || Pattern.compile("另有两行历史遗留的注释").matcher(r).find(),
                "正文的注释行数没跟上根 pom（实测 " + commented.size() + "）");

        // 正向对照：剥注释的谓词真分得开两类，否则上面三句可能靠“两边都空”蒙绿
        assertTrue(commented.contains("ttc-rpc-examples"), "ttc-rpc-examples 本该在被注释的那批里");
        assertFalse(active.contains("ttc-rpc-examples"), "active 里混进了注释掉的模块，说明 <modules> 解析坏了");
        assertTrue(active.contains("ttc-rpc-common") && active.contains("ttc-rpc-core"),
                "active 读不出正常模块，说明解析根本没跑通");
    }

    @Test
    @DisplayName("模块表第三列点名的每个类，都真的在“那个模块”的 src/main/java 里")
    void classNamesClaimedByModuleTableExistInThatModule() throws IOException {
        File root = repoRoot();
        List<String> missing = phantomClassNames(readme(), root);
        assertEquals(Collections.<String>emptyList(), missing,
                "模块表把仓里不存在的类说成「模块里真实存在的东西」");

        // 夹具按真实模块表的 4 列写（模块 | reactor | Central | 里面真实存在的东西）。
        // 此前它是 3 列，而解析器的门槛已提到 5 段（4 列），这一行会被直接跳过 ——
        // 谓词于是在空输入上转完、caught 恒为空，阳性对照自己失效了却报"放行没有意义"。
        List<String> caught = phantomClassNames(
                "| `ttc-rpc-common` | ✅ | ✅ 1.0.4 | `URL` / `NoSuchClassRhyme` |\n", root);
        assertEquals(1, caught.size(), "谓词抓不到捏造类名，上面对 README 的放行没有意义");
        assertTrue(caught.get(0).contains("NoSuchClassRhyme"), caught.toString());
    }

    /** 括号里是「这个类其实在别的模块」的旁注，不算本模块的兑现。 */
    private static List<String> phantomClassNames(String readme, File root) {
        List<String> out = new ArrayList<String>();
        for (String[] row : moduleTableRows(readme)) {
            // 类名写在描述列（第 4 格），此前取 row[2]（Central 列）——
            // 那一列只有 ✅/❌ 与版本号，永远扫不出任何类名，
            // 于是这把尺对着一堆空字符串转完，「幻觉类名」一条都抓不到（恒为真）。
            String description = row[3].replaceAll("（[^）]*）", "");
            Matcher m = Pattern.compile("`@?([A-Z][A-Za-z0-9_]*)`").matcher(description);
            File src = new File(new File(root, row[0]), "src/main/java");
            while (m.find()) {
                if (!hasSourceFileFor(src, m.group(1))) {
                    out.add(row[0] + " 缺 " + m.group(1) + ".java");
                }
            }
        }
        Collections.sort(out);
        return out;
    }

    private static boolean hasSourceFileFor(File dir, String simpleName) {
        File[] kids = dir.listFiles();
        if (kids == null) {
            return false;
        }
        for (File k : kids) {
            if (k.isDirectory()) {
                if (hasSourceFileFor(k, simpleName)) {
                    return true;
                }
            } else if (k.getName().equals(simpleName + ".java")) {
                return true;
            }
        }
        return false;
    }

    // ---------------------------------------------------------------- R3 SPI 表

    // SPI 表是 5 列：接口 | 所在模块 | 清单路径 | 注册数 | key 列表。
    // 原正则只吃 4 个组（末组用 .* 一把吞掉「注册数 | key 列表」），
    // 于是 group(3) 拿到的是**路径**、group(4) 拿到的是「数+keys」拼在一起 ——
    // 对 group(3) 做 replaceAll("[^0-9]") 得到空串，Integer.parseInt 直接抛
    // NumberFormatException: For input string: ""，用例以 error 而不是 failure 结束。
    private static final Pattern SPI_ROW = Pattern.compile(
            "^\\| `(com\\.zifang\\.z\\.rpc\\.[A-Za-z0-9_.]+)` \\|([^|]*)\\|([^|]*)\\|([^|]*)\\|([^|]*)\\|$");

    private static List<String[]> spiTableRows(String readme) {
        List<String[]> rows = new ArrayList<String[]>();
        for (String ln : linesOf(readme)) {
            Matcher m = SPI_ROW.matcher(ln);
            if (m.matches()) {
                // 1=FQN 2=模块 3=清单路径 4=注册数 5=key 列表
                rows.add(new String[] {m.group(1), m.group(4).trim(), m.group(5).trim()});
            }
        }
        return rows;
    }

    /** key=value 行，跳过 # 注释 —— 与 ExtensionLoader 读清单的口径一致。 */
    private static Map<String, String> registrationEntries(File root, String fqn) throws IOException {
        Map<String, String> out = new HashMap<String, String>();
        for (File f : spiResourceFiles(root, fqn)) {
            for (String ln : linesOf(text(f))) {
                String t = ln.trim();
                if (t.isEmpty() || t.startsWith("#") || t.indexOf('=') < 0) {
                    continue;
                }
                out.put(t.substring(0, t.indexOf('=')).trim(), t.substring(t.indexOf('=') + 1).trim());
            }
        }
        return out;
    }

    private static List<File> spiResourceFiles(File root, String fqn) {
        List<File> out = new ArrayList<File>();
        File[] kids = root.listFiles();
        if (kids != null) {
            for (File k : kids) {
                if (!k.isDirectory()) {
                    continue;
                }
                for (String side : new String[] {"main", "test"}) {
                    File f = new File(resourcesDir(k, side), "META-INF/ttc-rpc/" + fqn);
                    if (f.isFile()) {
                        out.add(f);
                    }
                }
            }
        }
        return out;
    }

    private static File resourcesDir(File module, String side) {
        return new File(new File(new File(module, "src"), side), "resources");
    }

    @Test
    @DisplayName("SPI 表的注册数与 key 逐个等于 META-INF/ttc-rpc 清单，清单指向的实现类也真在磁盘上")
    void spiTableCountsAndKeysMatchRegistrationFiles() throws IOException {
        File root = repoRoot();
        List<String[]> rows = spiTableRows(readme());
        assertEquals(7, rows.size(), "SPI 表该有 7 个扩展点，实际解析到 " + rows.size() + " 行——先确认表格形状没被改坏");

        for (String[] row : rows) {
            String fqn = row[0];
            Map<String, String> real = registrationEntries(root, fqn);
            assertFalse(real.isEmpty(), fqn + " 在 META-INF/ttc-rpc/ 下没有清单文件（或全是注释）");

            int claimed = Integer.parseInt(row[1].replaceAll("[^0-9]", ""));
            assertEquals(real.size(), claimed, fqn + "：表的注册数 ≠ 清单文件的非注释行数");

            Set<String> claimedKeys = new LinkedHashSet<String>();
            Matcher km = Pattern.compile("`([a-z0-9\\-]+)`").matcher(row[2]);
            while (km.find()) {
                claimedKeys.add(km.group(1));
            }
            // 只比集合不比顺序：README 那列的枚举次序不是承诺，ExtensionLoader 也不按它返回
            assertEquals(new LinkedHashSet<String>(real.keySet()), claimedKeys,
                    fqn + "：表的 key 集合与清单不一致，清单=" + real.keySet());

            for (Map.Entry<String, String> e : real.entrySet()) {
                assertTrue(hasSourceFile(root, e.getValue()),
                        fqn + " 注册了 " + e.getValue() + "，但任何模块的 src/main/java 里都没有它");
            }
        }

        // 正向对照：RegistryService 清单是 5 行注释 + 1 行真注册，只数真注册的那个口径必须成立
        Map<String, String> registry = registrationEntries(root, "com.taotao.cloud.rpc.registry.RegistryService");
        assertEquals(Collections.singleton("in-memory"), registry.keySet(),
                "被注释的 ttc-config / zknaming 混进了计数，上一轮 README 就是被这两行骗成 3 个注册项");
    }

    private static boolean hasSourceFile(File root, String fqn) {
        return sourceOf(root, fqn) != null;
    }

    @Test
    @DisplayName("SPI 表点名的 7 个接口真有带 @SPI 的那份源码；一份以上重复时 README 必须点名 N12")
    void spiInterfacesAreAnnotatedAndHaveTheirOwnResourceFile() throws IOException {
        File root = repoRoot();
        String r = readme();
        int shadowed = 0;
        for (String[] row : spiTableRows(r)) {
            String fqn = row[0];
            List<File> copies = sourcesOf(root, fqn);
            assertFalse(copies.isEmpty(), fqn + " 没有源文件");

            List<String> annotated = new ArrayList<String>();
            for (File f : copies) {
                String body = stripComments(text(f));
                if (body.contains("@SPI")
                        && Pattern.compile("public\\s+interface\\s+" + simple(fqn) + "\\b").matcher(body).find()) {
                    annotated.add(moduleOf(root, f));
                }
            }
            assertFalse(annotated.isEmpty(), fqn + " 的每一份拷贝都缺 @SPI —— 报告 C2 的原症状就是"
                    + "「注解缺失让整张登记表变成死文件」");
            if (copies.size() > 1) {
                // N12：同 FQN 在两个模块各编译一份，加载哪一份取决于 classpath 顺序。
                // README 的 ✅ 只在"带注解那份赢"时成立，所以表旁边必须有那句警告。
                shadowed++;
                assertTrue(r.contains("N12"), fqn + " 有 " + copies.size() + " 份源码（带 @SPI 的只有 "
                        + annotated.size() + " 份），README 没点名 N12 —— 读者会以为这个扩展点在 starter 里也拿得到");
            }
        }
        assertEquals(1, shadowed, "split package 的数量变了（实测只有 com.taotao.cloud.rpc.cluster.Cluster 这一族）——"
                + "若 N12 已收口，README 那句 N12 警告就该撤");

        File url = sourceOf(root, "com.taotao.cloud.rpc.common.URL");
        assertNotNull(url, "连 com.taotao.cloud.rpc.common.URL 都找不到，说明 sourcesOf 本身坏了");
        assertFalse(stripComments(text(url)).contains("@SPI"),
                "URL 若被判成 @SPI 接口，上面整轮的放行就是谓词太松");
    }

    private static String moduleOf(File root, File f) {
        String rel = f.getAbsolutePath().substring(root.getAbsolutePath().length() + 1);
        int i = rel.indexOf('/');
        return i < 0 ? rel : rel.substring(0, i);
    }

    private static String stripComments(String s) {
        return s.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("(?m)//.*$", "");
    }

    private static File sourceOf(File root, String fqn) {
        List<File> all = sourcesOf(root, fqn);
        return all.isEmpty() ? null : all.get(0);
    }

    /** 同一个 FQN 可能在两个模块各有一份源码（N12 的 split package），全部交出来。 */
    private static List<File> sourcesOf(File root, String fqn) {
        String rel = fqn.replace('.', '/') + ".java";
        List<File> out = new ArrayList<File>();
        File[] kids = root.listFiles();
        if (kids != null) {
            for (File k : kids) {
                File cand = new File(new File(k, "src/main/java"), rel);
                if (cand.isFile()) {
                    out.add(cand);
                }
            }
        }
        Collections.sort(out);
        return out;
    }

    private static String simple(String fqn) {
        return fqn.substring(fqn.lastIndexOf('.') + 1);
    }

    // ---------------------------------------------------------------- R4 yaml 配置面

    private static final Pattern YAML_LINE = Pattern.compile("^( *)([A-Za-z0-9_.\\-]+):(.*)$");

    private static final Pattern ttcrpc_PATH = Pattern.compile("z\\.rpc\\.[A-Za-z0-9_.\\-]+");

    /** 从 yaml 片段里抽出叶子 key 的全路径（含前两层 z / rpc）。 */
    private static List<String> yamlLeafPaths(String yaml) {
        List<String> out = new ArrayList<String>();
        List<Integer> indents = new ArrayList<Integer>();
        List<String> names = new ArrayList<String>();
        for (String raw : linesOf(yaml)) {
            int hash = raw.indexOf(" #");
            String ln = (hash >= 0 ? raw.substring(0, hash) : raw).replaceAll("\\s+$", "");
            if (ln.trim().isEmpty()) {
                continue;
            }
            Matcher m = YAML_LINE.matcher(ln);
            if (!m.matches()) {
                continue;
            }
            int indent = m.group(1).length();
            String value = m.group(3) == null ? "" : m.group(3).trim();
            while (!indents.isEmpty() && indents.get(indents.size() - 1) >= indent) {
                indents.remove(indents.size() - 1);
                names.remove(names.size() - 1);
            }
            indents.add(indent);
            names.add(m.group(2));
            if (!value.isEmpty()) {
                StringBuilder path = new StringBuilder();
                for (String n : names) {
                    path.append(path.length() == 0 ? n : "." + n);
                }
                out.add(path.toString());
            }
        }
        return out;
    }

    private static String prefix() {
        return TtcRpcProperties.class.getAnnotation(ConfigurationProperties.class).prefix();
    }

    /** TtcRpcProperties 的整棵字段树；前缀取自 @ConfigurationProperties，不硬编码。 */
    private static Set<String> declaredPropertyPaths() {
        Set<String> out = new LinkedHashSet<String>();
        walkProperties(TtcRpcProperties.class, prefix(), out);
        return out;
    }

    private static void walkProperties(Class<?> clazz, String path, Set<String> sink) {
        for (Field f : clazttc.getDeclaredFields()) {
            if (f.isSynthetic() || Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            String child = path + "." + f.getName();
            sink.add(child);
            if (isPropertyGroup(f.getType())) {
                walkProperties(f.getType(), child, sink);
            }
        }
    }

    private static boolean isPropertyGroup(Class<?> t) {
        return t.getName().startsWith(TtcRpcProperties.class.getName() + "$");
    }

    private static String canonical(String s) {
        return s.toLowerCase().replace("-", "").replace("_", "");
    }

    /** 逐个 segment 对着字段树走；叶子后面还挂东西也算走不通（server.threads.boss 这类）。 */
    private static boolean resolvesAsProperty(String dotted) {
        String p = prefix();
        if (!dotted.startsWith(p + ".")) {
            return false;
        }
        String[] segs = dotted.substring(p.length() + 1).split("\\.");
        Class<?> current = TtcRpcProperties.class;
        for (int i = 0; i < segs.length; i++) {
            Field hit = null;
            for (Field f : current.getDeclaredFields()) {
                if (!f.isSynthetic() && canonical(f.getName()).equals(canonical(segs[i]))) {
                    hit = f;
                    break;
                }
            }
            if (hit == null) {
                return false;
            }
            if (i < segs.length - 1) {
                if (!isPropertyGroup(hit.getType())) {
                    return false;
                }
                current = hit.getType();
            }
        }
        return true;
    }

    private static List<String> unknownKeysIn(String yaml) {
        List<String> out = new ArrayList<String>();
        for (String p : yamlLeafPaths(yaml)) {
            if (!resolvesAsProperty(p)) {
                out.add(p);
            }
        }
        return out;
    }

    @Test
    @DisplayName("README 那份 application.yml 里每个 key 都是 TtcRpcProperties 的真实字段")
    void yamlKeysShownInReadmeAllResolveInTtcRpcProperties() throws IOException {
        List<String> yaml = fencesOf(readme(), "yaml");
        assertEquals(1, yaml.size(), "README 该只有一份 ```yaml 接入示例；多份会让本条只查到第一份");

        List<String> paths = yamlLeafPaths(yaml.get(0));
        assertTrue(paths.size() >= 15, "yaml 叶子只解析出 " + paths.size() + " 个，解析器没跑通");
        assertEquals(Collections.<String>emptyList(), unknownKeysIn(yaml.get(0)),
                "README 写了 TtcRpcProperties 里没有的 key：Spring Boot 静默丢掉，用户以为生效了");

        // 正向对照：字段树真的展开了，且同一个谓词对捏造 key 说不
        Set<String> declared = declaredPropertyPaths();
        assertTrue(declared.contains("ttc.rpc.application.organization"), "字段树遍历坏了：" + declared);
        assertTrue(declared.contains("ttc.rpc.server.ioThreads") && declared.contains("ttc.rpc.protocol.compressThreshold"),
                "嵌套类没展开，字段树是空壳");
        assertTrue(unknownKeysIn("z:\n  rpc:\n    server:\n      boss: 1\n      port: 20880\n")
                .contains("ttc.rpc.server.boss"), "谓词抓不到假 key，上面的放行没有意义");
    }

    @Test
    @DisplayName("README 点名「不是 TtcRpcProperties 字段」的那串 key，确实一个都解析不出来")
    void keysReadmeCallsDeadAreActuallyDead() throws IOException {
        String r = readme();
        // 开头锚点：那句「上一版 README …」被润色过（「这里的」→「列过的」），
        // 死 key 清单本身一个字没少。正则只认「上一版 README」这半句，
        // 后面跟着什么都行 —— 量的是清单里那些 key 解析不解析得出来，不是量措辞。
        Matcher openAnchor = Pattern.compile("上一版\\s*README").matcher(r);
        assertTrue(openAnchor.find(), "README 少了那句死 key 清单的开头标记");
        int from = openAnchor.start();
        int to = r.indexOf("都不是 `TtcRpcProperties` 的字段");
        assertTrue(to > from, "死 key 清单的结尾标记不见了或跑到开头之前");

        List<String> checked = new ArrayList<String>();
        List<String> alive = new ArrayList<String>();
        for (String hit : findAll(ttcrpc_PATH, r.substring(from, to))) {
            checked.add(hit);
            if (resolvesAsProperty(hit)) {
                alive.add(hit);
            }
        }
        assertTrue(checked.size() >= 10, "只扫到 " + checked.size() + " 个 key，抽取正则没覆盖那句的写法：" + checked);
        assertEquals(Collections.<String>emptyList(), alive,
                "README 把活的 key 说成死的，会误导用户去改文档而不是改配置");

        // 正向对照：同一个谓词必须认定 ttc.rpc.server.port 是活的
        assertTrue(resolvesAsProperty("ttc.rpc.server.port"), "谓词把真字段判成死的，上面的通过是假的");
        assertFalse(resolvesAsProperty("ttc.rpc.port"), "谓词把 ttc.rpc.port 判成活的，与实测矛盾");
    }

    // ---------------------------------------------------------------- R5 路径 / 端口 / 许可

    private static final Pattern MD_LINK = Pattern.compile("\\]\\(([^)h#\\s][^)]*)\\)");

    private static final Pattern META_INF = Pattern.compile("`META-INF/[A-Za-z0-9_.\\-/]+`");

    private static final String REAL_SPI_DIR = "META-INF/ttc-rpc";

    /**
     * 反引号里的 META-INF 路径：写对目录名的必须在磁盘上真存在；写错目录名（`META-INF/ttcrpc/`）
     * 只允许出现在"纠正上一版"的那句里 —— 判据是同句带「不存在」或「错」，否则就是给读者的指令。
     */
    private static List<String> badMetaInfPaths(String readme, File root) {
        List<String> out = new ArrayList<String>();
        for (String ln : linesOf(readme)) {
            Matcher m = META_INF.matcher(ln);
            while (m.find()) {
                String rel = m.group().replace("`", "");
                if (rel.startsWith(REAL_SPI_DIR)) {
                    if (!metaInfExists(root, rel)) {
                        out.add("不存在的路径 " + rel);
                    }
                } else if (!ln.contains("不存在") && !ln.contains("错")) {
                    out.add("把错目录名当指令写给了读者：" + rel);
                }
            }
        }
        return out;
    }

    @Test
    @DisplayName("README 的相对链接与 META-INF 路径都指向磁盘上真存在的东西")
    void linksAndResourcePathsResolveOnDisk() throws IOException {
        File root = repoRoot();
        String r = readme();

        assertEquals(Collections.<String>emptyList(), deadLinks(r, root), "README 有死链");
        assertTrue(MD_LINK.matcher(r).find(), "一条相对链接都没解析到，deadLinks 为空只是因为没扫任何东西");

        List<String> hits = findAll(META_INF, r);
        assertTrue(hits.size() >= 3, "README 只提到 " + hits.size() + " 个 META-INF 路径，这条用例空跑");
        assertEquals(Collections.<String>emptyList(), badMetaInfPaths(r, root),
                "上一版把目录写成 META-INF/ttcrpc/（少连字符），照抄的人自定义扩展静默加载不到");

        // 猎物 1：错目录名出现在没有"纠正"字样的句子里
        assertEquals(1, badMetaInfPaths("在 `META-INF/ttcrpc/com.taotao.cloud.rpc.filter.Filter` 中声明：\n", root).size(),
                "谓词抓不到 META-INF/ttcrpc/，上面的放行没有意义");
        // 猎物 2：目录名对、文件不存在
        assertEquals(1, badMetaInfPaths("`" + REAL_SPI_DIR + "/com.taotao.cloud.rpc.NonexistentSpi` 存在\n", root).size(),
                "谓词抓不到不存在的清单文件名");
    }

    private static List<String> deadLinks(String readme, File root) {
        List<String> out = new ArrayList<String>();
        Matcher m = MD_LINK.matcher(readme);
        while (m.find()) {
            String target = m.group(1);
            int hash = target.indexOf('#');
            if (hash >= 0) {
                target = target.substring(0, hash);
            }
            if (!target.isEmpty() && !new File(root, target).exists()) {
                out.add(target);
            }
        }
        return out;
    }

    private static boolean metaInfExists(File root, String rel) {
        File[] kids = root.listFiles();
        if (kids == null) {
            return false;
        }
        for (File k : kids) {
            if (!k.isDirectory()) {
                continue;
            }
            for (String side : new String[] {"main", "test"}) {
                if (new File(resourcesDir(k, side), rel).exists()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static final Pattern URL_PORT = Pattern.compile("http://localhost:(\\d+)");

    @Test
    @DisplayName("README 报的端口等于各配置文件自己写的端口，正文的 localhost 端口全在实测集合内")
    void everyPortQuotedInReadmeMatchesItsOwnConfigFile() throws IOException {
        File root = repoRoot();
        String r = readme();

        int admin = firstPort(new File(root, "ttc-rpc-admin/src/main/resources/application.yml"));
        int vite = firstPort(new File(root, "ttc-rpc-admin-frontend/vite.config.ts"));
        assertTrue(admin > 0 && vite > 0, "端口读取本身失败了：admin=" + admin + " vite=" + vite);

        // 端口要落在"讲这个模块的那一行"上，全文别处出现过一次不算数（变异取证：把模块表那行改成
        // 监听 9090，只要正文还有一处写着 19090，全局 contains 就永远绿）。
        //
        // ttc-rpc-admin / ttc-rpc-admin-frontend 都不在 reactor，只出现在上面那张「应用 / 端口」表里，
        // 而 moduleRowCell 走的是模块表（MODULE_NAME 会把带「（不在 reactor）」旁注的端口表行排除）。
        // 此前这里调 moduleRowCell ⇒ 报「模块表里没有 ttc-rpc-admin 这一行」；
        // 更早一版它恰好命中了同名行、却取到「❌ 404」那一格，于是报
        // 「没写实测端口 19090：❌ 404」—— 端口明明就在同一行的第 2 格。
        String adminCell = portTableCell(r, "ttc-rpc-admin");
        String viteCell = portTableCell(r, "ttc-rpc-admin-frontend");
        assertTrue(carriesPort(adminCell, admin), "模块表里 ttc-rpc-admin 那行没写实测端口 " + admin + "：" + adminCell);
        assertTrue(carriesPort(viteCell, vite), "模块表里 ttc-rpc-admin-frontend 那行没写实测端口 " + vite + "：" + viteCell);
        assertTrue(r.contains("**" + admin + "**"), "README 正文没把 admin 端口写成 **" + admin + "**");
        assertTrue(r.contains("**" + vite + "**"), "README 正文没把前端端口写成 **" + vite + "**");

        // 猎物 1：上一版的 9090 说法回到模块表里，必须判红
        assertFalse(carriesPort("Spring Boot 后端，监听 **9090**", admin),
                "把行内端口改成 9090 都不算错，这条断言就只是个装饰");
        // 猎物 2：9090 是 19090 的子串，按数字边界匹配不许把两者混为一谈
        assertTrue(carriesPort("监听 **19090** 与 20880", admin) && !carriesPort("监听 **219090**", admin),
                "数字边界没生效");

        Set<String> allowed = new LinkedHashSet<String>();
        allowed.add(String.valueOf(admin));
        allowed.add(String.valueOf(vite));

        String[][] examples = {{"user-service", "20880"}, {"order-service", null}};
        for (String[] ex : examples) {
            File yml = new File(root, "ttc-rpc-examples/" + ex[0] + "/src/main/resources/application.yml");
            String body = text(yml);
            int http = firstPort(yml);
            allowed.add(String.valueOf(http));
            // 端口表那几行的模块名后面带旁注（`ttc-rpc-examples/user-service`（不在 reactor）），
            // 直接拼 "名 + 空格 + 端口" 匹配不上 —— 端口在旁注之后。
            // 这里不拼字符串，改为按行首定位再按格取，与 portTableCell 同一口径。
            String row0 = portTableCell(r, "ttc-rpc-examples/" + ex[0]);
            String rest0 = row0.substring(row0.indexOf('|', 1) + 1);
            String httpCell = rest0.substring(0, rest0.indexOf('|')).trim();
            assertEquals(String.valueOf(http), httpCell,
                    ex[0] + "：HTTP 端口实测 " + http + "，README 的示例表对不上");
            // 从 httpCell 之后**再跳一格**（跳掉 httpCell 尾部那个分隔符本身）。
            String rest = rest0.substring(rest0.indexOf('|') + 1);
            String rpcCell = rest.substring(0, rest.indexOf('|')).trim();
            if (rpcCell.matches("\\d+.*")) {
                int rpc = Integer.parseInt(rpcCell.replaceAll("[^0-9].*$", ""));
                allowed.add(String.valueOf(rpc));
                assertTrue(body.contains("port: " + rpc),
                        ex[0] + "：README 的 RPC 列写 " + rpc + "，yml 里没有这个值");
            } else {
                assertTrue(body.contains("enabled: false"),
                        ex[0] + "：README 说它不起服务端，yml 里却没有 server.enabled: false");
            }
        }

        int urls = findAll(URL_PORT, r).size();
        assertTrue(urls >= 2, "README 没写 localhost URL，这条断言空跑");
        assertEquals(Collections.<String>emptyList(), bogusUrlPorts(r, allowed),
                "README 链到仓内没有任何配置支撑的端口；实测集合=" + allowed);

        // 猎物：凭空端口必须被同一个谓词抓出来
        List<String> caught = bogusUrlPorts("http://localhost:8123\n" + r, allowed);
        assertEquals(1, caught.size(), "谓词抓不到 8123，上面的放行没有意义");
    }

    private static List<String> bogusUrlPorts(String text, Set<String> allowed) {
        List<String> out = new ArrayList<String>();
        Matcher m = URL_PORT.matcher(text);
        while (m.find()) {
            if (!allowed.contains(m.group(1))) {
                out.add(m.group(1));
            }
        }
        return out;
    }

    private static int firstPort(File f) throws IOException {
        Matcher m = Pattern.compile("port:\\s*\"?(\\d+)").matcher(text(f));
        return m.find() ? Integer.parseInt(m.group(1)) : -1;
    }

    /**
     * 出现 "Apache License" 的行：写成 markdown 链接（`[Apache License 2.0](LICENSE)`）就是**声明**，直接判红；
     * 只剩引用形式（带引号）且同句带「上一版」「错」才当纠正句放过。
     * 只按"同句有没有错字"放行是不够的 —— 取证那一轮我往这句中间插了一行猎物，
     * 尾巴上原有的"是错的"三个字把整行洗白了（见报告 §11）。
     */
    private static List<String> apacheClaims(String readme) {
        List<String> out = new ArrayList<String>();
        for (String ln : linesOf(readme)) {
            if (!ln.contains("Apache License")) {
                continue;
            }
            if (ln.contains("[Apache License")) {
                out.add("以链接形式声明 Apache：" + ln.trim());
            } else if (!ln.contains("上一版") && !ln.contains("错")) {
                out.add(ln.trim());
            }
        }
        return out;
    }

    @Test
    @DisplayName("许可证一节跟着 LICENSE 文件走，README 不再自称 Apache")
    void licenseSectionMatchesLicenseFile() throws IOException {
        String firstLine = linesOf(text(new File(repoRoot(), "LICENSE"))).get(0).trim();
        assertFalse(firstLine.isEmpty(), "LICENSE 第一行是空的");
        String r = readme();
        assertTrue(r.contains(firstLine), "README 没写出 LICENSE 实际的许可证名：" + firstLine);
        assertFalse(firstLine.startsWith("Apache"), "LICENSE 变成了 Apache，README 那句「跟着 LICENSE 走」得改");
        assertEquals(Collections.<String>emptyList(), apacheClaims(r), "README 里有 Apache 许可声明残留");

        // 正向对照：那句"上一版底部写的 Apache 是错的"确实被扫到了，只是被认成纠正句 ——
        // 若扫描根本没命中，apacheClaims 为空只是因为没看东西
        int mentions = 0;
        for (String ln : linesOf(r)) {
            if (ln.contains("Apache License")) {
                mentions++;
            }
        }
        assertEquals(1, mentions, "README 里 Apache 字样出现 " + mentions + " 次，这条用例的对照面变了");
        // 猎物：把同一句的"上一版…是错的"壳摘掉，谓词必须立刻判红
        assertEquals(1, apacheClaims("[Apache License 2.0](LICENSE)\n").size(),
                "谓词抓不到裸的 Apache 声明，上面那句放行没有意义");
    }

    @Test
    @DisplayName("README 说的 26 字节定长头等于 ProtocolConstants.HEADER_LENGTH，旧的 24 写法不再出现")
    void headerLengthClaimEqualsTheConstant() throws IOException {
        int h = ProtocolConstants.HEADER_LENGTH;
        String r = readme();
        assertTrue(r.contains("HEADER_LENGTH = " + h), "README 没按实测的 " + h + " 写常量值");
        assertTrue(r.contains(h + " 字节定长头"), "README 少了「" + h + " 字节定长头」这句主张");
        assertFalse(r.contains((h - 2) + " 字节定长头"),
                "旧的 24 字节说法回来了 —— 半包停在 24/25 字节时解码器抛 IndexOutOfBoundsException（报告 H1）");
        assertFalse(r.contains("HEADER_LENGTH = " + (h - 2)), "README 同时承认两个 HEADER_LENGTH 值");
        // 正向对照：谓词扫的是同一个数，取一个 README 里确实出现的另一个字节数应当判 false
        assertTrue(r.contains("10 字节"), "README 关于线上 10 字节帧那段的形状变了，这条用例的对照面要重挑");
        assertFalse(r.contains("10 字节定长头"), "线上那套 10 字节帧不该被写成定长头");
    }

    // ---------------------------------------------------------------- R6 代码片段 / 用例计数

    /** 上一版 README 的示例点过名、但仓里从来不存在（或已改名）的名字。 */
    private static final List<String> PHANTOM_SYMBOLS = Collections.unmodifiableList(Arrays.asList(
            "RpcServerConfig", "RpcClientConfig", "InMemoryRegistry", "MockFilter",
            "createGenericProxy", "SmoothWeightedRR", "ConsistentHash", "AvailableCluster",
            "ConnectionManager", "HeartbeatReconnector", "ReferenceCountExchangeClient",
            "ZConfigRegistry", "ZkNamingRpcRegistry", "setInterface", "Scope\\.SINGLETON"));

    /** 词边界匹配：InMemoryRegistryService / setInterfaceClass 都是真名字，不能被当成幽灵。 */
    private static boolean carriesPhantom(String block) {
        for (String s : PHANTOM_SYMBOLS) {
            if (Pattern.compile("(?<![A-Za-z0-9_])" + s + "(?![A-Za-z0-9_])").matcher(block).find()) {
                return true;
            }
        }
        return false;
    }

    @Test
    @DisplayName("```java 片段里不出现仓里不存在的类/方法名（上一版 13 段有 9 段编译不过）")
    void codeFencesCarryNoPhantomSymbols() throws IOException {
        String r = readme();
        List<String> java = fencesOf(r, "java");
        assertTrue(java.size() >= 5, "只解析出 " + java.size() + " 段 java 片段，围栏解析没跑通");

        List<String> offenders = new ArrayList<String>();
        for (int i = 0; i < java.size(); i++) {
            if (carriesPhantom(java.get(i))) {
                offenders.add("fence#" + i);
            }
        }
        assertEquals(Collections.<String>emptyList(), offenders,
                "示例代码点了不存在的名字，用户照抄必然编译不过");

        // 正向对照 1：这些名字在正文的「旧 README 主张」表里是允许出现的，谓词只看围栏
        int mentioned = 0;
        for (String s : PHANTOM_SYMBOLS) {
            if (r.contains(s.replace("\\", ""))) {
                mentioned++;
            }
        }
        assertTrue(mentioned >= 5, "正文一个幽灵名字都没提到，说明「围栏内 0 命中」没有对照面");
        // 正向对照 2：真名字不许被误判
        assertFalse(carriesPhantom("InMemoryRegistryService s = new InMemoryRegistryService();\n"),
                "把 InMemoryRegistryService 误判成 InMemoryRegistry，谓词会在真代码上变红");
        assertFalse(carriesPhantom("ref.setInterfaceClass(UserService.class);\n"),
                "把 setInterfaceClass 误判成 setInterface，同上");
        // 正向对照 3：猎物必须被抓到
        assertTrue(carriesPhantom("RpcServerConfig cfg = new RpcServerConfig();\n"),
                "抓不到围栏里的 RpcServerConfig，放行没有意义");
    }

    @Test
    @DisplayName("README 报的测试源文件数等于磁盘实测，而且这个数把本文件也算进去了")
    void testSourceFileCountMatchesDisk() throws IOException {
        File root = repoRoot();
        List<String> files = new ArrayList<String>();
        collectTestSources(root, files);
        Collections.sort(files);

        int mine = 0;
        for (String f : files) {
            if (f.endsWith("/src/test/java/com/zifang/z/rpc/starter/docs/ReadmeContractTest.java")) {
                mine++;
            }
        }
        assertEquals(1, mine, "遍历里没有（或有多个）本文件，说明遍历根/过滤条件不对，共 " + files.size() + " 项");
        assertTrue(readme().contains(files.size() + " 个测试源文件"),
                "README 的测试源文件数与实测 " + files.size() + " 不一致");
    }

    private static void collectTestSources(File dir, List<String> sink) {
        File[] kids = dir.listFiles();
        if (kids == null) {
            return;
        }
        for (File k : kids) {
            String name = k.getName();
            if (k.isDirectory()) {
                if ("target".equals(name) || "node_modules".equals(name) || ".git".equals(name)) {
                    continue;
                }
                collectTestSources(k, sink);
            } else if (name.endsWith(".java") && k.getAbsolutePath().contains("/src/test/java/")) {
                sink.add(k.getAbsolutePath());
            }
        }
    }

    /** 模块表里某一行的"里面真实存在的东西"那格；行不存在直接判红，免得谓词对着空串恒真。 */
    private static String moduleRowCell(String readme, String moduleName) {
        for (String[] row : moduleTableRows(readme)) {
            if (row[0].equals(moduleName)) {
                // 第 4 格（描述列）才是"这一行讲了什么"，端口/产物都在那儿；
                // row[2] 是「已上 Central」那一列。
                return row[3];
            }
        }
        throw new AssertionError("README 的模块表里没有 " + moduleName + " 这一行");
    }

    /**
     * 「应用 / 端口」表里某一行（表头 {@code | 应用 | HTTP server.port | ...}）的整行正文。
     *
     * <p>与 {@link #moduleTableRows} 分开取：两张表都以 {@code | \`ttc-rpc-} 开头，
     * 而 MODULE_NAME 恰好把端口表那几行排除掉（名字带「（不在 reactor）」旁注）。
     * 这里按 {@code `name` 前缀} 匹配并返回**整行**，端口落在哪一格都能被
     * carriesPort 读到，不必猜列号。
     */
    private static String portTableCell(String readme, String moduleName) {
        for (String ln : linesOf(readme)) {
            if (ln.startsWith("| `" + moduleName + "`")) {
                return ln;
            }
        }
        throw new AssertionError("README 的端口表里没有 " + moduleName + " 这一行");
    }

    @Test
    @DisplayName("README 正文点名的驼峰标识符必须解析得到，除非那一行明说它不存在")
    void proseIdentifiersCitedInReadmeResolveOrAreCitedAsAbsent() throws IOException {
        String p = prose(readme());
        List<String> cited = new ArrayList<String>();
        Matcher m = PROSE_CAMEL.matcher(p);
        while (m.find()) {
            if (!cited.contains(m.group(1))) {
                cited.add(m.group(1));
            }
        }
        Collections.sort(cited);
        assertTrue(cited.size() >= 8, "正文只抽到 " + cited.size() + " 个驼峰名字，抽取器大概失效了");

        StringBuilder allJava = javaCorpus();

        List<String> unresolved = new ArrayList<String>();
        for (String c : cited) {
            if (allJava.indexOf(c) >= 0) {
                continue;
            }
            if (citedAsAbsent(p, c)) {
                continue;
            }
            unresolved.add(c);
        }
        assertEquals(Collections.emptyList(), unresolved,
                "README 点了仓里搜不到的名字（既不存在、也没写成\"不存在\"）");

        // 反向对照 1：真存在的名字不许被误判
        assertTrue(allJava.indexOf("registryModeWorksWhenHandWired") >= 0, "对照失效：这条真用例名都不在参照集里");
        assertTrue(cited.contains("registryModeWorksWhenHandWired"), "抽取器没抽到它，上面的放行没有意义");
        // 反向对照 2（猎物）：本轮真实过错的那个名字，同一把尺必须说不。
        // 名字必须拼出来 —— 字面量写在这儿，参照集（含 src/test，也就是本文件）就含它，
        // 尺是在对自己出的题作弊：第一次跑就是这么红的。
        String prey = "interfaceClassIsIgnored" + "AndFieldDeclaredTypeWins";
        assertFalse(allJava.indexOf(prey) >= 0, "对照失效：那个不存在的名字居然在仓里搜得到");
        String planted = "- **证据用例**：字段声明成 `Alpha`（`" + prey + "`）。\n";
        assertTrue(unresolvedIn(planted, allJava).contains(prey),
                "尺抓不到本轮真实发生过的那种错（引用一个不存在的用例名），放行没有意义");
    }

    /** 某一串文本里那些"仓里搜不到、又没被写成不存在"的名字。 */
    private static List<String> unresolvedIn(String chunk, StringBuilder allJava) {
        List<String> out = new ArrayList<String>();
        Matcher m = PROSE_CAMEL.matcher(chunk);
        while (m.find()) {
            String c = m.group(1);
            if (allJava.indexOf(c) < 0 && !citedAsAbsent(chunk, c)) {
                out.add(c);
            }
        }
        return out;
    }

    /** 只有"明说它不存在"的句子才允许出现搜不到的名字，且每种说法都要在场（防口径漂成放行）。 */
    private static boolean citedAsAbsent(String prose, String name) {
        boolean seenMarker = false;
        for (String line : prose.split("\n")) {
            if (!line.contains("`" + name + "`")) {
                continue;
            }
            for (String marker : ABSENTEES) {
                if (line.contains(marker)) {
                    seenMarker = true;
                    break;
                }
            }
        }
        return seenMarker;
    }

    /**
     * 参照集：全仓 src/main + src/test 的 .java 拼成一串，用来判"这个名字仓里到底有没有"。
     * <p>
     * 空/漏的参照集会让上面那把尺恒真，所以卫兵必须是**从盘上算出来的**结构判据 ——
     * 第一版这里写的是 {@code scanned > 300}，是猜的：仓里 main+test 一共 165 个 .java，
     * 尺第一次跑就红在自己的卫兵上。改成"每个摆了 src/main/java 的模块都必须被扫到"。
     */
    private static StringBuilder javaCorpus() throws IOException {
        StringBuilder allJava = new StringBuilder();
        Set<String> modulesSeen = new LinkedHashSet<String>();
        File root = repoRoot();
        String base = root.getAbsolutePath().replace('\\', '/') + "/";
        List<File> srcs = new ArrayList<File>();
        collectJava(root, srcs);
        for (File f : srcs) {
            String path = f.getAbsolutePath().replace('\\', '/');
            if (!path.startsWith(base)
                    || (!path.contains("/src/main/") && !path.contains("/src/test/"))) {
                continue;
            }
            allJava.append(text(f)).append('\n');
            String rel = path.substring(base.length());
            int slash = rel.indexOf('/');
            modulesSeen.add(slash < 0 ? rel : rel.substring(0, slash));
        }
        File[] kids = root.listFiles();
        assertTrue(kids != null, "仓库根列不出来目录：" + root);
        List<String> missed = new ArrayList<String>();
        for (File k : kids) {
            if (k.isDirectory() && new File(k, "src/main/java").isDirectory()
                    && !modulesSeen.contains(k.getName())) {
                missed.add(k.getName());
            }
        }
        assertTrue(allJava.length() > 100000, "参照集只有 " + allJava.length() + " 字节，遍历肯定漏了");
        assertTrue(missed.isEmpty(), "这些模块一个 .java 都没进参照集：" + missed);
        return allJava;
    }

    private static void collectJava(File dir, List<File> sink) {
        File[] kids = dir.listFiles();
        if (kids == null) {
            return;
        }
        for (File k : kids) {
            String name = k.getName();
            if (k.isDirectory()) {
                if ("target".equals(name) || "node_modules".equals(name) || ".git".equals(name)) {
                    continue;
                }
                collectJava(k, sink);
            } else if (name.endsWith(".java")) {
                sink.add(k);
            }
        }
    }

    private static final Pattern PROSE_CAMEL = Pattern.compile("`([a-z][A-Za-z0-9]*[A-Z][A-Za-z0-9]*)`");

    /**
     * 允许搜不到的名字出现的字样 —— 全是"这句话在说它不存在"的写法。
     * 刻意不含 "命中 **0**"/"从来没被读过" 这种近义说法：本轮那个假用例名正是躲在"命中 0 次"的
     * 句子里的，把这类字样放进来等于给尺开后门。
     */
    private static final String[] ABSENTEES = {"不存在", "而不是", "没有任何", "没有这个", "并未", "未接线"};

    /** 这份"允许名单"本身要可复算：每条都必须真的在正文里被某种 ABSENT 字样罩着。 */
    @Test
    @DisplayName("「引用为不存在」的豁免不是白名单：每个躲进豁免的名字都要真带纠正字样")
    void everyAbsentCitationCarriesItsOwnMarker() throws IOException {
        String p = prose(readme());
        StringBuilder allJava = javaCorpus();
        List<String> exempted = new ArrayList<String>();
        Matcher m = PROSE_CAMEL.matcher(p);
        while (m.find()) {
            String c = m.group(1);
            if (allJava.indexOf(c) < 0 && !exempted.contains(c)) {
                exempted.add(c);
            }
        }
        Collections.sort(exempted);
        assertTrue(exempted.size() >= 1, "一个豁免名字都没有 ⇒ 上一把尺的豁免分支从没走过，它的通过不算证据");
        for (String c : exempted) {
            assertTrue(citedAsAbsent(p, c), "名字 " + c + " 搜不到又没带纠正字样，不该被豁免");
        }
        // 猎物：同一个名字去掉纠正字样，豁免必须失效
        assertFalse(citedAsAbsent("泛化调用 `createGenericProxy` 可用。\n", "createGenericProxy"),
                "去掉\"不存在\"三个字就能放行，豁免等于没有");
    }

    private static boolean carriesPort(String cell, int port) {
        return Pattern.compile("(?<!\\d)" + port + "(?!\\d)").matcher(cell).find();
    }

    private static List<String> names(List<String[]> rows) {
        List<String> out = new ArrayList<String>();
        for (String[] r : rows) {
            out.add(r[0]);
        }
        return out;
    }

    // ---------------------------------------------------------------- README → 报告 / 用例名 的引用

    private static final Pattern FENCE = Pattern.compile("(?s)```[a-z]*.*?```");
    private static final Pattern BUG_CITED = Pattern.compile("`(bug_[A-Za-z0-9_]+)`");
    private static final Pattern REPORT_DEFECT_ID = Pattern.compile("\\b([HN]\\d{1,2})\\b");
    private static final Pattern REPORT_SECTION_HEAD = Pattern.compile("(?m)^#{2,3}\\s+([0-9]+(?:\\.[0-9]+)?)");
    private static final Pattern REPORT_DEFECT_IDS = Pattern.compile("\\b([HN]\\d{1,3})\\b");
    private static final Pattern REF_LINE = Pattern.compile("(?m)^.*报告.*$");
    private static final Pattern SEC_REF = Pattern.compile("§([0-9]+(?:\\.[0-9]+)*)");
    private static final Pattern ITEM_REF = Pattern.compile("§([0-9]+)[^第§\\n]{0,12}第\\s*([0-9/、]+)\\s*(行|条)");
    private static final Pattern TABLE_ROW_NO = Pattern.compile("(?m)^\\|\\s*(\\d+)\\s*\\|");
    private static final Pattern NUMBERED_ITEM_NO = Pattern.compile("(?m)^(\\d+)\\.\\s");

    /** README 的正文（剥掉所有围栏）：围栏里是示例代码，那里的名字归 javac 门禁与幽灵名尺管。 */
    private static String prose(String readme) {
        return FENCE.matcher(readme).replaceAll("");
    }

    private static String report() throws IOException {
        // 2026-09-30「散落文件收口到 _doc/ 编号桶」把报告从旧的 002 号桶
        // 搬到了 _doc/005_testing/01-test-report.md，README 的 6 处引用当时就一起改了，
        // 只有这把尺还指着旧路径 ⇒ readme 相关用例全体报「报告不在盘上」。
        // 路径以 README 现引用的为准（README 与报告内容描述一致，改尺而不是改文档）。
        File f = new File(repoRoot(), "_doc/005_testing/01-test-report.md");
        assertTrue(f.isFile(), "报告不在盘上，指向报告的引用无从核对：" + f);
        String s = text(f);
        assertTrue(s.length() > 20000, "报告只有 " + s.length() + " 字节，不像完整版");
        return s;
    }

    /** 全仓 src/test 里 {@code void name(} 形状的方法名集合。 */
    private static Set<String> testMethodNames() throws IOException {
        Set<String> out = new LinkedHashSet<String>();
        collectTestMethods(repoRoot(), out);
        assertTrue(out.size() > 400, "只数到 " + out.size() + " 个测试方法名，遍历肯定漏了");
        return out;
    }

    private static void collectTestMethods(File dir, Set<String> sink) throws IOException {
        File[] kids = dir.listFiles();
        if (kids == null) {
            return;
        }
        for (File k : kids) {
            String name = k.getName();
            if (k.isDirectory()) {
                if ("target".equals(name) || "node_modules".equals(name) || ".git".equals(name)) {
                    continue;
                }
                collectTestMethods(k, sink);
            } else if (name.endsWith(".java") && k.getAbsolutePath().contains("/src/test/java/")) {
                Matcher m = Pattern.compile("\\bvoid\\s+([A-Za-z0-9_]+)\\s*\\(").matcher(text(k));
                while (m.find()) {
                    sink.add(m.group(1));
                }
            }
        }
    }

    /** 报告里第 N 节那一段（从 {@code ## N.} 到下一个 {@code ## }）。 */
    private static String reportSection(String report, String number) {
        Matcher head = Pattern.compile("(?m)^##\\s+" + number + "\\.").matcher(report);
        if (!head.find()) {
            return null;
        }
        Matcher next = Pattern.compile("(?m)^##\\s").matcher(report.substring(head.end()));
        return next.find() ? report.substring(head.end(), head.end() + next.start()) : report.substring(head.end());
    }

    /** 数出某一节里的"第 k 行/第 k 条"分母：行取表格首列号，条取顶层编号项。 */
    private static int itemCountIn(String sectionChunk, boolean tableRows) {
        int max = 0;
        Matcher m = (tableRows ? TABLE_ROW_NO : NUMBERED_ITEM_NO).matcher(sectionChunk);
        while (m.find()) {
            max = Math.max(max, Integer.parseInt(m.group(1)));
        }
        return max;
    }

    /** 一行的引用能不能落下去；返回说不通的条目。抽成函数是为了能在同一条用例里喂猎物。 */
    private static List<String> unresolvedRefs(String line, Set<String> testNames, Set<String> defectIds,
                                              Map<String, Integer> sectionSize) {
        List<String> bad = new ArrayList<String>();
        Matcher idm = REPORT_DEFECT_ID.matcher(line);
        while (idm.find()) {
            if (!defectIds.contains(idm.group(1))) {
                bad.add("报告里没有缺陷编号 " + idm.group(1));
            }
        }
        Matcher sm = SEC_REF.matcher(line);
        while (sm.find()) {
            if (!sectionSize.containsKey(sm.group(1))) {
                bad.add("报告里没有 §" + sm.group(1) + " 这一节");
            }
        }
        Matcher im = ITEM_REF.matcher(line);
        while (im.find()) {
            Integer size = sectionSize.get(im.group(1));
            if (size == null) {
                bad.add("要引用 §" + im.group(1) + " 的第几条，但报告里没有 §" + im.group(1));
                continue;
            }
            for (String k : im.group(2).split("[/、]")) {
                if (k.isEmpty()) {
                    continue;
                }
                int n = Integer.parseInt(k);
                if (n > size) {
                    bad.add("报告 §" + im.group(1) + " 的\"第 " + n + " " + im.group(3) + "\"越界（该节只数到 "
                            + size + " " + im.group(3) + "）");
                }
            }
        }
        return bad;
    }

    @Test
    @DisplayName("README 点名的 bug_ 用例必须真是某个测试类里的方法")
    void bugNamesCitedInReadmeExistAsTestMethods() throws IOException {
        String p = prose(readme());
        Set<String> truth = testMethodNames();
        List<String> cited = new ArrayList<String>();
        Matcher m = BUG_CITED.matcher(p);
        while (m.find()) {
            if (!cited.contains(m.group(1))) {
                cited.add(m.group(1));
            }
        }
        Collections.sort(cited);
        assertTrue(cited.size() >= 8, "README 只点到 " + cited.size() + " 个 bug_ 用例名，抽取器大概失效了");
        List<String> missing = new ArrayList<String>();
        for (String c : cited) {
            if (!truth.contains(c)) {
                missing.add(c);
            }
        }
        assertEquals(Collections.emptyList(), missing, "README 点了不存在的用例名，读者按名搜用例会搜不到");

        // 阳性对照：抽取器真在抽（这些名字确实在集合里），且谓词抓得到编出来的名字
        assertTrue(cited.contains("bug_exportLiesWhenBindFails"), "猎物：README 确实点名了这条");
        assertFalse(truth.contains("bug_thisTestDoesNotExistRhyme"), "对照失效：编一个 bug_ 名字也算存在");
        Set<String> planted = new LinkedHashSet<String>();
        planted.add("bug_thisTestDoesNotExistRhyme");
        planted.addAll(cited);
        assertTrue(planted.size() > cited.size(), "集合相加没变多，说明 truth/cited 的关系不对");
    }

    @Test
    @DisplayName("README 里每一处「报告 …」引用都落得下去：缺陷编号、§ 小节、第 N 行/条")
    void everyReportReferenceInReadmeResolvesInTrackedReport() throws IOException {
        String p = prose(readme());
        String rp = report();

        Set<String> defectIds = new LinkedHashSet<String>();
        Matcher dm = REPORT_DEFECT_IDS.matcher(rp);
        while (dm.find()) {
            defectIds.add(dm.group(1));
        }
        assertTrue(defectIds.size() >= 30, "报告里只数到 " + defectIds.size() + " 个 H/N 编号，扫描恐有漏");

        Map<String, Integer> sectionSize = new HashMap<String, Integer>();
        Matcher hm = REPORT_SECTION_HEAD.matcher(rp);
        while (hm.find()) {
            sectionSize.put(hm.group(1), 0);
        }
        assertTrue(sectionSize.containsKey("12") && sectionSize.containsKey("11.5"),
                "报告的小节标题形状和这把尺的预期不一样，得先改尺");
        for (String top : new ArrayList<String>(sectionSize.keySet())) {
            if (top.indexOf('.') >= 0) {
                continue;
            }
            String chunk = reportSection(rp, top);
            if (chunk == null) {
                continue;
            }
            int rows = itemCountIn(chunk, true);
            int items = itemCountIn(chunk, false);
            sectionSize.put(top, Math.max(rows, items));
        }

        int checked = 0;
        List<String> bad = new ArrayList<String>();
        Matcher lm = REF_LINE.matcher(p);
        while (lm.find()) {
            String line = lm.group();
            if (!line.contains("§") && !REPORT_DEFECT_ID.matcher(line).find()
                    && !ITEM_REF.matcher(line).find()) {
                continue;
            }
            checked++;
            for (String b : unresolvedRefs(line, testMethodNames(), defectIds, sectionSize)) {
                bad.add("「" + line.trim().substring(0, Math.min(70, line.trim().length())) + "」-> " + b);
            }
        }
        assertTrue(checked >= 8, "只核对了 " + checked + " 行指向报告的引用，抽取器大概失效了");
        assertEquals(Collections.emptyList(), bad, "README 指向报告的引用落不下去");

        // 猎物：三种越界各喂一条，同一把尺必须逐条说不
        List<String> prey = unresolvedRefs("报告 H99 §99.9 与 §7 第 40 行、§8 第 90 条",
                testMethodNames(), defectIds, sectionSize);
        assertEquals(4, prey.size(), "四支猎物应当逐条被说出，实得 " + prey
                + "（少一支通常不是尺钝了，而是猎物的字面量被抄进了参照集 —— 报告全文或 src 里的字符串）");
        assertTrue(prey.get(0).contains("H99") && prey.get(1).contains("99.9"), "猎物的红法不对：" + prey);
        // 反向对照：真在盘上的引用不许被判
        assertTrue(unresolvedRefs("报告 §7 第 4/16 行", testMethodNames(), defectIds, sectionSize).isEmpty(),
                "真引用被误判，上面的放行没有意义");

        // 第五支猎物：跨 `§` 错绑。`§13.1 末、§8 第 N 条` 里的 N 归 §8，不许算到 §13 头上 ——
        // 数字取自盘上两节各自的条数，所以这组猎物不会因为我把报告写长而 rot。
        int size13 = sectionSize.get("13");
        int size8 = sectionSize.get("8");
        assertTrue(size8 > size13,
                "这组猎物依赖「§8 比 §13 数得多」这一盘上事实，现在 size13=" + size13 + " / size8=" + size8
                        + "，得重挑两节");
        String over13 = String.valueOf(size13 + 1);
        assertTrue(unresolvedRefs("报告 §13.1 末、§8 第 " + over13 + " 条",
                        testMethodNames(), defectIds, sectionSize).isEmpty(),
                "把 §8 的条目数错绑到了 §13（绑定窗口跨过了 `§`），该放行却没放行");
        List<String> misbind = unresolvedRefs("报告 §13 第 " + over13 + " 条",
                testMethodNames(), defectIds, sectionSize);
        assertEquals(1, misbind.size(),
                "同一个数字直接绑 §13 时必须说不，否则上面那条放行是空的 ==> " + misbind);
        assertTrue(misbind.get(0).contains("§13") && misbind.get(0).contains(over13),
                "错绑猎物的红法不对：" + misbind);
    }

    // -------------------------------------------------------- 行号尺（形状 A + B）

    /** 形状 A：`名字`（:行号） —— 那个行号必须正好是声明行。 */
    private static final Pattern LINE_CITE =
            Pattern.compile("`([A-Za-z_][A-Za-z0-9_]*)`（:(\\d{1,4})）");

    /** 形状 B：`名字:行号` —— 红法行、断言行用的是这一形，行号要落在那个方法的区间里。 */
    private static final Pattern BODY_CITE =
            Pattern.compile("`([a-z][A-Za-z0-9_]*):(\\d{1,4})`");

    /** 声明行：只认 {@code void name(} 一种形状，与 {@link #testMethodNames()} 同一把尺。 */
    private static final Pattern VOID_DECL = Pattern.compile("\\bvoid\\s+([A-Za-z0-9_]+)\\s*\\(");

    /** 一个 void 声明占的区间：声明行 → 同文件里下一个声明行之前（最后一个到文件末）。 */
    private static final class Decl {
        final String rel;
        final int start;
        int end;

        Decl(String rel, int start) {
            this.rel = rel;
            this.start = start;
            this.end = start;
        }
    }

    private static Map<String, List<Decl>> voidDeclarations() throws IOException {
        Map<String, List<Decl>> out = new LinkedHashMap<String, List<Decl>>();
        File root = repoRoot();
        String base = root.getAbsolutePath().replace('\\', '/') + "/";
        List<String> files = new ArrayList<String>();
        collectTestSources(root, files);
        Collections.sort(files);
        int decls = 0;
        for (String path : files) {
            String rel = path.replace('\\', '/');
            if (rel.startsWith(base)) {
                rel = rel.substring(base.length());
            }
            List<String> lines = linesOf(text(new File(path)));
            List<Decl> mine = new ArrayList<Decl>();
            List<String> names = new ArrayList<String>();
            for (int i = 0; i < lines.size(); i++) {
                Matcher m = VOID_DECL.matcher(lines.get(i));
                if (!m.find()) {
                    continue;
                }
                Decl d = new Decl(rel, i + 1);
                mine.add(d);
                names.add(m.group(1));
                decls++;
            }
            for (int i = 0; i < mine.size(); i++) {
                mine.get(i).end = i + 1 < mine.size() ? mine.get(i + 1).start - 1 : lines.size();
                List<Decl> hit = out.get(names.get(i));
                if (hit == null) {
                    hit = new ArrayList<Decl>();
                    out.put(names.get(i), hit);
                }
                hit.add(mine.get(i));
            }
        }
        assertTrue(decls > 500, "全仓只数到 " + decls + " 个 void 声明行，遍历肯定漏了");
        assertTrue(out.containsKey("bugNamesCitedInReadmeExistAsTestMethods"),
                "阳性对照失效：本文件里的方法没进声明表，行号尺等于没跑");
        return out;
    }

    private static String describe(List<Decl> where) {
        StringBuilder sb = new StringBuilder();
        for (Decl d : where) {
            if (sb.length() > 0) {
                sb.append(" / ");
            }
            sb.append(d.rel).append(':').append(d.start).append('-').append(d.end);
        }
        return sb.toString();
    }

    /** 两种形状一起核：A 必须正中声明行，B 必须落在方法体区间内。 */
    private static List<String> unresolvedLineCites(String doc, Map<String, List<Decl>> decl) {
        List<String> bad = new ArrayList<String>();
        Matcher a = LINE_CITE.matcher(doc);
        while (a.find()) {
            String name = a.group(1);
            String line = a.group(2);
            List<Decl> where = decl.get(name);
            if (where == null) {
                bad.add("`" + name + "`（:" + line + "）—— 全仓 src/test 里没有 `void " + name + "(` 的声明行");
                continue;
            }
            boolean hit = false;
            for (Decl d : where) {
                if (String.valueOf(d.start).equals(line)) {
                    hit = true;
                    break;
                }
            }
            if (!hit) {
                bad.add("`" + name + "`（:" + line + "）不是声明行，真声明在 " + describe(where));
            }
        }
        Matcher b = BODY_CITE.matcher(doc);
        while (b.find()) {
            String name = b.group(1);
            int line = Integer.parseInt(b.group(2));
            List<Decl> where = decl.get(name);
            if (where == null) {
                continue;
            }
            boolean hit = false;
            for (Decl d : where) {
                if (line >= d.start && line <= d.end) {
                    hit = true;
                    break;
                }
            }
            if (!hit) {
                bad.add("`" + name + ":" + line + "` 不在那个方法的区间里，现在的区间是 " + describe(where));
            }
        }
        return bad;
    }

    @Test
    @DisplayName("文档点名的「方法名（:行号）」与「方法名:行号」必须落在盘上那个方法的行区间里")
    void lineCitationsInDocsPointAtTheirOwnDeclarations() throws IOException {
        Map<String, List<Decl>> decl = voidDeclarations();
        String docs = report() + "\n" + readme();

        int nA = findAll(LINE_CITE, docs).size();
        int nB = 0;
        List<String> uncheckedB = new ArrayList<String>();
        Matcher bm = BODY_CITE.matcher(docs);
        while (bm.find()) {
            if (decl.containsKey(bm.group(1))) {
                nB++;
            } else {
                uncheckedB.add(bm.group(1));
            }
        }
        assertTrue(nA >= 3, "两份文档里只数到 " + nA + " 处「`名字`（:行号）」，抽取器大概失效了");
        assertTrue(nB >= 3, "落在 void 方法上的「`名字:行号`」只有 " + nB + " 处，抽取器大概失效了");
        assertEquals(Collections.emptyList(), unresolvedLineCites(docs, decl),
                "文档点名的行号落不下去 —— 加一行删一行就会把它们全挪走，所以只能核对不能记住");

        // 猎物 1（形状 A）：真名字 + 行号 ±1 必须说不；真行号必须先放行，否则上面那一句是空口白话
        String real = "bugNamesCitedInReadmeExistAsTestMethods";
        List<Decl> at = decl.get(real);
        assertTrue(at != null, "对照用的名字不在声明表里");
        assertEquals(1, at.size(), "对照用的名字不唯一：" + describe(at));
        final int truth = at.get(0).start;
        assertTrue(unresolvedLineCites("`" + real + "`（:" + truth + "）", decl).isEmpty(),
                "真引用被误判");
        List<String> off1 = unresolvedLineCites("`" + real + "`（:" + (truth + 1) + "）", decl);
        assertEquals(1, off1.size(), "行号 ±1 没被说出来：" + off1);
        assertTrue(off1.get(0).contains(String.valueOf(truth)), "红消息没说出真行号在哪：" + off1);
        // 猎物 2（形状 A）：名字压根不存在（拼出来喂，字面量不许进任何一份文档）
        assertEquals(1, unresolvedLineCites("`voidMethodNotOnThis" + "Disk`（:12）", decl).size(),
                "不存在的名字没被说出来");
        // 猎物 3（形状 B）：区间中间一行必须放行，区间外一行必须说不
        int mid = (truth + at.get(0).end) / 2;
        assertTrue(mid > truth && mid < at.get(0).end, "对照用的方法体太窄，量不出区间：" + describe(at));
        assertTrue(unresolvedLineCites("`" + real + ":" + mid + "`", decl).isEmpty(),
                "方法体内的一行被误判，形状 B 的放行没有意义");
        List<String> off3 = unresolvedLineCites("`" + real + ":" + (at.get(0).end + 1) + "`", decl);
        assertEquals(1, off3.size(), "区间外的一行没被说出来：" + off3);
        // 覆盖边界要说清：形状 B 只核点得到 void 声明的名字，类名/`src/main` 里的方法现在没人核
        assertTrue(uncheckedB.size() > 0,
                "这一轮报告里竟没有一处形状 B 指向非 void 方法，那条覆盖边界无据可写");
    }

    // ------------------------------------------- 报告转述的 README 计数（第六把）

    /** 报告 §13.1 附的那行复算命令的期望输出，五个数一处不落。 */
    private static final Pattern CITE_COMPACT = Pattern.compile(
            "bug=([0-9]+) camel=([0-9]+) reflines=([0-9]+) ids=([0-9]+) secs=([0-9]+)");

    /** 报告 §13.1 开头那句散文里的同一批数：只按出现顺序取数，不关心中间怎么加粗、怎么打反引号。 */
    private static final Pattern CITE_PROSE = Pattern.compile(
            "这几批：(\\d+)[^0-9]*(\\d+)[^0-9]*(\\d+)[^0-9]*(\\d+)[^0-9]*(\\d+)[^0-9]*个驼峰标识符");

    private static final String[] COMPACT_KEYS = {"bug", "camel", "reflines", "ids", "secs"};
    private static final String[] PROSE_KEYS = {"bug", "reflines", "ids", "secs", "camel"};

    /**
     * README 正文里那五批指针的现场计数。口径与报告附的那条复算命令同源：
     * 全部先剥 ``` 围栏，"引用行"指含"报告"且带 `§` 或缺陷编号的行，各集合取唯一值。
     */
    private static Map<String, Integer> citeCounts(String readmeProse) {
        Map<String, Integer> out = new LinkedHashMap<String, Integer>();
        Set<String> bug = new LinkedHashSet<String>();
        Matcher m = BUG_CITED.matcher(readmeProse);
        while (m.find()) {
            bug.add(m.group(1));
        }
        Set<String> camel = new LinkedHashSet<String>();
        m = PROSE_CAMEL.matcher(readmeProse);
        while (m.find()) {
            camel.add(m.group(1));
        }
        Set<String> ids = new LinkedHashSet<String>();
        Set<String> secs = new LinkedHashSet<String>();
        int refLines = 0;
        for (String line : readmeProse.split("\n")) {
            if (!line.contains("报告")) {
                continue;
            }
            Matcher dm = REPORT_DEFECT_ID.matcher(line);
            Matcher sm = SEC_REF.matcher(line);
            boolean hasId = dm.find();
            boolean hasSec = sm.find();
            if (!hasId && !hasSec) {
                continue;
            }
            refLines++;
            if (hasId) {
                do {
                    ids.add(dm.group(1));
                } while (dm.find());
            }
            if (hasSec) {
                do {
                    secs.add(sm.group(1));
                } while (sm.find());
            }
        }
        out.put("bug", bug.size());
        out.put("camel", camel.size());
        out.put("reflines", refLines);
        out.put("ids", ids.size());
        out.put("secs", secs.size());
        return out;
    }

    /** 按同一种形状把一组数拼成文本 —— 猎物就是这么造的，字面量因此不落进任何一份文档。 */
    private static String compactString(Map<String, Integer> v) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < COMPACT_KEYS.length; i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(COMPACT_KEYS[i]).append('=').append(v.get(COMPACT_KEYS[i]));
        }
        return sb.toString();
    }

    private static String proseString(Map<String, Integer> v) {
        return "这几批：" + v.get("bug") + " 个 `bug_*` 用例名、" + v.get("reflines")
                + " 行含\"报告\"的引用（" + v.get("ids") + " 个 H/N 缺陷编号 + **" + v.get("secs")
                + "** 个 `§x.y` 小节号 + 若干\"第 N 行/条\"）、" + v.get("camel") + " 个驼峰标识符";
    }

    /** 文档里写着的数与现场数出来的数逐一对账，返回对不上的条目。 */
    private static List<String> staleCiteCounts(String doc, Map<String, Integer> truth) {
        List<String> bad = new ArrayList<String>();
        Matcher cm = CITE_COMPACT.matcher(doc);
        while (cm.find()) {
            for (int i = 0; i < COMPACT_KEYS.length; i++) {
                int written = Integer.parseInt(cm.group(i + 1));
                int measured = truth.get(COMPACT_KEYS[i]).intValue();
                if (written != measured) {
                    bad.add("复算串把 " + COMPACT_KEYS[i] + " 写成 " + written + "，README 现场实测是 " + measured);
                }
            }
        }
        Matcher pm = CITE_PROSE.matcher(doc);
        while (pm.find()) {
            for (int i = 0; i < PROSE_KEYS.length; i++) {
                int written = Integer.parseInt(pm.group(i + 1));
                int measured = truth.get(PROSE_KEYS[i]).intValue();
                if (written != measured) {
                    bad.add("「这几批」那句把 " + PROSE_KEYS[i] + " 写成 " + written
                            + "，README 现场实测是 " + measured);
                }
            }
        }
        return bad;
    }

    @Test
    @DisplayName("报告转述的 README 指针计数必须等于现场重数的数 —— 转述过的数没有尺就会 rot，这次它已经 rot 过一回")
    void countsQuotedInReportMatchRecompute() throws IOException {
        Map<String, Integer> truth = citeCounts(prose(readme()));
        for (String k : COMPACT_KEYS) {
            assertTrue(truth.get(k).intValue() > 0, "现场数出来的 " + k + " 是 0，口径或抽取器坏了");
        }
        String rp = report();
        int nCompact = findAll(CITE_COMPACT, rp).size();
        int nProse = findAll(CITE_PROSE, rp).size();
        assertTrue(nCompact >= 1, "报告里一处复算串都没数到：这把尺没有对象，通过不算证据");
        assertEquals(1, nProse, "报告里「这几批：…」那句数到 " + nProse + " 处，两句就不知以哪句为准");

        // 先读盘上的真文档 —— 少了这一句，整条用例只是在验尺自己拼的字符串
        assertEquals(Collections.emptyList(), staleCiteCounts(rp, truth),
                "报告转述的 README 计数与现场重数的对不上（README 一改这类数就漂，而除了这把尺没人会去读它）");

        // 正例：尺自己按同口径拼出来的串必须干净（否则上面那些红可能只是尺钝）
        assertTrue(staleCiteCounts(compactString(truth), truth).isEmpty(), "真值串被误判");
        assertTrue(staleCiteCounts(proseString(truth), truth).isEmpty(), "真值散文句被误判");

        // 猎物：在真值串上各动一刀（五个位置），每一处都必须被单独说出、且点名是哪一批
        for (String key : COMPACT_KEYS) {
            Map<String, Integer> bumped = new LinkedHashMap<String, Integer>(truth);
            bumped.put(key, bumped.get(key).intValue() + 1);
            List<String> off = staleCiteCounts(compactString(bumped), truth);
            assertEquals(1, off.size(), "把 " + key + " 加 1 没被单独说出来：" + off);
            assertTrue(off.get(0).contains(key), "红消息没点名是哪一批：" + off);
        }
        Map<String, Integer> proseBumped = new LinkedHashMap<String, Integer>(truth);
        proseBumped.put("secs", proseBumped.get("secs").intValue() + 1);
        List<String> poff = staleCiteCounts(proseString(proseBumped), truth);
        assertEquals(1, poff.size(), "散文句里把 secs 加 1 没被单独说出来：" + poff);
        assertTrue(poff.get(0).contains("secs") && poff.get(0).contains("这几批"),
                "散文句猎物的红法不对：" + poff);
    }
}
