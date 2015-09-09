initially a
a triggers (Janek, -A)


ScenarioOne {
  ACS = {
    ((Janek, A), 0)
    },
  OBS = {
  }
}

ScenarioTwo {
  ACS = {
    ((Janek, A), 3)
    },
  OBS = {
  }
}

ScenarioThree {
  ACS = {
    ((Janek, B), 0)
    },
  OBS = {
  }
}

ScenarioFour {
  ACS = {
    ((Janek, B), 3)
    },
  OBS = {
  }
}

always performed (Janek, A) at 0 when ScenarioOne
always performed (Janek, A) at 3 when ScenarioTwo
always performed (Janek, B) at 0 when ScenarioThree
always performed (Janek, B) at 3 when ScenarioFour