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
- Evolution du nombre d'infectés par pays (Graphique)

## Compilation
### Prérequis
Le projet a été réalisé avec Java en utilisant le JDK 15 et Maven, il vous faut donc une version du JDK similaire et maven d'installé sur l'ordinateur
(ou utiliser l'IDE IntelliJ).

Maven peut s'installer de diverses façons :
* avec la commande "sudo apt-get install maven" (uniquement sur Linux)
* sur le site officiel de maven : https://maven.apache.org/download.cgi

### Compilation & Lancement
La compilation et du programme se fait grâce aux commandes ci-dessous (à exécuter dans l'ordre) :
* mvn compile
* mvn clean install

Vous pouvez maintenant lancer le programme avec la commande : 
* mvn exec:java

