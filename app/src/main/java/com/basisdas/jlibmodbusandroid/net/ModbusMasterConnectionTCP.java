package com.basisdas.jlibmodbusandroid.net;

import com.basisdas.jlibmodbusandroid.exception.ModbusIOException;
import com.basisdas.jlibmodbusandroid.net.stream.base.LoggingInputStream;
import com.basisdas.jlibmodbusandroid.net.stream.base.LoggingOutputStream;
import com.basisdas.jlibmodbusandroid.net.transport.ModbusTransport;
import com.basisdas.jlibmodbusandroid.net.transport.ModbusTransportFactory;
import com.basisdas.jlibmodbusandroid.tcp.TcpParameters;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/*
 * Copyright (C) 2016 "Invertor" Factory", JSC
 * [http://www.sbp-invertor.ru]
 *
 * This file is part of JLibModbus.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors: Vladislav Y. Kochedykov, software engineer.
 * email: vladislav.kochedykov@gmail.com
 */
class ModbusMasterConnectionTCP extends ModbusConnection {

    final private TcpParameters parameters;
    private ModbusTransport transport = null;

    ModbusMasterConnectionTCP(TcpParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public LoggingOutputStream getOutputStream() {
        return transport.getOutputStream();
    }

    @Override
    public LoggingInputStream getInputStream() {
        return transport.getInputStream();
    }

    @Override
    public ModbusTransport getTransport() {
        return transport;
    }

    @Override
    protected void openImpl() throws ModbusIOException {
        if (!isOpened()) {
            if (parameters != null) {
                InetSocketAddress isa = new InetSocketAddress(parameters.getHost(), parameters.getPort());
                Socket socket = new Socket();
                try {
                    socket.connect(isa, parameters.getConnectionTimeout());
                    socket.setKeepAlive(parameters.isKeepAlive());
                    socket.setTcpNoDelay(true);

                    transport = ModbusTransportFactory.createTCP(socket);
                    setReadTimeout(getReadTimeout());
                } catch (Exception e) {
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        // ignored
                    }
                    throw new ModbusIOException(e);
                }
            } else {
                throw new ModbusIOException("TCP parameters is null");
            }
        }
    }

    @Override
    protected void closeImpl() throws ModbusIOException {
        try {
            if (transport != null) {
                transport.close();
            }
        } catch (IOException e) {
            throw new ModbusIOException(e);
        } finally {
            transport = null;
        }
    }
}
