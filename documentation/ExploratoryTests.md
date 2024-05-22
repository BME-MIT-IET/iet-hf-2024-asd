## Exploratory tests

A Termék/felhasználó fókusz exploratory tesztek készítése feladatot Ignáth Dávid és Romoda Bálint készítette el.

Az exploratory tesztek készítéséhez az alkalmazást futtattuk, majd az eredményeket/észrevételeket markdown dokumentumokba írtuk le. A tesztek leírása megtalálható az ExploratoryTests mappában találhatóak.

Az ehhez tartozó Pr: #13

A teszteket külön-külön végeztük el, majd egymást leellenőriztük.

Ignáth Dávid által készült tesztek:

- IllegalCommands

- NewPumpsAndPipesTests

- PreGameTest

- WaterflowTest

- WinningTest

A tesztek részletei a megfelelő .md file-ban részletezve.

Romoda Bálint által készült tesztek:

- MovementWithPipePickedUpTestDocumentation

- MultipleStickyPipeTestDocumentation

- NonMovementPickedUpPipeTestDocumentation

- SpecialPickedUPipeTestDocumentation

Movement with piped picked up tesztek:

A tesztek célja az volt, hogy ellenőrizzem, hogy a különböző mozgások megfelelően működnek-e, miközben egy cső a karakter kezében van.

Multiple sticky pipe tesztek:

A tesztek célja, hogy ellenőrizzem, hogy a játék megfelelően kezeli, ha több cső is ragacsos, illetve ha azokra különböző időpontokban lépnek rá.

Non movement picked up pipe tesztek:

A tesztek célja, hogy ellenőrizzem parancsok helyes működését, amikor egy cső a karakter kezében van.

Special picked up pipe tesztek:

A tesztek célja, hogy ellenőrizzem az input/output parancsok és egyéb, speciális esetek helyes működését, amikor egy cső a karakter kezében van.
