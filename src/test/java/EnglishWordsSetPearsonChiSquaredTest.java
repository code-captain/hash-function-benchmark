import org.junit.jupiter.api.BeforeAll;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnglishWordsSetPearsonChiSquaredTest extends AbstractPearsonChiSquaredTest {
    private static Map<Integer, List<String>> stringsByLengthsMap;

    @BeforeAll
    static void init() {
        stringsByLengthsMap = new HashMap<>();
        String resourceName = "words_english.txt";
        ClassLoader classLoader = EnglishWordsSetPearsonChiSquaredTest.class.getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                stringsByLengthsMap.putIfAbsent(line.length(), new ArrayList<>());
                stringsByLengthsMap.get(line.length()).add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getRandomString() {
        int randomWordsSetLength = getRandomStringLength();
        List<String> words = stringsByLengthsMap.get(randomWordsSetLength);
        return words.get(getRandom(stringLengths.size()));
    }
}
