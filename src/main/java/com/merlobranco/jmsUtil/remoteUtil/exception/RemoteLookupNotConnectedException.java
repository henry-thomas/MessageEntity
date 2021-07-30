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
package com.merlobranco.jmsUtil.remoteUtil.exception;

/**
 *
 * @author platar86
 */
public class RemoteLookupNotConnectedException extends RemoteLookupException {

    /**
     * Creates a new instance of <code>RemoteLookupNotConnectedException</code>
     * without detail message.
     */
    public RemoteLookupNotConnectedException() {
    }

    /**
     * Constructs an instance of <code>RemoteLookupNotConnectedException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public RemoteLookupNotConnectedException(String msg) {
        super(msg);
    }
}
