# Examen Scala

Ce projet a pour objectif de produire un code source avec le langage `Scala` utilisant une API Wikipédia. Il est divisé en deux parties : 
- Écriture du code source qui utilisera l'API de Wikipédia
- Tests Unitaires du code produit.

Nous l'avons réalisé dans le cadre de l'examen du cours `Programmation Fonctionnelle avec Scala` suivi lors de notre année de Master 1 Architecture Logicielle à l'ESGI de Lyon.

Participants :
- Yannick Boyer
- Robin Peignet


## A) Code source
Création de l'algorithme du projet

#### Mise en place du projet (1,2,3)
...

#### Fonction formatUrl (3) : Yannick
...

#### Fonction getPages et librairie scalaj.http (4) : Robin
1 - La fonction getPages est une fonction qui prend en paramètre un URL au format `String`. \
2 - Elle exécute une requête http à partir de cette url grâce à librairie `scalaj.http`. \
3 - La méthode renvoie un type `Either` étant soit un `Int` (un code d'erreur le cas échéant), soit un `String` (le body de la page obtenue lors d'un succès).

#### Classe WikiPage et Fonction parseJSON (6) : Robin
1 - `WikiPage` est une case classe qui représente une page Wikipédia. Elle contient deux attributs : 
- `title` au format string pour le titre de la page
- `words` de type int qui contient le nombre de mots présent dans la page.

2 - `parseJson` est une fonction qui prend en paramètre une chaîne de caractère `string` représentant un fichier Json au format brut. \
Elle désérialise les pages du contenu Json en objet de type WikiPage et retourne une séquence d'objet WikiPage.

#### Statistique sur les pages (8) : Robin
1- La méthode `totalWords` est une fonction qui prends en paramètre une séquence de page Wikipédia au format objet `WikiPage`. \
2 - Elle réalise un cumul du nombre total de mots de tous les objets WikiPage de la séquence, en utilisant la méthode `foldLeft`. \
3- Elle renvoi le résultat de ce cumul au format `Int`.

## B) Tests Unitaires
Ajout des tests unitaires sur le projet.

#### Mise en place de scalatest (1) : Yannick
...

#### Test unitaire de la méthode formatUrl (2) : Robin
1 - Le test de la méthode `formatUrl` décrit qu'elle doit retourner un URL correctement formaté. \
2 - En donnée de départ, on utilise deux variables immuable :
- `keyword` qui a pour valeur "scala"
- `limit` qui a pour valeur 10

3 - On appelle ensuite la méthode `formatUrl` en lui passant les deux paramètres. \
4 - On vérifie enfin que le résultat correspond bien à l'URL Wikipédia avec nos valeurs correctement formatées.

#### Test unitaire pour la fonction getPages (6) : Robin
1 - Le test de la méthode `getPages` vérifie qu'un URL retourne un succès. \
2 - En données de départ, on utilise une URL valide au format `string` :
- http://example.com 

3 - On fait ensuite appel à la méthode getPage en passant notre url. \
Nous lui passons aussi un objet MockHttpUtils qui simulera le comportement de la librairie scalaj.http en renvoyant un résultat HttpRequest sans passé par Internet (pour avoir de bonnes conditions de test). \
4 - On vérifie ensuite si le résultat obtenu de la méthode est bien un type `Either Right` correspondant ainsi a un succès.
