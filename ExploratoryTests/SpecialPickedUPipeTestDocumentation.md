# Test Charter
Title: ActionsWithPipePickedUP

Objective: Bugok keresése, ha egy karakter felvesz egy csövet

Scope: Input/output-tal és különleges esetekkel kapcsolatos tesztek.

Tester's Name: Romoda Bálint

Date: 2024.05.21
# Session Details
Session Duration: 30 perc

Start Time: 12:00

End Time: 12:30

Scenario 1: Hogyha fel van emelve a cső, be tudom-e állítani az input/outputot rá?

Steps Taken: A felvett cső lent lévő végénél lévő pumpára lépek egy karakterrel és megpróbálom állítani az input/outputot arra a csőre.

Observations: Sem az inputot, sem az outputot nem lehet állítani.

Scenario 2: Az előbbi teszt alapján arra következtetek, hogy mivel nem lehet kézben lévő csőre inputot/outputot állítani, ezért felvételkor elveszti egy ilyen cső a tulajdonságát.

Steps Taken: Felveszek egy-egy csövet, az egyik input legyen, a másik output.

Observations: A felvett csövek valóban elvesztik az input/output tulajdonságukat, ha felveszik őket.

Scenario 3: Helyesen kezeli-e a játék, ha olyan csövet veszek fel, amin egy olyan karakter áll, akinek a kezében szintén van egy cső?

Steps Taken: Felveszek egy olyan csövet, amin áll egy karakter, akinek már van cső a kezében. Ezután lépkedni fogok a karakterekkel: megpróbálok le/fel lépni a csőről azzal a karakterrel, amely csövet fog, de felemelt csövön is van. Megpróbálok vele a felvett vég felé lépni, ezt megteszem úgy is, hogy pumpán áll az, aki felvette a felvettet, meg úgy is, hogy csövön.

Observations: A játék helyesen kezeli az összes esetet. A karakterek megfelelően mozognak, és a felvett itemek is velük.

Scenario 4: Az előző folytatásaként: Helyesen le lehet-e helyezni a csöveket ezek után?

Steps Taken: Azzal, aki olyan csövet fog, amin van egy csövet fogó karakter, átlépek egy pumpára. Lehelyezem oda a csövet, majd átlépek a pumpára a csövön lévő karakterrel is. Majd megpróbálom azt a csövet is lehelyezni oda.

Observations: Helyesen le tudom helyezni a csöveket a pumpára.
# Debrief
A tesztelés közben ellenőriztem, hogy amikor felvesz egy karakter egy csövet, a csövön végrehajtott különböző input/output műveletek, illetve speciális esetek helyesen hajtódnak-e végre. Azt találtam, hogy a cső minden esetben helyesen kezeli a felvett állapotának összehangolását az egyéb állapotaival, akcióival.
