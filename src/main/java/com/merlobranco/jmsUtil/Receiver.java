/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlobranco.jmsUtil;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author henry
 */
@MessageDriven(name = "testmdb", activationConfig = {
    @ActivationConfigProperty(propertyName = "resourceAdapter", propertyValue = "jmsra"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "webappQueue"),
//    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:comp/jms/webappQueue"),
//    @ActivationConfigProperty(propertyName = "userName", propertyValue = "guest"),
//    @ActivationConfigProperty(propertyName = "password", propertyValue = "guest"),
    @ActivationConfigProperty(propertyName = "addressList", propertyValue = "localhost:7676")
})
//@MessageDriven(name = "testmdb", activationConfig = {
//    @ActivationConfigProperty(propertyName = "resourceAdapter", propertyValue = "imqjmsra"),
//    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
//    @ActivationConfigProperty(propertyName = "destination", propertyValue = "${MPCONFIG=mq.queue.name}"),
//    @ActivationConfigProperty(propertyName = "userName", propertyValue = "${MPCONFIG=mq.username}"),
//    @ActivationConfigProperty(propertyName = "password", propertyValue = "${MPCONFIG=mq.password}"),
//    @ActivationConfigProperty(propertyName = "addressList", propertyValue = "${MPCONFIG=mq.addressList}")
//})
public class Receiver implements MessageListener {

    @Override
    public void onMessage(Message message) {
        System.out.println("Got message " + message);
    }
}
