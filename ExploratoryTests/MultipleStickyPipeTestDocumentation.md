# Test Charter
Title:StickyPipeCharter

Objective: Bugok találása a több cső sticky tulajdonságának jelenlétekor

Scope: Sticky csővel kapcsolatos bugok keresése

Tester's Name: Romoda Bálint

Date: 2024.05.20

# Session Details
- Session Duration: 45 perc

- Start Time: 15:55

- End Time: 16:40
# Exploration notes
**Scenario 1: Jól kezeli-e vajon a játék, ha egyszerre több cső is sticky?**

- Steps Taken: A játékot alapállapotban indítottam, majd minden karakterrel ráléptem egy csőre. Miután minden karakter egy csövön volt, használtam a sticky parancsot mindre.

- Observations: A csövek láthatóan mind sticky-k lettek, azaz barna lett a színük. A rajta álló karakterek nem ragadnak egyből hozzá, de ez az elvárt működés.

**Scenario 2: Miután visszalép a karakter, hozzáragad-e a csőhöz?**

- Steps Taken: Ki- és visszalépek mind a négy karakterrel arra a csőre, amit azzal a karakterrel sticky-ztem be.

- Observations: Mind a 4 karakter hozzá ragadt a csőhöz, majd a harmadik körben ismét tudtak mozogni. Ez vizuálisan is helyesen jelent meg, a csövek színe is helyes.

**Scenario 3: Minden karakter egy más által beállított csőhöz ragadjon.**

- Steps Taken: Ki- és visszalépek mind a négy karakterrel a csövükre, majd beállítom a sticky-t, majd kilépek mindegyik karakterrel, majd mindegyik ragacsos csőre egy különböző karakterrel lépek.

- Observations: Mind a 4 karakter hozzá ragadt a csőhöz, majd a harmadik körben ismét tudtak mozogni. Ez vizuálisan is helyesen jelent meg, a csövek színe is helyes.

**Scenario 4: Minden karakter egy más által beállított csőhöz ragadjon, de minden karakter különböző körben, egymás utáni körben lépjen a csövekre.**

- Steps Taken: Egy pumpákból és csövekből álló (4 cső hosszú) körben helyezem el a karaktereket pumpákon, majd rálépek mindegyikkel a körön belül az egyik csőre. A csöveket ragacsossá teszem, de 2 csövet csak egy körrel később. A karakterek ezután egymást követő körökben rálépnek a csövekre.

- Observations: Amikor a negyedik karakterrel ráléptem volna a ragacsos csőre, mindhárom (leragadt) karakterről elmúlt a ragadás és az összes csőről eltűnt a ragadás.

- Issues Found: Hiba lehet a csövek ragacsosságának kezelésében, mivel csak az első karakternek (és csőnek) kellett volna megszabadulnia a ragadástól.

# Bugs and Issues:

Bug ID: MultiplePipesSticky01

Description: A lejárati ideje a ragacsoknak hibásan számolódik.

Severity: közepes

Steps to Reproduce: A körkörös ragacsos metódust végigcsinálni, ahogy le van írva a Scenario 4-ben.

Attachments: 

![](Aspose.Words.37535207-e1c6-454e-b63e-480bab7e7278.001.png)

A képen látszik, hogy a negyedik karakter még nem lépett, de már egyik cső sem ragacsos.

# Debrief:

A csövek sticky tulajdonságának beállítása általában megfelelően működik. Azonban, ha egyszerre több cső ragacsosságát kell ellenőrizni úgy, hogy azokra különböző időpontokban lépnek rá a karakterek akkor adódik egy probléma: a csövek ragacsossága néha véget ér akkor, amikor egy karakternek lejár az odaragadása. 
