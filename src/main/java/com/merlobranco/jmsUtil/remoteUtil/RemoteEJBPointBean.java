/* Copyright (C) Solar MD (Pty) ltd - All Rights Reserved
 * 
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  
 *  Written by brais, 2021
 *  
 *  For more information http://www.solarmd.co.za/ 
 *  email: info@solarmd.co.za
 *  7 Alternator Ave, Montague Gardens, Cape Town, 7441 South Africa
 *  Phone: 021 555 2181
 *  
 */
package com.merlobranco.jmsUtil.remoteUtil;

/**
 *
 * @author brais
 */
public class RemoteEJBPointBean {

    private final ServerRemotePointBean remoteServer;

    private String jndiPath;

    private Object remote;

    private long callsCounter = 0;

    public RemoteEJBPointBean(ServerRemotePointBean rpb, String jndiPath) {
        this.remoteServer = rpb;
        this.jndiPath = jndiPath;
    }

    public boolean isConnected() {
        return remoteServer.isConnected();
    }

    public String getJndiPath() {
        return jndiPath;
    }

    public void setJndiPath(String jndiPath) {
        this.jndiPath = jndiPath;
    }

    public Object getRemote() {
        callsCounter++;
        return remote;
    }

    public void setRemote(Object remote) {
        this.remote = remote;
    }

    public long getCallsCounter() {
        return callsCounter;
    }

    public void setCallsCounter(long callsCounter) {
        this.callsCounter = callsCounter;
    }

    public ServerRemotePointBean getRemoteServer() {
        return remoteServer;
    }

}
