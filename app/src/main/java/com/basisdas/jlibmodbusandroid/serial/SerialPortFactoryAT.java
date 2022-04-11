package com.basisdas.jlibmodbusandroid.serial;

import com.google.android.things.AndroidThings;
import com.google.android.things.pio.PeripheralManager;

import java.util.List;


public class SerialPortFactoryAT extends SerialPortAbstractFactory {

    public SerialPortFactoryAT() {
    }

    @Override
    public SerialPort createSerialImpl(SerialParameters sp) {
        return new SerialPortAT(sp);
    }

    @Override
    public List<String> getPortIdentifiersImpl() {
        final PeripheralManager pm = PeripheralManager.getInstance();
        return pm.getUartDeviceList();
    }

    @Override
    public String getVersion() {
        try {
            return AndroidThings.RELEASE;
        } catch (Exception e) {
            return super.getVersion();
        }
    }
}
