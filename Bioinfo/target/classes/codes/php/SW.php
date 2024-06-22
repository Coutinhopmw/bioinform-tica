<?php

function smith_score($seq1, $seq2, $match, $mismatch, $gap) {
    $len1 = strlen($seq1); 
    $len2 = strlen($seq2);

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

function score($char1, $char2) {
    return ($char1 == $char2) ? 1 : -1;
}

function smithWaterman($seq1, $seq2, $match, $mismatch, $gap) {
    // Traçar de volta para encontrar a subsequência alinhada de maior similaridade
    $matrix = smith_score($seq1, $seq2, $match, $mismatch, $gap);
    list($i, $j) = $matrix['score_pos'];
    $score_matrix = $matrix['matrix'];
    $align1 = '';
    $align2 = '';
    $barrinha = '';
    $gaps = 0;

    while ($i > 0 && $j > 0 && $score_matrix[$i][$j] > 0) {
        if ($score_matrix[$i][$j] == $score_matrix[$i - 1][$j - 1] + score($seq1[$i - 1], $seq2[$j - 1])) {
            $align1 = $seq1[$i - 1] . $align1;
            $align2 = $seq2[$j - 1] . $align2;
            if (score($seq1[$i - 1], $seq2[$j - 1]) == 1) {
                $barrinha = '|' . $barrinha;
            } else {
                $barrinha = ':' . $barrinha;
            }                
            $i--;
            $j--;
        } elseif ($score_matrix[$i][$j] == $score_matrix[$i - 1][$j] + $gap) {
            $align1 = $seq1[$i - 1] . $align1;
            $align2 = '-' . $align2;
            $barrinha = '-' . $barrinha;
            $i--;
            $gaps++;
        } else {
            $align1 = '-' . $align1;
            $align2 = $seq2[$j - 1] . $align2;
            $barrinha = '-' . $barrinha;
            $j--;
            $gaps++;
        }
    }
    return ['score' => $matrix['score'], 'gaps' => $gaps, 'align1' => $align1, 'align2' => $align2, 'barrinha' => $barrinha];
}

// Verificar se o script foi chamado corretamente
// if ($argc < 3) {
//     die("Usage: php script.php <seq1> <seq2>\n");
// }

// Argumentos da linha de comando
$seq1 = $argv[1];
//$seq2 = $argv[2];
$seq2 = "TTTCGGCGAATTGAGAGAAATTAGATGCGGTTTGTGTCTGAACCTTTTATCCTAGCGACGATTTTTTAAGGAAGTTGAATATGATCATCAAACCTAAAATTCGTGGATTTATCTGTACAACAACGCACCCAGTGGGTTGTGAAGCGAACGTAAAAGAACAAATTGCCTACACAAAAGCACAAGGTCCGATCAAAAACGCACCTAAGCGCGTGTTGGTTGTCGGATCGTCTAGCGGCTATGGTCTGTCATCACGCATCGCTGCGGCGTTTGGCGGTGGTGCGGCGACGATCGGCGTATTTTTCGAAAAGCCGGGCACTGACAAAAAACCAGGTACTGCGGGTTTCTACAATGCAGCAGCGTTTGACAAGCTAGCGCATGAAGCGGGCTTGTACGCAAAAAGCCTGAACGGCGATGCGTTCTCGAACGAAGCGAAGCAAAAAGCGATTGAGCTGATTAAGCAAGACCTCGGCCAGATTGATTTGGTGGTTTACTCATTGGCTTCTCCAGTGCGTAAAATGCCAGACACGGGTGAGCTAGTGCGCTCTGCACTAAAACCGATCGGCGAAACGTACACCTCTACCGCGGTAGATACCAATAAAGATGTGATCATTGAAGCCAGTGTTGAACCTGCGACCGAGCAAGAAATCGCTGACACTGTCACCGTGATGGGCGGTCAAGATTGGGAACTGTGGATCCAAGCACTGGAAGAGGCGGGTGTTCTTGCTGAAGGTTGCAAAACCGTGGCGTACAGCTACATCGGTACTGAATTGACTTGGCCAATCTACTGGGATGGCGCTTTAGGCCGTGCCAAGATGGACCTAGATCGCGCAGCGACAGCGCTGAACGAAAAGCTGGCAGCGAAAGGTGGTACCGCGAACGTTGCAGTTTTGAAATCAGTGGTGACTCAAGCAAGCTCTGCGATTCCTGTGATGCCGCTCTACATCGCGATGGTGTTCAAGAAGATGCGTGAACAGGGCGTGCATGAAGGCTGTATGGAGCAGATCTACCGCATGTTCAGTCAACGTCTGTACAAAGAAGATGGTTCAGCGCCGGAAGTGGATGATCACAATCGTCTGCGTTTGGATGACTGGGAACTGCGTGATGACATTCAGCAGCACTGCCGTGATCTGTGGCCACAAATCACTACAGAGAACCTGCGTGAGCTGACCGATTACGACATGTACAAAGAAGAGTTCATCAAGCTGTTTGGCTTTGGCATTGAAGGCATTGATTACGATGCTGACGTCAATCCAGAAGTCGAATTCGATGTGATTGATATCGAGTAAGAGAATTAACTCTTATCTTAAAAAGGCGCGTTATCGCGCCTTTTTTGTGTCCGGAGTACAGCATGAATACAGCAGGTTGC";

$start = microtime(true);
$result = smithWaterman($seq1, $seq2, 1, -1, -1);
$time = round((microtime(true) - $start),2);
$score = $result['score'];
$qtd_gap = $result['gaps'];
$barrinha = $result['barrinha'];
$percent = round(((100.0 * substr_count($barrinha, '|')) / strlen($barrinha)) * 100.0) / 100.0;

/*Percento:*/echo $percent . "\n";
/*Time:    */echo $time . "\n";
/*Score:   */echo $score . "\n";
/*Gap:     */echo $qtd_gap . "\n";

$text = "$time\n$score\n$qtd_gap\n$evalue";

    $arquivo = fopen("resultados_php.txt", "w");
    if ($arquivo == false){
        die ('Não foi possível criar o arquivo.');
    } else{
        fwrite($arquivo, $text);
    }
?>