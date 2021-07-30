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

import javax.ejb.Stateless;

/**
 *
 * @author brais
 */

@Stateless
public class EJBConnectionFacade implements EJBConnectionTestUtilRemote {
    
    @Override
    public void ping() {       
    }
    
    @Override
    public int speed(int value) {
        return value++;
    }
}
