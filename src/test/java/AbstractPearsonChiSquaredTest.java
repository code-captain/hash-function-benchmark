import com.hashfunctions.*;
import com.hashfunctions.murmur.Murmur2;
import com.hashfunctions.murmur.Murmur3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import utils.ChiSquareTestUtils;
import utils.DistributionInterval;
import utils.DistributionIntervalsStatistic;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static utils.UniformDistributionUtils.convertToDistributionIntervals;

public abstract class AbstractPearsonChiSquaredTest {
    protected static List<Integer> stringLengths = Arrays.asList(2, 4, 8, 16, 32, 64, 128, 256);

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void djb2(int size) {
        Collection<BigDecimal> hashCodes = getSortedHashCodes(size, (s -> Math.abs(Djb2.hash(s))));

        DistributionIntervalsStatistic statistic = convertToDistributionIntervals(hashCodes);
        printRelativeFrequencyHistogram(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void sdbm(int size) {
        Collection<BigDecimal> hashCodes = getSortedHashCodes(size, (s -> Math.abs(Sdbm.hash(s))));

        DistributionIntervalsStatistic statistic = convertToDistributionIntervals(hashCodes);
        printRelativeFrequencyHistogram(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void loseLose(int size) {
        Collection<BigDecimal> hashCodes = getSortedHashCodes(size, (s -> Math.abs(LoseLose.hash(s))));

        DistributionIntervalsStatistic statistic = convertToDistributionIntervals(hashCodes);
        printRelativeFrequencyHistogram(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void fnv1(int size) {
        Collection<BigDecimal> hashCodes = getSortedHashCodes(size, (data -> Math.abs(FNV1.hash32(data))));

        DistributionIntervalsStatistic statistic = convertToDistributionIntervals(hashCodes);
        printRelativeFrequencyHistogram(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void fnv1a(int size) {
        Collection<BigDecimal> hashCodes = getSortedHashCodes(size, (data -> Math.abs(FNV1.hash32a(data))));

        DistributionIntervalsStatistic statistic = convertToDistributionIntervals(hashCodes);
        printRelativeFrequencyHistogram(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void crc16(int size) {
        Collection<BigDecimal> hashCodes = getSortedHashCodes(size, (data -> Math.abs(CRC16_Redis.hash(data))));

        DistributionIntervalsStatistic statistic = convertToDistributionIntervals(hashCodes);
        printRelativeFrequencyHistogram(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void murmur2(int size) {
        Collection<BigDecimal> hashCodes = getSortedHashCodes(size, str ->
                Math.abs(Murmur2.hash_32(str, new Random().nextLong()))
        );

        DistributionIntervalsStatistic statistic = convertToDistributionIntervals(hashCodes);
        printRelativeFrequencyHistogram(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void murmur3(int size) {
        Collection<BigDecimal> hashCodes = getSortedHashCodes(size, str ->
                Math.abs(Murmur3.hash_32(str, new Random().nextLong()))
        );

        DistributionIntervalsStatistic statistic = convertToDistributionIntervals(hashCodes);
        printRelativeFrequencyHistogram(statistic);

        Assertions.assertTrue(ChiSquareTestUtils.isChiSquaredUniform(statistic));
    }

    protected void printRelativeFrequencyHistogram(DistributionIntervalsStatistic stat) {
        stat.getIntervals().stream()
                .collect(toMap(
                        DistributionInterval::getMiddleVal,
                        DistributionInterval::getRelativeFrequency,
                        (v1, v2) -> v1, TreeMap::new)
                ).forEach((key, val) ->
                System.out.println(String.format("%12f,%.12f", key, val))
        );
    }

    protected <T extends Number & Comparable<T>> Collection<BigDecimal> getSortedHashCodes(
            int count,
            Function<String, T> hashFunction
    ) {
        return Stream.generate(() -> {
            T hashCode = hashFunction.apply(getRandomString());
            return BigDecimal.valueOf(hashCode.longValue());
        }).limit(count)
                .sorted()
                .collect(Collectors.toList());
    }

    protected abstract String getRandomString();

    protected static int getRandomStringLength() {
        int i = getRandom(stringLengths.size());
        return stringLengths.get(i);
    }

    protected static int getRandom(int bound) {
        return new Random().nextInt(bound);
    }
}
