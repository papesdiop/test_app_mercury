/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mercury.bean;

import com.mercury.entity.Word;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Pape S. Diop <papesdiop@gmail.com> papesdiop
 */
@Stateless
public class WordFacade extends AbstractFacade<Word> {
    @PersistenceContext(unitName = "com.mercury_mercury-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WordFacade() {
        super(Word.class);
    }

}
