% Adds numbers stored in memory addresses 85 to 9D until the sum is equal to zero. %
% As soon as the sum is 0, the program stores the address of the last added number %
% to cell 80, and the number itself to cell 84. Then the program halts. %
% --------------------------------------------------------------------- %
% The result of this program: cell 80 stores 96, and cell 84 stores D9  %
% ========== MAIN ========== %
ORG 00
LDA @80
STA 81
LDA 81
CMA
STA 83
ISZ 83
BUN 18
LDA @80
STA 84
HLT
% ========== SUBROUTINE ========== %
ORG 18
LDA 80
INC
STA 80
LDA 81
ADD @80
STA 81
BUN 04
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
