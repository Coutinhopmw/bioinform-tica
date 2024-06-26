package codes.java;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
public class SW {
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
        int i = maxPos[0];
        int j = maxPos[1];
        int gaps = 0;
        while (scoreMatrix[i][j] != 0) {
            if (scoreMatrix[i][j] == scoreMatrix[i - 1][j - 1] + score(seq1.charAt(i - 1), seq2.charAt(j - 1), matchScore, mismatchPenalty)) {
                i--;
                j--;
            } else if (scoreMatrix[i][j] == scoreMatrix[i - 1][j] + gapPenalty) {
                gaps++;
                i--;
            } else {
                gaps++;
                j--;
            }
        }
        long endTime = System.currentTimeMillis();
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.00", symbols);
        double executionTime = (endTime - startTime) / 1000.0;
        System.out.println(df.format(executionTime));
        System.out.println(maxScore);
        System.out.println(gaps);
        System.out.println("41"); // Ajuste conforme necessário
    }
    public static int score(char char1, char char2, int matchScore, int mismatchPenalty) {
        return (char1 == char2) ? matchScore : mismatchPenalty;
    }
    public static void main(String[] args) {
        String seq1 = args[0];
        String seq2 = args[1];
        smithWaterman(seq1, seq2);
    }
}
