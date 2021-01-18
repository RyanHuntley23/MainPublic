package bearmaps;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.introcs.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class ArrayHeapMinPQTest {
    @Test
    public void intItemTest() {
        ArrayHeapMinPQ<Integer> aPQ = new ArrayHeapMinPQ<>();
        NaiveMinPQ<Integer> nPQ = new NaiveMinPQ<>();
        assertTrue(aPQ.size() == 0);
        for (int i = 0; i < 10000; i += 1) {
            double priority = StdRandom.uniform(-1000.0, 1000.0);
            aPQ.add(i, priority);
            nPQ.add(i, priority);
        }
        assertTrue(aPQ.contains(0));
        assertTrue(aPQ.contains(100));
        assertTrue(aPQ.contains(5555));
        assertTrue(aPQ.contains(9999));
        assertFalse(aPQ.contains(-1));
        assertFalse(aPQ.contains(10000));
        assertTrue(aPQ.size() == 10000);
        for (int i = 0; i < 1000; i += 1) {
            int intToChange = StdRandom.uniform(10000);
            double newPriority = StdRandom.uniform(-1000.0, 1000.0);
            aPQ.changePriority(intToChange, newPriority);
            nPQ.changePriority(intToChange, newPriority);
        }
        for (int u = 0; u < 10000; u += 1) {
            int x = aPQ.removeSmallest();
            int y = nPQ.removeSmallest();
            assertTrue(x == y);
        }
        assertTrue(aPQ.size() == 0);
    }

    @Test
    public void timeTest() {
        ArrayHeapMinPQ<Integer> aPQ;
        NaiveMinPQ<Integer> nPQ;
        Stopwatch sw;
        double workingTime;
        List<Double> tListAdd = new ArrayList<>();
        List<Double> tListChange = new ArrayList<>();
        List<Double> tListRemove = new ArrayList<>();
        List<Integer> nList = new ArrayList<>();
        List<Integer> opList = new ArrayList<>();
        for (int k = 31250; k <= 2000000; k = k * 2) {
            aPQ = new ArrayHeapMinPQ<>();
            nList.add(k);
            opList.add(1000000);
            sw = new Stopwatch();
            for (int i = 0; i < k; i += 1) {
                double priority = StdRandom.uniform(-1000.0, 1000.0);
                aPQ.add(i, priority);
            }
            workingTime = sw.elapsedTime();
            tListAdd.add(workingTime);
            sw = new Stopwatch();
            for (int i = 0; i < 1000000; i += 1) {
                int intToChange = StdRandom.uniform(k);
                double newPriority = StdRandom.uniform(-1000.0, 1000.0);
                aPQ.changePriority(intToChange, newPriority);
            }
            workingTime = sw.elapsedTime();
            tListChange.add(workingTime);
            sw = new Stopwatch();
            for (int i = 0; i < k; i += 1) {
                aPQ.removeSmallest();
            }
            workingTime = sw.elapsedTime();
            tListRemove.add(workingTime);
        }
        TimeAList.printTimingTable(nList, tListAdd, nList);
        TimeAList.printTimingTable(nList, tListChange, opList);
        TimeAList.printTimingTable(nList, tListRemove, nList);

        tListAdd = new ArrayList<>();
        tListChange = new ArrayList<>();
        tListRemove = new ArrayList<>();
        nList = new ArrayList<>();
        opList = new ArrayList<>();
        for (int k = 250; k <= 1000; k = k * 2) {
            nPQ = new NaiveMinPQ<>();
            nList.add(k);
            opList.add(1000000);
            sw = new Stopwatch();
            for (int i = 0; i < k; i += 1) {
                double priority = StdRandom.uniform(-1000.0, 1000.0);
                nPQ.add(i, priority);
            }
            workingTime = sw.elapsedTime();
            tListAdd.add(workingTime);
            sw = new Stopwatch();
            for (int i = 0; i < 1000000; i += 1) {
                int intToChange = StdRandom.uniform(k);
                double newPriority = StdRandom.uniform(-1000.0, 1000.0);
                nPQ.changePriority(intToChange, newPriority);
            }
            workingTime = sw.elapsedTime();
            tListChange.add(workingTime);
            sw = new Stopwatch();
            for (int i = 0; i < k; i += 1) {
                nPQ.removeSmallest();
            }
            workingTime = sw.elapsedTime();
            tListRemove.add(workingTime);
        }
        TimeAList.printTimingTable(nList, tListAdd, nList);
        TimeAList.printTimingTable(nList, tListChange, opList);
        TimeAList.printTimingTable(nList, tListRemove, nList);
    }
}
