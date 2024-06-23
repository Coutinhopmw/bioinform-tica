using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;

public class Result
{
    public string aligned_seq1 { get; set; }
    public string aligned_seq2 { get; set; }
    public int max_score { get; set; }
}

public class Program
{
    public static Result SmithWaterman(string seq1, string seq2, int match = 2, int mismatch = -1, int gap = -1)
    {
        int m = seq1.Length;
        int n = seq2.Length;
        var score_matrix = new int[m + 1, n + 1];
        var traceback_matrix = new int[m + 1, n + 1];

        int max_score = 0;
        (int, int) max_pos = (0, 0);

        for (int i = 1; i <= m; ++i)
        {
            for (int j = 1; j <= n; ++j)
            {
                int match_score = score_matrix[i - 1, j - 1] + (seq1[i - 1] == seq2[j - 1] ? match : mismatch);
                int delete_score = score_matrix[i - 1, j] + gap;
                int insert_score = score_matrix[i, j - 1] + gap;
                score_matrix[i, j] = Math.Max(0, Math.Max(match_score, Math.Max(delete_score, insert_score)));

                if (score_matrix[i, j] == match_score)
                {
                    traceback_matrix[i, j] = 1; // Diagonal
                }
                else if (score_matrix[i, j] == delete_score)
                {
                    traceback_matrix[i, j] = 2; // Up
                }
                else if (score_matrix[i, j] == insert_score)
                {
                    traceback_matrix[i, j] = 3; // Left
                }

                if (score_matrix[i, j] > max_score)
                {
                    max_score = score_matrix[i, j];
                    max_pos = (i, j);
                }
            }
        }

        string aligned_seq1 = "", aligned_seq2 = "";
        int ii = max_pos.Item1;
        int jj = max_pos.Item2;

        while (score_matrix[ii, jj] != 0)
        {
            if (traceback_matrix[ii, jj] == 1)
            {
                aligned_seq1 = seq1[ii - 1] + aligned_seq1;
                aligned_seq2 = seq2[jj - 1] + aligned_seq2;
                --ii;
                --jj;
            }
            else if (traceback_matrix[ii, jj] == 2)
            {
                aligned_seq1 = seq1[ii - 1] + aligned_seq1;
                aligned_seq2 = '-' + aligned_seq2;
                --ii;
            }
            else if (traceback_matrix[ii, jj] == 3)
            {
                aligned_seq1 = '-' + aligned_seq1;
                aligned_seq2 = seq2[jj - 1] + aligned_seq2;
                --jj;
            }
        }

        return new Result
        {
            aligned_seq1 = aligned_seq1,
            aligned_seq2 = aligned_seq2,
            max_score = max_score
        };
    }

    public static string ReadSequenceFromFile(string filename)
    {
        if (!File.Exists(filename))
        {
            Console.Error.WriteLine($"Erro ao abrir o arquivo: {filename}");
            Environment.Exit(EXIT_FAILURE);
        }
        return File.ReadAllText(filename).Replace("\n", "");
    }

    private const int EXIT_FAILURE = 1;

    public static void Main()
    {
        // Lê a sequência e o banco de dados a partir dos arquivos
        string seq1 = ReadSequenceFromFile("sequencia.txt");
        string seq2 = ReadSequenceFromFile("banco.txt");

        Result result = SmithWaterman(seq1, seq2);

        Console.WriteLine("Alinhamento:");
        Console.WriteLine(result.aligned_seq1);
        Console.WriteLine(result.aligned_seq2);
        Console.WriteLine("Pontuação: " + result.max_score);
    }
}
