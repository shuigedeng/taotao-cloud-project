package com.taotao.cloud.sys.biz.utils;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;

import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;

/**
 * -基于Redis缓存数据库布隆过滤
 * @author yweijian
 * @date 2022年9月15日
 * @version 2.0.0
 * @see com.google.common.hash.BloomFilter
 * @description
 */
public class BloomFilterRedis<T> implements Serializable {
    
    private static final long serialVersionUID = 738997219690261274L;

    private final int numHashFunctions;

    private final long bitSize;
    
    private final Funnel<? super T> funnel;
    
    private final Strategy strategy;

    public interface Strategy extends Serializable {
        
        public <T> boolean put(String cacheKey, T object, long[] offsets);

        public <T> boolean mightContain(String cacheKey, T object, long[] offsets);
        
        public boolean clean(String cacheKey);
    }

    private BloomFilterRedis(int numHashFunctions, long bitSize, Funnel<? super T> funnel, Strategy strategy) {
        this.numHashFunctions = numHashFunctions;
        checkArgument(bitSize > 0, "bitSize is zero!");
        this.bitSize = bitSize;
        this.funnel = checkNotNull(funnel);
        this.strategy = checkNotNull(strategy);
    }

    /**
     * -创建基于Redis缓存数据库的布隆过滤
     * @param funnel
     * @param expectedInsertions
     * @param fpp
     * @param redisTemplate
     * @param cacheKey
     * @param strategy
     * @return
     */
    public static <T> BloomFilterRedis<T> create(Funnel<? super T> funnel, Strategy strategy, long expectedInsertions, double fpp) {
        checkNotNull(funnel);
        checkArgument(expectedInsertions >= 0, "Expected insertions (%s) must be >= 0", expectedInsertions);
        checkArgument(fpp > 0.0, "False positive probability (%s) must be > 0.0", fpp);
        checkArgument(fpp < 1.0, "False positive probability (%s) must be < 1.0", fpp);
        checkNotNull(strategy);

        if (expectedInsertions == 0) {
            expectedInsertions = 1;
        }
        /*
         * TODO(user): Put a warning in the javadoc about tiny fpp values, since the resulting size
         * is proportional to -log(p), but there is not much of a point after all, e.g.
         * optimalM(1000, 0.0000000000000001) = 76680 which is less than 10kb. Who cares!
         */
        long numBits = optimalNumOfBits(expectedInsertions, fpp);
        int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
        try {
            return new BloomFilterRedis<T>(numHashFunctions, numBits, funnel, strategy);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Could not create BloomFilterRedis of " + numBits + " bits", e);
        }
    }

    public static long optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    public static int optimalNumOfHashFunctions(long n, long m) {
        // (m / n) * log(2), but avoid truncation due to division!
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

    public boolean put(String cacheKey, T object) {
        long[] offsets = getBitOffset(object);
        return strategy.put(cacheKey, object, offsets);
    }

    public boolean mightContain(String cacheKey, T object) {
        long[] offsets = getBitOffset(object);
        return strategy.mightContain(cacheKey, object, offsets);
    }
    
    public boolean clean(String cacheKey) {
        return strategy.clean(cacheKey);
    }
    
    public long[] getBitOffset(T object) {
        long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
        int hash1 = (int) hash64;
        int hash2 = (int) (hash64 >>> 32);

        long[] offsets = new long[numHashFunctions];
        for (int i = 1; i <= numHashFunctions; i++) {
            int combinedHash = hash1 + (i * hash2);
            if (combinedHash < 0) {
                combinedHash = ~combinedHash;
            }
            offsets[i - 1] = combinedHash % bitSize;
        }
        return offsets;
    }

}
