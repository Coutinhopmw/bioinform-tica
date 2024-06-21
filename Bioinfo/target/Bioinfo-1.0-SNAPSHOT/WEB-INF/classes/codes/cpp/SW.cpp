#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include <chrono>
#include <math.h>
#include <iomanip>

using namespace std;
using namespace std::chrono;

int score(char char1, char char2, int matchScore, int mismatchPenalty) {
    return (char1 == char2) ? matchScore : mismatchPenalty;
}

int countChar(const string& str, char ch) {
    int count = 0;
    for (char c : str) {
        if (c == ch) {
            count++;
        }
    }
    return count;
}

int countOccurrences(const string& str, char c) {
    int count = 0;
    for (char x : str) {
        if (x == c) {
            count++;
        }
    }
    return count;
}

void smithWaterman(const string& seq1, const string& seq2) {
    auto startTime = high_resolution_clock::now();
    int matchScore = 1;
    int mismatchPenalty = -1;
    int gapPenalty = -1;
    vector<vector<int>> scoreMatrix(seq1.length() + 1, vector<int>(seq2.length() + 1, 0));
    int maxScore = 0;
    vector<int> maxPos(2, 0);

    for (size_t i = 1; i <= seq1.length(); i++) {
        for (size_t j = 1; j <= seq2.length(); j++) {
            int match = scoreMatrix[i - 1][j - 1] + score(seq1[i - 1], seq2[j - 1], matchScore, mismatchPenalty);
            int deletion = scoreMatrix[i - 1][j] + gapPenalty;
            int insertion = scoreMatrix[i][j - 1] + gapPenalty;
            scoreMatrix[i][j] = max(0, max(match, max(deletion, insertion)));
            if (scoreMatrix[i][j] > maxScore) {
                maxScore = scoreMatrix[i][j];
                maxPos[0] = i;
                maxPos[1] = j;
            }
        }
    }

    string alignedSeq1, alignedSeq2, barrinha;
    size_t i = maxPos[0];
    size_t j = maxPos[1];
    while (scoreMatrix[i][j] != 0) {
        if (scoreMatrix[i][j] == scoreMatrix[i - 1][j - 1] + score(seq1[i - 1], seq2[j - 1], matchScore, mismatchPenalty)) {
            alignedSeq1 = seq1[i - 1] + alignedSeq1;
            alignedSeq2 = seq2[j - 1] + alignedSeq2;
            barrinha = (seq1[i - 1] == seq2[j - 1]) ? '|' + barrinha : ':' + barrinha;
            i--;
            j--;
        } else if (scoreMatrix[i][j] == scoreMatrix[i - 1][j] + gapPenalty) {
            alignedSeq1 = seq1[i - 1] + alignedSeq1;
            alignedSeq2 = '-' + alignedSeq2;
            barrinha = '-' + barrinha;
            i--;
        } else {
            alignedSeq1 = '-' + alignedSeq1;
            alignedSeq2 = seq2[j - 1] + alignedSeq2;
            barrinha = '-' + barrinha;
            j--;
        }
    }

    auto endTime = high_resolution_clock::now();
    auto duration = duration_cast<milliseconds>(endTime - startTime).count();

    /*"Percent"*/cout << round(((100.0 * countOccurrences(barrinha, '|')) / barrinha.length()) * 100.0) / 100.0 << endl;
    /*"Time   "*/cout << fixed << setprecision(2) << duration / 1000.0 << endl;
    /*"Score  "*/cout << maxScore << endl;
    /*"Gaps:  "*/cout << countChar(barrinha, '-') << endl;
    /*"EValue:"*/cout << "0.0" << endl;
    /*"Linhas:"*/cout << "56" << endl;
}

int main(int argc, char *argv[]) {
    if (argc < 2) {
        cerr << "Usage: " << argv[0] << " <seq1>" << endl;
        return 1;
    }
    string seq1 = argv[1];
    string seq2 = "TTTCGGCGAATTGAGAGAAATTAGATGCGGTTTGTGTCTGAACCTTTTATCCTAGCGACGATTTTTTAAGGAAGTTGAATATGATCATCAAACCTAAAATTCGTGGATTTATCTGTACAACAACGCACCCAGTGGGTTGTGAAGCGAACGTAAAAGAACAAATTGCCTACACAAAAGCACAAGGTCCGATCAAAAACGCACCTAAGCGCGTGTTGGTTGTCGGATCGTCTAGCGGCTATGGTCTGTCATCACGCATCGCTGCGGCGTTTGGCGGTGGTGCGGCGACGATCGGCGTATTTTTCGAAAAGCCGGGCACTGACAAAAAACCAGGTACTGCGGGTTTCTACAATGCAGCAGCGTTTGACAAGCTAGCGCATGAAGCGGGCTTGTACGCAAAAAGCCTGAACGGCGATGCGTTCTCGAACGAAGCGAAGCAAAAAGCGATTGAGCTGATTAAGCAAGACCTCGGCCAGATTGATTTGGTGGTTTACTCATTGGCTTCTCCAGTGCGTAAAATGCCAGACACGGGTGAGCTAGTGCGCTCTGCACTAAAACCGATCGGCGAAACGTACACCTCTACCGCGGTAGATACCAATAAAGATGTGATCATTGAAGCCAGTGTTGAACCTGCGACCGAGCAAGAAATCGCTGACACTGTCACCGTGATGGGCGGTCAAGATTGGGAACTGTGGATCCAAGCACTGGAAGAGGCGGGTGTTCTTGCTGAAGGTTGCAAAACCGTGGCGTACAGCTACATCGGTACTGAATTGACTTGGCCAATCTACTGGGATGGCGCTTTAGGCCGTGCCAAGATGGACCTAGATCGCGCAGCGACAGCGCTGAACGAAAAGCTGGCAGCGAAAGGTGGTACCGCGAACGTTGCAGTTTTGAAATCAGTGGTGACTCAAGCAAGCTCTGCGATTCCTGTGATGCCGCTCTACATCGCGATGGTGTTCAAGAAGATGCGTGAACAGGGCGTGCATGAAGGCTGTATGGAGCAGATCTACCGCATGTTCAGTCAACGTCTGTACAAAGAAGATGGTTCAGCGCCGGAAGTGGATGATCACAATCGTCTGCGTTTGGATGACTGGGAACTGCGTGATGACATTCAGCAGCACTGCCGTGATCTGTGGCCACAAATCACTACAGAGAACCTGCGTGAGCTGACCGATTACGACATGTACAAAGAAGAGTTCATCAAGCTGTTTGGCTTTGGCATTGAAGGCATTGATTACGATGCTGACGTCAATCCAGAAGTCGAATTCGATGTGATTGATATCGAGTAAGAGAATTAACTCTTATCTTAAAAAGGCGCGTTATCGCGCCTTTTTTGTGTCCGGAGTACAGCATGAATACAGCAGGTTGC";
    smithWaterman(seq1, seq2);
    return 0;
}
