package com.basisdas.jlibmodbusandroid.slave;

import com.basisdas.jlibmodbusandroid.net.ModbusConnectionFactory;
import com.basisdas.jlibmodbusandroid.serial.SerialParameters;
import com.basisdas.jlibmodbusandroid.serial.SerialPort;
import com.basisdas.jlibmodbusandroid.serial.SerialPortException;
import com.basisdas.jlibmodbusandroid.serial.SerialUtils;

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
public class ModbusSlaveASCII extends ModbusSlaveSerial {

    public ModbusSlaveASCII(SerialParameters sp) throws SerialPortException {
        super(sp, ModbusConnectionFactory.getASCII(SerialUtils.createSerial(sp)));
    }

    public ModbusSlaveASCII(String device, SerialPort.BaudRate baudRate, SerialPort.Parity parity) throws SerialPortException {
        this(new SerialParameters(device, baudRate, 7, parity == SerialPort.Parity.NONE ? 2 : 1, parity));
    }

    public ModbusSlaveASCII(String device, SerialPort.BaudRate baudRate) throws SerialPortException {
        this(device, baudRate, SerialPort.Parity.EVEN);
    }

}
