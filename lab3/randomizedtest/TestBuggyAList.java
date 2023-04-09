package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove(){
        AListNoResizing<Integer> anr = new AListNoResizing<>();
        BuggyAList<Integer> ba = new BuggyAList<>();
        anr.addLast(4);
        ba.addLast(4);
        anr.addLast(5);
        ba.addLast(5);
        anr.addLast(6);
        ba.addLast(6);

        Assert.assertEquals(anr.removeLast(), ba.removeLast());
        Assert.assertEquals(anr.removeLast(), ba.removeLast());
        Assert.assertEquals(anr.removeLast(), ba.removeLast());
    }


    @Test
    public void randomizedTest(){
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
                //System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int bSize = B.size();
                //System.out.println("size: " + size);
                Assert.assertEquals(size, bSize);
            } else if (operationNumber == 2) {
                if (L.size() > 0){
                    int x = L.getLast();
                    int y = B.getLast();
                    //System.out.println("getLast:" + x);
                    Assert.assertEquals(x, y);
                }
            } else if (operationNumber == 3) {
                if (L.size() > 0){
                    int x = L.removeLast();
                    int y = B.removeLast();
                    //System.out.println("removeLast(" + x + ")");
                    Assert.assertEquals(x, y);
                }
            }
        }
    }
}
