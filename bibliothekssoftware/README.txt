############################################################
#                   Voraussetzung                          #
############################################################

Java SE 7u45 muss Installiert sein

############################################################
#                         ADT                              #
############################################################
1. Downloaden, entpacken

http://developer.android.com/sdk/index.html

2. SDK-Manager starten

Folgende Pakete sollten installiert sein:

Tools
     Android SDK Tools             22.3
     Android SDK Platform-tools    19
     Android SDK Build-tools       17

Android 4.2.2
     SDK Platform
     ARM EABI v7a System Image

############################################################
#                        MAVEN                             #
############################################################

Windows:

http://maven.apache.org/download.cgi

=> Systemsteuerung\System und Sicherheit\System

Auf "Erweiterte Systemeinstellungen" klicken, im Reiter "Erweitert" auf "Umgebungsvariablen" klicken.

Folgende Benutzervariablen anlegen:

ANDROID_HOME    Pfad zum Ornder sdk im adt-bundle
     M2_HOME    Pfad zum Maven Ordner
   JAVA_HOME    Pfad zum jdk Ordner
        PATH    %JAVA_HOME%\bin;%M2_HOME%\bin;%ANDROID_HOME%\tools;%ANDROID_HOME%\platform-tools

Rechner neustarten.


Linux:

sudo apt-get install maven

############################################################
#                        Eclipse                           #
############################################################

Help->Install New Software

m2e   
Updatesite: http://download.eclipse.org/technology/m2e/releases

m2e-android
Updatesite: http://rgladwell.github.com/m2e-android/updates/
=> Android Connector for M2E

m2e-wtp
Updatesite: http://download.eclipse.org/m2e-wtp/releases/juno
=> Maven Integration for WTP

glassfish
Updatesite: http://download.java.net/glassfish/eclipse/helios

checkstyle
Updatesite: http://eclipse-cs.sf.net/update/
=> Checkstyle

Windows:

cd %ANDROID_HOME%/platform-tools
mklink aapt.exe "../build-tools/17.0.0/aapt.exe"
mklink /D lib "../build-tools/17.0.0/lib"
mklink aidl.exe "../build-tools/17.0.0/aidl.exe"

Linux:

cd $ANDROID_HOME/platform-tools
ln -s ../build-tools/17.0.0/aapt aapt
ln -s ../build-tools/17.0.0/lib lib
ln -s ../build-tools/17.0.0/aidl aidl

############################################################
#            ANDROID VIRTUAL DEVICE MANAGER                #
############################################################

Auf "New..." klicken.

Handy nach Wahl einrichten.

Beispiel:

        AVD Name: test
          Device: Nexus S (4.0", 480 x 800: hdpi)
          Target: Android 4.2.2 - API Level 17
         CPU/ABI: ARM (armeabi-v7a)
        Keyboard: Haken setzen
            Skin: Haken setzen
    Front Camera: none
     Back Camera: none
  Memory Options:
                  RAM: 343 (oder mehr)
                  VM Heap: 32 (oder mehr)
Internal Storage: 200 MiB
         SD Card: <frei lassen>
 Emulate Options: <so lassen>


############################################################
#                        GLASSFISH                         #
############################################################

Window->Preferences

=> Java => Installed JREs

Prüfen, ob JDK angezeigt wird.

Falls nicht:

"Add..." klicken

"Standard VM" auswählen

JRE home: Pfad zum JDK

"Finish"

Haken bei JDK setzen! 





Window->Preferences

=> GlassFish Preferences

Haken bei: "Start the JavaDB database process when Starting GlassFish Server" setzen.





Window->Show View->Other

View "Server/Servers" öffnen

In Glassfish-Konsole "Internal Glassfish 3.1": Run-Icon

In Glassfish->Resources->JDBC->JDBC Resources:
"new..." klicken

JNDI Name: jdbc/libraryDB
Pool Name: DerbyPool

"Save" klicken.



In Glassfish->Configurations->Server Config->Security->Realms:
"new..." klicken

Realm Name: security 
Class Name: com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm 

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



