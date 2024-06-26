using System.Diagnostics;
public class SWResult{
    public int max_score { get; set; }
    public int gap_count { get; set; }
    public double execution_time { get; set; }
}
public class SW{
    public static SWResult SmithWaterman(string seq1, string seq2, int match = 1, int mismatch = -1, int gap = -1){
        var stopwatch = Stopwatch.StartNew();
        int m = seq1.Length;
        int n = seq2.Length;
        var score_matrix = new int[m + 1, n + 1];
        var traceback_matrix = new int[m + 1, n + 1];
        int max_score = 0;
        (int, int) max_pos = (0, 0);
        for (int i = 1; i <= m; ++i){
            for (int j = 1; j <= n; ++j){
                int match_score = score_matrix[i - 1, j - 1] + (seq1[i - 1] == seq2[j - 1] ? match : mismatch);
                int delete_score = score_matrix[i - 1, j] + gap;
                int insert_score = score_matrix[i, j - 1] + gap;
                score_matrix[i, j] = Math.Max(0, Math.Max(match_score, Math.Max(delete_score, insert_score)));

                if (score_matrix[i, j] == match_score){
                    traceback_matrix[i, j] = 1; // Diagonal
                }
                else if (score_matrix[i, j] == delete_score){
                    traceback_matrix[i, j] = 2; // Up
                }
                else if (score_matrix[i, j] == insert_score){
                    traceback_matrix[i, j] = 3; // Left
                }
                if (score_matrix[i, j] > max_score){
                    max_score = score_matrix[i, j];
                    max_pos = (i, j);
                }
            }
        }
        int ii = max_pos.Item1;
        int jj = max_pos.Item2;
        int gap_count = 0;
        while (score_matrix[ii, jj] != 0){
            if (traceback_matrix[ii, jj] == 1){
                --ii;
                --jj;
            }
            else if (traceback_matrix[ii, jj] == 2){
                --ii;
                gap_count++;
            }
            else if (traceback_matrix[ii, jj] == 3){
                --jj;
                gap_count++;
            }
        }
        stopwatch.Stop(); // Fim da medição do tempo
        var duration = stopwatch.Elapsed.TotalSeconds;
        return new SWResult{
            gap_count = gap_count,
            execution_time = duration,
            max_score = max_score
        };
    }
    public static void Main(string[] args){
        string seq1 = args[0];
        string seq2 = args[1];
        SWResult result = SmithWaterman(seq1, seq2);
        // Usando o CultureInfo.InvariantCulture para garantir que o ponto decimal seja usado
        Console.WriteLine(result.execution_time.ToString("F2", System.Globalization.CultureInfo.InvariantCulture));
        Console.WriteLine(result.max_score);
        Console.WriteLine(result.gap_count);
        Console.WriteLine("56");
    }
}