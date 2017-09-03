#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <cuda.h>
#include <locale.h>


/*
   input: pointer to an array of long int
          number of elements in the array
   output: the maximum number of the array
*/
__global__ void getmaxcu(unsigned int* num, unsigned int* max, int size){
  extern __shared__ unsigned int sdata[];

  unsigned int tid = threadIdx.x;
  unsigned int i = blockIdx.x*(blockDim.x) + threadIdx.x;

  //Load from global memory to shared memory
  sdata[tid] = num[i];
  if(i >= size){
    sdata[tid] = 0;
  }
  __syncthreads();
  // do reduction in shared memory by reducing each blocks max to a single value
  //strided index and non-divergent branch
  //reversed loop and threadID-based indexing
  for (int s=blockDim.x/2; s>0; s>>= 1) {
    __syncthreads();
    if (tid < s) {
      if(sdata[tid] < sdata[tid+s]){
        sdata[tid] = sdata[tid+s];
      }
    }

  }
  // write result for this block to global mem
  if(tid == 0){
    max[blockIdx.x] = sdata[0];
  }

}

int main(int argc, char *argv[])
{
    unsigned int size = 0;  // The size of the array
    unsigned int i;  // loop index
    unsigned int * numbers; //pointer to the array

    if(argc !=2)
    {
       printf("usage: maxseq num\n");
       printf("num = size of the array\n");
       exit(1);
    }

    size = atol(argv[1]);

    numbers = (unsigned int *)malloc(size * sizeof(unsigned int));
    if( !numbers )
    {
       printf("Unable to allocate mem for an array of size %u\n", size);
       exit(1);
    }

    srand(time(NULL)); // setting a seed for the random number generator
    // Fill-up the array with random numbers from 0 to size-1
    for( i = 0; i < size; i++)
       numbers[i] = rand()  % size;

    //Get the propretiest of the specific device we're using
    cudaDeviceProp devProp;
    cudaGetDeviceProperties(&devProp, 0);

    //Determine the maximum amount of threads in each block
    int maxThreadsPerBlock = devProp.maxThreadsPerBlock;

    //Determine the number of threads we need
    int numberOfThreads;
    if(size<maxThreadsPerBlock)
    {
      numberOfThreads = size;
    }else{
      numberOfThreads = maxThreadsPerBlock;
    }

    //Since we use the maximum amount of threads available per block,
    //in some cases, the last few threads of a block will be uselss
    //we ensure that this does not cause any problems by modyfying the
    //size accordingly and setting the indexes of the useless threads in the array
    //to 0
    int modSize = numberOfThreads;
    if(size % maxThreadsPerBlock != 0){
      modSize = (size/ maxThreadsPerBlock+1)* maxThreadsPerBlock;
    }else{
      modSize = size;
    }
    unsigned int *modNumbers = (unsigned int *)malloc(modSize* sizeof(unsigned int));
    for (int i = 0; i < modSize; i++) {
        if (i < size) {
            modNumbers[i] = numbers[i];
        } else {
            modNumbers[i] = 0;
        }
    }

    int numberOfBlocks = modSize/maxThreadsPerBlock;
    //array to send numbers to the device
    unsigned int * deviceArray;
    //Allocate the array on the device
    cudaMalloc((void**)&deviceArray, modSize*(sizeof(unsigned int)));
    //Copy the values to the array on the device
    cudaMemcpy(deviceArray, modNumbers, modSize*(sizeof(unsigned int)), cudaMemcpyHostToDevice);
    //Array that stores the maximum of each block on the device
    unsigned int* deviceMax;
    //Allocate the array on the device
    cudaMalloc((void**) &deviceMax, numberOfBlocks * sizeof(unsigned int));
    //Array that stores the maximum of each block on the host
    unsigned int* hostMax = (unsigned int *)malloc(numberOfBlocks*sizeof(unsigned int));

    //While we don't have a single value, keep calling the kernel so that we reduce
    //to a single block/value
    do {
      numberOfBlocks = ceil((float)modSize/(float)maxThreadsPerBlock);
      getmaxcu<<<numberOfBlocks, maxThreadsPerBlock, maxThreadsPerBlock*sizeof(unsigned int)>>>(deviceArray, deviceMax, modSize);
      //adjust the size since the array has been reduced
      modSize = numberOfBlocks;
      //set input = output from previous iteration
      deviceArray = deviceMax;
    } while(numberOfBlocks>1);

    //Transfer the max from device to host
    cudaMemcpy(hostMax, deviceMax, numberOfBlocks*sizeof(unsigned int), cudaMemcpyDeviceToHost);
    printf("The maximum number in the array is: %u\n", hostMax[0]);
    cudaFree(deviceArray);
    cudaFree(deviceMax);
    free(numbers);
    exit(0);
}
