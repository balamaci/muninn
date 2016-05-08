<#list events as eventGroupIt>
<h3>${eventGroupIt.key.executionTime}</h3>
<table>
    <#list eventGroupIt.eventsHolder.events as eventIt>
        <tr>
            <td>${eventIt}</td>
        </tr>
    </#list>
</table>
</#list>
