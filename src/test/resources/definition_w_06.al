initially (-hasA && -hasB)
(Janek, buy) causes (hasA || hasB)
(Janek, buy) releases hasA if -hasA
(Janek, buy) releases hasB if -hasB

ScenarioOne {
  ACS = {
      ((Janek, buy), 0)
    },
  OBS = {
  }
}

always performed (Janek, buy) at 0 when ScenarioOne
ever (-hasA && hasB) at 1 when ScenarioOne
ever (hasA && -hasB) at 1 when ScenarioOne
ever (hasA && hasB) at 1 when ScenarioOne
always (-hasA && hasB) at 1 when ScenarioOne
always (hasA && -hasB) at 1 when ScenarioOne
always (hasA && hasB) at 1 when ScenarioOne
