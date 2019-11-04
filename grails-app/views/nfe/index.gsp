<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'nfe.label', default: 'Nfe')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#list-nfe" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
                <li><g:link class="create" action="searchNfes"><g:message code="nfe.searchNfes.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="list-nfe" class="content scaffold-list" role="main">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <table id="tabela">
              <thead>
                <tr>
                    <th class="sortable">Chave de Acesso</a></th>
                    <th class="sortable">Valor Total da NFe</th>
                </tr>
              </thead>
              <tbody>
                <g:each status="i" in="${nfeList}" var="item">
                <tr class="${ (i % 2) == 0 ? 'even' : 'odd'}">
                  <td><a href="/epnota/nfe/show/${item?.id}">${item.accessKey?.encodeAsHTML()}</a></td>
                  <td><g:formatNumber number='${item.totalInvoice}' type="currency"  maxFractionDigits="2" currencyCode="BRL"/></td>
                </tr>
              </g:each>
            </tbody>
            </table>
            <!--f:table collection="${nfeList}" /-->

            <div class="pagination">
                <g:paginate total="${nfeCount ?: 0}" />
            </div>
        </div>
    </body>
</html>