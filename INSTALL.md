Ce fichier liste les actions à faire avant d'installer la version v1.0.

# Migration des données

## Migration du fichier Calc du suivi des demandes (`DAL-CT_14_PIL_Suivi des demandes_*.ods`)
**NB** : A faire avant de migrer le fichier Calc du plan de charge.
1) Ajouter le projet `SIPV`.
1) Ajouter le profil `Encadrant`.
1) Affecter le profil `Encadrant` aux tâches `T0765`  et `T0952` (au lieu du profil `*`).
1) Mettre "(pas de ticket)" dans la colonne `N° de ticket IDAL` pour toutes les tâches dont cette colonne est vide.

_(fait en t4.37)_

## Migration du fichier Calc du plan de charge (`DAL-CT_11_PIL_Plan de charge_*.ods`)
1) Ajouter les statuts des tâches dans l'onglet "Param" (copier/coller depuis "Suivi des demandes").
1) Màj la liste des projets : ajouter `SOCTEC_V2`, `EQUAR`, `BDNI-Mig`, `SIPV`, etc. (copier/coller depuis "Suivi des demandes").
1) Ajouter le profil `Encadrant` dans l'onglet `Param`.
1) Affecter le profil `Encadrant` aux tâches `T0765`  et `T0952` (au lieu du profil `*`).
1) Ajouter le profil `Encadrant` à l'onglet `Charge`, en copiant/collant les formules des colonnes hebdo/trimestrielles (`L` à `AC`) :
    1) dans la section `Charge / profil (j)` (ligne 208)
    1) dans la section `Dispo. maxi. / profil (j)` (ligne 239)
    1) dans la section `Dispo. maxi. restante CT / profil (j)` (ligne 271)
1) Associer les ressources `FDA` et `CJE` au profil `Encadrant`, dans l'onglet `Dispo`. 
1) La tâche `T1618` "Comité Technique DAL (COTECDAL)" doit être mise au Statut `Récurrent`.
<br/>_(fait en t3.35)_
1) Passer au statut `Récurrent` les tâches de type provision mais qui sont (par erreur/oubli) en statut "Nouveau" : COTECDAL, Màj du suivi des contrats de dév/études, Intégration des RT aux plateaux Agiles, etc.


