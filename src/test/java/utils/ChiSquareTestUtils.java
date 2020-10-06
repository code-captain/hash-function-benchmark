package utils;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;

import java.math.BigDecimal;
import java.math.BigInteger;

import static utils.UniformDistributionUtils.DEFAULT_MATH_CONTEXT;
import static utils.UniformDistributionUtils.getIntervalProbability;

public class ChiSquareTestUtils {

    public static boolean isChiSquaredUniform(DistributionIntervalsStatistic statistic) {
        BigDecimal chiSquaredExpected = chiSquaredExpected(statistic.getIntervals().size() - 3, 0.05);
        BigDecimal chiSquaredObserved = chiSquaredObserved(statistic);
        return chiSquaredObserved.compareTo(chiSquaredExpected) < 0;
    }

    public static BigDecimal chiSquaredObserved(DistributionIntervalsStatistic statistic) {
        BigDecimal avg = statistic.getFrequencySum().multiply(getIntervalProbability(statistic));
        BigDecimal sqs = statistic.getIntervals().stream()
                .map(DistributionInterval::getFrequency)
                .reduce(new BigDecimal(BigInteger.ZERO), (a, b) -> a.add(b.subtract(avg).pow(2, DEFAULT_MATH_CONTEXT)));
        return sqs.divide(avg, DEFAULT_MATH_CONTEXT);
    }

    public static BigDecimal chiSquaredExpected(double dof, double alpha) {
        ChiSquaredDistribution x2 = new ChiSquaredDistribution(dof);
        return BigDecimal.valueOf(x2.inverseCumulativeProbability(1 - alpha));
    }
}
