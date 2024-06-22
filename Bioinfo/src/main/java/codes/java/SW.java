package codes.java;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class SW {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java SmithWaterman <seq1>");
            System.exit(1);
        }
        String seq1 = args[0];
        String seq2 = "TTTCGGCGAATTGAGAGAAATTAGATGCGGTTTGTGTCTGAACCTTTTATCCTAGCGACGATTTTTTAAGGAAGTTGAATATGATCATCAAACCTAAAATTCGTGGATTTATCTGTACAACAACGCACCCAGTGGGTTGTGAAGCGAACGTAAAAGAACAAATTGCCTACACAAAAGCACAAGGTCCGATCAAAAACGCACCTAAGCGCGTGTTGGTTGTCGGATCGTCTAGCGGCTATGGTCTGTCATCACGCATCGCTGCGGCGTTTGGCGGTGGTGCGGCGACGATCGGCGTATTTTTCGAAAAGCCGGGCACTGACAAAAAACCAGGTACTGCGGGTTTCTACAATGCAGCAGCGTTTGACAAGCTAGCGCATGAAGCGGGCTTGTACGCAAAAAGCCTGAACGGCGATGCGTTCTCGAACGAAGCGAAGCAAAAAGCGATTGAGCTGATTAAGCAAGACCTCGGCCAGATTGATTTGGTGGTTTACTCATTGGCTTCTCCAGTGCGTAAAATGCCAGACACGGGTGAGCTAGTGCGCTCTGCACTAAAACCGATCGGCGAAACGTACACCTCTACCGCGGTAGATACCAATAAAGATGTGATCATTGAAGCCAGTGTTGAACCTGCGACCGAGCAAGAAATCGCTGACACTGTCACCGTGATGGGCGGTCAAGATTGGGAACTGTGGATCCAAGCACTGGAAGAGGCGGGTGTTCTTGCTGAAGGTTGCAAAACCGTGGCGTACAGCTACATCGGTACTGAATTGACTTGGCCAATCTACTGGGATGGCGCTTTAGGCCGTGCCAAGATGGACCTAGATCGCGCAGCGACAGCGCTGAACGAAAAGCTGGCAGCGAAAGGTGGTACCGCGAACGTTGCAGTTTTGAAATCAGTGGTGACTCAAGCAAGCTCTGCGATTCCTGTGATGCCGCTCTACATCGCGATGGTGTTCAAGAAGATGCGTGAACAGGGCGTGCATGAAGGCTGTATGGAGCAGATCTACCGCATGTTCAGTCAACGTCTGTACAAAGAAGATGGTTCAGCGCCGGAAGTGGATGATCACAATCGTCTGCGTTTGGATGACTGGGAACTGCGTGATGACATTCAGCAGCACTGCCGTGATCTGTGGCCACAAATCACTACAGAGAACCTGCGTGAGCTGACCGATTACGACATGTACAAAGAAGAGTTCATCAAGCTGTTTGGCTTTGGCATTGAAGGCATTGATTACGATGCTGACGTCAATCCAGAAGTCGAATTCGATGTGATTGATATCGAGTAAGAGAATTAACTCTTATCTTAAAAAGGCGCGTTATCGCGCCTTTTTTGTGTCCGGAGTACAGCATGAATACAGCAGGTTGC";
        smithWaterman(seq1, seq2);
    }

    public static void smithWaterman(String seq1, String seq2) {
        long startTime = System.currentTimeMillis();
        int matchScore = 1;
        int mismatchPenalty = -1;
        int gapPenalty = -1;
        int[][] scoreMatrix = new int[seq1.length() + 1][seq2.length() + 1];
        int maxScore = 0;
        int[] maxPos = new int[2];
        for (int i = 1; i <= seq1.length(); i++) {
            for (int j = 1; j <= seq2.length(); j++) {
                int match = scoreMatrix[i - 1][j - 1] + score(seq1.charAt(i - 1), seq2.charAt(j - 1), matchScore, mismatchPenalty);
                int delete = scoreMatrix[i - 1][j] + gapPenalty;
                int insert = scoreMatrix[i][j - 1] + gapPenalty;
                scoreMatrix[i][j] = Math.max(0, Math.max(match, Math.max(delete, insert)));
                if (scoreMatrix[i][j] > maxScore) {
                    maxScore = scoreMatrix[i][j];
                    maxPos[0] = i;
                    maxPos[1] = j;
                }
            }
        }
        StringBuilder alignedSeq1 = new StringBuilder();
        StringBuilder alignedSeq2 = new StringBuilder();
        StringBuilder barrinha = new StringBuilder();
        int i = maxPos[0];
        int j = maxPos[1];
        while (scoreMatrix[i][j] != 0) {
            if (scoreMatrix[i][j] == scoreMatrix[i - 1][j - 1] + score(seq1.charAt(i - 1), seq2.charAt(j - 1), matchScore, mismatchPenalty)) {
                alignedSeq1.insert(0, seq1.charAt(i - 1));
                alignedSeq2.insert(0, seq2.charAt(j - 1));
                barrinha.insert(0, (seq1.charAt(i - 1) == seq2.charAt(j - 1)) ? '|' : ':');
                i--;
                j--;
            } else if (scoreMatrix[i][j] == scoreMatrix[i - 1][j] + gapPenalty) {
                alignedSeq1.insert(0, seq1.charAt(i - 1));
                alignedSeq2.insert(0, '-');
                barrinha.insert(0, '-');
                i--;
            } else {
                alignedSeq1.insert(0, '-');
                alignedSeq2.insert(0, seq2.charAt(j - 1));
                barrinha.insert(0, '-');
                j--;
            }
        }
        long endTime = System.currentTimeMillis();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        //*Percent:*/System.out.println(Math.round(((100.0 * countOccurrences(barrinha, '|')) / barrinha.length()) * 100.0) / 100.0);
        /*Time:   */System.out.println(df.format((endTime - startTime) / 1000.0));
        /*Score:  */System.out.println(maxScore);
        /*Gaps:   */System.out.println(countChar(barrinha.toString(), '-'));
        /*EValue: */System.out.println("0.0");
        /*Linhas: */System.out.println("56");
    }

    public static int score(char char1, char char2, int matchScore, int mismatchPenalty) {
        return (char1 == char2) ? matchScore : mismatchPenalty;
    }
    public static int countChar(String str, char ch) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ch) {
                count++;
            }
        }
        return count;
    }
    public static int countOccurrences(StringBuilder sb, char c) {
        int count = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }
}
