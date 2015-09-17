
action(gotobed).
action(wakeup).

e(wakeup, 13).
e(gotobed, X) :- between(0, 20, X).
h(night, X) :- between(0, 20, X).

solution(X) :-
  h(night, X),
  e(gotobed, X),
  e(wakeup, Y),
  Y is (X + 8).
