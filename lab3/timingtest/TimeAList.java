package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        AList<Integer> N = new AList<>();
        N.addLast(1000);
        N.addLast(2000);
        N.addLast(4000);
        N.addLast(8000);
        N.addLast(16000);
        N.addLast(32000);
        N.addLast(64000);
        N.addLast(128000);
        //N.addLast(100000000);
        AList<Double> time = new AList<>();
        AList<Integer> opCount = new AList<>();

        for (int i = 0; i < N.size(); i += 1){
            int n = N.get(i);
            opCount.addLast(n);
            AList<Integer> test = new AList<>();
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < n; j += 1){
                test.addLast(j);
            }
            double timeInSeconds = sw.elapsedTime();
            time.addLast(timeInSeconds);
        }
        printTimingTable(N, time, opCount);
    }
}
