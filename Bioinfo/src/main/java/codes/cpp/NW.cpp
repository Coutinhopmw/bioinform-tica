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
    int score;
    int gap_count;
    double execution_time;
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
            --i;
            --j;
        } else if (direction_matrix[i][j] == 1) {
            --i;
            gap_count++;
        } else {
            --j;
            gap_count++;
        }
    }
    auto stop = high_resolution_clock::now(); // Fim da medição do tempo
    auto duration = duration_cast<microseconds>(stop - start);
    return {score_matrix[m][n], gap_count, duration.count() / 1e6};
}
int main(int argc, char* argv[]) {
    string v = argv[1];
    string w = argv[2];
    Result result = needleman_wunsch(v, w);
    cout << fixed << setprecision(2) << result.execution_time  << endl;
    cout << result.score << endl;
    cout << result.gap_count << endl;
    cout << 49 << endl;
    return 0;
}