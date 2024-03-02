package com.effectiveMobile.client.infrastructure.repositories;

import java.util.List;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.MutationQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.effectiveMobile.client.core.models.UserEmail;
import com.effectiveMobile.client.core.repositories.IUserEmailRepo;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Repository
public class UserEmailRepo implements IUserEmailRepo{

    @Autowired
    SessionFactory sessionFactory;
    
    Logger logger = LoggerFactory.getLogger(UserEmailRepo.class);

    @Override
    public List<UserEmail> getAllEmais() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserEmail> cq  = cb.createQuery(UserEmail.class);
        Root<UserEmail> root = cq.from(UserEmail.class);

        cq.select(root);
        List<UserEmail> emails = session.createQuery(cq).getResultList();
        session.close();

        return emails;
    }

    @Override
    public List<UserEmail> getEmailsByUser(UUID userId) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserEmail> cq  = cb.createQuery(UserEmail.class);
        Root<UserEmail> root = cq.from(UserEmail.class);

        cq.select(root).where(root.get("userModel").in(userId));

        List<UserEmail> emails = session.createQuery(cq).getResultList();
        session.close();
        
        return emails;
    }

    @Override
    public boolean createEmail(UserEmail email) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try
        {
            transaction.begin();
            session.persist(email);
            transaction.commit();
        }
        catch(HibernateException e)
        {
            logger.error("email saving error", e);
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
        catch(Exception e)
        {
            logger.error("email saving error", e);
            transaction.rollback();
            session.close();
            throw e;
        }

        session.close();
        return true;
    }

    @Override
    public boolean deleteEmail(UUID id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaDelete<UserEmail> cd = cb.createCriteriaDelete(UserEmail.class);
        Root<UserEmail> root = cd.from(UserEmail.class);

        cd.where(root.get("id").in(id));
        MutationQuery delete = session.createMutationQuery(cd);

        try
        {
            transaction.begin();
            delete.executeUpdate();
            transaction.commit();
        }
        catch(HibernateException e)
        {
            logger.error("email deleting error", e);
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return false;
        }
        catch(Exception e)
        {
            logger.error("email deleting error", e);
            transaction.rollback();
            session.close();
            throw e;
        }

        session.close();
        return true;
    }

    @Override
    public UserEmail getEmailByEmail(String email) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserEmail> cq = cb.createQuery(UserEmail.class);
        Root<UserEmail> root = cq.from(UserEmail.class);

        cq.select(root)
            .where(cb.like(root.get("email"), email));
        
        UserEmail phoneModel = session.createQuery(cq).uniqueResult();

        session.close();
        
        return phoneModel;
    }

    @Override
    public Long coutnEmailsByUserId(UUID userId) {

        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> countQ = cb.createQuery(Long.class);
        Root<UserEmail> root = countQ.from(UserEmail.class);

        countQ.select(cb.count(root))
            .where(root.get("userModel").in(userId));

        Long result = session.createQuery(countQ).uniqueResult();

        session.close();

        return result;
    }
    
}
