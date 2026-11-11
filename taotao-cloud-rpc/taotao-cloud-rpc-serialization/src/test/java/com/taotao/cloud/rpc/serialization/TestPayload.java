package com.taotao.cloud.rpc.serialization;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Shared POJO fixture used by every serialization round-trip test.
 * <p>
 * Contract required by the implementations under test:
 * <ul>
 *   <li>{@link Serializable} - needed by {@link JavaSerialization} / {@link Hessian2Serialization}</li>
 *   <li>public no-arg constructor - needed by Kryo and by reflective JSON binders</li>
 *   <li>getters/setters for every field - needed by {@link JsonSerialization}</li>
 *   <li>{@code equals}/{@code hashCode} over every field - so a round-trip can be asserted as
 *       {@code assertEquals(fixture, roundTripped)} and any lost field shows up immediately</li>
 * </ul>
 * Deliberately covers: primitives, boxed types, String (unicode/emoji), nested POJO,
 * {@link List}, {@link Map}, {@link Date} and a large {@code byte[]}.
 */
public class TestPayload implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private int count;
    private long total;
    private boolean flag;
    private Integer boxed;
    private TestNested nested;
    private List<String> tags;
    private Map<String, String> meta;
    private Date createdAt;
    private byte[] blob;

    public TestPayload() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Integer getBoxed() {
        return boxed;
    }

    public void setBoxed(Integer boxed) {
        this.boxed = boxed;
    }

    public TestNested getNested() {
        return nested;
    }

    public void setNested(TestNested nested) {
        this.nested = nested;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestPayload other = (TestPayload) o;
        return count == other.count
                && total == other.total
                && flag == other.flag
                && Objects.equals(name, other.name)
                && Objects.equals(boxed, other.boxed)
                && Objects.equals(nested, other.nested)
                && Objects.equals(tags, other.tags)
                && Objects.equals(meta, other.meta)
                && Objects.equals(createdAt, other.createdAt)
                && java.util.Arrays.equals(blob, other.blob);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(name);
        result = 31 * result + count;
        result = 31 * result + Long.hashCode(total);
        result = 31 * result + (flag ? 1 : 0);
        result = 31 * result + Objects.hashCode(boxed);
        result = 31 * result + Objects.hashCode(nested);
        result = 31 * result + Objects.hashCode(tags);
        result = 31 * result + Objects.hashCode(meta);
        result = 31 * result + Objects.hashCode(createdAt);
        result = 31 * result + java.util.Arrays.hashCode(blob);
        return result;
    }

    @Override
    public String toString() {
        return "TestPayload{name=" + name + ", count=" + count + ", total=" + total
                + ", flag=" + flag + ", boxed=" + boxed + ", nested=" + nested
                + ", tags=" + tags + ", meta=" + meta + ", createdAt=" + createdAt
                + ", blob=" + (blob == null ? "null" : ("byte[" + blob.length + "]")) + '}';
    }
}
