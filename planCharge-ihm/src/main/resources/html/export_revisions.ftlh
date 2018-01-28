<!--TODO FDA 2018/01 Préciser la date de dernière révision. -->
<h1>Changements dans le plan de charge entre la dernière révision et le ${dateEtatFormatee}</h1>

<table border="1">
    <thead>
    <tr>
        <th colspan="4">&nbsp;</th>
        <th colspan="3">Changement / dernière planification</th>
    </tr>
    <tr>
        <th>N° tâche</th>
        <th>N° IDAL</th>
        <th>Tâche</th>
        <th>Projet / Appli</th>
        <th>Statut</th>
        <th>Validé ?</th>
        <th>Commentaire</th>
    </tr>
    </thead>
    <tbody>
    <#list planificationsBeans as planifBean>
        <tr>
            <td>${planifBean.noTache()!""}</td>
            <td>${planifBean.noTicketIdal!""}</td>
            <td>${planifBean.description!""}</td>
            <td><#if planifBean.projetAppli??>${planifBean.projetAppli.code!""}<#else>N/C</#if></td>
            <td><#if planifBean.statutRevision??>${planifBean.statutRevision.code!""}<#else></#if></td>
            <td><#if planifBean.validateurRevision??>${planifBean.validateurRevision.trigramme!""}<#else></#if></td>
            <td>${planifBean.commentaireRevision!""}</td>
        </tr>
    </#list>
    </tbody>
</table>