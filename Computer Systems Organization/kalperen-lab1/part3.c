#include <stdio.h>
#include <assert.h>
#include <limits.h>
#include <string.h>

// Helper function
char* short_to_binary(unsigned short x);

// Write a function that determines a number
// is odd using only bitwise operators.
// Return 1 for true or 0 for false.
//
// This should only take a couple of lines.
int is_odd(int i)
{
    //The only way a number is odd is if the first bit (2^0) is turned on
    //Check for that bit using a mask
    int is_odd = 0b1 & i;

    return is_odd;
}

// Write a function that determines the number of 1
// bits in the computers internal representation
// of a given unsigned long integer using only bitwise operators.
int count_bits(unsigned long l)
{

    unsigned int count = 0;
    //check if the first bit is turned on and then shift the long right by one
    //to check for the next bit until there are no more bits to check left
    while (l > 0)
    {
        if(l & 1)
        {
            count ++;
        }
        l >>= 1;
    }

    return count;
}


// Write a function that takes two chars and
// 'interleaves' their bits into a short.
// Moreover, bits of x are in the even positions
// and y in the odd of the result.
// Ex. interleave_bits(1111, 0000) = 10101010
//     interleave_bits(0000, 1111) = 01010101
//     interleave_bits(0101, 1010) = 01100110
//     (fewer bits shown for clarity)
unsigned short interleave_bits(unsigned char x, unsigned char y)
{
    unsigned short z = 0;

    int i;
    for (i = 0; i < sizeof(x) * CHAR_BIT; i++)
    {
        //asign the bits of y and x to z in alternating order by shifting them each time
        z |= (y & (1 << i)) << i;
        z |= (x & (1 << i)) << (i + 1);
    }

    return z;
}

// You can use this function to print out the bit patterns
// for a given unsigned short. This should help in debugging.
char* short_to_binary(unsigned short x)
{
    static char b[17];
    b[0] = '\0';

    int z;
    for (z = 32768; z > 0; z >>= 1)
    {
        strcat(b, ((x & z) == z) ? "1" : "0");
    }

    return b;
}

// feel free to write any other helper functions you require
