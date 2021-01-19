package org.ppkpub.ppklib;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class PTAP01DID {
  public static JSONObject getPubUserInfo(String user_odin_uri) {
    user_odin_uri = ODIN.formatPPkURI(user_odin_uri,true);
    
    if(user_odin_uri==null)
        return null;
    
    JSONObject default_user_info = new JSONObject();
    try{
      default_user_info.put("user_odin",user_odin_uri);
      default_user_info.put("full_odin_uri","");
      default_user_info.put("name","");
      default_user_info.put("email","");
      default_user_info.put("avatar",PPkDefine.ODIN_DEFAULT_AVATAR);

      JSONObject obj_result = PTTP.getPPkResource(user_odin_uri);

      if(obj_result.optInt(PPkDefine.PTTP_KEY_STATUS_CODE)==200){
            default_user_info.put("full_odin_uri",obj_result.getString(PPkDefine.JSON_KEY_PPK_URI));

            byte[] tmp_content_bytes= (byte[])obj_result.opt(PPkDefine.JSON_KEY_CHUNK_BYTES) ;
            String tmp_content = new String( tmp_content_bytes );
            default_user_info.put("original_content",tmp_content);

            JSONObject tmp_user_info=new JSONObject( tmp_content  );
            if(tmp_user_info!=null){
                if( tmp_user_info.has(PPkDefine.ODIN_EXT_KEY_DID_DOC) ){
                    JSONObject tmp_obj = tmp_user_info.getJSONObject (PPkDefine.ODIN_EXT_KEY_DID_DOC);

                    default_user_info.put("name", tmp_obj.optString("name","") );
                    default_user_info.put("email",tmp_obj.optString("email",""));
                    default_user_info.put("avatar",tmp_obj.optString("avatar",PPkDefine.ODIN_DEFAULT_AVATAR));

                    default_user_info.put("register",tmp_user_info.optString("register"));

                    default_user_info.put("authentication",tmp_obj.optJSONArray ("authentication"));
                }

                if( tmp_user_info.has(PPkDefine.ODIN_EXT_KEY_WALLET) ){
                    default_user_info.put(PPkDefine.ODIN_EXT_KEY_WALLET,tmp_user_info.optJSONObject (PPkDefine.ODIN_EXT_KEY_WALLET));
                }

                if( tmp_user_info.has(PPkDefine.ODIN_EXT_KEY_SNS) ){
                    default_user_info.put(PPkDefine.ODIN_EXT_KEY_SNS,tmp_user_info.optJSONObject (PPkDefine.ODIN_EXT_KEY_SNS));
                }
            }
      }
    }catch(Exception e){
    	Log.d("PTAP01DID","getPubUserInfo("+user_odin_uri+") error: "+e.toString());
    }

    return default_user_info;
  }

  public static boolean isSameSnsUser(String sns_type,String sns_name,String sns_host_domain,JSONObject odin_sns_set) {
      //Log.d("isSameSnsUser",sns_type+" , "+sns_name+" , "+sns_host_domain );

      if(sns_name==null || odin_sns_set==null)
          return false;

      if ( sns_name.indexOf('@')<0 && sns_host_domain!=null) {
          /*
          try {
              URL url = new URL(sns_url);
              String host = url.getHost();
              sns_name = sns_name + "@"+host ;
          } catch (MalformedURLException e) {
              //e.printStackTrace();
          }
          */
          sns_name = sns_name + "@"+sns_host_domain ;
      }

      //Log.d("isSameSnsUser",sns_name);

      JSONArray tmp_ids = odin_sns_set.optJSONArray(sns_type);
      if(tmp_ids==null)
          return false;

      for(int kk=0; kk<tmp_ids.length(); kk++  ){
          //Log.d("isSameSnsUser","["+kk+"]="+tmp_ids.optString(kk)+" " );
          if( sns_name.equals(tmp_ids.optString(kk)))
              return true;
      }

      return false;
  }
}