import org.apache.commons.codec.binary.StringUtils;

import java.nio.charset.Charset;
import java.security.SecureRandom;

public class RandomGeneratedWordsSetPearsonChiSquareTest extends AbstractPearsonChiSquaredTest {

    @Override
    protected String getRandomString() {
        int randomStringLength = getRandomStringLength();
        byte[] testArray = new byte[randomStringLength];
        new SecureRandom().nextBytes(testArray);
        return StringUtils.newString(testArray, Charset.defaultCharset().name());
    }
}
