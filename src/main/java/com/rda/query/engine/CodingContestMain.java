package com.rda.query.engine;

import com.rda.query.engine.model.AvgQueryElements;
import com.rda.query.engine.model.JoinQueryElements;
import com.rda.query.engine.model.QueryResult;
import com.rda.query.engine.model.SelectQueryElements;
import com.rda.query.engine.model.SortQueryElements;
import com.rda.query.engine.processor.AvgQueryProcessor;
import com.rda.query.engine.processor.JoinQueryProcessor;
import com.rda.query.engine.processor.SelectQueryProcessor;
import com.rda.query.engine.processor.SortQueryProcessor;
import com.rda.query.engine.utils.validator.AvgQueryValidator;
import com.rda.query.engine.utils.validator.BasicQueryValidator;
import com.rda.query.engine.utils.validator.JoinQueryValidator;
import com.rda.query.engine.utils.validator.SelectQueryValidator;
import com.rda.query.engine.utils.validator.SortQueryValidator;

import java.util.HashMap;
import java.util.Map;

/*
 * Coding problem, Add your testcase
 * Implement a common method which will parse the query and result the result from DB and provided the outout via given method.
 * */
public class CodingContestMain {

	/* Table jsonstructure
	* {
		  "Schema": "tablename",
		  "Columns": [],
		  "Types": [],
		  "Data": []
		}
	*
	* */

    private static String playerTable = "{\"Schema\":\"Player\",\"Columns\":[\"ID\",\"Name\",\"Height\",\"Weight\",\"Location\"],\"Types\":[\"Integer\",\"String\",\"Integer\",\"Integer\",\"String\"],\"Data\":[[10,\"sachin\",165,72,\"Dadar Mumbai\"],[11,\"Rahul\",169,78,\"Bangalore\"],[12,\"Saurav\",165,30,\"Bengal\"],[13,\"MSD\",10,78,\"Ranchi\"],[14,\"Daiana\",162,62,\"Dehli\"],[99,\"Sushant Rajput\",173,80,\"Jaipur\"]]}";
    private static String worthTable = "{\"Schema\":\"Worth\",\"Columns\":[\"ID\",\"Category\",\"IPLTeam\",\"Networth\",\"Gender\"],\"Types\":[\"Integer\",\"String\",\"String\",\"Integer\",\"String\"],\"Data\":[[10,\"A1\",\"MI\",1000,\"M\"],[11,\"A1\",\"RCB\",349,\"M\"],[12,\"A2\",\"KKR\",250,\"M\"],[13,\"B\",\"CK\",834,\"\"],[14,\"B2\",\"\",12,\"F\"],[99,\"\",\"\",1200,\"M\"]]}";


    public static void main(String[] args) {
        System.out.println("Hello!");
    }


    public Integer handleAverageQuery(String query) {
        BasicQueryValidator.validate(query);
        AvgQueryElements avgQueryElements = AvgQueryValidator.validate(query, playerTable, worthTable);
        int avg = AvgQueryProcessor.process(avgQueryElements);
        return new Integer(avg);
    }


    /**
     * Skipping this implementation as instructed by coding challenge co-ordinator.
     *
     * @param query
     * @return HashMap<String, Integer>()
     */
    public Map<String, Integer> calculateBMIOfEachPlayer(String query) {
        //implement your logic

        return new HashMap<String, Integer>();
    }

    public QueryResult handleSortQuery(String query) {
        BasicQueryValidator.validate(query);
        SortQueryElements sortQueryElements = SortQueryValidator.validate(query, playerTable, worthTable);
        QueryResult sortResult = SortQueryProcessor.process(sortQueryElements);
        return sortResult;
    }

    public QueryResult selectPlayersWithNetworth(String query) {
        BasicQueryValidator.validate(query);
        JoinQueryElements joinQueryElements = JoinQueryValidator.validate(query, playerTable, worthTable);
        QueryResult selectQueryResult = JoinQueryProcessor.process(joinQueryElements);
        return selectQueryResult;
    }

    public QueryResult selectFromTable(String query) {
        BasicQueryValidator.validate(query);
        SelectQueryElements selectQueryElements = SelectQueryValidator.validate(query, playerTable, worthTable);
        QueryResult selectQueryResult = SelectQueryProcessor.process(selectQueryElements);
        return selectQueryResult;
    }

}
