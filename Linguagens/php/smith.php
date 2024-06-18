<?php
    include('exec_time.php');
    function smith_score($seq1, $seq2, $match, $mismatch, $gap){
        $len1 = strlen($seq1); $len2 = strlen($seq2);

        $score_matrix = array_fill(0, $len1 + 1, array_fill(0, $len2 + 1, 0));

        $maxScore = 0;
        $max_pos = [0, 0];

        for ($i = 1; $i <= $len1; $i++) {
            for ($j = 1; $j <= $len2; $j++) {
                $scoreDiag = ($seq1[$i - 1] == $seq2[$j - 1]) ? $match : $mismatch;
                $diag = $score_matrix[$i - 1][$j - 1] + $scoreDiag;
                $up = $score_matrix[$i - 1][$j] + $gap;
                $left = $score_matrix[$i][$j - 1] + $gap;
                $score_matrix[$i][$j] = max(0, $diag, $up, $left);

                // Identificação do ponto de maior pontuação
                if ($score_matrix[$i][$j] > $maxScore) {
                    $maxScore = $score_matrix[$i][$j];
                    $max_pos = [$i, $j];
                }
            }
        }
        return ['score' => $maxScore, 'score_pos' => $max_pos, 'matrix' => $score_matrix];
    }
    function smithWaterman($seq1, $seq2, $match, $mismatch, $gap) {

        // Traçar de volta para encontrar a subsequência alinhada de maior similaridade
        $matrix = smith_score($seq1, $seq2, $match, $mismatch, $gap);
        list($i, $j) = $matrix['score_pos'];
        $score_matrix = $matrix['matrix'];
        $align1 = '';
        $align2 = '';
        $gaps = 0;

        while ($i > 0 && $j > 0 && $score_matrix[$i][$j] > 0) {
            if ($score_matrix[$i][$j] == $score_matrix[$i - 1][$j - 1] + (($seq1[$i - 1] == $seq2[$j - 1]) ? $match : $mismatch)) {
                $align1 = $seq1[$i - 1] . $align1;
                $align2 = $seq2[$j - 1] . $align2;
                $i--;
                $j--;
            } elseif ($score_matrix[$i][$j] == $score_matrix[$i - 1][$j] + $gap) {
                $align1 = $seq1[$i - 1] . $align1;
                $align2 = '-' . $align2;
                $i--;
                $gaps++;
            } else {
                $align1 = '-' . $align1;
                $align2 = $seq2[$j - 1] . $align2;
                $j--;
                $gaps++;
            }
        }
        return ['score' => $matrix['score'], 'gaps' => $gaps, 'align1' => $align1, 'align2' => $align2];
    }

    $seq1 = "AACATGATGGGAAAGAGGGAAAAGAAGCCAAGCGTGGCCGGCGAAGCGAAAGGGTCAAGAACTATCTGGT
ACATGTGGCTTGGGAGTCGATTCCTGGAATTTGAGGCCCTGGGATTTTTGAACGCGGGCCACTGGGTCAG
TCGGGAAAATTTTCCTGGTGGCGTTGGTGGTGTAGGTGTCAATTACTTTGGCAACTATCTAAAGGAAATC
TCGCTCAAAGGAAAGTATCTCTTTGCTGATGACACCGCCGGCTGGGACACA";
    $seq2 = "AACATGATGGGGAAAAGAGAGAAGAAGCCAAGCGTGGCCGGCGAAGCGAAAGGGTCAAGAACTATCTGGT
ACATGTGGCTTGGGAGTCGATTCCTGGAATTTGAGGCCCTGGGATTTTTGAACGCGGACCACTGGGTCAG
TCGGGAAAATTTTCCTGGTGGCGTTGGTGGTGTAGGTGTCAATTACTTTGGCAACTATCTAAAGGAAATC
TCGCTCAAAGGAAAGTATCTCTTTGCTGATGACACCGCCGGCTGGGACACA";
    $start = microtime(true);
    $result = smithWaterman($seq1, $seq2, 1, -1, -1);
    $time = microtime(true) - $start;
    $score = $result['score'];
    $qtd_gap = $result['gaps'];

    echo "Score: " . $score . "\n";
    echo "Alignment 1: " . $result['align1'] . "\n";
    echo "Alignment 2: " . $result['align2'] . "\n";

    $text = "$time\n$score\n$qtd_gap\n$evalue";

    $arquivo = fopen("resultados_php.txt", "w");
    if ($arquivo == false){
        die ('Não foi possível criar o arquivo.');
    } else{
        fwrite($arquivo, $text);
    }
?>
