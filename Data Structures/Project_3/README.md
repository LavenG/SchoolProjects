# Data Structures : Project 3

## Restaurant Inspection Software

This Java program reads in restaurant inspection results as CSV files and parses each record as a record object. 
The Record objects are then stored into an ArrayList. Methods are provided to search through and sort the data
given user provided keys. 

## Notes on the third version

In this version the storage method of the records is improved by implementing a binary search tree instead of an
ArrayList. The records are stored in this binary search tree when the CSV file is parsed according to their DBA 
(name of the restaurant). Restaurants with the same name are stored inside of a linkedlist which is contained inside
of the node of the binary search tree. This improvement results into a significant performance increase when searching for
records given a certain key.

