#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#define MATCH 1;
#define MISMATCH -1;
#define GAP 0;

void swa(char*, char*);

int main(void) {
  swa("CGCTATAT", "TATACTA ");
  return 0;
}

void swa(char* seq1, char* seq2){
  // INICIALIZACAO DA MATRIZ
  clock_t ini = clock();
  int **mat = malloc((strlen(seq1)+1) * sizeof(int*));
  for(int i = 0; i<=strlen(seq1)+1; i++){
    mat[i] = malloc((strlen(seq2)+1) * sizeof(int));
  }
  for (int i = 0; i <=strlen(seq1)+1; i++) {
    mat[i][0] = 0;
  }
  for(int i = 1; i <=strlen(seq2)+1; i++){
    mat[0][i] = 0;
  }
  //PREENCHIMENTO DA MATRIZ
  int greater = 0;
  int greater_j = 0;
  int greater_i = 0;
  for(int i = 1; i<=strlen(seq1); i++){
    for(int j = 1; j<=strlen(seq2); j++){
      int x = 0;
      int y = 0;
      int z = 0;
      if(seq1[i-1] == seq2[j-1]){
        x = mat[j-1][i-1] + MATCH; 
      }else{
        x = mat[j-1][i-1] + MISMATCH;
      }
      y = mat[j-1][i] + GAP;
      z = mat[j][i-1] + GAP;
      if(x >= y && x >= z && x>=0){
        mat[j][i] = x;
      }else if(y >= x && y >= z && y>=0){
        mat[j][i] = mat[j-1][i];
      }else if(z > x && z > y && z>=0){
        mat[j][i] = mat[j][i-1];
      }else{
        mat[j][i] = 0;
      }
      if(mat[j][i]>greater){
        greater = mat[j][i];
        greater_j = j;
        greater_i = i;
      }
    }
  }
  //ALINHAMENTO
  int col = greater_i;
  int lin = greater_j;
  int gaps = 0;
  int score = 0;
  while(mat[lin][col] != 0){
    if(mat[lin-1][col] > mat[lin-1][col-1] && mat[lin-1][col] > mat[lin][col-1]){
      lin--;
      gaps++;
    }else if(mat[lin][col-1] > mat[lin-1][col-1] && mat[lin][col-1] > mat[lin][col-1]){
      col--;
      gaps++;
    }else if(mat[lin-1][col-1] < mat[lin][col]){
      score++;
      col--;
      lin--;
    }else{
      col--;
      gaps++;
    }
  }
  printf("\ntem_de_exec: %f", ((double)clock() - ini) / CLOCKS_PER_SEC);
  printf("\nscore: %d", score);
  printf("\ngaps: %d", gaps);
  printf("\nlin_de_cod: 71");
}
