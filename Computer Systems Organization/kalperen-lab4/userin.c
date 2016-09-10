#include "lab4.h"

// Program buffers and work pointer
static char inpbuf[MAXBUF], tokbuf[2*MAXBUF], *ptr = inpbuf, *tok = tokbuf;

// Print prompt and read a line
int userinput( )
{
    // Initialization for later routines
    ptr = inpbuf;
    tok = tokbuf;

    // Display prompt
    printf("%s", PROMPT);

    int count = 0;
    while(1)
    {
        int c;
        signal(SIGINT,SIG_IGN);
        if((c = getchar()) == EOF)
            return(EOF);

        if(count < MAXBUF)
            inpbuf[count++] = c;

        if(c == '\n' && count < MAXBUF)
        {
            inpbuf[count] = '\0';
            return count;
        }

        // if line too long restart
        if(c == '\n')
        {
            printf( SHELL_NAME  ": input line too long\n");
            count = 0;
            printf("%s",PROMPT);
        }
    }
}

static char special[] = {' ','\t','&',';','\n','\0'};

int inarg(char c)
{
    char* wrk;
    for(wrk = special; *wrk; wrk++)
    {
        if(c == *wrk)
        {
            return 0;
        }
    }

    return 1;
}

// Get token, place into tokbuf
int gettok(char** outptr)
{
    // set the outptr string to tok
    *outptr = tok;

    // strip white space from the buffer containing the token
    while(*ptr == ' ' || *ptr == '\t')
        ptr++;

    // set the token pointer to the first token in the buffer
    *tok++ = *ptr;

    // set the type variable depending
    // on the token in the buffer
    int type;
    switch(*ptr++)
    {
    case '\n':
        type = EOL;
        break;
    case '&':
        type = AMPERSAND;
        break;
    case ';':
        type = SEMICOLON;
        break;
    default:
        type = ARG;
        // keep reading valid ordinary characters
        while(inarg(*ptr))
        {
            *tok++ = *ptr++;
        }
    }

    *tok++ = '\0';

    return type;
}
