% ========== MAIN ========== %
00 04 % LDA (direct) %
01 81
02 08 % STA (direct) %
03 A2
04 02 % ADD (direct) %
05 80
06 08 % STA (direct) %
07 A0
08 42 % CMA; %
09 08 % STA (direct) %
0A A1
0B 20 % ISZ (direct) %
0C A1
0D 10 % BUN (direct) %
0E 20
0F 04 % LDA (direct) %
10 A2
11 60 % HLT; %
% ========== SUBROUTINE ========== %
20 84 % LDA (indirect) %
21 B0
22 08 % STA (direct) %
23 A2
24 02 % ADD (direct) %
25 A0
26 08 % STA (direct) %
27 A0
28 20 % ISZ (direct) %
29 B0
2A 10 % BUN (direct) %
2B 08
% ========== DATA ========== %
80 21
81 B5
82 37
83 08
84 5C
85 84
86 A1
87 1D
88 72
89 FF
8A F6
8B 43
8C 03
8D A9
8E D4
8F 19
90 31
91 D9  % will be equal to 0 %
92 47
93 82
94 14
95 52
96 07
97 CA
98 04
% ========== VARIABLES ========== %
A0 00 % reserved for addition result %
A1 00 % reserved for complement %
A2 00 % reserved for last value added %
B0 82 % pointer to third operand %
