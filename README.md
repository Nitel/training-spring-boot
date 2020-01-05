# Projet micro commerce (spring-boot)

## Structure

Le projet se découpe en trois dossiers principaux :

- src : fichiers sources de l'API (spring-boot)
- web : les sources de la partie web (JavaScript)
- TestsPostman : les end-to-end tests de Postman

## Installation

Partie web (OPTIONNEL) :

- Placez vous dans le dossier `web`
- Installer les modules nodes : `npm install`
- Compiler : `npm run build`

La dernière action aura pour effet de compiler les fichiers web 
dans le dossier `public` à la racine du projet.

Partie API :

- Cloner le repo dans un dossier
- Compiler les sources : `mvn compile`
- Démarrer l'API : `mvn spring-boot:run`

## Développement (partie web)

Pour faire des modifications partie web
il faut lancer le serveur de avec la commande `npm run dev` dans le dossier `web`.  

## Auteurs 

Maxime MOINEAU, Valentin NAHIM, Robin PRETTE
