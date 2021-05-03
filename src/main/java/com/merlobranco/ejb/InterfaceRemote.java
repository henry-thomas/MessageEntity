/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlobranco.ejb;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author brais
 * @param <T>
 */
public interface InterfaceRemote<T> extends Serializable {
    
    void create(T entity);

    void edit(T entity);

    void remove(T entity);

    T find(Object id);

    List<T> findAll();

    List<T> findRange(int[] range);

    int count();
}
