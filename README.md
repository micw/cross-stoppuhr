cross-stoppuhr
==============

[![Build Status](https://drone.io/github.com/micw/cross-stoppuhr/status.png)](https://drone.io/github.com/micw/cross-stoppuhr/latest)

Eine Stoppuhr für die manuelle Zeitnahme Volksläufe bis ca. 300 Teilnehmern.

Die Idee stammt aus dem Programm "Cross" der Firma Rieping-Software, welches allerdings
seit Jahren nicht weiterentwickelt wird und inzwischen eingestellt ist. "Cross" war zwar nicht das
tollste Programm, hatte aber die meiner Meinung nach beste Umsetzung einer Stoppuhr.

Download
--------

Ein ausführbares .jar liegt unter https://drone.io/github.com/micw/cross-stoppuhr/files/target/cross-stoppuhr-1.0.0-SNAPSHOT.one-jar.jar - es
muss Java 7 installiert sein, um es zu starten (https://www.java.com/de/download/).


Stand
-----

- 01.09.2014 erster Prototyp
- 16.09.2014 viele Funktionen

Funktionen
----------

- Zeitnahme mit Leertaste
- Startnummerneingabe mit Ziffern+Enter
- Zeitnahme und Startnummerneingabe sind unabhängig voneinander
- Escape sprigt von überall aus zur Startnummerneingabe und leert diese
- Bearbeiten von Nummern und Zeiten: Nummer oder Zeit Doppelklicken
- Einfügen und Löschen von Nummern und Zeiten: Strg+E - einfügen, Strg+X - löschen
- Hervorheben doppelt eingegebener Startnummern

Geplant für erste Version:
- Verlauf bei jeder Änderung asynchron zwischenspeichern
- Excel-Export zur Auswertung
- Packen und erstellen einer .exe mit Launch4j

Ideen für spätere Versionen:
- Doppelte Startnummern hervorheben
- OpenOffice Export
- Auswertung integrieren? Ggf. Live-Anzeige der letzten Einläufe mit Platzierung auf 2. Bildschirm?

