import time
import sys
def score(char1, char2):
  if char1==char2:
    return 1
  else:
    return -1
def needleman_wunsch_reconstruct(seq1, seq2, match_score, mismatch_penalty, gap_penalty):
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
    i,j = len(seq1),len(seq2)
<<<<<<< HEAD
=======
    
>>>>>>> 6854c619f7ca2976d4aca38f2e540e81e169ed2f
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
<<<<<<< HEAD
    '''Percent:''';print(round(((100*barrinha.count("|"))/len(barrinha)),2))
    '''Time:   ''';print(round((fim - inicio),2))
    '''Score:  ''';print(score_matrix[len(seq1)][len(seq2)])
    '''Gaps:   ''';print(barrinha.count("-"))
    '''EVAlue: ''';print("0.0")
    '''Linhas: ''';print("51")
seq1 = sys.argv[1]
seq2 = "TTTCGGCGAATTGAGAGAAATTAGATGCGGTTTGTGTCTGAACCTTTTATCCTAGCGACGATTTTTTAAGGAAGTTGAATATGATCATCAAACCTAAAATTCGTGGATTTATCTGTACAACAACGCACCCAGTGGGTTGTGAAGCGAACGTAAAAGAACAAATTGCCTACACAAAAGCACAAGGTCCGATCAAAAACGCACCTAAGCGCGTGTTGGTTGTCGGATCGTCTAGCGGCTATGGTCTGTCATCACGCATCGCTGCGGCGTTTGGCGGTGGTGCGGCGACGATCGGCGTATTTTTCGAAAAGCCGGGCACTGACAAAAAACCAGGTACTGCGGGTTTCTACAATGCAGCAGCGTTTGACAAGCTAGCGCATGAAGCGGGCTTGTACGCAAAAAGCCTGAACGGCGATGCGTTCTCGAACGAAGCGAAGCAAAAAGCGATTGAGCTGATTAAGCAAGACCTCGGCCAGATTGATTTGGTGGTTTACTCATTGGCTTCTCCAGTGCGTAAAATGCCAGACACGGGTGAGCTAGTGCGCTCTGCACTAAAACCGATCGGCGAAACGTACACCTCTACCGCGGTAGATACCAATAAAGATGTGATCATTGAAGCCAGTGTTGAACCTGCGACCGAGCAAGAAATCGCTGACACTGTCACCGTGATGGGCGGTCAAGATTGGGAACTGTGGATCCAAGCACTGGAAGAGGCGGGTGTTCTTGCTGAAGGTTGCAAAACCGTGGCGTACAGCTACATCGGTACTGAATTGACTTGGCCAATCTACTGGGATGGCGCTTTAGGCCGTGCCAAGATGGACCTAGATCGCGCAGCGACAGCGCTGAACGAAAAGCTGGCAGCGAAAGGTGGTACCGCGAACGTTGCAGTTTTGAAATCAGTGGTGACTCAAGCAAGCTCTGCGATTCCTGTGATGCCGCTCTACATCGCGATGGTGTTCAAGAAGATGCGTGAACAGGGCGTGCATGAAGGCTGTATGGAGCAGATCTACCGCATGTTCAGTCAACGTCTGTACAAAGAAGATGGTTCAGCGCCGGAAGTGGATGATCACAATCGTCTGCGTTTGGATGACTGGGAACTGCGTGATGACATTCAGCAGCACTGCCGTGATCTGTGGCCACAAATCACTACAGAGAACCTGCGTGAGCTGACCGATTACGACATGTACAAAGAAGAGTTCATCAAGCTGTTTGGCTTTGGCATTGAAGGCATTGATTACGATGCTGACGTCAATCCAGAAGTCGAATTCGATGTGATTGATATCGAGTAAGAGAATTAACTCTTATCTTAAAAAGGCGCGTTATCGCGCCTTTTTTGTGTCCGGAGTACAGCATGAATACAGCAGGTTGC"
needleman_wunsch_reconstruct(seq1, seq2, 1, -1, -1)
=======
    '''Time:''';print(round((fim - inicio),2),"segs")
    '''Score:''';print(score_matrix[len(seq1)][len(seq2)])
    '''Gaps:''';print(barrinha.count("-"))
    '''EVAlue:''';print("0.0")
    '''Linhas:''';print("51")
    # print("aprox:",round(((100*barrinha.count("|"))/len(barrinha)),2),"%")
    # print("match:",barrinha.count("|"))
    # print("mssmatchs:",barrinha.count(":"))
    

#Vibrio cholerae bv. albensis enoyl-acyl carrier protein reductase IV (fabV) gene, complete cds

# GenBank accession number: EU183052.1
# Organism: Vibrio cholerae
# Sequence length: 1366 nucleotides

seq2 = "\
TTTCGGCGAATTGAGAGAAATTAGATGCGGTTTGTGTCTGAACCTTTTATCCTAGCGACGATTTTTTAAG\
GAAGTTGAATATGATCATCAAACCTAAAATTCGTGGATTTATCTGTACAACAACGCACCCAGTGGGTTGT\
GAAGCGAACGTAAAAGAACAAATTGCCTACACAAAAGCACAAGGTCCGATCAAAAACGCACCTAAGCGCG\
TGTTGGTTGTCGGATCGTCTAGCGGCTATGGTCTGTCATCACGCATCGCTGCGGCGTTTGGCGGTGGTGC\
GGCGACGATCGGCGTATTTTTCGAAAAGCCGGGCACTGACAAAAAACCAGGTACTGCGGGTTTCTACAAT\
GCAGCAGCGTTTGACAAGCTAGCGCATGAAGCGGGCTTGTACGCAAAAAGCCTGAACGGCGATGCGTTCT\
CGAACGAAGCGAAGCAAAAAGCGATTGAGCTGATTAAGCAAGACCTCGGCCAGATTGATTTGGTGGTTTA\
CTCATTGGCTTCTCCAGTGCGTAAAATGCCAGACACGGGTGAGCTAGTGCGCTCTGCACTAAAACCGATC\
GGCGAAACGTACACCTCTACCGCGGTAGATACCAATAAAGATGTGATCATTGAAGCCAGTGTTGAACCTG\
CGACCGAGCAAGAAATCGCTGACACTGTCACCGTGATGGGCGGTCAAGATTGGGAACTGTGGATCCAAGC\
ACTGGAAGAGGCGGGTGTTCTTGCTGAAGGTTGCAAAACCGTGGCGTACAGCTACATCGGTACTGAATTG\
ACTTGGCCAATCTACTGGGATGGCGCTTTAGGCCGTGCCAAGATGGACCTAGATCGCGCAGCGACAGCGC\
TGAACGAAAAGCTGGCAGCGAAAGGTGGTACCGCGAACGTTGCAGTTTTGAAATCAGTGGTGACTCAAGC\
AAGCTCTGCGATTCCTGTGATGCCGCTCTACATCGCGATGGTGTTCAAGAAGATGCGTGAACAGGGCGTG\
CATGAAGGCTGTATGGAGCAGATCTACCGCATGTTCAGTCAACGTCTGTACAAAGAAGATGGTTCAGCGC\
CGGAAGTGGATGATCACAATCGTCTGCGTTTGGATGACTGGGAACTGCGTGATGACATTCAGCAGCACTG\
CCGTGATCTGTGGCCACAAATCACTACAGAGAACCTGCGTGAGCTGACCGATTACGACATGTACAAAGAA\
GAGTTCATCAAGCTGTTTGGCTTTGGCATTGAAGGCATTGATTACGATGCTGACGTCAATCCAGAAGTCG\
AATTCGATGTGATTGATATCGAGTAAGAGAATTAACTCTTATCTTAAAAAGGCGCGTTATCGCGCCTTTT\
TTGTGTCCGGAGTACAGCATGAATACAGCAGGTTGC"

seq1 = sys.argv[1]

needleman_wunsch_reconstruct(seq1, seq2, 1, -1, -1)
>>>>>>> 6854c619f7ca2976d4aca38f2e540e81e169ed2f
