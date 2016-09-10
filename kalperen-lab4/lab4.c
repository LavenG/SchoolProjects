#include "lab4.h"

int main()
{   
    //Fix for bug 4
    //Handle SIGINT and kill child process without killing the shell to
    signal(SIGINT, sigint_handler);
    //run the shell until EOF (keyboard Ctrl+D) is detected
    while (userinput( ) != EOF)
    {
        process();
    }
    return 0;
}

//This function is used to catch and handle SIGINT signal in order to fix bug 4
void sigint_handler()
{
    //simply return
    return;
}

/*
*Fix for Bug 1 (process.c lines 8-13)
* Initializing narg to zero fixes bug 1. Each argument
* in the command line is assigned to an int value corresponding
* to the type of argument it is. Once all arguments are assigned 
* a value the array that contains the arguments and location of where to run
* them is passed to the runcommand function. After runcommand returns we reassing
* the value 0 to narg in order to ensure that the next set of argguments
*are passed correctly. 
*/

/*Fix for Bug 2 (runcommand.c lines 9-81)
* These lines implement the functionallity of the cd argument in the command line.
* The added code recognizes the cd command and asses different possible scenarios
* depending on what the command entails. Each scenario is discussed in the 
* inline comments of the code that implements this functionality.
*/

/* Fix for Bug 3 (runcommand.c lines 83-87)
 * The fix for bug 3 is straightforward. The runcommand method recognizes
 * when user enters the exit command and the process is terminated by calling
 * exit(0)
 */

/* Fix for Bug 4 (lab4.c  lines 5-7, lab4.c lines 16-21, runcommand.c lines 96-99, lab4.h lines 37-38)
 * The fix for bug 4 catches and handles SIGINt signals. If ctr+c is entered in the 
 * shell while nothing is running then we prevent the shell from exiting. If a program
 * is running in the foreground and ctrl+c is entered by the user then we terminate the program.
 * We also provide ctrl+c from terminating programs running in the background with the code 
 * on lines 96-99 of runcommand.c as those are supposed to be terminade by passing kill pid to the shell.
*/

/* Fix for Bug 5 (runcommand.c lines 116-117)
 * The fix for bug 5 makes sure that the parent processes reap their child processes 
 * running in the background before terminating. This is done by calling wait(NULL)
 * which ensures that the parent process waits for its child processes to terminate
 * before terminating itself, the child processes are thus properly reaped.
*/

