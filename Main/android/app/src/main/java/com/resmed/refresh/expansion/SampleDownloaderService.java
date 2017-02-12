package com.resmed.refresh.expansion;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

public class SampleDownloaderService
  extends DownloaderService
{
  private static final String BASE64_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwqSs2NVUYtA0IhI/GlRlayDKdAk/aImM/jOF5daxQuSYT2gwIce+1SD0v08to5lGWgOwYJOCkairDpLmuqO311tNT3buv4JKc6VAAnyDSDBquyzvYeNuKORIZp0LPR8vAxtWbzMVVyilxoXxjml93UIYyeorOA68qImCSAQ6rS/e7Ys6/LT5NYYX3O3Jb64tJK7P1BBVtf0aAiv5dV5RrpTANqQTcqyndCAIZXPQizHGU/dUm2/K52FvNk2tthNx3tBcn2k6yDm49ognbRTMxjZ9RGHMuAUYwuznOv6qjMIx9a+c9Y0Nrv9YxjMglOb9wWjlHuc6fTOq0PR2jXUF6QIDAQAB";
  private static final byte[] SALT = { 1, 43, -12, -1, 54, 98, -100, -12, 43, 2, -8, -4, 9, 5, -106, -108, -33, 45, -1, 84 };
  
  public String getAlarmReceiverClassName()
  {
    return SampleAlarmReceiver.class.getName();
  }
  
  public String getPublicKey()
  {
    return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwqSs2NVUYtA0IhI/GlRlayDKdAk/aImM/jOF5daxQuSYT2gwIce+1SD0v08to5lGWgOwYJOCkairDpLmuqO311tNT3buv4JKc6VAAnyDSDBquyzvYeNuKORIZp0LPR8vAxtWbzMVVyilxoXxjml93UIYyeorOA68qImCSAQ6rS/e7Ys6/LT5NYYX3O3Jb64tJK7P1BBVtf0aAiv5dV5RrpTANqQTcqyndCAIZXPQizHGU/dUm2/K52FvNk2tthNx3tBcn2k6yDm49ognbRTMxjZ9RGHMuAUYwuznOv6qjMIx9a+c9Y0Nrv9YxjMglOb9wWjlHuc6fTOq0PR2jXUF6QIDAQAB";
  }
  
  public byte[] getSALT()
  {
    return SALT;
  }
}


/* Location:              [...]
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */