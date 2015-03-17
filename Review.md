# Offene Fragen unsererseits #
  * Die Hauptklasse delegiert alle nicht abgefangenen Keystrokes an die Panels (meine bevorzugte Lösung, aber keine Ahnung wie das funktioniert). Wenn der Fokus auf der JTabbedPane liegt, sollen die Keystrokes in das jeweils selektierte TabbedPane Panel delegiert werden
  * Tab Layout Policy? Was würden Sie, Herr Stolze, am ehesten einsetzen? WRAP\_TAB\_LAYOUT oder SCROLL\_TAB\_LAYOUT?
  * Wie funktioniert das mit der minimum und preferred Size? Was müssen wir setzen, damit ein Fenster nicht kleiner gemacht werden kann als eine vorgegebene Grösse?
  * Wieso ist das BookDetailJPanel Fenster so hoch? Die Tabelle nimmt irgendwie so viel Platz ein, obwohl wir nichts definiert haben
  * Was machen, wenn ein Tab geschlossen wird, der noch ungespeicherte Änderungen hat? JOptionPane anzeigen, ob gespeichert werden soll? Aber diesen ignorieret der Benutzer ja dann auch mit der Zeit und macht seine Standardaktion (Ignorieren, Wegklicken wasauchimmmer)

# Bekannte Bugs #
  * Wird das Buch in der Tabelle bearbeitet, so wird der Tab auf „dirty“ gesetzt und der Speichern-Knopf kann betätigt werden
  * Minimumsize funktioniert nicht und das Layout skaliert beim Verkleinern auch nicht immer optimal
  * Einige Swing Bugs, dass bei der Action/InputMap die Keystrokes nachher nicht gekillt, sondern an andere GUI-Elemente geschickt werden (bsp. ALT+V um Bücher anzuschauen löst teilweise die Bearbeitung in der Tabelle aus was zu ganz doofem Verhalten führt)


# uns bekannte, fehlende Features #
  * Validierung fehlt komplett
  * Buchrückgabe

# was wir noch geplant haben #
  * CellRenderer (Zebra Look&Feel, Farben, je nachdem ob eine Kopie vorhanden ist etc.)
  * Handbuch ist in Arbeit
  * Defautbutton bei allen Frames (hat bisher zu Bugs geführt, weil Enter irgendwie zweimal geschickt wurde und so ein Tab gleich wieder geschlossen wird, nachdem er geöffnet wurde
  * Mnemonics für die restlichen Buttons bzw. Alternativen mittels Cmd oder Ctrl für Mac OS X Benutzer