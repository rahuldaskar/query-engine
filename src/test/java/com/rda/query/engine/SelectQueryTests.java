package com.rda.query.engine;

import com.rda.query.engine.exceptions.InvalidColumnException;
import com.rda.query.engine.exceptions.InvalidTableException;
import com.rda.query.engine.exceptions.OperatorNotSupportedException;
import com.rda.query.engine.exceptions.UnsupportedQueryException;
import com.rda.query.engine.model.QueryResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/*
 *
 * */
public class SelectQueryTests {


    @InjectMocks
    CodingContestMain codingContestMain;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testSelectStar() {
        QueryResult result = codingContestMain.selectFromTable("SELECT * FROM player");
        Assert.assertNotNull(result);
        Assert.assertEquals(6, result.getData().length);
    }

    @Test
    public void testSelectStarWithEqCondition() {
        QueryResult result = codingContestMain.selectFromTable("SELECT * FROM player where height = 165");
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.getData().length);
    }

    @Test
    public void testSelectStarWithLikeCondition() {
        QueryResult result = codingContestMain.selectFromTable("SELECT * FROM player where name like '%a%'");
        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.getData().length);
    }

    @Test
    public void testSelectSingleColumn() {
        QueryResult result = codingContestMain.selectFromTable("SELECT category FROM worth");
        Assert.assertNotNull(result);
        Assert.assertEquals(6, result.getData().length);
    }

    @Test
    public void testSelectSingleColumnWithEq() {
        QueryResult result = codingContestMain.selectFromTable("SELECT category FROM worth where gender = 'f'");
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.getData().length);
    }

    @Test
    public void testSelectSingleColumnWithLike() {
        QueryResult result = codingContestMain.selectFromTable("SELECT category FROM worth where iplteam like '%k%'");
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.getData().length);
    }

    @Test
    public void testSelectMultiColumnsWithEq() {
        QueryResult result = codingContestMain.selectFromTable("SELECT iplteam,category FROM worth where gender = 'f'");
        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.getData().length);
    }

    @Test
    public void testSelectMultiolumnsWithLike() {
        QueryResult result = codingContestMain.selectFromTable("SELECT iplteam, category  FROM worth where iplteam like '%k%'");
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.getData().length);
    }

    @Test(expected = InvalidTableException.class)
    public void testSelectTableNotFound() {
        QueryResult result = codingContestMain.selectFromTable("SELECT * FROM INVALID_TABLE where IPLTeam like '%k%'");
    }

    @Test(expected = InvalidColumnException.class)
    public void testSelectInvalidCol() {
        QueryResult result = codingContestMain.selectFromTable("SELECT category,networth FROM worth where invalid like '%k%'");
    }

    @Test(expected = OperatorNotSupportedException.class)
    public void testSelectInvalidOp() {
        QueryResult result = codingContestMain.selectFromTable("SELECT * FROM worth where networth > 500");
    }

    @Test(expected = UnsupportedQueryException.class)
    public void testSelectUnsupportedQuery() {
        QueryResult result = codingContestMain.selectFromTable("SELECT * FROM worth where networth = 500 AND iplteam like '%k%'");
    }

}
