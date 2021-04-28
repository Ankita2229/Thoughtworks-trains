package trains;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RailsServiceTest {
    private RailsService buildGraph;

    @Before
    public void initGraph(){
        String input = "AB5,BC4,CD8,DC8,DE6,AD5,CE2,EB3,AE7";
        buildGraph = new RailsService(input);
    }

    @Test
    public void test1(){
        String path1 = "A-B-C";
        String result1 = buildGraph.findDistanceOfPath(path1.split("-"));
        System.out.println("Output #1: "+result1);
        Assert.assertEquals("9", result1);
    }

    @Test
    public void test2(){
        String path2 = "A-D";
        String result2 = buildGraph.findDistanceOfPath(path2.split("-"));
        System.out.println("Output #2: "+result2);
        Assert.assertEquals("5", result2);
    }

    @Test
    public void test3(){
        String path3 = "A-D-C";
        String result3 = buildGraph.findDistanceOfPath(path3.split("-"));
        System.out.println("Output #3: "+result3);
        Assert.assertEquals("13", result3);
    }

    @Test
    public void test4(){
        String path4 = "A-E-B-C-D";
        String result4 = buildGraph.findDistanceOfPath(path4.split("-"));
        System.out.println("Output #4: "+result4);
        Assert.assertEquals("22", result4);
    }

    @Test
    public void test5(){
        String path5 = "A-E-D";
        String result5 = buildGraph.findDistanceOfPath(path5.split("-"));
        System.out.println("Output #5: "+result5);
        Assert.assertEquals("NO SUCH ROUTE", result5);
    }

    @Test
    public void test6(){
        String count = buildGraph.countMaxStops("C", "C", 3);
        System.out.println("Output #6: " +count);
        Assert.assertEquals("2", count);
    }

    @Test
    public void test7(){
        String count1 = buildGraph.countExactStops("A", "C" , 4);
        System.out.println("Output #7: "+count1);
        Assert.assertEquals("3", count1);
    }

    @Test
    public void test8(){
        int distance = buildGraph.findShortestDistance("A", "C");
        System.out.println("Output #8: "+distance);
        Assert.assertEquals(9, distance);
    }

    @Test
    public void test9(){
        int distance1 = buildGraph.findShortestDistance("B", "B");
        System.out.println("Output #9: "+distance1);
        Assert.assertEquals(9, distance1);
    }

    @Test
    public void test10(){
        String count2 = buildGraph.calculateDiffRoutes("C", "C" , 30);
        System.out.println("Output #10: "+count2);
        Assert.assertEquals("7", count2);
    }
}
