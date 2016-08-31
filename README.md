# KiTS

Das **Ki**nderserien Rate- und **T**rink-**S**piel ist eine App (Android) für mehrere Spieler, bei der die Spieler Kinderserien anhand von kurzen Ausschnitten aus Serien-Intros (Audio) erraten müssen.
Zusätzlich gibt es zu jeder Series leichte und anspruchsvollere Fragen, die beantwortet werden können.

Das Spiel umfasst über 120 (Kinder-)Serien der 80er/90er/2000er.
Welche Serien das sind, ist in der [Serienliste](res/raw/series.csv) einzusehen.

Aus rechtlichen Gründen können mediale Inhalte der Serien (Intro, Bilder, etc.) nicht mitgeliefert werden.

## Ablauf

Der Ablauf ist wie folgt:
1. Ausschnitt des Serien-Intros anhören (bis zu 3x, Audio)
2. erkannte Serie den Mitspieler mitteilen
3. Auflösen
4. Serie mit Rateversuch des Spieler abgleichen, wer fehlerhaft rät muss trinken (oder was immer ihr festlegt)
5. (optional) Serien-Intro in vollem Umfang ansehen (Video)

## KiTS-Server

Anstatt die Intro-Ausschnitte und vollen Intros auf dem Smartphone anzusehen und -zuhören, kann ein Server zur Wiedergabe genutzt werden.
Der KiTS-Server ist ein eigenes Projekt, das auf einem beliebigen Rechner im Heimnetz ausgeführt wird.

Die KiTS-App sucht automatisch nach im Heimnetz laufenden Servern und verbindet sich mit dem Erstbesten.
Über die Einstellungen kann die Wiedergabe auf Smartphone/Server aktiviert und deaktiviert werden.

So kann die Wiedergabe etwa über PC-Monitor/TV/Beamer und Lautsprecher erfolgen.

Wird ausschließlich der KiTS-Server verwendet, müssen weder Audio- noch Videodateien auf dem Smartphone gespeichert sein - das spart Platz.
