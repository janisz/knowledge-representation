------------------------

    initally night
    GoToBed causes sleep if night

    scenario = {}

    always/ever/typically performed GoToBed at 1 when scenario
    always/ever/typically night at 1 when scenario

------------------------

    initially night | day
    GoToBed causes sleep if night

    scenario = {}

    always/ever/typically performed GoToBed at 1 when scenario
    always/ever/typically night at 1 when scenario

------------------------

    initially night | day
    typically GoToBed causes sleep if night

    scenario = {}

    always/ever/typically performed GoToBed at 1 when scenario
    always/ever/typically night at 1 when scenario

------------------------

    initially night | day
    typically GoToBed causes sleep if night | day

    scenario = {}

    always/ever/typically performed GoToBed at 1 when scenario
    always/ever/typically night at 1 when scenario

------------------------

    initially night | day
    typically GoToBed causes sleep if night => day

    scenario = {}

    always/ever/typically performed GoToBed at 1 when scenario
    always/ever/typically night at 1 when scenario
