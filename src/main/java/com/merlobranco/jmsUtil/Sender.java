/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlobranco.jmsUtil;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.resource.AdministeredObjectDefinition;
import javax.resource.ConnectionFactoryDefinition;

/**
 *
 * @author henry
 */
@Stateless
@JMSDestinationDefinition(
        name = "java:comp/jms/webappQueue",
        interfaceName = "javax.jms.Queue",
        destinationName = "webappQueue")
//
//@ConnectionFactoryDefinition(name = "java:comp/jms/DefaultJMSConnectionFactory",
//        interfaceName = "javax.jms.ConnectionFactory",
//        resourceAdapter = "jmsra",
//        properties = {"UserName=guest", "Password=guest", "AddressList=localhost:7676"})
//
//@AdministeredObjectDefinition(resourceAdapter = "jmsra",
//        interfaceName = "javax.jms.Queue",
//        className = "com.sun.messaging.Queue",
//        name = "java:app/jms/TestQ",
//        properties = {"Name=TESTQ"})
//@ConnectionFactoryDefinition(name = "java:app/jms/SendJMS",
//        interfaceName = "javax.jms.ConnectionFactory",
//        resourceAdapter = "imqjmsra",
//        properties = {"UserName=${MPCONFIG=mq.username}", "Password=${MPCONFIG=mq.password}", "AddressList=${MPCONFIG=mq.addressList}"})
//
//@AdministeredObjectDefinition(resourceAdapter = "imqjmsra",
//        interfaceName = "javax.jms.Queue",
//        className = "com.sun.messaging.Queue",
//        name = "java:app/jms/TestQ",
//        properties = {"Name=${MPCONFIG=mq.queue.name}"})
public class Sender {

    public Sender() {
    }

    Queue queue;

    ConnectionFactory factory;

    public void sendMessage(String message) {
        try (JMSContext context = factory.createContext()){
            context.createProducer().send(queue, "This is a test at " + new Date());
            context.createProducer().send(queue, "Message Received: " + message);
        } catch (JMSRuntimeException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PostConstruct
    private void setContext() {
        try {
            Properties properties = new Properties();
            properties.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");
//            properties.setProperty(Context.PROVIDER_URL, "localhost:");
            
            Context ctx = new InitialContext();
            queue = (Queue) ctx.lookup("java:comp/jms/webappQueue");
            factory = (ConnectionFactory) ctx.lookup("java:comp/DefaultJMSConnectionFactory");
            
        } catch (NamingException ex) {
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
