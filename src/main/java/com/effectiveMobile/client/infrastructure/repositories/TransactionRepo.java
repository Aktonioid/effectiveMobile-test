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

import com.effectiveMobile.client.core.models.UserModel;
import com.effectiveMobile.client.core.repositories.ITransactionRepo;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;

@Repository
public class TransactionRepo implements ITransactionRepo{

    @Autowired
    SessionFactory sessionFactory;

    Logger logger = LoggerFactory.getLogger(TransactionRepo.class);

    
    @Override
    public UserModel moneyTransfer(UUID senderId, UUID recipientId, double transferSum) {
        
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<UserModel> getUsersAfterSend = cb.createQuery(UserModel.class);

        CriteriaUpdate<UserModel> senderUpdate = cb.createCriteriaUpdate(UserModel.class);
        CriteriaUpdate<UserModel> recipientUpdate = cb.createCriteriaUpdate(UserModel.class);

        Root<UserModel> root = senderUpdate.from(UserModel.class);

        logger.info("User with id" + senderId +" send " + transferSum+ " to user with id "+recipientId);

        senderUpdate.set("balance", "balance-"+transferSum);
        senderUpdate.where(root.get("id").in(senderId));

        recipientUpdate.set("balance", "balance+"+transferSum);
        recipientUpdate.where(root.get("id").in(recipientId));

        getUsersAfterSend.select(root).where(root.get("id"));

        try{
            transaction.begin();
            session.createMutationQuery(senderUpdate).executeUpdate();
            session.createMutationQuery(recipientUpdate).executeUpdate();
            transaction.commit();
        }
        catch(HibernateException e){
            logger.error("Error while transaction", e);
            e.printStackTrace();
            transaction.rollback();
            session.close();
            return null;
        }
        catch(Exception e){
            logger.error("Error while transaction", e);
            transaction.rollback();
            session.close();
            throw e;
        }

        UserModel sender = session.createQuery(getUsersAfterSend).uniqueResult();

        session.close();

        return sender;
    }

    @Override
    public boolean isEnoughMoney(UUID senderId, double transferSum) {
        
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Double> cq = cb.createQuery(Double.class);
        Root<UserModel> root = cq.from(UserModel.class);

        cq.select(root.get("balance"))
            .where(root.get("id").in(senderId));

        // если не хватает денег на счету возвращаем false
        if(session.createQuery(cq).uniqueResult() < transferSum)
        {
            session.close();
            return false;
        }
        
        session.close();

        return true;
    }
}
