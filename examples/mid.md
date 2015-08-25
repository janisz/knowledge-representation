### Kod programu
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
  night, 1

  Wakeup at 10
}

always/ever/typically performed GoToBed at 1 when scenario
always/ever/typically night at 1 when scenario
```
### Zamiana na formę predykatów
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

(night, 1) in H
(Wakeup, 10) in E

T_inf = 10


```
### Musimy rozmieścić w czasie wszystkie fluenty i akcje

  * Fluenty
    - night
    - day
    - sleep
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

### Wyznaczamy zbiór okluzji

*Dla uproszczenia nie przejmujemy się czasem*

```python
O(GoTOBed) = { sleep, nap }
O(WakeUp) = { tired }
O(Eat) = { hungry }
O(Sunrise) = { day, night }
```

Zbiór okluzji jest nam potrzebny aby zredukować liczbę sprawdzanych przypadków. 
Generujemy wszystkie możliwe wersje przebiegu akcji (jest ich zdecydowanie mniej niż możliwych 
kombinacji fluentów), a następnie dla każdego przypadku generujemy możliwe dla niego 
kombinacje fluentów. Dzięki temu jeśli w czasie `t` ma zajść akcja `a` to sprawdzimy
tylko te przypadki w których coś się może zmienić, zamiast wszystkich. Np. w chwili 4,
wykonana została akcja `GoToBed` to sprawdzimy tylko zmianę `sleep` i `nap` czyli w sumie
4 przypadki a nie 256.

### Odrzucamy niepoprawne scenariusze

Na przykład scenariusz w którym `(night, 5) not in H` nie jest poprawny i powinien
zostać odrzucony.

Jeden ze scenariuszy może wyglądać tak:

- H

|T\F|night|day|sleep|tired|hungry|headache|hangOver|
----|-----|---|-----|-----|------|--------|---------
|0  |0    |1  |0    |1    |0     |1       |0       |
|1  |1    |0  |0    |1    |0     |1       |0       |
|2  |1    |0  |0    |1    |0     |1       |0       |
|3  |1    |0  |0    |1    |0     |1       |0       |
|4  |1    |0  |0    |1    |0     |1       |0       |
|5  |1    |0  |0    |1    |0     |1       |0       |
|6  |1    |0  |1    |1    |0     |1       |0       |
|7  |1    |0  |1    |1    |0     |1       |0       |
|8  |1    |0  |1    |1    |0     |1       |0       |
|9  |1    |0  |1    |1    |0     |1       |0       |
|10 |1    |0  |1    |1    |0     |1       |0       |
|11 |0    |1  |0    |1    |0     |1       |0       |

- E

|T\F|GoToBed|Wakeup|Eat|NOP|
----|-------|------|---|----
|0  |1      |0     |0  |0  |
|1  |0      |0     |0  |1  |
|2  |0      |0     |0  |1  |
|3  |0      |0     |0  |1  |
|4  |0      |0     |0  |1  |
|5  |0      |0     |0  |1  |
|6  |0      |0     |0  |1  |
|7  |0      |0     |0  |1  |
|8  |0      |0     |0  |1  |
|9  |0      |0     |0  |1  |
|10 |0      |1     |0  |0  |
|11 |0      |0     |0  |1  |


### Drukujemy odpowiedzi na pytania

Mamy już pełen schemat wykonania dla wszystkich możliwych scenariuszy więc możemy
łatwo odpowiedzieć na pytania.

Pytania
-------
- Co ze zbiorem N? Jak powinien być uzupełniany?
- Co ze zbiorem O? Co się stanie jeśli nie będziemy go minimalizować?
- Jak sprawdzać które opcje są poprawne względem definicji?
