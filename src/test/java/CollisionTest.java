import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.hashfunctions.Crc16;
import com.hashfunctions.FNV1a;
import org.apache.commons.codec.digest.MurmurHash2;
import org.apache.commons.codec.digest.MurmurHash3;
import org.apache.commons.codec.digest.PureJavaCrc32;
import org.apache.commons.codec.digest.PureJavaCrc32C;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

public class CollisionTest {
    private File file;

    @Before
    public void setUp() throws Exception {
        String resourceName = "words.txt";
        ClassLoader classLoader = getClass().getClassLoader();
        file = new File(classLoader.getResource(resourceName).getFile());
    }

    @Test
    public void testGetCollisionsCount_Murmur3_com_google_common_hash() throws IOException {
        long opsCount = 0;
        long collisionCount = 0;
        Set<Integer> generatedHashes = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Hasher murmur3_32 = Hashing.murmur3_32().newHasher();
                murmur3_32.putString(line, Charset.defaultCharset());
                int hash = murmur3_32.hash().asInt();
                opsCount++;
                if (!generatedHashes.add(hash)) {
                    collisionCount++;
                }
            }
        }
        System.out.println("Murmur3_com_google_common_hash");
        System.out.println(String.format("Operations - %s", opsCount));
        System.out.println(String.format("Collisions - %s", collisionCount));
    }

    @Test
    public void benchCrc32_com_google_common_hash() throws IOException {
        long opsCount = 0;
        long collisionCount = 0;
        Set<Integer> generatedHashes = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Hasher скс32 = Hashing.crc32().newHasher();
                скс32.putString(line, Charset.defaultCharset());
                int hash = скс32.hash().asInt();
                opsCount++;
                if (!generatedHashes.add(hash)) {
                    collisionCount++;
                }
            }
        }
        System.out.println("Crc32_com_google_common_hash");
        System.out.println(String.format("Operations - %s", opsCount));
        System.out.println(String.format("Collisions - %s", collisionCount));
    }

    @Test
    public void testCollisionsCount_Crc32C_com_google_common_hash() throws IOException {
        long opsCount = 0;
        long collisionCount = 0;
        Set<Integer> generatedHashes = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Hasher скс32с = Hashing.crc32c().newHasher();
                скс32с.putString(line, Charset.defaultCharset());
                int hash = скс32с.hash().asInt();
                opsCount++;
                if (!generatedHashes.add(hash)) {
                    collisionCount++;
                }
            }
        }
        System.out.println("Crc32C_com_google_common_hash");
        System.out.println(String.format("Operations - %s", opsCount));
        System.out.println(String.format("Collisions - %s", collisionCount));
    }

    @Test
    public void testCollisionsCount_Murmur2__org_apache_commons_codec_digest() throws IOException {
        long opsCount = 0;
        long collisionCount = 0;
        Set<Integer> generatedHashes = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                int hash = MurmurHash2.hash32(line);
                opsCount++;
                if (!generatedHashes.add(hash)) {
                    collisionCount++;
                }
            }
        }
        System.out.println("Murmur2__org_apache_commons_codec_digest");
        System.out.println(String.format("Operations - %s", opsCount));
        System.out.println(String.format("Collisions - %s", collisionCount));
    }

    @Test
    public void testCollisionsCount_Murmur3__org_apache_commons_codec_digest() throws IOException {
        long opsCount = 0;
        long collisionCount = 0;
        Set<Integer> generatedHashes = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                int hash = MurmurHash3.hash32(line);
                opsCount++;
                if (!generatedHashes.add(hash)) {
                    collisionCount++;
                }
            }
        }
        System.out.println("Murmur3__org_apache_commons_codec_digest");
        System.out.println(String.format("Operations - %s", opsCount));
        System.out.println(String.format("Collisions - %s", collisionCount));
    }

    @Test
    public void testCollisionsCount_Crc32_org_apache_commons_codec_digest() throws IOException {
        long opsCount = 0;
        long collisionCount = 0;
        Set<Long> generatedHashes = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                PureJavaCrc32 pureJavaCrc32C = new PureJavaCrc32();
                byte[] bytes = line.getBytes();
                pureJavaCrc32C.update(bytes, 0, bytes.length);
                long hash = pureJavaCrc32C.getValue();
                opsCount++;
                if (!generatedHashes.add(hash)) {
                    collisionCount++;
                }
            }
        }
        System.out.println("Crc32_org_apache_commons_codec_digest");
        System.out.println(String.format("Operations - %s", opsCount));
        System.out.println(String.format("Collisions - %s", collisionCount));
    }

    @Test
    public void testCollisionsCount_Crc32C_org_apache_commons_codec_digest() throws IOException {
        long opsCount = 0;
        long collisionCount = 0;
        Set<Long> generatedHashes = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                PureJavaCrc32C pureJavaCrc32C = new PureJavaCrc32C();
                byte[] bytes = line.getBytes();
                pureJavaCrc32C.update(bytes, 0, bytes.length);
                long hash = pureJavaCrc32C.getValue();
                opsCount++;
                if (!generatedHashes.add(hash)) {
                    collisionCount++;
                }
            }
        }
        System.out.println("Crc32C_org_apache_commons_codec_digest");
        System.out.println(String.format("Operations - %s", opsCount));
        System.out.println(String.format("Collisions - %s", collisionCount));
    }

    @Test
    public void testCollisionsCount_Crc16_custom_implementation() throws IOException {
        long opsCount = 0;
        long collisionCount = 0;
        Set<Integer> generatedHashes = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                int hash = Crc16.hash(line);
                opsCount++;
                if (!generatedHashes.add(hash)) {
                    collisionCount++;
                }
            }
        }
        System.out.println("Crc16_custom_implementation");
        System.out.println(String.format("Operations - %s", opsCount));
        System.out.println(String.format("Collisions - %s", collisionCount));
    }

    @Test
    public void testCollisionsCount_FNV1a_custom_implementation() throws IOException {
        long opsCount = 0;
        long collisionCount = 0;
        Set<Integer> generatedHashes = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                int hash = FNV1a.hash32(line);
                opsCount++;
                if (!generatedHashes.add(hash)) {
                    collisionCount++;
                }
            }
        }
        System.out.println("FNV1a_custom_implementation");
        System.out.println(String.format("Operations - %s", opsCount));
        System.out.println(String.format("Collisions - %s", collisionCount));
    }
}
