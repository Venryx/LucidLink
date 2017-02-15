package com.resmed.refresh.utils;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import com.google.gson.Gson;
import com.resmed.refresh.R;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class BluetoothDataSerializeUtil {
    public static boolean clearJSONFile(Context context) {
        new File(RefreshTools.getFilesPath(), context.getString(R.string.bluetooth_device_file_name)).delete();
        return true;
    }

    public static synchronized boolean writeBulkBioDataFile(Context context, byte[] buffer) {
        synchronized (BluetoothDataSerializeUtil.class) {
            File btFile = new File(RefreshTools.getFilesPath(), "bluetooth_buffer_bulk_file");
            try {
                if (!btFile.exists()) {
                    btFile.createNewFile();
                }
                FileWriter outputWriter = new FileWriter(btFile, true);
                int[][] bioData = PacketsByteValuesReader.readBioData(buffer, buffer.length);
                for (int i = 0; i < bioData[0].length; i++) {
                    int mi = bioData[0][i];
                    outputWriter.append(new StringBuilder(String.valueOf(mi)).append(" ").append(bioData[1][i]).append("\n").toString());
                }
                outputWriter.flush();
                outputWriter.close();
				return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
				return false;
            } catch (IOException e2) {
                e2.printStackTrace();
				return false;
			}
        }
    }

    public static synchronized int[][] readBulkBioDataFile(Context context) {
        FileNotFoundException e;
        IOException e2;
        Throwable th;
        int i;
        int[][] bioData;
        synchronized (BluetoothDataSerializeUtil.class) {
            int[] miNativeValues;
            int[] mqNativeValues;
            File btFile = new File(RefreshTools.getFilesPath(), "bluetooth_buffer_bulk_file");
            List<Integer> miValues = new ArrayList();
            List<Integer> mqValues = new ArrayList();
            BufferedReader br = null;
            try {
                BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(btFile)));
                int n = 0;
                while (true) {
                    try {
                        String strLine = br2.readLine();
                        if (strLine == null) {
                            break;
                        }
                        n++;
                        String[] tokens = strLine.trim().split("\\s+");
                        int mi = Integer.parseInt(tokens[0]);
                        int mq = Integer.parseInt(tokens[1]);
                        miValues.add(Integer.valueOf(mi));
                        mqValues.add(Integer.valueOf(mq));
                    } catch (FileNotFoundException e3) {
                        e = e3;
                        br = br2;
                    } catch (IOException e4) {
                        e2 = e4;
                        br = br2;
                    } catch (Throwable th2) {
                        th = th2;
                        br = br2;
                    }
                }
                if (br2 != null) {
                    try {
                        br2.close();
                        br = br2;
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                    miNativeValues = new int[miValues.size()];
                    mqNativeValues = new int[mqValues.size()];
                    for (i = 0; i < miValues.size(); i++) {
                        miNativeValues[i] = ((Integer) miValues.get(i)).intValue();
                        mqNativeValues[i] = ((Integer) mqValues.get(i)).intValue();
                    }
                    bioData = (int[][]) Array.newInstance(Integer.TYPE, new int[]{2, miNativeValues.length});
                    bioData[0] = miNativeValues;
                    bioData[1] = mqNativeValues;
                }
                br = br2;
            } catch (FileNotFoundException e5) {
                e = e5;
                try {
                    e.printStackTrace();
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    miNativeValues = new int[miValues.size()];
                    mqNativeValues = new int[mqValues.size()];
                    for (i = 0; i < miValues.size(); i++) {
                        miNativeValues[i] = ((Integer) miValues.get(i)).intValue();
                        mqNativeValues[i] = ((Integer) mqValues.get(i)).intValue();
                    }
                    bioData = (int[][]) Array.newInstance(Integer.TYPE, new int[]{2, miNativeValues.length});
                    bioData[0] = miNativeValues;
                    bioData[1] = mqNativeValues;
                    return bioData;
                } catch (Throwable th3) {
                    th = th3;
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e2222) {
                            e2222.printStackTrace();
                        }
                    }
                    throw new Error(th);
                }
            }
            miNativeValues = new int[miValues.size()];
            mqNativeValues = new int[mqValues.size()];
            for (i = 0; i < miValues.size(); i++) {
                miNativeValues[i] = ((Integer) miValues.get(i)).intValue();
                mqNativeValues[i] = ((Integer) mqValues.get(i)).intValue();
            }
            bioData = (int[][]) Array.newInstance(Integer.TYPE, new int[]{2, miNativeValues.length});
            bioData[0] = miNativeValues;
            bioData[1] = mqNativeValues;
        }
        return bioData;
    }

    public static boolean deleteBulkDataBioFile(Context context) {
        return new File(RefreshTools.getFilesPath(), "bluetooth_buffer_bulk_file").delete();
    }

    public static boolean writeJsonFile(Context context, BluetoothDevice bluetoothDevice) {
        FileNotFoundException e;
        FileOutputStream fileOutputStream;
        IOException e2;
        if (bluetoothDevice == null) {
            return false;
        }
        String json = new Gson().toJson((Object) bluetoothDevice);
        try {
            File btFile = new File(RefreshTools.getFilesPath(), "bluetooth_device_file_name");
            if (!btFile.exists()) {
                btFile.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(btFile);
            try {
                outputStream.write(json.getBytes());
                outputStream.flush();
                outputStream.close();
                return true;
            } catch (FileNotFoundException e3) {
                e = e3;
                fileOutputStream = outputStream;
                e.printStackTrace();
                return false;
            } catch (IOException e4) {
                e2 = e4;
                fileOutputStream = outputStream;
                e2.printStackTrace();
                return false;
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
            return false;
        } catch (IOException e6) {
            e2 = e6;
            e2.printStackTrace();
            return false;
        }
    }

    public static boolean deleteJsonFile(Context context) {
        try {
            new File(RefreshTools.getFilesPath(), "bluetooth_device_file_name").delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static BluetoothDevice readJsonFile(Context context) {
        BluetoothDevice mDevice;
        FileInputStream fis = null;
        try {
            FileInputStream fis2 = new FileInputStream(new File(RefreshTools.getFilesPath(), "bluetooth_device_file_name"));
			StringBuffer fileContent = new StringBuffer("");
			byte[] buffer = new byte[1024];
			while (true) {
				int n = fis2.read(buffer);
				if (n == -1) {
					break;
				}
				fileContent.append(new String(buffer, 0, n));
			}
			mDevice = new Gson().fromJson(fileContent.toString(), BluetoothDevice.class);
			if (fis2 != null) {
				try {
					fis2.close();
					fis = fis2;
				} catch (IOException e4) {
					e4.printStackTrace();
				}
				return mDevice;
			}
			fis = fis2;
			return mDevice;
		} catch (Exception e11) {
			e11.printStackTrace();
            if (fis != null) {
				try {
					fis.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			return null;
        }
    }
}