initially ((a && -b) && -c)
(Janek, A) occurs at 0
(Janek, A) releases a
(Janek, B) occurs at 1
(Janek, B) releases b if a
b triggers (Janek, C)
-a triggers (Janek, D)
(Janek, D) causes (a && c)

ScenarioOne {
  ACS = {
    ((Janek, D), 2)
    },
  OBS = {
  }
}

ever c at 4 when ScenarioOne
ever -a at 4 when ScenarioOne
ever (a && -b) at 4 when ScenarioOne
ever (-a || (a && -b)) at 4 when ScenarioOne
always (-a || (a && -b)) at 4 when ScenarioOne
