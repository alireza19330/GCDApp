package com.developairs.util.test;

import java.util.concurrent.Callable;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RunAs;
import javax.ejb.Stateless;

/**
 * 
 * @author Ali Abazari
 * <p>This stateless session bean is used in test to call secured ejb by <b>soapclient</b> role</p>
 *
 */
@Stateless
@RunAs("soapclient")
@PermitAll
public class SoapUser {
    public void call(Callable callable) throws Exception {
        callable.call();
    }
}
