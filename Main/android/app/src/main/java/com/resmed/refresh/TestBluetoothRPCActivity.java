package com.resmed.refresh;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Messenger;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.resmed.refresh.bed.LedsState;
import com.resmed.refresh.model.json.JsonRPC;
import com.resmed.refresh.packets.PacketsByteValuesReader;
import com.resmed.refresh.packets.VLPacketType;
import com.resmed.refresh.ui.uibase.base.BaseBluetoothActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class TestBluetoothRPCActivity
  extends BaseBluetoothActivity
{
  private int bio_count_current = 0;
  private Button closeBtn;
  private View.OnClickListener closeSessionOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      TestBluetoothRPCActivity.this.textArea.setText(" Waiting for response... \n");
      paramAnonymousView = TestBluetoothRPCActivity.getRpcCommands().reset();
      TestBluetoothRPCActivity.this.sendRpcToBed(paramAnonymousView);
    }
  };
  private TimeSeries currentTimeSeries;
  private Button ledFlashBtn;
  private View.OnClickListener ledFlashOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      TestBluetoothRPCActivity.this.textArea.setText(" Waiting for response... \n");
      paramAnonymousView = TestBluetoothRPCActivity.getRpcCommands().leds(LedsState.GREENFLASH);
      TestBluetoothRPCActivity.this.sendRpcToBed(paramAnonymousView);
    }
  };
  private Button ledNoFlashBtn;
  private View.OnClickListener ledNoFlashOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      TestBluetoothRPCActivity.this.textArea.setText(" Waiting for response... \n");
      paramAnonymousView = TestBluetoothRPCActivity.getRpcCommands().leds(LedsState.RED);
      TestBluetoothRPCActivity.this.sendRpcToBed(paramAnonymousView);
    }
  };
  private GraphicalView mChartView;
  private XYMultipleSeriesDataset mDataset;
  private TextView mFirmwareTv;
  final Messenger mMessenger = new Messenger(new BaseBluetoothActivity.IncomingHandler(this));
  private XYMultipleSeriesRenderer mRenderer;
  private ProgressBar mSpinner;
  private Button nightStreamStopBtn;
  private View.OnClickListener nightStreamStopOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      TestBluetoothRPCActivity.this.textArea.setText(" Waiting for response... \n");
      paramAnonymousView = TestBluetoothRPCActivity.getRpcCommands().stopNightTimeTracking();
      TestBluetoothRPCActivity.this.sendRpcToBed(paramAnonymousView);
    }
  };
  private Button nightStreamingBtn;
  private View.OnClickListener nightStreamingOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      TestBluetoothRPCActivity.this.textArea.setText(" Waiting for response... \n");
      paramAnonymousView = TestBluetoothRPCActivity.getRpcCommands().startNightTracking();
      TestBluetoothRPCActivity.this.sendRpcToBed(paramAnonymousView);
    }
  };
  private Button openSessionBtn;
  private View.OnClickListener openSessionOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      TestBluetoothRPCActivity.this.textArea.setText(" Waiting for response... \n");
      paramAnonymousView = TestBluetoothRPCActivity.getRpcCommands().openSession("c63eb080-a864-11e3-a5e2-0800200c9a66");
      TestBluetoothRPCActivity.this.sendRpcToBed(paramAnonymousView);
    }
  };
  private Button realStreamStopBtn;
  private View.OnClickListener realStreamStopOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      TestBluetoothRPCActivity.this.textArea.setText(" Waiting for response... \n");
      paramAnonymousView = TestBluetoothRPCActivity.getRpcCommands().stopRealTimeStream();
      TestBluetoothRPCActivity.this.sendRpcToBed(paramAnonymousView);
    }
  };
  private Button realStreamingBtn;
  private View.OnClickListener realStreamingOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      TestBluetoothRPCActivity.this.textArea.setText(" Waiting for response... \n");
      paramAnonymousView = TestBluetoothRPCActivity.getRpcCommands().startRealTimeStream();
      TestBluetoothRPCActivity.this.sendRpcToBed(paramAnonymousView);
      paramAnonymousView = TestBluetoothRPCActivity.getRpcCommands().leds(LedsState.GREEN);
      TestBluetoothRPCActivity.this.sendRpcToBed(paramAnonymousView);
    }
  };
  private Button sampleCountBtn;
  private View.OnClickListener sampleCountBtnOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      TestBluetoothRPCActivity.this.textArea.setText(" Waiting for response... \n");
      paramAnonymousView = TestBluetoothRPCActivity.getRpcCommands().getSampleNumber(true);
      TestBluetoothRPCActivity.this.sendRpcToBed(paramAnonymousView);
    }
  };
  private Button serialNumberBtn;
  private View.OnClickListener serialNumberOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      TestBluetoothRPCActivity.this.textArea.setText(" Waiting for response... \n");
      paramAnonymousView = TestBluetoothRPCActivity.getRpcCommands().getSerialNumber();
      TestBluetoothRPCActivity.this.sendRpcToBed(paramAnonymousView);
    }
  };
  private EditText textArea;
  private Button transmitPacket;
  private View.OnClickListener transmitPacketOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      TestBluetoothRPCActivity.this.textArea.setText(" Waiting for response... \n");
      paramAnonymousView = TestBluetoothRPCActivity.getRpcCommands().transmitPacket(65535, false, false);
      TestBluetoothRPCActivity.this.sendRpcToBed(paramAnonymousView);
    }
  };
  
  private void handleBioToGraph(byte[] paramArrayOfByte)
  {
    paramArrayOfByte = PacketsByteValuesReader.readBioData(paramArrayOfByte);
    this.currentTimeSeries.add(this.bio_count_current, paramArrayOfByte[0]);
    this.bio_count_current += 1;
    if (this.bio_count_current + 100 > this.mRenderer.getXAxisMax())
    {
      this.mRenderer.setXAxisMax(this.mRenderer.getXAxisMax() + 1.0D);
      this.mRenderer.setXAxisMin(this.mRenderer.getXAxisMin() + 1.0D);
    }
  }
  
  private void handleDataToFile(byte[] paramArrayOfByte)
  {
    localObject = null;
    try
    {
      FileOutputStream localFileOutputStream = openFileOutput("samples.edf", 0);
      localObject = localFileOutputStream;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      for (;;)
      {
        try
        {
          localObject = openFileInput("samples.edf");
          paramArrayOfByte = new java/io/InputStreamReader;
          paramArrayOfByte.<init>((InputStream)localObject);
          localObject = new java/io/BufferedReader;
          ((BufferedReader)localObject).<init>(paramArrayOfByte);
        }
        catch (FileNotFoundException paramArrayOfByte)
        {
          StringBuilder localStringBuilder;
          paramArrayOfByte.printStackTrace();
          continue;
        }
        try
        {
          paramArrayOfByte = ((BufferedReader)localObject).readLine();
          if (paramArrayOfByte != null) {
            continue;
          }
          return;
        }
        catch (IOException paramArrayOfByte)
        {
          paramArrayOfByte.printStackTrace();
          continue;
        }
        localFileNotFoundException = localFileNotFoundException;
        localFileNotFoundException.printStackTrace();
        continue;
        localStringBuilder = new java.lang.StringBuilder;
        localStringBuilder.<init>("EDF FILE LINE : ");
        Log.d("com.resmed.refresh", paramArrayOfByte);
        paramArrayOfByte = ((BufferedReader)localObject).readLine();
      }
    }
    PacketsByteValuesReader.saveToEDF(paramArrayOfByte, (FileOutputStream)localObject);
  }
  
  private void handleLocalStore(byte[] paramArrayOfByte)
  {
    Log.d("com.resmed.refresh", " bio sample count : " + PacketsByteValuesReader.getStoreLocalBio(paramArrayOfByte));
    Log.d("com.resmed.refresh", " env sample count : " + PacketsByteValuesReader.getStoreLocalEnv(paramArrayOfByte));
  }
  
  private void handleNoteEnv(byte[] paramArrayOfByte)
  {
    ((TextView)findViewById(2131100723)).setText(Integer.toString(PacketsByteValuesReader.readIlluminanceValue(paramArrayOfByte)));
    ((TextView)findViewById(2131100724)).setText(Float.toString(PacketsByteValuesReader.readTemperatureValue(paramArrayOfByte)));
  }
  
  /* Error */
  public void handleConnectionStatus(com.resmed.refresh.bluetooth.CONNECTION_STATE paramCONNECTION_STATE)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokespecial 268	com/resmed/refresh/ui/uibase/base/BaseBluetoothActivity:handleConnectionStatus	(Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;)V
    //   7: getstatic 274	com/resmed/refresh/bluetooth/CONNECTION_STATE:SOCKET_CONNECTED	Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;
    //   10: aload_1
    //   11: if_acmpne +14 -> 25
    //   14: aload_0
    //   15: getfield 276	com/resmed/refresh/TestBluetoothRPCActivity:mSpinner	Landroid/widget/ProgressBar;
    //   18: iconst_4
    //   19: invokevirtual 282	android/widget/ProgressBar:setVisibility	(I)V
    //   22: aload_0
    //   23: monitorexit
    //   24: return
    //   25: getstatic 285	com/resmed/refresh/bluetooth/CONNECTION_STATE:SOCKET_BROKEN	Lcom/resmed/refresh/bluetooth/CONNECTION_STATE;
    //   28: aload_1
    //   29: if_acmpne -7 -> 22
    //   32: aload_0
    //   33: getfield 276	com/resmed/refresh/TestBluetoothRPCActivity:mSpinner	Landroid/widget/ProgressBar;
    //   36: iconst_0
    //   37: invokevirtual 282	android/widget/ProgressBar:setVisibility	(I)V
    //   40: goto -18 -> 22
    //   43: astore_1
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_1
    //   47: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	48	0	this	TestBluetoothRPCActivity
    //   0	48	1	paramCONNECTION_STATE	com.resmed.refresh.bluetooth.CONNECTION_STATE
    // Exception table:
    //   from	to	target	type
    //   2	22	43	finally
    //   25	40	43	finally
  }
  
  public void handleReceivedRpc(JsonRPC paramJsonRPC)
  {
    super.handleReceivedRpc(paramJsonRPC);
    if (paramJsonRPC != null)
    {
      paramJsonRPC = paramJsonRPC.getResult();
      if (paramJsonRPC != null)
      {
        paramJsonRPC = paramJsonRPC.getPayload();
        if (paramJsonRPC.contains("$"))
        {
          paramJsonRPC = paramJsonRPC.substring(paramJsonRPC.indexOf("$") + 1, paramJsonRPC.length() - 1);
          this.mFirmwareTv.setText(paramJsonRPC);
          checkForFirmwareUpgrade(paramJsonRPC);
        }
      }
    }
  }
  
  public void handleStreamPacket(Bundle paramBundle)
  {
    super.handleStreamPacket(paramBundle);
    byte[] arrayOfByte = paramBundle.getByteArray("REFRESH_BED_NEW_DATA");
    int i = paramBundle.getByte("REFRESH_BED_NEW_DATA_TYPE");
    if (VLPacketType.PACKET_TYPE_NOTE_ENV_1.ordinal() == i) {
      handleNoteEnv(arrayOfByte);
    }
    for (;;)
    {
      return;
      if (VLPacketType.PACKET_TYPE_NOTE_STORE_LOCAL.ordinal() == i)
      {
        handleLocalStore(arrayOfByte);
      }
      else if (VLPacketType.PACKET_TYPE_NOTE_BIO_1.ordinal() == i)
      {
        handleBioToGraph(arrayOfByte);
        this.mChartView.repaint();
      }
      else if (VLPacketType.PACKET_TYPE_ENV_1.ordinal() == i)
      {
        handleNoteEnv(arrayOfByte);
      }
      else if (VLPacketType.PACKET_TYPE_NOTE_ILLUMINANCE_CHANGE.ordinal() == i)
      {
        handleNoteEnv(arrayOfByte);
      }
      else if (VLPacketType.PACKET_TYPE_BIO_32.ordinal() == i)
      {
        handleDataToFile(arrayOfByte);
      }
      else if (VLPacketType.PACKET_TYPE_RETURN.ordinal() == i)
      {
        this.textArea.setText(this.textArea.getText().toString() + "\n" + new String(arrayOfByte));
      }
    }
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    setContentView(2130903213);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903213);
    this.openSessionBtn = ((Button)findViewById(2131100705));
    this.serialNumberBtn = ((Button)findViewById(2131100720));
    this.nightStreamingBtn = ((Button)findViewById(2131100714));
    this.nightStreamStopBtn = ((Button)findViewById(2131100715));
    this.realStreamingBtn = ((Button)findViewById(2131100711));
    this.realStreamStopBtn = ((Button)findViewById(2131100712));
    this.sampleCountBtn = ((Button)findViewById(2131100719));
    this.ledFlashBtn = ((Button)findViewById(2131100708));
    this.closeBtn = ((Button)findViewById(2131100706));
    this.ledNoFlashBtn = ((Button)findViewById(2131100709));
    this.textArea = ((EditText)findViewById(2131100728));
    this.transmitPacket = ((Button)findViewById(2131100718));
    this.mSpinner = ((ProgressBar)findViewById(2131100721));
    this.mFirmwareTv = ((TextView)findViewById(2131100729));
    this.openSessionBtn.setOnClickListener(this.openSessionOnClickListener);
    this.serialNumberBtn.setOnClickListener(this.serialNumberOnClickListener);
    this.nightStreamingBtn.setOnClickListener(this.nightStreamingOnClickListener);
    this.nightStreamStopBtn.setOnClickListener(this.nightStreamStopOnClickListener);
    this.realStreamingBtn.setOnClickListener(this.realStreamingOnClickListener);
    this.realStreamStopBtn.setOnClickListener(this.realStreamStopOnClickListener);
    this.sampleCountBtn.setOnClickListener(this.sampleCountBtnOnClickListener);
    this.ledFlashBtn.setOnClickListener(this.ledFlashOnClickListener);
    this.closeBtn.setOnClickListener(this.closeSessionOnClickListener);
    this.ledNoFlashBtn.setOnClickListener(this.ledNoFlashOnClickListener);
    this.transmitPacket.setOnClickListener(this.transmitPacketOnClickListener);
    getWindow().setSoftInputMode(3);
    ((ScrollView)findViewById(2131100727)).setBackgroundColor(-7829368);
    if (this.mChartView == null)
    {
      paramBundle = (LinearLayout)findViewById(2131100725);
      this.mDataset = new XYMultipleSeriesDataset();
      this.mRenderer = new XYMultipleSeriesRenderer();
      this.mRenderer.setChartTitle("Real Time");
      this.mRenderer.setBackgroundColor(-16777216);
      this.mRenderer.setAxesColor(65280);
      this.mRenderer.setXAxisMax(200.0D);
      this.mRenderer.setYAxisMax(2500.0D);
      this.mRenderer.setXAxisMin(0.0D);
      this.mRenderer.setYAxisMin(500.0D);
      this.mRenderer.setScale(1.0F);
      this.mRenderer.setPanEnabled(true);
      this.mRenderer.setClickEnabled(false);
      this.currentTimeSeries = new TimeSeries("");
      this.mDataset.addSeries(this.currentTimeSeries);
      XYSeriesRenderer localXYSeriesRenderer = new XYSeriesRenderer();
      localXYSeriesRenderer.setColor(-1);
      localXYSeriesRenderer.setLineWidth(4.0F);
      localXYSeriesRenderer.setFillPoints(true);
      localXYSeriesRenderer.setDisplayChartValues(true);
      this.mRenderer.addSeriesRenderer(localXYSeriesRenderer);
      this.mChartView = ChartFactory.getLineChartView(this, this.mDataset, this.mRenderer);
      this.mChartView.setBackgroundColor(-16777216);
      paramBundle.addView(this.mChartView, new ActionBar.LayoutParams(-2, -2));
    }
    for (;;)
    {
      return;
      this.mChartView.repaint();
    }
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    boolean bool = isBluetoothServiceRunning();
    Log.d("com.resmed.refresh", ": bluetooth service is running : " + bool);
    if (bool) {
      stopService(this.bluetoothManagerService);
    }
  }
  
  protected void onResume()
  {
    super.onResume();
    this.textArea.setBackgroundColor(-7829368);
    this.textArea.setEnabled(false);
    this.closeBtn.setVisibility(4);
    connectToBeD(true);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\TestBluetoothRPCActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */