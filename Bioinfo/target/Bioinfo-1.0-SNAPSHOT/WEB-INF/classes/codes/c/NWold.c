#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#define MATCH 1
#define MISMATCH -1
#define GAP -1
void nwa(char*, char*);
int main(int argc, char *argv[]) {
  nwa(argv[1], argv[2]);
  return 0;
}
void nwa(char* seq1, char* seq2) {
  clock_t ini = clock();
  int **mat = malloc((strlen(seq1) + 1) * sizeof(int*));
  for(int i = 0; i <= strlen(seq1); i++){
    mat[i] = malloc((strlen(seq2) + 1) * sizeof(int));
  }
  for (int i = 0; i <= strlen(seq1); i++) {
    mat[i][0] = i * GAP;
  }
  for(int i = 1; i <= strlen(seq2); i++){
    mat[0][i] = i * GAP;
  }
  for(int i = 1; i <= strlen(seq1); i++){
    for(int j = 1; j <= strlen(seq2); j++){
      int x, y, z;
      if(seq1[i-1] == seq2[j-1]){
        x = mat[i-1][j-1] + MATCH;
      } else {
        x = mat[i-1][j-1] + MISMATCH;
      }
      y = mat[i-1][j] + GAP;
      z = mat[i][j-1] + GAP;
      if(x >= y && x >= z){
        mat[i][j] = x;
      } else if(y >= x && y >= z){
        mat[i][j] = y;
      } else if(z >= x && z >= y){
        mat[i][j] = z;
      }
    }
  }
  int col = strlen(seq1);
  int lin = strlen(seq2);
  int gaps = 0;
  int score = 0;
  while(col > 0 && lin > 0){
    if(mat[col][lin] == mat[col-1][lin-1] + (seq1[col-1] == seq2[lin-1] ? MATCH : MISMATCH)){
      score += (seq1[col-1] == seq2[lin-1] ? MATCH : MISMATCH);
      col--;
      lin--;
    } else if(mat[col][lin] == mat[col-1][lin] + GAP){
      col--;
      gaps++;
    } else {
      lin--;
      gaps++;
    }
  }
  printf("%.2f\n", ((double)clock() - ini) / CLOCKS_PER_SEC);
  printf("%d\n", score);
  printf("%d\n", gaps);
  printf("69\n");
  for(int i = 0; i <= strlen(seq1); i++){
    free(mat[i]);
  }
  free(mat);
}