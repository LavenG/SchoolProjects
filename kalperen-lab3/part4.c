#include <stdlib.h>
#include <stdio.h>
#include <assert.h>

#include "part4.h"

/*
 * Implement the C function called part4_opt. It should perform
 * the same task as the implemented part4 function. In the
 * comments for the function describe why your changes make the
 * function more cache friendly.
 *
 * Note: your function needs to compute the entire matrix C, not only C[x].
 */

long part4(long* B, long* A, int x)
{
    long* C = (long*) calloc(N, sizeof(long));
    if (!C) return 0;

    int c, r;
    for(c = 0; c < N; c++)
        for(r = 0; r < N; r++)
            C[r] = B[r * N + c] + A[c * N + r];

    long ret = C[x];
    free(C);
    return ret;
}

long part4_opt(long* B, long* A, int x)
{

    long* C = (long*) malloc(N *sizeof(long));
    if (!C) return 0;

    int c, r;

    for(r = 0; r < N; r++)
        for(c = 0; c < N; c++)
            //We switch the for loops in order to reduce the strides
            //with each iteration and rewrite the B array accordingly
            //increasing cache hits
            B[r] = B[r * N + c];

    for(c = 0; c < N; c++)
        for(r = 0; r < N; r++)
        {
            //We rewrite the A array so that all the assignments in the C array
            //can be done in a single loop
            A[r] = A[c * N + r];
        }

    //We assign the values in the C array with a single for loop
    //which reduces strides and increases cache hits.
    for(r = 0; r < N; r++)
        C[r] = A[r] + B[r];

    //assert(0);

    //DO NOT modify the rest of this function
    long ret = C[x];
    free(C);
    return ret;
}
