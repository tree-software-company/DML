grammar DML;

@header {
    package parser;
}

file: statement+;

statement: variableDeclaration
         | enumDeclaration
         | classDeclaration
         | classInstanceDeclaration
         | assignment;


variableDeclaration: modifier? TYPE IDENTIFIER '=' expression ';';

enumDeclaration: 'enum' IDENTIFIER '=' '[' IDENTIFIER (',' IDENTIFIER)* ']' ';';

classDeclaration: 'class' IDENTIFIER '{' classField* '}' ';';

classField: TYPE IDENTIFIER ';';

classInstanceDeclaration: IDENTIFIER IDENTIFIER '{' classAssignment* '}' ';';

classAssignment: IDENTIFIER '=' expression ';';

assignment: IDENTIFIER '.' IDENTIFIER '=' expression ';';

modifier: 'private';

expression: additionExpression;

additionExpression: propertyAccessExpression ( '+' propertyAccessExpression )* ;

propertyAccessExpression: primaryExpression ( '.' IDENTIFIER )* ;

primaryExpression:
      nowFunction
    | STRING
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

TYPE: 'string' | 'number' | 'boolean' | 'list' | 'map' | 'date' | 'datetime' | 'time'| 'url' | 'file' | 'char';

STRING: '"' ( ~["\\] | '\\' . )* '"';
NUMBER: [0-9]+ ('.' [0-9]+)?;
BOOLEAN: 'true' | 'false';
IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]*;
nowFunction
    : 'now' '(' (STRING)? ')'
    ;

WS: [ \t\r\n]+ -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;
