#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define MAX_TASKS 1000

int main() {

  srand ( time(NULL) );

  int i;
  for (i = 0; i < MAX_TASKS; i++) {
    int x = rand() % 510; 
    while (x < 5 || x > 505 || (x > 25 && x < 45) || (x > 25 && x < 45) || (x > 135 && x < 155) || (x > 245 && x < 265) || (x > 355 && x < 375) || (x > 465 && x < 485) ) {
      x = rand() % 510; 
    }
    int y = rand() % 510;
    while (y < 5 || y > 505 || (y > 25 && y < 45) || (y > 25 && y < 45) || (y > 135 && y < 155) || (y > 245 && y < 265) || (y > 355 && y < 375) || (y > 465 && y < 485) ) {
      y = rand() % 510;
    }
    int z = rand() % 360;
    printf("%3.3f %3.3f 0 %d.000\n", (x - 255) * 0.05, (y - 255) * 0.05, z - 180);
  }

  return 0;
}
