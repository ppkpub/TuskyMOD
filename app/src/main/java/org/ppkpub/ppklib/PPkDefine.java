package org.ppkpub.ppklib;

public class PPkDefine {
  public static String PPK_URI_PREFIX = "ppk:";
  public static String DIDPPK_URI_PREFIX = "did:"+PPK_URI_PREFIX;
  public static String PPK_URI_RESOURCE_MARK="*";
  public static String PAYTOPPK_URI_PREFIX = "paytoppk:";
  
  public static String PPK_API_URL  = "http://47.114.169.156/ppkapi2/";  //解析根标识的服务API
  public static String PPK_API_PUBKEY_PEM = "-----BEGIN PUBLIC KEY-----\r\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn+a+pl1qjw34cuoj+vDDioG5rsTXLK+h\r\nDLQY1nqXkrGt+50lz3T92IiPM1DWzrRz7ycrPkaesHsdCoB4zPRQy0OJHIQzGfpXdrqZbchJZfTM\r\nIFYDoPoPnlNH8xb4hQ8LoERtYxxwddGiHfvYhTEYFQBh4cj+AxEsex/duWim9O5q1I9PHK6anwlS\r\nNqmhyzxLAnFUKSqr2crUy7ZfCTi9zN63JXOsJtWi/dSZuK1RISej6zqQgkiQceIoqFXBjRVpYJQe\r\niQ3mw3uoZqth80e8UqT1ZXqyD82Obsb3ofKRFqbmEhuLi6+GJakgZsYs/BM/SpfIF0Wny5PWTemZ\r\nCWajXwIDAQAB\r\n-----END PUBLIC KEY-----\r\n"; //对标识解析结果的验证公钥

  public static final long ppkToolCreationTime = 1400561240-1;  //UTC 2014-5-20 04:47:20
  public static Integer firstBlock = 0;  
  
  public static Integer ppkStandardDataFee = 1000;
  
  public static final int ODIN_PROTOCOL_VER=2; 
  public static final int PTTP_PROTOCOL_VER=2; 
  
  public static final Integer TESTNET_FIRST_BLOCK = 547660;  //Testnet
  public static final String PPK_ODIN_MARK_PUBKEY_HEX_TESTNET="02d173743cd0d94f64d241d82a42c6ca92327c443e489f3842464a4df118d4920a";//1PPkT1hoRbnvSRExCeNoP4s1zr61H12bbg : For testnet
  
  public static final Integer MAINNET_FIRST_BLOCK = 426896;  //Mainnet
  public static final String PPK_ODIN_MARK_PUBKEY_HEX_MAINNET="0320a0de360cc2ae8672db7d557086a4e7c8eca062c0a5a4ba9922dee0aacf3e12";//1PPkPubRnK2ry9PPVW7HJiukqbSnWzXkbi : For Mainnet
  
  public static final byte PPK_PUBKEY_TYPE_FLAG=(byte)3;  //ODIN协议承载消息内容使用的公钥类型前缀取值
  public static final byte PPK_PUBKEY_LENGTH=33;  //ODIN协议承载消息内容使用的单条公钥长度
  public static final byte PPK_PUBKEY_EMBED_DATA_MAX_LENGTH=31;  //ODIN协议在单条公钥中最多嵌入的消息数据长度
  
  public static final int MAX_MULTISIG_TX_NUM = 2; //一条交易里能支持的最大数量多重签名输出条目，建议设为2，如果过大可能会被比特币网络拒绝
  public static final int MAX_N = 3;   //多重签名1-OF-N中的参数N最大数量，建议设为3，如果过大可能会被比特币网络拒绝
  public static final int MAX_OP_RETURN_LENGTH = 75; //OP_RETURN能存放数据的最大字节数
  //public static final int MAX_ODIN_DATA_LENGTH=(MAX_N-2)*PPK_PUBKEY_EMBED_DATA_MAX_LENGTH+(MAX_N-1)*PPK_PUBKEY_EMBED_DATA_MAX_LENGTH*(MAX_MULTISIG_TX_NUM-1)+MAX_OP_RETURN_LENGTH;  //支持嵌入的ODIN数据最大字节数
  
  public static final Byte FUNC_ID_ODIN_REGIST='R'; 
  public static final Byte FUNC_ID_ODIN_UPDATE='U'; 

  public static final Byte DATA_TEXT_UTF8= 'T'; //normal text in UTF-8
  public static final Byte DATA_BIN_GZIP = 'G'; //Compressed by gzip
  public static final Byte DATA_BIN_DEFLATE = 'D'; //Compressed by deflate
  
  public static final String ODIN_CMD_UPDATE_BASE_INFO ="BI";
  public static final String ODIN_CMD_UPDATE_AP_SET ="AP";
  public static final String ODIN_CMD_UPDATE_VD_SET ="VD";
  public static final String ODIN_CMD_CONFIRM_UPDATE ="CU";
  public static final String ODIN_CMD_TRANS_REGISTER ="TR";  
  
  public static final String ODIN_STATUS_PENDING = "pending";       //"等待区块链收录"
  public static final String ODIN_STATUS_VALID = "valid";         //"正常"
  public static final String ODIN_STATUS_INVALID = "invalid";       //"有错误"
  
  public static final String ODIN_UPDATE_STATUS_RECEIPTING = "receipting";    //"待接收";
  public static final String ODIN_UPDATE_STATUS_AWAITING = "awaiting";      //"待同意更新"
  
  public static final String ODIN_BASE_SET_REGISTER="register";
  public static final String ODIN_BASE_SET_ADMIN="admin";
  public static final String ODIN_BASE_SET_AUTH="auth";
  public static final String ODIN_BASE_SET_PNS_URL="pns_url";
  public static final String ODIN_BASE_SET_PNS_PARSER="pns_parser";
  
  public static final String ODIN_SET_VD_TYPE="type";
  public static final String ODIN_SET_VD_PUBKEY="pubkey";
  public static final String ODIN_SET_VD_CERT_URI="cert_uri"; //保留字段，用于测试
  public static final String ODIN_SET_VD_ENCODE_TYPE_PEM = "PEM";   
  public static final String ODIN_SET_VD_ENCODE_TYPE_BASE64 = "BASE64";  
  
  public static final Byte DATA_CATALOG_UNKNOWN= 0; //Unkown Data,reserved
  
  public static final String ODIN_EXT_KEY_DID_DOC = "x_did";
  public static final String ODIN_EXT_KEY_WALLET = "x_wallet";
  public static final String ODIN_EXT_KEY_SNS = "x_sns";

  public static final String ODIN_DEFAULT_AVATAR = "https://tool.ppkpub.org/image/user.png";
  
  public static String PTTP_INTEREST="pttp";
	  
  public static final String PTTP_KEY_VER="ver";
  public static final String PTTP_KEY_URI="uri";
  
  public static final String PTTP_KEY_OPTION="option";

  public static final String PTTP_KEY_SPEC="spec";
  public static final String PTTP_KEY_SPEC_NONE="none"; //无签名
  public static final String PTTP_KEY_SPEC_PAST="past."; //带有签名并符合PASTPAST规范
  public static final String PTTP_KEY_SPEC_PAST_HEADER_V1_PUBLIC="v1.public."; //符合PASTPAST规范v1版本和采用公钥数字签名
  
  public static final String PTTP_KEY_METAINFO="metainfo";
  public static final String PTTP_KEY_IAT="iat";
  public static final String PTTP_KEY_STATUS_CODE="status_code";
  public static final String PTTP_KEY_STATUS_DETAIL="status_detail";
  public static final String PTTP_KEY_CONTENT_ENCODING="content_encoding";
  public static final String PTTP_KEY_CONTENT_TYPE="content_type";
  public static final String PTTP_KEY_CONTENT_LENGTH="content_length";
  public static final String PTTP_KEY_CACHE_AS_LATEST="cache_as_latest";
  
  public static final String PTTP_KEY_CONTENT="content";
  public static final String PTTP_KEY_SIGNATURE="signature";
  
  public static final String PTTP_SIGN_MARK_INTEREST  =  "INTEREST";
  public static final String PTTP_SIGN_MARK_DATA  =  "DATA";
  
  public static final int PTTP_STATUS_CODE_OK = 200;
  public static final int PTTP_STATUS_CODE_LOCAL_ERROR = 775;  //PPk定义的本地异常代码

  public static final String JSON_KEY_PPK_URI="ppk-uri";
  public static final String JSON_KEY_PPK_VALIDATION="validation";
  public static final String JSON_KEY_CHUNK_BYTES="chunk";
  public static final String JSON_KEY_CHUNK_TYPE="chunk-type";
  public static final String JSON_KEY_CHUNK_LENGTH="chunk-length";
  public static final String JSON_KEY_CHUNK_URL="chunk-url";
  public static final String JSON_KEY_EXP_UTC="exp-utc";
  public static final String JSON_KEY_FROM_CACHE="from-cache";

  public static final String JSON_KEY_ORIGINAL_RESP="original-resp";
  
  public static final String DEFAULT_CACHE_AS_LATEST="public,max-age=600"; //默认作为最新版本的缓存时间是10分钟
  public static final String IGNORE_CACHE_AS_LATEST="no-store"; //不作为最新版本缓存
  
  public static final long CACHE_NO_EXP_UTC=0; //长期缓存的特殊时间戳，没有具体失效时间，会根据系统存储容量等条件尽可能缓存长的时间
  
  public static final int PTTP_VALIDATION_OK        = 0;
  public static final int PTTP_VALIDATION_IGNORED   = 1;
  public static final int PTTP_VALIDATION_ERROR     = 2;
  
  //Charset
  public static String PPK_TEXT_CHARSET="UTF-8";  //适用文本内容
  public static String BINARY_DATA_CHARSET="ISO-8859-1";  //适用原始二进制数据与字符串类型间的转换

  //暂时保留的旧字段
  //public static String JSON_KEY_PPK_ALGO="algo";
  //public static String JSON_KEY_PPK_SIGN_BASE64="sign_base64";
  
  //Dat
  public static String[] DAT_DOWNLOAD_URL_LIST={"http://tool.ppkpub.org/dat/?uri=dat://","https://datbase.org/download/"}; 
  
  //IPFS
  //public static String IPFS_API_ADDRESS="/ip4/tool.ppkpub.org/tcp/5001"; //"https://ipfs.infura.io:5001"
  public static String IPFS_DOWNLOAD_URL="http://tool.ppkpub.org:8080/ipfs/";//"https://ipfs.infura.io/ipfs/";
  
  //Bytom File System
  public static String BTMFS_PROXY_URL="http://btmdemo.ppkpub.org/btmfs/"; //Test service
  
}
