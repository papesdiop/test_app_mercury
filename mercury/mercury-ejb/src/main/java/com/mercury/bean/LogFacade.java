/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mercury.bean;

import com.mercury.entity.Log;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Pape S. Diop <papesdiop@gmail.com> papesdiop
 */
@Stateless
public class LogFacade extends AbstractFacade<Log> {
    @PersistenceContext(unitName = "com.mercury_mercury-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LogFacade() {
        super(Log.class);
    }

}
