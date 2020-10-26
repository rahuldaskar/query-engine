package com.rda.query.engine;

import com.rda.query.engine.exceptions.InvalidColumnTypeException;
import com.rda.query.engine.exceptions.InvalidTableException;
import com.rda.query.engine.exceptions.OperatorNotSupportedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/*
 *
 * */
public class AvgFunctionTests {


    @InjectMocks
    CodingContestMain codingContestMain;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testAverageNetWeight() {
        Integer result = codingContestMain.handleAverageQuery("SELECT avg(weight) FROM player");
        Assert.assertNotNull(result);
        Assert.assertEquals(new Integer(66), result);
    }

    @Test
    public void testAverageNetWorthForA1() {
        Integer result = codingContestMain.handleAverageQuery("SELECT avg(networth) FROM worth where category = 'A1'");
        Assert.assertNotNull(result);
        Assert.assertEquals(new Integer(674), result);
    }

    @Test
    public void testAverageNetWorthFor11() {
        Integer result = codingContestMain.handleAverageQuery("SELECT avg(networth) FROM worth where id = 11");
        Assert.assertNotNull(result);
        Assert.assertEquals(new Integer(349), result);
    }

    @Test
    public void testAverageWeightNameEndsWith() {
        Integer result = codingContestMain.handleAverageQuery("SELECT avg(weight) FROM player where name like '%put'");
        Assert.assertNotNull(result);
        Assert.assertEquals(new Integer(80), result);
    }

    @Test
    public void testAverageWeightNameContains() {
        Integer result = codingContestMain.handleAverageQuery("SELECT avg(weight) FROM player where name like '%a%'");
        Assert.assertNotNull(result);
        Assert.assertEquals(new Integer(64), result);
    }

    @Test
    public void testAverageNetworth() {
        Integer result = codingContestMain.handleAverageQuery("SELECT avg(networth) FROM worth where IPLTeam like '%k%'");
        Assert.assertNotNull(result);
        Assert.assertEquals(new Integer(542), result);
    }

    @Test(expected = InvalidTableException.class)
    public void testAverageTableNotFound() {
        Integer result = codingContestMain.handleAverageQuery("SELECT avg(networth) FROM INVALID_TABLE where IPLTeam like '%k%'");
    }

    @Test(expected = InvalidColumnTypeException.class)
    public void testAverageNonNumericCol() {
        Integer result = codingContestMain.handleAverageQuery("SELECT avg(iplteam) FROM worth where IPLTeam like '%k%'");
    }

    @Test(expected = OperatorNotSupportedException.class)
    public void testAverageInvalidOp() {
        Integer result = codingContestMain.handleAverageQuery("SELECT avg(networth) FROM worth where networth > 500");
    }

}
