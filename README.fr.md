#  <img title="WorkFlow" alt="Logo" src="https://i.imgur.com/KQGAJb0.png" width=50>  WorkFlow — Une application de gestion des ressources humaines développé en JAVA

[![en](https://img.shields.io/badge/lang-en-red.svg)](https://github.com/NotSoHealthy/WorkFlow-Java/blob/master/README.md)

## Table des matières

- [Introduction](#introduction)
- [Fonctionnalités](#fonctionnalités)
- [Exigences](#exigences)
- [Installation](#installation)
- [Utilisation](#utilisation)
- [Contributions](#contributions)
- [License](#license)

## Introduction

WorkFlow est un système de gestion d'équipe moderne, léger et flexible, conçu pour aider les équipes à rationaliser leurs opérations, suivre leur progression et améliorer leur productivité.
Ce projet a été développé dans le cadre du cours PIDEV 3A à [ESPRIT École d'ingénieurs](https://esprit.tn/)

### Conçu avec

* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
* ![JavaFX](https://img.shields.io/badge/javafx-%23FF0000.svg?style=for-the-badge&logo=javafx&logoColor=white)

## Fonctionnalités

- Contrôle d'accès basé sur les rôles avec Employés et Managers.
- Organisez et gérez les utilisateurs à travers différents départements.
- Assignez des tâches aux employés, surveillez leur avancement et suivez leur achèvement en temps réel.
- Les managers peuvent créer et gérer des projets et des tâches au sein de leurs départements.
- Les employés peuvent s'inscrire à des sessions de formation de l'entreprise.
- Les employés peuvent s'inscrire à des événements de l'entreprise et recevoir les détails des événements.
- Les employés peuvent déposer des plaintes directement à la direction.
- Les employés peuvent faire des demandes de congé via un système simplifié.
- Les managers peuvent approuver ou refuser les demandes de congé et superviser les activités de l'équipe.
- Les managers peuvent publier des offres d'emploi et des stages accessibles au public.
- Les utilisateurs invités peuvent postuler à des offres d'emploi et recevoir les détails des entretiens sans avoir besoin d'un compte.

## Exigences

- Java JDK 21+

## Installation

1. Cloner le dépôt
    
    ```sh
    git clone https://github.com/NotSoHealthy/WorkFlow-Java.git
    cd WorkFlow-Java
    ```
2. Installer Java

  * Installer [Java](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)

3. Installer les paquets

  * Dans le répertoire du projet
    ```sh
    mvn install
    ```

## Utilisation
* Dans le répertoire du projet
  ```sh
  mvn clean package
  ```

## Contributions

Nous remercions tous ceux qui ont contribué à ce projet !

### Contributeurs

Les personnes suivantes ont contribué à ce projet en ajoutant des fonctionnalités, en corrigeant des bugs ou en améliorant la documentation :

- [Mohamed Amin Ben Hamouda](https://github.com/NotSoHealthy) - Développement du module de gestion des utilisateurs 
- [Fares Guermazi](https://github.com/FaresGuer) - Ajout du module de gestion des formations
- [Youssef Mlewhia](https://github.com/yssfmlha) - Mise en place du module d'événements 
- [Amine Kerfai](https://github.com/AmineKerfai) - Développement du module des offres d'emploi et de stages
- [Zakaria Bouchaddakh](https://github.com/zakariabouchaddakh) - Création du module de gestion des départements et des projets
- [Mahdi Hattab](https://github.com/MehdiHattab) - Ajout du module de gestion des réclamations des employés

N'hésitez pas à contribuer en [soumettant un ticket](https://github.com/NotSoHealthy/WorkFlow-Java/issues). Chaque contribution nous aide à évoluer et à nous améliorer.

Nous vous remercions pour votre soutien et avons hâte d’améliorer notre produit grâce à votre aide !

## Licence

Distribué sous la licence MIT. Voir `LICENSE.txt` pour plus d'informations.