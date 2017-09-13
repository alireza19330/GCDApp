package com.developairs.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.developairs.model.GCD;

@ApplicationScoped
public class GCDRepository {

    @Inject
    private EntityManager em;

    public GCD findById(Long id) {
        return em.find(GCD.class, id);
    }
    
    @SuppressWarnings("unchecked")
	public List<Integer> getAllGCDOrderbyDate() {
		Query createQuery = em.createQuery("SELECT g.value from GCD g ORDER BY g.addedDate");
		List<Integer> resultList = createQuery.getResultList();
		return resultList;
	}

	public long getSumOfAllGCD() {
		Query createQuery = em.createQuery("SELECT SUM(g.value) from GCD g");
		return (long) createQuery.getSingleResult();
	}
	
	public void save(GCD gcd){
		em.persist(gcd);
	}
}
