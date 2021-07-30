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

import com.merlobranco.jmsUtil.util.PropertiesUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Remove;
import javax.ejb.Singleton;
import javax.enterprise.concurrent.ManagedThreadFactory;

/**
 *
 * @author brais
 */
@Singleton
public class RemoteCORBALookUpContext {

    @Resource(name = "java:comp/DefaultManagedThreadFactory")
    ManagedThreadFactory threadFactory;

    private final Logger LOG = Logger.getLogger("RemoteMethodInvocationConnector");
    static final String PROPERTIES_EJB_FILENAME = "RemoteEJB.properties";

    private final String DEFAULT_PORT = "3700";

    private static final Map<String, ServerRemotePointBean> serverRemoteMap = new HashMap<>();
    private static final Map<String, RemoteEJBPointBean> remoteEJBConnecionsMap = new HashMap<>();

    private static boolean initComplete = false;

    @PreDestroy
    @Remove(retainIfException = false)
    public void onClose() {
        for (Map.Entry<String, ServerRemotePointBean> entry : serverRemoteMap.entrySet()) {
            ServerRemotePointBean sp = entry.getValue();
            sp.distroy();
        }
    }

    private Properties getPropertyFileFromConfig() {
        String configFolderPath = System.getProperty("user.dir");
        String osPropName = System.getProperty("os.name");
        String remoteEjbPropFilePath;
        if (osPropName != null && osPropName.contains("Windows")) {
            remoteEjbPropFilePath = configFolderPath + "\\" + PROPERTIES_EJB_FILENAME;
        } else {
            remoteEjbPropFilePath = configFolderPath + "/" + PROPERTIES_EJB_FILENAME;
        }

        if (configFolderPath != null) {
            System.out.println("REMOTE EJB Checking for Configuration in use: " + remoteEjbPropFilePath);
            File f = new File(remoteEjbPropFilePath);

            try (FileInputStream fis = new FileInputStream(f)) {
                Properties prop = new Properties();
                prop.load(fis);
                System.out.println("Config file in use: " + remoteEjbPropFilePath);
                return prop;
            } catch (IOException ex) {
//                Logger.getLogger(RemoteCORBALookUpContext.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("REMOTE EJB File not found or reading error: " + remoteEjbPropFilePath);
        return null;
    }

    private Properties getProperyFile() {
        Properties propFromDomainConfigFolder = getPropertyFileFromConfig();
        if (propFromDomainConfigFolder != null) {
            return propFromDomainConfigFolder;
        }

        try {
            System.out.println("REMOTE EJB Checking for  Config file in: Resource Folder - " + PROPERTIES_EJB_FILENAME);

            Properties props = PropertiesUtil.loadProperties(PROPERTIES_EJB_FILENAME);

            System.out.println("REMOTE EJB Config file in use: Resource Folder - " + PROPERTIES_EJB_FILENAME);
            return props;
        } catch (IOException ex) {
//            Logger.getLogger(RemoteCORBALookUpContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @PostConstruct
    public void init() {
        if (!initComplete) {
            initComplete = true;
            System.out.println("======================== RemoteEjb Lookup and Monitor Startup  =========================");

            Properties props = getProperyFile();
            if (props == null) {
                LOG.log(Level.INFO, "No " + PROPERTIES_EJB_FILENAME + " provided");
                return;
            }

            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                try {

                    String key = entry.getKey().toString();
                    String val = entry.getValue().toString();
                    if (val == null || val.length() == 0) {
                        LOG.log(Level.WARNING, "Invalid Property value for Key:[{0}]", key);
                        continue;
                    }

                    String[] valArr = val.split(ServerRemotePointBean.SEPARATOR);
                    if (valArr.length != 3) {
                        LOG.log(Level.WARNING, "Invalid Property value len for Key:[{0}] , Required 3 param found {1}.", new Object[]{key, valArr.length});
                        continue;
                    }

                    String hostIp = valArr[0];
                    String hostPort = valArr[1];
                    String serverId = hostIp + ServerRemotePointBean.SEPARATOR + hostPort;
                    String jndiName = valArr[2];

                    ServerRemotePointBean server = serverRemoteMap.get(serverId);
                    if (server == null) {
                        //create new Server if not found
                        server = new ServerRemotePointBean(hostIp, hostPort, serverId);

                        //start monitor
                        Thread monitorThread = threadFactory.newThread(server.getMonitor());
                        monitorThread.setName(server.getMonitor().getName());
                        monitorThread.start();

                        LOG.log(Level.INFO, "New Remote Server Created. Host:[{0}:{1}]", new Object[]{hostIp, hostPort});
                        serverRemoteMap.put(serverId, server);
                    }

                    RemoteEJBPointBean repb = new RemoteEJBPointBean(server, jndiName);

                    server.getRemoteEBJsMap().put(key, repb);
                    remoteEJBConnecionsMap.put(key, repb);
                } catch (Exception e) {
                    LOG.log(Level.SEVERE, "RemotEjbConnector Error. {}", e);
                }

            }
        }
    }

//    public static <T> T call(Class<T> type) throws RemoteLookupException {
//        String remoteName = type.getSimpleName();
//
//        if (!remoteEJBConnecionsMap.containsKey(remoteName)) {
//            throw new RemoteLookupNotDeclaredException(PROPERTIES_EJB + " file DOES NOT contain configuration for : '" + remoteName + "'");
//        }
//
//        RemoteEJBPointBean remote = remoteEJBConnecionsMap.get(remoteName);
//        if (!remote.isConnected()) {
//            throw new RemoteLookupNotConnectedException("RemoteEjbCall at server: [" + remote.getRemoteServer().getId() + "] fail: for Remote Bean [" + remoteName + "]'");
//        }
//        return (T) remote.getRemote();
//    }
    public Map<String, RemoteEJBPointBean> getRemoteEJBConnecionsMap() {
        return remoteEJBConnecionsMap;
    }

}
