# Base de données - Projet II
### Auteurs du projet
Groupe AZ
* IMESTIR Ibrahim 000524358
* OUDAHYA Ismaïl 000479390
* BELGADA Naoufal 000479191

## Features
Voici les différentes features intégrées au programme :
- Interface graphique
- Système de gestion de compte avec connexion et inscription
- Système de visualisation de la base de données simplifié
- Système de requêtes SQL simplifié
- Dashboard
- Carte du monde interactive
- Base de données

### Base de données
Nous utilisons PostgreSQL pour modéliser notre base de données relationnelle, il est donc nécessaire de disposer
d'une installation similaire sur la machine de test avec les même tables pour que le programme fonctionne
correctement.

Étant donné la nature de notre projet avec l'interface graphique simplifiée et la syntaxe particulière 
de PostgreSQL, nous ne pensons pas que notre interface fonctionnerait avec d'autres language SQL relationnelles.

Les informations de connexion à la base de données se trouvent dans la classe "DBManager.java"
se trouvant dans le package "database.access".

Voici la liste des tables de la BDD que nous avons utilisé :
- Countries
- Hospitals
- Producers
- Tests
- Climate
- User
- Epidemiologist

Les informations détaillées de ces tables sont fournies dans le rapport.

### Interface graphique
L'interface graphique permet de visualiser certaines statistiques sous forme de dashboard (voir section Dashboard), elle permet aussi la création de compte et la connexion

### Carte du monde
La carte du monde est une carte coloriant tous les pays selon leur nombre d'hospitalisations (selon le dernier rapport).
Elle se met à jour à chaque clique sur l'onglet "Carte/Map". 

Vous pouvez aussi cliquer sur un pays désiré pour obtenir 
les informations détaillées relatives à ce pays et au dernier rapport émis.

### Dashboard
Le dashboard permet de visualiser plusieurs statistiques contenues dans la base de données :
- Nombre de types de vaccins par pays (Camembert)
- Nombre de vaccinations par pays (Camembert)
- Nombre d'hospitalisations par pays selon le rapport le plus récent (Camembert)
- Evolution du nombre d'hospitalisations par pays (Graphique)
- Evolution du nombre de cas de soins intensifs par pays (Graphique)

## Compilation
### Prérequis
Le projet a été réalisé avec Java en utilisant le JDK 15 et Maven, il vous faut donc une version du JDK similaire et maven d'installé sur l'ordinateur
(ou utiliser l'IDE IntelliJ ayant un JDK15 d'installé).
Le programme ne pourra être compilé avec une version antérieure au JDK 15.

Maven peut être obtenu via le site officiel de maven : https://maven.apache.org/download.cgi \
La dernière version est compatible avec le JDK 15.

### Compilation & Lancement
La compilation du programme se fait grâce aux commandes ci-dessous (à exécuter dans l'ordre) :
* mvn compile
* mvn clean install

Vous pouvez maintenant lancer le programme avec la commande : 
* mvn exec:java

### Génération d'un fichier .jar
Vous pouvez générer un fichier executable .jar contenant toutes les dépendances pour but d'avoir un programme portable grace à la commande ci-dessous :
* mvn clean compile assembly:single

Le fichier .jar sera alors généré dans le dossier "target" et pourra être lancé comme tout autre exécutable java régulier :
* Soit en double cliquant dessus
* Soit en utilisant la commande : java -jar MonJar.jar

Veuillez bien faire attention à votre version de java car il faut la version du JDK 15 pour pouvoir démarrer l'application.

