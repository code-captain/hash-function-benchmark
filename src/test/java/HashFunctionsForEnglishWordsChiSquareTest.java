import com.hashfunctions.*;
import com.hashfunctions.murmur.Murmur2;
import com.hashfunctions.murmur.Murmur3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.ChiSquareTestUtils;
import utils.UniformDistributionTestUtils;
import utils.UniformDistributionTestUtilsLong;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static utils.UniformDistributionTestUtils.convertToDistributionIntervals;

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
        Collection<Long> values = getValues(size, (s -> Math.abs(Djb2.hash(s))));

        UniformDistributionTestUtilsLong.DistributionIntervalsStatistic statistic = UniformDistributionTestUtilsLong.convertToDistributionIntervals(values);
        printRelativeFrequencyHistogramStat(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void sdbm(int size) {
        Collection<Long> values = getValues(size, (s -> Math.abs(Sdbm.hash(s))));

        UniformDistributionTestUtilsLong.DistributionIntervalsStatistic statistic = UniformDistributionTestUtilsLong.convertToDistributionIntervals(values);
        printRelativeFrequencyHistogramStat(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void loseLose(int size) {
        Collection<Long> values = getValues(size, (s -> Math.abs(LoseLose.hash(s))));

        UniformDistributionTestUtilsLong.DistributionIntervalsStatistic statistic = UniformDistributionTestUtilsLong.convertToDistributionIntervals(values);
        printRelativeFrequencyHistogramStat(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void fnv1(int size) {
        Collection<Integer> values = getIntegerValues(size, (data -> Math.abs(FNV1.hash32(data))));

        UniformDistributionTestUtils.DistributionIntervalsStatistic statistic = convertToDistributionIntervals(values);
        printRelativeFrequencyHistogramStat(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void fnv1a(int size) {
        Collection<Integer> values = getIntegerValues(size, (data -> Math.abs(FNV1.hash32a(data))));

        UniformDistributionTestUtils.DistributionIntervalsStatistic statistic = convertToDistributionIntervals(values);
        printRelativeFrequencyHistogramStat(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void crc16(int size) {
        Collection<Long> values = getValues(size, (data -> Math.abs(CRC16_Redis.hash(data))));

        UniformDistributionTestUtilsLong.DistributionIntervalsStatistic statistic = UniformDistributionTestUtilsLong.convertToDistributionIntervals(values);
        printRelativeFrequencyHistogramStat(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void murmur2(int size) {
        Collection<Long> values = getValues(size, str ->
                Math.abs(Murmur2.hash_32(str, new Random().nextLong()))
        );

        UniformDistributionTestUtilsLong.DistributionIntervalsStatistic statistic = UniformDistributionTestUtilsLong.convertToDistributionIntervals(values);
        printRelativeFrequencyHistogramStat(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void murmur3(int size) {
        Collection<Long> values = getValues(size, str ->
                Math.abs(Murmur3.hash_32(str, new Random().nextLong()))
        );

        UniformDistributionTestUtilsLong.DistributionIntervalsStatistic statistic = UniformDistributionTestUtilsLong.convertToDistributionIntervals(values);
        printRelativeFrequencyHistogramStat(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    private void printRelativeFrequencyHistogramStat(UniformDistributionTestUtilsLong.DistributionIntervalsStatistic stat) {
        Map<Double, Double> histogram = stat.getIntervals().stream()
                .collect(toMap(
                        UniformDistributionTestUtilsLong.DistributionInterval::getMiddleVal,
                        UniformDistributionTestUtilsLong.DistributionInterval::getRelativeFrequency,
                        (v1, v2) -> v1, TreeMap::new)
                );
        for (Map.Entry<Double, Double> h : histogram.entrySet()) {
            System.out.println(String.format("%12f,%.12f", h.getKey(), h.getValue()));
        }
    }

    private void printRelativeFrequencyHistogramStat(UniformDistributionTestUtils.DistributionIntervalsStatistic stat) {
        Map<Double, Double> histogram = stat.getIntervals().stream()
                .collect(toMap(
                        UniformDistributionTestUtils.DistributionInterval::getMiddleVal,
                        UniformDistributionTestUtils.DistributionInterval::getRelativeFrequency,
                        (v1, v2) -> v1, TreeMap::new)
                );
        for (Map.Entry<Double, Double> h : histogram.entrySet()) {
            System.out.println(String.format("%12f,%.12f", h.getKey(), h.getValue()));
        }
    }

    private Collection<Integer> getIntegerValues(int count, Function<String, Integer> hashFunction) {
        Collection<Integer> values = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            String randomString = getRandomString();
            int hashCode = hashFunction.apply(randomString);
            values.add(hashCode);
        }
        return values.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    private Collection<Long> getValues(int count, Function<String, Long> hashFunction) {
        Collection<Long> values = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            String randomString = getRandomString();
            long hashCode = hashFunction.apply(randomString);
            values.add(hashCode);
        }
        return values.stream()
                .sorted()
                .collect(Collectors.toList());
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
}
