JUnit/TestNG execution reporter/failure analyzer

About:

- High level statistics of JUnit execution results.
- Configurable failure patterns for grouping related failures.
- Comparison view of runs.


Technical:

- AngularJS frontend
- Scala/Hibernate backend
- HSQL DB for peristence

Sequence Diagram:

1. How tests call this interface.

=========================================================================================================================================================================
TestListener(Junit/TestNG Hook implmentation)					|						TestStatisticsRest (TestAnalyzer)       |          DataAccessLayer(Hibernate)   |
=========================================================================================================================================================================

(onFailure, onSuccess, onError)::>---------------(REST REQUEST (Update Test Result))------> handleRestRequest()--------------------------->  Save Information in HSQL

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------

2. How UI loads

==================================================================================================================
WebPage - AngularJS (welcome.html)              |                       TestStatisticsRest (TestAnalyzer)        |
==================================================================================================================

GetContent()::>-------------------------REST REQUEST----------------------> QueryDatabAseForTestResult 

------------------------------------------------------------------------------------------------------------------