#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <omp.h>
#include <string.h>


int main(int argc, char *argv[]){
  //Initialize an array to keep track of prime numbers and two integeres
  //to read in the command line input
  int t;
  int N;

  //Check that the command line inputs are correct, if not exit with an error message
  if(argc != 3){
    printf("Error: Wrong usage, please provide 2 command line arguments N and t");
  }

  N = atoi(argv[1]);
  t = atoi(argv[2]);

  if(N < 2 || N > 1000000){
    printf("Wrong usage, N needs to be bigger than 2 and less than or equal to 1 000 000");
    exit(1);
  }
  if(t < 1 || t > 100){
    printf("Wrong usage, t needs to be a positive integer that does not exceed 100");
    exit(1);
  }

  //Set the number of threads and allocate the array of primes
  omp_set_num_threads(t);
  int* nums = (int *) malloc((N+1) * sizeof(int));

  int i, j, k;
  //fill the array with numbers from 1 to N
  for (k = 0; k <= (N + 1); k++) {
  	nums[k] = k;
  }

  //Used to measure time
  double tstart = 0.0, ttaken;

  //Determine when to stop
  int flag = floor( (N+1)/2 );

  //If there is only one thread don't divide the work between the two for loops
  if(t ==1){
    tstart = omp_get_wtime();
    #pragma omp parallel for num_threads(t)
    for(i = 2; i < flag; i++){
      #pragma omp parallel for num_threads(t)
      for(j=i; j<=N; j=j+i){
        if(j!=i){
          nums[j]=0;
        }
      }
    }
    ttaken = omp_get_wtime() - tstart;
  //With multiple threads divide the work between the two for loops
  }else{
    tstart = omp_get_wtime();
    #pragma omp parallel for num_threads(t/2)
    for(i = 2; i < flag; i++){
      #pragma omp parallel for num_threads(t/2)
      for(j=i; j<=N; j=j+i){
        if(j !=i){
          nums[j]=0;
        }
      }
    }
    ttaken = omp_get_wtime() - tstart;//0.008599-0.015040
  }

  printf("Time take for the main part: %f\n", ttaken);

  //Print to the output file
  FILE *fp;
  char file[6] = {0};
  char txt[10] = {0};

  //Handle creating the correct file name
  sprintf(file, "%d", N);
  strncpy(txt, ".txt", 4);
  strcat(file, txt);

  //the rank of the number
  int a = 1;
  //Interval between current prime and the last prime
  int interval = 0;
  //Keeps track of the last prime
  int last = 2;
  //Open the file
  fp = fopen(file, "w+");
  //Write the first line by skipping the 1, and then write
  //to the output in the specified format
  fprintf(fp, "%d, %d, %d\n", a, last, interval);
  for(i = 3; i <= N;i++){
    if(nums[i]!=0)
    {
      interval = nums[i]-last;
      last = nums[i];
      a++;
      fprintf(fp, "%d, %d, %d\n", a, last, interval);
    }

  }
  fclose(fp);

}
