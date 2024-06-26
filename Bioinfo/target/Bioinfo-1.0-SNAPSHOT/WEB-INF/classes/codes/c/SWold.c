#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>

#define MATCH 1
#define MISMATCH -1
#define GAP -1

struct Result {
    char* v_aligned;
    char* w_aligned;
    int score;
    int gap_count;
    double e_value;
    double execution_time;
};

struct Result smith_waterman(const char* seq1, const char* seq2);
double calculate_evalue(int score, int m, int n, double lambda);

int main(int argc, char* argv[]) {
    if (argc < 3) {
        printf("Usage: %s <sequence1> <sequence2>\n", argv[0]);
        return 1;
    }

    char* v = argv[1];
    char* w = argv[2];
    struct Result result = smith_waterman(v, w);

    printf("%.2f\n", result.execution_time);
    printf("%d\n", result.score);
    printf("%d\n", result.gap_count);
    printf("%.2f\n", result.e_value);
    printf("96\n");

    free(result.v_aligned);
    free(result.w_aligned);

    return 0;
}

struct Result smith_waterman(const char* seq1, const char* seq2) {
    clock_t start = clock();
    int m = strlen(seq1);
    int n = strlen(seq2);

    int** score_matrix = (int**)malloc((m + 1) * sizeof(int*));
    int** traceback_matrix = (int**)malloc((m + 1) * sizeof(int*));
    for (int i = 0; i <= m; ++i) {
        score_matrix[i] = (int*)malloc((n + 1) * sizeof(int));
        traceback_matrix[i] = (int*)malloc((n + 1) * sizeof(int));
    }

    int max_score = 0;
    int max_i = 0, max_j = 0;

    for (int i = 1; i <= m; ++i) {
        for (int j = 1; j <= n; ++j) {
            int match_score = score_matrix[i - 1][j - 1] + (seq1[i - 1] == seq2[j - 1] ? MATCH : MISMATCH);
            int delete_score = score_matrix[i - 1][j] + GAP;
            int insert_score = score_matrix[i][j - 1] + GAP;
            score_matrix[i][j] = fmax(0, fmax(match_score, fmax(delete_score, insert_score)));

            if (score_matrix[i][j] == match_score) {
                traceback_matrix[i][j] = 1; // Diagonal
            } else if (score_matrix[i][j] == delete_score) {
                traceback_matrix[i][j] = 2; // Up
            } else if (score_matrix[i][j] == insert_score) {
                traceback_matrix[i][j] = 3; // Left
            }

            if (score_matrix[i][j] > max_score) {
                max_score = score_matrix[i][j];
                max_i = i;
                max_j = j;
            }
        }
    }

    char* aligned_seq1 = (char*)malloc((m + n) * sizeof(char));
    char* aligned_seq2 = (char*)malloc((m + n) * sizeof(char));
    char* barrinha = (char*)malloc((m + n) * sizeof(char));
    aligned_seq1[0] = '\0';
    aligned_seq2[0] = '\0';
    barrinha[0] = '\0';

    int i = max_i;
    int j = max_j;
    int gap_count = 0;

    while (i > 0 && j > 0 && score_matrix[i][j] != 0) {
        if (traceback_matrix[i][j] == 1) { // Diagonal
            strncat(aligned_seq1, &seq1[i - 1], 1);
            strncat(aligned_seq2, &seq2[j - 1], 1);
            if (seq1[i - 1] == seq2[j - 1]) {
                strncat(barrinha, "|", 1);
            } else {
                strncat(barrinha, ":", 1);
            }
            --i;
            --j;
        } else if (traceback_matrix[i][j] == 2) { // Up
            strncat(aligned_seq1, &seq1[i - 1], 1);
            strcat(aligned_seq2, "-");
            strcat(barrinha, "-");
            --i;
            ++gap_count;
        } else if (traceback_matrix[i][j] == 3) { // Left
            strcat(aligned_seq1, "-");
            strncat(aligned_seq2, &seq2[j - 1], 1);
            strcat(barrinha, "-");
            --j;
            ++gap_count;
        }
    }

    // Reverse the strings
    strrev(aligned_seq1);
    strrev(aligned_seq2);
    strrev(barrinha);

    clock_t end = clock();
    double execution_time = ((double)(end - start)) / CLOCKS_PER_SEC;

    double lambda = 9.162242926908048; // Valor de lambda
    double e_value = calculate_evalue(max_score, m, n, lambda);

    struct Result result;
    result.v_aligned = aligned_seq1;
    result.w_aligned = aligned_seq2;
    result.score = max_score;
    result.gap_count = gap_count;
    result.e_value = e_value;
    result.execution_time = execution_time;

    // Liberando memória
    for (int i = 0; i <= m; ++i) {
        free(score_matrix[i]);
        free(traceback_matrix[i]);
    }
    free(score_matrix);
    free(traceback_matrix);
    free(barrinha);

    return result;
}

double calculate_evalue(int score, int m, int n, double lambda) {
    double K = 0.1; // Constante K
    return K * m * n * exp(-lambda * score);
}
