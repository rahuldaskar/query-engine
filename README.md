### Problem
You have to build a program that will act as a query engine.It will take sentences (strings) as input and process them.
This engine will support only SELECT queries in the current scope. You are also provided with JSON data for tables
along with their columns data types and the actual data (available in a main Java file). For all syntactically valid
queries, the program/engine should return appropriate data from the JSON as a console response.

### This program should 
1. Parse SELECT query for the valid syntax i.e. SELECT column | columns | * from table[a valid table name in JSON] 
where (single or joint condition)
2. Report unsupported or erroneous queries with the syntax violation
3. For valid queries, the program should then execute the query and fetch/display valid results as per table data[some pretty print on console]
4. Treat table/column names/queries as well as data as case insensitive
5. "" in a record to be taken as data not available