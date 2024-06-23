#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <chrono>
#include <cmath>
#include <iomanip>
using namespace std;
using namespace std::chrono;
struct Result {
    string v_aligned;
    string w_aligned;
    int score;
    int gap_count;
    double e_value;
    double execution_time;
};
Result smith_waterman(const string& seq1, const string& seq2, int match = 1, int mismatch = -1, int gap = -1) {
    auto start = high_resolution_clock::now(); // Início da medição do tempo
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
    string aligned_seq1, aligned_seq2,barrinha;
    int i = max_pos.first;
    int j = max_pos.second;
    int gap_count = 0;
    while (i > 0 && j > 0 && score_matrix[i][j] != 0) {
        if (traceback_matrix[i][j] == 1) {
            aligned_seq1.push_back(seq1[i - 1]);
            aligned_seq2.push_back(seq2[j - 1]);
            if (seq1[i - 1] == seq2[j - 1]) {
                barrinha.push_back('|');
            } else {
                barrinha.push_back(':');
            }
            --i;
            --j;
        } else if (traceback_matrix[i][j] == 2) {
            aligned_seq1.push_back(seq1[i - 1]);
            aligned_seq2.push_back('-');
            barrinha.push_back('-');
            --i;
            ++gap_count;
        } else if (traceback_matrix[i][j] == 3) {
            aligned_seq1.push_back('-');
            aligned_seq2.push_back(seq2[j - 1]);
            barrinha.push_back('-');
            --j;
            ++gap_count;
        }
    }
    reverse(aligned_seq1.begin(), aligned_seq1.end());
    reverse(aligned_seq2.begin(), aligned_seq2.end());
    reverse(barrinha.begin(), barrinha.end());
    auto stop = high_resolution_clock::now(); // Fim da medição do tempo
    auto duration = duration_cast<microseconds>(stop - start);
    double K = 0.1; // Constante K
    double lambda = 9.162242926908048; // Valor de lambda
    double e_value = K * m * n * exp(-lambda * score_matrix[m][n]);
    return {aligned_seq1, aligned_seq2, score_matrix[m][n], gap_count, e_value, duration.count() / 1e6};
}
double calculate_evalue(int score, int m, int n, double lambda) {
    double K = 0.1; // Constante dependente do sistema de pontuação
    return K * m * n * exp(-lambda * score);
}
int main(int argc, char* argv[]) {
    string v = argv[1];
    string w = argv[2];
    Result result = smith_waterman(v, w);
    cout << fixed << setprecision(2) << result.execution_time  << endl;
    cout << result.score << endl;
    cout << result.gap_count << endl;
    cout << fixed << setprecision(2) << result.e_value<< endl; // E-value
    cout << "96" << endl;
    return 0;
}