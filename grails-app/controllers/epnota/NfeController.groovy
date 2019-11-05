package epnota

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*
import grails.plugins.rest.client.RestBuilder
import groovy.util.XmlSlurper
import grails.converters.JSON

class NfeController {

    NfeService nfeService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond nfeService.list(params), model:[nfeCount: nfeService.count()]
    }

    def show(Long id) {
        respond nfeService.get(id)
    }

    def searchAccessKey(String id) {
        respond Nfe.findByAccessKey(id)
    }

    def searchVnf(String id) {
        def ret = Nfe.findByAccessKey(id).totalInvoice
        respond "Valor Total da NFe":ret
    }

    def cleanAllNfes() {
        try {
            Nfe.executeUpdate('delete from Nfe')
            flash.message = message(code: 'default.okCleanAll.message', args: [message(code: 'nfe.label', default: 'Nfe')])
        } catch (ValidationException e) {
            println(e.message)
            flash.message = message(code: 'default.errorCleanAll.message', args: [message(code: 'nfe.label', default: 'Nfe'), e.message])
            return
        }
        redirect action: "index", method: "GET"
    }

    def searchNfes() {

        def resp
        def cont = true
        def url = 'https://sandbox-api.arquivei.com.br/v1/nfe/received'
        def nfe
        def i = 0;

        while(cont)
        {
            try {
                resp = new RestBuilder().get(url){
                    header 'content-type', 'application/json'
                    header 'x-api-id', 'f96ae22f7c5d74fa4d78e764563d52811570588e'
                    header 'x-api-key', 'cc79ee9464257c9e1901703e04ac9f86b0f387c2'
                }
            } catch (ValidationException e) {
                cont = false
                println(e.message)
                return
            }

            if(resp.status == 200) {

                if(resp.json.count.toInteger() > 0)
                    cont = true
                else 
                    cont = false

                url = resp.json.page.next
                
                if(resp.json.data)
                {
                    resp.json.data.each { nfeu ->

                        nfe = Nfe.findByAccessKey(nfeu.access_key)
                        
                        println(nfeu.access_key)

                        if(!nfe) {
                            i++
                            nfe = new Nfe()
                            nfe.accessKey = nfeu.access_key
                            
                            def dataXml = nfeu.xml
                            byte[] decoded = dataXml.decodeBase64()
                            def xmlString = new String(decoded)
                            def response = new XmlSlurper().parseText(xmlString) 
                            def vnf
                            
                            if(dataXml.substring(0,3) == 'PG5')
                            {
                                vnf = response.NFe.infNFe.total.ICMSTot.vNF.text()
                            }
                            else if(dataXml.substring(0,3) == 'PE5')
                            {
                                vnf = response.infNFe.total.ICMSTot.vNF.text()
                            }
                            else
                                vnf = 0

                            println(vnf)
                            
                            nfe.totalInvoice = vnf as BigDecimal

                            nfe.save(flush:true)
                        }
                    }
                }
                flash.message = message(code: 'default.okApi.message', args: [message(code: 'nfe.label', default: 'Nfe'), resp.status, i])
            }
            else {
                cont = false
                flash.message = message(code: 'default.errorApi.message', args: [message(code: 'nfe.label', default: 'Nfe'), resp.status, i])
            }
        }

        redirect action: "index", method: "GET"
    }

    def create() {
        respond new Nfe(params)
    }

    def save(Nfe nfe) {
        if (nfe == null) {
            notFound()
            return
        }

        try {
            nfeService.save(nfe)
        } catch (ValidationException e) {
            respond nfe.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'nfe.label', default: 'Nfe'), nfe.id])
                redirect nfe
            }
            '*' { respond nfe, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond nfeService.get(id)
    }

    def update(Nfe nfe) {
        if (nfe == null) {
            notFound()
            return
        }

        try {
            nfeService.save(nfe)
        } catch (ValidationException e) {
            respond nfe.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'nfe.label', default: 'Nfe'), nfe.id])
                redirect nfe
            }
            '*'{ respond nfe, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        nfeService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'nfe.label', default: 'Nfe'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'nfe.label', default: 'Nfe'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
