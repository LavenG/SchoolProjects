# Computer Systems Organization : Lab 2

## Obtaining the lab code  

Clone this repository. (Do not fork!)

## Completing the lab

In this lab, you are given 5 object files, part1.o, part2.o, ..., part5.o. The source files used to generate these objects files have been withheld. Each object file implements the functions associated with the problem of the same name (e.g. part1.o defines function part). The later objects files contain additional functions. 

You are asked to dissassemble the object files and understand the x86-64 assembly code for each. Then write the corresponding C functions that perform the equivalent function. The assembly code generated from the code that you write may not be exactly the same as the decompiled versions of the posted files. It is sufficient that the functions perform the same tasks for all inputs. (However it should be pretty close!)

The problems are numbered in order of difficulty. You should attempt to solve them in that order. But you can jump around and work on the problems in any order you wish (this is a good stategy if you are stuck on a particular problem).

## Reading the assembly

The object files whose assembly code you seek to understand are in the objs/ subdirectory. Suppose you set out to figure out what function problem1 (implemented in objs/problem1.o) does. There are two approaches:

### objdump  

Disassemble the object files. Read the assembly and try to understand what the function tries to achieve. 

```
$ objdump -rd objects/problem1.o
```

### gdb  

Inspect the executable 

```
$ gdb bin/lab2
```

## Writing the C functions

The repository contains all files you need to work on this project. There are the problem files part1.c ... part5.c You will write your code within those files.

## Testing the C functions

To compile and execute each function...

```
$ make
$ build/part1
Part1 failed to perform the correct logic.
Aborted
```

From the above error messages, you can see that your code did not pass the test. Debug and try again. Complete and test each part individually. 

Once you've passed the tests for all six labs, you can double check your test-passing status by typing: 

```
$ ./test-lab
part1 [part1.c]:
  part1: OK
part2 [part2.c]:
  part2: OK
[part3.c]:
  part3: OK
[part4.c]:
  part4: OK
[part5.c]:
  part5: OK
Score: 90/90
```

## Evaluation
Your score will be computed out of a maximum of 100 points based on the following distribution:

* Correctness points (90 total).
* Style points (10 total). 10 points are reserved for a subjective evaluation of the style of your solutions and your commenting. Please use astyle regularly and especially before you submit your work. Your solutions should be as clean and straight-forward as possible. Your comments should be informative, but they need not be extensive. Also, taken into consideration are things like deallocating any memory you had to allocate.

<i>Note while the testing script will give you a good idea of your final lab grade, its score does NOT constitute an assurance. In particular, just because they pass the tests does not necessarily mean that you have implemented everything perfectly. The test harness tests your functions on a limited set of inputs. The fact that your implementation passes the test does not guarantee that the code that you wrote is correct.</i>

## Turning in your work

```
git commit -am "Submitting lab 2"  
git push origin master
```  

