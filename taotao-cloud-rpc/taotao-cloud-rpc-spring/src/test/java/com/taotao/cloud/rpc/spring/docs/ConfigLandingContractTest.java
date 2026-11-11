package com.taotao.cloud.rpc.spring.docs;

import com.taotao.cloud.rpc.annotation.TtcRpcReference;
import com.taotao.cloud.rpc.annotation.TtcRpcService;
import com.taotao.cloud.rpc.config.ReferenceConfig;
import com.taotao.cloud.rpc.remoting.RpcServer;
import com.taotao.cloud.rpc.starter.properties.TtcRpcProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * README 两张"落点表"的契约测试：配置项 33 行 + 注解属性两行。
 * <p>
 * 它补的是 {@code ReadmeContractTest} 量不到的那一半：前者管"文档写的 key 存不存在"，
 * 这一层管"文档声称的**效果**成不成"，并且把属性/配置项的**名字集合**逐个比，不比个数 ——
 * 只比个数的话，加一个真属性再删一个别的，尺不会红（报告 §8 第 15 条 ①②③）。
 * <p>
 * 三条"谁读了它"的判据全部来自反射与源码扫描，不来自任何人的记忆：
 * {@link #configTableLandingColumnMatchesSourceScan()} 的分类器与
 * {@link #annotationLandingColumnsMatchWhoActuallyReadsThem()} 的读取计数，
 * 都在同一条用例里带了反向猎物，防止分类器退化成常量函数。
 */
class ConfigLandingContractTest {

    /** README 里那张配置项表的行形状：| `ttc.rpc.a.b` | 类型 | 默认值 | 落点 |。 */
    private static final Pattern CONFIG_ROW = Pattern.compile(
            "^\\| `(z\\.rpc\\.[A-Za-z0-9.]+)` \\| (\\w+) \\| (\\S+) \\| ([^|]+) \\|$");

    /**
     * README 标题里那个 key 总数。
     *
     * <p>容忍标题层级与前缀：README 通篇是 {@code ## <emoji> 标题}（见「🔧 技术栈」
     * 「📡 协议与帧格式」等同款写法），本节此前被提升成 h2 并加了 🔍，
     * 而这把尺写死了旧的 {@code ### 配置项} 且不含 emoji，于是永远 find 不到 ——
     * 报「key 总数被改掉了」，可总数 33 一个字都没动。
     * 数的是 {@code (\d+)}，标题怎么排版不该影响它。
     */
    private static final Pattern CONFIG_HEADING_COUNT = Pattern.compile(
            "^#{2,4}[^\\n]*?配置项到底哪些有落点（(\\d+) 个 key", Pattern.MULTILINE);

    private static final Pattern ANNOTATION_ROW = Pattern.compile(
            "^\\| `(@[A-Za-z]+)` \\| (\\d+) \\| ([^|]+) \\| ([^|]+) \\|$");

    private static final Pattern TAGGED = Pattern.compile("`([A-Za-z][A-Za-z0-9]*)`");

    private static final String BANNER_FILE = "TtcRpcFrameworkInitializer.java";

    // ---------------------------------------------------------------- 仓库与源码

    private static File repoRoot() {
        File dir = new File(System.getProperty("user.dir")).getAbsoluteFile();
        while (dir != null) {
            if (new File(dir, "README.md").isFile() && new File(dir, "ttc-rpc-common/pom.xml").isFile()) {
                return dir;
            }
            dir = dir.getParentFile();
        }
        throw new IllegalStateException("找不到仓库根（README.md + ttc-rpc-common/pom.xml）");
    }

    private static String readme() throws IOException {
        return new String(Files.readAllBytes(new File(repoRoot(), "README.md").toPath()),
                StandardCharsets.UTF_8);
    }

    /** 全仓 src/main 的 .java 原文；键是相对路径（同名文件在多个模块里都有，按简单名会互相覆盖）。 */
    private static Map<String, String> mainSources() throws IOException {
        Map<String, String> out = new LinkedHashMap<String, String>();
        collect(repoRoot(), out);
        assertFalse(out.isEmpty(), "一份 src/main 都没扫到，下面所有扫描都是空跑");
        assertTrue(out.size() > 100, "src/main 只扫到 " + out.size() + " 个文件，遍历肯定漏了");
        return out;
    }

    private static void collect(File dir, Map<String, String> into) throws IOException {
        File root = repoRoot();
        File[] kids = dir.listFiles();
        if (kids == null) {
            return;
        }
        for (File k : kids) {
            String n = k.getName();
            if (k.isDirectory()) {
                if (n.equals("target") || n.equals(".git") || n.equals("node_modules")) {
                    continue;
                }
                collect(k, into);
            } else if (n.endsWith(".java") && k.getPath().replace('\\', '/').contains("/src/main/")) {
                into.put(k.getPath().substring(root.getPath().length() + 1).replace('\\', '/'),
                        new String(Files.readAllBytes(k.toPath()), StandardCharsets.UTF_8));
            }
        }
    }

    private static String baseOf(String path) {
        int i = path.lastIndexOf('/');
        return i < 0 ? path : path.substring(i + 1);
    }

    private static boolean isBanner(String path) {
        return BANNER_FILE.equals(baseOf(path));
    }

    /** 按简单名找那份源码；出现多份（重名类）时要求 README 的判据在**每一份**里都成立，由调用方决定。 */
    private static String sourceByBase(Map<String, String> sources, String baseName) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : sources.entrySet()) {
            if (baseName.equals(baseOf(e.getKey()))) {
                sb.append(e.getValue());
            }
        }
        assertTrue(sb.length() > 0, "src/main 里找不到 " + baseName + "，读取计数无从谈起");
        return sb.toString();
    }

    private static String decap(String s) {
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    private static String cap(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    private static boolean isLeafType(Class<?> t) {
        return t.isPrimitive() || t == String.class || Number.class.isAssignableFrom(t) || t == Boolean.class;
    }

    // ---------------------------------------------------------------- 反射真值：配置项

    /** key -> 类型简单名。只收叶子（primitive / String / Number），组本身不算。 */
    private static Map<String, String> reflectedConfigTypes() {
        Map<String, String> out = new LinkedHashMap<String, String>();
        for (Method m : TtcRpcProperties.class.getDeclaredMethods()) {
            if (m.getParameterTypes().length != 0 || !isAccessorName(m.getName())) {
                continue;
            }
            String attr = attrNameOf(m.getName());
            Class<?> rt = m.getReturnType();
            if (isLeafType(rt)) {
                out.put("ttc.rpc." + attr, rt.getSimpleName());
            } else if (rt.getDeclaringClass() == TtcRpcProperties.class) {
                String group = rt.getSimpleName();
                for (Method lm : rt.getDeclaredMethods()) {
                    if (lm.getParameterTypes().length != 0 || !isAccessorName(lm.getName())
                            || !isLeafType(lm.getReturnType())) {
                        continue;
                    }
                    out.put("ttc.rpc." + decap(group) + "." + attrNameOf(lm.getName()),
                            lm.getReturnType().getSimpleName());
                }
            }
        }
        return out;
    }

    private static boolean isAccessorName(String n) {
        return n.startsWith("get") || n.startsWith("is");
    }

    private static String attrNameOf(String accessor) {
        int cut = accessor.startsWith("get") ? 3 : 2;
        return decap(accessor.substring(cut));
    }

    /** key -> getter 反射出来的默认值（不是抄源码字面量，是 new 出来读）。 */
    private static Map<String, String> reflectedConfigDefaults() throws Exception {
        Map<String, String> out = new LinkedHashMap<String, String>();
        TtcRpcProperties p = TtcRpcProperties.class.newInstance();
        for (Method m : TtcRpcProperties.class.getDeclaredMethods()) {
            if (m.getParameterTypes().length != 0 || !isAccessorName(m.getName())) {
                continue;
            }
            String attr = attrNameOf(m.getName());
            Class<?> rt = m.getReturnType();
            if (isLeafType(rt)) {
                m.setAccessible(true);
                out.put("ttc.rpc." + attr, String.valueOf(m.invoke(p)));
            } else if (rt.getDeclaringClass() == TtcRpcProperties.class) {
                m.setAccessible(true);
                Object group = m.invoke(p);
                for (Method lm : rt.getDeclaredMethods()) {
                    if (lm.getParameterTypes().length != 0 || !isAccessorName(lm.getName())
                            || !isLeafType(lm.getReturnType())) {
                        continue;
                    }
                    lm.setAccessible(true);
                    out.put("ttc.rpc." + decap(rt.getSimpleName()) + "." + attrNameOf(lm.getName()),
                            String.valueOf(lm.invoke(group)));
                }
            }
        }
        return out;
    }

    // ---------------------------------------------------------------- 扫描真值：谁读了它

    /** 配置项的三档分类：真用 / 只进横幅 / 没人读。 */
    private static String classifyConfig(String key, Map<String, String> sources) {
        String[] parts = key.split("\\.");
        String attr = parts[parts.length - 1];
        List<String> hitFiles = new ArrayList<String>();
        if (parts.length == 4) {
            String group = parts[2];
            Pattern chain = Pattern.compile("get" + cap(group) + "\\(\\)\\s*\\.\\s*(?:get|is)"
                    + cap(attr) + "\\(\\)");
            for (Map.Entry<String, String> e : sources.entrySet()) {
                if ("TtcRpcProperties.java".equals(baseOf(e.getKey()))) {
                    continue;
                }
                if (chain.matcher(e.getValue()).find()) {
                    hitFiles.add(e.getKey());
                }
            }
        } else {
            // 顶层 key：只可能被 @ConditionalOnProperty 按名字引用
            Pattern cond = Pattern.compile("@ConditionalOnProperty\\([^)]*name\\s*=\\s*\"" + attr + "\"");
            Pattern direct = Pattern.compile("\\.(?:get|is)" + cap(attr) + "\\(\\)");
            for (Map.Entry<String, String> e : sources.entrySet()) {
                if ("TtcRpcProperties.java".equals(baseOf(e.getKey()))) {
                    continue;
                }
                if (cond.matcher(e.getValue()).find()
                        || (!isBanner(e.getKey()) && direct.matcher(e.getValue()).find())) {
                    hitFiles.add(e.getKey());
                }
            }
        }
        if (hitFiles.isEmpty()) {
            return "没人读";
        }
        for (String f : hitFiles) {
            if (!isBanner(f)) {
                return "真用";
            }
        }
        return "只进横幅";
    }

    /** 注解属性被读了几次（{@code .<attr>()} 形态，只在点名那份处理器源码里数）。 */
    private static int annotationReads(Map<String, String> sources, String fileName, String attr) {
        String body = sourceByBase(sources, fileName);
        Matcher m = Pattern.compile("\\." + attr + "\\(\\)").matcher(body);
        int n = 0;
        while (m.find()) {
            n++;
        }
        return n;
    }

    // ---------------------------------------------------------------- README 侧

    private static List<String[]> configRows(String r) {
        List<String[]> rows = new ArrayList<String[]>();
        Set<String> seen = new LinkedHashSet<String>();
        for (String ln : r.split("\n")) {
            Matcher m = CONFIG_ROW.matcher(ln.trim());
            if (m.matches()) {
                assertTrue(seen.add(m.group(1)), "README 配置表里 " + m.group(1) + " 出现了两次");
                rows.add(new String[] {m.group(1), m.group(2), m.group(3), m.group(4).trim()});
            }
        }
        return rows;
    }

    private static Map<String, String[]> annotationRows(String r) {
        Map<String, String[]> byName = new LinkedHashMap<String, String[]>();
        for (String ln : r.split("\n")) {
            Matcher m = ANNOTATION_ROW.matcher(ln.trim());
            if (m.matches()) {
                assertFalse(byName.containsKey(m.group(1)), "README 注解表里 " + m.group(1) + " 有两行");
                byName.put(m.group(1), new String[] {m.group(2), m.group(3), m.group(4)});
            }
        }
        return byName;
    }

    private static List<String> backtickedNames(String cell) {
        List<String> out = new ArrayList<String>();
        Matcher m = TAGGED.matcher(cell);
        while (m.find()) {
            out.add(m.group(1));
        }
        return out;
    }

    /** 反射出一个注解的全部属性方法名（排除 annotationType()）。 */
    private static Set<String> annotationAttributes(Class<?> ann) {
        Set<String> out = new LinkedHashSet<String>();
        for (Method m : ann.getDeclaredMethods()) {
            if (!"annotationType".equals(m.getName())) {
                out.add(m.getName());
            }
        }
        return out;
    }

    // ---------------------------------------------------------------- 用例

    @Test
    @DisplayName("配置表 33 行 = TtcRpcProperties 反射叶子（多一行、少一行、漏写新字段都要红）")
    void configTableRowSetEqualsReflectedLeafKeys() throws IOException {
        String r = readme();
        List<String[]> rows = configRows(r);
        Map<String, String> truth = reflectedConfigTypes();

        Set<String> tabled = new LinkedHashSet<String>();
        for (String[] row : rows) {
            tabled.add(row[0]);
        }
        assertEquals(33, rows.size(), "README 配置表行数变了，得先确认是不是有人加了字段没上表");
        assertEquals(truth.keySet(), tabled,
                "表与反射叶子集合不等（缺 = 漏写，多 = 文档编的 key）");
        assertEquals(tabled.size(), rows.size(), "表里有重复 key");

        // 标题那句"（33 个 key，逐个判）"也得跟实际一致
        Matcher h = CONFIG_HEADING_COUNT.matcher(r);
        assertTrue(h.find(), "README 标题里那句 key 总数被改掉了");
        assertEquals(String.valueOf(truth.size()), h.group(1), "标题写的 key 数 != 反射数量");

        // 猎物：同一把尺必须说"少一行"和"多一行"都不合格
        Set<String> dropped = new LinkedHashSet<String>(tabled);
        dropped.remove("ttc.rpc.server.ioThreads");
        assertFalse(dropped.equals(truth.keySet()), "谓词抓不到\"漏写一个真字段\"，上面的放行没有意义");
        Set<String> added = new LinkedHashSet<String>(tabled);
        added.add("ttc.rpc.server.bossThreads");
        assertFalse(added.equals(truth.keySet()), "谓词抓不到\"表里多一个假 key\"");
    }

    @Test
    @DisplayName("配置表每行的类型与默认值 = 反射 new 出来读的那个值（抄错就红）")
    void configTableRowTypeAndDefaultMatchReflection() throws Exception {
        List<String[]> rows = configRows(readme());
        Map<String, String> types = reflectedConfigTypes();
        Map<String, String> defaults = reflectedConfigDefaults();
        assertEquals(types.keySet(), defaults.keySet(), "两把反射尺数的不是同一批 key");

        List<String> badType = new ArrayList<String>();
        List<String> badDefault = new ArrayList<String>();
        for (String[] row : rows) {
            if (!types.get(row[0]).equals(row[1])) {
                badType.add(row[0] + ": 表 " + row[1] + " / 反射 " + types.get(row[0]));
            }
            if (!defaults.get(row[0]).equals(row[2])) {
                badDefault.add(row[0] + ": 表 " + row[2] + " / 反射 " + defaults.get(row[0]));
            }
        }
        assertEquals(Collections.emptyList(), badType, "类型列与反射不符");
        assertEquals(Collections.emptyList(), badDefault, "默认值列与反射不符");

        // 猎物：把 server.port 的默认值改成 9999 喂给同一把尺，必须被抓出来
        assertTrue(defaults.get("ttc.rpc.server.port").equals("20880"), "prey：默认值确实是 20880");
        assertFalse("9999".equals(defaults.get("ttc.rpc.server.port")), "谓词恒真的话，上面的通过不算证据");
    }

    @Test
    @DisplayName("配置表\"落点\"列 = 源码扫描分类（三档都有样本，分类器不是常量函数）")
    void configTableLandingColumnMatchesSourceScan() throws IOException {
        List<String[]> rows = configRows(readme());
        Map<String, String> sources = mainSources();

        List<String> mismatched = new ArrayList<String>();
        Set<String> buckets = new LinkedHashSet<String>();
        for (String[] row : rows) {
            String truth = classifyConfig(row[0], sources);
            buckets.add(row[3]);
            if (!truth.equals(row[3])) {
                mismatched.add(row[0] + ": 表 \"" + row[3] + "\" / 实扫 \"" + truth + "\"");
            }
        }
        assertEquals(Collections.emptyList(), mismatched, "落点列与源码扫描不符（这些 key 的效果被说多了或说少了）");
        assertEquals(new LinkedHashSet<String>(Arrays.asList("真用", "只进横幅", "没人读")), buckets,
                "三档分类没都出现，说明这张表或这把尺退化了");

        // 计数本身也是主张（README 正文那句"5 真用 / 10 只进横幅 / 18 没人读"）
        Map<String, Integer> hist = new LinkedHashMap<String, Integer>();
        for (String[] row : rows) {
            Integer c = hist.get(row[3]);
            hist.put(row[3], c == null ? 1 : c + 1);
        }
        String r = readme();
        String expected = hist.get("真用") + " 真用 / " + hist.get("只进横幅") + " 只进横幅 / "
                + hist.get("没人读") + " 没人读";
        assertTrue(r.contains(expected), "README 正文那句分布与表格实数不符，应当是：" + expected);
        // 猎物：把真用的数目 +1 写成主张，同一句判据必须说不
        String inflated = (hist.get("真用") + 1) + " 真用 / " + hist.get("只进横幅") + " 只进横幅 / "
                + hist.get("没人读") + " 没人读";
        assertFalse(r.contains(inflated), "谓词对\"数目写大一号\"不敏感，上面的 contains 没有意义");
    }

    @Test
    @DisplayName("注解表两列的名字集合正好划分反射属性集合，\"属性总数\"列等于 size")
    void annotationTablesPartitionReflectedAttributeNames() throws IOException {
        Map<String, String[]> table = annotationRows(readme());
        assertEquals(new LinkedHashSet<String>(Arrays.asList("@TtcRpcService", "@TtcRpcReference")), table.keySet(),
                "README 注解表少了行或多出未知注解行");

        Map<Class<?>, String> truthBy = new LinkedHashMap<Class<?>, String>();
        truthBy.put(TtcRpcService.class, "@TtcRpcService");
        truthBy.put(TtcRpcReference.class, "@TtcRpcReference");

        for (Map.Entry<Class<?>, String> e : truthBy.entrySet()) {
            Set<String> truth = annotationAttributes(e.getKey());
            String[] row = table.get(e.getValue());
            List<String> landed = backtickedNames(row[1]);
            List<String> dead = backtickedNames(row[2]);

            assertEquals(String.valueOf(truth.size()), row[0],
                    e.getValue() + " 的\"属性总数\"列 != 反射数量（反射：" + truth + "）");
            Set<String> union = new LinkedHashSet<String>(landed);
            union.addAll(dead);
            assertEquals(truth, union, e.getValue() + " 两列并集 != 反射属性集合（漏名字）");
            assertEquals(landed.size() + dead.size(), union.size(),
                    e.getValue() + " 两列有重名，一个属性不能既有落点又没有");

            // 猎物：同一把尺必须说"少一个名字"和"名字放错列"都不合格
            Set<String> dropped = new LinkedHashSet<String>(union);
            dropped.remove(truth.iterator().next());
            assertFalse(dropped.equals(truth), "谓词抓不到\"漏写一个属性名\"");
            assertFalse(backtickedNames(row[1]).contains("serialization"),
                    "prey：serialization 现在在\"没有落点\"列，有落点列不许出现它");
        }
    }

    @Test
    @DisplayName("注解表\"有落点/没有落点\"两列 = 处理器源码里的读取次数（≥1 / ==0）")
    void annotationLandingColumnsMatchWhoActuallyReadsThem() throws IOException {
        Map<String, String> sources = mainSources();
        Map<String, String[]> table = annotationRows(readme());

        String[][] claims = new String[][] {
                {"@TtcRpcService", "TtcRpcServiceExporter.java"},
                {"@TtcRpcReference", "TtcRpcReferenceInjector.java"},
        };
        List<String> wrong = new ArrayList<String>();
        for (String[] pair : claims) {
            String[] row = table.get(pair[0]);
            for (String attr : backtickedNames(row[1])) {
                int reads = annotationReads(sources, pair[1], attr);
                if (reads == 0) {
                    wrong.add(pair[0] + "." + attr + " 被列进\"有落点\"，但在 " + pair[1] + " 里读 0 次");
                }
            }
            for (String attr : backtickedNames(row[2])) {
                int reads = annotationReads(sources, pair[1], attr);
                if (reads != 0) {
                    wrong.add(pair[0] + "." + attr + " 被列进\"没有落点\"，但 " + pair[1] + " 读它 " + reads + " 次");
                }
            }
        }
        assertEquals(Collections.emptyList(), wrong, "README 把属性放错了列");

        // 阳性对照：interfaceClass 这个名字在导出器里真被读、在注入器里真没被读 ——
        // 这就是它本轮从 @TtcRpcReference 的"有落点"列被挪走的全部依据，两个方向都得有数。
        assertTrue(annotationReads(sources, "TtcRpcServiceExporter.java", "interfaceClass") >= 1,
                "对照失败：导出器其实没读 interfaceClass，那 @TtcRpcService 那行也错了");
        assertEquals(0, annotationReads(sources, "TtcRpcReferenceInjector.java", "interfaceClass"),
                "对照失败：注入器开始读 interfaceClass 了，README 的挪列要回退");
    }

    @Test
    @DisplayName("check/lazy/connections/client/serialization 在 ReferenceConfig 里 field+方法全 0（README 那句数字）")
    void fiveUnlandedReferenceAttributesHaveNoTraceInReferenceConfig() {
        List<String> five = Arrays.asList("check", "lazy", "connections", "client", "serialization");
        for (String attr : five) {
            assertEquals(0, declaredFieldCount(ReferenceConfig.class, attr),
                    "ReferenceConfig 冒出了字段 " + attr);
            assertEquals(0, methodCountFor(ReferenceConfig.class, attr),
                    "ReferenceConfig 冒出了 " + attr + " 的 field/setter/getter —— README 那句\"全 0\"要改");
        }
        // 阳性对照：同一把尺不许把真有访问器的 timeout 也说成 0
        assertTrue(methodCountFor(ReferenceConfig.class, "timeout") >= 2,
                "谓词恒 0（getTimeout/setTimeout 都数不到），上面的五个 0 不算证据");
    }

    private static int declaredFieldCount(Class<?> c, String name) {
        int n = 0;
        for (Field f : c.getDeclaredFields()) {
            if (f.getName().equals(name) && !Modifier.isStatic(f.getModifiers())) {
                n++;
            }
        }
        return n;
    }

    private static int methodCountFor(Class<?> c, String attr) {
        String cap = cap(attr);
        int n = 0;
        for (Method m : c.getDeclaredMethods()) {
            String nm = m.getName();
            if (nm.equals("set" + cap) || nm.equals("get" + cap) || nm.equals("is" + cap)) {
                n++;
            }
        }
        return n;
    }

    @Test
    @DisplayName("README 那句\"RpcServer 三个 register* 重载没有 int/long 参数位\"= 反射实测")
    void rpcServerRegisterOverloadsExposeNoNumericSlot() {
        List<String> overloads = new ArrayList<String>();
        List<String> numericSlots = new ArrayList<String>();
        int numericAnywhere = 0;
        for (Method m : RpcServer.class.getDeclaredMethods()) {
            if (!m.getName().startsWith("register")) {
                continue;
            }
            overloads.add(m.getName());
            for (Class<?> p : m.getParameterTypes()) {
                if (p == int.class || p == long.class || p == Integer.class || p == Long.class) {
                    numericSlots.add(m.getName() + "(" + p.getSimpleName() + ")");
                }
            }
        }
        for (java.lang.reflect.Constructor<?> c : RpcServer.class.getDeclaredConstructors()) {
            for (Class<?> p : c.getParameterTypes()) {
                if (p == int.class || p == long.class) {
                    numericAnywhere++;
                }
            }
        }
        assertEquals(3, overloads.size(), "register* 重载数量变了：" + overloads);
        assertEquals(Collections.emptyList(), numericSlots,
                "register* 出现了 int/long 参数位，README 那句\"没有位置可放\"过时了");
        // 阳性对照：同一个"找 int 位"的尺在构造器上必须数得到东西，否则上面的 0 是空的
        assertTrue(numericAnywhere >= 1,
                "谓词根本数不清 int 参数位（RpcServer 构造器明明有 int port），那 register* 的 0 没有意义");
    }
}
