# Build keretrendszer beüzemelése
## Gradle vs Maven
A cikkek és vélemények alapján, amiket olvastam, arra jutottam, hogy a Maven keretrendszer lesz a megfelelő válazstás, többek között az alábbiak miatt:
- A Maven célja, hogy a szoftverprojektek építését és menedzsmentjét egységesítse és egyszerűsítse
- A Maven könnyen használható és gyorsan beállítható
- A Maven hosszú ideje létezik, ezért rengeteg forrás és közösségi támogatás áll rendelkezésre
- Kiválóan kezeli a függőségeket, automatikusan letölti és frissíti a szükséges könyvtárakat
## Megvalósítás
- IntelliJ- ben a projekten jobb klikk
- Add Framework Support
- Maven kiválasztása és Ok
- GroupId beállítása és esetleges dependenciek hozzáadása
## Tesztelés
Az app továbbra is fordult és helyesen futott.
Érdekesség: az IntelliJ- ben az importok miatt sok piros aláhúzás lett, de a futásban mégsem okozott hibát.