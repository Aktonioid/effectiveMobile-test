package com.effectiveMobile.client.infrastructure.repositories;

import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.effectiveMobile.client.core.models.RefreshTokenModel;
import com.effectiveMobile.client.core.repositories.IRefreshTokenRepo;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Root;

@Repository
public class RefreshTokenRepo implements IRefreshTokenRepo
{

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public RefreshTokenModel GetTokenById(UUID tokenId) 
    {
        Session session = sessionFactory.openSession();

        RefreshTokenModel token = session.get(RefreshTokenModel.class, tokenId);

        session.close();
        return token;
    }

    @Override
    public boolean CreateToken(RefreshTokenModel token) 
    {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try
        {
            transaction.begin();

            session.persist(token);
            
            transaction.commit();
        }
        catch(HibernateException e)
        {
            transaction.rollback();
            session.close();
            return false;
        }
        catch(Exception e)
        {
            transaction.rollback();
            session.close();
            throw e;
        }

        session.close();

        return true;
    }

    @Override
    public boolean DeleteTokenById(UUID tokenId) 
    {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaDelete<RefreshTokenModel> cd = cb.createCriteriaDelete(RefreshTokenModel.class);
        Root<RefreshTokenModel> root = cd.from(RefreshTokenModel.class);

        cd.where(root.get("id").in(tokenId));

        try
        {
            transaction.begin();
            session.createMutationQuery(cd).executeUpdate();
            transaction.commit();
        }
        catch(HibernateException e)
        {
            transaction.rollback();
            session.close();
            return false;
        }
        catch(Exception e)
        {
            transaction.rollback();
            session.close();
            throw e;
        }

        session.close();

        return true;
    }

}
