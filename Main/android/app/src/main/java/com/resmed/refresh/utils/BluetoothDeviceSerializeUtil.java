package com.resmed.refresh.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import com.google.gson.Gson;
import com.resmed.refresh.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BluetoothDeviceSerializeUtil {
    public static boolean writeJsonFile(Context context, BluetoothDevice bluetoothDevice) {
        String json = new Gson().toJson((Object) bluetoothDevice);
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(RefreshTools.getFilesPath(), "bluetooth_device_file_name"));
            outputStream.write(json.getBytes());
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static BluetoothDevice readJsonFile(Context context) {
        FileInputStream fis = null;
        BluetoothDevice mDevice = null;
        try {
            FileInputStream fis2 = new FileInputStream(new File(RefreshTools.getFilesPath(), "bluetooth_device_file_name"));
            try {
                StringBuffer fileContent = new StringBuffer("");
                try {
                    byte[] buffer = new byte[1024];
                    while (true) {
                        int n = fis2.read(buffer);
                        if (n == -1) {
                            break;
                        }
                        fileContent.append(new String(buffer, 0, n));
                    }
                    mDevice = (BluetoothDevice) new Gson().fromJson(fileContent.toString(), BluetoothDevice.class);
                    if (fis2 != null) {
                        try {
                            fis2.close();
                            fis = fis2;
                        } catch (IOException e3) {
                            e3.printStackTrace();
                        }
                        BluetoothAdapter.getDefaultAdapter();
                        return mDevice;
                    }
                    fis = fis2;
                } catch (Throwable th2) {
                    fis = fis2;
                }
            } catch (Throwable th4) {
                fis = fis2;
                if (fis != null) {
                    fis.close();
                }
                throw new Error(th4);
            }
        } catch (Exception e8) {
			e8.printStackTrace();
            if (fis != null) {
				try {
					fis.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
        }
        BluetoothAdapter.getDefaultAdapter();
        return mDevice;
    }
}