import time
import sys
import math
def score(char1, char2):
    if char1 == char2:
        return 1
    else:
        return -1
def needleman_wunsch_reconstruct(seq1, seq2, match_score, mismatch_penalty, gap_penalty,):
    inicio = time.perf_counter()
    score_matrix = [[0 for _ in range(len(seq2) + 1)] for _ in range(len(seq1) + 1)]
    for i in range(len(seq1) + 1):
        score_matrix[i][0] = gap_penalty * i
    for j in range(len(seq2) + 1):
        score_matrix[0][j] = gap_penalty * j
    for i in range(1, len(seq1) + 1):
        for j in range(1, len(seq2) + 1):
            match_score_ij = score_matrix[i - 1][j - 1] + score(seq1[i - 1], seq2[j - 1])
            delete_seq1_ij = score_matrix[i - 1][j] + gap_penalty
            delete_seq2_ij = score_matrix[i][j - 1] + gap_penalty
            score_matrix[i][j] = max(match_score_ij, delete_seq1_ij, delete_seq2_ij)
    i, j = len(seq1), len(seq2)
    gaps = 0
    while i > 0 and j > 0:
        if score_matrix[i][j] == score_matrix[i - 1][j - 1] + score(seq1[i - 1], seq2[j - 1]):
            i -= 1
            j -= 1
        elif score_matrix[i][j] == score_matrix[i - 1][j] + gap_penalty:
            i -= 1
            gaps += 1
        else:
            gaps += 1
            j -= 1
    while i > 0:
        gaps += 1
        i -= 1
    while j > 0:
        gaps += 1
        j -= 1
    fim = time.perf_counter()
    alignment_score = score_matrix[len(seq1)][len(seq2)]
    print(round((fim - inicio), 2))
    print(alignment_score)
    print(gaps)
    print("34")
seq1 = sys.argv[1]
seq2 = sys.argv[2]
needleman_wunsch_reconstruct(seq1, seq2, 1, -1, -1)