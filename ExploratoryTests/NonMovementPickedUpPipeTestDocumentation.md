# Test Charter
Title: ActionsWithPipePickedUP

Objective: Bugok keresése, ha egy karakter felvesz egy csövet

Scope: Nem mozgással (egyéb parancsokkal) kapcsolatos bugok

Tester's Name: Romoda Bálint

Date: 2024.05.20

# Session Details
- Session Duration: 35 perc

- Start Time: 19:40

- End Time: 20:15
# Exploration notes
**Scenario 1: Ha felveszek egy csövet, akkor lehet-e sticky az a cső?**

- Steps Taken: Felvettem az egyik karakterrel egy csövet, ami mellette volt, odébb léptem vele, majd ráléptem egy másikkal. A következő körben sticky-vé tettem, majd leléptem róla és vissza.

- Observations: A sticky beállítható, ha odébb mozgunk vele, akkor is megmarad a tulajdonsága, ha rálépek, akkor tényleg odaragad a karakter megfelelő ideig.

**Scenario 2: Ki tudom-e lyukasztani, illetve meg tudom-e javítani a felvett csövet?**

- Steps Taken: Felveszek egy csövet. Egy másik karakterrel kilyukasztom. Lerakom a csövet. Ismét felveszem, majd megjavítom egy másik karakterrel. Ismét lerakom.

- Observations: A cső helyesen mutatta állapotát minden esetben.

**Scenario 3: Ha csúszóssá teszem a csövet, akkor megfelelően kezeli-e a felvételt?**

- Steps Taken: Felveszek egy csövet. Egy másik karakterrel csúszóssá teszem. Lelépek a csőről, majd vissza, ameddig csúszós, majd a végén még egyszer, amikor már nem.

- Observations: A cső helyesen mutatta állapotát minden esetben. A karakter minden alkalommal a pumpához csatolt végéhez csúszott a csőnek, majd az utolsó lépéskor már nem csúszott. Ez így helyes.
# Debrief
A tesztelés közben ellenőriztem, hogy amikor felvesz egy karakter egy csövet, a csövön végrehajtott különböző műveletek helyesen hajtódnak-e végre. Azt találtam, hogy a cső minden esetben helyesen kezeli a felvett állapotának összehangolását az egyéb állapotaival, akcióival.
