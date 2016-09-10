#include "lab4.h"

/* Shell input line */
int process(void)
{
    char *arg[MAXARG + 1];  /* pointer array for runcommand */

    int toktype = 0;            /* type of token in command */

    //Initializing narg to 0 fixes bug 1
    int narg = 0;              /* number of arguments so far */

    int type = 0;               /* type = FOREGROUND or BACKGROUND */

    while (1)
    {
        /* Take action according to token type */
        switch(toktype = gettok(&arg[narg]))
        {
        case ARG:
            if(narg < MAXARG)
            {
                narg++;
            }
            break;

        case EOL:

        case SEMICOLON:

        case AMPERSAND:
            type = toktype == AMPERSAND ? BACKGROUND : FOREGROUND;

            if(narg != 0)
            {
                arg[narg] = 0;
                runcommand(arg,type);
            }


            if(toktype == EOL)
            {
                return;
            }

            narg = 0;

            break;
        }
    }
}
