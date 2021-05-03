/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlobranco.ejb;

import javax.ejb.Remote;

/**
 *
 * @author brais
 */
@Remote
public interface TestRemote {
    public String method();
    public Integer next();
}