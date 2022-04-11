package com.basisdas.jlibmodbusandroid.net.transport;

import com.basisdas.jlibmodbusandroid.net.stream.InputStreamASCII;
import com.basisdas.jlibmodbusandroid.net.stream.OutputStreamASCII;
import com.basisdas.jlibmodbusandroid.serial.SerialPort;

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
class ModbusTransportASCII extends ModbusTransportSerial {

    ModbusTransportASCII(SerialPort serial) {
        super(new InputStreamASCII(serial), new OutputStreamASCII(serial));
    }

}
