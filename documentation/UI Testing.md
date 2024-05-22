## UI Testing

A Termék/felhasználó fókusz UI tesztek készítése (Selenium, Tosca, Appium...) feladatot Ignáth Dávid és Romoda Bálint készítette el.
A UI tesztek készítéséhez a Tricentis Tosca 16.0 LTS verzióját használtuk. A Toscan Commander Repository megtalálhatók a "UITestingToscan" mappán belül "iet\_teszt" néven. A teszteket a projecten belül mappákba rendeztük, futtatást a Scratchbook-al végeztük el.

A feladat pull request-je: [#10](https://github.com/BME-MIT-IET/iet-hf-2024-asd/pull/10)
A PR le lett ellenőrizve és approvove-olva az egész csapat által.

# UI-ról megtanultuk:
Kék körrel van jelölve a vízforrás, naracssárgával a pumpák, sárgával a ciszterna. A fekete vonalak pedig a csövek, rajtuk lévő nyilak pedig a víz folyásának iránya. Ha van a ciszternánál felvehető új pumpa azt egy piros körrel, illetve ha van cső azt fekete vonallal jelöltük. A soron lévő játékos neve pirosan világít és megtalálható a bal felső sarokban, a pont és kör számlálóval együtt. Bal alul pedig a parancsok listája található, amelyeket az adott billentyű lenyomásával végezhetünk el.

A teszteket külön-külön végeztük el, majd egymást leellenőriztük.

# Ignáth Dávid által készült tesztek:

- PreGameTest: kipróbáljuk az összes UI elementet a játék előtti képernyőn.
- Attach Pipe tests: játékon belül az „a” gomb megnyomására több testcase
- Movement tests: játékon belül az „m” gomb megnyomására több testcase
- Pick up new pipe tests: játékon belül az „n” gomb megnyomására több testcase
- Pick up pipe tests: játékon belül az „p” gomb megnyomására több testcase
- Pick up pump tests: játékon belül az „b” gomb megnyomására több testcase
- Plcae pump tests: játékon belül az „q” gomb megnyomására több testcase

# Romoda Bálint által készült tesztek:

Csúszás tesztek:
Ezen tesztekben az "s - slippery" parancsot vizsgáljuk meg.
Erre 5 test case készült el:

- Csövet csúszóssá tenni.
- Elcsúszni egy csövön
  -A cső már nem csúszós.
- Csúszóssá tenni bármit, ami nem cső
- Átcsúszni egy vízforrásra.
  A tesztek a "Slippery tests" mappában elérhetők.

Sticky tesztek:
Ezen tesztekben a "t - sticky" parancsot vizsgáljuk meg.
Erre 3 test case készült:

- Csövet ragacsossá tenni, illetve ennek lefolyása
- A ragacsos tulajdonság megújítása, miközben oda vagyunk ragadva
- Ragacsossá tenni bármit, ami nem cső

Skip tesztek:
Ezen a tesztekben az "x - skip" parancsot vizsgáljuk meg.
Erre 2 test case készült:

- A skippelés tesztelése egy karakterre
- Az új körbe kerülés ellenőrzése

Holey tesztek:
Ezen a tesztekben a "h - holey pipe" parancsot vizsgáljuk meg.
Erre 4 test case készült:

- Egy alapállapotú cső kilyukasztása
- Egy kilyukasztott cső kilyukasztásának tesztelése
- Egy pumpa kilyukasztásának tesztelése
- Egy ciszterna kilyukasztásának tesztelése

Fix tesztek:
Ezen a tesztekben a "f - fix" parancsot vizsgáljuk meg.
Erre 4 test case készült:

- Egy lyukas cső megjavítása
- Egy alapállapotú cső megjavításának tesztelése
- Egy pumpa megjavításának tesztelése
- Egy ciszterna megjavításának tesztelése

Input/Output tesztek:
Ezen a tesztekben az "i - input" és "o - output" parancsokat vizsgáljuk meg.
Erre 5 test case készült:

- A pumpák megfelelően töltődnek
  Input specifikus:
- Sikeres input állítás
- Sikertelen input állítás
  Output specifikus:
- Sikeres output állítás
- Sikertelen output állítás

# Konklúzió:
A UI interakciók nem törik meg a játék szabályait. Ha egy parancs illegális lenne, akkor az nem hajtódik végre, viszont ha nem akkor egyértelműen látszik a változás.

# Tanulság:
Ilyen játékoknál a felhasználói élményt nagyban növeli, hogy ha egy parancsot nem tudtunk sikeresen végrehajtani, annak indokát visszajelezzük a felhasználóknak, ezáltal azok, akik nem ismerik teljesen a játék szabályait könnyebben megérthetik miért nem működött a parancs és tanulhatnak belőle.

