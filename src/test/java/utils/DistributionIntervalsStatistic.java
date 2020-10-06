package utils;

import java.math.BigDecimal;
import java.util.NavigableSet;

import static utils.UniformDistributionUtils.DEFAULT_MATH_CONTEXT;

public class DistributionIntervalsStatistic {
    private final BigDecimal intervalsLength;
    private final BigDecimal frequencySum;
    private final BigDecimal frequencyToMiddleMultiplicationSum;
    private final BigDecimal frequencyToMiddleInPowerTwoMultiplicationSum;
    private final NavigableSet<DistributionInterval> intervals;

    public DistributionIntervalsStatistic(NavigableSet<DistributionInterval> intervals) {
        this.intervals = intervals;
        this.intervalsLength = intervals.stream()
                .map(DistributionInterval::getLength)
                .findAny()
                .get();
        this.frequencySum = intervals.stream()
                .map(DistributionInterval::getFrequency)
                .reduce(BigDecimal::add)
                .get();
        this.frequencyToMiddleMultiplicationSum = intervals.stream()
                .map(interval -> interval.getFrequency().multiply(interval.getMiddleVal()))
                .reduce(BigDecimal::add)
                .get();
        this.frequencyToMiddleInPowerTwoMultiplicationSum = intervals.stream()
                .map(interval -> interval.getFrequency().multiply(interval.getMiddleVal().pow(2)))
                .reduce(BigDecimal::add)
                .get();

        this.intervals.forEach(interval ->
                interval.setRelativeFrequency(interval.getFrequency().divide(this.frequencySum, DEFAULT_MATH_CONTEXT)));
    }

    public NavigableSet<DistributionInterval> getIntervals() {
        return intervals;
    }

    public BigDecimal getFrequencySum() {
        return frequencySum;
    }

    public BigDecimal getFrequencyToMiddleMultiplicationSum() {
        return frequencyToMiddleMultiplicationSum;
    }

    public BigDecimal getFrequencyToMiddleInPowerTwoMultiplicationSum() {
        return frequencyToMiddleInPowerTwoMultiplicationSum;
    }

    public BigDecimal getIntervalsLength() {
        return intervalsLength;
    }

    @Override
    public String toString() {
        return "DistributionIntervalsStatistic{" +
                "intervals=" + intervals +
                ", intervalsLength=" + intervalsLength +
                ", freqeuncySum=" + frequencySum +
                ", frequencyToMiddleMultiplicationSum=" + frequencyToMiddleMultiplicationSum +
                ", frequencyToMiddleInPowerTwoMultiplicationSum=" + frequencyToMiddleInPowerTwoMultiplicationSum +
                '}';
    }
}