/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercury.rest;

import com.mercury.bean.WordFacade;
import com.mercury.entity.Word;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pape S. Diop <papesdiop@gmail.com> papesdiop
 */
@Stateless
@Path("words")
public class WordsFacadeREST {
    static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WordsFacadeREST.class);
    @EJB
    WordFacade dao;

    @POST
    @Consumes({"application/xml", "application/json"})
    public Response create(Word entity, @Context HttpServletRequest req) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("------------REMOTE IP ADDRESS------------" + req.getRemoteAddr());
        }
        dao.create(entity);
        return Response.status(201).entity(entity).build();
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Word find(@PathParam("id") Integer id, @Context HttpServletRequest req) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("------------REMOTE IP ADDRESS------------" + req.getRemoteAddr());
        }       
        return dao.find(id);
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    public void edit(Word entity) {
        dao.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        dao.remove(dao.find(id));
    }

    @GET
    @Produces({"application/xml", "application/json"})
    public List<Word> findAll() {
        return dao.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Word> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return dao.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(dao.count());
    }
}
