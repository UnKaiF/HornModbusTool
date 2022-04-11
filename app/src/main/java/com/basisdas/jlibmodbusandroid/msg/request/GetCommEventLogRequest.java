package com.basisdas.jlibmodbusandroid.msg.request;

import com.basisdas.jlibmodbusandroid.data.CommStatus;
import com.basisdas.jlibmodbusandroid.data.DataHolder;
import com.basisdas.jlibmodbusandroid.exception.ModbusNumberException;
import com.basisdas.jlibmodbusandroid.msg.base.ModbusRequest;
import com.basisdas.jlibmodbusandroid.msg.base.ModbusResponse;
import com.basisdas.jlibmodbusandroid.msg.response.GetCommEventLogResponse;
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
final public class GetCommEventLogRequest extends ModbusRequest {

    public GetCommEventLogRequest() {
        super();
    }

    @Override
    protected Class getResponseClass() {
        return GetCommEventLogResponse.class;
    }

    @Override
    public void writeRequest(ModbusOutputStream fifo) throws IOException {
        //no op
    }

    @Override
    public int requestSize() {
        return 0;
    }

    @Override
    public ModbusResponse process(DataHolder dataHolder) throws ModbusNumberException {
        GetCommEventLogResponse response = new GetCommEventLogResponse();
        response.setServerAddress(getServerAddress());
        CommStatus commStatus = dataHolder.getCommStatus();
        response.setEventCount(commStatus.getEventCount());
        response.setStatus(commStatus.getCommStatus());
        response.setMessageCount(commStatus.getMessageCount());
        response.setEvents(commStatus.getEventLog());
        return response;
    }

    @Override
    public boolean validateResponseImpl(ModbusResponse response) {
        return true;
    }

    @Override
    public void readPDU(ModbusInputStream fifo) throws ModbusNumberException, IOException {
        //no op
    }

    @Override
    public int getFunction() {
        return ModbusFunctionCode.GET_COMM_EVENT_LOG.toInt();
    }
}
