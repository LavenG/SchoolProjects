#include <assert.h>

#include <stdlib.h>

#include "part5.h"
//This function takes an array and its size
//It calculates the sum of every element except the last one
//and every element multiplied by each other except the last one
//it returns the sum of the additions and multiplications
long bar(long * a, int size)
{

    //the index variable keeps track of the number of
    //iterations in the loop
    int index = 0;
    //the mult variable is used to store the value of each element
    //at every index of the array except the last element multiplied
    //by each other
    int mult = 1;
    //the sum variable is used to keep track of the sum of all elements
    //in the array except the last one
    int sum = 0;
    //we decrement the size by one because the last element is not accessed
    size --;

    while(index<size)
    {
        sum += a[index];
        mult *= a[index];
        index ++;
    }
    return sum+mult;
}
//This function takes a size as an argument and creates
//an array of that size and initializes every element to
//double of its index. It then passes the created array
//to the bar function and returns the result
long part5(int size)
{
    //Create an array with the size passed to the function
    long a[size];
    long index = 0;
    //Initialize each element of the array to double its index
    while(index < size)
    {
        a[index] = 2*index;
        index++;
    }
    //pass the created array to the bar function and return the result
    return bar(a, size);
}
