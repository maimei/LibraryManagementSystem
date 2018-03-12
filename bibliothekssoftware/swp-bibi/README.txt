Anforderungen
=============

Android Development Kit (ADT)
-----------------------------

Download des ADT-Bundle. Enthält Eclipse und SDK unter:
http://developer.android.com/sdk/index.html

Bei Installation von ADT für existierendes Eclipse:
Setzen von ANDROID_HOME und JAVA_HOME.

cd $ANDROID_HOME/platform-tools
ln -s ../build-tools/17.0.0/aapt aapt
ln -s ../build-tools/17.0.0/lib lib
ln -s ../build-tools/17.0.0/aidl aidl

Maven
-----

Maven3 muss installiert sein.
In Windows Path ggf. um mvn erweitern.

Unter Help->Install New Software eine Updatesite für m2e einfügen.
Anleitung: http://eclipse.org/m2e/download/
Updatesite: http://download.eclipse.org/technology/m2e/releases 

Weitere Updatesite für m2e-android (Android Connector for M2E):
Anleitung: http://marketplace.eclipse.org/content/android-configurator-m2e
Updatesite: http://rgladwell.github.com/m2e-android/updates/
=> Android Connector for M2E

m2e-wtp für Zusammenspiel mit Glassfish
http://download.eclipse.org/m2e-wtp/releases/juno
=> Maven Integration for WTP (Incubation)

Java 6 SDK oder Java 7 SDK
--------------------------

http://www.webupd8.org/2012/01/install-oracle-java-jdk-7-in-ubuntu-via.html

Glassfish for Eclipse/Juno
--------------------------

Glassfish version 3.1
- - - - - - - - - - -

Unter Help->Install New Software
http://download.java.net/glassfish/eclipse/helios
(auch für Juno!)
- Glassfish Application Server

Glassfish version 4.0 (still experimental, not recommended)
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

Unter Help->Install New Software for Eclipse/Juno
Update site: http://download.oracle.com/otn_software/oepe/juno
- Glassfish Tools 
- Java EE 6 Documentation
- Oracle ADF Tools

Download Glassfish runtime from:
https://glassfish.java.net/download.html
Unpack the archive.
Add the target directory as runtime path to server configuration
(reachable from tab "Servers" in view "Servers").

For updating to Glassfish 4, see also:
https://blogs.oracle.com/piotrik/entry/new_version_of_glassfish_eclipse

Einrichten von Glassfish
- - - - - - - - - - - - -

siehe Tutoriumsfolien 

Start von Glassfish in Eclipse:

View "Server/Servers" öffnen (Window=>Show View=>Other)
In Glassfish-Konsole "Internal Glassfish 3.1": Run-Icon

In Glassfish->Configurations->Server Config->Security->Realms:
Neuer Realm: "security"
Combo Box 'Class Name' Klasse JDBCRealm auswählen.
Unter 'Properties specific to this Class' eintragen: 
JAAS Context: jdbcRealm
JNDI: jdbc/libraryDB
User Table: READER
User Name Column: username
Password Column: password
Group Table: grouptable
Group Name Column: groupid
Assign Groups: <leer lassen>
Database User: <leer lassen>
Database Password: <leer lassen>
Digest Algorithm: MD5
Encoding: <leer lassen>
Charset: <leer lassen>

Aktueller default nutzer: "admin"
mit Passwort: "admin"

Checkstyle (optional)
---------------------

Checkstyle Eclipse Plugin Updatesite:
http://eclipse-cs.sf.net/update/

Unsere Java-Projekte
====================

Projekte einbinden mit:
Import->Existing Maven Projects
Dort Pfad zu den Projekten eingeben.

Aktive Projekte:
  bibcommon  - enthält Klassen, die im Client und Server benutzt werden
  bibclient  - Implementierung des Android Clients
  bibjsf     - Implementierung des Servers auf Basis von JSF

Anpassungen in Konfigurationsdateien
====================================

Konfiguration des Servers:
  bibjsf/src/main/java/bibi_config.properties

Darin muss man den Google-API-Key setzen, wenn man beim
Eintragen von Büchern auf die Google Book API zugreifen
möchte, um sich die Daten für ein Buch mit gegebener ISBN
zu besorgen. Diese Datei enthält auch Hinweise, wie man
sich einen solchen Key besorgt.

Maven-Strukturen
================

Neue Projekte Bauen mit Parent in Maven:
In den Kinderprojekten das Elternprojekt eintragen:
 <parent>
    	<groupId>swp</groupId>
    	<artifactId>all</artifactId>
    	<relativePath>../all/pom.xml</relativePath>
    	<version>0.0.1-SNAPSHOT</version>
    </parent>

In den Kinderprojekten als Dependency Common eintragen:
<dependency>
        	<groupId>swp</groupId>
        	<artifactId>bibcommon</artifactId>
        	<version>${project.parent.version}</version>
</dependency>

Im Elternprojekt die Kinderprojekte als Module eintragen:

<modules>
  	<module>../bib-server</module>
  	<module>../bibclient</module>
  	<module>../bibcommon</module>
</modules>

-----------------------------------------------------------

Squirell SQL:
Modify Drivers Tutorial:
http://www.ensode.net/squirrel_sql_2.html

-----------------------------------------------------------

Falls Eclipse unter Windows kein App deployed:  adb kill-server

Start
=====

1. Glassfish starten (siehe oben)

View "Server/Servers" öffnen (Window=>Show View=>Other)
In Glassfish-Konsole "Internal Glassfish 3.1": Run-Icon

2. bibjsf starten:
Projekt bibjsf auswählen; 
  Run As => Run on Server

Dann URL:
http://localhost:8080/bibjsf/index.xhtml

3. Android-Client bibclient starten

   Projekt bibclient auswählen
   Run As => Android Application

   ggf. Virtual Device konfigurieren:
   In "Android Device Chooser": 
   Launch a new Android Virtual Device
   => Manager
   In "Android Virtual Device Manager"
   => New
   Dort dann Gerät nach Wahl konfigurieren

