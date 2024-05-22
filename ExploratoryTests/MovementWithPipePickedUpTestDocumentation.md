# Test Charter
Title: MovementsWithPipePickedUP

Objective: Bugok keresése, ha egy karakter felvesz egy csövet

Scope: Mozgással kapcsolatos bugok

Tester's Name: Romoda Bálint

Date: 2024.05.20

# Session Details
- Session Duration: 30 perc

- Start Time: 19:10

- End Time: 19:40
# Exploration notes
**Scenario 1: Ha felveszi a pumpába érkező csövet, hogyan tud mozogni a karakter?**

- Steps Taken: Felvettem az egyik karakterrel egy csövet, ami mellette volt, majd megpróbáltam a következő körben a felvett csőre rálépni.

- Observations: A karakter nem tud rálépni arra a csőre, amelynek a végét a kezében tartja, ez így helyes.

**Scenario 2: Egy másik karakter rátud-e lépni a csőre, illetve onnan tovább tud-e menni a pumpára?**

- Steps Taken: Megpróbálok egy karakterrel rálépni a csőre, majd ha ez sikeres, a pumpára, amelyen az a karakter áll, amelynek a kezében ott a cső.

- Observations: A karakter rá tudott lépni a csőre, de a pumpára nem. Ezt helyesnek tartom, hiszen a cső még mindig ott van, de a pumpához nincs hozzákapcsolva.

**Scenario 3: Ha odébb mozgok a csővel úgy, hogy egy karakter rajta van, akkor hogyan viselkedik a rajta lévő karakter?**

- Steps Taken: Egy karakterrel rálépek a felvett csőre, majd elmozgok azzal a karakterrel, akinek a kezében a cső van.

- Observations: A karakterrel sikeresen odébb léptem, a cső is és a csövön lévő karakter is jött vele.

**Scenario 4: Most, hogy a cső úgy van a kezében a karakternek, hogy az egy csövön áll, a felvett csövön álló karakter ugyanúgy nem képes a csövet kezében tartó karakterhez lépni.**

- Steps Taken: Megpróbálok rálépni a felvett csövön lévő karakterrel arra csőre, amin az áll, akinek a kezében a cső van.

- Observations: A játék nem engedi, hogy oda lépjek, ezt helyesnek tartom.

**Scenario 5: Most, hogy elmozogtunk a csővel, vissza tud-e lépni a csövön lévő karakter a pumpára, ahonnan jött?**

- Steps Taken: Megpróbálok visszalépni a csövön lévő karakterrel a pumpára.

- Observations: Ez sikeres volt, ismét helyesen.

**Scenario 6: Ha bekötöm a csövet egy pumpába, akkor át tud-e lépni oda a csövön lévő karakter?**

- Steps Taken: A karakterrel, aki a kezében fogja a csövet, átlépek egy pumpára, majd hozzáillesztem a csövet, majd megpróbálok átlépni az eddig mozgó csövön álló karakterrel az új végén lévő pumpára.

- Observations: A cső illesztése, majd a mozgás is sikeres volt, így ezt a chartert sikeresnek mondhatom.
# Debrief
A tesztelés közben ellenőriztem, hogy amikor felvesz egy karakter egy csövet, a csőre, illetve csőről hogy lehet fel-le mozogni. A tesztek során hibát nem találtam, a karakterek megfelelően kezelik mozgásuk szempontjából a cső felvett állapotát.
