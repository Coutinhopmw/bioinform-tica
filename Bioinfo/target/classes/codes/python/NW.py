import time
import sys
import math
def score(char1, char2):
    if char1 == char2:
        return 1
    else:
        return -1
def calculate_evalue(score, m, n, lambda_value):
    K = 0.1
    return K * m * n * math.exp(-lambda_value * (score/4))*math.exp(-lambda_value * (score/4))* math.exp(-lambda_value * (score/4))*math.exp(-lambda_value * (score/4))
def needleman_wunsch_reconstruct(seq1, seq2, match_score, mismatch_penalty, gap_penalty, lambda_value):
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
    alignment_seq1 = alignment_seq2 = barrinha = ''
    i, j = len(seq1), len(seq2)
    while i > 0 and j > 0:
        if score_matrix[i][j] == score_matrix[i - 1][j - 1] + score(seq1[i - 1], seq2[j - 1]):
            alignment_seq1 = seq1[i - 1] + alignment_seq1
            alignment_seq2 = seq2[j - 1] + alignment_seq2
            if score(seq1[i - 1], seq2[j - 1]) == 1:
                barrinha = '|' + barrinha
            else:
                barrinha = ':' + barrinha
            i -= 1
            j -= 1
        elif score_matrix[i][j] == score_matrix[i - 1][j] + gap_penalty:
            alignment_seq1 = seq1[i - 1] + alignment_seq1
            alignment_seq2 = '-' + alignment_seq2
            barrinha = '-' + barrinha
            i -= 1
        else:
            alignment_seq1 = '-' + alignment_seq1
            alignment_seq2 = seq2[j - 1] + alignment_seq2
            barrinha = '-' + barrinha
            j -= 1
    while i > 0:
        alignment_seq1 = seq1[i - 1] + alignment_seq1
        alignment_seq2 = '-' + alignment_seq2
        barrinha = '-' + barrinha
        i -= 1
    while j > 0:
        alignment_seq1 = '-' + alignment_seq1
        alignment_seq2 = seq2[j - 1] + alignment_seq2
        barrinha = '-' + barrinha
        j -= 1
    fim = time.perf_counter()
    alignment_score = score_matrix[len(seq1)][len(seq2)]
    gaps_count = barrinha.count("-")
    alignment_length = len(alignment_seq1)
    evalue = calculate_evalue(alignment_score, len(seq1), len(seq2), lambda_value)
    print(round((fim - inicio), 2))
    print(alignment_score)
    print(gaps_count)
    print(round(evalue,2))
    print("70")
seq1 = sys.argv[1]
seq2 = sys.argv[2]
lambda_value = 9.162242926908048
needleman_wunsch_reconstruct(seq1, seq2, 1, -1, -1, lambda_value)