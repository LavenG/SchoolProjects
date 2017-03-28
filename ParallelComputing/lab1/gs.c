#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <mpi.h>
/*** Skeleton for Lab 1 ***/

/***** Globals ******/
float **a; /* The coefficients */
float *x;  /* The unknowns */
float *b;  /* The constants */
float err; /* The absolute relative error */
int num = 0;  /* number of unknowns */

float *new_values;
float *errors;

int keepGoing = 1;

/****** Function declarations */
void check_matrix(); /* Check whether the matrix will converge */
void get_input();  /* Read input from file */

/********************************/
int keep_going();
float update_values();

/* Function definitions: functions are ordered alphabetically ****/
/*****************************************************************/

/*
   Conditions for convergence (diagonal dominance):
   1. diagonal element >= sum of all other elements of the row
   2. At least one diagonal element > sum of all other elements of the row
 */
void check_matrix()
{
  int bigger = 0; /* Set to 1 if at least one diag element > sum  */
  int i, j;
  float sum = 0;
  float aii = 0;

  for(i = 0; i < num; i++)
  {
    sum = 0;
    aii = fabs(a[i][i]);

    for(j = 0; j < num; j++)
       if( j != i)
	 sum += fabs(a[i][j]);

    if( aii < sum)
    {
      printf("The matrix will not converge.\n");
      exit(1);
    }

    if(aii > sum)
      bigger++;

  }

  if( !bigger )
  {
     printf("The matrix will not converge\n");
     exit(1);
  }
}


/******************************************************/
/* Read input from file */
/* After this function returns:
 * a[][] will be filled with coefficients and you can access them using a[i][j] for element (i,j)
 * x[] will contain the initial values of x
 * b[] will contain the constants (i.e. the right-hand-side of the equations
 * num will have number of variables
 * err will have the absolute error that you need to reach
 */
void get_input(char filename[])
{
  FILE * fp;
  int i,j;

  fp = fopen(filename, "r");
  if(!fp)
  {
    printf("Cannot open file %s\n", filename);
    exit(1);
  }

 fscanf(fp,"%d ",&num);
 fscanf(fp,"%f ",&err);

 /* Now, time to allocate the matrices and vectors */
 a = (float**)malloc(num * sizeof(float*));
 if( !a)
  {
	printf("Cannot allocate a!\n");
	exit(1);
  }

 for(i = 0; i < num; i++)
  {
    a[i] = (float *)malloc(num * sizeof(float));
    if( !a[i])
  	{
		printf("Cannot allocate a[%d]!\n",i);
		exit(1);
  	}
  }

 x = (float *) malloc(num * sizeof(float));
 if( !x)
  {
	printf("Cannot allocate x!\n");
	exit(1);
  }


 b = (float *) malloc(num * sizeof(float));
 if( !b)
  {
	printf("Cannot allocate b!\n");
	exit(1);
  }

 /* Now .. Filling the blanks */

 /* The initial values of Xs */
 for(i = 0; i < num; i++)
	fscanf(fp,"%f ", &x[i]);

 for(i = 0; i < num; i++)
 {
   for(j = 0; j < num; j++)
     fscanf(fp,"%f ",&a[i][j]);

   /* reading the b element */
   fscanf(fp,"%f ",&b[i]);
 }

 fclose(fp);

}

int keep_going (float *new_values) {
	int keep_going =1;
	int i = 0;
	float curr_err;
	//Check the error for every element
	for (i = 0; i < num; i++) {
		curr_err = fabs((new_values[i]- x[i])/new_values[i]);
		if (curr_err <= err) {
			keep_going = 0;
		}else{
      keep_going = 1;
    }
	}
	return keep_going;
}

float update_values(int index) {
    int i, j;
    float sum = b[index];
    for (i = 0; i < num; i++) {
        if (i != index) {
            sum-= a[index][i] * x[i];
        }
    }
    sum /= (float)a[index][index];
    return sum;
}

/************************************************************/


int main(int argc, char *argv[])
{
  int i;
  int nit = 0; /* number of iterations */
  int n;

  //Array to hold new values for the unknowns
  float * new_values = (float *) malloc(num * sizeof(float));
  //Initialize communicators

  if( argc != 2)
  {
    printf("Usage: gsref filename\n");
    exit(1);
  }

  /* Read the input file and fill the global data structure above */
  get_input(argv[1]);

  /* Check for convergence condition */
  /* This function will exit the program if the coffeicient will never converge to
  * the needed absolute error.
  * This is not expected to happen for this programming assignment.
  */
  check_matrix();


  int source;
  int my_rank;
  int comm_sz;

  double MPI_Wtime(void);
  double start_time, finish_time;

  start_time = MPI_Wtime();

  //Initialize and start MPI
  MPI_Init(&argc, &argv);

  MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);
  MPI_Comm_size(MPI_COMM_WORLD, &comm_sz);


  int count[comm_sz];
  int recieve[comm_sz];
  int displacements[comm_sz];

  int amount = num/comm_sz;
  int extra = num % comm_sz;
  int curr_displacement = 0;

   //fill count array with the unknowns
   for(i = 0; i<comm_sz; i++){
	    if (i < extra) {
		      count[i] = amount + 1;
	    }else {
		      count[i] = amount;
	    }
	     recieve[i] = count[i];
       displacements[i] = curr_displacement;
	     curr_displacement+= count[i];
  }

  //transfer values for the first iteration
  for (i = 0; i < num; i++) {
	   new_values[i] = x[i];
  }

  do {

	   nit++;
     //update the old values with the new ones
	   for (i = 0; i < num; i++) {
		      x[i] = new_values[i];
	   }

     int num = (int)ceil( (double) num/comm_sz  );
     float *arr = (float *) malloc(num * sizeof(float));
     int j;
     for (i = 0; i < count[my_rank]; i++) {
	      int current_index = i;
	      for (j = 0; j < my_rank; j++) {
          current_index = current_index + count[j];
	      }
	      arr[i] = update_values(current_index);
      }
      MPI_Allgatherv(arr, count[my_rank], MPI_FLOAT, new_values, recieve, displacements, MPI_FLOAT, MPI_COMM_WORLD);
  }

  while (keep_going(new_values));

  MPI_Barrier(MPI_COMM_WORLD);

  if (my_rank == 0) {
 	   for( i = 0; i < num; i++){
       printf("%f\n",new_values[i]);
     }
     printf("total number of iterations: %d\n", nit);
     finish_time = MPI_Wtime();
     printf("Elapsed time = %e seconds\n", finish_time - start_time);
  }

   MPI_Finalize();
   return 0;
   exit(0);
}
