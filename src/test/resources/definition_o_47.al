initially ((a && b) && (c && d) )
(Janek, A) occurs at 0
(Janek, A) causes ((b || c) && (-b || -c))

typically b triggers (Janek, B)
(Janek, B) causes -b
typically c triggers (Janek, C)
(Janek, C) causes -c

((b || c) && (-b || -c)) at 2

typically (Janek, D) occurs at 2

ScenarioOne {
  ACS = {
    },
  OBS = {
  }
}

typically performed (Janek, D) at 2 when ScenarioOne