package benchmark.RandomWords;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class HashFunctionsForRandomWordsBenchmarkRunner {
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(HashFunctionsForRandomWordsBenchmark.class.getSimpleName())
                //.jvmArgs("-Xms1G", "-Xmx8G")
                .addProfiler("org.openjdk.jmh.profile.GCProfiler")
                //.shouldDoGC(true)
                .build();

        new Runner(opt).run();
    }
}
