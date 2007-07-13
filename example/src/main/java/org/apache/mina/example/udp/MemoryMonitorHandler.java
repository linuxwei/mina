/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.mina.example.udp;

import java.net.SocketAddress;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;

/**
 * Class the extends IoHandlerAdapter in order to properly handle
 * connections and the data the connections send
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class MemoryMonitorHandler extends IoHandlerAdapter {

    private MemoryMonitor server;

    public MemoryMonitorHandler(MemoryMonitor server) {
        this.server = server;
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        session.close();
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {

        if (message instanceof ByteBuffer) {
            ByteBuffer buffer = (ByteBuffer) message;
            SocketAddress remoteAddress = session.getRemoteAddress();
            server.recvUpdate(remoteAddress, buffer.getLong());
        }
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        System.out.println("Session closed...");
        SocketAddress remoteAddress = session.getRemoteAddress();
        server.removeClient(remoteAddress);
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {

        System.out.println("Session created...");

        SocketAddress remoteAddress = session.getRemoteAddress();
        server.addClient(remoteAddress);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        System.out.println("Session idle...");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        System.out.println("Session Opened...");
    }
}
