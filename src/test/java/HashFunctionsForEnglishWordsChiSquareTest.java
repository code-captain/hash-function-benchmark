import com.hashfunctions.*;
import com.hashfunctions.murmur.Murmur2;
import com.hashfunctions.murmur.Murmur3;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.special.Gamma;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.util.Arrays.stream;

public class HashFunctionsForEnglishWordsChiSquareTest {

    private static List<Integer> wordsLengths = Arrays.asList(2, 4, 8, 16, 32, 64, 128, 256);
    private static Map<Integer, List<String>> wordsByLengthsMap;

    @BeforeAll
    static void init() {
        wordsByLengthsMap = new HashMap<>();
        String resourceName = "words_english.txt";
        ClassLoader classLoader = HashFunctionsForEnglishWordsChiSquareTest.class.getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                wordsByLengthsMap.putIfAbsent(line.length(), new ArrayList<>());
                wordsByLengthsMap.get(line.length()).add(line);
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
        Collection<Long> values = getValuesFrequencies(size, (Djb2::hash));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void sdbm(int size) {
        Collection<Long> values = getValuesFrequencies(size, (Sdbm::hash));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void loseLose(int size) {
        Collection<Long> values = getValuesFrequencies(size, (LoseLose::hash));
        Assertions.assertTrue(isChiSquaredUniform(values));

    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void fnv1(int size) {
        Collection<Long> values = getValuesFrequencies(size, (FNV1::hash32));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void fnv1a(int size) {
        Collection<Long> values = getValuesFrequencies(size, (FNV1::hash32a));
        System.out.println(values);
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void crc16(int size) {
        Collection<Long> values = getValuesFrequencies(size, (CRC16_Redis::hash));
        System.out.println(values);
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void murmur2(int size) {
        Collection<Long> values = getValuesFrequencies(size, str ->
                Murmur2.hash_32(str, new Random().nextLong())
        );
        System.out.println(values);
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void murmur3(int size) {
        Collection<Long> values = getValuesFrequencies(size, str ->
                Murmur3.hash_32(str, new Random().nextLong())
        );
        double v = calculateBhattacharyyaCoefficientByUniformDistribution(values);
        System.out.println(v);
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    private <T extends Number> Collection<Long> getValuesFrequencies(int count, Function<String, T> hashFunction) {
        List<Long> values = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            String randomString = getRandomString();
            long hashCode = hashFunction.apply(randomString).longValue() % count;
            values.add(hashCode);
        }
        HashMap<Long, Long> frequenciesMap = values.stream()
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        HashMap::new,
                        Collectors.counting()
                ));

        return frequenciesMap.values();
    }

    private static String getRandomString() {
        int randomWordsSetLength = getRandomWordsSetLength();
        List<String> words = wordsByLengthsMap.get(randomWordsSetLength);
        return words.get(getRandom(wordsLengths.size()));
    }

    private static int getRandomWordsSetLength() {
        int i = getRandom(wordsLengths.size());
        return wordsLengths.get(i);
    }

    private static int getRandom(int bound) {
        return new Random().nextInt(bound);
    }

    private static <T extends Number> boolean isChiSquaredUniform(Collection<Long> frequencies) {
        return chiSquaredObserved(frequencies) < chiSquaredExpected(frequencies.size(), 0.95);
    }

    private static <T extends Number> double chiSquaredObserved(Collection<Long> frequencies) {
        double avg = frequencies.stream().mapToDouble(Number::doubleValue).sum() / frequencies.size();
        double sqs = frequencies.stream().mapToDouble(Number::doubleValue).reduce(0, (a, b) -> a + pow((b - avg), 2));
        return sqs / avg;
    }

    private static double chiSquaredExpected(double dof, double significance) {
        ChiSquaredDistribution x2 = new ChiSquaredDistribution(dof);
        return x2.inverseCumulativeProbability(significance);
    }

    static double x2Dist(double[] data) {
        double avg = stream(data).sum() / data.length;
        double sqs = stream(data).reduce(0, (a, b) -> a + pow((b - avg), 2));
        return sqs / avg;
    }

    static double x2Prob(double dof, double distance) {
        return Gamma.regularizedGammaQ(dof / 2, distance / 2);
    }

    static boolean x2IsUniform(double[] data, double significance) {
        return x2Prob(data.length - 1.0, x2Dist(data)) > significance;
    }

    static double calculateBhattacharyyaCoefficientByUniformDistribution(Collection<Long> frequencies) {
        double avg = frequencies.stream().mapToDouble(Number::doubleValue).sum() / frequencies.size();
        double sum = 0;
        for (Long freqeunty : frequencies) {
            sum += sqrt (freqeunty * avg);
        }
        double sqs = frequencies.stream().mapToDouble(Number::doubleValue).reduce(0, (a, b) -> a + sqrt(b * avg));
        double huiv = Math.log1p(sum);
        System.out.println(huiv);
        return sqs;
    }
}
