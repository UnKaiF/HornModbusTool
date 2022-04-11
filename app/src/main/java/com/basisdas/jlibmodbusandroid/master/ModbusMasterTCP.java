package com.basisdas.jlibmodbusandroid.master;

import com.basisdas.jlibmodbusandroid.Modbus;
import com.basisdas.jlibmodbusandroid.data.CommStatus;
import com.basisdas.jlibmodbusandroid.exception.IllegalFunctionException;
import com.basisdas.jlibmodbusandroid.exception.ModbusIOException;
import com.basisdas.jlibmodbusandroid.exception.ModbusNumberException;
import com.basisdas.jlibmodbusandroid.exception.ModbusProtocolException;
import com.basisdas.jlibmodbusandroid.msg.base.ModbusMessage;
import com.basisdas.jlibmodbusandroid.msg.base.ModbusRequest;
import com.basisdas.jlibmodbusandroid.net.ModbusConnectionFactory;
import com.basisdas.jlibmodbusandroid.tcp.TcpParameters;
import com.basisdas.jlibmodbusandroid.utils.ModbusFunctionCode;

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

final public class ModbusMasterTCP extends ModbusMaster {
    final private boolean keepAlive;

    public ModbusMasterTCP(TcpParameters parameters) {
        super(ModbusConnectionFactory.getTcpMaster(parameters));
        keepAlive = parameters.isKeepAlive();
        try {
            if (isKeepAlive()) {
                connect();
            }
        } catch (ModbusIOException e) {
            Modbus.log().warning("keepAlive is set, connection failed at creation time.");
        }
    }

    private int nextTransactionId() {
        int nextId = getTransactionId() + 1;
        return nextId > Modbus.TRANSACTION_ID_MAX_VALUE ? 0 : nextId;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    @Override
    protected void sendRequest(ModbusMessage msg) throws ModbusIOException {
        if (!isKeepAlive())
            connect();
        try {
            if (Modbus.isAutoIncrementTransactionId()) {
                setTransactionId(nextTransactionId());
            }
            msg.setTransactionId(getTransactionId());
            super.sendRequest(msg);
        } catch (ModbusIOException e) {
            if (isKeepAlive()) {
                disconnect();
                connect();
                super.sendRequest(msg);
            } else {
                throw e;
            }
        }
    }

    @Override
    protected ModbusMessage readResponse(ModbusRequest request) throws ModbusNumberException, ModbusIOException, ModbusProtocolException {
        ModbusMessage msg = super.readResponse(request);
        if (!isKeepAlive()) {
            disconnect();
        }
        return msg;
    }

    @Override
    public int readExceptionStatus(int serverAddress) throws ModbusNumberException, ModbusIOException, ModbusProtocolException {
        throw new IllegalFunctionException(ModbusFunctionCode.READ_EXCEPTION_STATUS.toInt());
    }

    @Override
    public byte[] reportSlaveId(int serverAddress) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        throw new IllegalFunctionException(ModbusFunctionCode.REPORT_SLAVE_ID.toInt());
    }

    @Override
    public CommStatus getCommEventCounter(int serverAddress) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        throw new IllegalFunctionException(ModbusFunctionCode.GET_COMM_EVENT_COUNTER.toInt());
    }

    @Override
    public CommStatus getCommEventLog(int serverAddress) throws ModbusProtocolException, ModbusNumberException, ModbusIOException {
        throw new IllegalFunctionException(ModbusFunctionCode.GET_COMM_EVENT_LOG.toInt());
    }

    @Override
    public void diagnosticsReturnQueryData(int serverAddress, int queryData) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }

    @Override
    public void diagnosticsRestartCommunicationsOption(int serverAddress, boolean clearLog) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }

    @Override
    public int diagnosticsReturnDiagnosticRegister(int serverAddress) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }

    @Override
    public void diagnosticsChangeAsciiInputDelimiter(int serverAddress, int delimiter) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }

    @Override
    public void diagnosticsForceListenOnlyMode(int serverAddress) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }

    @Override
    public void diagnosticsClearCountersAndDiagnosticRegister(int serverAddress) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }

    @Override
    public int diagnosticsReturnBusMessageCount(int serverAddress) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }

    @Override
    public int diagnosticsReturnBusCommunicationErrorCount(int serverAddress) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }

    @Override
    public int diagnosticsReturnBusExceptionErrorCount(int serverAddress) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }

    @Override
    public int diagnosticsReturnSlaveMessageCount(int serverAddress) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }

    @Override
    public int diagnosticsReturnSlaveNoResponseCount(int serverAddress) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }

    @Override
    public int diagnosticsReturnSlaveNAKCount(int serverAddress) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }

    @Override
    public int diagnosticsReturnSlaveBusyCount(int serverAddress) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }

    @Override
    public int diagnosticsReturnBusCharacterOverrunCount(int serverAddress) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }

    @Override
    public void diagnosticsClearOverrunCounterAndFlag(int serverAddress) throws ModbusNumberException, IllegalFunctionException {
        throw new IllegalFunctionException(ModbusFunctionCode.DIAGNOSTICS.toInt());
    }
}
