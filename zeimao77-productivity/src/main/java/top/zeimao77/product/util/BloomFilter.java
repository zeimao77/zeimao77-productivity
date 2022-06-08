package top.zeimao77.product.util;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Collection;

public class BloomFilter<E> implements Serializable {

    private BitSet bitset;
    private int bitSetSize;
    private double bitsPerElement;
    private int expectedNumberOfFilterElements;
    private int numberOfAddedElements; // number of elements actually added to the Bloom filter
    private int k;

    static final Charset charset = StandardCharsets.UTF_8;

    static final String hashName = "MD5"; // MD5/SHA-256
    static final MessageDigest digestFunction;
    static {
        MessageDigest tmp;
        try {
            tmp = MessageDigest.getInstance(hashName);
        } catch (NoSuchAlgorithmException e) {
            tmp = null;
        }
        digestFunction = tmp;
    }

    /**
     * @param c 每个元素使用的位数.
     * @param n 预期的元素数量. 总长度为c*n
     * @param k 使用散列函数数量.
     */
    public BloomFilter(double c, int n, int k) {
        this.expectedNumberOfFilterElements = n;
        this.k = k;
        this.bitsPerElement = c;
        this.bitSetSize = (int)Math.ceil(c * n);
        numberOfAddedElements = 0;
        this.bitset = new BitSet(bitSetSize);
    }

    /**
     * 创建一个布隆过滤器
     * @param bitSetSize 过滤器总共应该使用多少位
     * @param expectedNumberOElements 预期包含的最大元素数
     */
    public BloomFilter(int bitSetSize, int expectedNumberOElements) {
        this(bitSetSize / (double)expectedNumberOElements,
                expectedNumberOElements,
                (int) Math.round((bitSetSize / (double)expectedNumberOElements) * Math.log(2.0)));
    }

    /**
     * 通过给定期望的报错率以及预期元素数量的方式构造布隆过滤器
     * @param falsePositiveProbability 期望的报错率.
     * @param expectedNumberOfElements 元素的预期数量.
     */
    public BloomFilter(double falsePositiveProbability, int expectedNumberOfElements) {
        this(Math.ceil(-(Math.log(falsePositiveProbability) / Math.log(2))) / Math.log(2), // c = k / ln(2)
                expectedNumberOfElements,
                (int)Math.ceil(-(Math.log(falsePositiveProbability) / Math.log(2)))); // k = ceil(-log_2(false prob.))
    }


    public static int createHash(String val, Charset charset) {
        return createHash(val.getBytes(charset));
    }

    public static int createHash(String val) {
        return createHash(val, charset);
    }

    public static int createHash(byte[] data) {
        return createHashes(data, 1)[0];
    }

    /**
     * 对data进行消息摘要,并将结果拆分成4字节Int类型存储到数组中 直到产生足够数量的int值
     * @param data 字节消息
     * @param hashes 摘要
     * @return 结果
     */
    public static int[] createHashes(byte[] data, int hashes) {
        int[] result = new int[hashes];
        int hashks = 0;
        byte salt = 0;
        while (hashks < hashes) {
            byte[] digest;
            synchronized (digestFunction) {
                digestFunction.update(salt);
                salt++;
                digest = digestFunction.digest(data);
            }
            for (int i = 0; i < digest.length/4 && hashks < hashes; i++) {
                int h = 0;
                for (int j = (i*4); j < (i*4)+4; j++) {
                    h <<= 8;
                    h |= ((int) digest[j]) & 0xFF;
                }
                result[hashks] = h;
                hashks++;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BloomFilter<E> other = (BloomFilter<E>) obj;
        if (this.expectedNumberOfFilterElements != other.expectedNumberOfFilterElements) {
            return false;
        }
        if (this.k != other.k) {
            return false;
        }
        if (this.bitSetSize != other.bitSetSize) {
            return false;
        }
        if (this.bitset != other.bitset && (this.bitset == null || !this.bitset.equals(other.bitset))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.bitset != null ? this.bitset.hashCode() : 0);
        hash = 61 * hash + this.expectedNumberOfFilterElements;
        hash = 61 * hash + this.bitSetSize;
        hash = 61 * hash + this.k;
        return hash;
    }

    /**
     * 根据预期的最大元素数量，计算误报率
     * 如果添加的元素数量小于预期的最大元素数量，则实际的错误率会更小
     * @return 误报率
     */
    public double expectedFalsePositiveProbability() {
        return getFalsePositiveProbability(expectedNumberOfFilterElements);
    }

    /**
     * 在给定的元素数量情况下，计算错误率
     * @return 错误率
     */
    public double getFalsePositiveProbability(double numberOfElements) {
        return Math.pow((1 - Math.exp(-k * (double) numberOfElements
                / (double) bitSetSize)), k);          // (1 - e^(-k * n / m)) ^ k

    }

    /**
     * @return 返回误报的概率.
     */
    public double getFalsePositiveProbability() {
        return getFalsePositiveProbability(numberOfAddedElements);
    }


    /**
     * 返回预期的散列函数数
     */
    public int getK() {
        return k;
    }

    /**
     * 清空布隆过滤器
     */
    public void clear() {
        bitset.clear();
        numberOfAddedElements = 0;
    }

    /**
     * 添加元素到过滤器，对象的toString()函数将作为hash函数的输入
     * @param element 添加一个元素.
     */
    public void add(E element) {
        add(element.toString().getBytes(charset));
    }

    /**
     * 添加元素
     * @param bytes 字节数组元素
     */
    public void add(byte[] bytes) {
        int[] hashes = createHashes(bytes, k);
        for (int hash : hashes)
            bitset.set(Math.abs(hash % bitSetSize), true);
        numberOfAddedElements ++;
    }

    public void addAll(Collection<? extends E> c) {
        for (E element : c)
            add(element);
    }

    /**
     * @return 元素是否已经添加过过滤器
     */
    public boolean contains(E element) {
        return contains(element.toString().getBytes(charset));
    }

    /**
     * @param bytes 元素
     * @return 元素是否已经添加过过滤器
     */
    public boolean contains(byte[] bytes) {
        int[] hashes = createHashes(bytes, k);
        for (int hash : hashes) {
            if (!bitset.get(Math.abs(hash % bitSetSize))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param bit 下标
     * @return 从过滤器中读取一位
     */
    public boolean getBit(int bit) {
        return bitset.get(bit);
    }

    /**
     * 在布隆过滤器中设置一个位
     */
    public void setBit(int bit, boolean value) {
        bitset.set(bit, value);
    }

    /**
     * @return 位图
     */
    public BitSet getBitSet() {
        return bitset;
    }

    /**
     * 布隆过滤器的位数
     */
    public int size() {
        return this.bitSetSize;
    }

    /**
     * @return 布隆过滤器添加的元素数量
     */
    public int count() {
        return this.numberOfAddedElements;
    }

    /**
     * 过滤器预期的元素个数,构造方法输入
     */
    public int getExpectedNumberOfElements() {
        return expectedNumberOfFilterElements;
    }

    /**
     * @return 每个元素使用的位数.
     */
    public double getExpectedBitsPerElement() {
        return this.bitsPerElement;
    }

}
