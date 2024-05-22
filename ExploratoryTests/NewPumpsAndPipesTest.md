# Test Charter
Title: NewPumpsAndPipes

Objective: Ciszternánál termelt pumpák és csövek használata, beépítése a csőrenszerbe

Scope: Pontok és vízfolyás változása új pumpákkal és csövekkel 

Tester's Name: Ignáth Dávid

Date: 2024.05.20

# Session Details
- Session Duration: 60 perc
- Start Time: - 10:30
- End Time: - 11:30

# Preconditions
A tesztek elvégzéséhez el kell menni a ciszternához és megvárni, míg spawnol egy új cső vagy pumpa, ami random történhet minden kör elején.
# Exploration notes
**Scenario 1: Új csövet le lehet-e tenni**

- Steps Taken: Miután felvettem egy új csövet az „n” gomb megnyomásával, átmentem egy pumpára és leraktam az „a” gomb megnyomásával
- Observation: Az új csövet le lehet tenni, hasonló módón, mintha egy már játék kezdete óta létrehozott csövet.

**Scenario 2: Új pumpát le lehet-e tenni**

- Steps Taken: Miután felvettem egy új pumpát a„p” gomb megnyomásával, átmentem egy csőre és leraktam az „q” gomb megnyomásával
- Observation: Az új pumpá le lehet tenni.

**Scenario 3: Vizet tároló pumpára új cső kötése**

- Steps Taken: Kicseréltem egy olyan csövet, ami eddig vizet szállított a ciszternába egy új a ciszternából készült csővel. A régi csövet egy másik útvonalra kötöttem, ami nem befolyásolja az új csövet.

- Observations:Az új cső nem szállította a vizet a ciszternába. Ennek az oka az, hogy nem állítottam át a víz folyását, az „i- input” vagy „o-output parancs használatval”. 

**Scenario 4: Vizet szállító csőre új pumpát rakni.**

- Steps Taken: Egy új pumpát raktam egy vizet szállító csőre.
- Observation: A vízáramlás megáll, ha új pumpát szerelünk egy vizet szállító, csőre. A szerelők nem kapnak pontokat. Ennek az oka az, hogy nem állítottam át a víz folyását, az „i- input” vagy „o-output parancs használatval”.

**Scenario 5: Forrásra csatolt csőre lehet-e pumpát rakni**

- Steps Taken: Egy új pumpát raktam egy forrásra csatolt csőre.
- Observation: A parancs nem hajtódik végbe. Ennek az oka az, hogy nem lehet pumpát rakni egy forrásra, csak csövet csatlakoztatni

# Debrief
A ciszternánál felvett pumpákat csöveket le lehet helyezni a megfelelő tárgyakra. Az újonnan felrakott csövek/ pumpák nem szállítanak/pumpálnak vizet alapból.

Discussion points: Csapattal megbeszélve ez a viselkedés nem egy hiba. Ha új csövet és pumpát helyezünk le a csőszerkezetve, azokra a víz folyását egy külön paranccsal („i – input”, „o – output”) kell beállítani. 
