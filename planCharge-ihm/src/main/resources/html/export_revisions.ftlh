<h1>Changements</h1>

<!--TODO FDA 2018/01 Préciser la date de dernière révision. -->
Cette partie liste les changements apportés au plan de charge de la CT entre la dernière révision et le ${dateEtatFormatee}.

<table border="1">
    <thead>
    <tr>
        <th colspan="4">Tâche</th>
        <th colspan="3">Changement / dernière planification</th>
    </tr>
    <tr>
        <!--Tâche :-->
        <th>N° tâche</th>
        <th>N° IDAL</th>
        <th>Tâche</th>
        <th>Projet / Appli</th>
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
            <!--Révision :-->
            <td><#if tacheBean.statutRevision??>${tacheBean.statutRevision.libelle!""}<#else></#if></td>
            <td><#if tacheBean.validateurRevision??>${tacheBean.validateurRevision.trigramme!""}<#else></#if></td>
            <td>${tacheBean.commentaireRevision!""}</td>
        </tr>
    </#list>
    </tbody>
</table>