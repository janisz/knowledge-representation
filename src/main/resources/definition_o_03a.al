initially (a && b)
typically a triggers (Janek, A)
(Janek, A) causes -a
-a at 1
(a || b) at 1

ScenarioOne {
  ACS = {
    },
  OBS = {
  }
}

ScenarioTwo {
  ACS = {
    ((Janek, B), 0)
    },
  OBS = {
  }
}

typically performed (Janek, A) at 0 when ScenarioOne
typically performed (Janek, B) at 0 when ScenarioTwo
always (-a && b) at 1 when ScenarioOne