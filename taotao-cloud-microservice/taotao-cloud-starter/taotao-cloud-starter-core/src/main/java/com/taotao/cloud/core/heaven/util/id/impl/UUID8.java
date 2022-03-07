package com.taotao.cloud.core.heaven.util.id.impl;


import com.taotao.cloud.core.heaven.annotation.ThreadSafe;
import com.taotao.cloud.core.heaven.util.guava.Guavas;
import com.taotao.cloud.core.heaven.util.id.Id;
import java.util.List;

/**
 * 在数据量较多的时候，
 *
 * 如果基于 ID 添加索引，字符串越长，则性能越差。
 *
 * （1）uuid 是基于 16 进制的，将 128 位的 bit ，变为 16 进制。
 *  0-F   0123456789ABCDEFG
 *  （2）转换为 62 进制的信息，考虑便于阅读
 *  0-9  52 个字母 共计 62 位
 *
 *  所以 32 位，分成 8 组，每一组 4 位。
 *
 *  16 * 4 = 64 位。
 *
 *  如果不想重复，需要 0xffff 共计 65535 的字符。
 *
 * （3）缺点：会存在重复
 *
 * 重复的概率：
 *
 * 取62模会导致重复率大增 P(A)
 *
 * 短uuid重复概率未知 P(B)
 *
 * uuid重复概率=x
 *
 * P(A|B)=1
 *
 * P(A|B') =y
 *
 * 全概率公式计算 P(A)=P(B)P(A|B)+P(B')P(A|B') =x+y-xy
 *
 * 其中y=((64-62)/64)^8=2^(-40) 看出x远小于y，所以短uuid的重复概率完全取决于y的值
 *
 * （4）建议应用场景
 *
 * 可以用来生成一个 8 位的 token，而不是用来做唯一标识
 *
 */
@ThreadSafe
@Deprecated
public class UUID8 implements Id {

    /**
     * 62 进制符号
     */
    private static final char[] CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    @Override
    public String genId() {
        final List<String> uuidUnits = uuidUnits();
        StringBuilder stringBuilder = new StringBuilder();
        for (String unit : uuidUnits) {
            int x = Integer.parseInt(unit, 16);
            stringBuilder.append(CHARS[x % 62]);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取 uuid 分段后的内容
     * @return 分段后的内容
     */
    private List<String> uuidUnits() {
        final String uuid32 = new UUID32().genId();
        final int size = 8;

        List<String> units = Guavas.newArrayList(size);
        for(int i = 0; i < size; i++) {
            units.add(uuid32.substring(i * 4, i * 4 + 4));
        }
        return units;
    }

}
