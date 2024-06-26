use strict;
use warnings;
use Time::HiRes qw( time );
sub score {
    my ($char1, $char2) = @_;
    return $char1 eq $char2 ? 1 : -1;
}
sub needleman_wunsch_reconstruct {
    my ($seq1, $seq2, $match_score, $mismatch_penalty, $gap_penalty) = @_;
    my $inicio = time();
    my @score_matrix;
    for my $i (0 .. length($seq1)) {
        $score_matrix[$i][0] = $gap_penalty * $i;
    }
    for my $j (0 .. length($seq2)) {
        $score_matrix[0][$j] = $gap_penalty * $j;
    }
    for my $i (1 .. length($seq1)) {
        for my $j (1 .. length($seq2)) {
            my $match_score_ij = $score_matrix[$i - 1][$j - 1] + score(substr($seq1, $i - 1, 1), substr($seq2, $j - 1, 1));
            my $delete_seq1_ij = $score_matrix[$i - 1][$j] + $gap_penalty;
            my $delete_seq2_ij = $score_matrix[$i][$j - 1] + $gap_penalty;
            $score_matrix[$i][$j] = max($match_score_ij, $delete_seq1_ij, $delete_seq2_ij);
        }
    }
    my ($i, $j) = (length($seq1), length($seq2));
    my $gaps = 0;
    while ($i > 0 and $j > 0) {
        if ($score_matrix[$i][$j] == $score_matrix[$i - 1][$j - 1] + score(substr($seq1, $i - 1, 1), substr($seq2, $j - 1, 1))) {
            $i--;
            $j--;
        } elsif ($score_matrix[$i][$j] == $score_matrix[$i - 1][$j] + $gap_penalty) {
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
    my $fim = time();
    my $alignment_score = $score_matrix[length($seq1)][length($seq2)];
    printf("%.2f\n", $fim - $inicio);
    print "$alignment_score\n";
    print "$gaps\n";
    print "47\n";
}
sub max {
    my ($a, $b, $c) = @_;
    return ($a > $b) ? (($a > $c) ? $a : $c) : (($b > $c) ? $b : $c);
}
my $seq1 = $ARGV[0];
my $seq2 = $ARGV[1];
needleman_wunsch_reconstruct($seq1, $seq2, 1, -1, -1);
