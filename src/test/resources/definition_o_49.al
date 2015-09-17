initially ((-a && -b) && (-c && -d) )
(Janek, A) occurs at 0

typically (Janek, A) invokes (Janek, B)
typically (Janek, A) invokes (Janek, C)

(Janek, B) invokes (Janek, D)
(Janek, C) invokes (Janek, E)

(Janek, D) causes (a || b)
(Janek, E) causes (c || d)

(a || b) triggers (Janek, F)
(c || d) triggers (Janek, F)

ScenarioOne {
  ACS = {
    },
  OBS = {
  }
}

ever performed (Janek, D) at 2 when ScenarioOne
ever performed (Janek, E) at 2 when ScenarioOne
typically performed (Janek, F) at 3 when ScenarioOne
