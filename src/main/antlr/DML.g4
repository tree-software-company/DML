grammar DML;

@header {
    package parser;
}

file: statement+;

statement
    : declaration
    | assignment
    | validation
    | functionCallStatement
    | functionDeclaration
    | importStatement
    | printStatement
    | returnStatement
    | varDeclaration
    | variableDeclaration
    | enumDeclaration
    | classDeclaration
    | classInstanceDeclaration
    | assertStatement
    | timezoneDeclaration
    ;

importStatement
    : 'import' STRING ';'
    ;

functionDeclaration
    : 'function' IDENTIFIER '(' parameterList? ')' '{' statement* '}'
    ;

parameterList
    : IDENTIFIER (',' IDENTIFIER)*
    ;

validation
    : 'regex' IDENTIFIER '=' STRING ';'
    | 'validate' IDENTIFIER 'matches' IDENTIFIER ';'
    ;

declaration
    : TYPE IDENTIFIER ('=' expression)? ';'
    ;

assignment
    : IDENTIFIER '.' IDENTIFIER '=' expression ';'
    ;

functionCallStatement: IDENTIFIER '(' argumentList? ')' ';' ;

printStatement: 'print' '(' expression ')' ';' ;

returnStatement: 'return' expression? ';' ;

varDeclaration: 'var' IDENTIFIER '=' expression ';' ;

variableDeclaration
    : modifier? TYPE IDENTIFIER '=' expression ';'
    ;

timezoneDeclaration
    : 'timezone' IDENTIFIER '=' STRING ';'?
    ;

enumDeclaration
    : 'enum' IDENTIFIER '{' (IDENTIFIER (',' IDENTIFIER)*)? '}'
    ;

classDeclaration
    : 'class' IDENTIFIER '{' classField+ '}'
    ;

classField
    : TYPE IDENTIFIER ';'
    ;

classInstanceDeclaration
    : IDENTIFIER IDENTIFIER '{' classAssignment* '}'
    ;

classAssignment
    : IDENTIFIER '=' expression ';'
    ;

assertStatement
    : 'assert' expression ';'
    ;

expression: comparisonExpression ;

comparisonExpression
    : additionExpression (COMPARISON_OPERATOR additionExpression)?
    ;

additionExpression
    : multiplicationExpression (('+' | '-') multiplicationExpression)*
    ;

multiplicationExpression
    : propertyAccessExpression (('*' | '/' | '%') propertyAccessExpression)*
    ;

propertyAccessExpression
    : primaryExpression ('.' IDENTIFIER)*
    ;

primaryExpression
    : STRING
    | NUMBER
    | BOOLEAN
    | IDENTIFIER ('(' argumentList? ')')?
    | listExpression
    | mapExpression
    | nowFunction
    | '(' expression ')'
    ;

argumentList: expression (',' expression)* ;

listExpression: '[' (expression (',' expression)*)? ']' ;

mapExpression: '{' (pair (',' pair)*)? '}' ;

pair: STRING ':' expression ;

nowFunction: 'now' '(' STRING? ')' ;

modifier: 'private' ;

TYPE: 'string' | 'number' | 'boolean' | 'list' | 'map' | 'url' | 'file' | 'char' | 'date' | 'datetime' | 'time' ;

IDENTIFIER: [a-zA-Z_][a-zA-Z0-9_]* ;

STRING: ('"' (~["\r\n] | '\\"')* '"') | ('\'' (~['\r\n] | '\\\'')* '\'') ;

NUMBER: [0-9]+ ('.' [0-9]+)? ;

BOOLEAN: 'true' | 'false' ;

COMPARISON_OPERATOR: '==' | '!=' | '>' | '<' | '>=' | '<=' ;

WS: [ \t\r\n]+ -> skip ;

COMMENT: '//' ~[\r\n]* -> skip ;

BLOCK_COMMENT: '/*' .*? '*/' -> skip ;
