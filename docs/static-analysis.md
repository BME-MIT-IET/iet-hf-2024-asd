# Manuális kód átvizsgálás

IntelliJben megnyitottam a projektet és ott ellenőriztem azokat az osztályokat, amiket én írtam még a projekt laborban.

## Észrevételek

1. Helykitöltő metódusokat meg lehetne jelölni absztraktként és az alosztályokban megvalósítani őket
2. `toString` metódus lehetne olvashatóbb
3. Elírások
4. Az objektumorientált elveket megszegő függvények (nem javítottuk, mert ez a rossz tervezés ereménye volt és ezek a működést nem befolyásolják).

# Statikus analízis eszköz futtatása és jelzett hibák átnézése

A statikus kódanalízis során SonarLint és SonarCloud segítségével számos hibát azonosítottam, különösen a elnevezési konvenciók terén, ezeket nem javítottam, mert sok volt belőlük és nem tanultam volna sokat belőle. Magas súlyosságú hibákat, például a nem megfelelően használt final típusú adattagok elnevezését, javítottam. Továbbá refaktoráltam hosszú és bonyolult függvényeket, elláttam a switch utasításokat default esetekkel, és biztonságossá tettem a random számgenerátorokat. Ezek a változtatások hozzájárultak a kód karbantarthatóságának javításához és a potenciális hibák csökkentéséhez.
## Tanulság

Az IDE-k beépített kódanalizátorai nem mindig fedik fel az összes problémát. A jövőben meggondolom a statikus kódanalízis használatát, hogy elősegítsem a kód minőségének javítását.