###########################################
#         Lokale Kopie erstellen          #
###########################################

git clone git@gitlab.informatik.uni-bremen.de:serpentin/bibliothekssoftware.git



###########################################
#             Eclipse Import              #
###########################################

Rechtsklick -> Import -> Maven -> Existing Maven Project.



###########################################
#          Änderungen prüfen              #
###########################################

git pull



###########################################
#          Änderungen hochladen           #
###########################################

"git add *"  (Bei warning: "git add --all")

"git commit -m "Ich bin eine Nachricht"

"git push" 



###########################################
#   Lokale Änderungen rückgängig machen   #
###########################################

"git checkout --"

"git fetch origin"

"git reset --hard origin/master"