package utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import static java.math.MathContext.DECIMAL128;

public final class UniformDistributionUtils {
    public static final MathContext DEFAULT_MATH_CONTEXT = DECIMAL128 ;

    public static BigDecimal getIntervalProbability(DistributionIntervalsStatistic statistic) {
        BigDecimal distance = getUniformDistributionRightBoundary(statistic).subtract(getUniformDistributionLeftBoundary(statistic));
        return statistic.getIntervalsLength().divide(distance, DEFAULT_MATH_CONTEXT);
    }

    public static BigDecimal getUniformDistributionLeftBoundary(DistributionIntervalsStatistic statistic) {
        BigDecimal sampleMean = getSampleMean(statistic);
        BigDecimal sampleDeviation = getSampleDeviation(statistic);
        return sampleMean.subtract(BigDecimal.valueOf(3).sqrt(DEFAULT_MATH_CONTEXT).multiply(sampleDeviation));
    }

    public static BigDecimal getUniformDistributionRightBoundary(DistributionIntervalsStatistic statistic) {
        BigDecimal sampleMean = getSampleMean(statistic);
        BigDecimal sampleDeviation = getSampleDeviation(statistic);
        return sampleMean.add(BigDecimal.valueOf(3).sqrt(DEFAULT_MATH_CONTEXT).multiply(sampleDeviation));
    }

    public static BigDecimal getSampleDeviation(DistributionIntervalsStatistic statistic) {
        BigDecimal sampleDispersion = getSampleDispersion(statistic);
        return sampleDispersion.sqrt(DEFAULT_MATH_CONTEXT);
    }

    public static BigDecimal getSampleDispersion(DistributionIntervalsStatistic statistic) {
        return statistic.getFrequencyToMiddleInPowerTwoMultiplicationSum().divide(statistic.getFrequencySum(), DEFAULT_MATH_CONTEXT).subtract(getSampleMean(statistic).pow(2));
    }

    public static BigDecimal getSampleMean(DistributionIntervalsStatistic statistic) {
        return statistic.getFrequencyToMiddleMultiplicationSum().divide(statistic.getFrequencySum(), DEFAULT_MATH_CONTEXT);
    }

    public static DistributionIntervalsStatistic convertToDistributionIntervals(Collection<BigDecimal> data) {
        int size = data.size();
        BigDecimal first = data.stream().min(Comparator.naturalOrder()).get();
        BigDecimal last = data.stream().max(Comparator.naturalOrder()).get();
        BigDecimal length = getIntervalsLength(first, last , size);

        NavigableSet<DistributionInterval> intervals = new TreeSet<>();
        DistributionInterval currInterval = null;
        for (BigDecimal value : data) {
            if (currInterval == null) {
                intervals.add(currInterval = new DistributionInterval(first, length));
            } else if (currInterval.getRightBorder().compareTo(value) < 0) {
                intervals.add(currInterval = new DistributionInterval(currInterval.getRightBorder().add(BigDecimal.ONE), length));
            }
            currInterval.incrementFrequency();
        }

        return new DistributionIntervalsStatistic(intervals);
    }

    /*
        Get interval length for uniform partition
    */
    static BigDecimal getIntervalsLength(BigDecimal min, BigDecimal max, int count) {
        BigDecimal intervalCount = getIntervalsCount(count);
        return max.subtract(min).divide(intervalCount, DEFAULT_MATH_CONTEXT);
    }
    /*
        Get interval count by Sturges rule
    */
    static BigDecimal getIntervalsCount(int count) {
        return BigDecimal.valueOf(Math.round(1 + 3.322 * Math.log10(count)));
    }
}
