initially ((-a && -b) && (-c && -d) )
(Janek, A) occurs at 2

typically (Janek, A) invokes (Janek, B) after 2 if -a
typically (Janek, A) invokes (Janek, C) after 2 if -b

(Janek, B) invokes (Janek, D) after 2 if -c
(Janek, C) invokes (Janek, E) after 2 if -d

(Janek, D) causes (a || b) if -a
(Janek, E) causes (c || d) if -c

(a || b) triggers (Janek, F)
(c || d) triggers (Janek, F)

ScenarioOne {
  ACS = {
    },
  OBS = {
  }
}

ever performed (Janek, D) at 6 when ScenarioOne
ever performed (Janek, E) at 6 when ScenarioOne
typically performed (Janek, F) at 7 when ScenarioOne
