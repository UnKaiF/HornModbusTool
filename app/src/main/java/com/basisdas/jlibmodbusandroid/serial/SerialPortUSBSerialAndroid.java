package com.basisdas.jlibmodbusandroid.serial;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.basisdas.jlibmodbusandroid.serial.util.AndroidContextHelper;
import com.basisdas.jlibmodbusandroid.serial.util.AndroidUSBSerialPortResolver;
import com.basisdas.jlibmodbusandroid.serial.util.CircularByteBuffer;
import com.hoho.android.usbserial.driver.UsbSerialDriver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPortUSBSerialAndroid extends SerialPort
	{

	private final Context appContext;
	private final UsbManager manager;
	private com.hoho.android.usbserial.driver.UsbSerialPort port;

	private final CircularByteBuffer circularBuffer;
	private InputStream cirBufInputStream;
	private OutputStream cirBufOutputStream;

	final byte[] b = new byte[1];
	final byte[] bytes = new byte[0xffff];
	private int bitsPerUARTPacket;

	public SerialPortUSBSerialAndroid(SerialParameters sp)
		{
		super(sp);
		manager = AndroidContextHelper.getInstance().getUsbManager();
		appContext = AndroidContextHelper.getInstance().getAppContext();
		circularBuffer = new CircularByteBuffer(0xffff, false);
		}

	private int calculateWriteTimeout(int bytes)
		{
		//20 milliseconds minimum + double of time required for UART protocol
		return 20 + (bitsPerUARTPacket *  2000 * bytes) / getSerialParameters().getBaudRate();
		}

	@Override
	public void write(int b) throws IOException
		{
		this.b[0] = (byte) b;
		if (!isOpened())
			{
			throw new IOException("Port not opened");
			}
		try
			{
			port.write(this.b, calculateWriteTimeout(this.b.length));
			}
		catch (Exception e)
			{
			throw new IOException(e);
			}
		}

	@Override
	public void write(byte[] _bytes) throws IOException
		{
		if (!isOpened())
			{
			throw new IOException("Port not opened");
			}
		try
			{
			port.write(_bytes, calculateWriteTimeout(_bytes.length));
			}
		catch (Exception e)
			{
			throw new IOException(e);
			}
		}


	@Override
	public void open() throws SerialPortException
		{
		SerialParameters sp = getSerialParameters();
		UsbDeviceConnection connection = null;
		UsbSerialDriver driver = AndroidUSBSerialPortResolver.getUsbSerialDriver(sp.getDevice(), appContext);
		if (driver == null)
			throw new SerialPortException("USB device not found : " + sp.getDevice());

		if (manager.hasPermission(driver.getDevice()))
			connection = manager.openDevice(driver.getDevice());
		if (connection == null)
			{
			throw new SerialPortException("No permissions to open USB device: " + sp.getDevice());
			}

		port = driver.getPorts().get(AndroidUSBSerialPortResolver.getPortNumber(sp.getDevice()));

		try
			{
			port.open(connection);
			port.setParameters(sp.getBaudRate(), sp.getDataBits(), sp.getStopBits(), sp.getParity().getValue());
			}
		catch (Exception e)
			{
			throw new SerialPortException(e);
			}

		bitsPerUARTPacket = 1 + sp.getDataBits() + sp.getStopBits() + (sp.getParity().getValue() == 0 ? 0 : 1);
		cirBufInputStream = circularBuffer.getInputStream();
		cirBufOutputStream = circularBuffer.getOutputStream();
		}


	@Override
	public int read() throws IOException
		{
		if (!isOpened())
			throw new IOException("Port not opened");

		int  readedByteCount;

		if (cirBufInputStream.available() != 0)
			{
			cirBufInputStream.read(b, 0, 1);
			return b[0];
			}
		else
			{
			try
				{
				readedByteCount = port.read(bytes, getReadTimeout());
				}
			catch (Exception e)
				{
				throw new IOException(e);
				}
			if (readedByteCount > 0)
				{
				cirBufOutputStream.write(bytes, 0, readedByteCount);
				cirBufInputStream.read(b, 0, 1);
				return b[0];
				}
			else
				throw new IOException("Read timeout");
			}
		}

	@Override
	public int read(byte[] b, int off, int len) throws IOException
		{
		if (!isOpened())
			{
			throw new IOException("Port not opened");
			}
		int  readedByteCount;

		if (cirBufInputStream.available() >= len)
			{
			return cirBufInputStream.read(b, off, len);
			}
		else
			{
			try
				{
				readedByteCount = port.read(bytes, getReadTimeout());
				}
			catch (Exception e)
				{
				throw new IOException(e);
				}
			if (readedByteCount > 0)
				{
				cirBufOutputStream.write(bytes, 0, readedByteCount);
				return cirBufInputStream.read(b, off, Math.min(cirBufInputStream.available(), len));
				}
			else
				throw new IOException("Read timeout");
			}
		}

	@Override
	public void close()
		{
		try
			{
			if (isOpened())
				{
				cirBufOutputStream.close();//Only in this order !!!
				cirBufInputStream.close();
				port.close();
				}
			}
		catch (Exception e)
			{
			e.printStackTrace();
			}
		}

	@Override
	public boolean isOpened()
		{
		return port.isOpen();
		}
	}
