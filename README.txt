JenkinsBlame hat das Ziel, Entwickler bei Ihrer Arbeit zu motivieren.

Der Service greift dafür per Web-Interface auf einen Jenkins CI Server zu und wertet die einzelnen Build-Prozesse aus.
Schlägt ein Build fehl, so zeigt JenkinsBlame den Verantwortlichen sehr auffällig an.
Ist der Build wieder lauffähig, so wird der Entwickler angezeigt, der das Problem behoben hat.
Zusätzlich werden alle Mitwirkenden eines Projekts ermittelt und ausgewertet, wer wieviele Builds zerstörte oder fixte.

JenkinsBlame ist ein Service der auf der GoogleAppEngine läuft.
Dabei wird der google-eigene Datenspeicher verwendet, um die erforderlichen Daten zu persistieren.

Zu jeder Zeit besteht die Möglichkeit, die gespeicherten Daten rückstandslos zu löschen.
