grammar Query;

ALWAYS : 'always';
EVER: 'ever';
GENERALLY: 'generally';
AT: 'at';
WHEN: 'when';
INVOLVED: 'involved';
PERFORMED: 'perfomed';

query
  : question fluent AT time WHEN scenarioId
  | question PERFORMED action AT time WHEN scenarioId
  | basicQuestion INVOLVED actor WHEN scenarioId
  ;

question
  : basicQuestion
  | 'generally'
  ;
basicQuestion
  : 'always'
  | 'ever'
  ;

action: actor task;
fluent: id | NOT id;
actor: id;
task: id;
scenarioId: id;
time: DecimalConstant;
id : Nondigit+;

fragment Nondigit : [a-zA-Z_];
fragment DecimalConstant : NonzeroDigit Digit*;
fragment NonzeroDigit : [1-9];
fragment Digit : [0-9];