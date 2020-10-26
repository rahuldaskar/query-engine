package com.rda.query.engine;

import com.rda.query.engine.model.QueryResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Basic test cases for candidates
 * */
public class CodingContestMainTests {

    //millisecond
    long expectedTime = 1;

    @InjectMocks
    CodingContestMain codingContestMain;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAverageWeight() {
        Integer result = codingContestMain.handleAverageQuery("SELECT avg(weight) FROM player where name like 's%'");
        Assert.assertNotNull(result);
        Assert.assertEquals(new Integer(60), result);
    }

    @Test
    @Ignore
    public void testCalculateBMIOfEachPlayer() {
        Map<String, Integer> result = codingContestMain.calculateBMIOfEachPlayer("SELECT name, (weight/(height*height)) FROM player;");
        Assert.assertNotNull(result);
        Map<String, Integer> resultMap = new HashMap<String, Integer>();
        resultMap.put("sachin", (72 / (165) * 2));
        resultMap.put("Rahul", (78 / (169) * 2));
        resultMap.put("Saurav", (30 / (165) * 2));
        resultMap.put("MSD", (78 / (10) * 2));
        resultMap.put("Daiana", (62 / (162) * 2));
        resultMap.put("Sushant Rajput", (80 / (173) * 2));
        Assert.assertEquals(resultMap.size(), result.size());
        for (int i = 0; i <= resultMap.size(); i++) {
            Assert.assertEquals(resultMap.get(i), result.get(i));
        }
    }


    @Test
    public void testSortPlayerByHeight() {
        QueryResult result = codingContestMain.handleSortQuery("SELECT name FROM player order by height");
        Assert.assertNotNull(result);
        List<String> resultMap = new ArrayList<String>();
        resultMap.add("MSD");
        resultMap.add("Daiana");
        resultMap.add("sachin");
        resultMap.add("Saurav");
        resultMap.add("Rahul");
        resultMap.add("Sushant Rajput");
        Assert.assertEquals(resultMap.size(), result.getData().length);
        for (int i = 0; i < resultMap.size(); i++) {
            Assert.assertEquals(resultMap.get(i).toLowerCase(), result.getData()[i][0]);
        }
    }

    @Test
    public void testSelectPlayersWithNetworth() {
        QueryResult result = codingContestMain.selectPlayersWithNetworth("SELECT p.name FROM player p join worth w on p.id = w.id where w.Networth >= 1000 and w.Gender = 'M';");
        Assert.assertNotNull(result);
        List<String> resultMap = new ArrayList<String>();
        resultMap.add("sachin");
        resultMap.add("Sushant Rajput");
        Assert.assertEquals(resultMap.size(), result.getData().length);
        for (int i = 0; i < resultMap.size(); i++) {
            Assert.assertEquals(resultMap.get(i).toLowerCase(), result.getData()[i][0]);
        }
    }

    @Test(expected = Exception.class)
    public void testSelectFromTable() {
        QueryResult result = codingContestMain.selectFromTable("Select * from playerx");
    }

    @Test(expected = Exception.class)
    public void testSelectFromTablewithinvalidcol() {
        QueryResult result = codingContestMain.selectFromTable("Select namx, height from player");
    }
}
