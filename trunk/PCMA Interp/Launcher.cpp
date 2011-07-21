#include <iostream>
#include "Execute.h"
#include <stdio.h>
#include <stdlib.h>


int main(int argc, char* argv[]) {

  Execute e;
  if(argc < 2) {
      printf("You must provide at least one argument\n");
      
  }
  printf("provided argument %s",argv[1]); 
  e.executeTasks(atoi(argv[1]), atoi(argv[2]), atoi(argv[3]));

  return 0;
}
