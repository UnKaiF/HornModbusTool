package com.basisdas.jlibmodbusandroid.msg.request;

import com.basisdas.jlibmodbusandroid.Modbus;
import com.basisdas.jlibmodbusandroid.data.DataHolder;
import com.basisdas.jlibmodbusandroid.exception.ModbusNumberException;
import com.basisdas.jlibmodbusandroid.exception.ModbusProtocolException;
import com.basisdas.jlibmodbusandroid.msg.base.AbstractDataRequest;
import com.basisdas.jlibmodbusandroid.msg.base.ModbusResponse;
import com.basisdas.jlibmodbusandroid.msg.response.WriteSingleRegisterResponse;
import com.basisdas.jlibmodbusandroid.net.stream.base.ModbusInputStream;
import com.basisdas.jlibmodbusandroid.net.stream.base.ModbusOutputStream;
import com.basisdas.jlibmodbusandroid.utils.ModbusFunctionCode;

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
public class WriteSingleRegisterRequest extends AbstractDataRequest {

    private int value;

    public WriteSingleRegisterRequest() {
        super();
    }

    @Override
    protected Class getResponseClass() {
        return WriteSingleRegisterResponse.class;
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        WriteSingleRegisterResponse response = (WriteSingleRegisterResponse) getResponse();
        response.setStartAddress(getStartAddress());
        response.setValue(getValue());
        try {
            dataHolder.writeHoldingRegister(getStartAddress(), getValue());
        } catch (ModbusProtocolException e) {
            response.setException();
            response.setModbusExceptionCode(e.getException().getValue());
        }
        return response;
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
        if (!(response instanceof WriteSingleRegisterResponse)) {
            return false;
        }
        WriteSingleRegisterResponse r = (WriteSingleRegisterResponse) response;
        return r.getStartAddress() == getStartAddress() && r.getValue() == getValue();
    }

    @Override
    final protected void readData(ModbusInputStream fifo) throws IOException {
        try {
            setValue(fifo.readShortBE());
        } catch (ModbusNumberException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.WRITE_SINGLE_REGISTER.toInt();
    }

    @Override
    final public void writeData(ModbusOutputStream fifo) throws IOException {
        fifo.writeShortBE(getValue());
    }

    @Override
    protected int dataSize() {
        return 2;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) throws ModbusNumberException {
        if (!Modbus.checkRegisterValue(value)) {
            throw new ModbusNumberException("Register value out of range", value);
        }
        this.value = ((short) value) & 0xffff;
    }
}
