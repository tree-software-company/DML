grammar DML;

@header {
    package parser;
}

file: statement+;

statement: variableDeclaration;

variableDeclaration: TYPE IDENTIFIER '=' expression ';';

TYPE: 'string' | 'number' | 'boolean' | 'list' | 'map';

expression: STRING
          | NUMBER
          | BOOLEAN
          | IDENTIFIER
          | expression '+' expression
          | '(' expression ')';

STRING: '"' .*? '"';
NUMBER: [0-9]+;
BOOLEAN: 'true' | 'false';
IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;
WS: [ \t\r\n]+ -> skip;
