//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.taotao.cloud.mq.common.hash;

import com.taotao.boot.common.utils.common.ArgUtils;
import com.taotao.cloud.mq.common.hash.api.IHash;
import com.taotao.cloud.mq.common.hash.api.IHashContext;
import com.taotao.cloud.mq.common.hash.api.IHashResult;
import com.taotao.cloud.mq.common.hash.api.IHashResultHandler;
import com.taotao.cloud.mq.common.hash.core.HashContext;
import com.taotao.cloud.mq.common.hash.core.HashResultHandlers;
import com.taotao.cloud.mq.common.hash.core.Hashes;
import com.xkzhangsan.time.utils.StringUtil;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class HashBs {
    private IHash hash = Hashes.md5();
    private int times = 1;
    private byte[] salt;
    private IHashContext hashContext;
    private Charset charset;

    private HashBs() {
        this.charset = StandardCharsets.UTF_8;
    }

    public static HashBs newInstance() {
        return new HashBs();
    }

    public HashBs hash(IHash hash) {
        ArgUtils.notNull(hash, "hash");
        this.hash = hash;
        return this;
    }

    public HashBs times(int times) {
        this.times = times;
        return this;
    }

    public HashBs salt(byte[] salt) {
        this.salt = salt;
        return this;
    }

    public HashBs charset(Charset charset) {
        ArgUtils.notNull(charset, "charset");
        this.charset = charset;
        return this;
    }

    public synchronized HashBs init() {
        this.hashContext = HashContext.newInstance().salt(this.salt).times(this.times).charset(this.charset);
        return this;
    }

    public <T> T execute(byte[] source, IHashResultHandler<T> handler) {
        if (this.hashContext == null) {
            this.init();
        }

        IHashResult result = this.hash.hash(source, this.hashContext);
        return (T)handler.handle(result);
    }

    public <T> T execute(String source, IHashResultHandler<T> handler) {
        byte[] bytes = null;
        if (StringUtil.isNotEmpty(source)) {
            bytes = source.getBytes(this.charset);
        }

        return (T)this.execute(bytes, handler);
    }

    public String execute(String source) {
        return (String)this.execute(source, HashResultHandlers.hex());
    }
}
