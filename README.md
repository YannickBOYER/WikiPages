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

#### Mise en place du projet (1,2,3) : Yannick
1 - Ajout de `scalafmt` et des librairies `scalaj-http`, `play-json` et `scopt`. \
2 - Modification de la fonction `parseArguments` pour permettre au script de prendre en paramètre 1 argument optionnel et 1 argument obligatoire.

#### Fonction formatUrl (3) : Yannick
1 - La fonction `formatUrl` permetter de formater l'URL de l'API en fonction des paramètres reçus. \
- `keyword` est le mot clé recherché et `limit` le nombre de résultats maximum. \
- Via une String interpolation, cette fonction retourne l'URL construit à l'aide des variables reçues en argument.

#### Fonction getPages et librairie scalaj.http (4) : Robin
1 - La fonction getPages est une fonction qui prend en paramètre un URL au format `String`. \
2 - Elle exécute une requête http à partir de cette url grâce à librairie `scalaj.http`. \
3 - La méthode renvoie un type `Either` étant soit un `Int` (un code d'erreur le cas échéant), soit un `String` (le body de la page obtenue lors d'un succès).

#### Modification de la fonction run (5) : Yannick
1 - Modification de la fonction `run` pour qu'elle puisse utiliser les deux fonctions créées précédemment. \
2 - Traitement du cas left pour afficher un message d'erreur, et right pour afficher le corps de la réponse. \

#### Classe WikiPage et Fonction parseJSON (6) : Robin
1 - `WikiPage` est une case classe qui représente une page Wikipédia. Elle contient deux attributs : 
- `title` au format string pour le titre de la page
- `words` de type int qui contient le nombre de mots présent dans la page.

2 - `parseJson` est une fonction qui prend en paramètre une chaîne de caractère `string` représentant un fichier Json au format brut. \
Elle désérialise les pages du contenu Json en objet de type WikiPage et retourne une séquence d'objet WikiPage.

#### Nouvelle modification de la fonction run (7) : Yannick
1 - Modification de la fonction `run` pour qu'elle utilise la méthode `parseJson`. \
2 - Affichage du nombre de pages trouvées à l'aide de `pages.length`. \
3 - Affichage du titre de chaque page `page.title`, et du nombre de mots pour chaque page `page.words`. \

#### Statistique sur les pages (8) : Robin
1- La méthode `totalWords` est une fonction qui prends en paramètre une séquence de page Wikipédia au format objet `WikiPage`. \
2 - Elle réalise un cumul du nombre total de mots de tous les objets WikiPage de la séquence, en utilisant la méthode `foldLeft`. \
3- Elle renvoi le résultat de ce cumul au format `Int`.

## B) Tests Unitaires
Ajout des tests unitaires sur le projet.

#### Mise en place de scalatest (1) : Yannick
- Ajout de la librairie `scalatest` et création de la classe `MainSpec` pour les tests unitaires. \

#### Test unitaire de la méthode formatUrl (2) : Robin
1 - Le test de la méthode `formatUrl` décrit qu'elle doit retourner un URL correctement formaté. \
2 - En données de départ, on utilise deux variables immuable :
- `keyword` qui a pour valeur "scala"
- `limit` qui a pour valeur 10

3 - On appelle ensuite la méthode `formatUrl` en lui passant les deux paramètres. \
4 - On vérifie enfin que le résultat correspond bien à l'URL Wikipédia avec nos valeurs correctement formatées.

#### Test unitaire de la méthode parseJson (3) : Yannick

1 - Le test de la méthode `parseJson` indique qu'elle doit retourner une liste de WikiPage. \
2 - En donnée d'entrée, on utilise une variable `json` contenant un exemple de json à traiter. \
3 - On appelle la méthode `parseJson` en lui passant le json en paramètre. \
4 - On vérifie enfin que le résultat correspond bien à la liste de WikiPage attendues.

#### Tests unitaires de la fonction totalWords (4) : Yannick

1 - Il y a 2 tests de la méthode `totalWords`: \
- Un avec une liste de `pages` non vide en entrée
- Un autre avec une liste vide en entrée
3 - On appelle la méthode `totalWorlds` en lui passant la liste en paramètre. \
4 - On vérifie enfin que le résultat correspond bien au résultat attendu:
- 0 pour la liste vide, le nombre de mots spécifié dans le test pour la liste remplie.

#### Tests unitaires de la méthode parseArguments (5) : Yannick

1 - Il y a 3 tests pour la méthode `parseArguments`. La variable d'entrée `args` contient dans chacun des tests : \
- Un argument non parsable
- Un argument mot-clé
- Un argument mot-clé et une limite
3 - On appelle la méthode `parseArguments` en lui passant les arguments en paramètre. \
4 - On vérifie enfin que les résultats correspondent bien aux résultats attendus:
- Un résultat `None` si l'argument n'est pas parsable
- Un objet `Config` avec le mot-clé dans le deuxième cas
- Un objet `Config` avec un mot-clé et une limite pour le dernier test

#### Test unitaire pour la fonction getPages (6) : Robin
1 - Le test de la méthode `getPages` vérifie qu'un URL retourne un succès. \
2 - En données de départ, on utilise une URL valide au format `string` :
- http://example.com 

3 - On fait ensuite appel à la méthode getPage en passant notre url. \
Nous lui passons aussi un objet MockHttpUtils qui simulera le comportement de la librairie scalaj.http en renvoyant un résultat HttpRequest sans passé par Internet (pour avoir de bonnes conditions de test). \
4 - On vérifie ensuite si le résultat obtenu de la méthode est bien un type `Either Right` correspondant ainsi a un succès.
