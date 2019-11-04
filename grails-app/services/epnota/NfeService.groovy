package epnota

import grails.gorm.services.Service

@Service(Nfe)
interface NfeService {

    Nfe get(Serializable id)

    List<Nfe> list(Map args)

    Long count()

    void delete(Serializable id)

    Nfe save(Nfe nfe)

}