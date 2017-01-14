#knowledge-representation

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2abe923f412e4101b8a6fa9dc58948f6)](https://www.codacy.com/app/janiszt/knowledge-representation?utm_source=github.com&utm_medium=referral&utm_content=janisz/knowledge-representation&utm_campaign=badger)
[![GitHub license](https://img.shields.io/badge/license-ISC-blue.svg)](https://raw.githubusercontent.com/janisz/knowledge-representation/master/LICENSE) [![Build Status](https://travis-ci.org/janisz/knowledge-representation.svg?branch=master)](https://travis-ci.org/janisz/knowledge-representation) [![Dependency Status](https://www.versioneye.com/user/projects/55633032366466001bc30100/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55633032366466001bc30100) [![Coverage Status](https://coveralls.io/repos/janisz/knowledge-representation/badge.svg?branch=master&service=github)](https://coveralls.io/github/janisz/knowledge-representation?branch=master)
##Scenariusze działań z projektami domyślymi

### Harmonogram


| Data                | Co             |
|--------------------|--------            |
|25.03|  część teoretyczna (my 14:00)|
|**15.04**| część teoretyczna | 
|**22.04**| część teoretyczna (termin zapasowy) |
|13.05| część praktyczna|
|**27.05**| oddanie testerom|
|03.06| testerzy zwracają autorom|
|**10.06**| (my 14:30)|

### Budowanie projektu
	make
	
### Uruchamianie

- dev `./gradlew run < plik_z_kodem_programu`
- prod - zip z aplikacją znajduje się w `build/distributions`, należy go rozpakować. Skrypt wykonywalny znajduje się 
w wewnątrz katalogu `bin`.

#### Zależności

1. Dokumentacja
	- Latex (`sudo apt-get install latexmk texlive-full`)
2. Kod
	- Java 8 (http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

### Użyte narzędzia
- [make](http://en.wikipedia.org/wiki/Make_%28software%29)
- [Gradle](https://gradle.org/)
- [LaTex](http://www.latex-project.org/)
- [Java](http://www.oracle.com/pl/java/overview/index.html)
- [ANTLR](http://www.antlr.org/)
- [jline](http://jline.github.io/jline2/)
- [Groovy](http://www.groovy-lang.org/)
- [Spock](http://spockframework.org/)
- [Guava](https://github.com/google/guava)
- [slf4j/logback](http://logback.qos.ch/index.html)
	

		
