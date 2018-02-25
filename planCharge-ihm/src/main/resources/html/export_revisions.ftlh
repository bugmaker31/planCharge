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
    <#list tachesBeans as tacheBean>
        <tr>
            <!--Tâche :-->
            <td>${tacheBean.noTache()!""}</td>
            <td>${tacheBean.noTicketIdal!""}</td>
            <td>${tacheBean.description!""}</td>
            <td><#if tacheBean.projetAppli??>${tacheBean.projetAppli.code!""}<#else></#if></td>
            <!--Changement :-->
            <td><#if tacheBean.typeChangement??>${tacheBean.typeChangement.name()!""}<#else></#if></td>
            <!--Révision :-->
            <td><#if tacheBean.statutRevision??>${tacheBean.statutRevision.libelle!""}<#else></#if></td>
            <td><#if tacheBean.validateurRevision??>${tacheBean.validateurRevision.trigramme!""}<#else></#if></td>
            <td>${tacheBean.commentaireRevision!""}</td>
        </tr>
    </#list>
    </tbody>
</table>