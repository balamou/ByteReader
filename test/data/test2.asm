% Adds numbers stored in memory addresses 85 to 9D until the sum is equal to zero. %
% As soon as the sum is 0, the program stores the address of the last added number %
% to cell 80, and the number itself to cell 84. Then the program halts. %
% --------------------------------------------------------------------- %
% The result of this program: cell 80 stores 96, and cell 84 stores D9  %
% ========== MAIN ========== %
00 84
01 80
02 08
03 81
04 04
05 81
06 42
07 08
08 83
09 20
0A 83
0B 10
0C 18
0D 84
0E 80
0F 08
10 84
11 60 % HALT %
% ========== SUBROUTINE ========== %
18 04
19 80
1A 50
1B 08
1C 80
1D 04
1E 81
1F 82
20 80
21 08
22 81
23 10
24 04
% ========== DATA ========== %
80 85  % i %
81 00  % SUM %
82 FF  % -1 %
83 00  % Tmp %
84 00  % Last %
% ========== ARRAY ========== %
85 21
86 B5
87 37
88 08
89 5C
8A 84
8B A1
8C 1D
8D 72
8E FF
8F F6
90 43
91 03
92 A9
93 D4
94 19
95 31
96 D9  % the sum will equal to 0 %
97 47
98 82
99 14
9A 52
9B 07
9C CA
9D 04
