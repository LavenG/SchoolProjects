Alperen Karaoglu N13012011 ak4410
Lab2

First run module load gcc-6.2.0 (or whatever latest version of gcc you find)

To Compile:
 gcc -g -lm -Wall -fopenmp -o genprime genprime.c
The -lm tag is necessary because of the use of the math library to implement floor.
To run:
./genprime N t
The output will be in a text file with the name "[N].txt"
