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
- 17.09.2014 OpenOffice Läufererfassung und Zeitauswertung

Funktionen OpenOffice Läufererfassung und Zeitauswertung
--------------------------------------------------------

- keine Makros außer zum Einlesen der Stoppuhr
- Automatische Berechnung Altersklassen nach Jahr der Veranstaltung
- Erfassung von Läufern direkt in der Tabelle
- Automatische Erkennung, ob Geburtsjahr, Geburtsdatum oder Altersklasse eingegeben wurde
- Automatische Zuweisung der Altersklasse
- Makro, welches die letzte gespeicherte Stoppuhr im aktuellen Verzeichnis einließt
- Ermittlung der Platzierung je Strecke: nach Altersklasse, nach Geschlecht

Offen:
- Ausdruckbare Listen
- Urkunden über Seriendruckfunktion


Funktionen Stoppuhr
-------------------

- Zeitnahme mit Leertaste
- Startnummerneingabe mit Ziffern+Enter
- Zeitnahme und Startnummerneingabe sind unabhängig voneinander
- Escape sprigt von überall aus zur Startnummerneingabe und leert diese
- Bearbeiten von Nummern und Zeiten: Nummer oder Zeit Doppelklicken
- Einfügen und Löschen von Nummern und Zeiten: Strg+E - einfügen, Strg+X - löschen
- Hervorheben doppelt eingegebener Startnummern
- Speichern mit Strg + S im CSV-Format (Automatisch fortlaufende Nummern Stoppuhr_X.csv) im Arbeitsverzeichnis
- Automatisches Laden des letzten gespeicherten Standes aus dem Arbeitsverzeichnis



Offen (geplant für erste Version):
- Laden eines beliebigen gespeicherten Standes
- Excel-Export zur Auswertung
- Start-Funktion (derzeit startet die Uhr mit Programmstart)
- Neustart-Funktion
- Warung/Rückfrage beim Beenden/ bei Neustart
- Speichern unter neuer Nummer nur, wenn es Änderungen gab

Ideen für spätere Versionen:
- Packen und erstellen einer .exe mit Launch4j
- OpenOffice Export
- Verlauf bei jeder Änderung asynchron zwischenspeichern
- Auswertung integrieren? Ggf. Live-Anzeige der letzten Einläufe mit Platzierung auf 2. Bildschirm?

