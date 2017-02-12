package com.resmed.refresh.model.json;

import com.google.gson.annotations.SerializedName;

public class User
{
  @SerializedName("Email")
  private String email = "";
  @SerializedName("FirstName")
  private String firstName = "";
  @SerializedName("LastName")
  private String lastName = "";
  @SerializedName("Password")
  private String password = "";
  @SerializedName("RegistrationDate")
  private String registrationDate = "";
  @SerializedName("UserId")
  private String userId = "";
  @SerializedName("Username")
  private String username = "";
  
  public String getEmail()
  {
    return this.email;
  }
  
  public String getFirstName()
  {
    return this.firstName;
  }
  
  public String getLastName()
  {
    return this.lastName;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public String getRegistrationDate()
  {
    return this.registrationDate;
  }
  
  public String getUserId()
  {
    return this.userId;
  }
  
  public String getUsername()
  {
    return this.username;
  }
  
  public void setEmail(String paramString)
  {
    this.email = paramString;
  }
  
  public void setFirstName(String paramString)
  {
    this.firstName = paramString;
  }
  
  public void setLastName(String paramString)
  {
    this.lastName = paramString;
  }
  
  public void setPassword(String paramString)
  {
    this.password = paramString;
  }
  
  public void setRegistrationDate(String paramString)
  {
    this.registrationDate = paramString;
  }
  
  public void setUsername(String paramString)
  {
    this.username = paramString;
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\model\json\User.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */