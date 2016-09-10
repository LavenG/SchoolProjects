# Computer Systems Organization : Lab 4

A student attempted to implement her own shell program. It works, but it has several bugs.  Your task for this lab is to fix the bugs and add some additional features. In addition, you may do some 'opportunistic refactoring', which simply means you can look for opportunity to improve code quality while you work. For example, you may try to reduce the usage of global variables.

# List of Known Bugs

## Bug 1: Only the first command works properly

```
$ ./lab4
sh201>  ls
Makefile        lab4.c          process.c       test_run.c        test_run_pause.c    userin.c
README.md       lab4.h          runcommand.c    test_run_loop.c   test_run_sleep.c
sh201>  ls
ls: cannot access ls: No such file or directory
sh201>  ls
ls: cannot access ls: No such file or directory
ls: cannot access ls: No such file or directory
sh201>  who
who: extra operand   w h o
Try ’who --help’ for more information.
sh201>
```

After we start the shell running lab4 the output from the first command is correct, but all later attempts at running anything fail. HINT: this is a very simple fix, but you need to understand the code for the shell, before you can fix it. Fixing this bug, will make it much easier to observe the behavior of the other bugs and test if the fixes to them are correct.

## Bug 2: `cd` command does not behave as expected

The `cd` command is not supported at all.

```
$ ./lab4
sh201> cd /home
cd: No such file or directory
sh201>
```

The reason for that is that passing `cd` as the name of the program to `execvp` system call fails (this command requires change of the environment and hence it is handled in a different fashion than most other commands that you encountered this semester). The program should, instead, use `chdir` system call to change the current working directory. (Use the man pages to find out how to use `chdir`.) You need to fix this problem by implementing the code that will allow the user to use the cd command in the following three ways:

* `cd` changes the current working directory to the user’s home directory (you will need to use
`getenv()` function to obtain the name of the user’s home directory 
* `cd PATH_NAME` changes the current working directory to the one specified by the
PATH_NAME (if such directory does not exist, the shell should print an error message and return to the prompt)
* `cd ..` changes the current working directory to the one that is the parent of the current working directory

## Bug 3: `exit` command does not terminate the shell

This is really a feature request. The `exit` command is not implemented.  We would like to be able to type `exit` with no parameters to terminate the shell program.  Currently the only way to terminate the shell is by typing the end of like (EOF) character on the keyboard (this is done by pressing `Ctrl+D`).

```
$ ./proj4
sh201>  exit
exit: No such file or directory
sh201>
$
```

## Bug 4: Pressing `Ctrl+C` should not kill the shell

It is customary that the shell itself should be immune to SIGINT signal (`Ctrl+C` from the keyboard). Pressing the key combination should terminate a process running in the foreground mode, but not the shell itself. Currently the shell terminates when the user presses `Ctrl+C`.

```
$ ./proj4
sh201>  ls
Makefile        lab4.c          process.c       test_run.c        test_run_pause.c    userin.c
README.md       lab4.h          runcommand.c    test_run_loop.c   test_run_sleep.c
sh201> ˆC
$
```

## Bug 5: Background processes are not reaped

The shell supports running processes in the background, but it does not reap such processes properly.  All background processes should be reaped (as opposed to becoming zombie processes).

```
$ ./proj4
sh201>  ./test_run &
[Process id 3865]
sh201>  program name: ./test_run
sh201>  ps
PID TTY          TIME CMD
3249 pts/13   00:00:00 bash
3862 pts/13   00:00:00 proj4
3865 pts/13   00:00:00 test_run <defunct>
3870 pts/13   00:00:00 ps
sh201>
```

# Files 

Your repository contains the following files

* `lab4.c` - the actual program that starts the shell  
* `lab4.h` - the header file containing function declarations, definitions of constant, define statements, etc.  (if you need to define additional structures, functions or constants you should add them to this file)  
* `process.c, runcommand.c, userin.c` - functions that implement the shell.  
* `test_run.c, test_run_loop.c, test_run_sleep.c, test_run_pause.c` - the simple self contained programs (use them to test if you can run a program in a foreground / background, if a process can be killed by Ctrl+C, if the process is reaped after termination, etc; feel free to add some of your own program like these ones)  
*  `Makefile` - the makefile for this lab  

# Compiling and Building 

You can compile and build the code by running:

```
$ make clean && make
```

This produces lab4 program that runs the shell.

The bugs are fairly independent. An attempt was made to list them in the order of difficulty, but you may decide otherwise. Fixing bug 1 should make the behavior of the shell more useful for testing of the behavior of the other bugs and bug fixes.

# How to fix a bug?

For each bug, you need to add/modify the code that causes the problem. You should clearly indicate in inline comments which bug is being fixed by the change.

For each bug, your lab4.c file should contain a paragraph with the brief description of how you fixed the bug and where the fix is (name of the file and line numbers, or multiple such locations if the fix is distributed).

If you add any functions, you should document those functions. You are not obligated to document the code that is given.

## Evaluation
Your score will be computed out of a maximum of 100 points based on the following distribution:

* Correctness: (100 points total) Each bug fix is worth 20 points. 5 points of which is the documentation of the fix as described above.
* Extra Credit: (5 points total) Reduce or remove the utilization of global variables.

## Turning in your work

```
git commit -am "Submitting lab 4"  
git push origin master
``` 
