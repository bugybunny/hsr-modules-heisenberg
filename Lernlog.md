# Allgemeine Informationen #
## Bedienbarkeit ##
Unsere Bibliotheksapplikation zeichnet sich durch einfache und schnelle Bedienbarkeit aus und nicht durch viele (unnötige) Funktionen. Die komplette Applikation ist mit der Tastatur zu bedienen (siehe https://docs.google.com/document/d/1kV6n3jRrtOqBjhsnB6XKUmNPrTUvClzKK9IavTIQb6o). Poweruser – wozu hoffentlich auch Olga Ordentlich nach einigen Wochen Benutzung gehört, wenn sie merkt, dass sie Tasks über Shortcuts viel schneller arbeiten kann als über die Mausbedienung – können Tasks in wenigen Sekunden lösen.
Da unter Mac OS X keine Mnemonics existieren, gibt es die meisten Accelerators auch über Ctrl verfügbar. Die Aktionen wurden so implementiert, dass die meisten Benutzer, die oft einen Webbrowser benutzen, damit zurechtkommen (Tabswitching mit Ctrl+Tab, Schliessen mit Ctrl+W/Ctrl+F4, alle Tabs schliessen mit Ctrl+Shift+W, Suchfeld clearen mit Escape etc.).
Die Anzahl aufpoppender Dialoge (JOptionPane) wurde minimal gehalten, um den Arbeitsfluss nicht zu unterbrechen. Fehlermeldungen, oder wieso Aktionen nicht ausführbar sind, werden dem Benutzer über Tooltipps angezeigt. Fast alle Komponenten haben einen Tooltipp, die auch immer geupdated werden und unterschiedliche Tooltipps wenn sie aktiviert oder deaktiviert sind. Der Benutzer kennt die Applikation nach einiger Zeit und weiss, wieso Aktionen nicht möglich sind und will sie deshalb nicht immer lesen. Hat er dennoch das Bedürfnis, kann er das trotzdem tun, es stört aber den Arbeitsfluss so nicht. Es erscheinen ausserdem keine Bestätigungsdialoge um auch wieder den Arbeitsfluss nicht zu unterbrechen. Im GUI gibt es immer mindestens etwas das sich ändert, wenn Daten gespeichert/geändert wurden, wodurch der Benutzer merkt, dass wirklich etwas geändert hat.

## Code ##
Wir haben alle Komponenten selbst implementiert und Klassenhierarchien gebildet und stark auf Generics gesetzt um duplizierten Code zu reduzieren und bei gleichen Komponenten das gleiche Verhalten sicherzustellen. Die Applikation wäre somit leicht erweiterbar.
Wir haben ein komplettes Observer/Observable Modell implementiert wo wir uns von Swing selbst haben inspirieren lassen. Es gibt eine Unterscheidung von notify Events. Also das Ändern des Buchtitels führt zu einem anderen notify Event als wenn ein neues Buch hinzugefügt wurde. So behandeln alle Observer nur die Events die sie interessieren und machen keine unnötigen Updates. Ausserdem werden alle Observer, die irgendwie disposed werden können, auch wieder deleted.
Den Controller haben wir nach Absprachen mit Herrn Stolze weggelassen, da die View so eng mit dem Model gekoppelt ist.

Die Checkstyle Eingaben wurden bis auf wenige (leere catch Blöcke) Ausnahmen alle beseitigt und Parameter und Klassen folgen einem strikten Muster. Ausserdem wurden sehr strenge Compilereinstellungen verwendet (beispielsweise kein Autoboxing weil das zu NullPointern führen kann, Parameter dürfen nicht neu zugewiesen werden etc.).

## Sonstiges ##

Die Aufgaben wurden so aufgeteilt, dass Theo Winter die meisten GUIs gemacht hat und sich externe Designsachen überlegt hat und Marco Syfrig viele Zusatzfeatures und eigene GUI-Komponenten implementiert hat. Dies weil Marco Swing schon relativ gut kennt und Theo noch nie damit gearbeitet hat und es so für die Prüfung lernt ;-)


# SW4 #

Erledigt:
  * Bookmaster-Frame & Bookdetail
  * Observer Pattern
  * Abstrakte Oberklasse für alle Dialoge in der Applikation mit Standardverhalten für alle Dialoge (Keyhandling wie Escape, Enter, Anordnung der Dialoge)
  * Öffnen mehrerer Detailansichten und Prüfung, ob eine Buchdetailansicht bereits offen ist und falls ja, wird sie in den Vordergrund gebracht

Offen:
  * ~~Braucht das Buch keine ISBN? Herr Stolze fragen~~ **Antwort**: Braucht es nicht, ohne sogar einfacher
  * ~~Wann soll das Update des Buchobjekts geupdatet werden? Bei der Eingabe oder erst wenn Enter/Ok gedrückt wird?~~ **Antwort**: Erst wenn Enter/Speichern gedrückt wird

Probleme:
  * Bei Konflikten mit Git können wir noch nicht mergen
  * Projectpreferences (wie Compilersettings und Formatting, Templates etc.) über Git synchronisieren ist keine gute Idee und verursacht nur Probleme. Wir haben diese Settings deshalb anders ausgetauscht und importiert
  * Update der JList wenn sich ein Buch ändert. Der Eintrag wird visuell erst geupdatet wenn der Eintrag selektiert wird
  * Mindestgrösse der JList welche die Bücher anzeigt und des Fensters allgemein funktioniert nicht (kann beliebig klein gemacht werden und die JList zeigt nicht automatisch den ganzen Text an, sondern wird abgeschnitten)

# SW5 #

Erledigt:
  * Die JList entfernt und anstelle dieser eine JTable eingebaut. Die JTable wird vom BookTableModel abgefüllt, welches die Bücher aus dem BookDO nimmt.
  * Verwendung von KeyBindings anstatt über alle Komponenten eines Containers zu iterieren und den KeyListener zu adden
  * Validierung der Felder vor dem Speichern

Offen:
  * ~~Die Elemente in der JTable wieder bearbeitbar machen, atm funktioniert der Edit-Button nicht mehr.~~
  * Validierung ist noch nicht implementiert
  * Herr Stolze fragen: Müssen die geänderten Daten wieder persistent gespeichert werden oder ist das nicht im Scope dieses Miniprojekt?

Probleme:
  * ~~Immer noch Probleme mit Git, dass wir nicht genau wissen wie wir mergen können und was der Rebase genau macht. Funktioniert nun aber durch die Hilfe anderer Personen und so langsam verstehen wir Git)~~ <sub>das Egit Plugin ist deswegen aber immer noch mist <.<</sub>
  * ~~durch die neue JTabbedPane entstehen neue Probleme. Beispielsweise will ich eine Klasse haben die von JPanel ableitet und einen einzelnen Tab repräsentiert und von dort will ich auch gleich den Tab schliessen können (um den Tab zu schliessen, wenn Enter gedrückt und gespeichert wurde). Die Logik dazu müsste ich allerdings in der Klasse machen, die die JTabbedPane enthält.~~ über `SwingUtilities.getAncestorOfClass(JTabbedPane.class, panel.this).remove(panel.this);` kann das auch direkt vom JPanel aus gemacht werden, super :)
  * einige Architekturprobleme: View-Klasse enthält andere Viewklassen und eingebetteten Viewklassen müssen die Klasse kennen, in der sie enthalten sind (siehe auch Problem oben). Lösung: Hauptklasse ist verantwortlich für gesamte Logik aller eingebetteten Klassen. Auch bei der JTable.

Entscheidungen:
  * Die Buchdetails können entweder direkt in der Tabelle editiert werden oder über den bereits bestehenden Dialog um Usability zu bieten aber trotzdem noch alle Szenarios umsetzen zu können (wäre nicht mehr gewährleistet wenn wir nur noch die Bearbeitung über die Tabelle hätten)
  * Die Detailansicht der Bücher wird nicht mehr über einzelne Dialoge, also dass pro Buch ein Dialog geöffnet wird, sondern über eine TabbedPane gelöst. Pro anzuzeigendem/zu editierenden Buch wird ein Tab geöffnet

# SW6 #

Erledigt:
  * Buchtabellenfilterung
  * ch\_DE locale hinzugefügt
  * Sogenanntes Ghost Hint Textfield, zeigt einen ausgegrauten Text an wenn das Textfeld keinen Text enthält und nicht den Fokus hat. Dieser Text (beispielsweise „Search“ bei der Buchtabellenfilterung) verschwindet sobald dieses Textfeld den Fokus erhält und bleibt erhalten, wenn der Fokus wieder verloren geht und der User Text eingegeben hat. So wie man dies halt aus anderen gängigen Implementierungen kennt. Dieses Ghost Hint Textfield soll in Zukunft alle anderen Textfelder ersetzen, damit keine Labels mehr angezeigt werden müssen und so Platz gespart wird.
  * Exemplar-Tabelle beim Detail-Frame hinzugefügt. Neu wird die Library übergeben anstelle der BookDO Liste, so dass wir die Loans etc. im Detail-Frame abrufen können.

Offen:
  * Alle Listener removen bevor eine Component geschlossen wird, ansonsten räumt der GC diese Objekte nicht auf, obwohl sie null sind
  * X-Buttons in den JTabs hinzufügen zum einfachen schliessen der Tabs.

Probleme:
  * Um die Tabelle zu filtern, muss der Rowsorter der Tabelle nochmals gesetzt werden, obwohl ich ihn vorher schon gesetzt habe. Das ist wohl kaum beabsichtigt noch ist es in einem Beispiel zu finden, also wohl ein Swing Bug.

Entscheidungen:
  * Mit Herr Stolze besprochen dass wir keinen Controller brauchen weil Modell und View so eng gekoppelt sind d.h. wenn man etwas am Modell ändert muss man sowieso die View neu machen.

# SW7 #

Erledigt:
  * Add Book Button verlinkt mit dem BookDetail Dialog, so dass ein neues, leeres Buch generiert wird.

Offen:

Probleme:
  * Ghost Hint Textfield kann doch nicht für alle Textfelder verwendet werden. Ich habe zu wenig überlegt, d’oh. Gedacht war, dass wie bei den Suchfeldern ein ausgegrauter Text angezeigt bei Textfeldern, der das Label des Textfeldes ersetzt und anzeigt, welchen Inhalt dieses Feld hat. Das würde allerdings maximal noch bei der Eingabe von neuen Daten funktionieren, aber auf keinen Fall wenn bereits bestehende Daten angezeigt werden müssen, weil der ausgegraute Text ja dann nirgendwo mehr angezeigt werden kann. Also musste diese Idee wieder verworfen werden und es werden immer noch neben allen bisherigen Textfeldern Labels angezeigt.

Entscheidungen:
  * Um ein neues Buch hinzuzufügen wird einfach im Detaildialog ein neuer Tab erzeugt, wo die Daten eingegeben werden können
  * Ein Buchtitel kann auch ohne Exemplar/Kopie existieren. Ansonsten müsste beim Erfassen eines neuen Buchtitels direkt automatisch ein Exemplar generiert/eingetragen werden. Das Verhalten wäre zwar logisch, weil man trägt ja ein Titel meistens ein, wenn ein neues Buch in die Bibliothek dazukommt. Allerdings wird so die Logik vereinfacht und wenn alle Kopien eines Buchs gelöscht werden, dann soll der Titel trotzdem noch existieren können.

# SW8 #

Erledigt:
  * Allen Büchern werden nun automatisch zufällige Gestelle hinzugefügt. Im BookDetailJPanel zeigen wir nun korrekt das Gestell anstelle des Buch-Zustands. Das Gestell eines Buchs kann geändert und gespeichert werden.
  * Bücher die geöffnet werden können nun verändert und gespeichert werden. Der Speichern-Button ist nur dann aktiv wenn auch wirklich Änderungen vorliegen. Dazu wird die gleiche Funktion verwendet die auch den Stern bei Änderungen anzeigt.
  * Viel Codeanpassungen und Erstellung neuer Klassen und Methoden um redundanten Code zu eliminieren
  * Loan Panel komplett erstellt, also die Ansicht. Loans hinzufügen, ändern, löschen wird über einen separaten Dialog erledigt wie bei den Büchern auch.
  * verschiedene Usability Sachen
  1. Escape um Suchfilter zu reseten
  1. Ctrl+F um im Mainfenster den Fokus auf das Suchtextfeld zu setzen
  1. Ctrl+(Shift)+Tab um zwischen den Tabs zu wechseln
  1. Anzeige im Suchfeld für die Tabellen mittels Farben ob Items gefunden wurden oder nicht

Offen:
  * Selektierte Bücher/Ausleihen anzeigen, neues Buch/Ausleihe hinzufügen Buttons nach rechts ausrichten? Bisher sind sie links am Suchfeld für die Tabelle angebunden. Rechts würde besser ins GUI passen, wäre logischer und Benutzerfreundlicher (Buttons sind ganz rechts im Fenster einfacher zu treffen als irgendwo mittendrin) und bei den meisten Applikationen ist dies auch der Fall. ⇒ imt Theo besprechen
  * Machen wir überhaupt Menüs? Styleguides schreiben zwar vor (oder empfehlen es zumindest streng), dass es für jede Aktion irgendwo im GUI auch einen entsprechenden Menüeintrag gibt. Aber
  1. Es gibt nicht viele Aktionen
  1. Alle Aktionen sind bereits sprechenden GUI-Elementen zugeordnet
  1. Keine Aktion ist nur über ein Shortcut erreichbar, es existiert immer mindestens ein GUI-Element dazu
  1. Menüs würden das ganze Look and Feel der Applikation zerstören und passen nicht ins Konzept. Bisher ist die Oberfläche sehr minimal gehalten (wünschbar wäre natürlich etwas wie es in Browsern zu finden ist, wo das Menü normalerweise versteckt ist und angezeigt werden kann, das ist aber zu aufwendig zum implementieren)
  * die Vorteile hingegen sind gering ⇒ noch im Team besprechen

Probleme:
  * Zuerst war bei allen Bücher das gleiche Gestell eingetragen und es sah nach einem Fehler aus. Um sicherzustellen, dass unsere Gestell-Anzeige wirklich funktioniert, haben wir jetzt eine Methode, die bei der Initialisierung der Bibliothek, den Büchern zufällige Gestelle zuweist.
  * Um mittels Ctrl+Tab zum nächsten bzw. Ctrl+Shift+Tab zum vorherigen Tab zu wechseln, muss das Defaultverhalten entfernt werden. Das Defaultverhalten ist (je nach Look and Feel, beim Standard Windows Metal Look and Feel wäre dies kein Problem und Ctrl+Tab würde sich sogar so verhalten wie ich es implementieren will, allerdings wäre dass dann unter Linux und Mac OS X nicht mehr der Fall und wir benutzen sowieso Nimbus), mittels Ctrl+Tab wird der Fokus auf die nächste Komponente gelegt, je nach Focustravelpolicy halt. Dies ist toll um beispielsweise aus einer JTextArea mittels Tastatur rauszukommen, allerdings haben wir nirgendswo eine JTextArea. Für diesen Keystroke ist also bereits eine Action definiert, die ich zuerst suchen und rauslöschen muss, bevor ich meine eigenen Actions hinzufügen kann, um dann mit Ctrl+Tab die Tabs zu wechseln. Das Problem bei Swing ist sowieso, dass das ganze Actionhandling sehr unschön ist. Definiere ich beispielsweise den Buchstaben 'g' um den Tab zu wechseln und setze den Fokus auf ein Textfeld, so erscheint teilweise das 'g' im Textfeld und teilweise wird der Tab gewechselt. WTF?! Es scheint so zu sein, dass der Actionhandler, welches den Keystroke zuerst erhält seine Aktion ausführt und der Rest wird dann ignoriert.

Entscheidungen:
  * keine

Erkenntnisse:
  * C++ ist doch nicht so schlecht wie ich immer während den Vorlesungen und Übungen in Prog3 denke. Einige Sachen wie Lambdas (Java 8 ist leider noch weit entfernt) würden die Programmierung stark vereinfachen, immer Interfaces zu definieren und diese dann zu implementieren kostet erstens sehr viel mehr Zeit als in C++ und zweitens wird viel mehr Code generiert
  * Auch die wenigen Ideen die C++ von der funktionalen Programmierung hat würden das Projekt sehr viel einfacher machen
  * Auch das Definieren von const Sachen ist in C++ viel mächtiger und besser umgesetzt als in Java mit dem kaum brauchbaren final

# SW9 #

Erledigt:
  * Komplettes Update/Event System für das Observer Pattern, damit nicht die Sourcen, welche das Update ausgelöst haben unterschieden werden müssen und so können viel mehr Events unterschieden werden
  * Korrektes Updaten der Anzahl Bücher, Kopien, Ausleihen etc. über das Observer-Pattern (bisher gar nicht aktualisiert worden) mit unterscheidbaren Updateevents
  * Anzeige in der Ausleihetabelle, wie lange der Kunde das Buch noch ausgeliehen haben kann oder wie lange es schon über der Ausleihefrist in der Tabelle. Mit korrektem Singular/Plural und je nachdem ob er noch Zeit hat oder eben schon darüber hinaus ist unter Verwendung von ChoiceFormat. Hat zwar viel Zeit gekostet um das zu verstehen aber das ist ja hammergeil, was das JDK hier bietet :)
  * Alle ActionsListener durch Actions ersetzt. Dies würde es in Zukunft ermöglichen, die Aktionen auch per Menü anzubieten ohne Mehraufwand, da nun einfach die Aktionen disabled werden können anstelle der Buttons. Die Actions sind nun auch korrekt disabled oder enabled je nachdem ob sie verfügbar sind oder nicht
  * Viele Updates in der Library und Büchern und Kopien selber lösen nun die korrekten GUI-Updates aus (bisher gar nicht oder falsch)
  * Ich habe eine Klasse erstellt mit der einfach die Tooltips gesetzt werden können, wenn der Button disabled oder enabled ist und alle bestehenden Buttons ersetzt. Dadurch sind garantiert überall die Tooltips gesetzt und ich muss mich nicht selber darum kümmern, diesen zu ändern, wenn setEnabled() aufgerufen wird
  * Mnemonics für die meisten Buttons und also Ctrl+N beispielsweise um ein neues Buch hinzuzufügen, weil die Mnemonics unter Mac OS X nicht verfügbar sind
  * Die Anzahl an verfügbaren Kopien werden nun endlich angezeigt (bisher random generiert)

Offen:
  * Wir haben eine Klasse für das Hauptfenster welche nur eine JTabbedPane enthält. Die beiden Tabs sind dann als eigene JPanels implementiert und haben ihre eigenen Input- und ActionMaps. Das heisst, alle Tastatureingaben, wenn der Fokus auf der JTabbedPane selbst liegt und nicht in einem Panel selbst, müsste im Hauptfenster selbst abgefangen werden. Nun haben wir Ctrl+N um einen neuen Buchtitel hinzuzufügen (und in Zukunft wohl auch um eine neue Ausleihe zu erfassen) und diese lösen momentan nichts aus. Bzw. der Benutzer muss zuerst den Fokus vom JTabbedPane in den JPanel verändern, dies passiert wahrscheinlich nur direkt nach dem Applikationsstart, ist aber trotzdem nervig und mich nervt es selbst immer wenn ich teste. Nun gäbe es folgende Möglichkeiten
  1. Die Panels implementieren ein Interface oder leiten von einer Klasse ab, die garantiert, dass eine Methode bereitgestellt wird, welche alle Actions und ihre auslösenden Keystrokes zurückliefert und das Hauptfenster fügt diese dann hinzu und je nach selektiertem Tab werden die Aktionen dann im entsprechenden Panel aufgerufen
  1. Die Hauptklasse delegiert alle nicht abgefangenen Keystrokes an die Panels (meine bevorzugte Lösung, aber keine Ahnung wie das funktioniert. **Herr Stolze fragen**)
  1. nach dem Systemstart wird automatisch der Fokus auf eine Komponente in dem ersten Panel gesetzt oder ein Tab Keystroke simuliert (sehr unschön, da unerwartetes Verhalten für den Benutzer)
  1. immer wenn die JTabbedPane den Fokus erhält wird eine Komponente in dem aktuell selektierten Panel fokusiert (sehr ähnlich wie Punkt drei und genau gleich unschön)

Probleme:
  * In der Ausleiheetabelle wurden bisher Rückgabedaten 00.00.00 angezeigt, da ich Loan.getReturnDate() verwendet habe (und die Namenswahl der Vorlage nicht gerade aussagekräftig war), welches das wirkliche Rückgabedatum des Exemplars angibt und nicht, wann es spätestens zurückgebracht werden muss. Habe ich behoben, indem ich eine neue Membervariable und Methode in der Loan Klasse gemacht habe, die genau dieses Datum berechnet und zurückgibt, damit der Caller das nicht berechnen muss (Responsibility liegt bei der Loan Klasse)
  * Checkstyle Warnungen die wir nicht wegkriegen: Verweist man in Javadocs auf Klassen dann müssen diese importiert werden. Werden diese Imports sonst nirgendwo verwendet, zeigt Checkstyle eine Warning für einen ungebrauchten Import an. Dies wurde als Bug bei Checkstyle gemeldet und es existiert eine Option um dieses Verhalten auszuschalten. Allerdings ist diese Einstellung defaultmässig deaktiviert, das heisst bei Codekontrollen auf anderen Rechnern wird die Warning immer noch angezeigt. So können leider nicht alle Checkstyle Warnings nach Vorgabe vermieden/entfernt werden.

Entscheidungen:
  * Das Menü wird weggelassen wegen den Punkten die letzte Woche aufgezählt wurden
  * Die anzeige/editieren/hinzufügen Buttons werden doch nicht nach rechts verschoben nach Absprache im Team. Der Mausweg würde dadurch unnötig verlängert werden, wenn die Benutzerin etwas sucht und danach einen dieser Buttons betätigen will und nur die Maus als Eingabegerät verwendet und nicht mit Shortcuts arbeitet
  * Wird per Suche genau ein Eintrag in der Tabelle gefunden, so wird er automatisch selektiert. Damit wird folgendes Szenario einfacher: Ein Buch, dessen eindeutiger Titel bekannt ist, suchen und anzeigen. Dadurch kann man nach solange die Zeichen des Buchtitels in das Suchfeld eingeben und sobald nur noch ein Eintrag gefunden wird, kann einfach Alt+V gedrückt werden, um die Buchdetails und Ausleihen dazu anzuzeigen.

# SW10 #

Erledigt:
  * Alle Observer, die überhaupt obsolet werden können (also nicht solche von Fenstern die sowieso nie disposed werden können und beim Programm beenden sowieso aufgeräumt werden) werden nun entfernt, bevor die Komponente disposed wird.
  * Validierung implementiert: Alle Felder (Titel, Autor und Publisher) dürfen nicht leer sein und ein Buch darf nicht doppelt vorkommen in der Bibliothek.
  * Die Validierung zeigt falsche Eingaben rot und gute Eingaben grün an.
  * Bei speziellen Fällen z. B. dupliziertes Buch festgestellt wird auch noch eine passenden Fehlermeldung neben dem Save Button dargestellt.
  * Alle Fenster und Dialoge haben nun eine minimale Grösse
  * Layoutanpassungen, damit der Platz optimal genutzt wird und die „sinnvollsten“ Komponenten in der Grösse angepasst werden. Heisst, dass beispielsweise beim Vergrössern der Dialoge die Tabelle mehr Platz erhält und nicht einfach ein leerer Platz, ohne Komponenten, grösser gemacht wird
  * Die meisten GUI-Updates werden nun über SwingUtilities.invokeLater() gemacht
  * Der offene Punkt von letzter Woche, dass die Keystrokes auch erkannt werden, wenn der Fokus auf der TabbedPane liegt, gelöst. Die Panels leiteten schon bisher von einer abstrakten Klasse ab, diese wurde um ein Interface erweitert um beispielsweise etwas neues zu erstellen (neues Buch, neue Ausleihe), das Suchfeld zu fokussieren, die Tabelle zu fokussieren, die selektierten Einträge anzuzeigen etc. Nun kann das Hauptfenster alle Keystrokes abfangen und dann die durch das Interface bereitgestellte Methoden aufrufen.
  * Viele Shortcuts wie schon oben erwähnt. Auf dem Hauptfenster existiert nun für jedes GUI-Element ein Shortcut um sie entweder zu selektieren oder beispielsweise bei den Checkboxen an- oder abzuwählen
  * Viele andere Usability Sachen, unter anderem:
  1. beim Fokusieren aller Textfelder wird nun der gesamte Text selektiert
  1. wird der Text in einem Textfeld vom Programm mit setText überschrieben, wird nun die Caretposition gespeichert und nach dem setText wieder dorthin gesetzt
  * Neue Loans können jetzt hinzugefügt werden.
  * Loans können entfernt werden, mehrere oder nur einer aufs Mal.


Offen:
  * 

Probleme:
  * Wir haben ein eigenes ComboboxModel geschrieben um alle Customer anzuzeigen. Da der angezeigte Eintrag nicht beeinflusst werden kann und automatisch die toString Methode auf der angezeigten Instanz (oder wenn man das anders will, treten anderen Probleme auf). Also habe ich eine eigene, neue Klasse gemacht. Diese erweitert einfach den Customer um ein Feld, welches die aktuelle Ausleihenzahl angibt und welche die toString Methode dann entsprechend überschreibt. Nun haben wir ein Dropdownmenü, welches schön den Customer mit den aktiven Ausleihen anzeigt, dies führte allerdings zu dutzend anderen Problemen, welche gelöst werden mussten. Diese neue Klasse DisplayableCustomer und Customer müssen mit equals verglichen werden können, weil sonst die Suche in Collections nicht mehr funktioniert und einfaches Casten reicht nicht aus, die Klassen müssen die ganze Zeit hin- und herkonvertiert werden und die Liste muss immer aktualisiert werden, wenn eine Ausleihe hinzugefügt oder zurückgebracht wird etc. Sehr mühsam. Und das nur, um im Dropdownmenü die Anzahl der aktuellen Ausleihen anzuzeigen für jeden Customer.
  * Wird ein Tablemodel auf editierbar gesetzt (egal ob eine oder mehrere Spalten) und wählt dann eine Zeile aus und drückt irgendeinen Hotkey (beispielsweise Alt+V bei uns um die Detailansicht zu öffnen) so wird automatisch der Bearbeitungsmodus für das zuletzt selektierte Feld gestartet was überhaupt nicht gewünscht ist. Allerdings ist das ein Swing Feature und kein Bug. Jedenfalls wird der Bearbeitungsmodus in drei Fällen gestartet
  1. Es wird F2 gedrückt
  1. Es wird ein Doppelklick auf ein Feld gemacht
  1. **oder es wird irgendeine Taste gedrückt**
  * und genau der letzte Fall ist das Problem hier. Swing schickt den Keystroke nach dem Actionhandling anscheinend noch an alle Komponenten weiter. Jedenfalls kann dieses Verhalten abgeschaltet werden, jedoch nicht mit einer einfachen Methode in JTable sondern über `jtableinstance.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);` :o was für ein Mist und unschöner Code. Über die Klasse BasicTableUI könnte noch ganz viel unschönes Zeug gemacht werden.

Erkenntnisse:
  * Leider nicht in der Vorlesung gelernt oder gehört was mich wundert, da es so ein grundlegendes und wichtig zu verstehendes Konzept der GUI-Programmierung ist, sondern von einem Kollegen gehört, der das Modul letztes Jahr besucht hat: Alle GUI-Updates sollten anscheinend über SwingUtilities#invokeLater gemacht werden um es Threadsafe (bzw. korrekt zu queuen) zu machen, da sonst schnell mal die GUI-Updates in der falschen Reihenfolge ausgeführt werden. Dies löst bei uns auch gleich mehrere Bug.
  * Die Swing Bibliothek wird mir immer unsympathischer mit undurchdachter Architektur und Programmierung (legacy Code der vor Java 1.5 noch ohne Generics geschrieben wurde und deshabl immer noch existiert, ganze zwei Javaversionen später und die Methode ist noch nichteinmal als deprecated markiert -.- ComboBoxModel#getSelectedItem und ComboBoxModel#setSelectedItem liefern bzw. erwarten noch ein Object obwohl sie generisch sind) und das ComboBoxModel selbst zu programmieren macht es schwer wenn man einen anderen Text anzeigen will und nicht nur einfach die toString Methode des angezeigten generischen Typs aufrufen will. Erstellt man dann kurz eine eigene Klasse, welche die toString Methode so wie gewünscht implementiert, hat man wieder andere Probleme (setSelectedItem etc. funktionieren dann nicht mehr richtig weil die equals Methoden angepasst werden müssen und und und)
  * Nächstes mal verwende ich wieder SWT auch wenn die GUI-Elemente dort nicht leightweight sind


Entscheidungen:
  * Ein Buch ist eindeutig anhand seines Buchtitels und Autors erkennbar und so werden auch Duplikate erkannt (wird bei der Validierung verwendet)

# SW11 #

Erledigt:
  * Bei den Tabs (Books/Loans Detail Panels) haben wir jetzt einen X-Button so dass man die Tabs auch grafisch schliessen kann. Der Button hat einen Rollover-Effekt.
  * Wir haben eine Dokumentation zu den Shortcuts sowie allen Fenstern der Applikation auf Google Docs erstellt. https://docs.google.com/document/d/1kV6n3jRrtOqBjhsnB6XKUmNPrTUvClzKK9IavTIQb6o/edit?usp=sharing
  * Wir haben ein Sequenz-Diagramm des Observer-Pattern für den Review am Donnerstag erstellt.
  * Viele weitere Shortcuts und Mnemonics, sodass das komplette Programm noch besser und schneller ohne Maus und nur mit Tastatur zu bedienen ist (siehe Dokumentation oben)
  * Mehr als 50 kleine und grössere Bugfixes von Observernupdates die bisher fehlerhaft oder gar nicht implementiert waren. Alle Datenänderungen sind jetzt sofort überall sichtbar, ohne dass zuerst noch in die Tabelle geklickt werden muss oder Ähnliches
  * Kleine Sachen wie: die Farbe des Suchfelds wird nun geupdated wenn nach einem Buch gesucht wurde und die Werte des Buchs so verändert werden, dass keines mehr gefunden wird und umgekehrt.
  * Implementierung einiger Cellrenderer. Beispielsweise für den Loanstatus oder die Anzahl der verfügbaren Kopien eines Buches um es erkennbarer zu machen. Ausserdem in der „lent until“ Spalte in der Loantable. Dort wird angezeigt, wie viele Tage der Kunde noch hat um das Exemplar zurückzugeben oder schon überzogen hat. Bisher wurde das im TableModel gemacht und führt natürlich (je nach Datumsformat) zu einer falschen Sortierung. Neu wird nur ein java.util.Date zurückgegeben welches automatisch sortiert werden kann und über einen Cellrenderer wird dann die Anzahl der verbleibenden Tage gesetzt.
  * Beim Schliessen von Tabs wird überprüft ob der Inhalt dirty (=ungespeicherte Änederungen besitzt) ist. Ist dies der Fall, wird der Benutzer gefragt, ob er die Änderungen speichern, sie verwerfen oder abbrechen will. Die Defaultaktion ist abbrechen, damit er nicht ausversehen die Änderungen speichert oder verwirft, wenn er zu schnell drückt. Sondern er wird in den Dialog zurückgeworfen und wundert sich dann (wenn er die Meldung nicht gelesen hat), wieso er wieder im eben vermeintlich geschlossenen Dialog ist. Er versucht es nochmals und liest dieses Mal die Meldung. Werden alle Tabs aufs Mal geschlossen (Fenster schliessen oder Ctrl+Shift+W) werden alle undirty Tabs geschlossen und wird immer ein Tab selektiert und der Benutzer gefragt, was er tun will für den aktuell selektierten Tab. Um keine „falschen“ Daten in die Bibliothek speichern zu können, wird auch geprüft ob der Tab nur valide Daten enthält. Falls nicht, wird ihm die Speichernoption nicht angegeben, sondern er kann nur verwerfen oder abbrechen.

Offen:
  * **Herr Stolze fragen: Wann Ellipse anzeigen bei Buttons? Sollten wir bei Add new Loan/Book und view selected eine anzeigen? Ist das nur bei MenuItems?**
  * Die Tabellensortierung wird nicht ausgeführt, wenn neue Rows hinzugefügt werden. Werden bestehende angepasst, funktioniert das ohne Probleme. Eigentlich sollte es reichen um JTable#setSortsOnUpdate(true) aufzurufen (was wir mehrmals machen, also nach jedem Filtern damit es bestimmt nicht von einer anderen Komponente/Methode resetted wird). Ich habe dazu zwei noch offene (Open)JDK Bugs gefunden, die mit dem TableRowSorter zu tun haben und das Fehlverhalten vielleicht auslösen könnten: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6539455 und http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6592461.

Probleme:
  * Der X-Button in den Tab verursacht dass der "dirty-Stern" nicht mehr angezeigt wird. Beim Setzen einer Tabcomponent über JTabbedPane#setTabComponentAt wird die Komponente (meistens ein JLabel) gesetzt, welches als Registerkarte angezeigt wird, also bei uns der Titel neu mit dem Schliessenbutton. Nun funktionieren allerdings die Methoden JTabbedPanel#setTitleAt und JTabbedPane#setToolTipTextAt nicht mehr, bzw. schon, nur sieht man es kaum. Die JTabbedPane zeigt das alte Titellabel mit dem Text und dem Tooltip immer noch an und updated es auch und grafisch sieht man es, wenn man über die Registerkarte fährt. Unser neues Label ist leicht umrundet mit einem andere JLabel (welches wir vorher hatten und von der JTabbedPane automatisch erstellt wird) welches sich durch leicht andere Farben bemerkbar macht, wenn man mit der Maus rüber fährt. Jedenfalls sieht man innerhalb dieser 2-3 Pixel Umrandung um die Registerkarte einen anderen Tooltip. Um nun den Titel mit dem `*`, um ungespeicherte Änderungen anzuzeigen, dem Tooltip und dem Schliessen Button korrekt anzuzeigen, müssen wir das von uns neu gesetzte JLabel über JTabbedPane#getTabComponentAt (sorgt für grosse Verwirrung zwischen den Methoden JTabbedPane#getComponentAt und JTabbedPane#getTabComponentAt weil die Javadoc überhaupt nicht beschreibend ist, bei den Settern genau das Gleiche) holen und dort den Titel und Tooltip ändern und schliesslich auch noch normal über JTabbedPane#setTitelAt und JTabbedPane#setToolTipTextAt. Diesen Schliessenbutton haben wir nach dem Oracle Tutorial implementiert http://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html. Dort ist kaum herauszulesen, dass die Titel jetzt nachher anders gesetzt werden müssen noch das von uns festgestellte Verhalten, dass das alte JLabel auch noch angezeigt wird. Folglich wohl ein Swing Bug.
  * Es sind noch dutzende "Last-Minute"-Bugs aufgetaucht, die wir noch fixen müssen vor dem Review.

Entscheidungen:
  * Wir haben uns entschieden, bei Buttons die ein Fenster öffnen, keine 3 Punkte (Ellipse) anzuzeigen da dies offebar nur in Menüs üblich ist und sofern nicht klar ist, dass sowieso ein neues Fenster geöffnet wird. **Nach Absprache mit Herr Stolze doch noch implementiert**

Erkenntnisse:
  * Bei den meisten Regex Implementationen bedeuted die vordefinierte Klasse \d [0-9] und ebenso alle Ostarabischen Zahlenliterale. Bei Java sind es wirklich nur [0-9], sehr interessant.
  * Die Reihenfolge der GUI-Updates ist wirklich so wichtig und hat auch diese Woche bestimmt die Hälfte (also mehr als 25) aller Bugs gefixed, die sonst Mühsam mittels if/else Statements und vielen weiteren Massnahmen hätten gefixed werden müssen
  * Die meisten Nimbus Look and Feel Properties müssen logischerweise vor dem Erzeugen des GUIs geändert (also in die Map gepusht) werden. Allerdings muss (zumindest bei uns, bei anderen Teams ist dies nicht der Fall) das Tooltip.background Property **nach** dem Erzeugen mindestens eines Fensters gemacht werden oO. Setze ich das vor dem Erzeugen des ersten Frames, hat es keine Auswirkungen.


<a href='Hidden comment: 
Entweder bini z dumm oder es funzt eifach nöd, aber has nöd gschaft das bild ufem git z hoste und via web druff zuezgriffe..
'></a>
## Sequenz-Diagramm: ##
![http://theowinter.ch/imageUpload/observer_sequenzdiag.png](http://theowinter.ch/imageUpload/observer_sequenzdiag.png)