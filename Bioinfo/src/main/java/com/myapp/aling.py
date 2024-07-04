import time
import sys
def score(char1, char2):
  if char1==char2:
    return 1
  else:
    return -1

def needleman_wunsch_reconstruct(seq1, seq2, match_score, mismatch_penalty, gap_penalty):
    # Cria a matriz de pontuação
    score_matrix = [[0 for _ in range(len(seq2) + 1)] for _ in range(len(seq1) + 1)]

    # Preenche a primeira linha e coluna da matriz com gap penalties
    for i in range(len(seq1) + 1):
        score_matrix[i][0] = gap_penalty * i
    for j in range(len(seq2) + 1):
        score_matrix[0][j] = gap_penalty * j

    # Calcula a pontuação de alinhamento para cada subsequência
    for i in range(1, len(seq1) + 1):
        for j in range(1, len(seq2) + 1):
            match_score_ij = score_matrix[i - 1][j - 1] + score(seq1[i - 1], seq2[j - 1])
            delete_seq1_ij = score_matrix[i - 1][j] + gap_penalty
            delete_seq2_ij = score_matrix[i][j - 1] + gap_penalty
            score_matrix[i][j] = max(match_score_ij, delete_seq1_ij, delete_seq2_ij)

    # Inicialização do alinhamento
    alignment_seq1 = ''

    # Inicialização do índice para percorrer a tabela de pontuação
    i = len(seq1)
    j = len(seq2)

    # Loop para percorrer a tabela de pontuação e reconstruir o alinhamento
    while i > 0 and j > 0:
        if score_matrix[i][j] == score_matrix[i - 1][j - 1] + score(seq1[i - 1], seq2[j - 1]):
            # Diagonal: correspondência ou mismatch
            alignment_seq1 = seq1[i - 1] + alignment_seq1
            i -= 1
            j -= 1
        elif score_matrix[i][j] == score_matrix[i - 1][j] + gap_penalty:
            # Para cima: lacuna na segunda sequência
            alignment_seq1 = seq1[i - 1] + alignment_seq1
            i -= 1
        else:
            # Para a esquerda: lacuna na primeira sequência
            alignment_seq1 = '-' + alignment_seq1
            j -= 1

    # Lidar com lacunas restantes, se houver
    while i > 0:
        alignment_seq1 = seq1[i - 1] + alignment_seq1
        i -= 1
    while j > 0:
        alignment_seq1 = '-' + alignment_seq1
        j -= 1

    # Exibir o alinhamento
    print(alignment_seq1)

needleman_wunsch_reconstruct(sys.argv[1], sys.argv[2], 1, -1, -1)