initially ((a && -b) && ((-c && d) && (e && f)) )
(Janek, A) occurs at 2
(Janek, A) causes ((b || c) && (-b || -c)) if (a && d)

typically b triggers (Janek, B)
(Janek, B) causes (-b && -e)
typically c triggers (Janek, C)
(Janek, C) causes (-c && -f)

((e || f) && (-e || -f)) at 6

typically (Janek, D) occurs at 6

ScenarioOne {
  ACS = {
    },
  OBS = {
  }
}

typically performed (Janek, D) at 6 when ScenarioOne