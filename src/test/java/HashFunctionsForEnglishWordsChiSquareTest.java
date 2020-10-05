import com.hashfunctions.*;
import com.hashfunctions.murmur.Murmur2;
import com.hashfunctions.murmur.Murmur3;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.util.*;
import java.util.function.Function;

import static java.lang.Math.pow;

public class HashFunctionsForEnglishWordsChiSquareTest {

    private static Map<Integer, List<String>> testWordsByLengthsMap;

    @BeforeAll
    static void init() {
        testWordsByLengthsMap = new HashMap<>();
        String resourceName = "words_english.txt";
        ClassLoader classLoader = HashFunctionsForEnglishWordsChiSquareTest.class.getClassLoader();
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

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void djb2(int size) {
        List<Long> values = getValues(size, (Djb2::hash));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void sdbm(int size) {
        List<Long> values = getValues(size, (Sdbm::hash));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void loseLose(int size) {
        List<Long> values = getValues(size, (LoseLose::hash));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void fnv1(int size) {
        List<Integer> values = getValues(size, (FNV1::hash32));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void fnv1a(int size) {
        List<Integer> values = getValues(size, (FNV1::hash32a));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void crc16(int size) {
        List<Long> values = getValues(size, (CRC16_Redis::hash));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void murmur2(int size) {
        List<Long> values = getValues(size, (Murmur2::hash));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void murmur3(int size) {
        List<Long> values = getValues(size, (Murmur3::hash32));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    private <T extends Number> List<T> getValues(int count, Function<String, T> hashFunction) {
        List<T> values = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            List<String> strings = testWordsByLengthsMap.get(4);
            int item = new Random().nextInt(strings.size());
            String testString = strings.get(item);
            values.add(hashFunction.apply(testString));
        }
        return values;
    }

    private static <T extends Number> boolean isChiSquaredUniform(Collection<T> data) {
        return chiSquaredObserved(data) < chiSquaredExpected(data.size(), 0.95);
    }

    private static <T extends Number> double chiSquaredObserved(Collection<T> data) {
        double avg = data.stream().mapToDouble(Number::doubleValue).sum() / data.size();
        double sqs = data.stream().mapToDouble(Number::doubleValue).reduce(0, (a, b) -> a + pow((b - avg), 2));
        return sqs / avg;
    }

    private static double chiSquaredExpected(double dof, double significance) {
        ChiSquaredDistribution x2 = new ChiSquaredDistribution(dof);
        return x2.inverseCumulativeProbability(significance);
    }
}
