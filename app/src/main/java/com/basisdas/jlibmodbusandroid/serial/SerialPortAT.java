package com.basisdas.jlibmodbusandroid.serial;

import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.UartDevice;
import com.basisdas.jlibmodbusandroid.Modbus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;



public class SerialPortAT extends SerialPort {

    private UartDevice port;
    byte[] buffer = new byte[Modbus.MAX_PDU_LENGTH];
    volatile long readTimeNs = 0;

    public SerialPortAT(SerialParameters sp) {
        super(sp);
        port = null;
    }

    @Override
    public void write(int b) throws IOException {
        try {
            port.write(new byte[]{(byte)(b&0xff)}, 1);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        try {
            port.write(bytes, bytes.length);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void open() throws SerialPortException {
        try {
            SerialParameters sp = getSerialParameters();
            port = PeripheralManager.getInstance().openUartDevice(sp.getDevice());
            port.setBaudrate(sp.getBaudRate());
            port.setDataSize(sp.getDataBits());
            port.setParity(sp.getParity().getValue());
            port.setStopBits(sp.getStopBits());
            port.setHardwareFlowControl(UartDevice.HW_FLOW_CONTROL_NONE);
        } catch (Exception ex) {
            throw new SerialPortException(ex);
        }
    }

    @Override
    public int read() throws IOException {
        try {
            readTimeNs = System.nanoTime();
            while (port.read(buffer, 1) < 1) {
                if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - readTimeNs) > getReadTimeout()) {
                    throw new IOException("Read timeout");
                }
            }
            return buffer[0];
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public int read(byte[] b, int off, final int len) throws IOException {
        try {
            readTimeNs = System.nanoTime();
            int read;
            int count = 0;

            int length = len-off;

            if (length < 0 || b.length < off+len)
                throw new ArrayIndexOutOfBoundsException("len + off is greater than b.length");

            while (count < length) {
                read = port.read(buffer, length-count);
                count += read;
                if (count > length) {
                    int diff = count - length;
                    read -= diff;
                    count -= diff;
                }
                System.arraycopy(buffer, 0, b, off, read);
                off += read;

                if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - readTimeNs) > getReadTimeout()) {
                    throw new IOException("Read timeout");
                }
            }
            return count;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void close() {
        if (isOpened()) {
            try {
                synchronized (this) {
                    port.close();
                    port = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    synchronized public boolean isOpened() {
        return port != null;
    }
}
