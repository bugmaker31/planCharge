Ce fichier liste les modif à faire sur les données, pour les rendre compatibles avec le code.

# Migration (upgrade) en 1.0-m3

## Migration des données

### Migration du fichier Calc du plan de charge
**NB** : A faire avant de migrer le fichier Calc du plan de charge.
1) Ajouter le projet `SIPV`.
1) Ajouter le profil "Encadrant".
1) Affecter le profil "Encadrant" aux 2 tâches ayant le profil "*".

### Migration du fichier Calc du plan de charge
1) Ajouter les statuts des tâches dans l'onglet "Param" (copier/coller depuis "Suivi des demandes").
1) Màj la liste des projets : ajouter `SOCTEC_V2`, `EQUAR`, `BDNI-Mig`, `SIPV`, etc. (copier/coller depuis "Suivi des demandes").
1) Ajouter le profil "Encadrant".
1) Affecter le profil "Encadrant" aux 2 tâches ayant le profil "*".
1) Ajouter le profil "Encadrant" à l'onglet "Charge", en copiant/collant les formules des colonnes hebdo/trimestrielles (`L` à `AC`) :
    1) dans la section "Charge / profil (j)" (ligne 208)
    1) dans la section "Dispo. maxi. / profil (j)" (ligne 239)
    1) dans la section "Dispo. maxi. restante CT / profil (j)" (ligne 271)
1) La tâche T1618 "Comité Technique DAL (COTECDAL)" doit être mise au Statut "Récurrent".
    

