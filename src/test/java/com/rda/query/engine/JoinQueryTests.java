package com.rda.query.engine;

import com.rda.query.engine.exceptions.AliasNotInOrderException;
import com.rda.query.engine.exceptions.InvalidColumnException;
import com.rda.query.engine.exceptions.InvalidQueryException;
import com.rda.query.engine.exceptions.InvalidTableException;
import com.rda.query.engine.exceptions.UnsupportedQueryException;
import com.rda.query.engine.model.QueryResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

/*
 *
 * */
public class JoinQueryTests {


    @InjectMocks
    CodingContestMain codingContestMain;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testJoinWithoutCondition() {
        QueryResult result = codingContestMain.selectPlayersWithNetworth("SELECT p.name FROM player p join worth w on p.id = w.id");
        Assert.assertNotNull(result);
        Assert.assertEquals(6, result.getData().length);
        Assert.assertEquals("sachin", result.getData()[0][0]);
    }

    @Test
    public void testSelectPlayersWithNetworth() {
        QueryResult result = codingContestMain.selectPlayersWithNetworth("SELECT w.iplteam FROM player p join worth w on p.id = w.id where w.Networth <= 1000;");
        Assert.assertNotNull(result);
        List<String> resultMap = new ArrayList<String>();
        resultMap.add("mi");
        resultMap.add("rcb");
        resultMap.add("kkr");
        resultMap.add("ck");
        resultMap.add("");
        Assert.assertEquals(resultMap.size(), result.getData().length);
        for (int i = 0; i < resultMap.size(); i++) {
            Assert.assertEquals(resultMap.get(i).toLowerCase(), result.getData()[i][0]);
        }
    }

    @Test(expected = InvalidTableException.class)
    public void testJoinTableNotFound() {
        QueryResult result = codingContestMain.selectPlayersWithNetworth("SELECT p.name FROM invalid_table p join worth w on p.id = w.id");
    }

    @Test(expected = InvalidColumnException.class)
    public void testJoinInvalidColumn() {
        QueryResult result = codingContestMain.selectPlayersWithNetworth("SELECT p.name FROM player p join worth w on p.pid = w.id");
    }

    @Test(expected = InvalidQueryException.class)
    public void testJoinMissingJoin() {
        QueryResult result = codingContestMain.selectPlayersWithNetworth("SELECT p.name FROM player p worth w on p.pid = w.id");
    }

    @Test(expected = InvalidQueryException.class)
    public void testJoinMissingOn() {
        QueryResult result = codingContestMain.selectPlayersWithNetworth("SELECT p.name FROM player p join worth w p.pid = w.id");
    }

    @Test(expected = AliasNotInOrderException.class)
    public void testJoinWrongAlias() {
        QueryResult result = codingContestMain.selectPlayersWithNetworth("SELECT p.name FROM player w join worth p on p.id = w.id where w.Networth >= 1000 and w.Gender = 'M';");
    }


    @Test(expected = UnsupportedQueryException.class)
    public void testJoinUnsupportedStar() {
        QueryResult result = codingContestMain.selectPlayersWithNetworth("SELECT * FROM player p join worth w on p.id = w.id;");
    }

    @Test(expected = UnsupportedQueryException.class)
    public void testJoinUnsupportedMultiCols() {
        QueryResult result = codingContestMain.selectPlayersWithNetworth("SELECT p.name,p.height FROM player p join worth w on p.id = w.id;");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testJoinOR() {
        QueryResult result = codingContestMain.selectPlayersWithNetworth("SELECT p.name FROM player p join worth w on p.id = w.id where w.Networth >= 1000 OR w.Gender = 'M';");
    }

}
