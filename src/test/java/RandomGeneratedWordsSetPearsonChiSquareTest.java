import org.apache.commons.lang3.RandomStringUtils;

public class RandomGeneratedWordsSetPearsonChiSquareTest extends AbstractPearsonChiSquaredTest {

    @Override
    protected String getRandomString() {
        int length = getRandomStringLength();
        return RandomStringUtils.random(length, true, true);
    }
}
