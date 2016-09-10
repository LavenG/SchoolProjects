#include <assert.h>

#include "part2.h"

long part2(long x, long y)
{

    //This function takes in two arguments.
    //If the value of the second argument passed is greater than zero
    //the function right shift the second argument by 5 and compares it to
    //three times the first argument left shifted by 4 and returns the number
    //created by the comparison
    //If the value of the second argument is less than zero the function
    //add 31 to it and then right shifts it by 5 bits and compares it to
    //three times the first argument left shifted by 4 and returns the number
    //created by the comparison.
    return y >= 0 ? (y>>5)&((3*x)<<4) : ((31+y)>>5)&((3*x)<<4);
}
