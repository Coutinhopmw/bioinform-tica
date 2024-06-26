#!/usr/bin/perl
use strict;
use warnings;
use Time::HiRes qw( time );
use constant MATCH => 1;
use constant MISMATCH => -1;
use constant GAP => -1;

sub nwa {
  my $start = time();
  #INICIALIZAÇÃO
  my ($seq1, $seq2) = @_;
  my $len_1 = length($seq1);
  my $len_2 = length($seq2);
  my @mat;
  my @mat_dir;
  for my $i (0 .. $len_2 + 1) {
    for my $j (0 .. $len_1 + 1) {
      $mat[$i][$j] = 0;
      if($i == 0){
        $mat[$i][$j] = GAP*$j;
      }
      if($j == 0){
        $mat[$i][$j] = GAP*$i
      }
      $mat_dir[$i][$j] = 0;
    }
  }
  #PREENCHIMENTO
  for my $i (1 .. $len_2){
    for my $j (1 .. $len_1){
      my $x = 0;
      my $y = 0;
      my $z = 0;
      if(substr($seq1, $j-1, 1) eq substr($seq2, $i-1, 1)){
        $x = $mat[$i-1][$j-1] + MATCH;
      }else{
        $x = $mat[$i-1][$j-1] + MISMATCH;
      }
      $y = $mat[$i-1][$j] + GAP;
      $z = $mat[$i][$j-1] + GAP;
      if($x >= $y && $x >= $z){
        $mat[$i][$j] = $x;
        $mat_dir[$i][$j] = 3;
      }elsif($y >= $x && $y >= $z){
        $mat[$i][$j] = $y;
        $mat_dir[$i][$j] = 1;
      }elsif($z > $x && $z > $y){
        $mat[$i][$j] = $z;
        $mat_dir[$i][$j] = 2;
      }
    }
  }
    for my $i (0 .. $len_2) {
      for my $j (0 .. $len_1) {
        print $mat[$i][$j], " ";
      }
      print "\n";
    }
  #ALINHAMENTO
  my $gaps = 0;
  my $score = $mat[$len_2][$len_1];
  while($len_1 != 0 && $len_2 != 0){
    if($mat_dir[$len_2][$len_1] == 1){
      $len_1 = $len_1 - 1;
      $gaps = $gaps + 1;
    }elsif($mat_dir[$len_2][$len_1] == 2){
      $len_2 = $len_2 - 1;
      $gaps = $gaps + 1;
    }else{
      $len_1 = $len_1 - 1;
      $len_2 = $len_2 - 1;
    }
  }
  my $end = time();
  print "\ntem_de_exec: ", $end - $start;
  print "\nscore: ", $score;
  print "\ngaps: ", $gaps;
  print "\nlin_de_cod: 79\n";
}

nwa("AGACTAGTTAC", "CGAGACGT");
