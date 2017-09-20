<!--
 license: Copyright 2017, Stichting Kennisnet
          
          Licensed under the Apache License, Version 2.0 (the "License");
          you may not use this file except in compliance with the License.
          You may obtain a copy of the License at
          
              http://www.apache.org/licenses/LICENSE-2.0
          
          Unless required by applicable law or agreed to in writing, software
          distributed under the License is distributed on an "AS IS" BASIS,
          WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
          See the License for the specific language governing permissions and
          limitations under the License.
-->

# Nummervoorziening - Java Client Reference Application

## Algemeen

De Nummervoorziening - Java Client Reference Application is een client implementatie in Java dat middels diverse unit tests de communicatie met de Nummervoorziening applicatie binnen de Educatieve Contentketen aantoont. De applicatie is opgezet als een Java Library aangevuld met een Unit Test Project en een Console Application. De Java Client Reference Application implementeert globaal de volgende functionaliteiten;
 * Globale communicatie met de Nummervoorziening (PingOperation t.b.v. het vaststellen van een correcte verbinding met en werking van de Nummervoorziening applicatie).
 * Genereren van een eerste niveau hash van een PGN middels het SCrypt hashing algoritme.
 * Het opvragen van een Stampseudoniem bij de Nummervoorziening op basis van een gehashte PGN (RetrieveStampseudoniemOperation).
 * Het opvragen van een ECK-ID bij de Nummervoorziening op basis van een gehashte PGN, een keten-id en sector-id (RetrieveEckIdOperation).
 * Het opvragen van ondersteunde ketens bij de Nummervoorziening (RetrieveChainsOperation).
 * Het opvragen van ondersteunde sectoren bij de Nummervoorziening (RetrieveSectorsOperation).
 * Opvoeren van nieuwe gehashte PGNs die dienen te worden doorverwezen naar reeds bestaande gehashte PGNs bij het creëren van ECK-IDs (ReplaceEckIdOperation).
 * Het opvoeren van een batch met gehaste PGNs (SubmitEckIdBatchOperation).
 * Het opvragen van de status en/of ophalen van een batch met ECK-IDs (RetrieveEckIdBatchOperation).

Voor alle bovenstaande functionaliteiten wordt gebruik gemaakt van SOAP Messaging tussen de verschillende actoren. Authenticatie vindt plaats middels PKI-Certificaten die over TLS worden uitgewisseld. Aanvullende informatie over Nummervoorziening kan gevonden worden op de [Edukoppeling Wiki] van [Stichting Kennisnet].  

## Version
0.3-20170408

## Gebruikte Technologiën

Bij de ontwikkeling van Nummervoorziening - Java Client Reference Application zijn diverse technologiën ingezet:
 * Java 8
 * IntelliJ IDEA 2016.3 - Ontwikkelomgeving
  * Maven 3.3.9 (bundled) - Build tool
 * Fiddler - TLS/SSL debugging
 * Wireshark - TLS/SSL debugging
 * SOAP UI - Functional Testing framework for SOAP and REST APIs
 
### Dependencies

| GroupId | ArtifactId | Version | Beschrijving |
| ------- | :--------: | :-----: | -----------: |
|com.lambdaworks|scrypt|1.4.0|SCrypt library|
|junit|junit|4.12|Unit Test framework|
||||||

 ### Plugins
 
| GroupId | ArtifactId | Version | Beschrijving |
| ------- | :--------: | :-----: | -----------: |
|org.apache.maven.plugins|maven-compiler-plugin|2.6|Release compiler|
|org.apache.maven.plugins|maven-assembly-plugin|2.6|Release packager|
|org.sonarsource.scanner.maven|sonar-maven-plugin|3.0.1|Code Quality check|
|org.owasp|dependency-check-maven|1.3.4|Controleert op security issues|
|org.jvnet.jax-ws-commons|jaxws-maven-plugin|2.3|Classgenerator op basis van WSDL|
|org.codehaus.mojo|build-helper-maven-plugin|1.10|Classgenerator op basis van WSDL|
||||||

Alle dependencies en plugins worden beheerd middels Maven; er hoeven geen aanvullende zaken manueel geïnstalleerd te worden door de ontwikkelaar zelf. 

  
## Communicatie
De Client Reference Application communiceert met de Nummervoorziening applicatie middels het SOAP-messaging protocol. Hierbij wordt het profiel 2W-be (tweeweg, Best Effort) zoals beschreven in de Edukoppeling Transactiestandaard versie 1.2 en Digikoppeling WUS 3.0 gevolgd. Praktisch gezien houdt dit het volgende in:
 * synchrone uitwisselingen die geen faciliteiten voor betrouwbaarheid (ontvangstbevestigingen, duplicaateliminatie etc.) vereisen. Voorbeelden zijn toepassingen waar het eventueel verloren raken van sommige berichten niet problematisch is en waar snelle verwerking gewenst is.
 * gebaseerd op SOAP 1.1.
 * gebruik van de verplichte WS-Addressing 1.0 headers, waarbij de From header verplicht is, en de ReplyTo header niet wordt gebruikt.
 * gebruik van PKIoverheid certificaten voor point-to-point security.
 * geen gebruik van End-to-End beveiliging (geen WS-Security: signing en/of encryptie van de SOAP headers of body).

## Project Modules
 * **ConsoleApplication**: Voorbeeldapplicatie om de werking van de *schoolID* module te demonstreren 
 * **SchoolID**: Library met de basisfunctionaliteiten om de Nummervoorziening applicatie op een juiste wijze te kunnen bevragen. 
 * **UnitTestProject**: Voorbeeldcode voor het gebruik van de Nummervoorziening applicatie, tevens gebruikmakend van de *SchoolID* module en de *scrypt* library van *com.lambdaworks*. Om de Java Reference Application analoog te houden aan de C#.NET variant is er voor gekozen om een separaat UnitTest module op te nemen in de code. 
 * **pom.xml**: Maven parent build bestand voor het gehele project.

### ConsoleApplication - Structuur
 * **n.k.n.c.c/Program.java**: Simpele class met een Main hook. Deze class wordt als Console applicatie gebruikt om een subset van specifieke functionaliteiten en bijbehorende operaties te demonstreren.  
 * **pom.xml**: Maven build bestand voor de ConsoleApplication module.
 
### SchoolID - Structuur  
 * **n.k.n.c.s/scrypter**: Bevat de logica ter aansturing van de scrypt library.
    * *Constants.java* De SCrypt constanten zoals vastgesteld.
    * *ScryptUtil.java* Bevat de *generateHexHash()* functie die in de rest van de Library wordt gebruikt om de eerste niveau hash te berekenen.  
 * **n.k.n.c.s/AuthorizedSoapHeaderOinInterceptor.java**: Interceptor class voor het toevoegen van de vereiste From header aan de SOAP Header van de berichten.
 * **n.k.n.c.s/Configuration.java**: Map class voor het ophalen en verwerken van de parameters uit het *config.properties* bestand.
 * **n.k.n.c.s/SchoolIDBatch.java**: Map class voor de opslag en verwerking van opgehaalde batches uit de Nummervoorziening applicatie gebruikmakend van standaard Java objecten.  
 * **n.k.n.c.s/SchoolIDServiceUtil.java**: Service util class voor centrale initializatie van de verbinding met de Nummervoorziening applicatie (certificaten & WS-Adressing) en het uitvoeren van operaties.
 * **n.k.n.c.s/TrustAllX509TrustManager.java**: Override class voor het toestaan van self-signed certificaten.
 * **Resources**: Aanvullende bestanden ter ondersteuning van de Solution.
    * *client_certificate_test.jks*: Certificate store met daarin het client certificaat ter authenticatie aan de Nummervoorziening applicatie.
    * *schoolid.wsdl*: De WSDL welke is gebruikt als input voor het genereren van de classes.
 * **pom.xml**: Maven build bestand voor de SchoolID module.
 
### UnitTestProject - Structuur
 * **n.k.n.c/AbstractUnitTest.java**: Basis Class voor het initializeren van de SchoolIDServiceUtil instance. Daarnaast bevat deze class diverse variabelen die als input dienen voor de testen. Alle UnitTest classes erven over van de AbstractUnitTest class.
 * **n.k.n.c/PingOperationTest.java**: Voorbeeldcode voor het uitvoeren van een Ping Operation: het uitlezen van de status van de Nummervoorziening applicatie.
 * **n.k.n.c/ReplaceStampseudonymOperationTest.java**: Voorbeeldcode voor het uitvoeren van een Replace Stampseudonym Operation: het vervangen van een nieuwe HPGN door een reeds bestaande HPGN om zodoende het reeds uitgegeven Stampseudoniem te kunnen blijven gebruiken in de keten bij een PGN/BSN wijziging.
 * **n.k.n.c/RetrieveChainsOperationTest.java**: Voorbeeldcode voor het uitvoeren van een Retrieve Chains Operation: het ophalen van ondersteunde ketens in de Nummervoorziening applicatie.
 * **n.k.n.c/RetrieveEckIdBatchOperationTest.java**: Voorbeeldcode voor het uitvoeren van een Retrieve Eck ID Batch Operation: het ophalen van een batch van ECK ID in de Nummervoorziening applicatie op basis van Stampseudoniemen, keten id en sector id.
 * **n.k.n.c/RetrieveEckIdOperationTest.java**: Voorbeeldcode voor het uitvoeren van een Retrieve Eck ID Operation: het ophalen van een enkele ECK ID in de Nummervoorziening applicatie op basis van een Stampseudoniem, keten id en sector id.
 * **n.k.n.c/RetrieveSectorsOperationTest.java**: Voorbeeldcode voor het uitvoeren van een Retrieve Sectors Operation: het ophalen van ondersteunde sectoren in de Nummervoorziening applicatie.
 * **n.k.n.c/RetrieveStampseudonymOperationTest.java**: Voorbeeldcode voor het uitvoeren van een Retrieve Stampseudoniem Operation: het ophalen van een enkele ECK ID in de Nummervoorziening applicatie op basis van een eerste niveau hash.
 * **n.k.n.c/ScryptUtilTest.java**: Voorbeeldcode voor het genereren van een eerste niveau hash op basis van een PGN.
 * **n.k.n.c/SubmitEckIdBatchOperationTest.java**: Voorbeeldcode voor het aanleveren van meerdere HPGNs als een batch aan de Nummervoorziening applicatie.
 * **pom.xml**: Maven build bestand voor de UnitTestProject module.

## Applicatie Configuratie
De applicatie configuratie is opgenomen in het */config.properties* bestand in de vorm van key/value pairs. Zowel de ConsoleApplication als de UnitTestProject maken gebruik van deze configuratie. Dit configuratiebestand wordt buiten het Classpath aangeroepen zodat wijzigingen hierin niet een hercompilatie vereisen. Omdat de Configuration class vanuit verschillende plekken kan worden aangeroepen, wordt hierbij intern het volledige pad opgevraagd, en alles na */JavaReferenceImplementation/ gestript, zodat altijd wordt verwezen naar de plek waar het bestand zich bevindt. 

### Parameters
 * **endpoint.address**: De url van de Nummervoorziening applicatie.
 * **certificate.KeyStorePath**: De (relatieve) locatie van de Certificate store waarin het client certificaat is opgenomen.
 * **certificate.KeyStorePassword**: Het wachtwoord van de Certificate store.
 * **certificate.Password**: Het wachtwoord van het client certificaat.
 * **client.instanceOin**: De op de BRIN4 gebaseerde OIN van de School.

Let op: bij wijzigingen in de config.properties dient de SchoolID module opnieuw te worden gecompileerd, aangezien dit configuratiebestand binnen het classpath wordt opgenomen als onderdeel van de SchoolID package.
   
## Ontwikkeling

### WSDL naar Proxy Class
Voor de ontwikkeling van de applicatie is de WSDL als uitgangspunt genomen. De gebruikte WSDL is onderdeel van de Reference Application package, en is te vinden in *SchoolID/src/main/resources/schoolid.wsdl*.

Vanuit de *pom.xml* in de *SchoolID* module wordt de meegeleverde WSDL gedurende het compileren verwerkt en omgezet in classes. De classes worden vervolgens gebruikt in de *SchoolID* module voor de communicatie met de Nummervoorziening. 

De juiste invulling van de *From* SOAP header is tezamen met het ondersteunen van *self-signed* certificaten (voor testdoeleinden) opgenomen in de initialization methode van de *SchoolIDServiceUtil* class. De From header wordt bepaald en toegevoegd vanuit de SOAP interceptor class *AuthorizedSoapHeaderOinInterceptor*. 

## Installatie
De applicatie is gebouwd voor Java 8 en getest op diverse platformen. Om de applicatie succesvol te laten draaien moet aan een aantal randvoorwaarden worden voldaan:
 * Java 8 (OpenJDK of Oracle JDK)
 * De machine dient een geldig en geregistreerd TLS client certificaat te hebben waarmee geidentificeerd kan worden bij de Nummervoorziening applicatie
 * Het certificaat is in een Certificate Store (JKS bestand) opgenomen en in de juiste directory geplaatst (*SchoolID/src/main/resources/*)
 * *SchoolIDServiceUtil* variabelen zijn aangepast op basis van certificaat gegevens
 * Maven is geinstalleerd.
 * De aanvullende plugins en/of libraries zijn middels Maven opgehaald.

Middels het *install* commando kan Maven vervolgens de classes compileren. Vervolgens kan de ConsoleApplication en/of de Unit testen worden uitgevoerd. 

Client certificaten voor de communicatie met de Nummervoorziening applicatie op de Sandbox omgeving dient bij Kennisnet opgevraagd te worden. Voor de productieomgevingen is een valide PKI-Overheid certificaat vereist.

## Omgevingen
 * Acceptatieomgeving: https://service-a.id.school/eck/ws/201703
 * Sandboxomgeving: https://service-s.id.school/eck/ws/201703
 * Kwalificatieomgeving: https://service-q.id.school/eck/ws/201703
 * Productieomgeving: https://service.id.school/eck/ws/201703

## Licenties
 * **Nummervoorziening - Java Client Reference Application**: Apache License, Version 2.0.
 * **Scrypt Java library**: Apache License, Version 2.0. <https://github.com/wg/scrypt>

## Contact
Voor meer informatie kunt u contact opnemen met [Marc Fleischeuers](mailto:M.Fleischeuers@kennisnet.nl).

** Copyright(c) 2017 [Stichting Kennisnet]**

[//]: # (These are reference links used in the body of this note)
   [Edukoppeling Wiki]: <http://developers.wiki.kennisnet.nl/index.php?title=Standaarden:Edukoppeling>
   [Stichting Kennisnet]: <http://www.kennisnet.nl>
