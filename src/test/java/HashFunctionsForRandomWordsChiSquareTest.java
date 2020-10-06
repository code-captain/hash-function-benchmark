import com.hashfunctions.*;
import com.hashfunctions.murmur.Murmur2;
import com.hashfunctions.murmur.Murmur3;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.special.Gamma;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static java.lang.Math.pow;
import static java.util.Arrays.stream;

public class HashFunctionsForRandomWordsChiSquareTest {

/*    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void djb2(int size) {
        List<Integer> values = getRandomValues(size, (Djb2::hash));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void sdbm(int size) {
        List<Integer> values = getRandomValues(size, (Sdbm::hash));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void loseLose(int size) {
        List<Integer> values = getRandomValues(size, (LoseLose::hash));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void fnv1(int size) {
        List<Integer> values = getRandomValues(size, (FNV1::hash32));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void fnv1a(int size) {
        List<Integer> values = getRandomValues(size, (FNV1::hash32a));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void crc16(int size) {
        List<Integer> values = getRandomValues(size, (CRC16_Redis::hash));
        System.out.println("Is internet test" + x2IsUniform(values.stream().mapToDouble(Integer::doubleValue).toArray(), 0.05));

        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void murmur2(int size) {
        List<Integer> values = getRandomValues(size, str ->
                Murmur2.hash_32(str, new Random().nextInt())
        );
        System.out.println("Is internet test" + x2IsUniform(values.stream().mapToDouble(Integer::doubleValue).toArray(), 0.05));

        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    @ParameterizedTest
    @ValueSource(ints = { 16384, 8192, 4096, 2048, 1024 })
    public void murmur3(int size) {
        List<Integer> values = getRandomValues(size, str ->
                Murmur3.hash_32(str, new Random().nextInt())
        );
        System.out.println("Is my test" + isChiSquaredUniform(values));
        System.out.println("Is internet test" + x2IsUniform(values.stream().mapToDouble(Integer::doubleValue).toArray(), 0.05));
        Assertions.assertTrue(isChiSquaredUniform(values));
    }

    private <T extends Number> List<T> getRandomValues(int count, Function<String, T> hashFunction) {
        List<T> values = new ArrayList<>();
        for (int i = 0; i <= count; i++) {
            byte[] testArray = new byte[4];
            new SecureRandom().nextBytes(testArray);
            String randomString = StringUtils.newString(testArray, Charset.defaultCharset().name());
            values.add(hashFunction.apply(randomString));
        }
        return values;
    }

    private static <T extends Number> boolean isChiSquaredUniform(Collection<T> data) {
        return chiSquaredObserved(data) < chiSquaredExpected(data.size(), 0.95);
    }

    private static <T extends Number> double chiSquaredObserved(Collection<T> data) {
        double avg = data.stream().mapToDouble(Number::doubleValue).sum() / data.size();
        double sqs = data.stream().mapToDouble(Number::doubleValue).reduce(0, (a, b) -> a + pow((b - avg), 2));
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
    }*/
}
