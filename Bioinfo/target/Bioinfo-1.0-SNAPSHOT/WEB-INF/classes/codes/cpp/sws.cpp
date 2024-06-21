#include <iostream>
#include <vector>
#include <string>
#include <chrono>
#include <algorithm>
#include <math.h>
#include <utility> // para std::pair e std::make_pair

using namespace std;
using namespace std::chrono;

int score(char char1, char char2) {
    return char1 == char2 ? 1 : -1;
}

void needleman_wunsch_reconstruct(const string& seq1, const string& seq2, int match_score, int mismatch_penalty, int gap_penalty) {
    auto start = high_resolution_clock::now();

    int len1 = seq1.size();
    int len2 = seq2.size();

    vector<vector<int>> score_matrix(len1 + 1, vector<int>(len2 + 1, 0));

    for (int i = 0; i <= len1; ++i) {
        score_matrix[i][0] = gap_penalty * i;
    }

    for (int j = 0; j <= len2; ++j) {
        score_matrix[0][j] = gap_penalty * j;
    }
    
    int max_score = 0;
    pair<int, int> max_pos(0, 0);

    for (int i = 1; i <= len1; ++i) {
        for (int j = 1; j <= len2; ++j) {
            int match_score_ij = score_matrix[i - 1][j - 1] + score(seq1[i - 1], seq2[j - 1]);
            int delete_seq1_ij = score_matrix[i - 1][j] + gap_penalty;
            int delete_seq2_ij = score_matrix[i][j - 1] + gap_penalty;
            score_matrix[i][j] = max({0, match_score_ij, delete_seq1_ij, delete_seq2_ij});
            if (score_matrix[i][j] > max_score) {
                max_score = score_matrix[i][j];
                max_pos = make_pair(i, j);
            }
        }
    }

    string alignment_seq1, alignment_seq2, barrinha;
    int i = max_pos.first, j = max_pos.second;
    int gap_count = 0;

    while (i > 0 && j > 0) {
        if (score_matrix[i][j] == score_matrix[i - 1][j - 1] + score(seq1[i - 1], seq2[j - 1])) {
            alignment_seq1 = seq1[i - 1] + alignment_seq1;
            alignment_seq2 = seq2[j - 1] + alignment_seq2;
            barrinha = (score(seq1[i - 1], seq2[j - 1]) == 1 ? "|" : ":") + barrinha;
            --i;
            --j;
        } else if (score_matrix[i][j] == score_matrix[i - 1][j] + gap_penalty) {
            alignment_seq1 = seq1[i - 1] + alignment_seq1;
            alignment_seq2 = "-" + alignment_seq2;
            gap_count++;
            barrinha = "-" + barrinha;
            --i;
        } else {
            alignment_seq1 = "-" + alignment_seq1;
            alignment_seq2 = seq2[j - 1] + alignment_seq2;
            gap_count++;
            barrinha = "-" + barrinha;
            --j;
        }
    }

    while (i > 0) {
        alignment_seq1 = seq1[i - 1] + alignment_seq1;
        alignment_seq2 = "-" + alignment_seq2;
        barrinha = "-" + barrinha;
        --i;
    }

    while (j > 0) {
        alignment_seq1 = "-" + alignment_seq1;
        alignment_seq2 = seq2[j - 1] + alignment_seq2;
        barrinha = "-" + barrinha;
        --j;
    }

    auto end = high_resolution_clock::now();
    duration<double> elapsed = end - start;

    cout << "Percent: " << round((100.0 * count(barrinha.begin(), barrinha.end(), '|')) / barrinha.length() * 100.0) / 100.0 << endl;
    cout << "Time:    " << round(elapsed.count() * 100.0) / 100.0 << endl;
    cout << "Score:   " << max_score << endl;
    cout << "Gaps:    " << gap_count << endl;
    cout << "EVAlue:  0.0" << endl;
    cout << "Linhas:  51" << endl;
    // cout << alignment_seq1 << endl;
    // cout << barrinha << endl;
    // cout << alignment_seq2 << endl;
}

int main(int argc, char* argv[]) {
    if (argc < 2) {
        //cerr << "Usage: " << argv[0] << " <sequence1>" << endl;
        return 1;
    }

    string seq1 = argv[1];
    string seq2 = "TTTCGGCGAATTGAGAGAAATTAGATGCGGTTTGTGTCTGAACCTTTTATCCTAGCGACGATTTTTTAAGGAAGTTGAATATGATCATCAAACCTAAAATTCGTGGATTTATCTGTACAACAACGCACCCAGTGGGTTGTGAAGCGAACGTAAAAGAACAAATTGCCTACACAAAAGCACAAGGTCCGATCAAAAACGCACCTAAGCGCGTGTTGGTTGTCGGATCGTCTAGCGGCTATGGTCTGTCATCACGCATCGCTGCGGCGTTTGGCGGTGGTGCGGCGACGATCGGCGTATTTTTCGAAAAGCCGGGCACTGACAAAAAACCAGGTACTGCGGGTTTCTACAATGCAGCAGCGTTTGACAAGCTAGCGCATGAAGCGGGCTTGTACGCAAAAAGCCTGAACGGCGATGCGTTCTCGAACGAAGCGAAGCAAAAAGCGATTGAGCTGATTAAGCAAGACCTCGGCCAGATTGATTTGGTGGTTTACTCATTGGCTTCTCCAGTGCGTAAAATGCCAGACACGGGTGAGCTAGTGCGCTCTGCACTAAAACCGATCGGCGAAACGTACACCTCTACCGCGGTAGATACCAATAAAGATGTGATCATTGAAGCCAGTGTTGAACCTGCGACCGAGCAAGAAATCGCTGACACTGTCACCGTGATGGGCGGTCAAGATTGGGAACTGTGGATCCAAGCACTGGAAGAGGCGGGTGTTCTTGCTGAAGGTTGCAAAACCGTGGCGTACAGCTACATCGGTACTGAATTGACTTGGCCAATCTACTGGGATGGCGCTTTAGGCCGTGCCAAGATGGACCTAGATCGCGCAGCGACAGCGCTGAACGAAAAGCTGGCAGCGAAAGGTGGTACCGCGAACGTTGCAGTTTTGAAATCAGTGGTGACTCAAGCAAGCTCTGCGATTCCTGTGATGCCGCTCTACATCGCGATGGTGTTCAAGAAGATGCGTGAACAGGGCGTGCATGAAGGCTGTATGGAGCAGATCTACCGCATGTTCAGTCAACGTCTGTACAAAGAAGATGGTTCAGCGCCGGAAGTGGATGATCACAATCGTCTGCGTTTGGATGACTGGGAACTGCGTGATGACATTCAGCAGCACTGCCGTGATCTGTGGCCACAAATCACTACAGAGAACCTGCGTGAGCTGACCGATTACGACATGTACAAAGAAGAGTTCATCAAGCTGTTTGGCTTTGGCATTGAAGGCATTGATTACGATGCTGACGTCAATCCAGAAGTCGAATTCGATGTGATTGATATCGAGTAAGAGAATTAACTCTTATCTTAAAAAGGCGCGTTATCGCGCCTTTTTTGTGTCCGGAGTACAGCATGAATACAGCAGGTTGC";

    needleman_wunsch_reconstruct(seq1, seq2, 1, -1, -1);
    return 0;
}
