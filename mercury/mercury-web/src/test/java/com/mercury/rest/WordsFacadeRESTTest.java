package com.mercury.rest;

import com.mercury.bean.EmailSenderBean;
import com.mercury.bean.LogBean;
import com.mercury.bean.WordBean;
import com.mercury.entity.Word;
import com.mercury.interceptor.WordInterceptor;
import com.mercury.util.LogFilter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse;
import java.net.URL;
import java.util.logging.Level;
import javax.ws.rs.ApplicationPath;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Pape S. Diop <papesdiop@gmail.com>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class WordsFacadeRESTTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordsFacadeRESTTest.class);
    private static final String RESOURCE_PREFIX = JaxRsActivator.class.getAnnotation(ApplicationPath.class).value().substring(1);

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        //File[] libs = Maven.resolver().loadPomFromFile("pom.xml").importRuntimeAndTestDependencies().asFile();
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(Word.class.getPackage())
                //.addPackage(LogFilter.class.getPackage())
                .addClasses(WordBean.class,LogBean.class, WordInterceptor.class, WordsFacadeREST.class)//, EmailSenderBean.class)
                .addAsResource("persistence-test.xml", "META-INF/persistence.xml")
                .addAsResource("logback-test.xml", "logback.xml")
                //.addAsResource("import.sql")
                //.addAsWebInfResource("log4j.properties")
                .addAsWebInfResource("web.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    URL deploymentUrl;
    
 
    /**
     * Teste l'envoi d'objet JSON par la methode POST, 
     * et teste si le code HTTP retourné est égal à 201
     * @throws Exception 
     */
    @Test
    public void testSendWordRequestWithPostHttpMethod_WithJerseyClient() throws Exception{
        Client client = Client.create();
        String uri = deploymentUrl.toString() + RESOURCE_PREFIX + "/words";
        
        WebResource webResource = client.resource(uri);
        String input = "{\"message\":\"Hello Mercury\"}";
        LOGGER.debug("URI :" + uri + "  "+input);
        ClientResponse response = webResource.type("application/json")
                .post(ClientResponse.class, input);       
        Assert.assertEquals("Test Failed, must return 201. HTTP error code expected is: " + response.getStatus(), response.getStatus(), 201);
    }
    
    /**
     * Teste l'URI /rest/words 
     * et devrait retourner une liste d'objet de type JSON de la forme {"id":1,"libelle":"Hello Mercury"}
     * @throws Exception 
     */
    @Test
    public void testGetWordRequestWithGetHttpMethod_WithJersey() throws Exception {
        String uri = deploymentUrl.toString() +  RESOURCE_PREFIX + "/words";
        Client client = Client.create();

        WebResource webResource = client.resource(uri);

        ClientResponse response = webResource.accept("application/json")
                .get(ClientResponse.class);        
        Object output = response.getEntity(String.class);
        LOGGER.info("mercury log: Output from Server .... {}", output);
        LOGGER.debug("mercury log: " + output.toString());  
        Assert.assertEquals("Failed : HTTP error code : " + response.getStatus(), response.getStatus(), 200);       
    }
    
    /**
     * 
     */
    @Test
    public void simulateErrorAndWriteLogBasedOnLogBackConfig(){
        LOGGER.error("Exeption ou erreur serveur rencontree lors de l'enregistrement du mot {}","192.250.89.1");
    }
}
