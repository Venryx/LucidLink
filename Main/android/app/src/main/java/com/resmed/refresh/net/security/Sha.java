package com.resmed.refresh.net.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Sha
{
  public static String bytesToHex(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int j = paramArrayOfByte.length;
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return localStringBuffer.toString();
      }
      localStringBuffer.append(Integer.toString((paramArrayOfByte[i] & 0xFF) + 256, 16).substring(1));
    }
  }
  
  public static String hash256(String paramString)
    throws NoSuchAlgorithmException
  {
    MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-256");
    localMessageDigest.update(paramString.getBytes());
    return bytesToHex(localMessageDigest.digest());
  }
  
  public static String hmacSha256(String paramString1, String paramString2)
    throws Exception
  {
    Mac localMac = Mac.getInstance("HmacSHA256");
    localMac.init(new SecretKeySpec(paramString1.getBytes(), "HmacSHA256"));
    return bytesToHex(localMac.doFinal(paramString2.getBytes()));
  }
}


/* Location:              C:\Root\@Objects\Tablet\Resmed\App Inspect\JD Gui\com.resmed.refresh-158.jar!\com\resmed\refresh\net\security\Sha.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */