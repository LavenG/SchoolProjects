# Data Structures : Project 4

## Restaurant Inspection Software

This Java program reads in restaurant inspection results as CSV files and parses each record as a record object. 
The Record objects are then stored into an ArrayList. Methods are provided to search through and sort the data
given user provided keys. 

## Notes on the fourth version

In this version the storage method of the records is improved by implementing a hashtables and two priority queues besides
the previously implemented binary search tree. Now when the user tries to find a record given the name the search time is only
O(1). The efficiency of finding records given the score or date is also increased by implementing priority queues. 

Analysis of search and Sort times of the methods is provided in the comments.