ORG 00
LDA 81
STA A2
ADD 80
STA A0
CMA
STA A1
ISZ A1
BUN 20
LDA A2
HLT
ORG 20
LDA @B0
STA A2
ADD A0
STA A0
ISZ B0
BUN 08
ORG 80
80 21
81 B5
82 37
STA 5C
LDA @A1
87 1D
88 72
89 FF
8A F6
8B 43
SUB A9
8E D4
8F 19
90 31
91 D9
92 47
ADD @14
95 52
96 07
97 CA
98 04
% ========== VARIABLES ========== %
A0 00 % reserved for addition result %
A1 00 % reserved for complement %
A2 00 % reserved for last value added %
B0 82 % pointer to third operand %
