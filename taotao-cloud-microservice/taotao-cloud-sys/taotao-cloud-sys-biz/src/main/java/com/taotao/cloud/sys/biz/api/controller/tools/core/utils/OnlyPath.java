package com.taotao.cloud.sys.biz.api.controller.tools.core.utils;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 路径处理工具
 */
public class OnlyPath{
    private Path path;

    private static final Path _ROOT = Paths.get("/");

    /**
     * 根路径, 固定为 /
     */
    public static final OnlyPath ROOT = new OnlyPath(_ROOT);

    public OnlyPath(Path path){
        this.path = path;
    }

    public OnlyPath(String path){
        this.path = Paths.get(path);
    }

    public OnlyPath(File file){
        this.path = file.toPath();
    }

    /**
     * 获取父级路径
     * @param onlyPath
     * @return
     */
    public OnlyPath getParent(){
        final Path parent = this.path.getParent();
        if (parent == null){
            return null;
        }
        return new OnlyPath(parent);
    }

    public OnlyPath getRoot() {
        return ROOT;
    }

    public boolean isRoot(){
        return this.equals(ROOT);
    }

    public String getFileName() {
        if (path.equals(_ROOT)){
            return "/";
        }
        return path.getFileName().toString();
    }

    public OnlyPath lastPath(){
        return new OnlyPath(path.getFileName());
    }

    public int getNameCount() {
        return path.getNameCount();
    }

    public OnlyPath getName(int index) {
        return new OnlyPath(path.getName(index));
    }

    public OnlyPath subpath(int beginIndex, int endIndex) {
        return new OnlyPath(path.subpath(beginIndex,endIndex));
    }

    public boolean startsWith(OnlyPath other) {
        return this.path.startsWith(other.path);
    }

    public boolean startsWith(String other) {
        return startsWith(new OnlyPath(other));
    }

    public boolean endsWith(OnlyPath other) {
        return this.path.endsWith(other.path);
    }

    public boolean endsWith(String other) {
        return endsWith(new OnlyPath(other));
    }

    public OnlyPath normalize() {
        return new OnlyPath(this.path.normalize());
    }

    public OnlyPath resolve(OnlyPath other) {
        final Path resolve = this.path.resolve(other.path);
        return new OnlyPath(resolve);
    }

    public OnlyPath resolve(String other) {
        return resolve(new OnlyPath(other));
    }

    public OnlyPath resolveSibling(OnlyPath other) {
        final Path path = this.path.resolveSibling(other.path);
        return new OnlyPath(path);
    }

    public OnlyPath resolveSibling(String other) {
        return resolveSibling(new OnlyPath(other));
    }

    public OnlyPath relativize(OnlyPath other) {
        return new OnlyPath(this.path.relativize(other.path));
    }

    public OnlyPath relativize(String otherPath){
        return relativize(new OnlyPath(otherPath));
    }

    public URI toUri() {
        return path.toUri();
    }

    public OnlyPath toAbsolutePath() {
        return new OnlyPath(_ROOT.resolve(this.path));
    }

    /**
     * 转成文件
     * @return
     */
    public File toFile(){
        if (isAbsolutePath()){
            return path.toFile();
        }
        return path.toFile();
    }

    /**
     * 是否为绝对路径
     * @return
     */
    public boolean isAbsolutePath(){
        if (path.isAbsolute()){
            return true;
        }
        return this.startsWith(ROOT);
    }

    /**
     * 是否为文件路径, 如果存在后缀, 则为文件路径
     * @return
     */
    public boolean isFile(){
        if (isAbsolutePath()){
            return path.toFile().isFile();
        }
        return StringUtils.isNotBlank(FilenameUtils.getExtension(this.getFileName()));
    }

    public Iterator<OnlyPath> iterator() {
        return new Iterator<OnlyPath>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return this.i < OnlyPath.this.getNameCount();
            }

            @Override
            public OnlyPath next() {
                if (this.i < OnlyPath.this.getNameCount()) {
                    OnlyPath var1 = OnlyPath.this.getName(this.i);
                    ++this.i;
                    return var1;
                } else {
                    throw new NoSuchElementException();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * 解析为文件, 需提供一个基础路径
     * @param baseDir
     * @return
     */
    public File resolveFile(File baseDir){
        if (this.isAbsolutePath()){
            // 绝对路径时, 和基础路径无关
            return new File(this.toString());
        }
        return new File(baseDir,this.toString());
    }

    public int compareTo(OnlyPath other) {
        return this.path.compareTo(other.path);
    }

    @Override
    public String toString() {
        final String toString = path.normalize().toString();
        return StringUtils.replaceChars(toString,'\\','/');
    }

    @Override
    public boolean equals(Object other){
        if (other == null ){
            return false;
        }
        if (!(other instanceof OnlyPath)){
            return false;
        }
        OnlyPath onlyPathOther = (OnlyPath) other;
        return path.equals(onlyPathOther.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
