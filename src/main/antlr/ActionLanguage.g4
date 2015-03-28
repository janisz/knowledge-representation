grammar ActionLanguage;

AFTER: 'after';
ALWAYS: 'always';
AT: 'at';
CAUSES: 'causes';
IF: 'if';
INITIALLY: 'initially';
IMPOSSIBLE: 'imnposible';
INVOKES: 'invokes';
NOT: 'not';
RELEASES: 'releases';
TRIGGERS: 'triggers';
TYPICALLY: 'typically';

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
fluentsList : fluent | fluentsList ',' fluent;

fluent: id | NOT id;
actor: id;
task: id;
time: DecimalConstant;
id : Nondigit+;

fragment Nondigit : [a-zA-Z_];
fragment DecimalConstant : NonzeroDigit Digit*;
fragment NonzeroDigit : [1-9];
fragment Digit : [0-9];