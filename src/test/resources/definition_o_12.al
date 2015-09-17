initially ((a && b) && (c && d))
(Janek, A) releases a
(Janek, B) releases b

(Janek, C) causes -c
(Janek, D) causes -d

ScenarioOne {
  ACS = {
    ((Janek, A), 0),
    ((Janek, B), 3),
    ((Janek, C), 6),
    ((Janek, D), 9)
    },
  OBS = {
  }
}

ScenarioTwo {
  ACS = {
    ((Janek, A), 2),
    ((Janek, B), 5),
    ((Janek, C), 8),
    ((Janek, D), 11)
    },
  OBS = {
  }
}

ever ((a && -b) && (-c && -d)) at 14 when ScenarioOne
ever ((-a && b) && (-c && -d)) at 14 when ScenarioOne
ever ((a && b) && (-c && -d)) at 14 when ScenarioOne
ever ((-a && -b) && (-c && -d)) at 14 when ScenarioOne
ever ((a && -b) && (-c && -d)) at 14 when ScenarioTwo
ever ((-a && b) && (-c && -d)) at 14 when ScenarioTwo
ever ((a && b) && (-c && -d)) at 14 when ScenarioTwo
ever ((-a && -b) && (-c && -d)) at 14 when ScenarioTwo
