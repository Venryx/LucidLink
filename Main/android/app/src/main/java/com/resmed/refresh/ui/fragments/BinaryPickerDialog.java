package com.resmed.refresh.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.resmed.refresh.utils.Log;
import java.util.ArrayList;
import java.util.List;

public class BinaryPickerDialog
  extends DialogFragment
{
  public static String TAG = "BinaryPickerDialog";
  private BinaryPickHandler binaryHandler;
  private List<String> binaryList;
  private BinaryAdapter listAdapter;
  private ListView listView;
  
  public BinaryPickerDialog(BinaryPickHandler paramBinaryPickHandler, String[] paramArrayOfString)
  {
    this.binaryHandler = paramBinaryPickHandler;
    this.binaryList = new ArrayList();
    for (int i = 0;; i++)
    {
      if (i >= paramArrayOfString.length) {
        return;
      }
      Log.d("com.resmed.refresh.ui", "binary : " + paramArrayOfString[i]);
      if (paramArrayOfString[i].contains("BeD_")) {
        this.binaryList.add(paramArrayOfString[i]);
      }
    }
  }
  
  public Dialog onCreateDialog(final Bundle paramBundle)
  {
    paramBundle = new Dialog(getActivity(), 16973833);
    View localView = getActivity().getLayoutInflater().inflate(2130903208, null);
    paramBundle.addContentView(localView, new ViewGroup.LayoutParams(-2, -2));
    this.listView = ((ListView)localView.findViewById(2131100695));
    this.listAdapter = new BinaryAdapter(this.binaryList, null);
    this.listView.setAdapter(this.listAdapter);
    this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        paramAnonymousAdapterView = (String)BinaryPickerDialog.this.binaryList.get(paramAnonymousInt);
        BinaryPickerDialog.this.binaryHandler.handleOnBinaryPicked(paramAnonymousAdapterView);
        paramBundle.dismiss();
      }
    });
    return paramBundle;
  }
  
  private class BinaryAdapter
    extends BaseAdapter
  {
    private List<String> list;
    
    private BinaryAdapter()
    {
      List localList;
      this.list = localList;
    }
    
    public int getCount()
    {
      return this.list.size();
    }
    
    public Object getItem(int paramInt)
    {
      return this.list.get(paramInt);
    }
    
    public long getItemId(int paramInt)
    {
      return paramInt;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      paramViewGroup = paramView;
      if (paramView == null) {
        paramViewGroup = ((LayoutInflater)BinaryPickerDialog.this.getActivity().getSystemService("layout_inflater")).inflate(2130903200, null);
      }
      TextView localTextView = (TextView)paramViewGroup.findViewById(2131099796);
      localTextView.setTextColor(-1);
      paramView = (String)getItem(paramInt);
      localTextView.setText(paramView.substring(paramView.indexOf("BeD_")));
      return paramViewGroup;
    }
  }
  
  public static abstract interface BinaryPickHandler
  {
    public abstract void handleOnBinaryPicked(String paramString);
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\fragments\BinaryPickerDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */