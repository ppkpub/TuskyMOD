package org.ppkpub.ppklib;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;

import android.util.Base64;
import android.util.Log;

public class PPkUtil {
	//判断一个字符串是URI
	public static boolean  isURI(String str){
		return str!=null && str.indexOf(':')>0;
	}
  
	public static String getPage(String urlString) {
	    return getPage(urlString, 1);
	}

	public static String getPage(String urlString, int retries) {
	    try {
	      return CommonHttpUtil.getInstance().getContentFromUrl(urlString);
	    } catch (Exception e) {
	      Log.d("Util","Fetch URL error: "+e.toString());
	    }
	    return null;
	}  

    //获取网页标题
  	public static String getPageTitle(String urladdress,String htmlcontent)
  	{
      String temp="";
      try{
        String regex="<[Tt][Ii][Tt][Ll][Ee]>([^</[Tt][Ii][Tt][Ll][Ee]>]*)";
        Pattern pat = Pattern.compile(regex);
        Matcher match = pat.matcher(htmlcontent);
        while (match.find()) {
          int start = match.start();
          int end = match.end();
          temp = htmlcontent.substring(start+7, end);
        }
        
      }catch(Exception ex){
      }
      return temp.length()>0 ? temp:urladdress;
  	}
  	
	
	

  public static byte[] toByteArray(List<Byte> in) {
    final int n = in.size();
    byte ret[] = new byte[n];
    for (int i = 0; i < n; i++) {
      ret[i] = in.get(i);
    }
    return ret;
  }
  
  public static List<Byte> toByteArrayList(byte[] in) {
    List<Byte> arrayList = new ArrayList<Byte>();

    for (byte b : in) {
      arrayList.add(b);
    }
    return arrayList;
  }  
	  
  /**
       * 对图片字节数组进行Base64编码处理生成Data URL
	   * @param  img_type ：图片的类型，如 image/jpeg 
	   * @param  img_bytes ： 图片的字节数组
	   * @return Data URL字符串
	   */
  public static String imageToBase64DataURL(String img_type, byte[] img_bytes) {
    return "data:"+img_type+";base64,"+Base64.encodeToString( img_bytes, Base64.DEFAULT );
  }
	  
  public static byte[] readInputStream(InputStream inStream) throws Exception{
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    //创建一个Buffer字符串
    byte[] buffer = new byte[1024];
    //每次读取的字符串长度，如果为-1，代表全部读取完毕
    int len = 0;
    //使用一个输入流从buffer里把数据读取出来
    while( (len=inStream.read(buffer)) != -1 ){
        //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
        outStream.write(buffer, 0, len);
    }
    //关闭输入流
    inStream.close();
    //把outStream里的数据写入内存
    return outStream.toByteArray();
  }

  /**
   * 匹配是否为数字
   * @param str 可能为中文，也可能是-19162431.1254，不使用BigDecimal的话，变成-1.91624311254E7
   * @return
   * @author yutao
   * @date 2016年11月14日下午7:41:22
   */
  public static boolean isNumeric(String str) {
    String bigStr;
    try {
        bigStr = new BigDecimal(str).toString();
    } catch (Exception e) {
        return false;//异常 说明包含非数字。
    }
    return true;
  }

  /*
  * Convert byte[] to hex string.。   
  * @param src byte[] data   
  * @return hex string   
  */      
 public static String bytesToHexString(byte[] src){   
     StringBuilder stringBuilder = new StringBuilder("");   
     if (src == null || src.length <= 0) {   
         return null;   
     }   
     for (int i = 0; i < src.length; i++) {   
         int v = src[i] & 0xFF;   
         String hv = Integer.toHexString(v);   
         if (hv.length() < 2) {   
             stringBuilder.append(0);   
         }   
         stringBuilder.append(hv);   
     }   
     return stringBuilder.toString();   
 }   
 
  /**  
   * Convert hex string to byte[]  
   * @param hexString the hex string  
   * @return byte[]  
   */  
  public static byte[] hexStringToBytes(String hexString) {   
	  if (hexString == null) {   
          return null;   
      }
      if (hexString.equals("")) {   
          return new byte[0];   
      }
      
      hexString = hexString.toUpperCase();   
      int length = hexString.length() / 2;   
      char[] hexChars = hexString.toCharArray();   
      byte[] d = new byte[length];   
      for (int i = 0; i < length; i++) {   
          int pos = i * 2;   
          d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
      }   
      return d;   
  }   

  /**  
   * Convert char to byte  
   * @param c char  
   * @return byte  
   */  
  private static byte charToByte(char c) {   
      return (byte) "0123456789ABCDEF".indexOf(c);   
  }  
  
  
//获取URI的正文内容
  //会自动处理301/302转向得到最终内容
  public static String  fetchUriContent(String uri){
    try{
      String[] uri_chunks=uri.split(":");
      if(uri_chunks.length<2){
    	Log.d("Util","fetchURI() meet invalid uri:"+uri);
        return null;
      }
      
      if(uri_chunks[0].equalsIgnoreCase("ipfs")){
        return getIpfsData(uri_chunks[1]);
      }else if(uri_chunks[0].equalsIgnoreCase("dat")){
        return getDatData(uri);
      }else if(uri_chunks[0].equalsIgnoreCase("btmfs")){
        return getBtmfsData(uri);
      }else if(uri_chunks[0].equalsIgnoreCase("ppk")){
    	JSONObject obj_decoded_chunk=PTTP.getPPkResource(uri);
        if(obj_decoded_chunk==null)
            return null;
          
        return new String( (byte[])obj_decoded_chunk.opt(PPkDefine.JSON_KEY_CHUNK_BYTES) );
      }else if(uri_chunks[0].equalsIgnoreCase("data")){
        int from=uri_chunks[1].indexOf(",");
        if(from>=0){
          return uri_chunks[1].substring(from+1,uri_chunks[1].length());
        } else
          return uri_chunks[1];
      }else{
        return getPage(uri);
      }
    }catch(Exception e){
       Log.d("Util","fetchURI("+uri+") error:"+e.toString());
    }
    return null;
  }
  
  
  
  //循环删除目录和文件函数 
  public static void deleteDir(String file_path,boolean del_self) {
      File fp = new File(file_path);
      deleteDir(fp, del_self);
  }    
  
  public static void deleteDir(File file,boolean del_self) {
    if (file.isDirectory()) {
        for (File f : file.listFiles())
            deleteDir(f,true);
    }
    
    if(del_self)
        file.delete(); 
 }
  
  public static String getBtmfsData(String btmfs_uri){
      String tmp_url=PPkDefine.BTMFS_PROXY_URL+"?uri=" + java.net.URLEncoder.encode(btmfs_uri);
      Log.d("Util","Using BTMFS Proxy to fetch:"+ tmp_url);
      
      return getPage(tmp_url);
  }
  
  public static String getIpfsData(String ipfs_hash_address){
	  String tmp_url=PPkDefine.IPFS_DOWNLOAD_URL+ipfs_hash_address;
	  Log.d("Util","Using IPFS Proxy to fetch:"+ tmp_url);
      
      return getPage(tmp_url);
    /*try{
      IPFS ipfs = new IPFS(PPkDefine.IPFS_API_ADDRESS);
      Multihash filePointer = Multihash.fromBase58(ipfs_hash_address);
      byte[] fileContents = ipfs.cat(filePointer);
      return new String(fileContents);
    }catch(Exception e){
      System.out.println("PPkUtil.getIpfsData() error:"+e.toString());
      
      String tmp_url=PPkDefine.IPFS_DOWNLOAD_URL+ipfs_hash_address;
      System.out.println("Using IPFS Proxy to fetch:"+ tmp_url);
      
      return getPage(tmp_url);
    }
    */
  }
  
  public static String getDatData(String dat_uri){
      String dat_hash=dat_uri.substring("dat://".length());
      
      String tmp_url=null;
      String tmp_page_result=null;
      for(int kk=0;kk<PPkDefine.DAT_DOWNLOAD_URL_LIST.length;kk++){
          tmp_url=PPkDefine.DAT_DOWNLOAD_URL_LIST[kk]+dat_hash;
          Log.d("Util","Using Dat Proxy to fetch:"+ tmp_url);
          
          tmp_page_result=getPage(tmp_url);
          if(tmp_page_result!=null && tmp_page_result.length()>0){
              return tmp_page_result;
          }
      }
      return null;
  }
  
  
  public static String shortAddressView(String address){
	  if(address==null || address.length()<=15)
		  return address;
	  else
		  return address.substring(0, 8)+"..."+address.substring( address.length()-4 );
  }

  
  public static long getNowTimestamp() {
    return (new Date()).getTime()/(long)1000;
  }
  
}
