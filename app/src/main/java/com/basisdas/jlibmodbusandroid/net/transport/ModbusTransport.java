package com.basisdas.jlibmodbusandroid.net.transport;

import com.basisdas.jlibmodbusandroid.exception.ModbusIOException;
import com.basisdas.jlibmodbusandroid.exception.ModbusNumberException;
import com.basisdas.jlibmodbusandroid.msg.ModbusMessageFactory;
import com.basisdas.jlibmodbusandroid.msg.ModbusRequestFactory;
import com.basisdas.jlibmodbusandroid.msg.base.ModbusMessage;
import com.basisdas.jlibmodbusandroid.msg.base.ModbusRequest;
import com.basisdas.jlibmodbusandroid.net.stream.base.LoggingInputStream;
import com.basisdas.jlibmodbusandroid.net.stream.base.LoggingOutputStream;

import java.io.IOException;

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
public abstract class ModbusTransport {

    final private LoggingInputStream is;
    final private LoggingOutputStream os;

    ModbusTransport(LoggingInputStream is, LoggingOutputStream os) {
        this.is = is;
        this.os = os;
    }

    public void close() throws IOException {
        is.close();
        os.close();
    }

    protected ModbusMessage createMessage(ModbusMessageFactory factory) throws IOException, ModbusNumberException, ModbusIOException {
        int serverAddress = is.read();
        int functionCode = is.read();
        ModbusMessage msg = factory.createMessage(functionCode);
        msg.setServerAddress(serverAddress);
        msg.read(is);
        return msg;
    }

    public ModbusMessage readRequest() throws ModbusNumberException, ModbusIOException {
        return readMessage(ModbusRequestFactory.getInstance());
    }

    public ModbusMessage readResponse(ModbusRequest request) throws ModbusNumberException, ModbusIOException {
        return readMessage(request);
    }

    final public ModbusMessage readMessage(ModbusMessageFactory factory) throws ModbusNumberException, ModbusIOException {
        try {
            return read(factory);
        } finally {
            getInputStream().log();
        }
    }

    public void send(ModbusMessage msg) throws ModbusIOException {
        try {
            sendImpl(msg);
            getOutputStream().flush();
        } catch (IOException e) {
            throw new ModbusIOException(e);
        }

    }

    public LoggingInputStream getInputStream() {
        return is;
    }

    public LoggingOutputStream getOutputStream() {
        return os;
    }

    abstract protected ModbusMessage read(ModbusMessageFactory factory) throws ModbusNumberException, ModbusIOException;

    abstract protected void sendImpl(ModbusMessage msg) throws ModbusIOException;
}
