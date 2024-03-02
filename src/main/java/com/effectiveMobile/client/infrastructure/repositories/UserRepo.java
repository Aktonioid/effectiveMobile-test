package com.effectiveMobile.client.infrastructure.repositories;

import java.util.Date;
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

import com.effectiveMobile.client.core.models.UserEmail;
import com.effectiveMobile.client.core.models.UserModel;
import com.effectiveMobile.client.core.models.UserPhone;
import com.effectiveMobile.client.core.repositories.IUserRepo;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;

@Repository
public class UserRepo implements IUserRepo
{
    @Autowired
    SessionFactory sessionFactory;
    Logger logger = LoggerFactory.getLogger(UserRepo.class);

    @Override
    public List<UserModel> getAllUsers() {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> cq = cb.createQuery(UserModel.class);
        Root<UserModel> root = cq.from(UserModel.class);

        cq.select(root);

        List<UserModel> users = session.createQuery(cq).getResultList();

        session.close();
        return users;
    }

    @Override
    public boolean CreateUser(UserModel model) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        logger.info("savig userModel");

        try
        {
            transaction.begin();
            session.persist(model);
            session.persist(model.getPhones());
            session.persist(model.getEmails());
            transaction.commit();
        }
        catch(HibernateException e)
        {
            logger.error("Error while saving ", e);
            transaction.rollback();
            session.close();
            return false;
        }
        catch(Exception e)
        {
            logger.error("Error while saving ", e);
            transaction.rollback();
            session.close();
            throw e;
        }

        session.close();
        return true;
    }

    @Override
    public boolean updateUser(UserModel model) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();

        try
        {
            transaction.begin();
            session.merge(model);
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
    public boolean balanceIncreaceByMinute() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.getTransaction();
        
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaUpdate<UserModel> cu = cb.createCriteriaUpdate(UserModel.class);
        Root<UserModel> root = cu.from(UserModel.class);

        // умножаем баланс на 1.0
        cu.set("balance", "balance*1.05");

        // обновляем только если баланс меньше, чем изначальный вклад * 2.07
        cu.where( 
            cb.lessThan(
                root.get("balance"), 
                cb.prod(root.get("basicDeposit"), 2.07) // умножение базового вклада на 2.07
                ));

        try
        {
            transaction.begin();
            session.createMutationQuery(cu).executeUpdate();
            session.flush();
            transaction.commit();
        }
        catch(HibernateException e){
            logger.error("Error while icreasing balance", e);
            transaction.rollback();
            session.close();
            return false;
        }
        catch(Exception e){
            logger.error("Error while icreasing balance", e);
            transaction.rollback();
            session.close();
            throw e;
        }

        session.close();
        return true;
    }

    @Override
    public UserModel getUserById(UUID userId) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> cq = cb.createQuery(UserModel.class);
        Root<UserModel> root = cq.from(UserModel.class);

        cq.select(root).where(root.get("id").in(userId));

        UserModel model = session.createQuery(cq).uniqueResult();

        session.close();
        
        return model;
    }

    // потск по пользователю
    @Override
    public List<UserModel> getUserByName(String fio) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> cq = cb.createQuery(UserModel.class);
        Root<UserModel> root = cq.from(UserModel.class);

        cq.select(root).where(cb.like(root.get("username"), fio+"%"));

        List<UserModel> users = session.createQuery(cq).getResultList();

        session.close();
        return users;
    }

    // поиск по телефону
    @Override
    public UserModel getUserByPhone(UserPhone phone) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> cq = cb.createQuery(UserModel.class);
        Root<UserModel> root = cq.from(UserModel.class);

        cq.select(root)
            .where(cb.isMember(phone, root.get("phones")));

        UserModel userModel = session.createQuery(cq).uniqueResult();

        session.close();

        return userModel;
    }

    // поиск по почте
    @Override
    public UserModel getUserByEmail(UserEmail email) {
        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> cq = cb.createQuery(UserModel.class);
        Root<UserModel> root = cq.from(UserModel.class);

        cq.select(root)
            .where(root.get("emails").in(email));

        UserModel model = session.createQuery(cq).uniqueResult();

        session.close();

        return model;
    }

    @Override
    public List<UserModel> getUsersByBirthDate(Date birthDate) {

        Session session = sessionFactory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserModel> cq = cb.createQuery(UserModel.class);
        Root<UserModel> root = cq.from(UserModel.class);


        cq.select(root)
        .where(cb.gt(root.get("bitrhDate"), birthDate.getTime()));

        List<UserModel> users = session.createQuery(cq).getResultList();

        session.close();

        return users;
    }


    
}
