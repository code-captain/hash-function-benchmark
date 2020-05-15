package benchmark;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.hashfunctions.CRC16_Princeton;
import com.hashfunctions.CRC16_Redis;
import com.hashfunctions.Crc16;
import com.hashfunctions.FNV1a;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.MurmurHash2;
import org.apache.commons.codec.digest.MurmurHash3;
import org.apache.commons.codec.digest.PureJavaCrc32;
import org.apache.commons.codec.digest.PureJavaCrc32C;
import org.openjdk.jmh.annotations.*;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Random;

@State(Scope.Thread)
@Fork(1)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 2)
@BenchmarkMode(Mode.Throughput)
public class BenchMark {
    private static final Random RND = new SecureRandom();

    @State(Scope.Benchmark)
    public static class ExecutionPlan {

        @Param({"2", "4", "8", "16", "32", "128", "512", "2048", "8192", "16384"})
        public int length;
        public byte[] testArray;
        public String testString;

        @Setup(Level.Invocation)
        public void setup() {
            testArray = new byte[length];
            RND.nextBytes(testArray);
            testString = StringUtils.newString(testArray, Charset.defaultCharset().name());
        }
    }

    @State(Scope.Benchmark)
    public static class HashFunctions {
        public Hasher murmur3_com_google_common_hash;
        public Hasher crc32_com_google_common_hash;
        public Hasher crc32C_com_google_common_hash;
        public PureJavaCrc32 crc32_org_apache_commons_codec_digest;
        public PureJavaCrc32C crc32C_org_apache_commons_codec_digest;

        @Setup(Level.Invocation)
        public void setUp() {
            murmur3_com_google_common_hash = Hashing.murmur3_32().newHasher();
            crc32_com_google_common_hash = Hashing.crc32().newHasher();
            crc32C_com_google_common_hash = Hashing.crc32c().newHasher();
            crc32_org_apache_commons_codec_digest = new PureJavaCrc32();
            crc32C_org_apache_commons_codec_digest = new PureJavaCrc32C();
        }
    }

    @Benchmark
    public void benchMurmur3_com_google_common_hash(ExecutionPlan plan, HashFunctions hashFunctions) {
        String testString = plan.testString;
        hashFunctions.murmur3_com_google_common_hash.putString(testString, Charset.defaultCharset());
        hashFunctions.murmur3_com_google_common_hash.hash();
    }

    @Benchmark
    public void benchCrc32_com_google_common_hash(ExecutionPlan plan, HashFunctions hashFunctions) {
        String testString = plan.testString;
        hashFunctions.crc32_com_google_common_hash.putString(testString, Charset.defaultCharset());
        hashFunctions.crc32_com_google_common_hash.hash();
    }

    @Benchmark
    public void benchCrc32C_com_google_common_hash(ExecutionPlan plan, HashFunctions hashFunctions) {
        String testString = plan.testString;
        hashFunctions.crc32C_com_google_common_hash.putString(testString, Charset.defaultCharset());
        hashFunctions.crc32C_com_google_common_hash.hash();
    }

    @Benchmark
    public void benchMurmur2__org_apache_commons_codec_digest(ExecutionPlan plan) {
        String testString = plan.testString;
        MurmurHash2.hash32(testString);
    }

    @Benchmark
    public void benchMurmur3__org_apache_commons_codec_digest(ExecutionPlan plan) {
        String testString = plan.testString;
        MurmurHash3.hash32(testString);
    }

    @Benchmark
    public void benchCrc32_org_apache_commons_codec_digest(ExecutionPlan plan, HashFunctions hashFunctions) {
        String testString = plan.testString;
        byte[] bytes = testString.getBytes();
        hashFunctions.crc32_org_apache_commons_codec_digest.update(bytes, 0, bytes.length);
        hashFunctions.crc32_org_apache_commons_codec_digest.getValue();
    }

    @Benchmark
    public void benchCrc32C_org_apache_commons_codec_digest(ExecutionPlan plan, HashFunctions hashFunctions) {
        String testString = plan.testString;
        byte[] bytes = testString.getBytes();
        hashFunctions.crc32C_org_apache_commons_codec_digest.update(bytes, 0, bytes.length);
        hashFunctions.crc32C_org_apache_commons_codec_digest.getValue();
    }

    @Benchmark
    public void benchFNV1a_custom_implementation(ExecutionPlan plan) {
        String testString = plan.testString;
        FNV1a.hash32(testString);
    }

    @Benchmark
    public void benchCrc16_Redis_implementation(ExecutionPlan plan) {
        String testString = plan.testString;
        CRC16_Redis.hash(testString);
    }

    @Benchmark
    public void benchCrc16_Princeton_implementation(ExecutionPlan plan) {
        String testString = plan.testString;
        CRC16_Princeton.hash(testString);
    }
}