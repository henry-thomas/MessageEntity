package com.merlobranco.jmsUtil.remoteUtil;

///* Copyright (C) Solar MD (Pty) ltd - All Rights Reserved
// * 
// *  Unauthorized copying of this file, via any medium is strictly prohibited
// *  Proprietary and confidential
// *  
// *  Written by brais, 2021
// *  
// *  For more information http://www.solarmd.co.za/ 
// *  email: info@solarmd.co.za
// *  7 Alternator Ave, Montague Gardens, Cape Town, 7441 South Africa
// *  Phone: 021 555 2181
// *  
// */
//package com.myPower24.commonLib.remoteUtil;
//
//import com.myPower24.commonLib.ejbLib.util.LookUpUtil;
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import javax.ejb.LocalBean;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;
//import javax.ejb.Timeout;
//import javax.ejb.Timer;
//import javax.ejb.TimerService;
//import javax.ejb.TransactionAttribute;
//import javax.ejb.TransactionAttributeType;
//
///**
// *
// * @author brais
// */
//@Singleton
//@LocalBean
//@Startup
//public class EJBConnectionTimer {
//    
//    private static final long TIMEOUT = 5 * 1000;
//    
//    @Resource
//    private TimerService timerService;
//    
//    @PostConstruct
//    public void init() {
//        if (LookUpUtil.isEmpty()) {
//            return;
//        }
//        String timerName = EJBConnectionTimer.class.getSimpleName();
//        if (!timerService.getAllTimers().stream().anyMatch((t) -> t.getInfo().equals(timerName))) {
//            timerService.createTimer(1000, TIMEOUT, timerName);
//        }
//    }
//    
//    @Timeout
//    @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
//    public void timeoutCallback(Timer timer) {
//        System.out.println("Ping Timer ...");
//        LookUpUtil.ping();
//        System.out.println("Ping Timer Finished ...");
//    }
//}
