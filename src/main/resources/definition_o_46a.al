initially ((a &&- b) && (-c && d) )
(Janek, A) occurs at 2
(Janek, A) causes ((b || c) && (-b || -c)) if (a && d)

typically b triggers (Janek, B)
(Janek, B) causes -b if b
typically c triggers (Janek, C)
(Janek, C) causes -c if c

(Janek, B) invokes (Janek, D) after 2 if d
(Janek, C) invokes (Janek, D) after 2 if d

ScenarioOne {
  ACS = {
    },
  OBS = {
  }
}

typically performed (Janek, D) at 5 when ScenarioOne