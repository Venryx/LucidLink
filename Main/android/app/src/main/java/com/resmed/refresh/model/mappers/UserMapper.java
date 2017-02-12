package com.resmed.refresh.model.mappers;

import com.resmed.refresh.model.RST_LocationItem;
import com.resmed.refresh.model.RST_User;
import com.resmed.refresh.model.RefreshModelController;
import com.resmed.refresh.model.json.User;
import com.resmed.refresh.model.json.UserProfile;
import com.resmed.refresh.model.json.UserResponse;
import java.util.Locale;

public class UserMapper
{
  public static UserProfile getProfile(RST_User paramRST_User)
  {
    UserProfile localUserProfile = UserProfileMapper.getProfile(paramRST_User.getProfile());
    RST_LocationItem localRST_LocationItem2 = paramRST_User.getLocation();
    RST_LocationItem localRST_LocationItem1 = localRST_LocationItem2;
    if (localRST_LocationItem2 == null)
    {
      localRST_LocationItem1 = new RST_LocationItem();
      localRST_LocationItem1.setLocale(Locale.getDefault().getISO3Country());
    }
    localUserProfile.setLocation(LocationMapper.getLocation(localRST_LocationItem1));
    localUserProfile.setTimeZone(RefreshModelController.getInstance().userTimezoneOffset());
    paramRST_User = paramRST_User.getSettings();
    localUserProfile.setLocationPermission(paramRST_User.getLocationPermission());
    localUserProfile.setPushNotifications(paramRST_User.getPushNotifications());
    localUserProfile.setUseMetricUnits(paramRST_User.getUseMetricUnits());
    localUserProfile.setTac1(paramRST_User.getTac1());
    localUserProfile.setTac2(paramRST_User.getTac2());
    localUserProfile.setTac3(paramRST_User.getTac3());
    localUserProfile.setWeightUnit(paramRST_User.getWeightUnit());
    localUserProfile.setHeightUnit(paramRST_User.getHeightUnit());
    localUserProfile.setTemperatureUnit(paramRST_User.getTemperatureUnit());
    localUserProfile.setLocale(paramRST_User.getLocale());
    localUserProfile.setCountryCode(paramRST_User.getCountryCode());
    return localUserProfile;
  }
  
  public static User getUser(RST_User paramRST_User)
  {
    User localUser = new User();
    localUser.setEmail(paramRST_User.getEmail());
    localUser.setFirstName(paramRST_User.getFirstName());
    localUser.setLastName(paramRST_User.getFamilyName());
    localUser.setPassword(paramRST_User.getPassword());
    localUser.setUsername(paramRST_User.getEmail());
    return localUser;
  }
  
  public static UserResponse getUserResponse(RST_User paramRST_User)
  {
    UserResponse localUserResponse = new UserResponse();
    localUserResponse.setUserProfile(getProfile(paramRST_User));
    localUserResponse.setUserId(paramRST_User.getId());
    return localUserResponse;
  }
  
  public static RST_User updateRST_User(RST_User paramRST_User, User paramUser)
  {
    if (paramUser.getUserId() != null) {
      paramRST_User.setId(paramUser.getUserId());
    }
    if (paramUser.getEmail() != null) {
      paramRST_User.setEmail(paramUser.getEmail());
    }
    if (paramUser.getPassword() != null) {
      paramRST_User.setPassword(paramUser.getPassword());
    }
    if (paramUser.getFirstName() != null) {
      paramRST_User.setFirstName(paramUser.getFirstName());
    }
    if (paramUser.getLastName() != null) {
      paramRST_User.setFamilyName(paramUser.getLastName());
    }
    return paramRST_User;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */