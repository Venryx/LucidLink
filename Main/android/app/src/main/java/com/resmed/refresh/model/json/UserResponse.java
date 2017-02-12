package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class UserResponse
{
  @SerializedName("FirstName")
  private String firstName;
  @SerializedName("LastName")
  private String lastName;
  @SerializedName("RSTKey")
  private String rstKey;
  @SerializedName("ResMedUserId")
  private String userId;
  @SerializedName("UserProfile")
  private UserProfile userProfile;
  
  public String getFirstName()
  {
    return this.firstName;
  }
  
  public String getLastName()
  {
    return this.lastName;
  }
  
  public String getRstKey()
  {
    return this.rstKey;
  }
  
  public String getUserId()
  {
    return this.userId;
  }
  
  public UserProfile getUserProfile()
  {
    return this.userProfile;
  }
  
  public void setFirstName(String paramString)
  {
    this.firstName = paramString;
  }
  
  public void setLastName(String paramString)
  {
    this.lastName = paramString;
  }
  
  public void setRstKey(String paramString)
  {
    this.rstKey = paramString;
  }
  
  public void setUserId(String paramString)
  {
    this.userId = paramString;
  }
  
  public void setUserProfile(UserProfile paramUserProfile)
  {
    this.userProfile = paramUserProfile;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\json\UserResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */