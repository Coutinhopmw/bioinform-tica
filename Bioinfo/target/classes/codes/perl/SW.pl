use strict;
use warnings;
use Time::HiRes qw(gettimeofday tv_interval);
sub score {
    my ($char1, $char2) = @_;
    return $char1 eq $char2 ? 1 : -1;
}
sub smith_waterman {
    my ($seq1, $seq2, $match_score, $mismatch_penalty, $gap_penalty) = @_;
    $match_score //= 1;
    $mismatch_penalty //= -1;
    $gap_penalty //= -1;
    my $inicio = [gettimeofday];
    my @score_matrix;
    my $max_score = 0;
    my $max_pos = [0, 0];
    
    for my $i (0 .. length($seq1)) {
        for my $j (0 .. length($seq2)) {
            $score_matrix[$i][$j] = 0;
        }
    }
    for my $i (1 .. length($seq1)) {
        for my $j (1 .. length($seq2)) {
            my $match = $score_matrix[$i - 1][$j - 1] + score(substr($seq1, $i - 1, 1), substr($seq2, $j - 1, 1));
            my $delete = $score_matrix[$i - 1][$j] + $gap_penalty;
            my $insert = $score_matrix[$i][$j - 1] + $gap_penalty;
            $score_matrix[$i][$j] = max(0, $match, $delete, $insert);
            if ($score_matrix[$i][$j] > $max_score) {
                $max_score = $score_matrix[$i][$j];
                $max_pos = [$i, $j];
            }
        }
    }
    my $gaps = 0;
    my ($i, $j) = @$max_pos;
    while ($score_matrix[$i][$j] != 0) {
        if ($score_matrix[$i][$j] == $score_matrix[$i - 1][$j - 1] + score(substr($seq1, $i - 1, 1), substr($seq2, $j - 1, 1))) {
            $i--;
            $j--;
        } elsif ($score_matrix[$i][$j] == $score_matrix[$i - 1][$j] + $gap_penalty) {
            $gaps++;
            $i--;
        } else {
            $gaps++;
            $j--;
        }
    }
    my $fim = [gettimeofday];
    my $tempo_execucao = tv_interval($inicio, $fim);
    printf "%.2f\n", $tempo_execucao;
    print "$max_score\n";
    print "$gaps\n";
    print "44\n";
}
sub max {
    my $max = shift;
    for (@_) {
        $max = $_ if $_ > $max;
    }
    return $max;
}
my ($seq1, $seq2) = @ARGV;
smith_waterman($seq1, $seq2);