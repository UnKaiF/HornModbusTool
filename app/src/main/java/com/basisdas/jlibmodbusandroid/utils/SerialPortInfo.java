package com.basisdas.jlibmodbusandroid.utils;

import com.basisdas.jlibmodbusandroid.serial.SerialParameters;

public class SerialPortInfo {
    volatile boolean isOpened = false;
    private SerialParameters serialParameters = new SerialParameters();

    public SerialPortInfo(SerialParameters serialParameters) {
        this.serialParameters = serialParameters;
    }

    public SerialParameters getSerialParameters() {
        return serialParameters;
    }

    public void setSerialParameters(SerialParameters serialParameters) {
        this.serialParameters = serialParameters;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }
}
