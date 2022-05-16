package com.taotao.cloud.elasticsearch.esearchx;

import com.taotao.cloud.elasticsearch.esearchx.exception.NoExistException;
import com.taotao.cloud.elasticsearch.esearchx.model.EsAggs;
import com.taotao.cloud.elasticsearch.esearchx.model.EsCondition;
import com.taotao.cloud.elasticsearch.esearchx.model.EsData;
import com.taotao.cloud.elasticsearch.esearchx.model.EsHighlight;
import com.taotao.cloud.elasticsearch.esearchx.model.EsSort;
import com.taotao.cloud.elasticsearch.esearchx.model.EsSource;
import org.noear.snack.ONode;
import org.noear.snack.core.Options;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * ElasticSearch 查询构建器
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:09:31
 */
public class EsQuery {
    private final EsContext context;
    private final String indiceName;
    private final boolean isStream;

    private ONode dslq;
    private ONode queryMatch;
    private ONode item;

    private Options options;

    protected EsQuery(EsContext context, String indiceName, boolean isStream) {
        this.context = context;
        this.indiceName = indiceName;
        this.isStream = isStream;
        this.options = Options.def();
    }

    private PriHttpUtils getHttp(String path) {
        return context.getHttp(path);
    }

    private ONode getDslq() {
        if (dslq == null) {
            dslq = PriUtils.newNode().asObject();
        }

        return dslq;
    }

    private ONode getQueryMatch() {
        if (queryMatch == null) {
            queryMatch = PriUtils.newNode().asObject();
        }

        return queryMatch;
    }

    private String getJson(ONode oNode){
        return oNode.options(options).toJson();
    }

    public EsQuery options(Options options) {
        if (options != null) {
            this.options = options;
        }

        return this;
    }

    public EsQuery options(Consumer<Options> builder) {
        builder.accept(options);
        return this;
    }


    public EsQuery set(String field, Object value) {
        if (item == null) {
            item = PriUtils.newNode();
        }

        item.set(field, value);
        return this;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //
    // insert
    //

    private String insertDo(ONode doc) throws IOException {
        EsCommand cmd = new EsCommand();
        cmd.timeout = timeout;
        cmd.method = PriWw.method_post;
        cmd.dslType = PriWw.mime_json;
        cmd.dsl = getJson(doc);
        cmd.path = String.format("/%s/_doc/", indiceName);

	    return context.execAsBody(cmd);
    }

    private String upsertDo(String docId, ONode doc) throws IOException {
        EsCommand cmd = new EsCommand();
        cmd.timeout = timeout;
        cmd.method = PriWw.method_put;
        cmd.dslType = PriWw.mime_json;
        cmd.dsl = getJson(doc);
        cmd.path = String.format("/%s/_doc/%s", indiceName, docId);

	    return context.execAsBody(cmd);
    }

    /**
     * 插入
     */
    public String insert() throws IOException {
        return insertDo(item);
    }

    public <T> String insert(T doc) throws IOException {
        if (doc instanceof ONode) {
            return insertDo((ONode) doc);
        } else {
            return insertDo(ONode.loadObj(doc, options));
        }
    }

    public <T> String insertList(List<T> docs) throws IOException {
        StringBuilder docJson = new StringBuilder();
        String type = (isStream ? "create" : "index");

        docs.forEach((doc) -> {
            docJson.append(getJson(PriUtils.newNode().build(n -> n.getOrNew(type).asObject()))).append("\n");

            if (doc instanceof ONode) {
                docJson.append(getJson((ONode) doc)).append("\n");
            } else {
                docJson.append(getJson(ONode.loadObj(doc, options))).append("\n");
            }
        });

        EsCommand cmd = new EsCommand();
        cmd.timeout = timeout;
        cmd.method = PriWw.method_post;
        cmd.dslType = PriWw.mime_ndjson;
        cmd.dsl = docJson.toString();

        if (context.getVersion() > Constants.Es7) {
            cmd.path = String.format("/%s/_bulk", indiceName); //"/_bulk";
        } else {
            cmd.path = String.format("/%s/_doc/_bulk", indiceName);
        }

        String tmp = context.execAsBody(cmd); //需要 post

        return tmp;
    }


    public String upsert(String docId) throws IOException {
        return upsertDo(docId, item);
    }

    public <T> String upsert(String docId, T doc) throws IOException {
        if (doc instanceof ONode) {
            return upsertDo(docId, (ONode) doc);
        } else {
            return upsertDo(docId, ONode.loadObj(doc, options));
        }
    }

    public <T> String upsertList(Map<String, T> docs) throws IOException {
        StringBuilder docJson = new StringBuilder();
        String type = (isStream ? "create" : "index");

        docs.forEach((docId, doc) -> {
            docJson.append(getJson(PriUtils.newNode().build(n -> n.getOrNew(type).set("_id", docId)))).append("\n");
            if (doc instanceof ONode) {
                docJson.append(getJson((ONode) doc)).append("\n");
            } else {
                docJson.append(getJson(ONode.loadObj(doc, options))).append("\n");
            }
        });

        EsCommand cmd = new EsCommand();
        cmd.timeout = timeout;
        cmd.method = PriWw.method_post;
        cmd.dslType = PriWw.mime_ndjson;
        cmd.dsl = docJson.toString();

        if (context.getVersion() > Constants.Es7) {
            cmd.path = String.format("/%s/_bulk", indiceName);//cmd.path = "/_bulk";
        } else {
            cmd.path = String.format("/%s/_doc/_bulk", indiceName);
        }

        String tmp = context.execAsBody(cmd); //需要 post

        return tmp;
    }


    //
    // select
    //
    public EsQuery where(Consumer<EsCondition> condition) {
        ONode oNode1 = PriUtils.newNode();
        EsCondition c = new EsCondition(oNode1);
        condition.accept(c);
        getDslq().set("query", oNode1);
        return this;
    }

    private String _format;
    public EsQuery sql(String sql, String format) {
        _format = format;

        getDslq().set("query", sql);
        return this;
    }

    public EsQuery sql(String sql) {
        return sql(sql, "json");
    }


    private static final int limit_max_hits = 10000;

    public EsQuery limit(int start, int size) {
        getDslq().set("from", start);
        getDslq().set("size", size);

        if (size >= limit_max_hits || (start + size) >= limit_max_hits) {
            getDslq().set("track_total_hits", "true");
        }

        return this;
    }

    public EsQuery limit(int size) {
        getDslq().set("size", size);

        if (size >= limit_max_hits) {
            getDslq().set("track_total_hits", "true");
        }

        return this;
    }

    //
    //排序
    //

    public EsQuery orderBy(Consumer<EsSort> sort) {
        EsSort s = new EsSort(getDslq().getOrNew("sort").asArray());
        sort.accept(s);
        return this;
    }

    public EsQuery orderByAsc(String field) {
        getDslq().getOrNew("sort").addNew().getOrNew(field).set("order", "asc");
        return this;
    }

    public EsQuery orderByDesc(String field) {
        getDslq().getOrNew("sort").addNew().getOrNew(field).set("order", "desc");
        return this;
    }

    public EsQuery andByAsc(String field) {
        getDslq().getOrNew("sort").addNew().getOrNew(field).set("order", "asc");
        return this;
    }

    public EsQuery andByDesc(String field) {
        getDslq().getOrNew("sort").addNew().getOrNew(field).set("order", "desc");
        return this;
    }

    /**
     * search_after
     */
    public EsQuery onAfter(Object... values) {
        getDslq().getOrNew("search_after").addAll(Arrays.asList(values));
        return this;
    }

    /**
     * min_score
     */
    public EsQuery minScore(Object value) {
        getDslq().getOrNew("min_score").val(value);
        return this;
    }

    //
    //aggs
    //

    public EsQuery aggs(Consumer<EsAggs> aggs) {
        EsAggs a = new EsAggs(getDslq().getOrNew("aggs"));
        aggs.accept(a);
        return this;
    }

    //
    //highlight
    //

    public EsQuery highlight(Consumer<EsHighlight> highlight){
        EsHighlight h = new EsHighlight(getDslq().getOrNew("highlight"));
        highlight.accept(h);
        return this;
    }

    PriHttpTimeout timeout;
    public EsQuery timeout(int timeoutSeconds) {
        if (timeoutSeconds > 0) {
            timeout = new PriHttpTimeout(timeoutSeconds);
        }

        return this;
    }

    public EsQuery timeout(int connectTimeoutSeconds, int writeTimeoutSeconds, int readTimeoutSeconds) {
        if (connectTimeoutSeconds > 0) {
            timeout = new PriHttpTimeout(connectTimeoutSeconds, writeTimeoutSeconds, readTimeoutSeconds);
        }

        return this;
    }

    //
    // select
    //
    public String select(String dsl) throws IOException {
        EsCommand cmd = new EsCommand();
        cmd.timeout = timeout;
        cmd.method = PriWw.method_post;
        cmd.dslType = PriWw.mime_json;
        cmd.dsl = dsl;

        if(PriUtils.isEmpty(_format)){
            cmd.path = String.format("/%s/_search", indiceName);
        }else{
            cmd.path = String.format("/_sql?format=%s", _format);
        }

        String json = context.execAsBody(cmd);

        return json;
    }


    public String selectJson() throws IOException {
        return selectJson(null);
    }

    public String selectJson(String fields) throws IOException {
        if (PriUtils.isNotEmpty(fields)) {
            EsSource s = new EsSource(getDslq().getOrNew("_source"));
            if (fields.startsWith("!")) {
                s.excludes(fields.substring(1).split(","));
            } else {
                s.includes(fields.split(","));
            }
        }

        return select(getJson(getDslq()));
    }

    public ONode selectNode() throws IOException {
        return ONode.loadStr(selectJson());
    }

    public ONode selectNode(String fields) throws IOException {
        return ONode.loadStr(selectJson(fields));
    }

    public ONode selectAggs() throws IOException {
        return selectNode().getOrNew("aggregations");
    }

    public ONode selectAggs(String fields) throws IOException {
        return selectNode(fields).getOrNew("aggregations");
    }


    public Map selectMap() throws IOException {
        return selectOne(Map.class);
    }

    public Map selectMap(String fields) throws IOException {
        return selectOne(Map.class, fields);
    }

    public List<Map> selectMapList() throws IOException {
        return selectList(Map.class).getList();
    }

    public List<Map> selectMapList(String fields) throws IOException {
        return selectList(Map.class, fields).getList();
    }

    public <T> T selectOne(Class<T> clz) throws IOException {
        return selectOne(clz, null);
    }

    public <T> T selectOne(Class<T> clz, String fields) throws IOException {
        limit(1);
        EsData<T> page = selectList(clz, fields);
        if (page.getListSize() > 0) {
            return page.getList().get(0);
        } else {
            return null;
        }
    }

    public <T> EsData<T> selectList(Class<T> clz) throws IOException {
        return selectList(clz, null);
    }

    public <T> EsData<T> selectList(Class<T> clz, String fields) throws IOException {
        if (queryMatch != null) {
            if (queryMatch.count() > 1) {
                getDslq().getOrNew("query").set("multi_match", queryMatch);
            } else {
                getDslq().getOrNew("query").set("match", queryMatch);
            }
        }

        String json = selectJson(fields);

        ONode oHits = ONode.loadStr(json).get("hits");

        long total = oHits.get("total").get("value").getLong();
        double max_score = oHits.get("oHits").getDouble();

        oHits.get("hits").forEach(n -> {
            n.setAll(n.get("_source"));
        });

        List<T> list = oHits.get("hits").toObjectList(clz);

        return new EsData<>(total, max_score, list);
    }


    //
    // selectByIds
    //
    public <T> List<T> selectByIds(Class<T> clz, List<String> docIds) throws IOException {
        try {

            ONode oNode = PriUtils.newNode();
            oNode.getOrNew("query").getOrNew("ids").getOrNew("values").addAll(docIds);

            String json = select(getJson(oNode));

            ONode oHits = ONode.loadStr(json).get("hits");

            oHits.get("hits").forEach(n -> {
                n.setAll(n.get("_source"));
            });

            return oHits.get("hits").toObjectList(clz);
        } catch (NoExistException e) {
            return null;
        }
    }

    //
    // selectById
    //
    public <T> T selectById(Class<T> clz, String docId) throws IOException {
        try {
            EsCommand cmd = new EsCommand();
            cmd.timeout = timeout;
            cmd.method = PriWw.method_get;
            cmd.path = String.format("/%s/_doc/%s", indiceName, docId);

            String tmp = context.execAsBody(cmd);

            ONode oItem = ONode.loadStr(tmp);
            oItem.setAll(oItem.get("_source"));

            return oItem.toObject(clz);
        } catch (NoExistException e) {
            return null;
        }
    }


    //
    // delete
    //

    public String delete() throws IOException {
        if (queryMatch != null) {
            if (queryMatch.count() > 1) {
                getDslq().getOrNew("query").set("multi_match", queryMatch);
            } else {
                getDslq().getOrNew("query").set("match", queryMatch);
            }
        }

        EsCommand cmd = new EsCommand();
        cmd.timeout = timeout;
        cmd.method = PriWw.method_post;
        cmd.dslType = PriWw.mime_json;
        cmd.dsl = getJson(getDslq());
        cmd.path = String.format("/%s/_delete_by_query", indiceName);

        String tmp = context.execAsBody(cmd);

        return tmp;
    }


    public boolean deleteById(String docId) throws IOException {
        EsCommand cmd = new EsCommand();
        cmd.timeout = timeout;
        cmd.method = PriWw.method_delete;
        cmd.path = String.format("/%s/_doc/%s", indiceName, docId);

        try {
            context.execAsBody(cmd);
            return true;
        } catch (NoExistException e) {
            return true;
        }
    }
}
