# Base de données - Projet II
### Auteurs du projet
* IMESTIR Ibrahim 000524358
* OUDAHYA Ismaïl 000479390
* BELGADA Naoufal 000479191  
Groupe AZ

## Features
- Interface graphique
- Système de création de compte
- Système de connexion
- Dashboard
- Requêtes SQL

### Interface graphique
L'interface graphique permet de visualiser certaines statistiques sous forme de dashboard (voir section Dashboard), elle permet aussi la création de compte et la connexion

### Dashboard
Le dashboard permet de visualiser plusieurs statistiques contenues dans la base de données :
- Types de vaccins par pays (Camembert)
- Nombre de vaccinations par pays (Camembert)
- Nombre d'infectés par pays selon le rapport le plus récent (Camembert)
- Evolution du nombre d'hospitalisations par pays (Graphique)
- Evolution du nombre d'infectés par pays (Graphique)

## Compilation
### Prérequis
Le projet a été réalisé avec Java en utilisant le JDK 15 et Maven, il vous faut donc une version du JDK similaire et maven d'installé sur l'ordinateur
(ou utiliser l'IDE IntelliJ).
Le programme ne pourra être compilé avec une version antérieure au JDK 15.

Maven peut être obtenu via le site officiel de maven : https://maven.apache.org/download.cgi \
La dernière version est compatible avec le JDK 15.

### Compilation & Lancement
La compilation et du programme se fait grâce aux commandes ci-dessous (à exécuter dans l'ordre) :
* mvn compile
* mvn clean install

Vous pouvez maintenant lancer le programme avec la commande : 
* mvn exec:java

