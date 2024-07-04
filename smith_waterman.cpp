#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <cmath>
#include <chrono>

using namespace std;
using namespace std::chrono;

struct Result {
    string aligned_seq1;
    string aligned_seq2;
    int max_score;
    int gap_count;
    double e_value;
    double execution_time;
};

Result smith_waterman(const string& seq1, const string& seq2, int match = 2, int mismatch = -1, int gap = -1) {
    auto start = high_resolution_clock::now(); // Início da medição do tempo

    int m = seq1.length();
    int n = seq2.length();
    vector<vector<int>> score_matrix(m + 1, vector<int>(n + 1, 0));
    vector<vector<int>> traceback_matrix(m + 1, vector<int>(n + 1, 0));

    int max_score = 0;
    pair<int, int> max_pos = {0, 0};
    int gap_count = 0;

    for (int i = 1; i <= m; ++i) {
        for (int j = 1; j <= n; ++j) {
            int match_score = score_matrix[i - 1][j - 1] + (seq1[i - 1] == seq2[j - 1] ? match : mismatch);
            int delete_score = score_matrix[i - 1][j] + gap;
            int insert_score = score_matrix[i][j - 1] + gap;
            score_matrix[i][j] = max({0, match_score, delete_score, insert_score});

            if (score_matrix[i][j] == match_score) {
                traceback_matrix[i][j] = 1; // Diagonal
            } else if (score_matrix[i][j] == delete_score) {
                traceback_matrix[i][j] = 2; // Up
            } else if (score_matrix[i][j] == insert_score) {
                traceback_matrix[i][j] = 3; // Left
            }

            if (score_matrix[i][j] > max_score) {
                max_score = score_matrix[i][j];
                max_pos = {i, j};
            }
        }
    }

    string aligned_seq1, aligned_seq2;
    int i = max_pos.first;
    int j = max_pos.second;

    while (score_matrix[i][j] != 0) {
        if (traceback_matrix[i][j] == 1) {
            aligned_seq1.push_back(seq1[i - 1]);
            aligned_seq2.push_back(seq2[j - 1]);
            --i;
            --j;
        } else if (traceback_matrix[i][j] == 2) {
            aligned_seq1.push_back(seq1[i - 1]);
            aligned_seq2.push_back('-');
            --i;
            ++gap_count;
        } else if (traceback_matrix[i][j] == 3) {
            aligned_seq1.push_back('-');
            aligned_seq2.push_back(seq2[j - 1]);
            --j;
            ++gap_count;
        }
    }

    reverse(aligned_seq1.begin(), aligned_seq1.end());
    reverse(aligned_seq2.begin(), aligned_seq2.end());

    auto stop = high_resolution_clock::now(); // Fim da medição do tempo
    auto duration = duration_cast<microseconds>(stop - start);

    // Cálculo do valor E (E-value)
    double K = 0.1, lambda = 0.1;
    double e_value = K * m * n * exp(-lambda * max_score);

    return {aligned_seq1, aligned_seq2, max_score, gap_count, e_value, duration.count() / 1e6};
}

int main() {
    // Sequências diretamente no código
    string seq1 =
        "ATGGCAGGTCAAGGCATTCCGTTTGTGTTGCCCGAAAGTGTTTGGGAAGGCGTTATTGATTTGTATGTGA"
        "ATCATGGTTGGGGAGCAAATAAAATCCTCAAACATCTTGATTTGCATATTGATCCAAAGACGCTTCTTCG"
        "ACACTTGAAAAAGCGTGGAGTCACCATTCGGAAGTTTGACGAACGTCTCCCCGAACCCTGCAAGGGTTGT"
        "GGGGAAATGTTTGAAAAAGACGCATGGAATCGTCAATTGTGTCTGACTTGTGCGCCCACCCAAGCGTGGT"
        "CGCAAACTTTCTACCGTTATGGGATTACAAAGCCAGAGTTTGATAAGAAGCTGGATGAACAGAATTATCT"
        "CTGTGGTCTTTGTGGAAAGCCACTTCCTGCCGACCCCAAGGAAATGCGTATTGATCATTGCCATGAGCAA"
        "GGTCATGTAAGAGATATTCTCCATAACAAGTGCAATATCGGGTTGCATTATGTTGAAGATTCTGAGTTTG"
        "TTGGACTTGCGGTTAAGTACATCGAAAGGCATAAGCTGTGA";

    string seq2 =
        "ATGACCCTGTTCGGGATCGACGTCAGCAACCACCAGAAGCAGTTCGACTTCGCGCGAGCCAAGCGCGAGG"
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

    Result result = smith_waterman(seq1, seq2);

    cout << "Alinhamento:" << endl;
    cout << result.aligned_seq1 << endl;
    cout << result.aligned_seq2 << endl;
    cout << "Pontuação: " << result.max_score << endl;
    cout << "Quantidade de gaps: " << result.gap_count << endl;
    cout << "E-value: " << result.e_value << endl;
    cout << "Tempo de execução: " << result.execution_time << " segundos" << endl;

    return 0;
}
