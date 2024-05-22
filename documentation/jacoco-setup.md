# Jacoco Kódlefedettség Vizsgáló Eszköz Integrálása

A Jacoco egy nyílt forráskódú eszköz, amely segít mérni, hogy a tesztjeink milyen mértékben fedik le a kódunkat. Ez az eszköz rendkívül hasznos a tesztelési folyamatok optimalizálása és a kódminőség javítása érdekében.

## Elvégzett Munka

### Jacoco Integrálása

Jacoco konfigurálása: A Jacoco-t integráltam a projekt build folyamatába, hozzáadva a szükséges konfigurációkat a build eszközhöz (Maven). Ez magában foglalta a Jacoco plugin hozzáadását és a lefedettségi jelentések generálásának beállítását.
Lefedettségi Jelentések Generálása: Beállítottam a Jacoco-t, hogy a tesztek futtatása után automatikusan generáljon részletes lefedettségi jelentéseket. Ezek a jelentések megmutatják, hogy a kód mely részei vannak lefedve tesztekkel és melyek nem.
Eredmények és Tanulságok
A Jacoco integrálásának eredményeként részletes lefedettségi jelentéseket kaptunk, amelyek segítségével pontosan láthatjuk, hogy a kódbázis mely részei vannak megfelelően tesztelve, és melyek igényelnek további tesztelést. Ez lehetővé teszi számunkra, hogy célzottan javítsuk a tesztlefedettséget, növelve ezzel a kód megbízhatóságát és karbantarthatóságát.