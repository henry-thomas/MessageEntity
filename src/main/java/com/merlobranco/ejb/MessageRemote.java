/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.merlobranco.ejb;

import com.merlobranco.entity.Message;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author user-pc
 */
@Remote
public interface MessageRemote extends InterfaceRemote<Message>{
    
    List<Message> findOrdered();
}
