package com.test.dao;

import com.test.domain.Person;
import com.test.domain.Person_;
import com.test.domain.Pet;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class PersonDAO {

    @PersistenceContext(name="test")
    private EntityManager entityManager;


    public List<Person> readPersonByFirstNameUsingNativeQuery(String firstName) {

        Query q = entityManager.createNativeQuery("SELECT * FROM person WHERE firstname = :firstname", Person.class);
        q.setParameter("firstname", firstName);

        List<Person> result = q.getResultList();

        return result;

    }

    public List<Person> readPersonByFirstNameUsingHQL(String firstName) {

        Query q = entityManager.createQuery("from Person p where p.firstName = :firstName", Person.class);
        q.setParameter("firstName", firstName);

        List<Person> result = q.getResultList();

        return result;

    }

    public List<Person> readPersonByFirstNameUsingCriteria(String firstName) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        Root<Person> root = cq.from(Person.class);
//        ListJoin<Person, Pet> itemNode = root.join(Person_.pets);
        cq.where( cb.equal(root.get(Person_.firstName), firstName ) );

        return entityManager.createQuery(cq).getResultList();
    }

    @Transactional
    public void storePerson(Person person) {
        entityManager.persist(person);
    }

    public Person readPersonById(int i) {

        return entityManager.find(Person.class, i);

    }

}
