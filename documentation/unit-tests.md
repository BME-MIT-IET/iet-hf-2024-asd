# MechanicCharacter és SaboteurCharacter Egységtesztek

## Leírás

Ebben a munkában átfogó egységteszteket készítettem a MechanicCharacter és a SaboteurCharacter osztályokhoz a Model csomagban. Az egységtesztek célja, hogy biztosítsák ezeknek az osztályoknak a megfelelő működését különböző szituációkban, valamint a konstruktorok és a főbb metódusok helyes működését.

## Elvégzett Munka

### MechanicCharacter Tesztelése

A MechanicCharacter tesztelése során a következőket végeztem el:

- Konstruktorok ellenőrzése: Teszteltem, hogy a különböző konstruktorok helyesen állítják-e be az osztály attribútumait.
- Főbb metódusok tesztelése: Ide tartozik a csövek és pumpák javítása, felvétele és lerakása. Az ilyen tesztek segítenek biztosítani, hogy a mechanikus karakter a játék különböző helyzeteiben is megfelelően működjön, és helyesen kezelje azokat az eseteket is, amikor nincs pumpa vagy cső a karakter birtokában.

### SaboteurCharacter Tesztelése

A SaboteurCharacter tesztelése során a következőket végeztem el:

- Konstruktorok ellenőrzése: Teszteltem, hogy a különböző konstruktorok helyesen állítják-e be az osztály attribútumait.
- Főbb metódusok tesztelése: Ide tartozik a csövek csúszóssá tétele. Ezek a tesztek biztosítják, hogy a szabotőr karakter megfelelően végre tudja hajtani a neki szánt romboló tevékenységeket a játékban.

### Tesztelési Módszerek

A tesztek írása során a Mockito-t használtam a függőségek mockolásához. Ez lehetővé tette az izolált tesztelést, ahol csak a tesztelt osztály viselkedésére koncentráltam. A Field, Pump és Pipe osztályokat nem kellett ténylegesen implementálni, így gyorsabban és hatékonyabban tudtam tesztelni.

## Eredmények és Tanulságok
Ezek az egységtesztek jelentősen növelik a kódbázis megbízhatóságát és karbantarthatóságát. A jövőbeli fejlesztések során könnyen ellenőrizhetővé válik, hogy a változtatások nem okoznak-e hibát a meglévő funkciókban. A tesztek segítenek a kódminőség fenntartásában és a potenciális hibák korai felismerésében, ezáltal gördülékenyebbé téve a fejlesztési folyamatokat.