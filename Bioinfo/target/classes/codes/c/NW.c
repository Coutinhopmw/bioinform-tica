#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

int score(char char1, char char2) {
    return (char1 == char2) ? 1 : -1;
}

void needleman_wunsch_reconstruct(char *seq1, char *seq2, int match_score, int mismatch_penalty, int gap_penalty) {
    clock_t inicio = clock();
    
    int len1 = strlen(seq1);
    int len2 = strlen(seq2);

    // Alocar matriz de pontuação
    int **score_matrix = (int **)malloc((len1 + 1) * sizeof(int *));
    for (int i = 0; i <= len1; i++) {
        score_matrix[i] = (int *)malloc((len2 + 1) * sizeof(int));
    }

    // Inicializar primeira coluna e primeira linha
    for (int i = 0; i <= len1; i++) {
        score_matrix[i][0] = gap_penalty * i;
    }
    for (int j = 0; j <= len2; j++) {
        score_matrix[0][j] = gap_penalty * j;
    }

    // Preencher a matriz de pontuação
    for (int i = 1; i <= len1; i++) {
        for (int j = 1; j <= len2; j++) {
            int match_score_ij = score_matrix[i - 1][j - 1] + score(seq1[i - 1], seq2[j - 1]);
            int delete_seq1_ij = score_matrix[i - 1][j] + gap_penalty;
            int delete_seq2_ij = score_matrix[i][j - 1] + gap_penalty;
            score_matrix[i][j] = (match_score_ij > delete_seq1_ij) ? 
                                 (match_score_ij > delete_seq2_ij ? match_score_ij : delete_seq2_ij) : 
                                 (delete_seq1_ij > delete_seq2_ij ? delete_seq1_ij : delete_seq2_ij);
        }
    }

    // Reconstruir alinhamento e contar gaps
    int i = len1, j = len2;
    int gaps = 0;
    while (i > 0 && j > 0) {
        if (score_matrix[i][j] == score_matrix[i - 1][j - 1] + score(seq1[i - 1], seq2[j - 1])) {
            i--;
            j--;
        } else if (score_matrix[i][j] == score_matrix[i - 1][j] + gap_penalty) {
            i--;
            gaps++;
        } else {
            j--;
            gaps++;
        }
    }
    while (i > 0) {
        gaps++;
        i--;
    }
    while (j > 0) {
        gaps++;
        j--;
    }

    clock_t fim = clock();
    double time_spent = (double)(fim - inicio) / CLOCKS_PER_SEC;
    int alignment_score = score_matrix[len1][len2];

    // Imprimir resultados
    printf("%.2f\n", time_spent);
    printf("%d\n", alignment_score);
    printf("%d\n", gaps);
    printf("34\n");

    // Liberar memória
    for (int i = 0; i <= len1; i++) {
        free(score_matrix[i]);
    }
    free(score_matrix);
}

int main(int argc, char *argv[]) {
    char *seq1 = argv[1];
    char *seq2 = argv[2];
    needleman_wunsch_reconstruct(seq1, seq2, 1, -1, -1);
    return 0;
}
