package org.ppkpub.ppklib;

import android.util.Log;

public class APoverPTTP {
  public static String fetchInterest(String ap_url, String interest) {
    if(ap_url==null)
        return null;
    
    String str_ap_resp_json=null;
    String ap_fetch_url=ap_url;
    
    try{
      if( ap_fetch_url.endsWith("/")){
        ap_fetch_url += "pttp("+PPkUtil.bytesToHexString(interest.getBytes(PPkDefine.PPK_TEXT_CHARSET))+")"+PPkDefine.PPK_URI_RESOURCE_MARK;
      }
      Log.d("APoverPTTP","fetchInterest("+ap_fetch_url+") ...");
    
      str_ap_resp_json = PPkUtil.fetchUriContent(ap_fetch_url);
    }catch(Exception e){
    	Log.d("APoverPTTP","fetchInterest("+ap_fetch_url+") error: "+e.toString());
    }
    
    //System.out.println("APoverPTTP.fetchInterest() str_ap_resp_json:"+str_ap_resp_json);
    return str_ap_resp_json;
  }
}