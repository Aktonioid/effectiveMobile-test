package com.effectiveMobile.client.infrastructure.repositories;

import java.util.List;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.effectiveMobile.client.core.models.UserPhone;
import com.effectiveMobile.client.core.repositories.IUserPhoneRepo;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Repository
public class UserPhoneRepo implements IUserPhoneRepo
{
    @Autowired
    SessionFactory sessionFactory;
    
    Logger logger = LoggerFactory.getLogger(UserPhoneRepo.class);

    
    @Override
    public List<UserPhone> getAllPhones() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserPhone> cq = cb.createQuery(UserPhone.class);
        Root<UserPhone> root = cq.from(UserPhone.class);

        cq.select(root);

        List<UserPhone> phones = session.createQuery(cq).getResultList();

        session.close();

        return phones;
    }

    @Override
    public List<UserPhone> getPhonesByUserId(UUID userId) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserPhone> cq = cb.createQuery(UserPhone.class);
        Root<UserPhone> root = cq.from(UserPhone.class);

        cq.select(root).where(root.get("user").in(userId));

        List<UserPhone> phones = session.createQuery(cq).getResultList();

        session.close();

        return phones;
    }

    @Override
    public boolean createUserPhone(UserPhone phone) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try
        {
            transaction.begin();
            session.persist(phone);
            transaction.commit();
        }
        catch(HibernateException e)
        {
            logger.error("Error while saving phone '"+phone.getPhoneNumber()+"'", e);
            transaction.rollback();
            session.close();
            return false;
        }
        catch(Exception e)
        {
            logger.error("Error while saving phone '"+phone.getPhoneNumber()+"'", e);
            
            transaction.rollback();
            session.close();
            throw e;
        }

        

        session.close();
        return true;
    }

    @Override
    public boolean delteUserPhone(UUID phoneId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaDelete<UserPhone> cd = cb.createCriteriaDelete(UserPhone.class);
        Root<UserPhone> root = cd.from(UserPhone.class);

        cd.where(root.get("id").in(phoneId));

        try
        {
            transaction.begin();
            session.createMutationQuery(cd).executeUpdate();
            transaction.commit();
        }
        catch(HibernateException e)
        {
            logger.error("Error while deleting phone with id" +phoneId, e);
            transaction.rollback();
            session.close();
            return false;
        }
        catch(Exception e)
        {
            logger.error("Error while deleting phone with id" +phoneId, e);
            transaction.rollback();
            session.close();
            throw e;
        }


        session.close();
        return true;
    }

    @Override
    public UserPhone getPhoneByPhone(String phone) 
    {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserPhone> cq = cb.createQuery(UserPhone.class);
        Root<UserPhone> root = cq.from(UserPhone.class);

        cq.select(root)
            .where(cb.like(root.get("phone"), phone));
        
        UserPhone phoneModel = session.createQuery(cq).uniqueResult();

        session.close();
        
        return phoneModel;
    }

    @Override
    public Long countPhonesByUserId(UUID userId) 
    {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> countQ = cb.createQuery(Long.class);
        Root<UserPhone> root = countQ.from(UserPhone.class);

        countQ.select(cb.count(root))
            .where(root.get("user").in(userId));

        Long result = session.createQuery(countQ).uniqueResult();

        session.close();

        return result;
    }
    
}
