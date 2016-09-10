#include <stdlib.h>
#include <stdio.h>
#include <assert.h>

#include "part3.h"

/*
 * Implement the C function called part3_opt. It should perform
 * the same task as the implemented part3 function. In the
 * comments for the function describe why your changes make the
 * function more cache friendly.
 *
 * You may observe that this function performs "useless" looping
 * over an array of structures that are all filled with zero bits.
 * Your task is not to remove that "useless" code, but modify it
 * so that it is more cache friendly. You also should not be removing
 * any parts of the structures.
 */

char* part3()
{
    // Note: struct 'data' is defined in part3.h, take a look
    data* B = (data*) calloc(LARGER, sizeof(data));
    if (!B) return 0;

    double sum_f = 0.0;
    double sum_n = 0;
    char* sum_c = (char*) malloc(( 2 * LARGER + 1) * sizeof(char));

    sum_c[2 * LARGER] = '\0';

    int i;
    for(i = 0; i < LARGER; i++)
    {
        sum_f += B[i].f1 + B[i].f2;
        sum_n += B[i].n1 + B[i].n2;
        sum_c[2 * i] = B[i].c1;
        sum_c[2 * i + 1] = B[i].c2;
    }

    free(B);
    return sum_c;
}

char* part3_opt()
{
    // TODO: Create the array of structures same or equivalent
    // to the one in the function above
    //assert(0);

    packedData* B = (packedData*) calloc(LARGER, sizeof(packedData));
    if (!B) return 0;

    // DO NOT modify the following 4 lines
    double sum_f = 0.0;
    double sum_n = 0;
    char* sum_c = (char*) malloc((2 * LARGER + 1) * sizeof(char));
    sum_c[2 * LARGER] = '\0';
    // DO NOT modify the preceeding 4 lines

    // TODO: Enter your code here (make sure to free
    // the array B once before returning)
    int i;
    for(i = 0; i < LARGER; i++)
    {
        sum_f += B[i].f1 + B[i].f2;
        sum_n += B[i].n1 + B[i].n2;
        sum_c[2 * i] = B[i].c1;
        sum_c[2 * i + 1] = B[i].c2;
    }

    //Check part3.h for the explanation of the optimization.

    //DO NOT modify the rest of this function
    return sum_c;
}
