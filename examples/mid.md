------------------------

    initially night or day
    typically GoToBed causes sleep if night
    typically GoToBed causes nap if day
    hungry triggers Eat

    GoToBed invokes WakeUp after 8 if night
    WakeUp releases tired after 1 if -hangOver, -headache

    Eat releases Hungry
    impossible Eat if night

    Sunrise causes day if night
    Sunrise causes -night if night


    scenario = {
      night, 5

      Wakeup at 13
    }

    always/ever/typically performed GoToBed at 1 when scenario
    always/ever/typically night at 1 when scenario
