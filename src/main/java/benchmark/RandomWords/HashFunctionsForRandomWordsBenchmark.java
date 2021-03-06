package benchmark.RandomWords;

import com.hashfunctions.*;
import com.hashfunctions.murmur.Murmur2;
import com.hashfunctions.murmur.Murmur3;
import com.hashfunctions.murmur.MurmurConstants;
import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@Warmup(iterations = 25, time = 1)
@Measurement(iterations = 100, time = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class HashFunctionsForRandomWordsBenchmark {
    private static final Random RND = new SecureRandom();

    @State(Scope.Benchmark)
    public static class ExecutionPlan {

        @Param({"2", "4", "8", "16", "32", "64", "128", "256"})
        public int length;
        public String testString;

        @Setup(Level.Invocation)
        public void setup() {
            testString = RandomStringUtils.random(length, true, true);
        }
    }

    @Benchmark
    public void djb2(ExecutionPlan plan, Blackhole blackhole) {
        String testString = plan.testString;
        blackhole.consume(Djb2.hash(testString));
    }

    @Benchmark
    public void sdbm(ExecutionPlan plan, Blackhole blackhole) {
        String testString = plan.testString;
        blackhole.consume(Sdbm.hash(testString));
    }


    @Benchmark
    public void loseLose(ExecutionPlan plan, Blackhole blackhole) {
        String testString = plan.testString;
        blackhole.consume(LoseLose.hash(testString));
    }

    @Benchmark
    public void fnv1(ExecutionPlan plan, Blackhole blackhole) {
        String testString = plan.testString;
        blackhole.consume(FNV1.hash32(testString));
    }

    @Benchmark
    public void fnv1a(ExecutionPlan plan, Blackhole blackhole) {
        String testString = plan.testString;
        blackhole.consume(FNV1.hash32a(testString));
    }

    @Benchmark
    public void crc16(ExecutionPlan plan, Blackhole blackhole) {
        String testString = plan.testString;
        blackhole.consume(CRC16_Redis.hash(testString));
    }

    @Benchmark
    public void murmur2(ExecutionPlan plan, Blackhole blackhole) {
        String testString = plan.testString;
        blackhole.consume(Murmur2.hash_32(testString, MurmurConstants.SEED));
    }

    @Benchmark
    public void murmur3(ExecutionPlan plan, Blackhole blackhole) {
        String testString = plan.testString;
        blackhole.consume(Murmur3.hash_32(testString, MurmurConstants.SEED));
    }
}