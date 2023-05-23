# GlamReserve

GlamReserve és una aplicació web que permet reservar sessions de perruqueria i serveis d'estètica. Els usuaris poden buscar locals pròxims per distància i tipus de servei, i reservar la seva cita en línia. Les empreses poden administrar les seves reserves mitjançant un panell d'administració.

## Característiques

Algunes de les característiques de GlamReserve inclouen:

- Cerca de locals per distància i tipus de servei
- Reserva de cites en línia
- Administració de reserves per a empreses

## Anàlisis d'accessibilitat

L'accessibilitat web és un aspecte clau per a garantir que totes les persones, independentment de les seves habilitats o discapacitats puguin accedir i utilitzar una aplicació web sense problemes. Per a complir amb les directrius d'accessibilitat de la Web Content Accessibility Guidelines (WCAG), hi ha una sèrie de pautes en els quals s'ha d'enfocar per a assegurar-se que l'aplicació web sigui accessible.

- Percepció: La percepció és la capacitat dels usuaris per a percebre i comprendre el contingut de l'aplicació web.

S'han de considerar les següents àrees:

Per al contrast de color i facilitar una lectura clara utilitzarem un fons #FFF amb unes lletres en #3A3845. En les imatges o altres elements no textuals es farà ús d'un text alternatiu perquè els usuaris puguin entendre el seu contingut sense necessitat de veure les imatges.

- Operabilitat: L'operabilitat és la capacitat dels usuaris per a poder navegar i operar en l'aplicació web.
S'han de considerar les següents àrees:

La navegació de l'aplicació web ha de ser clara i coherent, perquè els usuaris puguin trobar fàcilment el que estan buscant. A més, es proporcionarà ajuda contextual perquè els usuaris puguin entendre com funciona l'aplicació web i com fer tasques específiques.

- Comprensibilitat: La comprensibilitat és la capacitat dels usuaris per a entendre i utilitzar el contingut de l'aplicació web.

S'han de considerar les següents àrees:

S'haurà d'emprar un llenguatge clar i senzill fàcil d'entendre per a tots els usuaris. D'altra banda, el disseny en tota l'aplicació web ha de ser consistent, incloent-hi el disseny de la interfície d'usuari i la navegació.

Sobre els productes i serveis oferts per les empreses en el marketplace s'haurà d'oferir una informació rellevant i precisa.

- Robustesa: La robustesa és la capacitat de l'aplicació web per a funcionar correctament en diferents entorns i dispositius.

S'han de considerar les següents àrees:

Tant el codi HTML i CSS han de ser vàlids i ben estructurats. S'haurà d'assegurar que l'aplicació web funcioni correctament en els diferents navegadors web. Per tant, s'ha d'oferir la major compatibilitat amb els diferents dispositius, incloent-hi ordinadors d'escriptori, portàtils, tauletes i telèfons intel·ligents.

En resum, en garantir que l'aplicació web sigui accessible, es podrà arribar a una audiència més àmplia i millorar l'experiència d'usuari per a tots. També compliria amb els estàndards d'accessibilitat requerits per la llei i ajudaria a millorar la reputació del marketplace com un servei inclusiu i compromès amb la igualtat d'accés a la informació i tecnologia.

Per a poder verificar aquestes pautes establertes es pot fer ús del servei WAVE, https://wave.webaim.org/ , que permet avaluar el nivell de l'accessibilitat de l'aplicació web una vegada finalitzada.

## Elements funcionals

1. Registre d'usuari: Permetre als usuaris registrar-se i crear un compte per a poder reservar cites i accedir a altres funcionalitats de l'aplicació.

2. Alta com a empresa: Opció de l'usuari a convertir-se en empresa mitjançant una subscripció i poder prestar els seus serveis i tenir accés al servei web.

3. Cerca de serveis: Permetre als usuaris buscar serveis d'estètica segons les seves necessitats i preferències, com a tipus de servei, ubicació i preu.

4. Reserva de cites: Permetre als usuaris reservar cites amb empreses d'estètica segons la seva disponibilitat. Això inclouria un calendari de cites per a les empreses.

5. Gestió de cites: Permetre a les empreses d'estètica gestionar les cites que han estat reservades.

6. Perfil d'empresa: Permetre a les empreses d'estètica crear un perfil en l'aplicació que mostri informació sobre la seva descripció, serveis, preus, horaris de treball i ubicació.

7. Comentaris: Permetre als usuaris deixar comentaris sobre els serveis en les empreses assistides pels serveis consumits, la qual cosa pot ajudar altres usuaris a prendre decisions informades. Les empreses podran validar la ressenya.

8. Historial de cites: Permetre als usuaris veure les seves pròximes cites reservades.

9. Suport al client: Proporcionar un sistema de suport al client per a ajudar els usuaris i empreses d'estètica a resoldre qualsevol problema o inquietud que puguin tenir.

10. Llista de favorits: Permetre als usuaris guardar empreses favorites en una llista de favorits per a accedir fàcilment a elles en el futur.

11. Integració de Google Maps: Permetre als usuaris veure la ubicació de les empreses d'estètica en un mapa interactiu.

12. Personalització del perfil d'usuari: Permetre als usuaris modificar el seu perfil amb la seva informació.

## Experiència d'usuari i disseny d'interfície d'usuari (UX/UI)

L'objectiu d'aquest projecte és desenvolupar una plataforma de reserves per a centres d'estètica, que permet als usuaris buscar i reservar serveis de diferents centres d'estètica en un sol lloc. El sistema ha de ser fàcil d'usar, intuïtiu i atractiu per als usuaris.

1. Recerca d'usuaris
   Per a crear una bona experiència d'usuari, és important conèixer als usuaris i les seves necessitats. Realitzem una recerca per a comprendre els perfils dels usuaris i els seus comportaments en la reserva de serveis d'estètica. Els resultats d'aquesta recerca s'utilitzaran per a dissenyar la plataforma de reserva d'estètica de manera eficient.

2. Objectius de la plataforma
   La plataforma ha de tenir els següents objectius:

- Permetre als usuaris buscar centres d'estètica i serveis d'estètica de manera fàcil i ràpida.
- Oferir als usuaris informació detallada sobre els serveis oferts pels centres d'estètica.
- Permetre als usuaris reservar serveis d'estètica en línia.
- Facilitar el procés de pagament i proporcionar opcions de pagament segures.
- Proporcionar als centres d'estètica una plataforma fàcil d'usar per a administrar els seus serveis i reserves.

3. Arquitectura de la plataforma
   La plataforma serà desenvolupada en Spring Boot, la qual cosa proporcionarà una arquitectura robusta i escalable. S'utilitzaran les millors pràctiques de desenvolupament de programari per a garantir la qualitat del codi i l'estabilitat de la plataforma.

4. Disseny d'interfície d'usuari
   El disseny d'interfície d'usuari se centrarà en crear una experiència d'usuari fàcil i atractiva. S'utilitzaran els principis de disseny de UX/UI per a desenvolupar una plataforma fàcil d'usar i comprensible. S'utilitzarà una paleta de colors atractiva i es proporcionaran elements visuals per a millorar l'experiència d'usuari.

5. Proves i avaluacions
   La plataforma serà sotmesa a proves rigoroses per a garantir que funcioni correctament i que sigui fàcil d'usar per als usuaris. Es duran a terme avaluacions d'usabilitat per a identificar qualsevol problema en l'experiència de l'usuari i es realitzaran millores segons sigui necessari.

6. Implementació
   Una vegada que es completi el desenvolupament i es realitzin les proves, s'implementarà la plataforma en un entorn de producció. Es proporcionarà documentació detallada sobre l'ús i manteniment del sistema per a garantir que els usuaris i els centres d'estètica puguin utilitzar el sistema de manera efectiva.
