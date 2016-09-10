/***********************************************/
/* Do not change anything below this line      */
/***********************************************/

#define SMALL   64
#define N       1024
#define DIM     512
#define DIM2    1024
#define LARGE   10000
#define LARGER  50000

char* part3();

char* part3_opt();

/***********************************************/
/* Do not change anything above this line      */
/***********************************************/

/*
 * If you wish to use a different definition
 * of the following structure, provide a new
 * definition (do not remove the existing one).
 */

typedef struct
{
    char c1;
    double f1;
    int n1;
    char c2;
    int n2;
    double f2;
} data;

//By redefining the struct and reorganizing it
//we make sure that the new struct is packed
//wasting only 6 bytes at the end instead of 14 bytes as in
//the struct above. We bring the total size of the struct from 40 bytes
//to 32 bytes which increases the amount of cache hits significantl because
//you can store more structs in one cache at a time
typedef struct
{
    double f1;
    double f2;
    int n1;
    int n2;
    char c1;
    char c2;
} packedData;

