% Generates a Fibonacci sequence in memory cells 80 to 8B %
% The result is a sequence: 01, 01, 02, 03, 05, 08, 0D, 15, 22, 37, 59, 90 %
% ========== MAIN ========== %
00 04  % LDA (direct) %
01 a0
02 42  % CMA %
03 08  % STA (direct) %
04 a0
05 20  % ISZ (direct) %
06 a0
07 10  % BUN (direct) %
08 20
09 60  % HLT %
% ========== SUBROUTINE ========== %
20 84  % LDA (indirect) %
21 a1
22 82  % ADD (indirect) %
23 a2
24 88  % STA (indirect) %
25 a3
26 04  % LDA (direct) %
27 a1
28 50  % Inc %
29 08  % STA (direct) %
2a a1
2b 50  % Inc %
2c 08  % STA (direct) %
2d a2
2e 50  % Inc %
2f 08  % STA (direct) %
30 a3
31 10  % BUN (direct) %
32 05
% ========== DATA ========== %
80 01
81 01
a0 0a  % loop counter, which will be done 10 times %
a1 80  % pointer to the first number to be added %
a2 81  % pointer to the second number to be added %
a3 82  % pointer to memory location where result will be stored %
