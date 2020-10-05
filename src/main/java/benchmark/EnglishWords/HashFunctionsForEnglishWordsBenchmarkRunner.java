package benchmark.EnglishWords;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class HashFunctionsForEnglishWordsBenchmarkRunner {
    public static void main(String[] args)  throws Exception {
        Options opt = new OptionsBuilder()
                .include(HashFunctionsForEnglishWordsBenchmark.class.getSimpleName())
                //.jvmArgs("-Xms1G", "-Xmx8G")
                .addProfiler("org.openjdk.jmh.profile.GCProfiler")
                .build();

        new Runner(opt).run();
    }
}
