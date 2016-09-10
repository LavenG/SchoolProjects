#include <assert.h>

#include "part4.h"

//This function takes in a pointer to an array
//and a value and increments the pointer by the value passed to it
//it returns the original pointer
long foo(long* p, long val)
{
    //the tempPointer variable stores the original pointer passed
    long tempPointer = *p;
    //increment the original pointer by the value passed
    *p += val;
    //return the original pointer
    return tempPointer;
}

long part4(long* array, int size)
{

    //The arrayPtr variable keeps track of the original index of the array
    long *arrayPtr = array;
    //The index variable keeps track of the iterations in the while loop
    int index = 0;
    //The returnValue variable keeps track of the final value to be returned
    int returnValue = 0;

    while(index<size)
    {
        //Increment the pointer to the array by the original location plus the current index
        //to access element of the array
        array = arrayPtr+ index;
        //pass the array and 2 to the foo function and store the return
        //value
        returnValue += foo(array, 2);
        //increment the index
        index++;
    }
    return returnValue;
}
