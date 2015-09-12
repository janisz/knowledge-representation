initially ((a && -b) && (-c && -d) )
(Janek, A) occurs at 0
(Janek, A) releases a
(Janek, B) occurs at 1
(Janek, B) releases b if a
b triggers (Janek, C)
-a triggers (Janek, D)
(Janek, D) causes (a && c)
(Janek, C) causes c
(Janek, E) causes c

(Janek, F) causes d
typically d triggers (Janek, G)
(Janek, G) causes -d

ScenarioOne {
  ACS = {
    ((Janek, E), 2),
    ((Janek, F), 7)
    },
  OBS = {
  }
}

always c at 4 when ScenarioOne
ever a at 4 when ScenarioOne
ever (a && -b) at 4 when ScenarioOne
ever (-a || (a && -b)) at 4 when ScenarioOne
always (-a || (a && -b)) at 4 when ScenarioOne
