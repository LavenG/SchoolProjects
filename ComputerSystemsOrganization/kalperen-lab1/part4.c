#include <stdio.h>
#include <assert.h>

// Extract the 8-bit exponent field of single precision
// floating point number f and return it as an unsigned byte
unsigned char get_exponent_field(float f)
{
    //cast the float to an unsigned int using a floater
    unsigned int* u = (unsigned int*) &f;
    //shift the float right by 23 to place the exponent field into the rightmost 8 bits and assign those to an unsigned char
    return (unsigned char)(*u >> 23U);
}

// Clear the most significant b bits of unsigned integer number
// and return the resulting value.
// As an example, suppose
//   unsigned int number = 0xffff0000;
//   int b = 15;
// then the correct return value is 65536
// if b = 30, then the return value is 0
// if b = 40, the return value should also be 0
unsigned int clear_msb(unsigned int number, int b)
{
    unsigned int mask = 0;
    unsigned int i;
    //determine how many bits are left to the right once the b most significant bits are cleared
    unsigned rightMostBits = 32 -b;
    for(i =0; i< rightMostBits; i++)
    {
        //create a mask to select the least significant bits that aren't supposed to be cleared
        mask|= 1<<i;
    }
    //apply the mask to the number to clear the most significant b bits
    number = mask & number;

    return number;
}

// Given an array of bytes whose length is array_size (bytes), treat it as a bitmap (i.e. an array of bits),
// and return the bit value at index i (from the left) of the bitmap.
//
// As an example, suppose char array[3] = {0x00, 0x1f, 0x12}, the corresponding
// bitmap is 0x001f12, thus,
// the bit at index 0 is bitmap[0] = 0
// the bit at index 1 is bitmap[1] = 0
// ...
// the bit at index 11 is bitmap[11] = 1
// the bit at index 12 is bitmap[12] = 1
// ...
// the bit at index 16 is bitmap[16] = 0
// ...
unsigned char bit_at_index(unsigned char *array, int array_size, int i)
{
    //check if i is out of bounds
    if(i > (array_size*8)-1)
    {
        return 0;
    }
    else
    {
        //Determine the position of the Byte and Bit desired
        int bytePos= i/8;
        int bitPos = i%8;

        //Get the Byte value at the desired index
        char byteValue = array[bytePos];
        //Check if the bit at the specified index is turned on
        int integerValue = byteValue >> (8-(bitPos+1)) & 0x0001;

        return integerValue;
    }

}
