grammar ActionLanguage;
@header {
    package pl.edu.pw.mini.msi.knowledgerepresentation.grammar;
}

programm: instruction* EOF;

instruction
  : initiallisation
  | entry
  | scenario
  | query
  ;

entry
  : TYPICALLY? causes
  | TYPICALLY? invokes
  | TYPICALLY? releases
  | TYPICALLY? triggers
  | TYPICALLY? occurs
  | impossible
  | always
  ;

initiallisation: INITIALLY fluentsList;
causes: action CAUSES fluentsList afterTime? underCondition?;
invokes: action INVOKES action afterTime? underCondition?;
releases: action RELEASES fluentsList afterTime? underCondition?;
triggers: fluentsList TRIGGERS action;
occurs: action OCCURS AT time;
impossible: IMPOSSIBLE action AT time underCondition?;
always: ALWAYS fluent;

underCondition: IF fluentsList;
afterTime: AFTER time;
action: '('actor ',' task ')';

fluentsList: '[' fluents ']';
fluents: fluent | fluents ',' fluent;

scenariosList: scenario | scenariosList scenario;
scenario: IDENTIFIER '{' actions ',' observations '}';

actions: ACS '=' '{' eventsList '}';
eventsList: event | eventsList ',' event;
event: '(' action ',' time ')';

observations: OBS '=' '{' observationsList '}';
observationsList: observation | observationsList ',' observation;
observation: '(' fluent ',' time ')';

query
  : question fluentsList AT time WHEN scenarioId
  | question PERFORMED action AT time WHEN scenarioId
  | basicQuestion INVOLVED actorsList WHEN scenarioId
  ;
question
  : basicQuestion
  | 'generally'
  ;
basicQuestion
  : 'always'
  | 'ever'
  ;


actorsList: '[' actors ']';
actors: actor | actors ',' actor;

fluent: IDENTIFIER | NOT IDENTIFIER;
actor: IDENTIFIER;
task: IDENTIFIER;
time: DecimalConstant;
scenarioId: IDENTIFIER;

WS: [ \n\t\r]+ -> skip;

ACS: 'ACS';
AFTER: 'after';
ALWAYS: 'always';
AT: 'at';
CAUSES: 'causes';
EVER: 'ever';
GENERALLY: 'generally';
IF: 'if';
IMPOSSIBLE: 'imposible';
INITIALLY: 'initially';
INVOKES: 'invokes';
INVOLVED: 'involved';
NOT: '-';
OBS: 'OBS';
OCCURS: 'occurs';
PERFORMED: 'perfomed';
RELEASES: 'releases';
TRIGGERS: 'triggers';
TYPICALLY: 'typically';
WHEN: 'when';
IDENTIFIER : [a-zA-Z]+;
DecimalConstant: [1-9] [0-9]*;