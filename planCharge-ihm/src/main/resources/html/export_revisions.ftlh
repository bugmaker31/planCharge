<table border="1">
    <thead>
    <tr>
        <th colspan="4">Tâche</th>
        <th colspan="4">Changement entre la révision du ${dateEtatFormatee} et la précédente</th> <!--TODO FDA 2018/01 Préciser la date de dernière révision. -->
    </tr>
    <tr>
        <!--Tâche :-->
        <th>N° tâche</th>
        <th>N° IDAL</th>
        <th>Tâche</th>
        <th>Projet / Appli</th>
        <!--Changement :-->
        <th>Changement</th>
        <!--Révision :-->
        <th>Statut</th>
        <th>Validé ?</th>
        <th>Commentaire</th>
    </tr>
    </thead>
    <tbody>
    <#list tachesBeans as valeurApres>
        <tr>
            <!--Tâche :-->
            <td>${valeurApres.noTache()!""}</td>
            <td>${valeurApres.noTicketIdal!""}</td>
            <td>${valeurApres.description!""}</td>
            <td><#if valeurApres.projetAppli??>${valeurApres.projetAppli.code!""}<#else></#if></td>
            <!--Changement :-->
            <td><#if valeurApres.typeChangement??>${valeurApres.typeChangement.name()!""}<#else></#if></td>
            <!--Révision :-->
            <td><#if valeurApres.statutRevision??>${valeurApres.statutRevision.libelle!""}<#else></#if></td>
            <td><#if valeurApres.validateurRevision??>${valeurApres.validateurRevision.trigramme!""}<#else></#if></td>
            <td>${valeurApres.commentaireRevision!""}</td>
        </tr>
    </#list>
    </tbody>
</table>