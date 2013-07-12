/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mercury.bean;

import com.mercury.entity.Log;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Pape S. Diop <papesdiop@gmail.com> papesdiop
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LogBean extends AbstractFacade<Log> {
    @PersistenceContext//(unitName = "com.mercury_mercury-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    //@Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void create(Log entity) {
        super.create(entity);
    }
    
    

    public LogBean() {
        super(Log.class);
    }

}
