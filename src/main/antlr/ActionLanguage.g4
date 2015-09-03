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
  | TYPICALLY? releases
  | TYPICALLY? triggers
  | TYPICALLY? occurs
  | impossible
  ;

initialization: INITIALLY logicalExpression;
causes: action CAUSES logicalExpression afterTime? underCondition?;
invokes: action INVOKES action afterTime? underCondition?;
releases: action RELEASES fluent afterTime? underCondition?;
triggers: logicalExpression TRIGGERS action;
occurs: action OCCURS AT time;
impossible: IMPOSSIBLE action AT time underCondition?;

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
observation: '(' fluentsList ',' time ')';

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
  : EVER
  ;


actorsList: actors;
actors: actor | actors ',' actor;

fluent: IDENTIFIER | NOT IDENTIFIER;
actor: IDENTIFIER;
task: IDENTIFIER;
time: DecimalConstant;
scenarioId: IDENTIFIER;
logicalOperator: LOGICAL_AND | LOGICAL_IF | LOGICAL_OR;

WS: [ \n\t\r]+ -> skip;

ACS: 'ACS';
AFTER: 'after';
AT: 'at';
CAUSES: 'causes';
EVER: 'ever';
IF: 'if';
IMPOSSIBLE: 'impossible';
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
