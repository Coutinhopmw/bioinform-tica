#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#define MATCH 1;
#define MISMATCH -1;
#define GAP -1;

void nwa(char*, char*);

int main(void) {
  nwa("GTCGACGCA", "GTCGACGCA");
  return 0;
}

void nwa(char* seq1, char* seq2){
  // INICIALIZACAO DA MATRIZ
  clock_t ini = clock();
  int **mat = malloc((strlen(seq1)+1) * sizeof(int*));
  int **mat_dir = malloc((strlen(seq1)+1) * sizeof(int*));
  for(int i = 0; i<=strlen(seq1)+1; i++){
    mat[i] = malloc((strlen(seq2)+1) * sizeof(int));
    mat_dir[i] = malloc((strlen(seq2)+1) * sizeof(int*));
  }
  
  for (int i = 0; i <=strlen(seq1)+1; i++) {
    mat[i][0] = i*GAP;
    mat_dir[i][0] = 1;
  }
  
  for(int i = 1; i <=strlen(seq2)+1; i++){
    mat[0][i] = i*GAP;
    mat_dir[0][i] = 2;
  }
  //PREENCHIMENTO DA MATRIZ
  for(int i = 1; i<=strlen(seq1); i++){
    for(int j = 1; j<=strlen(seq2); j++){
      int x = 0;
      int y = 0;
      int z = 0;
      if(seq1[i-1] == seq2[j-1]){
        x = mat[i-1][j-1] + MATCH;
      }else{
        x = mat[i-1][j-1] + MISMATCH;
      }
      y = mat[i-1][j] + GAP;
      z = mat[i][j-1] + GAP;
      if(x >= y && x >= z){
        mat[i][j] = x;
        mat_dir[i][j] = 3;
      }else if(y >= x && y >= z){
        mat[i][j] = y;
        mat_dir[i][j] = 1;
      }else if(z > x && z > y){
        mat[i][j] = z;
        mat_dir[i][j] = 2;
      }
    }
  }

  for(int i = 0; i<=strlen(seq1) + 1; i++){
    printf("\n");
    for(int j = 0; j<=strlen(seq2); j++){
      printf("%2d ", mat_dir[i][j]);
    }
  }
  //ALINHAMENTO
  int col = strlen(seq1);
  int lin = strlen(seq2);
  int gaps = 0;
  int score = mat[lin][col];
  while(col != 0 || lin != 0){
    if(mat_dir[lin][col] == 2){
      col--;
      gaps++;
    }else if(mat_dir[lin][col] == 1){
      lin--;
      gaps++;
    }else{
      col--;
      lin--;
    }
  }
  printf("\ntem_de_exec: %f", ((double)clock() - ini) / CLOCKS_PER_SEC);
  printf("\nscore: %d", score);
  printf("\ngaps: %d", gaps);
  printf("\nlin_de_cod: 61\n");
}
