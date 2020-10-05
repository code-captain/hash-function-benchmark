package utils;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;

public final class UniformDistributionTestUtils {

    public static double getIntervalProbability(DistributionIntervalsStatistic statistic) {
        return statistic.intervalsLength / (getUniformDistributionRightBoundary(statistic) - getUniformDistributionLeftBoundary(statistic));
    }

    public static double getUniformDistributionLeftBoundary(DistributionIntervalsStatistic statistic) {
        return getSampleMean(statistic) - (Math.sqrt(3) * getSampleDeviation(statistic));
    }

    public static double getUniformDistributionRightBoundary(DistributionIntervalsStatistic statistic) {
        return getSampleMean(statistic) + (Math.sqrt(3) * getSampleDeviation(statistic));
    }

    public static double getSampleMean(DistributionIntervalsStatistic statistic) {
        return statistic.getFrequencyMiddleProductSum() / statistic.getFreqeuncySum();
    }

    public static double getSampleDispersion(DistributionIntervalsStatistic statistic) {
        return (statistic.getFrequencyMiddlePowerTwoProductSum() / statistic.getFreqeuncySum()) - Math.pow(getSampleMean(statistic), 2);
    }

    public static double getSampleDeviation(DistributionIntervalsStatistic statistic) {
        double sampleDispersion = getSampleDispersion(statistic);
        return Math.sqrt(sampleDispersion);
    }

    public static DistributionIntervalsStatistic convertToDistributionIntervals(Collection<Integer> set) {
        int size = set.size();
        Integer first = set.stream().min(Comparator.naturalOrder()).get();
        Integer last = set.stream().max(Comparator.naturalOrder()).get();

        int intervalsLength = getIntervalsLength(first, last , size);
        NavigableSet<DistributionInterval> intervals = new TreeSet<>();
        DistributionInterval currInterval = null;
        for (Integer value : set) {
            if (currInterval == null) {
                intervals.add(currInterval = new DistributionInterval(first, intervalsLength));
            } else if (currInterval.rightBorder < value) {
                intervals.add(currInterval = new DistributionInterval(currInterval.rightBorder + 1, intervalsLength));
            }
            currInterval.incrementFreqeuncy();
        }

        return new DistributionIntervalsStatistic(intervals);
    }

    /*
        Get interval length for uniform partition
    */
    static int getIntervalsLength(int min, int max, int count) {
        int intervalCount = getIntervalsCount(count);
        return (max - min) / intervalCount;
    }

    /*
        Get interval count by Sturges rule
    */
    static int getIntervalsCount(int count) {
        return (int) Math.round(1 + 3.322 * Math.log10(count));
    }

    public static class DistributionIntervalsStatistic {
        private final NavigableSet<DistributionInterval> intervals;
        private final long intervalsLength;
        private final long freqeuncySum;
        private final double frequencyMiddleProductSum;
        private final double frequencyMiddlePowerTwoProductSum;

        public DistributionIntervalsStatistic(NavigableSet<DistributionInterval> intervals) {
            this.intervals = intervals;
            this.intervalsLength = intervals.stream()
                    .mapToLong(DistributionInterval::getLength)
                    .findAny().getAsLong();
            this.freqeuncySum = intervals.stream()
                    .mapToLong(interval -> interval.freqeuncy.longValue())
                    .sum();
            this.frequencyMiddleProductSum = intervals.stream()
                    .mapToDouble(interval -> interval.freqeuncy.doubleValue() * interval.middleVal)
                    .sum();
            this.frequencyMiddlePowerTwoProductSum = intervals.stream()
                    .mapToDouble(interval -> interval.freqeuncy.doubleValue() * Math.pow(interval.middleVal, 2))
                    .sum();
        }

        public NavigableSet<DistributionInterval> getIntervals() {
            return intervals;
        }

        public long getFreqeuncySum() {
            return freqeuncySum;
        }

        public double getFrequencyMiddleProductSum() {
            return frequencyMiddleProductSum;
        }

        public double getFrequencyMiddlePowerTwoProductSum() {
            return frequencyMiddlePowerTwoProductSum;
        }

        public long getIntervalsLength() {
            return intervalsLength;
        }

        @Override
        public String toString() {
            return "DistributionIntervalsStatistic{" +
                    "intervals=" + intervals +
                    ", intervalsLength=" + intervalsLength +
                    ", freqeuncySum=" + freqeuncySum +
                    ", frequencyMiddleProductSum=" + frequencyMiddleProductSum +
                    ", frequencyMiddlePowerTwoProductSum=" + frequencyMiddlePowerTwoProductSum +
                    '}';
        }
    }

    public static class DistributionInterval implements Comparable<DistributionInterval> {
        private final long length;
        private final long leftBorder;
        private final long rightBorder;
        private final double middleVal;
        private final LongAdder freqeuncy;

        public DistributionInterval(long leftBorder, long length) {
            this.length = length;
            this.leftBorder = leftBorder;
            this.rightBorder = leftBorder + length;
            this.middleVal = ((double) leftBorder + rightBorder) / 2;
            this.freqeuncy = new LongAdder();
        }

        public long getLeftBorder() {
            return leftBorder;
        }

        public long getRightBorder() {
            return rightBorder;
        }

        public double getMiddleVal() {
            return middleVal;
        }

        public LongAdder getFreqeuncy() {
            return freqeuncy;
        }

        public long getLength() {
            return length;
        }

        void incrementFreqeuncy() {
            freqeuncy.increment();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DistributionInterval interval = (DistributionInterval) o;
            return leftBorder == interval.leftBorder &&
                    rightBorder == interval.rightBorder &&
                    Double.compare(interval.middleVal, middleVal) == 0 &&
                    Objects.equals(freqeuncy, interval.freqeuncy);
        }

        @Override
        public int hashCode() {
            return Objects.hash(leftBorder, rightBorder, middleVal, freqeuncy);
        }

        @Override
        public String toString() {
            return "Interval{" +
                    "leftBorder=" + leftBorder +
                    ", rightBorder=" + rightBorder +
                    ", middleVal=" + middleVal +
                    ", freqeuncy=" + freqeuncy +
                    '}';
        }

        @Override
        public int compareTo(DistributionInterval interval) {
            return Long.compare(interval.leftBorder, this.rightBorder);
        }
    }
}
