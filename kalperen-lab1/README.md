# Computer Systems Organization : Lab 1

## Obtaining the lab code  

Clone this repository. (Do not fork!)

## Completing the lab

This lab contains four independent exercises. The instructions for each exercise can be found in files part1.c, part2.c, ..., part4.c. Fill in your code in each of the files and test them individually. For example, suppose you have completed the exercise in part1.c. Test by typing at the console the following: 

```
$ make
$ build/part1
set_point failed to set values correctly  
Aborted
```

-or-

```
$ make  
$ build/part1  
part1: set_point OK  
Segmentation fault: 11  
```

From the above error messages, you can see that your code did not pass the tests for this part. Debug and try again. Complete and test each part individually. 

Once you've passed the tests for all six labs, you can double check your test-passing status by typing: 

```
$ ./test-lab
point structure [part1.c]:
  set_point: OK
  create_point: OK
  point_dist: OK
linked list [part2.c]:
  list_insert: OK
  list_end: OK
  list_size: OK
  list_find: OK
  list_remove: OK
bitwise operations [part3.c]:
  is_odd: OK
  count_bits: OK
  interleave_bits: OK
more bitwise operations [part4.c]:
  get_exponent_field: OK
  clear_msb: OK
  bit_at_index: OK
```

## Evaluation
Your score will be computed out of a maximum of 100 points based on the following distribution:

* Correctness points (90 total).
* Style points (10 total). 10 points are reserved for a subjective evaluation of the style of your solutions and your commenting. Please use astyle regularly and especially before you submit your work. Your solutions should be as clean and straight-forward as possible. Your comments should be informative, but they need not be extensive. 

<i>Note while the testing script will give you a good idea of your final lab grade, its score does NOT constitute an assurance. In particular, just because they pass the tests does not necessarily mean that you have implemented everything perfectly.</i>

## Turning in your work

```
git commit -am "Submitting lab 1"  
git push origin master
```  

