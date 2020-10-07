package benchmark.EnglishWords;

import com.hashfunctions.*;
import com.hashfunctions.murmur.Murmur2;
import com.hashfunctions.murmur.Murmur3;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 1, time = 1)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class HashFunctionsForEnglishWordsBenchmark {

    @State(Scope.Benchmark)
    public static class ExecutionPlan {

        @Param({"2", "4", "8", "16", "32", "64", "128", "256"})
        public int length;
        public String testString;
        public Map<Integer, List<String>> testWordsByLengthsMap;
        {
            testWordsByLengthsMap = new HashMap<>();
            String resourceName = "words_english.txt";
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(resourceName).getFile());
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    testWordsByLengthsMap.putIfAbsent(line.length(), new ArrayList<>());
                    testWordsByLengthsMap.get(line.length()).add(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Setup(Level.Invocation)
        public void setup() {
            List<String> strings = testWordsByLengthsMap.get(length);
            int item = new Random().nextInt(strings.size());
            testString = strings.get(item);
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
        blackhole.consume(Murmur2.hash_32(testString,111111));
    }

    @Benchmark
    public void murmur3(ExecutionPlan plan, Blackhole blackhole) {
        String testString = plan.testString;
        blackhole.consume(Murmur3.hash_32(testString, 111111));
    }
}
