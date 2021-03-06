package com.sm.music.MusicUtils;

import com.alibaba.fastjson.JSONObject;
import com.sm.music.Bean.KuwoMus;
import com.sm.music.Bean.Music;
import com.sm.music.Bean.RecMusic;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
四种接口
 */
public class GetMusic {

	public static final int RESPOND_SUCCESS = 200;

	public static final int RESPOND_TIMEOUT = 500;

	public static final int RESPOND_EMPTY = 404;

	public static final int REQUEST_TYPE_URL = 1;

	public static final int REQUEST_TYPE_PIC = 2;

	public static final int REQUEST_TYPE_LYRIC = 3;

	public static final int MUSIC_SOURCE_NETEASE = 0;

	public static final int MUSIC_SOURCE_TENCENT = 1;

	public static final int MUSIC_SOURCE_KUGOU = 2;

	public static final int MUSIC_SOURCE_MIGU = 3;

	public static final int MUSIC_SOURCE_KOOWO = 4;

	public static final String REQUEST_URL_SEARCH_KUWO="http://music.dwh99.cn/api/search/?name=";

	public static final String REQUEST_URL_PLAY_KUWO = "http://music.dwh99.cn/api/mp3/?rid=";

	public static final String REQUEST_URL_LYRIC_KUWO = "http://www.dwh99.cn:8080/api/lrc/?rid=";

	public static final String REQUEST_URL_URL = "https://api.zhuolin.wang/api.php?types=url";

	public static final String REQUEST_URL_SEARCH = "https://api.zhuolin.wang/api.php?types=search";

	public static final String REQUEST_URL_PIC = "https://api.zhuolin.wang/api.php?types=pic";

	public static final String REQUEST_URL_LYRIC = "https://api.zhuolin.wang/api.php?types=lyric";

	public static final String REQUEST_URL_PLAYLIST = "https://api.zhuolin.wang/api.php?types=playlist&id=";

	public static final long MUSIC_PLAY_LIST_1 = 3778678L;

	public static final long MUSIC_PLAY_LIST_2 = 3779629L;

	public static final long MUSIC_PLAY_LIST_3 = 19723756L;

	public static final long MUSIC_PLAY_LIST_4 = 5059642708L;

	public static final long MUSIC_PLAY_LIST_5 = 5059633707L;

	public static final long MUSIC_PLAY_LIST_6 = 5059661515L;

	public static final long MUSIC_PLAY_LIST_7 = 991319590L;

	public static final long MUSIC_PLAY_LIST_8 = 4395559L;

	public static final long MUSIC_PLAY_LIST_9 = 64016L;

	public static final long MUSIC_PLAY_LIST_10 = 112504L;

	public static final long MUSIC_PLAY_LIST_11 = 2884035L;

	public static final long MUSIC_PLAY_LIST_12 = 2809513713L;

	public static final long MUSIC_PLAY_LIST_13 = 2809577409L;

	public static final long MUSIC_PLAY_LIST_14 = 5059644681L;

	public static final long MUSIC_PLAY_LIST_15 = 745956260L;

	private Connection connection;

	private Map<String,String> headerMap;

	private List<Music> musicList ;
	
	private List<String> requestMusicID;
	
	private List<String> requestMusicSource;
	
	private List<String> requestMusicURL_URL;

	private List<String> requestMusicURL_Search;

	private List<String> requestMusicURL_Pic;

	private List<String> requestMusicURL_Lyric;

	private List<String> kuWoMusPlayUrl;

	private List<RecMusic> recMusicList;

	private List<KuwoMus> kuWoMusicList;

	private List<Music> recMusList;

	public GetMusic(){}

	/**
	 *
	 * @param host
	 * @return
	 */
	public Map<String,String> SetheaderMap(String host) {
		headerMap = new HashMap<String,String>();
		headerMap.put("Host",host);
        return headerMap;
	}

	/**
	 *
	 * @return
	 */
	public Map<String,String> setHeaderMap_MusicPlayerURL(){
		headerMap = new HashMap<String,String>();
		headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.146 Safari/537.36");
		headerMap.put("Accept","application/json;charset=UTF-8");
		return  headerMap;
	}

	/**
	 *
	 * @param url
	 * @return
	 */
	public Connection getConnection(String url){
		return connection = Jsoup.connect(url).data(setHeaderMap_MusicPlayerURL());
	}
	/**
	 *
	 * @param Url
	 * @param host
	 * @return
	 */
	public Connection GetConnection(String Url,String host) {
		return connection = Jsoup.connect(Url).data(SetheaderMap(host));
	}

	/**
	 *
	 * @param Url
	 * @return
	 */
	public Connection GetConnection(String Url) {
		return connection = Jsoup.connect(Url).timeout(3000);
	}

	/**
	 *
	 * @param URL
	 * @return
	 */
	public String fengeURL(String URL){
		String url = "";
		if(URL.contains("url")&&URL.contains("size")&&URL.contains("br")){
			String regex = "\"url\":\"(.*?)\",\"s";
			Pattern pattern1 = Pattern.compile(regex);
			Matcher m = pattern1.matcher(URL);
			while (m.find()) {
				int i = 1;
				url+=m.group(i);
				i++;
			}
		}
		return  url;
	}

	public String fengeUrl2(String URL){
		String url ="";
		String regex = "\"url\":\"(.*?)\"";
		Pattern pattern1 = Pattern.compile(regex);
		Matcher m = pattern1.matcher(URL);
		while (m.find()) {
			int i = 1;
			url+=m.group(i);
			i++;
		}
		return url;
	}
	/**
	 * 分割url拿出主机地址
	 * @param url
	 * @return
	 */
	public static String getHost(String url) {
		String cache = "";
		if(url.contains("www.")) {
			String regex1 = "www.(.*?).com";
			String host = "";
			Pattern pattern1 = Pattern.compile(regex1);
			Matcher m = pattern1.matcher(url);
			while (m.find()) {  
	            int i = 1;  
	            host+=m.group(i);
	            i++;  
	        } 
			cache= host+".com";
		}else if(url.contains("https://")){
			String regex1 = "https://(.*?).com";
			String host = "";
			Pattern pattern1 = Pattern.compile(regex1);
			Matcher m = pattern1.matcher(url);
			while (m.find()) {  
	            int i = 1;  
	            host+=m.group(i);
	            i++;  
	        } 
			cache = host+".com";
		}else if(url.contains("http://")) {
			String regex1 = "http://(.*?).com";
			String host = "";
			Pattern pattern1 = Pattern.compile(regex1);
			Matcher m = pattern1.matcher(url);
			while (m.find()) {  
	            int i = 1;  
	            host+=m.group(i);
	            i++;  
	        } 
			cache= host+".com";
		}
	return cache;
	}

	/**
	 * 返回歌曲播放链接的json（header特殊）
	 */
	public String getMusicPlayJson(String url) throws Exception{
		Document document = getConnection(url).ignoreContentType(true).get();
		String result = Parser.unescapeEntities(document.body().html(), false);
		return result ;
	}

	/**
	 * （1）首先你得根据方法获得返回请求的URL
	 * （2）使用下面的方法进行get请求，拿到最后服务器返回的json字符串
	 */
	public String  getJSON(String url) throws Exception{
		Document document = GetConnection(url).ignoreContentType(true).get();
		return document.body().html();
	}



	/**
	 * 返回搜索歌曲的请求连接，
	 * 只需要传入两个参数
	 * 	 * 根据index来筛选music的来源
	 * 	 *  0 netease
	 * 	 *  1 tencent
	 * 	 *  2 kugou
	 * 	 *  default kugou
	 * @return
	 */
	public String getSearchRequsetURL(String musicName, int musicSource){
		requestMusicURL_Search = new ArrayList<>();
		if(musicName!=null){
			requestMusicURL_Search.add(REQUEST_URL_SEARCH+"&name="+musicName+"&source="+chooseMusicSource(musicSource));
		}
		return requestMusicURL_Search.get(0);
	}

	/**
	 * 返回搜索歌词的请求连接，
	 * 只需要传入两个参数
	 * 	 * 根据index来筛选music的来源
	 * 	 *  0 netease
	 * 	 *  1 tencent
	 * 	 *  2 kugou
	 * 	 *  default kugou
	 * @return
	 */
	public List<String> getLyricRequestURL(List<String> musicIDList,int musicSource){
		requestMusicURL_Lyric = new ArrayList<>();
		if(musicIDList!=null){
			for (String musicID : musicIDList) {
				requestMusicURL_Lyric.add(REQUEST_URL_LYRIC+"&id="+musicID+"&source="+chooseMusicSource(musicSource));
			}
		}
		return requestMusicURL_Lyric;
	}
	/**
	 * 传入index（int）类型的参数，
	 * 返回相应的请求类型  type
	 */
	public String chooseRequestType(int index){
		switch (index){
			case 0:
				return "search";
			case 1:
				return "url";
			case 2:
				return "pic";
			case 3:
				return "lyric";
			default:
				return "search";
		}
	}
	/**
	 * 根据index来筛选music的来源
	 *  0 netease
	 *  1 tencent
	 *  2 kugou
	 *  default kugou
	 */
	public String chooseMusicSource(int index){
		switch (index){
			case 0:
				return  "netease";
			case 1:
				return  "tencent";
			case 2:
				return  "kugou";
			case 3:
				return  "migu";
			default:
				return  "kugou";
		}
	}

	/**
	 * 接受来自服务器的返回的json字符串
	 * 将字符串转为对应的Music对象
	 * @param json
	 * @return
	 */
	public List<Music> getMusicList(String json){
		musicList = new ArrayList<>();
		if(json!=null){
			musicList= JSONObject.parseArray(json,Music.class);
		}else{
			throw new RuntimeException("json is null");
		}
		return  musicList;
	}

	/**
	 * 获得请求播放地址的url时候，必要的参数之一：
	 * 获得播放地址必要有两个参数：
	 * 		第一个参数：音乐ID musicId
	 * 		第二个参数：播放源 source	
	 * 	是一个必要的参数，返回的是一个String类型的List集合	
	 * @param musicList
	 * @return
	 */
	public List<String> requestMusicID(List<Music> musicList){
		requestMusicID = new ArrayList<>();
		if (musicList!=null){
			for (Music music : musicList) {
				requestMusicID.add(music.getId());
			}
		}else{
			throw new RuntimeException("musicList is null");
		}
		return  requestMusicID;
	}

	/**
	 * 传入的参数是一个musiclist 取出请求播放地址的第二个参数：source
	 * 是一个必要的参数，返回的是一个String类型的List集合
	 * @param musicList
	 * @return
	 */
	public List<String> requestMusicSource(List<Music> musicList){
		requestMusicSource = new ArrayList<>();
		if(musicList!=null){
			for (Music music : musicList) {
				requestMusicSource.add(music.getSource());
			}
		}else{
			throw new RuntimeException("musicList is null");
		}
		return  requestMusicSource;
	}
	
	public List<String> requestMusicURL(List<String> requestMusicID,List<String> requestMusicSource){
		requestMusicURL_URL = new ArrayList<>();
		if(requestMusicID.size()!=0){
			for (int index = 0; index < requestMusicID.size(); index++) {
				requestMusicURL_URL.add(REQUEST_URL_URL+"&id="+requestMusicID.get(index)+"&source="+requestMusicSource.get(index));
			}
		}else{
			throw new RuntimeException("requestMusicList is null");
		}
		return requestMusicURL_URL;
	}

	/**
	 * 获得单条音乐的播放地址 要哪个就去拿哪一个
	 * @param requestMusicID
	 * @param requestMusicSource
	 * @return
	 */
	public String getMusicPlayURL(String requestMusicID,String requestMusicSource) throws Exception {
		//第一步： 获得request（Get请求）请求的url,也就是拼装url
		String url = REQUEST_URL_URL + "&id=" + requestMusicID + "&source=" + requestMusicSource;
		//第二步： 发起请求，解析数据
		String  mp3PlayURL = fengeURL(getMusicPlayJson(url)).replace("\\","");
		//第三步:
		return  mp3PlayURL;
	}

	/** TODO 获得推荐歌单的音乐（封装的对象并不是Music，而是RecMusic）
	 * TODO 此方法无分页
	 * 获得指定推荐歌单的所有音乐播放地址
	 * 返回的是一个List<Music>对象
	 * @param playListId
	 * @return
	 */
	public List<String> getRecMusicPlayURL(String playListId) throws Exception {
		recMusicList = new ArrayList<>();
		//第一步 拿到歌单的json
		String musicPlayListJson = getMusicPlayJson(REQUEST_URL_PLAYLIST + playListId);
		//第二步 将获得的JSON字符串转成封装好的RecMusic对象
		JSONObject jsonObject = JSONObject.parseObject(musicPlayListJson);
		String playlist = jsonObject.getString("playlist");
		jsonObject = JSONObject.parseObject(playlist);
		String tracks = jsonObject.getString("tracks");
		recMusicList = JSONObject.parseArray(tracks,RecMusic.class);
		//遍历recMusicList集合，拿到歌单中200首歌曲的每一首歌曲的播放地址
		//首先遍历集合，拿到请求的地址
		List<String> recMusicPlayList = new ArrayList<>();
		for (RecMusic recMusic : recMusicList) {
				recMusicPlayList.add(getMusicPlayURL(recMusic.getId(),"netease"));
		}
		return recMusicPlayList;
	}


	/** TODO 分页获得推荐歌单的音乐（封装的对象并不是Music，而是RecMusic）
	 * TODO 此方法需要重写 此方法有分页
	 * 传入两个参数 一个playListId 一个页数页码 (注意： 这个是歌单列表分页，不是搜索分页)
	 * 再就是注意：这个分页 起始的位置 起 index = 0 始 index = 9 （0~9）一共是十页
	 * @param playListId
	 * @return
	 */
	public List<RecMusic> getRecMusicListByPages(String playListId,int pageIndex) throws Exception {
		recMusicList = new ArrayList<>();
		//第一步 拿到歌单的json
		String musicPlayListJson = getMusicPlayJson(REQUEST_URL_PLAYLIST + playListId);
		//第二步 将获得的JSON字符串转成封装好的RecMusic对象
		JSONObject jsonObject = JSONObject.parseObject(musicPlayListJson);
		String playlist = jsonObject.getString("playlist");
		jsonObject = JSONObject.parseObject(playlist);
		String tracks = jsonObject.getString("tracks");
		recMusicList = JSONObject.parseArray(tracks,RecMusic.class);
		List<RecMusic> recMusicListByPages = new ArrayList<>();
		for (int i = 20 * pageIndex; i < 20 * (1+pageIndex)  ; i++) {
			recMusicListByPages.add(recMusicList.get(i));
		}
		return  recMusicListByPages;
	}

	/**
	 * TODO 返回Music对象的推荐歌单，使用的是Music对象封装的
	 * @param playListId
	 * @param pageIndex
	 * @return
	 * @throws Exception
	 */
	public List<Music> getRecMusListByPages(String playListId,int pageIndex) throws Exception {
		recMusicList = new ArrayList<>();
		//第一步 拿到歌单的json
		String musicPlayListJson = getMusicPlayJson(REQUEST_URL_PLAYLIST + playListId);
		//第二步 将获得的JSON字符串转成封装好的RecMusic对象
		JSONObject jsonObject = JSONObject.parseObject(musicPlayListJson);
		String playlist = jsonObject.getString("playlist");
		jsonObject = JSONObject.parseObject(playlist);
		String tracks = jsonObject.getString("tracks");
		recMusicList = JSONObject.parseArray(tracks,RecMusic.class);
		List<Music> recMusicListByPages = new ArrayList<>();
		for (int i = 20 * pageIndex; i < 20 * (1+pageIndex)  ; i++) {
			Music music  = new Music();
			music.setId(recMusicList.get(i).getId());
			JSONObject al = JSONObject.parseObject(recMusicList.get(i).getAl().toString());
			music.setAlbum(al.getString("name"));
			music.setName(recMusicList.get(i).getName());
			music.setSource("netease");
			JSONObject ar = JSONObject.parseObject(recMusicList.get(i).getAr()[0]);
			music.setArtist(new String[]{ar.getString("name")});
			music.setPic_id(al.getString("pic_str"));
			music.setLyric_id(recMusicList.get(i).getId());
			music.setUrl_id(recMusicList.get(i).getId());
			recMusicListByPages.add(music);
		}
		return recMusicListByPages;
	}

	/**
	 * 分页查询获得MusicList
	 * 传入的参数一共有三个，
	 * 搜索的歌曲名字
	 * 搜索的歌曲的来源
	 * 搜索的歌曲的页码
	 * @param musicName
	 * @param musicSource
	 * @param pageIndex
	 * @return
	 * @throws Exception
	 */
	public List<Music> getMusicPlayURLByPages(String musicName,int musicSource,int pageIndex) throws Exception {
		String reqMusicPlayUrlByPagesUrl = REQUEST_URL_SEARCH + "&name="+musicName+"&source="+chooseMusicSource(musicSource)+"&count=20"+"&pages="+pageIndex;
		String json = getJSON(reqMusicPlayUrlByPagesUrl);
		List<Music> musicList = getMusicList(json);
		return musicList;
	}

	/**
	 * 获得歌曲的播放图片地址
	 * @param musicId
	 * @param musicSource
	 * @return
	 */
	public InputStream getMusicPlayPicUrl(String musicId,String musicPic_id,int musicSource) throws Exception {
		String musicPicRequestUrl = REQUEST_URL_PIC+"&id="+musicId+"&source="+chooseMusicSource(musicSource);
		String json = "1";
		InputStream input = null;
		if(musicSource==2){
			musicPicRequestUrl = REQUEST_URL_PIC+"&id="+musicId+"&source="+chooseMusicSource(musicSource);
			json= getJSON(musicPicRequestUrl).replace("\\","");
		}else if(musicSource==0){
			musicPicRequestUrl = REQUEST_URL_PIC+"&id="+musicPic_id+"&pic_id="+musicId+"&source="+chooseMusicSource(musicSource);
			json= getJSON(musicPicRequestUrl).replace("\\","");
			json = fengeUrl2(json);
			HttpConn httpConn = new HttpConn().setUri(json).setAccept("image/*");
				input  = httpConn.get().getInputStream();
		}
		return input;
	}

	/**
	 * 获得当前播放歌曲的歌词，此方法需要传入两个参数
	 * 一个是歌曲的id
	 * 一个是歌曲的source
	 * @param musicId
	 * @param musicSource
	 * @return
	 * @throws Exception
	 */
	public String getMusicLyric(String musicId,int musicSource)throws Exception{
		String musicLyric = REQUEST_URL_LYRIC + "&id="+musicId+"&source="+chooseMusicSource(musicSource);
		String json = getMusicPlayJson(musicLyric);
		return StringEscapeUtils.unescapeJava(json);
	}
//-------------------------------酷我-----------音乐------------特殊---------------------------------------------------------------

	//TODO 酷我音乐是可以下载周杰伦的音乐的接口哦，所以会特殊一点，所有付费的都可以下载
	public List<Music> getKuWoMusicListByPage(String musicName,int source,int pageIndex) throws Exception {
		String reqUrl = REQUEST_URL_SEARCH_KUWO + musicName + "&page=" + pageIndex;
		//拿到最终的JSON
		String json = tansToZh(getJSON(reqUrl));
		JSONObject jsonObject = JSONObject.parseObject(json);
		String playlist = jsonObject.getString("data");
		kuWoMusicList = JSONObject.parseArray(playlist, KuwoMus.class);
		List<Music> recMusicListByPages = new ArrayList<>();
		for (int i = 30 * pageIndex; i < 30 * (1 + pageIndex); i++) {
			Music music = new Music();
			music.setId(kuWoMusicList.get(i).getRid());
			music.setAlbum(kuWoMusicList.get(i).getAlbum());
			music.setName(kuWoMusicList.get(i).getMusic());
			music.setSource("kuwo");
			music.setArtist(new String[]{kuWoMusicList.get(i).getPeople()});
			music.setPic_id(kuWoMusicList.get(i).getRid());
			music.setLyric_id(kuWoMusicList.get(i).getRid());
			music.setUrl_id(kuWoMusicList.get(i).getRid());
			recMusicListByPages.add(music);
		}
		return recMusicListByPages;
	}

	/**
	 * 传入整页面的List<Music>  最后返回这一页的播放链接
	 *     </>
	 */
	public List<String> getKuWoMusPlayUrl(List<Music> KuWoMusicList) throws Exception {
		kuWoMusPlayUrl = new ArrayList<>();
		for (Music kuwoMus : KuWoMusicList) {
			String json = getJSON(REQUEST_URL_PLAY_KUWO+kuwoMus.getUrl_id());
			JSONObject jsonObject = JSONObject.parseObject(json);
			String playUrl = jsonObject.getString("url");
			kuWoMusPlayUrl.add(playUrl);
		}
		return kuWoMusPlayUrl;
	}

	/**
	 * 返回单条酷我音乐的播放地址
	 */
	public String getKuWoMusPlayUrl(Music kuWoMus) throws Exception {
		String json = getJSON(REQUEST_URL_PLAY_KUWO+kuWoMus.getUrl_id());
		JSONObject jsonObject = JSONObject.parseObject(json);
		return jsonObject.getString("url");
	}

	/** 获得单条音乐的歌词
	 */
	public String getKuWoLyricUrl(Music kuWoMus)throws Exception{
		JSONObject jsonObject = JSONObject.parseObject(getJSON(REQUEST_URL_LYRIC_KUWO+kuWoMus.getUrl_id()));
		return jsonObject.toString();
	}
	/**
	 * 将Unicode编码转为中文
	 */
	public static String tansToZh(String str) {
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch+"" );
		}
		return str;
	}

	/**
	 * 测试方法
	 */
	public static void main(String[] args) throws Exception {
		GetMusic getMusic =new GetMusic();
//		System.out.println(getMusic.getKuWoMusicListByPage("周杰伦", 0, 0));
		System.out.println(getMusic.getKuWoMusPlayUrl(getMusic.getKuWoMusicListByPage("周杰伦", 0, 0)));
//		System.out.println(getMusic.getRecMusListByPages(String.valueOf(MUSIC_PLAY_LIST_1), 1));
//		System.out.println(getMusic.getMusicPlayListCover(MUSIC_PLAY_LIST_1));
//		System.out.println(getMusic.getMusicPlayPicUrl("1809286552","109951165605881639",0));
//		System.out.println(getMusic.getMusicPlayURLByPages("我不对",0,1));
//		String json = getMusic.getJSON("https://api.zhuolin.wang/api.php?types=search&count=20&source=tencent&pages=1&name=%E6%88%91%E4%B8%8D%E5%AF%B9");
//		List<Music> musicList = getMusic.getMusicList(json);
//		System.out.println(musicList);
//		//获得播放的requestMusicID的List
//		List<String> requestMusicIDList = getMusic.requestMusicID(musicList);
//		System.out.println(requestMusicIDList);
//		//获得播放的requestMusicSource的List
//		List<String> requestMusicSourceList = getMusic.requestMusicSource(musicList);
//		System.out.println(requestMusicSourceList);
//		//获得播放的requestMusicURL
//		List<String> requestMusicURLList = getMusic.requestMusicURL(requestMusicIDList, requestMusicSourceList);
//		System.out.println(requestMusicURLList);
//		System.out.println(getMusic.getMusicPlayURL("003x3xWw27Y2nf", "tencent"));
//		System.out.println(MUSIC_PLAY_LIST_4);
	}
}
