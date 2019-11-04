package epnota

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class NfeServiceSpec extends Specification {

    NfeService nfeService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Nfe(...).save(flush: true, failOnError: true)
        //new Nfe(...).save(flush: true, failOnError: true)
        //Nfe nfe = new Nfe(...).save(flush: true, failOnError: true)
        //new Nfe(...).save(flush: true, failOnError: true)
        //new Nfe(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //nfe.id
    }

    void "test get"() {
        setupData()

        expect:
        nfeService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Nfe> nfeList = nfeService.list(max: 2, offset: 2)

        then:
        nfeList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        nfeService.count() == 5
    }

    void "test delete"() {
        Long nfeId = setupData()

        expect:
        nfeService.count() == 5

        when:
        nfeService.delete(nfeId)
        sessionFactory.currentSession.flush()

        then:
        nfeService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Nfe nfe = new Nfe()
        nfeService.save(nfe)

        then:
        nfe.id != null
    }
}
