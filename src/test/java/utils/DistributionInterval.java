package utils;

import java.math.BigDecimal;
import java.util.Objects;

import static utils.UniformDistributionUtils.DEFAULT_MATH_CONTEXT;

public class DistributionInterval implements Comparable<DistributionInterval> {
    private final BigDecimal length;
    private final BigDecimal leftBorder;
    private final BigDecimal rightBorder;
    private final BigDecimal middleVal;
    private BigDecimal frequency;
    private BigDecimal relativeFrequency;

    public DistributionInterval(BigDecimal leftBorder, BigDecimal length) {
            this.length = length;
            this.leftBorder = leftBorder;
            this.rightBorder = leftBorder.add(length);
            this.middleVal = leftBorder.add(rightBorder).divide(BigDecimal.valueOf(2), DEFAULT_MATH_CONTEXT);
            this.frequency = BigDecimal.valueOf(0);
    }

    public BigDecimal getLength() {
        return length;
    }

    public BigDecimal getLeftBorder() {
        return leftBorder;
    }

    public BigDecimal getRightBorder() {
        return rightBorder;
    }

    public BigDecimal getMiddleVal() {
        return middleVal;
    }

    public BigDecimal getFrequency() {
        return frequency;
    }

    public void incrementFrequency() {
        this.frequency = frequency.add(BigDecimal.ONE);
    }

    public BigDecimal getRelativeFrequency() {
        return relativeFrequency;
    }

    public void setRelativeFrequency(BigDecimal relativeFrequency) {
        this.relativeFrequency = relativeFrequency;
    }

    @Override
    public int compareTo(DistributionInterval interval) {
        return interval.leftBorder.compareTo(this.rightBorder);
    }

    @Override
    public String toString() {
        return "Interval{" +
            "leftBorder=" + leftBorder +
            ", rightBorder=" + rightBorder +
            ", middleVal=" + middleVal +
            ", frequency=" + frequency +
            ", relativeFrequency=" + relativeFrequency +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistributionInterval that = (DistributionInterval) o;

        return Objects.equals(length, that.length) &&
            Objects.equals(leftBorder, that.leftBorder) &&
            Objects.equals(rightBorder, that.rightBorder) &&
            Objects.equals(middleVal, that.middleVal) &&
            Objects.equals(frequency, that.frequency) &&
            Objects.equals(relativeFrequency, that.relativeFrequency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(length, leftBorder, rightBorder, middleVal, frequency, relativeFrequency);
    }
}