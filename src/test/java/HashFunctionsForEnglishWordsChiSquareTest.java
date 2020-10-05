import com.hashfunctions.*;
import com.hashfunctions.murmur.Murmur2;
import com.hashfunctions.murmur.Murmur3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.ChiSquareTestUtils;
import utils.UniformDistributionTestUtils;

import java.io.*;
import java.util.*;
import java.util.function.Function;

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
        Collection<Integer> values = getValues(size, (Djb2::hash));

        UniformDistributionTestUtils.DistributionIntervalsStatistic statistic = convertToDistributionIntervals(values);
        System.out.println(statistic);
        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void sdbm(int size) {
        Collection<Integer> values = getValues(size, (Sdbm::hash));

        UniformDistributionTestUtils.DistributionIntervalsStatistic statistic = convertToDistributionIntervals(values);
        System.out.println(statistic);
        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void loseLose(int size) {
        Collection<Integer> values = getValues(size, (LoseLose::hash));

        UniformDistributionTestUtils.DistributionIntervalsStatistic statistic = convertToDistributionIntervals(values);
        System.out.println(statistic);
        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void fnv1(int size) {
        Collection<Integer> values = getValues(size, (FNV1::hash32));

        UniformDistributionTestUtils.DistributionIntervalsStatistic statistic = convertToDistributionIntervals(values);
        System.out.println(statistic);
        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void fnv1a(int size) {
        Collection<Integer> values = getValues(size, (FNV1::hash32a));

        UniformDistributionTestUtils.DistributionIntervalsStatistic statistic = convertToDistributionIntervals(values);
        System.out.println(statistic);
        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void crc16(int size) {
        Collection<Integer> values = getValues(size, (CRC16_Redis::hash));

        UniformDistributionTestUtils.DistributionIntervalsStatistic statistic = convertToDistributionIntervals(values);
        System.out.println(statistic);
        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void murmur2(int size) {
        Collection<Integer> values = getValues(size, str ->
                Murmur2.hash_32(str, new Random().nextInt())
        );

        UniformDistributionTestUtils.DistributionIntervalsStatistic statistic = convertToDistributionIntervals(values);
        System.out.println(statistic);
        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void murmur3(int size) {
        Collection<Integer> values = getValues(size, str ->
                Murmur3.hash_32(str, new Random().nextInt())
        );

        UniformDistributionTestUtils.DistributionIntervalsStatistic statistic = convertToDistributionIntervals(values);
        System.out.println(statistic);
        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    private Collection<Integer> getValues(int count, Function<String, Integer> hashFunction) {
        Collection<Integer> values = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            String randomString = getRandomString();
            int hashCode = hashFunction.apply(randomString);
            values.add(hashCode);
        }
        return values;
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
