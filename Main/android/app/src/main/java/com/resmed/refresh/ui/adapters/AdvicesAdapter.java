package com.resmed.refresh.ui.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.resmed.refresh.model.RST_AdviceItem;
import com.resmed.refresh.model.RST_AdviceItem.AdviceType;
import com.resmed.refresh.model.RST_Button;
import com.resmed.refresh.model.RST_CallbackItem;
import com.resmed.refresh.model.RST_Response;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.ui.uibase.app.RefreshApplication;
import com.resmed.refresh.utils.Log;
import com.resmed.refresh.utils.RefreshTools;
import java.util.List;

public class AdvicesAdapter
  extends ArrayAdapter<RST_AdviceItem>
  implements View.OnClickListener
{
  private Typeface akzidenzLight;
  private Typeface akzidenzLightBold;
  private Context context;
  private List<RST_AdviceItem> items;
  private LayoutInflater li;
  private ListView listView;
  
  public AdvicesAdapter(Context paramContext, ListView paramListView, List<RST_AdviceItem> paramList, LayoutInflater paramLayoutInflater)
  {
    super(paramContext, 2130903197, paramList);
    this.items = paramList;
    this.li = paramLayoutInflater;
    this.listView = paramListView;
    this.context = paramContext;
    this.akzidenzLight = Typeface.createFromAsset(paramContext.getAssets(), "AkzidenzGroteskBE-Light.otf");
    this.akzidenzLightBold = Typeface.createFromAsset(paramContext.getAssets(), "AkzidenzGroteskBE-Bold.otf");
  }
  
  private void sendFeedback(RST_AdviceItem paramRST_AdviceItem)
  {
    RefreshModelController localRefreshModelController = RefreshModelController.getInstance();
    paramRST_AdviceItem.update();
    localRefreshModelController.save();
    Log.d("com.resmed.refresh.ui", "Advices Adapter. Sending Feedback(" + paramRST_AdviceItem.getId() + ")=" + paramRST_AdviceItem.getFeedback());
    localRefreshModelController.updateFeedback(paramRST_AdviceItem, new RST_CallbackItem()
    {
      public void onResult(RST_Response<Object> paramAnonymousRST_Response)
      {
        if (paramAnonymousRST_Response.isStatus()) {
          Toast.makeText(AdvicesAdapter.this.context, "Feedback updated", 0).show();
        }
      }
    });
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    paramViewGroup = paramView;
    paramView = paramViewGroup;
    if (paramViewGroup == null)
    {
      paramView = this.li.inflate(2130903197, null);
      paramViewGroup = new ViewHolder();
      paramViewGroup.tvAdviceTitle = ((TextView)paramView.findViewById(2131100648));
      paramViewGroup.tvAdviceContent = ((TextView)paramView.findViewById(2131100651));
      paramViewGroup.tvAdviceSubtitle = ((TextView)paramView.findViewById(2131100650));
      paramViewGroup.tvAdviceArticleLink = ((TextView)paramView.findViewById(2131100653));
      paramViewGroup.llAdviceArticleLink = ((LinearLayout)paramView.findViewById(2131100652));
      paramViewGroup.ivAdviceType = ((ImageView)paramView.findViewById(2131100647));
      paramViewGroup.ivAdviceShare = ((ImageView)paramView.findViewById(2131100649));
      paramViewGroup.tvAdviceFeedback1 = ((TextView)paramView.findViewById(2131100656));
      paramViewGroup.tvAdviceFeedback2 = ((TextView)paramView.findViewById(2131100659));
      paramViewGroup.tvAdviceFeedback3 = ((TextView)paramView.findViewById(2131100662));
      paramViewGroup.ivAdviceFeedback1 = ((ImageView)paramView.findViewById(2131100655));
      paramViewGroup.ivAdviceFeedback2 = ((ImageView)paramView.findViewById(2131100658));
      paramViewGroup.ivAdviceFeedback3 = ((ImageView)paramView.findViewById(2131100661));
      paramViewGroup.llAdviceFeedback1 = paramView.findViewById(2131100654);
      paramViewGroup.llAdviceFeedback2 = paramView.findViewById(2131100657);
      paramViewGroup.llAdviceFeedback3 = paramView.findViewById(2131100660);
      paramView.findViewById(2131100654).setOnClickListener(this);
      paramView.findViewById(2131100657).setOnClickListener(this);
      paramView.findViewById(2131100660).setOnClickListener(this);
      paramView.setTag(paramViewGroup);
    }
    paramViewGroup = (ViewHolder)paramView.getTag();
    paramViewGroup.tvAdviceTitle.setText(((RST_AdviceItem)this.items.get(paramInt)).getHeader());
    paramViewGroup.tvAdviceContent.setText(((RST_AdviceItem)this.items.get(paramInt)).getContent());
    if (((RST_AdviceItem)this.items.get(paramInt)).getSubtitle().length() > 0)
    {
      paramViewGroup.tvAdviceSubtitle.setVisibility(0);
      paramViewGroup.tvAdviceSubtitle.setText(((RST_AdviceItem)this.items.get(paramInt)).getSubtitle());
      if (((RST_AdviceItem)this.items.get(paramInt)).getArticleUrl().length() <= 0) {
        break label1020;
      }
      paramViewGroup.llAdviceArticleLink.setVisibility(0);
      paramViewGroup.tvAdviceArticleLink.setPaintFlags(paramViewGroup.tvAdviceArticleLink.getPaintFlags() | 0x8);
      paramViewGroup.tvAdviceArticleLink.setText(((RST_AdviceItem)this.items.get(paramInt)).getArticleUrl());
      final Object localObject = ((RST_AdviceItem)this.items.get(paramInt)).getArticleUrl();
      paramViewGroup.tvAdviceArticleLink.setOnClickListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          paramAnonymousView = new Intent("android.intent.action.VIEW");
          paramAnonymousView.setData(Uri.parse(localObject));
          try
          {
            AdvicesAdapter.this.context.startActivity(paramAnonymousView);
            return;
          }
          catch (ActivityNotFoundException paramAnonymousView)
          {
            for (;;)
            {
              paramAnonymousView.printStackTrace();
            }
          }
        }
      });
      label473:
      if (((RST_AdviceItem)this.items.get(paramInt)).getButtons().size() >= 3)
      {
        paramViewGroup.tvAdviceFeedback1.setText(((RST_Button)((RST_AdviceItem)this.items.get(paramInt)).getButtons().get(0)).getTitle());
        paramViewGroup.tvAdviceFeedback2.setText(((RST_Button)((RST_AdviceItem)this.items.get(paramInt)).getButtons().get(1)).getTitle());
        paramViewGroup.tvAdviceFeedback3.setText(((RST_Button)((RST_AdviceItem)this.items.get(paramInt)).getButtons().get(2)).getTitle());
        localObject = RefreshTools.StringToBitMap(((RST_Button)((RST_AdviceItem)this.items.get(paramInt)).getButtons().get(0)).getIcon());
        if (localObject != null) {
          paramViewGroup.ivAdviceFeedback1.setImageBitmap((Bitmap)localObject);
        }
        localObject = RefreshTools.StringToBitMap(((RST_Button)((RST_AdviceItem)this.items.get(paramInt)).getButtons().get(1)).getIcon());
        if (localObject != null) {
          paramViewGroup.ivAdviceFeedback2.setImageBitmap((Bitmap)localObject);
        }
        localObject = RefreshTools.StringToBitMap(((RST_Button)((RST_AdviceItem)this.items.get(paramInt)).getButtons().get(2)).getIcon());
        if (localObject != null) {
          paramViewGroup.ivAdviceFeedback3.setImageBitmap((Bitmap)localObject);
        }
      }
      paramViewGroup.tvAdviceTitle.setTypeface(this.akzidenzLight);
      paramViewGroup.tvAdviceSubtitle.setTypeface(this.akzidenzLight);
      paramViewGroup.tvAdviceArticleLink.setTypeface(this.akzidenzLightBold);
      paramViewGroup.tvAdviceContent.setTypeface(this.akzidenzLight);
      paramViewGroup.tvAdviceFeedback1.setTypeface(this.akzidenzLight);
      paramViewGroup.tvAdviceFeedback2.setTypeface(this.akzidenzLight);
      paramViewGroup.tvAdviceFeedback3.setTypeface(this.akzidenzLight);
      paramViewGroup.llAdviceFeedback1.setSelected(false);
      paramViewGroup.llAdviceFeedback1.setSelected(false);
      paramViewGroup.llAdviceFeedback1.setSelected(false);
      Log.d("com.resmed.refresh.ui", "Advices Adapter. Displayed feedback(" + ((RST_AdviceItem)this.items.get(paramInt)).getId() + ")=" + ((RST_AdviceItem)this.items.get(paramInt)).getFeedback());
      switch (((RST_AdviceItem)this.items.get(paramInt)).getFeedback())
      {
      default: 
        label948:
        localObject = RST_AdviceItem.AdviceType.fromValue(((RST_AdviceItem)this.items.get(paramInt)).getCategory());
        switch (localObject)
        {
        default: 
          paramViewGroup.ivAdviceType.setImageResource(2130837602);
        }
        break;
      }
    }
    for (;;)
    {
      return paramView;
      paramViewGroup.tvAdviceSubtitle.setVisibility(8);
      break;
      label1020:
      paramViewGroup.llAdviceArticleLink.setVisibility(8);
      break label473;
      paramViewGroup.llAdviceFeedback1.setSelected(true);
      break label948;
      paramViewGroup.llAdviceFeedback2.setSelected(true);
      break label948;
      paramViewGroup.llAdviceFeedback3.setSelected(true);
      break label948;
      paramViewGroup.ivAdviceType.setImageResource(2130837603);
    }
  }
  
  public void onClick(View paramView)
  {
    int i = this.listView.getPositionForView(paramView) - 1;
    View localView3 = (View)paramView.getParent();
    View localView1 = localView3.findViewById(2131100654);
    View localView2 = localView3.findViewById(2131100657);
    localView3 = localView3.findViewById(2131100660);
    switch (paramView.getId())
    {
    }
    for (;;)
    {
      return;
      localView1.setSelected(true);
      localView2.setSelected(false);
      localView3.setSelected(false);
      ((RST_AdviceItem)this.items.get(i)).setFeedback(1);
      sendFeedback((RST_AdviceItem)this.items.get(i));
      continue;
      localView1.setSelected(false);
      localView2.setSelected(true);
      localView3.setSelected(false);
      ((RST_AdviceItem)this.items.get(i)).setFeedback(0);
      sendFeedback((RST_AdviceItem)this.items.get(i));
      continue;
      localView1.setSelected(false);
      localView2.setSelected(false);
      localView3.setSelected(true);
      ((RST_AdviceItem)this.items.get(i)).setFeedback(2);
      sendFeedback((RST_AdviceItem)this.items.get(i));
      continue;
      paramView = new Intent();
      paramView.setAction("android.intent.action.SEND");
      paramView.putExtra("android.intent.extra.TEXT", ((RST_AdviceItem)this.items.get(i)).getContent());
      paramView.setType("text/plain");
      paramView.setFlags(268435456);
      RefreshApplication.getInstance().startActivity(paramView);
    }
  }
  
  static class ViewHolder
  {
    public ImageView ivAdviceFeedback1;
    public ImageView ivAdviceFeedback2;
    public ImageView ivAdviceFeedback3;
    public ImageView ivAdviceShare;
    public ImageView ivAdviceType;
    public LinearLayout llAdviceArticleLink;
    public View llAdviceFeedback1;
    public View llAdviceFeedback2;
    public View llAdviceFeedback3;
    public TextView tvAdviceArticleLink;
    public TextView tvAdviceContent;
    public TextView tvAdviceFeedback1;
    public TextView tvAdviceFeedback2;
    public TextView tvAdviceFeedback3;
    public TextView tvAdviceSubtitle;
    public TextView tvAdviceTitle;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\ui\adapters\AdvicesAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */