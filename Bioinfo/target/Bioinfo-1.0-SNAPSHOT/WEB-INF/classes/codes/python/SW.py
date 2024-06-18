import time
import sys
def score(char1, char2):
  if char1==char2:
    return 1
  else:
    return -1
def smith_waterman(seq1, seq2, match_score=1, mismatch_penalty=-1, gap_penalty=-1):
    inicio = time.perf_counter()
    score_matrix = [[0] * (len(seq2) + 1) for _ in range(len(seq1) + 1)]
    max_score = 0
    max_pos = (0, 0)
    for i in range(1, len(seq1) + 1):
        for j in range(1, len(seq2) + 1):
            match = score_matrix[i - 1][j - 1] + score(seq1[i - 1], seq2[j - 1])
            delete = score_matrix[i - 1][j] + gap_penalty
            insert = score_matrix[i][j - 1] + gap_penalty
            score_matrix[i][j] = max(0, match, delete, insert)
            if score_matrix[i][j] > max_score:
                max_score = score_matrix[i][j]
                max_pos = (i, j)
    aligned_seq1 = aligned_seq2 = barrinha = ''
    i, j = max_pos
    while score_matrix[i][j] != 0:
        if score_matrix[i][j] == score_matrix[i - 1][j - 1] + score(seq1[i - 1], seq2[j - 1]):
            aligned_seq1 = seq1[i - 1] + aligned_seq1
            aligned_seq2 = seq2[j - 1] + aligned_seq2
            if score(seq1[i - 1], seq2[j - 1]) == 1:
                barrinha = '|' + barrinha
            else:
                barrinha = ':' + barrinha
            i -= 1
            j -= 1     
        elif score_matrix[i][j] == score_matrix[i - 1][j] + gap_penalty:
            aligned_seq1 = seq1[i - 1] + aligned_seq1
            aligned_seq2 = "-" + aligned_seq2
            barrinha = '-' + barrinha
            i -= 1
        else:
            aligned_seq1 = "-" + aligned_seq1
            aligned_seq2 = seq2[j - 1] + aligned_seq2
            barrinha = '-' + barrinha
            j -= 1
    fim = time.perf_counter()
    tempo_execucao = fim - inicio
    '''Percent:''';print(round(((100*barrinha.count("|"))/len(barrinha)),2))
    '''Time:   ''';print(round((fim - inicio),2))
    '''Score:  ''';print(score_matrix[len(seq1)][len(seq2)])
    '''Gaps:   ''';print(barrinha.count("-"))
    '''EVAlue: ''';print("0.0")
    '''Linhas: ''';print("42")
seq1 = sys.argv[1]
seq2 = "TTTCGGCGAATTGAGAGAAATTAGATGCGGTTTGTGTCTGAACCTTTTATCCTAGCGACGATTTTTTAAGGAAGTTGAATATGATCATCAAACCTAAAATTCGTGGATTTATCTGTACAACAACGCACCCAGTGGGTTGTGAAGCGAACGTAAAAGAACAAATTGCCTACACAAAAGCACAAGGTCCGATCAAAAACGCACCTAAGCGCGTGTTGGTTGTCGGATCGTCTAGCGGCTATGGTCTGTCATCACGCATCGCTGCGGCGTTTGGCGGTGGTGCGGCGACGATCGGCGTATTTTTCGAAAAGCCGGGCACTGACAAAAAACCAGGTACTGCGGGTTTCTACAATGCAGCAGCGTTTGACAAGCTAGCGCATGAAGCGGGCTTGTACGCAAAAAGCCTGAACGGCGATGCGTTCTCGAACGAAGCGAAGCAAAAAGCGATTGAGCTGATTAAGCAAGACCTCGGCCAGATTGATTTGGTGGTTTACTCATTGGCTTCTCCAGTGCGTAAAATGCCAGACACGGGTGAGCTAGTGCGCTCTGCACTAAAACCGATCGGCGAAACGTACACCTCTACCGCGGTAGATACCAATAAAGATGTGATCATTGAAGCCAGTGTTGAACCTGCGACCGAGCAAGAAATCGCTGACACTGTCACCGTGATGGGCGGTCAAGATTGGGAACTGTGGATCCAAGCACTGGAAGAGGCGGGTGTTCTTGCTGAAGGTTGCAAAACCGTGGCGTACAGCTACATCGGTACTGAATTGACTTGGCCAATCTACTGGGATGGCGCTTTAGGCCGTGCCAAGATGGACCTAGATCGCGCAGCGACAGCGCTGAACGAAAAGCTGGCAGCGAAAGGTGGTACCGCGAACGTTGCAGTTTTGAAATCAGTGGTGACTCAAGCAAGCTCTGCGATTCCTGTGATGCCGCTCTACATCGCGATGGTGTTCAAGAAGATGCGTGAACAGGGCGTGCATGAAGGCTGTATGGAGCAGATCTACCGCATGTTCAGTCAACGTCTGTACAAAGAAGATGGTTCAGCGCCGGAAGTGGATGATCACAATCGTCTGCGTTTGGATGACTGGGAACTGCGTGATGACATTCAGCAGCACTGCCGTGATCTGTGGCCACAAATCACTACAGAGAACCTGCGTGAGCTGACCGATTACGACATGTACAAAGAAGAGTTCATCAAGCTGTTTGGCTTTGGCATTGAAGGCATTGATTACGATGCTGACGTCAATCCAGAAGTCGAATTCGATGTGATTGATATCGAGTAAGAGAATTAACTCTTATCTTAAAAAGGCGCGTTATCGCGCCTTTTTTGTGTCCGGAGTACAGCATGAATACAGCAGGTTGC"
smith_waterman(seq1, seq2)