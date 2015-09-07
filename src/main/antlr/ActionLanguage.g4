grammar ActionLanguage;
@header {
    package pl.edu.pw.mini.msi.knowledgerepresentation.grammar;
}

programm: instruction* EOF;

instruction
  : initialization
  | entry
  | scenario
  | query
  ;

entry
  : causes
  | TYPICALLY? invokes
  | releases
  | TYPICALLY? triggers
  | TYPICALLY? occurs
  | atsentence
  ;

initialization: INITIALLY logicalExpression;
causes: action CAUSES logicalExpression underCondition?;
invokes: action INVOKES action afterTime? underCondition?;
releases: action RELEASES fluent underCondition?;
triggers: logicalExpression TRIGGERS action;
occurs: action OCCURS AT time;
atsentence: logicalExpression AT time;

underCondition: IF logicalExpression;
afterTime: AFTER time;
action: '('actor ',' task ')';

fluentsList: fluent | fluentsList ',' fluent;

scenario: IDENTIFIER '{' actions ',' observations '}';

actions: ACS '=' '{' eventsList? '}';
eventsList: event | eventsList ',' event;
event: '(' action ',' time ')';

observations: OBS '=' '{' observationsList? '}';
observationsList: observation | observationsList ',' observation;
observation: '(' logicalExpression ',' time ')';

logicalExpression: fluent | '(' logicalExpression logicalOperator logicalExpression ')';

query
  : state
  | performed
  | involved
  ;

state: question logicalExpression AT time WHEN scenarioId;
performed: question PERFORMED action AT time WHEN scenarioId;
involved: basicQuestion INVOLVED actorsList WHEN scenarioId;

question
  : basicQuestion
  | TYPICALLY
  ;
basicQuestion
  : ALWAYS
  | EVER
  ;


actorsList: actors;
actors: actor | actors ',' actor;

fluent: IDENTIFIER | NOT IDENTIFIER;
actor: IDENTIFIER;
task: IDENTIFIER | NOT IDENTIFIER;
time: DecimalConstant;
scenarioId: IDENTIFIER;
logicalOperator: LOGICAL_AND | LOGICAL_IF | LOGICAL_OR;

WS: [ \n\t\r]+ -> skip;

ACS: 'ACS';
AFTER: 'after';
ALWAYS: 'always';
AT: 'at';
CAUSES: 'causes';
EVER: 'ever';
IF: 'if';
INITIALLY: 'initially';
INVOKES: 'invokes';
INVOLVED: 'involved';
NOT: '-';
OBS: 'OBS';
OCCURS: 'occurs';
PERFORMED: 'performed';
RELEASES: 'releases';
TRIGGERS: 'triggers';
TYPICALLY: 'typically';
WHEN: 'when';
IDENTIFIER : [a-zA-Z]+;
DecimalConstant: [0-9]+;
LOGICAL_OR: '||';
LOGICAL_IF: '=>';
LOGICAL_AND: '&&';
