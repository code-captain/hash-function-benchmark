package utils;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import static java.lang.Math.pow;
import static utils.UniformDistributionTestUtils.getIntervalProbability;

public class ChiSquareTestUtils {

    public static <T extends Number> boolean isChiSquaredUniform(UniformDistributionTestUtilsLong.DistributionIntervalsStatistic statistic) {
        return chiSquaredObserved(statistic) < chiSquaredExpected(statistic.getIntervals().size() - 3, 0.05);
    }

    public static <T extends Number> double chiSquaredObserved(UniformDistributionTestUtilsLong.DistributionIntervalsStatistic statistic) {
        double avg = statistic.getFreqeuncySum() * UniformDistributionTestUtilsLong.getIntervalProbability(statistic);
        double sqs = statistic.getIntervals().stream()
                .mapToDouble(interval -> interval.getFrequency().doubleValue())
                .reduce(0, (a, b) -> a + pow(b - avg, 2));
        return sqs / avg;
    }

    public static <T extends Number> boolean isChiSquaredUniform(UniformDistributionTestUtils.DistributionIntervalsStatistic statistic) {
        return chiSquaredObserved(statistic) < chiSquaredExpected(statistic.getIntervals().size() - 3, 0.05);
    }

    public static <T extends Number> double chiSquaredObserved(UniformDistributionTestUtils.DistributionIntervalsStatistic statistic) {
        double avg = statistic.getFreqeuncySum() * getIntervalProbability(statistic);
        double sqs = statistic.getIntervals().stream()
                .mapToDouble(interval -> interval.getFreqeuncy().doubleValue())
                .reduce(0, (a, b) -> a + pow(b - avg, 2));
        return sqs / avg;
    }

    public static double chiSquaredExpected(double dof, double alpha) {
        ChiSquaredDistribution x2 = new ChiSquaredDistribution(dof);
        return x2.inverseCumulativeProbability(1 - alpha);
    }
}
