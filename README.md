Développement d'une plateforme d'automatisation des demandes de propositions (RFP)
Vue d'ensemble du projet
Ce projet vise à développer une plateforme intuitive et performante permettant d'automatiser la gestion des demandes de propositions (RFP). La plateforme est composée d'un backend développé avec Spring Boot et d'un frontend réalisé avec Angular. Elle fournit des fonctionnalités complètes pour gérer les candidats, les contextes, les références, les technologies et bien plus encore.

Objectifs
Améliorer l'efficacité dans la gestion des RFP.
Simplifier les processus d'analyse et de traitement des documents.
Offrir une interface utilisateur conviviale et intuitive.
Garantir la sécurité et la flexibilité grâce à l'intégration avec Keycloak pour l'authentification.
Structure du projet
Backend
Framework : Spring Boot
Base de données : PostgreSQL
Authentification : Keycloak
Fonctionnalités principales :
CRUD pour les candidats, contextes, références, technologies, utilisateurs et rôles.
Analyse des documents RFP grâce à l'intégration d'IA.
API REST sécurisée pour les communications avec le frontend.
Frontend
Framework : Angular
Fonctionnalités principales :
Tableau de bord administrateur.
Interface utilisateur pour consulter les données et interagir avec les sections spécifiques (candidats, références, etc.).
Téléversement et visualisation des documents RFP.
Résultats d'analyse générés automatiquement.
Fonctionnalités principales
Dashboard Administrateur
Situé dans src/app/components/admin, il permet :

D'ajouter, de modifier, de supprimer et de consulter les entités.
De gérer les utilisateurs et leurs rôles.
Page d'Analyse RFP
Située dans src/app/views/rfp-analysis, elle permet :

Le téléversement des documents RFP.
L'affichage des résultats d'analyse générés par l'IA.
Modèles de données
Les modèles d'entités sont définis dans src/app/models et incluent :

Candidat : Nom, compétences, expérience, etc.
Référence : Détails sur les projets ou réalisations.
Technologie : Nom, version et configurations associées.
![Capture d'écran 2024-05-16 114906](https://github.com/user-attachments/assets/70429594-cdfa-420c-8140-ea5c9ebdb166)

![acceuil](https://github.com/user-attachments/assets/3aa6badc-5064-4c52-8e94-67285db240e8)

![capp1](https://github.com/user-attachments/assets/bb3320b7-bba6-44a0-b26c-67c178eaac45)

![proposal 2](https://github.com/user-attachments/assets/d0472690-64f6-4173-942a-37d45d71d45c)

![detailrolee](https://github.com/user-attachments/assets/7a3a1f3f-6624-4476-bac9-e755f470fb03)

![create role](https://github.com/user-attachments/assets/0da00a8c-5d1b-47b8-9376-182cfc6e48d9)

![deleterole](https://github.com/user-attachments/assets/399c7afa-2190-4f81-a4b1-7ed7bbb62a70)

![Capture d'écran 2024-05-20 110550](https://github.com/user-attachments/assets/a1c42900-ec01-4f86-8eaa-c6a9685ba39e)


