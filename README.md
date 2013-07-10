Mercury est une application de test des technologies qui vont intervenir dans un projet.

C'est une application de Front-end qui devra etre atteinte par un web service (Jax-WS ou Jax-RS), 
cette application permettra d'envoyer un mot qui devra etre enregistré dans une base de données. 
A chaque enregistrement reussi, un email devra etre envoye, sinon un message doit etre inscrit dans un fichier log.
Et pour le tracking, la transaction doit enregistree dans une table de la base données, qui va inclure  l'adresse IP du client 
qui a envoye le message ainsi que les details de la tansaction.

Les technologies et outils utilisées sont: 
- Les web services avec Jax-Rs spec, l'implémentation de Jersey, 
- Les EJB3, SessionBean et Message Driven Bean,
- Interceptors et Listeners avec les EJB3
- Maven comme outil de gestion des dépendances, de build, de test, etc...
- projet Arquillian pour les tests unitaires
- le module LogBack pour la gestion des logs et le tracking

L'application est configuré en maven-module (ejb, war), on pouvait aussi utiliser ear ou en une application web.

-------------- Configuration --------------------------------------

Cette application est preconfiguree avec la base de données Mysql 5, le serveur mail de Gmail, l'application server glassfish3-embedded dans arquillian.
    
   - Donc on peut executer le script mercury.sql pour la création des tables de l'application et de celles de LogBack pour le logging
   oubien
   - executer le script https://github.com/qos-ch/logback/blob/master/logback-classic/src/main/java/ch/qos/logback/classic/db/script/mysql.sql 
    pour creer les tables pour logBack et decommenter dans les fichiers persitence-test.xml la propriété eclipselink.ddl-generation
   
    Pour configurer les log dans un fichier donné, aller dans test/resources/logback-test.xml, et au niveau APPENDER FILE, mettez le lien du fichier
    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
       <file>CHEMIN_DU_REPERTOIRE_DU_FICHIER_LOG/Mercury-test-${logByDay}.log</file>
   Le fichier log des erreurs est configuré fichier/jour

Aller à la racine du projet, cd mercury
    puis Taper la commande maven: mvn test (qui executera le test avec le profile par défaut glassfish-embedded, ça prendra quelques secondes pour le téléchargement des dépendances)

