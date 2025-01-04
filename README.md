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
