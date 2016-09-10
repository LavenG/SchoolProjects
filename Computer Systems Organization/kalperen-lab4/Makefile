# Makefile for lab 4
# 
# DO NOT modify this file 

FILES  := process.c userin.c runcommand.c lab4.c lab4.h  
CC		= gcc
CFLAGS := -g -Og -std=gnu99 

all: lab4 test_run test_run_loop

lab4: $(FILES)
	cc -o lab4   $(FILES) 
    	
test_run: test_run.c 
	cc -o test_run test_run.c 	
	
test_run_loop: test_run_loop.c 
	cc -o test_run_loop test_run_loop.c 
	
test_run_sleep: test_run_sleep.c 
	cc -o test_run_sleep test_run_sleep.c 
	
test_run_pause: test_run_pause.c 
	cc -o test_run_pause test_run_pause.c 

clean: 
	rm -f *~ lab4 lab4.o *.o a.out test_run test_run_loop test_run_pause test_run_sleep core
