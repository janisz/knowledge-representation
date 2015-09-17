initially ((a && b) && (c && d) )
(Janek, A) occurs at 2

typically (Janek, A) invokes (Janek, B) after 2 if (a || b)
typically (Janek, A) invokes (Janek, C) after 2 if (c || d)

(Janek, B) invokes (Janek, D) after 2 if (a || b)
(Janek, C) invokes (Janek, D) after 2 if (c || d)

ScenarioOne {
  ACS = {
    },
  OBS = {
  }
}

typically performed (Janek, D) at 6 when ScenarioOne