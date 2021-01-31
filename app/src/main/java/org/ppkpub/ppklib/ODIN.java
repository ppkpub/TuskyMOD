package org.ppkpub.ppklib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

public class ODIN {
  
  private static String[] LetterEscapeNumSet={"O","ILA","BCZ","DEF","GH","JKS","MN","PQR","TUV","WXY"};
  
  //格式化输入URI参数，使之符合ODIN标识定义规范，无效返回null
  //参数prior_add_resource_mark取值true时 追加资源标识符（需要完整格式ID使用时）， 否则忽略尾部的资源标识符
  //public static String formatPPkURI(String ppk_uri){
  //	  return formatPPkURI(ppk_uri,false);
  //}
  public static String formatPPkURI(String ppk_uri,boolean need_add_resource_mark_for_id){
    if(ppk_uri==null)
        return null;
    
    if( ppk_uri.indexOf("//") >0 ){
        //存在连续的/字符
        return null;
    }
    
    //替换可能的中文易输错字符
    ppk_uri=ppk_uri.replaceFirst("：", ":");
    
    int scheme_posn=ppk_uri.indexOf(":");
    String main_part=null;
    
    if( scheme_posn<0){ //输入地址没有任何类型前缀时
        main_part = ppk_uri.trim();
    }else{//检查前缀是否以ppk:起始
        String prefix = ppk_uri.substring(0,scheme_posn+1);
        
        if( !prefix.equalsIgnoreCase(PPkDefine.PPK_URI_PREFIX) )
            return null;
        
        main_part = ppk_uri.substring(scheme_posn+1).trim();
    }
    
    if(main_part.length()==0){ //没有指定实际网址时
    	return null;
    }
    
    ppk_uri = PPkDefine.PPK_URI_PREFIX+main_part;
    
    int old_resoure_mark_posn=ppk_uri.lastIndexOf("#");
    if(old_resoure_mark_posn==ppk_uri.length()-1) {//自动替换旧版URI中的后缀标志符#
    	ppk_uri = ppk_uri.substring(0, old_resoure_mark_posn)+PPkDefine.PPK_URI_RESOURCE_MARK;;
    }
    
    int resoure_mark_posn=ppk_uri.lastIndexOf(PPkDefine.PPK_URI_RESOURCE_MARK);
    
    if( resoure_mark_posn<0 && need_add_resource_mark_for_id){
    	ppk_uri += PPkDefine.PPK_URI_RESOURCE_MARK;
    }else if( resoure_mark_posn==ppk_uri.length()-1 && !need_add_resource_mark_for_id){
        ppk_uri = ppk_uri.substring(0,resoure_mark_posn);
    }

    /*
    //自动判断先添加缺少的"/"字符
    int fisrt_slash_posn=ppk_uri.indexOf("/");
    if(fisrt_slash_posn<0){ //是根标识
        ppk_uri += "/";
    }else{ //是扩展标识
        //判断尾部的内容资源名是否有文件扩展名或者方法标志符
        int last_slash_posn=ppk_uri.lastIndexOf("/");

        if( last_slash_posn!=ppk_uri.length()-1){ //不是以"/"字符结尾
            int last_point_posn=ppk_uri.lastIndexOf(".");
            int function_mark_posn=ppk_uri.lastIndexOf(")");
            if(last_point_posn<last_slash_posn && function_mark_posn<0){
                //没有文件扩展名或者是方法标识，默认为目录，需要补上"/"
                ppk_uri += "/";
            }
        }
    }
    */

    return ppk_uri;
  }
  
  //解构PPK资源地址
  public static PPkUriParts splitPPkURI(String in_uri)
  {
    try{
      //检查URI格式符合要求
      String format_ppk_uri = formatPPkURI(in_uri,true);
      
      if( format_ppk_uri==null ){
        Log.d("Odin","splitPPkURI() meet invalid ppk-uri:"+in_uri);
        return null;
      }
      
      int resoure_mark_posn=format_ppk_uri.indexOf(PPkDefine.PPK_URI_RESOURCE_MARK);
      
      PPkUriParts obj_uri_parts = new PPkUriParts();
      obj_uri_parts.resource_versoin = format_ppk_uri.substring(resoure_mark_posn+1,format_ppk_uri.length());
      String str_path_segment = format_ppk_uri.substring(PPkDefine.PPK_URI_PREFIX.length(),resoure_mark_posn);

      //System.out.println("str_path_segment="+str_path_segment);
      if(str_path_segment.endsWith("/")){ //类似"ppk:123/"或"ppk:123/abc/"指向默认内容主页 
        obj_uri_parts.resource_id="";
        obj_uri_parts.parent_odin_path=str_path_segment.substring(0,str_path_segment.length()-1);
      }else{
        int tmp_posn=str_path_segment.lastIndexOf('/');
        if(tmp_posn>0){
          obj_uri_parts.resource_id=str_path_segment.substring(tmp_posn+1,str_path_segment.length());
          obj_uri_parts.parent_odin_path=str_path_segment.substring(0,tmp_posn);
        }else{
          obj_uri_parts.parent_odin_path="";
          obj_uri_parts.resource_id=str_path_segment;
        }
      }

      obj_uri_parts.format_uri = format_ppk_uri;
          
      //System.out.println("ODIN.splitPPkURI()\n format_uri="+obj_uri_parts.format_uri+" , parent_odin_path="+obj_uri_parts.parent_odin_path+", resource_id="+obj_uri_parts.resource_id+", resource_versoin="+obj_uri_parts.resource_versoin+"\n");
      
      return obj_uri_parts;
    }catch(Exception e){
      Log.d("Odin","splitPPkURI() meet invalid ppk-uri:"+in_uri + " ,"+e.toString() );
      return null;
    }
  }



    public static ArrayList matchPPkURIs(String str) {
        return matchPPkURIs(str,null);
    }

    public static ArrayList matchPPkURIs(String str,String add_prefix) {
        ArrayList result_array=new ArrayList();

        String pattern =  add_prefix==null ? "": "["+add_prefix+"]";
        pattern +="[pP][pP][kK][:：]\\w+([\\w-./]*)?";

        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(str);
        while(matcher.find()){
            String odin_uri=matcher.group();

            Log.d("matchPPkURIs","find "+odin_uri);
            if(add_prefix!=null)
                odin_uri = odin_uri.substring(add_prefix.length());

            odin_uri = ODIN.formatPPkURI(odin_uri,false);
            if(odin_uri!=null  && result_array.indexOf(odin_uri)<0 )
                result_array.add(odin_uri);
        }
        Collections.sort(result_array, Collections.reverseOrder());


        return result_array;
    }
  
  //获得PPk URI对应资源版本号，即结尾类似“*1.0”这样的描述，如果没有则返回空字符串
  public static String getPPkResourceVer(String ppk_uri){
    if(ppk_uri==null)
        return null;

    int resoure_mark_posn=ppk_uri.indexOf(PPkDefine.PPK_URI_RESOURCE_MARK);

    return resoure_mark_posn>0 ? ppk_uri.substring(resoure_mark_posn+1).trim():"";

  }
  
  //获得表示最新版本的URI（去掉可能的版本号）
  public static String getLastestPPkURI(String ppk_uri){
    if(ppk_uri==null)
        return null;

    int resoure_mark_posn=ppk_uri.indexOf(PPkDefine.PPK_URI_RESOURCE_MARK);
    if( resoure_mark_posn<0){ 
        ppk_uri += PPkDefine.PPK_URI_RESOURCE_MARK;
    }else{
        ppk_uri = ppk_uri.substring(0,resoure_mark_posn+1);
    }

    return ppk_uri;
  }
  
//将根标识中的英文字母按奥丁号规范转换成对应数字
  public static String convertLetterToNumberInRootODIN(String  original_odin){  
     String converted_odin="";
     original_odin=original_odin.toUpperCase();
     for(int kk=0;kk<original_odin.length();kk++){  
        int chr=original_odin.charAt(kk);  
        switch(chr){
            case 'O':
              chr='0';
              break;
            case 'I':
            case 'L':
            case 'A':
              chr='1';
              break;
            case 'B':
            case 'C':
            case 'Z':
              chr='2';
              break;
            case 'D':
            case 'E':
            case 'F':
              chr='3';
              break;
            case 'G':
            case 'H':
              chr='4';
              break;
            case 'J':
            case 'K':
            case 'S':
              chr='5';
              break;
            case 'M':
            case 'N':
              chr='6';
              break;
            case 'P':
            case 'Q':
            case 'R':
              chr='7';
              break;
            case 'T':
            case 'U':
            case 'V':
              chr='8';
              break;
            case 'W':
            case 'X':
            case 'Y':
              chr='9';
              break;
            default:
              break;
        }
        converted_odin=converted_odin+(char)chr;
     }  
     return PPkUtil.isNumeric(converted_odin)?converted_odin:null;  
  }   
  
  //获得指定数字短标识的对应字母转义名称组合
  public static List getEscapedListOfShortODIN(Integer  short_odin){ 
    List<String> listEscaped = new ArrayList<String>();
    
    
    String strTmp=short_odin.toString();
    listEscaped=getEscapedLettersOfShortODIN(listEscaped,strTmp,0,"");
    
    System.out.println("listEscaped:"+listEscaped.toString());
    
    return listEscaped;
  }
  
  public static List getEscapedLettersOfShortODIN(List listEscaped,String  original,int posn,String pref){ 
    int tmpNum=Integer.parseInt(String.valueOf(original.charAt(posn)));
    System.out.println("original["+posn+"]:"+tmpNum);
    
    String tmpLetters=LetterEscapeNumSet[tmpNum];
    for(int tt=0;tt<tmpLetters.length();tt++){
      String new_str=pref+String.valueOf(tmpLetters.charAt(tt));
      
      if(posn<original.length()-1){
        listEscaped=getEscapedLettersOfShortODIN(listEscaped,original,posn+1,new_str);
      }else{
        listEscaped.add(new_str);
      }
    }
    
    return listEscaped;
  }
  
  //对类似did:ppk:起始网址转换得到实际的PPK资源网址，如果出错则返回null
  public static String  getRealPPkURI(String uri){
      if( uri.toLowerCase().startsWith(PPkDefine.DIDPPK_URI_PREFIX)) 
	      	uri = PPkDefine.PPK_URI_PREFIX + uri.substring(PPkDefine.DIDPPK_URI_PREFIX.length());

      return formatPPkURI( uri,true )  ;
  }

  public static JSONObject getRootOdinSet(String root_odin)  {
		String str_req_uri = PPkDefine.PPK_URI_PREFIX+root_odin+PPkDefine.PPK_URI_RESOURCE_MARK;
		//String interest="{\"ver\":1,\"hop_limit\":6,\"interest\":{\"uri\":\""+str_req_uri+"\"}}";
		
		String str_ap_resp_json=APoverHTTP.fetchInterest(PPkDefine.PPK_API_URL,  str_req_uri);
		
	    try {
	    	JSONObject  parent_vd_set=null;
	    	
	    	if(PPkDefine.PPK_API_PUBKEY_PEM.length()>0){
	    		parent_vd_set=new JSONObject();
	    		parent_vd_set.put(PPkDefine.ODIN_SET_VD_TYPE, PPkDefine.ODIN_SET_VD_ENCODE_TYPE_PEM);
	    		parent_vd_set.put(PPkDefine.ODIN_SET_VD_PUBKEY, PPkDefine.PPK_API_PUBKEY_PEM);
	    	}
	    	JSONObject tmp_resp = PTTP.parseRespOfPTTP(PPkDefine.PPK_API_URL,str_ap_resp_json,str_req_uri,parent_vd_set);   
			
			if(tmp_resp==null || tmp_resp.optInt(PPkDefine.JSON_KEY_PPK_VALIDATION,PPkDefine.PTTP_VALIDATION_ERROR) == PPkDefine.PTTP_VALIDATION_ERROR ){
				return null;
	        }else{
	        	byte[] api_content_bytes= (byte[])tmp_resp.opt(PPkDefine.JSON_KEY_CHUNK_BYTES) ;
	        	String str_resp_setting = new String( api_content_bytes );
	              
	            return new JSONObject( str_resp_setting  );
	        }
		} catch (JSONException e) {
			Log.d("Odin-Exception",e.toString());
		}
	    
	    return null;
  }


}
