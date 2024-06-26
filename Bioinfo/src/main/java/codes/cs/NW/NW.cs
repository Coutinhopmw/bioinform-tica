using System.Diagnostics;
public class NWResult{
    public int score { get; set; }
    public int gap_count { get; set; }
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
        int ii = m, jj = n;
        int gap_count = 0;
        while (ii > 0 || jj > 0){
            if (direction_matrix[ii, jj] == 3){
                --ii;
                --jj;
            }
            else if (direction_matrix[ii, jj] == 1){
                --ii;
                gap_count++;
            }
            else{
                --jj;
                gap_count++;
            }
        }
        stopwatch.Stop(); // Fim da medição do tempo
        var duration = stopwatch.Elapsed.TotalSeconds;
        return new NWResult{
            score = score_matrix[m, n],
            gap_count = gap_count,
            execution_time = duration
        };
    }
    public static void Main(string[] args){
        string seq1 = args[0];
        string seq2 = args[1];
        NWResult result = NeedlemanWunsch(seq1, seq2);
        // Usando o CultureInfo.InvariantCulture para garantir que o ponto decimal seja usado
        Console.WriteLine(result.execution_time.ToString("F2", System.Globalization.CultureInfo.InvariantCulture));
        Console.WriteLine(result.score);
        Console.WriteLine(result.gap_count);
        Console.WriteLine("57");
    }
}