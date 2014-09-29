Tabelle "Teilnehmer"
- Spalten für die Daten anlegen
- Über Daten->Filter->AutoFilter die Spaltenköpfe sortierbar machen
- Bedingte Formatierung für Gerade/Ungerade Zellen: ISTGERADE(ZEILE()) bzw. ISTUNGERADE(ZEILE())
- Bedingte Formatierung für doppelte Startnummern: ZÄHLENWENN($A$3:$A$503;A3)>1
  Gerade/Ungerade für diese Spalte separat, damit es keinen Konflikt mit der Formatierung "doppelte Startnummer" gibt
- Formel für Altersklassen (Beispiel: Zeile 51). Alle Kommentare ab # entfernen und ins Excel reinkopieren

```
=WENN(
  ODER (
	# Wahr, wenn das Feld Geburtsdatum eine Altersklasse männlich enthält
    NICHT(ISTNV(SVERWEIS(D51;Veranstaltung.$E$10:$E$99;1;0)));
	# Wahr, wenn das Feld Geburtsdatum eine Altersklasse weiblich enthält
    NICHT(ISTNV(SVERWEIS(D51;Veranstaltung.$G$10:$G$99;1;0)))
  );
  # dann die Altersklasse ins Feld schreiben
  D51;
  # sonst...
  WENN(
    # Geburtstag ungültig oder Geschlecht leer
    ODER(N(D51)=0;ISTLEER(E51));
    # dann keine Altersklasse
    "";
    # sonst: Suche
	SVERWEIS(
      # AK-Alter: JahrDerVeranstaltung-Geburtsjahr. Geburtsjahr: wenn Datum (=große Zahl) in Geburtstag -> davon das Jahr. Sonst steht dort das Jahr
      JahrDerVeranstaltung-WENN(N(D51)<3000;D51;JAHR(D51));
      # in Altersklassen
      Veranstaltung.$C$10:$H$99;
      # Wenn Treffer -> Spalte+3 (männliche AK) bzw. +5 (weibliche AK)
      WENN(KLEIN(E51)="m";3;5);
      # Werte sind sortiert -> findet exakten oder nächstniedrigeren Treffer
      1
    )
  )
 )

=WENN(
  ODER (
    NICHT(ISTNV(SVERWEIS(D51;Veranstaltung.$E$10:$E$99;1;0)));
    NICHT(ISTNV(SVERWEIS(D51;Veranstaltung.$G$10:$G$99;1;0)))
  );
  D51;
  WENN(
    ODER(N(D51)=0;ISTLEER(E51));
    "";
	SVERWEIS(
      JahrDerVeranstaltung-WENN(N(D51)<3000;D51;JAHR(D51));
      Veranstaltung.$C$10:$H$99;
      WENN(KLEIN(E51)="m";3;5);
      1
    )
  )
 )
```


Oder das Gleich für Excel angepasst (Zeile 5, in dem Fall M/W und Geburtsdatum vertauscht:

```
=WENN(
  ODER(
    NICHT(ISTNV(SVERWEIS(E5;Veranstaltung!$E$10:$E$99;1;0)));
    NICHT(ISTNV(SVERWEIS(E5;Veranstaltung!$G$10:$G$99;1;0)))
  );
  E5;
  WENN(
    ODER(N(E5)=0;ISTLEER(E5));
    "";
    SVERWEIS(
      JahrDerVeranstaltung-WENN(N(E5)<3000;E5;JAHR(E5));
      Veranstaltung!$C$10:$H$99;
      WENN(KLEIN(D5)="m";3;5);
      1
    )
  )
)
```
