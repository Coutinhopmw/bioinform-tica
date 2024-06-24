using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
public class NWResult{
    public string v_aligned { get; set; }
    public string w_aligned { get; set; }
    public string barrinha { get; set; }
    public int score { get; set; }
    public int gap_count { get; set; }
    public double e_value { get; set; }
    public double execution_time { get; set; }
}
public class NW{
    public static NWResult NeedlemanWunsch(string v, string w, int match_score = 1, int mismatch_score = -1, int gap_penalty = -1){
        var stopwatch = Stopwatch.StartNew(); // Início da medição do tempo
        int m = v.Length;
        int n = w.Length;
        var score_matrix = new int[m + 1, n + 1];
        var direction_matrix = new int[m + 1, n + 1];
        for (int i = 1; i <= m; ++i){
            score_matrix[i, 0] = i * gap_penalty;
            direction_matrix[i, 0] = 1; // Acima
        }
        for (int j = 1; j <= n; ++j){
            score_matrix[0, j] = j * gap_penalty;
            direction_matrix[0, j] = 2; // Esquerda
        }
        for (int i = 1; i <= m; ++i){
            for (int j = 1; j <= n; ++j){
                int match = score_matrix[i - 1, j - 1] + (v[i - 1] == w[j - 1] ? match_score : mismatch_score);
                int delete_score = score_matrix[i - 1, j] + gap_penalty;
                int insert_score = score_matrix[i, j - 1] + gap_penalty;
                score_matrix[i, j] = Math.Max(Math.Max(match, delete_score), insert_score);
                if (score_matrix[i, j] == match){
                    direction_matrix[i, j] = 3; // Diagonal
                }
                else if (score_matrix[i, j] == delete_score){
                    direction_matrix[i, j] = 1; // Acima
                }
                else{
                    direction_matrix[i, j] = 2; // Esquerda
                }
            }
        }
        string v_aligned = "", w_aligned = "", barrinha = "";
        int ii = m, jj = n;
        int gap_count = 0;
        while (ii > 0 || jj > 0){
            if (direction_matrix[ii, jj] == 3){
                v_aligned = v[ii - 1] + v_aligned;
                w_aligned = w[jj - 1] + w_aligned;
                if (v[ii - 1] == w[jj - 1]){
                    barrinha = '|' + barrinha;
                }
                else{
                    barrinha = ':' + barrinha;
                }
                --ii;
                --jj;
            }
            else if (direction_matrix[ii, jj] == 1){
                v_aligned = v[ii - 1] + v_aligned;
                w_aligned = '-' + w_aligned;
                barrinha = '-' + barrinha;
                --ii;
                gap_count++;
            }
            else{
                w_aligned = w[jj - 1] + w_aligned;
                v_aligned = '-' + v_aligned;
                barrinha = '-' + barrinha;
                --jj;
                gap_count++;
            }
        }
        stopwatch.Stop(); // Fim da medição do tempo
        var duration = stopwatch.Elapsed.TotalSeconds;
        double K = 0.1, lambda = 9.162242926908048;
        double e_value = K * m * n * Math.Exp(-lambda * score_matrix[m, n]);
        return new NWResult{
            v_aligned = v_aligned,
            w_aligned = w_aligned,
            barrinha = barrinha,
            score = score_matrix[m, n],
            gap_count = gap_count,
            e_value = e_value,
            execution_time = duration
        };
    }
    public static void Main(string[] args){
        string seq1 = args[0];
        string seq2 = args[1];
        NWResult result = NeedlemanWunsch(seq1, seq2);
        Console.WriteLine(result.execution_time.ToString("F2"));
        Console.WriteLine(result.score);
        Console.WriteLine(result.gap_count);
        Console.WriteLine(result.e_value.ToString("F2"));
        Console.WriteLine("102");
    }
}