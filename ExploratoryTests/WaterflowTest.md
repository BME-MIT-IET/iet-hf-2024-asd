# Test Charter
Title: Vízfolyás tesztek

Objective: Víz folyásának logikáját 

Scope: Víz folyás különböző esetekben

Tester's Name: Ignáth Dávid

Date: 2024.05.20

# Session Details
- Session Duration: 11:30
- Start Time: 13:00
- End Time: 90 perc
# Exploration notes
**Scenario 1: Víz folyása interakciók nélkül**

- Steps Taken: x gomb lenyomásával skippeltem mindenkit roundokon keresztül
- Observations: A szerelők körönként kapnak egy pontot. A víz folyását a csöveken található nyilak jelzik.

**Scenario 2: Víz folyása forrásra csatolt kilyukasztott csövekkel**

- Steps Taken: Egyik játékossal kilyukasztottam a forráshoz csatolt egyik csövet
- Observations: Ha egy cső kilyukadt, megváltozik a mintázata. A szabotőrök, akkor nyernek pontot ha egy csőböl kifolyik a víz. Ez azt jelenti, hogy arra nem jár pont ha egy olyan csövet lyukasztanak ki amiben nem folyhat víz. Ha viszont a víz elér a ciszternához, akkor a szerelők kapnak 1 pontot.

**Scenario 3: Víz folyása felvett csövekkel**

- Steps Taken: Felvettem mindkét csövet, ami a forráshoz van csatolva
- Observations: A játék ugyanúgy kezeli a felvett csövet, mintha ki lenne lyukadva.Ez mindenképp egy helyes megoldás, mert tulajdonképpen a forrásból kifolyik  az összes víz.

**Scenario 4: Víz folyása elromlott pumpával**

- Steps Taken: Skippeltem köröket míg el nem romlott az a pumpa, ami össze van kötve a forrás felső csövével.
- Observations: Senki nem kap pontot. Ennek a magyarázata az hogy a szerelők, azért nem kapnak pontot, mert nem tud a víz tovább folyni. A szabotőrök pedig azért nem mert nem folyik ki a víz sehonnan. Ez a működés helyes.

Scenario 5: **Víz folyása elmozgatott csővel.**

Tesztelés közben, rájöttem, hogy a szerelők csak egy csővel tudnak szerezni pontot. Ezt a csövet át akartam helyezni egy olyan helyre, ahol nem folyik a víz és megvizsgálni, hogy vajon kapnak-e pontot.

Steps taken: A ciszterna felső csövet felvettem és átraktam az alsó mellé.

A játék ekkor így néz ki:

![](Aspose.Words.58be4037-715c-49dd-8114-4e60acc045bf.001.png)

- Observation: Egy kör elteltével egyik csapat sem kapott pontot, ami a megfelelő eredmény. A csőről még a víz folyását jelző nyíl is lekerült.
- Issues found: Nem lehet tisztán látni, hogy kettő cső van egy pumpán. A két cső egymás tetejére kerül, mintha csak egy vastag vonal lenne 2 cső helyett.

**Scenario 6: Víz folyása új csőrendszerrel**

Megvizsgálom, egy új vízfolyási útvonallal, hogy  a játék logika konzisztens marad-e.

- Steps taken: Játék indítása után, egy a forráshoz csatlakoztatott csövet közvetlen a ciszternára kötöttem

Az eredeti pálya:

![](Aspose.Words.58be4037-715c-49dd-8114-4e60acc045bf.002.png)

Az új pálya:

![](Aspose.Words.58be4037-715c-49dd-8114-4e60acc045bf.003.png)

- Observation: A szerelők megkapják a megérdemelt pontot.
- Issues found: A átrakott csőről eltűnt a vízfolyást jelző nyíl.

Bugs and Issues:

Két UI hibát találtam, Scenario 5 és 6-ban.

# Debrief
A vízfolyás helyesen lett implementálva, a játék szabályait nem lettek megszegve egy Scenariovan sem.

Follow-Up Actions: A UI kódjában kijavítani a két talált hibát.

