<?php
function score($char1, $char2){
    return ($char1==$char2) ? 1 : -1;
}

function needleman_score($seq1, $seq2, $match_penalty, $mismatch_penalty, $gap_penalty){
    $len1 = strlen($seq1); $len2 = strlen($seq2);

    $score_matrix = array_fill(0 ,$len1+1, array_fill(0, $len2+1, 0));

    for($i = 1; $i <= $len1; $i++){
        for($j = 1; $j <= $len1; $j++){
            $score_diag = $score_matrix[$i-1][$j-1] + (score($seq1[$i], $seq2[$j]) ? $match_penalty : $mismatch_penalty);
            $score_up = $score_matrix[$i-1][$j] + $gap_penalty;
            $score_left = $score_matrix[$i][$j-1] + $gap_penalty;
            $score_matrix[$i][$j] = max($score_diag, $score_left, $score_up);
        }
    }

    return ['score' => $score_matrix[$len1+1], 'matrix' => $score_matrix];
}

function needleman_wusnch($seq1, $seq2, $match_penalty, $mismatch_penalty, $gap_penalty){
    $matrix = needleman_score($seq1, $seq2, $gap_penalty, $match_penalty, $mismatch_penalty);
    $score_matrix = $matrix['matrix'];
    $align1 = '';
    $align2 = '';
    $barrinha = '';
    $gaps = 0;
    $i = (strlen($seq1)+1); $j = (strlen($seq2));

    while($i > 0 && $j > 0){
        if($score_matrix[$i][$j] == $score_matrix[$i-1][$j-1] + score($seq1[$i-1], $seq2[$j-1])){
            $align1 = $seq1[$i-1] . $align1;
            $align2 = $seq2[$j-1] . $align2;
            if (score($seq1[$i - 1], $seq2[$j - 1]) == 1) {
                $barrinha = '|' . $barrinha;
            } else {
                $barrinha = ':' . $barrinha;
            }    
            $i--;
            $j--;
        } elseif($score_matrix[$i][$j] == $score_matrix[$i-1][$j] + $gap_penalty){
            $align1 = $seq1[$i-1] . $align1;
            $align2 = '-' . $align2;
            $barrinha = '-' . $barrinha;
            $i--;
            $gaps++;

        } else {
            $align1 = '-' . $align1;
            $align2 = $seq2[$j-1] . $align2;
            $barrinha = '-' . $barrinha;
            $j--;
            $gaps++;
        }
    }
    $percent = round(((100.0 * substr_count($barrinha, '|')) / strlen($barrinha)) * 100.0) / 100.0;

    return ['score' => $matrix['score'], 'gaps' => $gaps, 'homo' => $percent];
}

$seq1 = $argv[1];

$start = microtime(true);
$result = needleman_wusnch($seq1, $seq2, 1, -1, -1);
$time = round((microtime(true) - $start),2);
$score = $result['score'];
$qtd_gap = $result['gaps'];
$homology = $result['homo'];

/*Percento:*/echo $homology . "\n";
/*Time:    */echo $time . "\n";
/*Score:   */echo $score . "\n";
/*Gap:     */echo $qtd_gap . "\n";

?>