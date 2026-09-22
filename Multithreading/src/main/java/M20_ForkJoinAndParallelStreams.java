import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

// fork/join recursively splits CPU work and idle workers steal queued work from other workers
// RecursiveTask returns a result while RecursiveAction performs work without returning one

public class M20_ForkJoinAndParallelStreams {
    public static void main(String[] args) {
        try (ForkJoinPool pool = new ForkJoinPool(2)) {
            System.out.println(pool.invoke(new Sum(1, 11)));	// 55
            int[] values = {1, 2, 3, 4};
            pool.invoke(new DoubleValues(values, 0, values.length));
            System.out.println(Arrays.toString(values));	// [2, 4, 6, 8]
        }

        // associative reduction avoids sharing a mutable accumulator between stream workers
        System.out.println(IntStream.rangeClosed(1, 10).parallel().sum());	// 55
        System.out.println(List.of(3, 1, 2).parallelStream().sorted().toList());	// [1, 2, 3]
        List.of(1, 2, 3).parallelStream().forEachOrdered(System.out::println);	// 1, then 2, then 3
    }

    static class Sum extends RecursiveTask<Integer> {
        private final int from;
        private final int to;

        Sum(int from, int to) {
            this.from = from;
            this.to = to;
        }

        @Override
        protected Integer compute() {
            if (to - from <= 3) return IntStream.range(from, to).sum();
            int middle = (from + to) / 2;
            Sum left = new Sum(from, middle);
            left.fork();
            int right = new Sum(middle, to).compute();
            return right + left.join();
        }
    }

    static class DoubleValues extends RecursiveAction {
        private final int[] values;
        private final int from;
        private final int to;

        DoubleValues(int[] values, int from, int to) {
            this.values = values;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void compute() {
            if (to - from <= 2) {
                for (int index = from; index < to; index++) values[index] *= 2;
            } else {
                int middle = (from + to) / 2;
                invokeAll(new DoubleValues(values, from, middle), new DoubleValues(values, middle, to));
            }
        }
    }

    // task thresholds avoid splitting work so finely that scheduling costs exceed useful computation
    // parallel streams commonly share the common pool and blocking operations can reduce its throughput
    // forEach permits arbitrary order while forEachOrdered preserves encounter order at a coordination cost
    // avoid shared ArrayList mutation in parallel callbacks and prefer reductions or collectors
    // small inputs, contention and ordering costs can make parallel execution slower; measure realistic workloads
}
