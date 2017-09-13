package com.developairs.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.developairs.model.Message;

@ApplicationScoped
public class MessageRepository {

    @Inject
    private EntityManager em;

    public Message findById(Long id) {
        return em.find(Message.class, id);
    }

    public void save(Message m){
    	em.persist(m);
    }

	@SuppressWarnings("unchecked")
	public List<Integer> findAllNumbersOrderByDate() {
		Query createQuery = em.createQuery("SELECT m.number from Message m ORDER BY m.addedDate");
		List<Integer> resultList = createQuery.getResultList();
		return resultList;
	}
}
