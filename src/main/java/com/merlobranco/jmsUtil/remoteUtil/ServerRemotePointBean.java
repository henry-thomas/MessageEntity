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

import com.merlobranco.jmsUtil.util.FixedSizeQueue;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author brais
 */
public class ServerRemotePointBean {

    private static final Logger LOG = Logger.getLogger("RemoteMethodInvocationConnector");

//    @Resource
//    private ManagedExecutorService threadFactory;
//      @Resource(lookup = "java:comp/DefaultManagedExecutorService")
//      ExecutorService executorService;
    public static final String SEPARATOR = "@";

    //remote point definitions
    private final Hashtable<String, String> remotePointParamMap = new Hashtable<>();
    private final Map<String, RemoteEJBPointBean> remoteInterfaceMap = new HashMap<>();
    private final String id;
    private final String ipAddress;
    private final String ipPort;

    private boolean connectionState = false;

    private Context context = null;
    private EJBConnectionTestUtilRemote remotePing = null;

    //statistic data
    private long callsCounter = 0;
    private Date lastUp = null;
    private Date lastDown = new Date();
    private Date startup = new Date();
    private int disconnectionCounter;
    private final FixedSizeQueue<Date> connectionsUpLog = new FixedSizeQueue<>(100);
    private final FixedSizeQueue<Date> connectionsDownLog = new FixedSizeQueue<>(100);

    private final ServerRemoteMonitor monitor;

//    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public ServerRemotePointBean(String host, String port, String id) {

        LOG.setLevel(Level.ALL);
        System.out.println("========================== Remote EJB Server " + id + " STARTUP ============================");
        remotePointParamMap.put("org.omg.CORBA.ORBInitialHost", host);
        remotePointParamMap.put("org.omg.CORBA.ORBInitialPort", port);
        remotePointParamMap.put("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory");

        this.id = id;
        this.ipAddress = host;
        this.ipPort = port;

        //last to init
        monitor = new ServerRemoteMonitor(this, "RemoteEjbMonitor-" + id);

    }

    public void distroy() {
        monitor.shutdown();
        remoteInterfaceMap.clear();
        remotePointParamMap.clear();
        LOG.log(Level.FINE, "RemoteEjbMonitor {0} Cleaning up ", id);
    }

    public ServerRemoteMonitor getMonitor() {
        return monitor;
    }

    public String getId() {
        return id;
    }

    public Context getContext() {
        return context;
    }

    private void setDisconnected() {
        lastDown = new Date();
        connectionsUpLog.add(lastDown);
    }

    private void setConnected(Context ctx) {
        try {
            this.context = ctx;

            remotePing = (EJBConnectionTestUtilRemote) context.lookup(EJBConnectionTestUtilRemote.class.getName());
            remotePing.ping();

            //if ping successful init all dudes
            for (Map.Entry<String, RemoteEJBPointBean> entry : remoteInterfaceMap.entrySet()) {

                RemoteEJBPointBean remoteIface = entry.getValue();
                Object lookup = ctx.lookup(remoteIface.getJndiPath());
                remoteIface.setRemote(lookup);
            }

            lastDown = new Date();
            connectionsUpLog.add(lastDown);

            connectionState = true;

            //corba exception are generated and printed from JNI, but we can always try 
        } catch (NamingException ex) {
            LOG.log(Level.SEVERE, ex.getMessage() + " @ " + id);
        }
    }

    void checkConnection() {
        LOG.log(Level.FINE, "RemoteEjbMonitor Checking Connection with {0} ...", id);
        try {
            if (connectionState) {
                remotePing.ping();
            } else {
                Context ctx = new InitialContext(remotePointParamMap);
                if (!connectionState) {
                    setConnected(ctx);
                }
            }
            LOG.log(Level.FINE, "RemoteEjbMonitor Checking Connection {0}  SUCCESS", id);
        } catch (NamingException ex) {
            if (connectionState) {
                setDisconnected();
            }
            //  Logger.getLogger(ServerRemotePointBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Map<String, RemoteEJBPointBean> getRemoteEBJsMap() {
        return remoteInterfaceMap;
    }

    public boolean isConnected() {
        return context != null;
    }

    public long getCallsCounter() {
        return callsCounter;
    }

    public void setCallsCounter(long callsCounter) {
        this.callsCounter = callsCounter;
    }

    public Date getLastUp() {
        return lastUp;
    }

    public void setLastUp(Date lastUp) {
        this.lastUp = lastUp;
    }

    public Date getLastDown() {
        return lastDown;
    }

    public void setLastDown(Date lastDown) {
        this.lastDown = lastDown;
    }

    public int getDisconnectionCounter() {
        return disconnectionCounter;
    }

    public void setDisconnectionCounter(int disconnectionCounter) {
        this.disconnectionCounter = disconnectionCounter;
    }

    public EJBConnectionTestUtilRemote getRemotePing() {
        return remotePing;
    }

    public void setRemotePing(EJBConnectionTestUtilRemote remotePing) {
        this.remotePing = remotePing;
    }

    public Date getStartup() {
        return startup;
    }

    public void setStartup(Date startup) {
        this.startup = startup;
    }

    public Map<String, RemoteEJBPointBean> getRemoteInterfaceMap() {
        return remoteInterfaceMap;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getIpPort() {
        return ipPort;
    }

}
