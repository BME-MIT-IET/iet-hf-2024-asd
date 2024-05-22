# Test Charter
Title: PreGameCharter

Objective: Bugok találása a játék előtti képernyőn

Scope: Játék előtti képernyő bugok keresése

Tester's Name: Ignáth Dávid

Date: 2024.05.20

# Session Details
Session Duration: 10:15

Start Time: 10:45

End Time: 30 perc
# Exploration notes
**Scenario 1: Nem adok meg egy játékosnak sem nevet**

- Steps Taken: Hozzáadtam egy szerelőt és szabotőrt, a megfelelő gombok megnyomásával. Majd elindítottam a játékot a „Játék indítása” gombra kattintva.
- Observations: A játék elindult és a játékosoknak elnevezte az alábbi módon: A szerelők neve „mcX”, ahol X egy szám, ami 1-től kezdve felfele számol, mc1 az első szerelő játékosnak a neve, mc2 a másodiké stb.. A szabotőröknél hasonló a logika, csak „sc”-vel kezdődnek a nevek.

**Scenario 2: Mennyi játékost lehet hozzáadni a játékhoz**

- Steps Taken: Addig adok hozzá új játékost, amíg váratlan esemény nem történik.
- Observations: 10 új játékos hozzáadásával eltűnik a legutolsó szöveges beviteli mező. A eltűnt mezőt nem tuom elérni sehogyan sem. A játékosok listája nem görgethető.
- Issues Found: Elég játékos hozzáadásával nem tudjuk elnevezni minden egyes játékos. A csapattal megbeszélve a problémától eltekinthetünk, mivel a játékot 4-6 személyre tervezték, viszont, ha jövőben esetleg másik csapát tovább szeretné fejleszteni a játékos, erre a problémára oda kell figyelni.

Bugs and Issues:

Egy issue-t találtam scenario 2-ben.

Severity: nagyon alacsony

Attachments: 

![](Aspose.Words.14e3abff-f6dc-4abe-8901-44d842b839e2.001.png)

A 2. szabotőr játékosnak nem lehet nevet adni

# Debrief
Discussion Points: A csapattal konzultálás után megegyeztünk, hogy mivel a játékot 4-6 főre tervezték, ezért nem kell kijavítani a AddingNewPlayers01 bugot

Follow-Up Actions: A bugot dokumentálni, ha esetleg a jövőben ki szeretnénk terjeszteni a játékot 10+ főre.
