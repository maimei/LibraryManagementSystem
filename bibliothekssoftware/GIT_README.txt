###########################################
#         Lokale Kopie erstellen          #
###########################################

git clone git@gitlab.informatik.uni-bremen.de:serpentin/bibliothekssoftware.git



###########################################
#             Eclipse Import              #
###########################################

Rechtsklick -> Import -> Maven -> Existing Maven Project.



###########################################
#          �nderungen pr�fen              #
###########################################

git pull



###########################################
#          �nderungen hochladen           #
###########################################

"git add *"  (Bei warning: "git add --all")

"git commit -m "Ich bin eine Nachricht"

"git push" 



###########################################
#   Lokale �nderungen r�ckg�ngig machen   #
###########################################

"git checkout --"

"git fetch origin"

"git reset --hard origin/master"