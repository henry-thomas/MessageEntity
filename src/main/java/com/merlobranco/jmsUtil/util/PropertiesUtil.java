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
package com.merlobranco.jmsUtil.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author brais
 * The properties file will be loaded from anywhere in project’s classpath.
 */
public class PropertiesUtil {
    private static final Logger LOG = Logger.getLogger(PropertiesUtil.class.getName());
    
    public static Properties loadProperties(String propFileName) throws IOException {
        Properties props = new Properties();
        try (InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(propFileName)) {
            if (inputStream == null) {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            props.load(inputStream);
        }
        return props;
    }
}
