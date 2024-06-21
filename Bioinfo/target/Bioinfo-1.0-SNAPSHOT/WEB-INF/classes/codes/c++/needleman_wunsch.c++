#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <iomanip> // Inclui a biblioteca para definir a precisão dos números

using namespace std;

// PROTÓTIPO DO ALGORÍTIMO

struct Result {
    string v_aligned;
    string w_aligned;
    int score;
};

Result needleman_wunsch(const string& v, const string& w, int match_score = 1, int mismatch_score = -1, int gap_penalty = -1) {
    int m = v.size();
    int n = w.size();
    vector<vector<int>> score_matrix(m + 1, vector<int>(n + 1, 0));
    vector<vector<int>> direction_matrix(m + 1, vector<int>(n + 1, 0));

    for (int i = 1; i <= m; ++i) {
        score_matrix[i][0] = i * gap_penalty;
        direction_matrix[i][0] = 1; // Acima
    }
    for (int j = 1; j <= n; ++j) {
        score_matrix[0][j] = j * gap_penalty;
        direction_matrix[0][j] = 2; // Esquerda
    }

    for (int i = 1; i <= m; ++i) {
        for (int j = 1; j <= n; ++j) {
            int match = score_matrix[i - 1][j - 1] + (v[i - 1] == w[j - 1] ? match_score : mismatch_score);
            int delete_score = score_matrix[i - 1][j] + gap_penalty;
            int insert_score = score_matrix[i][j - 1] + gap_penalty;
            score_matrix[i][j] = max({match, delete_score, insert_score});

            if (score_matrix[i][j] == match) {
                direction_matrix[i][j] = 3; // Diagonal
            } else if (score_matrix[i][j] == delete_score) {
                direction_matrix[i][j] = 1; // Acima
            } else {
                direction_matrix[i][j] = 2; // Esquerda
            }
        }
    }

    string v_aligned, w_aligned;
    int i = m, j = n;
    while (i > 0 || j > 0) {
        if (direction_matrix[i][j] == 3) {
            v_aligned = v[i - 1] + v_aligned;
            w_aligned = w[j - 1] + w_aligned;
            --i;
            --j;
        } else if (direction_matrix[i][j] == 1) {
            v_aligned = v[i - 1] + v_aligned;
            w_aligned = '-' + w_aligned;
            --i;
        } else {
            v_aligned = '-' + v_aligned;
            w_aligned = w[j - 1] + w_aligned;
            --j;
        }
    }

    return {v_aligned, w_aligned, score_matrix[m][n]};
}

int main() {
    string v = "ATGGCAGGTCAAGGCATTCCGTTTGTGTTGCCCGAAAGTGTTTGGGAAGGCGTTATTGATTTGTATGTGA"
               "ATCATGGTTGGGGAGCAAATAAAATCCTCAAACATCTTGATTTGCATATTGATCCAAAGACGCTTCTTCG"
               "ACACTTGAAAAAGCGTGGAGTCACCATTCGGAAGTTTGACGAACGTCTCCCCGAACCCTGCAAGGGTTGT"
               "GGGGAAATGTTTGAAAAAGACGCATGGAATCGTCAATTGTGTCTGACTTGTGCGCCCACCCAAGCGTGGT"
               "CGCAAACTTTCTACCGTTATGGGATTACAAAGCCAGAGTTTGATAAGAAGCTGGATGAACAGAATTATCT"
               "CTGTGGTCTTTGTGGAAAGCCACTTCCTGCCGACCCCAAGGAAATGCGTATTGATCATTGCCATGAGCAA"
               "GGTCATGTAAGAGATATTCTCCATAACAAGTGCAATATCGGGTTGCATTATGTTGAAGATTCTGAGTTTG"
               "TTGGACTTGCGGTTAAGTACATCGAAAGGCATAAGCTGTGA";
    string w = "ATGACCCTGTTCGGGATCGACGTCAGCAACCACCAGAAGCAGTTCGACTTCGCGCGAGCCAAGCGCGAGG"
               "GCATGGTCTTCGCCACCCACAAGATCACCGAAGGTGACTACTACCGCGACACCTACTGGGCGCGCGCCAA"
               "GGCCGAGATGAAGCGCGAGTTCCCCGGCCTCTGGGGCGGGTACGTCTTCTGTCGGCGCGCCTCGCACCCC"
               "AAGCGCGAGGCCGAACTGCTTGCCTCGCACGCGGGCTCGACCGACTTCCCGCTGCAGATCGACTACGAGG"
               "ACACCGATGGCGGCGGCAGCTACGACGACATGGTGGCCCGCTACGAGGCCTATCGTGCGGTCGGCTTCAC"
               "CCAGTTCCTGCCCATCTACCTGCCGCGCTGGTTCTGGCAAGGGCGCATGGGTGCTCCCGACCTTCGTGAC"
               "TTCCCTCTGCCGGTGTGGAACTCCGACTATGGCAGCAGCCGGGCGGGCAACTACAAGGCCATCTACCCCG"
               "GCGATGACAACGGGGGATGGGCGAGCTTCAAGGGGAAGCCCGTCGCACTTCTGCAGTACACCGAGCGCGG"
               "CAATGTCGCTGGACAAGCCATCGACGTCAACGCTTTTCGGGGAAGTATGCAAGACTTACAAGAACTCTTT"
               "GGAGGAGGAAAAGATATGGCTGTAGAGCACGAGATCCTGCGTCAGGAGACCGGCTCGCTGGAGGTCGGAA"
               "AGTTCCCCGGTCACAAGACCCGTCGCCATGAGGGCAGCGTGCAGGGGTCCTTCACGCAGACCGACCTCAT"
               "GCGTGAGATGGACCGAGAACTCAACAGCGTCTTCAGTCTCGAAGGACGCCCGGTGGACCCCAGCAAGGGC"
               "GACACTGTGGTCGGCCACGTCCTCAGCCTGCGGGCCGAGGTCGCCGAACTCCGCAAGCTCATCGAGGAGA"
               "AGCTCAAGTGA";

    Result result = needleman_wunsch(v, w);

    cout << "Alinhamento de v:\n" << result.v_aligned << endl;
    cout << "Alinhamento de w:\n" << result.w_aligned << endl;
    cout << "Pontuação: " << result.score << endl;

    return 0;
}
