##CSCI 202 2016-17 Fall Lab 2-Scheduler

In this lab we simulate scheduling in order to see how the time required depends on the scheduling algorithm and the request patterns. In order to do this we implement the Shortest Job First, Uniprogrammed, First Come First Serve and Round Robin scheduling algorithms.

To compile the program navigate to the directory containing it and type:
javac *.java

To run:
make sure that the input files and the "random-numbers.txt" file are in the same directory as the program.
IMPORTANT: the program will only work on inputs without the parenthesis around the processes. Sample input files have been included for convenience.

java Scheduler <tag> [arg0]

<tag> (optional) if the tag "--verbose" is used the output is in the format of the verbose output. If the tag is not used the output is the normal output.
[arg0] is the name of the input file it should be provided in the following format:

examples:
with normal output:

java Scheduler input-1.txt

with verbose output:
java Scheduler --verbose input-1.txt

The four Scheduling algorithms will print one after the other in the terminal.
