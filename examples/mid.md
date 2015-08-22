1. Kod programu
```python
initially night or day
typically GoToBed causes sleep if night
typically GoToBed causes nap if day
hungry triggers Eat

GoToBed invokes Wakeup after 8 if night
Wakeup releases tired after 1 if -hangOver, -headache

Eat releases Hungry
impossible Eat if night

Sunrise causes day if night
Sunrise causes -night if night


scenario = {
  night, 5

  Wakeup at 13
}

always/ever/typically performed GoToBed at 1 when scenario
always/ever/typically night at 1 when scenario
```
2. Zamiana na formę predykatów
```python
(a, t_a) in E and (b, t_b) in E => t_a ≠ t_b or a = b

(night, t_1) in H and (GoToBed, t_1) in E and (GoToBed, t_1) in N => (sleep, t_1+1) in H
(day, t_2) in H and (GoToBed, t_2) in E and (GoToBed, t_2) in N => (nap, t_2+1) in H
(hungry, t_3) in H => (Eat, t_3+1) in E

(night, t_4) in H and (GoToBed, t_4) in E => (Wakeup, t_4+8) in E
((hangOver, t_5) not in H and (headache, t_5) not in H) and (Wakeup, t_5) in E => (tired, t_5+8) not in H

(Eat, t_6) in E => (hungry, t_6+1) not in H
(night, t_7) in E => (Eat, t_7) not in E

(night, t_8) in H and (Sunrise, t_8) in E => (day, t_8+1) in H
(night, t_9) in H and (Sunrise, t_9) in E => (night, t_9+1) not in H

(night, 5) in H
(Wakeup, 13) in E

T_inf = 10


```
3. Musimy rozmieścić w czasie wszystkie fluenty i akcje

    * Fluenty
      - night
      - day
      - sleep
      - night
      - tired
      - hungry
      - headache
      - hangOver
    * Akcje
      - GoToBed
      - Wakeup
      - Eat
      - [NOP](https://en.wikipedia.org/wiki/NOP)

W jednej chwili może być tylko jedna akcja lecz wiele fluentów.
Zakładając, że horyzont czasowy wynosi 10 chwil to mamy 10 × 2^8 = 2560 kombinacji
zbioru H, oraz 10 × 4 = 40 możliwości zbioru E i tele samo zbioru N.
W sumie daje nam to 2560 × 40 × 40 = 4096000 opcji do przejżenia.

4. Odrzucamy niepoprawne scenariusze

Na przykład scenariusz w którym `(night, 5) not in H` nie jest poprawny i powinien
zostać odrzucony.

5. Drukujemy odpowiedzi na pytania

Mamy już pełen schemat wykonania dla wszystkich możliwych scenariuszy więc możemy
łatwo odpowiedzieć na pytania.

Pytania
-------
- Co ze zbiorem N? Jak powinien być uzupełniany?
- Co ze zbiorem O? Co się stanie jeśli nie będziemy go minimalizować?
- Jak sprawdzać które opcje są poprawne względem definicji?
