package com.basisdas.jlibmodbusandroid.serial;

import java.util.ArrayList;
import java.util.List;


public class SerialPortFactoryJSerialComm extends SerialPortAbstractFactory {

    public SerialPortFactoryJSerialComm() { }

    @Override
    public SerialPort createSerialImpl(SerialParameters sp) {
        return new SerialPortJSerialComm(sp);
    }

    @Override
    public List<String> getPortIdentifiersImpl() {
        com.fazecast.jSerialComm.SerialPort[] ports = com.fazecast.jSerialComm.SerialPort.getCommPorts();
        List<String> portIdentifiers = new ArrayList<String>(ports.length);
        for (int i = 0; i < ports.length; i++) {
            portIdentifiers.add(ports[i].getSystemPortName());
        }
        return portIdentifiers;
    }
}
