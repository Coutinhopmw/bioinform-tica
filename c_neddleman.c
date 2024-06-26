#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define MATCH 1
#define MISMATCH -1
#define GAP -1

void nwa(char*, char*);

int main(void) {
  nwa("G", "GTETYCGACGCA");
  return 0;
}

void nwa(char* seq1, char* seq2){
  clock_t ini = clock();

  int len_seq1 = strlen(seq1);
  int len_seq2 = strlen(seq2);

  // Alocação das matrizes
  int **mat = (int **)malloc((len_seq1 + 1) * sizeof(int *));
  int **mat_dir = (int **)malloc((len_seq1 + 1) * sizeof(int *));
  
  for(int i = 0; i <= len_seq1; i++){
    mat[i] = (int *)malloc((len_seq2 + 1) * sizeof(int));
    mat_dir[i] = (int *)malloc((len_seq2 + 1) * sizeof(int));
  }

  // Inicialização da matriz
  mat[0][0] = 0;
  mat_dir[0][0] = 0;

  for (int i = 1; i <= len_seq1; i++) {
    mat[i][0] = i * GAP;
    mat_dir[i][0] = 1;  // Direção 1 representa gap na sequência 2
  }

  for (int j = 1; j <= len_seq2; j++) {
    mat[0][j] = j * GAP;
    mat_dir[0][j] = 2;  // Direção 2 representa gap na sequência 1
  }

  // Preenchimento da matriz
  for (int i = 1; i <= len_seq1; i++) {
    for (int j = 1; j <= len_seq2; j++) {
      int score_diag = mat[i-1][j-1] + (seq1[i-1] == seq2[j-1] ? MATCH : MISMATCH);
      int score_up = mat[i-1][j] + GAP;
      int score_left = mat[i][j-1] + GAP;

      if (score_diag >= score_up && score_diag >= score_left) {
        mat[i][j] = score_diag;
        mat_dir[i][j] = 3;  // Direção 3 representa diagonal
      } else if (score_up >= score_diag && score_up >= score_left) {
        mat[i][j] = score_up;
        mat_dir[i][j] = 1;  // Direção 1 representa gap na sequência 2
      } else {
        mat[i][j] = score_left;
        mat_dir[i][j] = 2;  // Direção 2 representa gap na sequência 1
      }
    }
  }

  // Alinhamento
  int col = len_seq2;
  int lin = len_seq1;
  int gaps = 0;
  int score = mat[lin][col];

  while (col > 0 && lin > 0) {
    if (mat_dir[lin][col] == 2) {
      col--;
      gaps++;
    } else if (mat_dir[lin][col] == 1) {
      lin--;
      gaps++;
    } else {
      col--;
      lin--;
    }
  }

  // Liberação de memória
  for (int i = 0; i <= len_seq1; i++) {
    free(mat[i]);
    free(mat_dir[i]);
  }
  free(mat);
  free(mat_dir);

  // Impressão dos resultados
  printf("\ntem_de_exec: %f", ((double)clock() - ini) / CLOCKS_PER_SEC);
  printf("\nscore: %d", score);
  printf("\ngaps: %d", gaps);
  printf("\nlin_de_cod: 61\n");
}
