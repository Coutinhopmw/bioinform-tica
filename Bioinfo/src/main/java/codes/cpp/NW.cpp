#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <chrono>
#include <cmath> // Para exp
#include <iomanip> // Para setprecision

using namespace std;
using namespace std::chrono;

struct Result {
    string v_aligned;
    string w_aligned;
    int score;
    int gap_count;
    double e_value;
    double execution_time;
    double percent;
};

Result needleman_wunsch(const string& v, const string& w, int match_score = 1, int mismatch_score = -1, int gap_penalty = -1) {
    auto start = high_resolution_clock::now(); // Início da medição do tempo

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

    string v_aligned, w_aligned, barrinha;
    int i = m, j = n;
    int gap_count = 0;

    while (i > 0 || j > 0) {
        if (direction_matrix[i][j] == 3) {
            v_aligned = v[i - 1] + v_aligned;
            w_aligned = w[j - 1] + w_aligned;
            --i;
            --j;
            if (v[i] == w[j]) {
                barrinha = '|' + barrinha;
            } else {
                barrinha = ':' + barrinha;
            }
        } else if (direction_matrix[i][j] == 1) {
            v_aligned = v[i - 1] + v_aligned;
            w_aligned = '-' + w_aligned;
            --i;
            gap_count++;
            barrinha = '-' + barrinha;
        } else {
            v_aligned = '-' + v_aligned;
            w_aligned = w[j - 1] + w_aligned;
            --j;
            gap_count++;
            barrinha = '-' + barrinha;
        }
    }

    double percent = (100.0 * count(barrinha.begin(), barrinha.end(), '|')) / barrinha.length();
    auto stop = high_resolution_clock::now(); // Fim da medição do tempo
    auto duration = duration_cast<microseconds>(stop - start);
    double K = 0.1, lambda = 0.1;
    double e_value = K * m * n * exp(-lambda * score_matrix[m][n]);

    return {v_aligned, w_aligned, score_matrix[m][n], gap_count, e_value, duration.count() / 1e6, percent};
}

int main(int argc, char* argv[]) {
    if (argc < 2) {
        cerr << "Usage: " << argv[0] << " <string_v>" << endl;
        return 1;
    }

    string v = argv[1];
    string w = "TTTCGGCGAATTGAGAGAAATTAGATGCGGTTTGTGTCTGAACCTTTTATCCTAGCGACGATTTTTTAAGGAAGTTGAATATGATCATCAAACCTAAAATTCGTGGATTTATCTGTACAACAACGCACCCAGTGGGTTGTGAAGCGAACGTAAAAGAACAAATTGCCTACACAAAAGCACAAGGTCCGATCAAAAACGCACCTAAGCGCGTGTTGGTTGTCGGATCGTCTAGCGGCTATGGTCTGTCATCACGCATCGCTGCGGCGTTTGGCGGTGGTGCGGCGACGATCGGCGTATTTTTCGAAAAGCCGGGCACTGACAAAAAACCAGGTACTGCGGGTTTCTACAATGCAGCAGCGTTTGACAAGCTAGCGCATGAAGCGGGCTTGTACGCAAAAAGCCTGAACGGCGATGCGTTCTCGAACGAAGCGAAGCAAAAAGCGATTGAGCTGATTAAGCAAGACCTCGGCCAGATTGATTTGGTGGTTTACTCATTGGCTTCTCCAGTGCGTAAAATGCCAGACACGGGTGAGCTAGTGCGCTCTGCACTAAAACCGATCGGCGAAACGTACACCTCTACCGCGGTAGATACCAATAAAGATGTGATCATTGAAGCCAGTGTTGAACCTGCGACCGAGCAAGAAATCGCTGACACTGTCACCGTGATGGGCGGTCAAGATTGGGAACTGTGGATCCAAGCACTGGAAGAGGCGGGTGTTCTTGCTGAAGGTTGCAAAACCGTGGCGTACAGCTACATCGGTACTGAATTGACTTGGCCAATCTACTGGGATGGCGCTTTAGGCCGTGCCAAGATGGACCTAGATCGCGCAGCGACAGCGCTGAACGAAAAGCTGGCAGCGAAAGGTGGTACCGCGAACGTTGCAGTTTTGAAATCAGTGGTGACTCAAGCAAGCTCTGCGATTCCTGTGATGCCGCTCTACATCGCGATGGTGTTCAAGAAGATGCGTGAACAGGGCGTGCATGAAGGCTGTATGGAGCAGATCTACCGCATGTTCAGTCAACGTCTGTACAAAGAAGATGGTTCAGCGCCGGAAGTGGATGATCACAATCGTCTGCGTTTGGATGACTGGGAACTGCGTGATGACATTCAGCAGCACTGCCGTGATCTGTGGCCACAAATCACTACAGAGAACCTGCGTGAGCTGACCGATTACGACATGTACAAAGAAGAGTTCATCAAGCTGTTTGGCTTTGGCATTGAAGGCATTGATTACGATGCTGACGTCAATCCAGAAGTCGAATTCGATGTGATTGATATCGAGTAAGAGAATTAACTCTTATCTTAAAAAGGCGCGTTATCGCGCCTTTTTTGTGTCCGGAGTACAGCATGAATACAGCAGGTTGC";

    Result result = needleman_wunsch(v, w);

    /*"Percent"*/cout << fixed << setprecision(2) << result.percent << endl;
    /*"Time   "*/cout << fixed << setprecision(2) << result.execution_time  << endl;
    /*"Score  "*/cout << result.score << endl;
    /*"Gaps:  "*/cout << result.gap_count << endl;
    /*"EValue:"*/cout << "0.0" /*<< fixed << setprecision(5) << result.e_value */<< endl;
    /*"Linhas:"*/cout << "76" << endl;

    return 0;
}

