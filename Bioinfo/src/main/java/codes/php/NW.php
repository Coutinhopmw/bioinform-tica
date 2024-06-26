<?php
function score($char1, $char2) {
    if ($char1 === $char2) {
        return 1;
    } else {
        return -1;
    }
}

function needleman_wunsch_reconstruct($seq1, $seq2, $match_score, $mismatch_penalty, $gap_penalty) {
    $inicio = microtime(true);

    $len1 = strlen($seq1);
    $len2 = strlen($seq2);
    $score_matrix = array_fill(0, $len1 + 1, array_fill(0, $len2 + 1, 0));

    for ($i = 0; $i <= $len1; $i++) {
        $score_matrix[$i][0] = $gap_penalty * $i;
    }
    for ($j = 0; $j <= $len2; $j++) {
        $score_matrix[0][$j] = $gap_penalty * $j;
    }

    for ($i = 1; $i <= $len1; $i++) {
        for ($j = 1; $j <= $len2; $j++) {
            $match_score_ij = $score_matrix[$i - 1][$j - 1] + score($seq1[$i - 1], $seq2[$j - 1]);
            $delete_seq1_ij = $score_matrix[$i - 1][$j] + $gap_penalty;
            $delete_seq2_ij = $score_matrix[$i][$j - 1] + $gap_penalty;
            $score_matrix[$i][$j] = max($match_score_ij, $delete_seq1_ij, $delete_seq2_ij);
        }
    }

    $i = $len1;
    $j = $len2;
    $gaps = 0;

    while ($i > 0 && $j > 0) {
        if ($score_matrix[$i][$j] == $score_matrix[$i - 1][$j - 1] + score($seq1[$i - 1], $seq2[$j - 1])) {
            $i--;
            $j--;
        } elseif ($score_matrix[$i][$j] == $score_matrix[$i - 1][$j] + $gap_penalty) {
            $i--;
            $gaps++;
        } else {
            $gaps++;
            $j--;
        }
    }

    while ($i > 0) {
        $gaps++;
        $i--;
    }
    while ($j > 0) {
        $gaps++;
        $j--;
    }

    $fim = microtime(true);
    $alignment_score = $score_matrix[$len1][$len2];

    echo round(($fim - $inicio), 2) . "\n";
    echo $alignment_score . "\n";
    echo $gaps . "\n";
    echo "34\n";
}
$seq1 = $argv[1];
$seq2 = $argv[2];
needleman_wunsch_reconstruct($seq1, $seq2, 1, -1, -1);
?>
