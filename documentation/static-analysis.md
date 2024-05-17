Mivel azon a projlab projekten dolgozunk, aminek az elkészítésében én is részt vettem, a feladataimat a projekt azon részén végzem el, amiket én ítam.
Ezek a Character, MechanicCharacter és SaboteurCharacter osztályok

# Manuális kód átvizsgálás

## Character

A Character osztály a játékban szereplő karakterek absztrakt reprezentációja. Megvalósítja a Nameable és a Comparable<Character> interfészeket, és különféle módszereket biztosít a játék környezetével való interakcióra, például mezők közötti mozgásra, csövekkel és pumpákkal való interakcióra, valamint különböző állapotok kezelésére, mint például a beragadás.

**Észrevételek:**
1. <del>Helykitöltő metódusokat meg lehetne jelölni absztraktként és az alosztályokban megvalósítani őket<del>
2. A compareTo metódus nem túl oop, de túl sok dolog épít rá a projekt többi részében
3. Az equals metódus sem túl oop, de túl sok dolog épít rá a projekt többi részében
4. <del>Typo: In word 'recieve'<del>

## MechanicCharacter

A MechanicCharacter osztály egy speciális karaktert reprezentál a játékban, aki képes csöveket és pumpákat javítani vagy állítani. Az osztály kiterjeszti a Character osztályt, és új funkcionalitást ad hozzá a játékhoz.

**Észrevételek:**
1. <del>Lehetne javítani a toString metóduson, hogy olvashatóbb legyen<del>
2. A null ellenőrzések néha !=, néha ==-vel történnek, ami fejlesztés közben is problémát okozott az átláthatóságban
3. <del>Typo: In word 'recieve'<del>

## SaboteurCharacter

A SaboteurCharacter osztály egy speciális karaktert reprezentál a játékban, aki képes csöveket és pumpákat tönkretenni vagy állítani. Az osztály kiterjeszti a Character osztályt, és új funkcionalitást ad hozzá a játékhoz.

**Észrevételek:**
1. <del>Lehetne javítani a toString metóduson, hogy olvashatóbb legyen<del> 

# Statikus analízis eszköz futtatása és jelzett hibák átnézése

