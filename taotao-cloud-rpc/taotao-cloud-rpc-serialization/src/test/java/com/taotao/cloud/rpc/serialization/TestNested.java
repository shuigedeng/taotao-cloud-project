package com.taotao.cloud.rpc.serialization;

import com.taotao.cloud.rpc.serialize.TestPayload;

import java.io.Serializable;
import java.util.Objects;

/**
 * Nested POJO fixture (see {@link TestPayload}).
 */
public class TestNested implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private double score;

    public TestNested() {
    }

    public TestNested(String id, double score) {
        this.id = id;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestNested other = (TestNested) o;
        return Double.compare(other.score, score) == 0 && Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, score);
    }

    @Override
    public String toString() {
        return "TestNested{id=" + id + ", score=" + score + '}';
    }
}
