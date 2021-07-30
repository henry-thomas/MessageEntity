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

import java.util.ArrayList;

/**
 *
 * @author brais
 * @param <E>
 */
public class FixedSizeQueue<E> extends ArrayList<E> {

    private static final long serialVersionUID = 1L;
    
    private final int size;
    
    public FixedSizeQueue(int size) {
        this.size = size;
    }

    @Override
    public void add(int index, E element) {
        if (super.size() == size) {
            super.remove(0);
        }
        super.add(index, element);
    }

    @Override
    public boolean add(E e) {
        if (super.size() == size) {
            super.remove(0);
        }
        return super.add(e);
    }
    
    public int getSize() {
        return size;
    }
}
