package de.wuespace.telestion.tool.connector.serial;

import com.rm5248.serial.SerialPort;

import java.io.IOException;
import java.util.Arrays;

public class App {

    public static void main(String[] args) throws IOException {
        System.out.println(Arrays.toString(SerialPort.getSerialPorts()));
    }
}
