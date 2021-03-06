# Reprezentacja wiedzy

![Alt Text](graphics/intro.png)


## Instalacja i uruchomienie

Aby uruchomić program należy dwa razy kliknąć w plik „program01/KnowledgeRepresentation.exe” (są dwa pliki o tej nazwie – plik aplikacji i plik ikony, należy wybrać plik aplikacji).

Jeśli program się nie uruchomi należy:  
1. Dwa razy kliknąć w „program02/jre-8u45-windows-i586.exe” i postępować według instrukcji instalatora najnowszej wersji Javy  
2. Po zakończeniu fazy z punktu 1. należy dwa razy kliknąć w „program02/knowledge representation/knowledge-representation-1.0.jar”

## Praca z programem

Okno programu składa się z

1. Pola tekstowego (podglądu) definicji
2. Pola tekstowego zapytań
3. Przycisku "Open", który otwiera menadżera plików w celu wybrania definicji do załadowania
4. Przycisku "Compute", który uruchamia obliczenia
5. Okna komunikatów
6. Menu wyboru maksymalnego czasu wykonania

![Alt Text](graphics/overview.png)


### Ładowanie definicji

Aby załadować definicje należy klinkąć przycisk "Open" i wybrać pożądany plik.
Plik zostanie załadowany do pamięci i wyświetlony w polu "Definitions" tylko
do odczytu.

### Wpisywanie zapytań

Zapytania zgodne z gramatyką ustalonego języka akcji należy wpisywać w polu
"Queries".

### Uruchamianie obliczeń i odczytywanie wyniku

W celu otrzymania odpowiedzi na zapytania należy kliknąć przycisk "Compute".
Po zakończeniu obliczeń pojawi się Okno komunikatów w listą wyników `true/false/null`
dla odpowiednio prawdy, fałszu i zapytania obecnie nieobsługiwanego.

![Alt Text](graphics/compute_result.png)


### Błędy

Jeśli wpisane zapytania, lub załadowany plik zawierają błędy, to po kliknięciu
przycisku "Compute" natychmiast pojawi się Okno komunikatów z informacją o błędzie
oraz o najbardziej prawdopodobnym miejscu jego wystąpienia.

![Alt Text](graphics/error_missing_coma.png)
![Alt Text](graphics/error_missing_decimal.png)


## Syntaktyka języka akcji

Gramatyka języka została opracowana w taki sposób aby wyeliminować
znaczenie białych znaków. Ponadto, aby ułatwić czytelność kodu
wszystkie sekcje są otoczone specjalnymi znakami.

1. Każda akcja ma postać `(aktor, zadanie)`
```javascript
ever performed (dog, CommitSuicide) at 4 when scenarioOne
```

2. Dostępne są następujące operatory logiczne, za pomocą których można budować wyrażenia z fluentów: 
    1. alternatywa `||`
    1. koniunkcja `&&`
    1. implikacja `=>`

3. Scenariusz ma syntaktykę opartą o format [JSON](http://json.org/)

```javascript
scenario {
            ACS = {
                ((Janek, takesCard), 3),
                ((Janek, locksTheDoor), 4),
                ((Janek, comeback), 10)
            },
            OBS = {
                (-hasCard, inHostel, 4),
                (hasCard, 5),
                (inHostel, 4)
            }
}
```

### Przykładowy program

* Plik definicji:

```javascript
initially (dogHungry && -dogDead)

(dogHungry && -dogDead) triggers (dog, Sad)
(dog, Sad) invokes (dog, CommitSuicide) after 5
(dog, CommitSuicide) causes dogDead if dogHungry && -dogDead

(dog, eats) causes -dogHungry

scenarioOne {
    ACS = {
    },
    OBS = {
    }
}
```

* Plik zapytań:

```javascript
ever dogHungry at 0 when scenarioOne
ever -dogHungry at 1 when scenarioOne
```