grammar Scenario;

ACS: 'ACS';
OBS: 'OBS';

scenario: id '{' actions ',' observations '}';

actions : ACS '=' '{' eventsList '}';
eventsList : event | eventsList ',' event;
event: '(' action ',' time ')';

observations : '{' observationsList '}';
observationsList : observation | observationsList ',' observation;
observation: '(' fluent ',' time ')';

action: actor task;
fluent: id | NOT id;
actor: id;
task: id;
time: DecimalConstant;
id : Nondigit+;

fragment Nondigit : [a-zA-Z_];
fragment DecimalConstant : NonzeroDigit Digit*;
fragment NonzeroDigit : [1-9];
fragment Digit : [0-9];