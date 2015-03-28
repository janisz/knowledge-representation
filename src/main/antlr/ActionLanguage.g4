grammar ActionLanguage;

ACS: 'ACS';
AFTER: 'after';
ALWAYS: 'always';
AT: 'at';
CAUSES: 'causes';
EVER: 'ever';
GENERALLY: 'generally';
IF: 'if';
IMPOSSIBLE: 'imnposible';
INITIALLY: 'initially';
INVOKES: 'invokes';
INVOLVED: 'involved';
NOT: 'not';
OBS: 'OBS';
PERFORMED: 'perfomed';
RELEASES: 'releases';
TRIGGERS: 'triggers';
TYPICALLY: 'typically';
WHEN: 'when';


typicallyCauses: TYPICALLY causes;
typicallyInvokes: TYPICALLY invokes;
typicallyReleases: TYPICALLY releases;
typicallyTriggers: TYPICALLY triggers;

initiallisation: INITIALLY fluentsList;
causes: action CAUSES fluentsList underCondition;
invokes: action INVOKES action afterTime underCondition;
releases: action RELEASES fluentsList afterTime underCondition;
triggers: fluentsList TRIGGERS action;
impossible: IMPOSSIBLE action AT time underCondition;
always: ALWAYS fluent;

underCondition: IF fluentsList | ;
afterTime: AFTER time | ;
action: actor task;
fluentsList: fluent | fluentsList ',' fluent;


scenario: id '{' actions ',' observations '}';

actions: ACS '=' '{' eventsList '}';
eventsList: event | eventsList ',' event;
event: '(' action ',' time ')';

observations: '{' observationsList '}';
observationsList: observation | observationsList ',' observation;
observation: '(' fluent ',' time ')';


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


fluent: id | NOT id;
actor: id;
task: id;
time: DecimalConstant;
scenarioId: id;
id: Nondigit+;

fragment Nondigit: [a-zA-Z_];
fragment DecimalConstant: NonzeroDigit Digit*;
fragment NonzeroDigit: [1-9];
fragment Digit: [0-9];