/**
 * RemoteFacadeService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.mxunit.eclipseplugin.actions.bindings.generated;

public interface RemoteFacadeService extends javax.xml.rpc.Service {

/**
 * Main default interface into MXUnit framework from the MXUnit Ecplise
 * Plugin.
 */
    public java.lang.String getRemoteFacadeCfcAddress();

    public org.mxunit.eclipseplugin.actions.bindings.generated.RemoteFacade getRemoteFacadeCfc() throws javax.xml.rpc.ServiceException;

    public org.mxunit.eclipseplugin.actions.bindings.generated.RemoteFacade getRemoteFacadeCfc(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
