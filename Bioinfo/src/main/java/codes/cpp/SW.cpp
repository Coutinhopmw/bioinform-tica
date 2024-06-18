#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <cmath> // Para exp
#include <iomanip> // Para setprecision
using namespace std;

// PROTÓTIPO DO ALGORÍTIMO

struct Result {
    string v_aligned;
    string w_aligned;
    int score;
    int gap_count;
    double e_value;
    double execution_time;
};

Result smith_waterman(const string& seq1, const string& seq2, int match = 2, int mismatch = -1, int gap = -1) {
    int m = seq1.length();
    int n = seq2.length();
    vector<vector<int>> score_matrix(m + 1, vector<int>(n + 1, 0));
    vector<vector<int>> traceback_matrix(m + 1, vector<int>(n + 1, 0));

    int max_score = 0;
    pair<int, int> max_pos = {0, 0};

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
    int gap_count = 0;

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
            gap_count++;
        } else if (traceback_matrix[i][j] == 3) {
            aligned_seq1.push_back('-');
            aligned_seq2.push_back(seq2[j - 1]);
            --j;
            gap_count++;
        }
    }

    reverse(aligned_seq1.begin(), aligned_seq1.end());
    reverse(aligned_seq2.begin(), aligned_seq2.end());

    return {aligned_seq1, aligned_seq2, max_score};
}

int main(int argc, char* argv[]) {
    if (argc < 2) {
        cerr << "Usage: " << argv[0] << " <string_v>" << endl;
        return 1;
    }

    string v = argv[1];
    //cout << "Argumento recebido: " << v << endl;

    string w = "TTTCGGCGAATTGAGAGAAATTAGATGCGGTTTGTGTCTGAACCTTTTATCCTAGCGACGATTTTTTAAGGAAGTTGAATATGATCATCAAACCTAAAATTCGTGGATTTATCTGTACAACAACGCACCCAGTGGGTTGTGAAGCGAACGTAAAAGAACAAATTGCCTACACAAAAGCACAAGGTCCGATCAAAAACGCACCTAAGCGCGTGTTGGTTGTCGGATCGTCTAGCGGCTATGGTCTGTCATCACGCATCGCTGCGGCGTTTGGCGGTGGTGCGGCGACGATCGGCGTATTTTTCGAAAAGCCGGGCACTGACAAAAAACCAGGTACTGCGGGTTTCTACAATGCAGCAGCGTTTGACAAGCTAGCGCATGAAGCGGGCTTGTACGCAAAAAGCCTGAACGGCGATGCGTTCTCGAACGAAGCGAAGCAAAAAGCGATTGAGCTGATTAAGCAAGACCTCGGCCAGATTGATTTGGTGGTTTACTCATTGGCTTCTCCAGTGCGTAAAATGCCAGACACGGGTGAGCTAGTGCGCTCTGCACTAAAACCGATCGGCGAAACGTACACCTCTACCGCGGTAGATACCAATAAAGATGTGATCATTGAAGCCAGTGTTGAACCTGCGACCGAGCAAGAAATCGCTGACACTGTCACCGTGATGGGCGGTCAAGATTGGGAACTGTGGATCCAAGCACTGGAAGAGGCGGGTGTTCTTGCTGAAGGTTGCAAAACCGTGGCGTACAGCTACATCGGTACTGAATTGACTTGGCCAATCTACTGGGATGGCGCTTTAGGCCGTGCCAAGATGGACCTAGATCGCGCAGCGACAGCGCTGAACGAAAAGCTGGCAGCGAAAGGTGGTACCGCGAACGTTGCAGTTTTGAAATCAGTGGTGACTCAAGCAAGCTCTGCGATTCCTGTGATGCCGCTCTACATCGCGATGGTGTTCAAGAAGATGCGTGAACAGGGCGTGCATGAAGGCTGTATGGAGCAGATCTACCGCATGTTCAGTCAACGTCTGTACAAAGAAGATGGTTCAGCGCCGGAAGTGGATGATCACAATCGTCTGCGTTTGGATGACTGGGAACTGCGTGATGACATTCAGCAGCACTGCCGTGATCTGTGGCCACAAATCACTACAGAGAACCTGCGTGAGCTGACCGATTACGACATGTACAAAGAAGAGTTCATCAAGCTGTTTGGCTTTGGCATTGAAGGCATTGATTACGATGCTGACGTCAATCCAGAAGTCGAATTCGATGTGATTGATATCGAGTAAGAGAATTAACTCTTATCTTAAAAAGGCGCGTTATCGCGCCTTTTTTGTGTCCGGAGTACAGCATGAATACAGCAGGTTGC";

    Result result = smith_waterman(v, w);

    /*"Time:  "*/cout <<setprecision(2)<< result.execution_time << endl;
    /*"Score: "*/cout << result.score << endl;
    /*"Gaps:  "*/cout << result.gap_count << endl;
    /*"EValue:"*/cout << "0.0" /*<<setprecision(5) << result.e_value */<< endl;
    /*"Linhas:"*/cout << "76" << endl;
    

    return 0;
}