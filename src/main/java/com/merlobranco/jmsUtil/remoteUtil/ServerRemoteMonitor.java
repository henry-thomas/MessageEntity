/* Copyright (C) Solar MD (Pty) ltd - All Rights Reserved
 * 
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  
 *  Written by platar86, 2021
 *  
 *  For more information http://www.solarmd.co.za/ 
 *  email: info@solarmd.co.za
 *  7 Alternator Ave, Montague Gardens, Cape Town, 7441 South Africa
 *  Phone: 021 555 2181
 *  
 */
package com.merlobranco.jmsUtil.remoteUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author platar86
 */
public class ServerRemoteMonitor implements Runnable {

    private static final Logger LOG = Logger.getLogger("RemoteMethodInvocationConnector");

    private final ServerRemotePointBean server;
    private boolean running;

    private int pingDelaySec = 5;

    private final String name;

    public ServerRemoteMonitor(ServerRemotePointBean server, String name) {
        this.server = server;
        this.name = name;
    }

    @Override
    public void run() {
        LOG.log(Level.FINEST, "Remote Server Monitor Created. Host:[{0}:{1}]  {2}", new Object[]{server.getIpAddress(), server.getIpPort(), this.toString()});
        running = true;
        while (running) {
            try {
                Thread.sleep(pingDelaySec * 1000);
                if (server.getRemoteEBJsMap().isEmpty()) {
                    continue;
                }
                server.checkConnection();
            } catch (Throwable e) {
                LOG.log(Level.INFO, "RemoteEjb {0} exception:{1}", new Object[]{server.getId(), e.getMessage()});
            }
        }
        LOG.log(Level.FINEST, "Remote Server Monitor Created. Host:[{0}:{1}]  {2}", new Object[]{server.getIpAddress(), server.getIpPort(), this.toString()});
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getPingDelaySec() {
        return pingDelaySec;
    }

    public void shutdown() {
        running = false;
    }

    public void setPingDelaySec(int pingDelaySec) {
        if (pingDelaySec <= 0) {
            pingDelaySec = 1;
        }
        this.pingDelaySec = pingDelaySec;
    }

    public ServerRemotePointBean getServer() {
        return server;
    }

    public String getName() {
        return name;
    }
    
    

}
