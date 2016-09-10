#include "lab4.h"

// Execute a command with optional wait
int runcommand(char** cline, int where)
{
    pid_t pid;
    int status;

    //Implements a cd functionaly to fix bug 2
    //check if user typed cd in the command line
    if(strcmp(*cline, "cd") == 0){

        //second argument in the command line is stored as the pathname
        char * path = cline[1];

        //if there is no path name we go to the home directory
        if(path == NULL || path == ""){

            chdir(getenv("HOME"));
        }

        //if there is a path name we check all possible cases
        else if(path!=NULL){

            //if the user typed a backslash we pass the pathname
            //to the chdir funtion
            if(path[0] == '/'){

                int chdirVal = chdir(path);

                if(chdirVal != 0){
                    printf("The specified directory is non existant\n");
                }
            }
        
            //if .. is the second argument we go to the parent directory
            else if(strcmp(path, "..") == 0){
                chdir(path);
            }

            //last case is when the user entered a pathname but didn't 
            //enter a backslash as the first character
            else{

                //create a new character array
                char pathWBackslash[1024];

                //clear the new array's contents
                memset(&pathWBackslash[0], 0, sizeof(pathWBackslash));

                //store a backslash in the first index
                pathWBackslash[0] = '/';

                //concatenate user entry with backlash
                strcat(pathWBackslash, path);

                //create an array to store the current directory
                char currentDir[1024];

                //clear current directory's content
                memset(&currentDir[0], 0, sizeof(currentDir));

                //check that current directory is stored in currentDir correcltly
                if(getcwd(currentDir, sizeof(currentDir)) != NULL){

                    strcat(currentDir, pathWBackslash);

                    //change the directory to the user entered directory
                    int chdirVal = chdir(currentDir);

                    //if the change is not successful display an error message
                    if(chdirVal != 0){

                    printf("The specified directory is non existant.\n");
                    }
                }
            }
        }
    
      
    }

    //Implement an exit functionality to fix bug 3
    else if(strcmp(*cline, "exit") == 0){
        //If the user entered "exit" exit from the program
        exit(0);
    }

    else{
         switch(pid = fork())
         {
            case -1:
                perror(SHELL_NAME);
                return (-1);
            case 0:
                //Fixes Bug 4 by making sure ctrl+c does not kill 
                //processes running in the background
                //If we are not in the background
                if(where!= BACKGROUND)
                    //catch and handle the signal
                    signal(SIGINT,SIG_DFL);
                execvp(*cline,cline);
                //we should never get to this code, sice execve does not return
                perror(*cline);
                exit(1);
        }
    }
   

    // Code for parent
    // If background process print pid and exit program is running in the background
    if(where == BACKGROUND)
    {
        printf("[Process id %d]\n",pid);
        return 0;
    }

    //Wait for the child processes to terminate in order to fix bug 5
    wait(NULL);
    
    // Wait until process pid exits
    if (waitpid(pid,&status,0) == -1) 
    {
        return -1;
    } 
    else 
    {
        return status;
    }
}
