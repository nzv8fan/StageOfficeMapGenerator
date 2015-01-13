#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define OUTSIDE_WIDTH 30
#define WALL_WIDTH 10
#define WALL_LENGTH 40
#define DOOR_WIDTH 20
#define GRID_COUNT 4

int main() {

  srand ( time(NULL) );

  printf("<?xml version=\"1.0\" standalone=\"no\"?>\n");
  printf("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n");
  printf("<svg width=\"510\" height=\"510\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n");
  printf("<rect width=\"510\" height=\"510\" style=\"fill:rgb(255,255,255);\"/>\n");

  printf("<line x1=\"0\" y1=\"0\" x2=\"510\" y2=\"0\" style=\"stroke:rgb(0,0,0);stroke-width:3\"/>\n");
  printf("<line x1=\"0\" y1=\"510\" x2=\"510\" y2=\"510\" style=\"stroke:rgb(0,0,0);stroke-width:3\"/>\n");
  printf("<line x1=\"0\" y1=\"0\" x2=\"0\" y2=\"510\" style=\"stroke:rgb(0,0,0);stroke-width:3\"/>\n");
  printf("<line x1=\"510\" y1=\"0\" x2=\"510\" y2=\"510\" style=\"stroke:rgb(0,0,0);stroke-width:3\"/>\n");

  int i, j; 

  for (i = 0; i <= GRID_COUNT; i++) {
    for (j = 0; j < GRID_COUNT; j++) {
      int startOffsetX = OUTSIDE_WIDTH + j * (WALL_WIDTH + WALL_LENGTH * 2 + DOOR_WIDTH);
      int startOffsetY = OUTSIDE_WIDTH + i * (WALL_WIDTH + WALL_LENGTH * 2 + DOOR_WIDTH) + 5;
      printf("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX, startOffsetY, startOffsetX + WALL_WIDTH + WALL_LENGTH, startOffsetY);
      if (rand() % 3 == 0) {  /*  insert random door here. */ 
        printf("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX + WALL_WIDTH + WALL_LENGTH, startOffsetY, startOffsetX + WALL_WIDTH + WALL_LENGTH + DOOR_WIDTH, startOffsetY);
      }
      printf("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX + WALL_WIDTH + WALL_LENGTH + DOOR_WIDTH, startOffsetY, startOffsetX + WALL_WIDTH + DOOR_WIDTH + 2 * WALL_LENGTH, startOffsetY);
      
    }
  }

  for (i = 0; i < GRID_COUNT; i++) {
    for (j = 0; j <= GRID_COUNT; j++) {
      int startOffsetX = OUTSIDE_WIDTH + j * (WALL_WIDTH + WALL_LENGTH * 2 + DOOR_WIDTH) + 5;
      int startOffsetY = OUTSIDE_WIDTH + i * (WALL_WIDTH + WALL_LENGTH * 2 + DOOR_WIDTH);
      
      printf("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX, startOffsetY, startOffsetX, startOffsetY + WALL_WIDTH + WALL_LENGTH);
      if (rand() % 3 == 0) {  /*  insert random door here. */ 
        printf("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX, startOffsetY + WALL_WIDTH + WALL_LENGTH, startOffsetX, startOffsetY + WALL_WIDTH + WALL_LENGTH + DOOR_WIDTH);
      }
      printf("<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"stroke:rgb(0,0,0);stroke-width:10\"/>\n", startOffsetX, startOffsetY + WALL_WIDTH + WALL_LENGTH + DOOR_WIDTH, startOffsetX, startOffsetY + 2 * WALL_WIDTH + DOOR_WIDTH + 2 * WALL_LENGTH);
     }
  }
  
  printf("</svg>\n");

  return 0;
}
