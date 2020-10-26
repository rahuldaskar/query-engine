package com.rda.query.engine;

import com.rda.query.engine.exceptions.InvalidColumnException;
import com.rda.query.engine.exceptions.InvalidSortOrderException;
import com.rda.query.engine.exceptions.InvalidSortQueryException;
import com.rda.query.engine.exceptions.InvalidTableException;
import com.rda.query.engine.exceptions.MissingSortColumnException;
import com.rda.query.engine.model.QueryResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/*
 *
 * */
public class SortQueryTests {


    @InjectMocks
    CodingContestMain codingContestMain;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testSelectStarSort() {
        QueryResult result = codingContestMain.handleSortQuery("SELECT * FROM player order by name");
        Assert.assertNotNull(result);
        Assert.assertEquals(6, result.getData().length);
        Assert.assertEquals("daiana", result.getData()[0][1]);
    }

    @Test
    public void testSelectStarSortDesc() {
        QueryResult result = codingContestMain.handleSortQuery("SELECT * FROM player order by weight DESC");
        Assert.assertNotNull(result);
        Assert.assertEquals(6, result.getData().length);
        Assert.assertEquals(30.0, result.getData()[5][3]);
    }

    @Test
    public void testSelectSingleColSort() {
        QueryResult result = codingContestMain.handleSortQuery("SELECT iplteam FROM worth order by networth ASC");
        Assert.assertNotNull(result);
        Assert.assertEquals(6, result.getData().length);
        Assert.assertEquals("kkr", result.getData()[1][0]);
    }

    @Test
    public void testSelectMultiColsSort() {
        QueryResult result = codingContestMain.handleSortQuery("SELECT name,location FROM player order by height");
        Assert.assertNotNull(result);
        Assert.assertEquals(6, result.getData().length);
        Assert.assertEquals("ranchi", result.getData()[0][1]);
    }


    @Test(expected = InvalidTableException.class)
    public void testSortTableNotFound() {
        QueryResult result = codingContestMain.handleSortQuery("SELECT * FROM INVALID_TABLE order by name");
    }

    @Test(expected = InvalidColumnException.class)
    public void testSortInvalidColumn() {
        QueryResult result = codingContestMain.handleSortQuery("SELECT * FROM player order by namex");
    }

    @Test(expected = InvalidSortQueryException.class)
    public void testSortInvalidQuery() {
        QueryResult result = codingContestMain.handleSortQuery("SELECT * FROM worth where networth > 500");
    }

    @Test(expected = InvalidSortOrderException.class)
    public void testSortInvalidOrder() {
        QueryResult result = codingContestMain.handleSortQuery("SELECT * FROM player order by name ASCENDING");
    }

    @Test(expected = MissingSortColumnException.class)
    public void testSortMissingSortCol() {
        QueryResult result = codingContestMain.handleSortQuery("SELECT * FROM player order by ");
    }
}
