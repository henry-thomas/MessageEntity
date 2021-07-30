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


import com.merlobranco.jmsUtil.remoteUtil.exception.RemoteLookupException;
import com.merlobranco.jmsUtil.remoteUtil.exception.RemoteLookupFailException;
import com.merlobranco.jmsUtil.remoteUtil.exception.RemoteLookupNotConnectedException;
import com.merlobranco.jmsUtil.remoteUtil.exception.RemoteLookupNotDeclaredException;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author brais
 */
@Stateless
public class RemoteLookUp {

    @EJB
    RemoteCORBALookUpContext remoteLookUpContext;

    /**
     * Call Remote Service via CORBA RPC
     *
     * @param <T> Remote Interface class
     * @return
     * @throws RemoteLookupException extends RuntimeException
     */
    public <T> T call(Class<T> type) throws RemoteLookupException {
        String remoteName = type.getSimpleName();
        Map<String, RemoteEJBPointBean> remoteMap = remoteLookUpContext.getRemoteEJBConnecionsMap();

        if (!remoteMap.containsKey(remoteName)) {
            throw new RemoteLookupNotDeclaredException(RemoteCORBALookUpContext.PROPERTIES_EJB_FILENAME + " file DOES NOT contain configuration for : '" + remoteName + "'");
        }

        RemoteEJBPointBean remote = remoteMap.get(remoteName);
        if (!remote.isConnected()) {
            throw new RemoteLookupNotConnectedException("RemoteEjbCall at server: [" + remote.getRemoteServer().getId() + "] fail: for Remote Bean [" + remoteName + "]'");
        }
        return (T) remote.getRemote();
    }

    /**
     * Call Remote Service via CORBA RPC
     *
     * @param <T> Remote Interface class
     * @return
     * @throws RemoteLookupFailException extends Exception
     */
    public <T> T callSafe(Class<T> type) throws RemoteLookupFailException {
        String remoteName = type.getSimpleName();
        Map<String, RemoteEJBPointBean> remoteMap = remoteLookUpContext.getRemoteEJBConnecionsMap();

        if (!remoteMap.containsKey(remoteName)) {
            throw new RemoteLookupFailException(RemoteCORBALookUpContext.PROPERTIES_EJB_FILENAME + " file DOES NOT contain configuration for : '" + remoteName + "'");
        }

        RemoteEJBPointBean remote = remoteMap.get(remoteName);
        if (!remote.isConnected()) {
            throw new RemoteLookupFailException("RemoteEjbCall at server: [" + remote.getRemoteServer().getId() + "] fail: for Remote Bean [" + remoteName + "]'");
        }
        return (T) remote.getRemote();
    }

}
