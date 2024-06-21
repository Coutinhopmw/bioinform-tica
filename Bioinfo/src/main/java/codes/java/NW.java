package codes.java;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NW {
    public static int score(char char1, char char2) {
        return char1 == char2 ? 1 : -1;
    }
    public static void needlemanWunschReconstruct(String seq1, String seq2, int matchScore, int mismatchPenalty, int gapPenalty) {
        long startTime = System.currentTimeMillis();
        int[][] scoreMatrix = new int[seq1.length() + 1][seq2.length() + 1];
        for (int i = 0; i <= seq1.length(); i++) {
            scoreMatrix[i][0] = gapPenalty * i;
        }
        for (int j = 0; j <= seq2.length(); j++) {
            scoreMatrix[0][j] = gapPenalty * j;
        }
        for (int i = 1; i <= seq1.length(); i++) {
            for (int j = 1; j <= seq2.length(); j++) {
                int matchScoreIJ = scoreMatrix[i - 1][j - 1] + score(seq1.charAt(i - 1), seq2.charAt(j - 1));
                int deleteSeq1IJ = scoreMatrix[i - 1][j] + gapPenalty;
                int deleteSeq2IJ = scoreMatrix[i][j - 1] + gapPenalty;
                scoreMatrix[i][j] = Math.max(Math.max(matchScoreIJ, deleteSeq1IJ), deleteSeq2IJ);
            }
        }
        StringBuilder alignmentSeq1 = new StringBuilder();
        StringBuilder alignmentSeq2 = new StringBuilder();
        StringBuilder barrinha = new StringBuilder();
        int i = seq1.length();
        int j = seq2.length();
        while (i > 0 && j > 0) {
            if (scoreMatrix[i][j] == scoreMatrix[i - 1][j - 1] + score(seq1.charAt(i - 1), seq2.charAt(j - 1))) {
                alignmentSeq1.insert(0, seq1.charAt(i - 1));
                alignmentSeq2.insert(0, seq2.charAt(j - 1));
                if (score(seq1.charAt(i - 1), seq2.charAt(j - 1)) == 1) {
                    barrinha.insert(0, '|');
                } else {
                    barrinha.insert(0, ':');
                }
                i--;
                j--;
            } else if (scoreMatrix[i][j] == scoreMatrix[i - 1][j] + gapPenalty) {
                alignmentSeq1.insert(0, seq1.charAt(i - 1));
                alignmentSeq2.insert(0, '-');
                barrinha.insert(0, '-');
                i--;
            } else {
                alignmentSeq1.insert(0, '-');
                alignmentSeq2.insert(0, seq2.charAt(j - 1));
                barrinha.insert(0, '-');
                j--;
            }
        }
        while (i > 0) {
            alignmentSeq1.insert(0, seq1.charAt(i - 1));
            alignmentSeq2.insert(0, '-');
            barrinha.insert(0, '-');
            i--;
        }
        while (j > 0) {
            alignmentSeq1.insert(0, '-');
            alignmentSeq2.insert(0, seq2.charAt(j - 1));
            barrinha.insert(0, '-');
            j--;
        }
        long endTime = System.currentTimeMillis();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        /*Percent:*/System.out.println(Math.round(((100.0 * countOccurrences(barrinha, '|')) / barrinha.length()) * 100.0) / 100.0);
        /*Time:   */System.out.println(df.format((endTime - startTime) / 1000.0));
        /*Score:  */System.out.println(scoreMatrix[seq1.length()][seq2.length()]);
        /*Gaps:   */System.out.println(countChar(barrinha.toString(), '-'));
        /*EValue: */System.out.println("0.0");
        /*Linhas: */System.out.println("61");
    }
    public static int countChar(String str, char ch) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == ch) {
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
    public static void main(String[] args) {
        String seq1 = args[0];
        String seq2 = "TTTCGGCGAATTGAGAGAAATTAGATGCGGTTTGTGTCTGAACCTTTTATCCTAGCGACGATTTTTTAAGGAAGTTGAATATGATCATCAAACCTAAAATTCGTGGATTTATCTGTACAACAACGCACCCAGTGGGTTGTGAAGCGAACGTAAAAGAACAAATTGCCTACACAAAAGCACAAGGTCCGATCAAAAACGCACCTAAGCGCGTGTTGGTTGTCGGATCGTCTAGCGGCTATGGTCTGTCATCACGCATCGCTGCGGCGTTTGGCGGTGGTGCGGCGACGATCGGCGTATTTTTCGAAAAGCCGGGCACTGACAAAAAACCAGGTACTGCGGGTTTCTACAATGCAGCAGCGTTTGACAAGCTAGCGCATGAAGCGGGCTTGTACGCAAAAAGCCTGAACGGCGATGCGTTCTCGAACGAAGCGAAGCAAAAAGCGATTGAGCTGATTAAGCAAGACCTCGGCCAGATTGATTTGGTGGTTTACTCATTGGCTTCTCCAGTGCGTAAAATGCCAGACACGGGTGAGCTAGTGCGCTCTGCACTAAAACCGATCGGCGAAACGTACACCTCTACCGCGGTAGATACCAATAAAGATGTGATCATTGAAGCCAGTGTTGAACCTGCGACCGAGCAAGAAATCGCTGACACTGTCACCGTGATGGGCGGTCAAGATTGGGAACTGTGGATCCAAGCACTGGAAGAGGCGGGTGTTCTTGCTGAAGGTTGCAAAACCGTGGCGTACAGCTACATCGGTACTGAATTGACTTGGCCAATCTACTGGGATGGCGCTTTAGGCCGTGCCAAGATGGACCTAGATCGCGCAGCGACAGCGCTGAACGAAAAGCTGGCAGCGAAAGGTGGTACCGCGAACGTTGCAGTTTTGAAATCAGTGGTGACTCAAGCAAGCTCTGCGATTCCTGTGATGCCGCTCTACATCGCGATGGTGTTCAAGAAGATGCGTGAACAGGGCGTGCATGAAGGCTGTATGGAGCAGATCTACCGCATGTTCAGTCAACGTCTGTACAAAGAAGATGGTTCAGCGCCGGAAGTGGATGATCACAATCGTCTGCGTTTGGATGACTGGGAACTGCGTGATGACATTCAGCAGCACTGCCGTGATCTGTGGCCACAAATCACTACAGAGAACCTGCGTGAGCTGACCGATTACGACATGTACAAAGAAGAGTTCATCAAGCTGTTTGGCTTTGGCATTGAAGGCATTGATTACGATGCTGACGTCAATCCAGAAGTCGAATTCGATGTGATTGATATCGAGTAAGAGAATTAACTCTTATCTTAAAAAGGCGCGTTATCGCGCCTTTTTTGTGTCCGGAGTACAGCATGAATACAGCAGGTTGC";
        needlemanWunschReconstruct(seq1, seq2, 1, -1, -1);
    }
}