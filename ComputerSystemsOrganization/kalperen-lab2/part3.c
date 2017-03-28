#include <assert.h>

#include "part3.h"

char part3(long x, int b)
{

//This function takes two arguments, the first one is a long
//and the second one is an int. If the second argument is a value
//between 0 and 7 (inclusive) it multiplies the second argument by eight,
//casts the result to a character, right shifts the first argument by that character
//and casts the result to a character. If the second argument is less than 0 or
//greater than 7 it returns 0.

    return b >= 0 && b <= 7 ? (char)(x>>(b*8)): 0;


}
