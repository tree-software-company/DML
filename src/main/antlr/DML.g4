grammar DML;

@header {
    package parser;
}

file: statement+;

statement: variableDeclaration;

variableDeclaration: TYPE IDENTIFIER '=' expression ';';

expression: additionExpression;

additionExpression: propertyAccessExpression ( '+' propertyAccessExpression )* ;

propertyAccessExpression: primaryExpression ( '.' IDENTIFIER )* ;

LINE_COMMENT: '//' ~[\r\n]* -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;

primaryExpression: STRING
                  | NUMBER
                  | BOOLEAN
                  | IDENTIFIER
                  | listExpression
                  | mapExpression
                  | '(' expression ')'
                  ;

listExpression: '[' expression (',' expression)* ']' ;

mapExpression: '{' pair (',' pair)* '}' ;

pair: STRING ':' expression ;

TYPE: 'string' | 'number' | 'boolean' | 'list' | 'map';

STRING: '"' .*? '"';
NUMBER: [0-9]+;
BOOLEAN: 'true' | 'false';
IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;
WS: [ \t\r\n]+ -> skip;
