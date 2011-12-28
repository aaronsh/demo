/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package weibo4android;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.util.Log;

import weibo4android.http.AccessToken;
import weibo4android.http.HttpClient;
import weibo4android.http.ImageItem;
import weibo4android.http.PostParameter;
import weibo4android.http.RequestToken;
import weibo4android.http.Response;
import weibo4android.org.json.JSONException;
import weibo4android.org.json.JSONObject;
import weibo4android.util.URLEncodeUtils;

/**
 * A java reporesentation of the <a href="http://open.t.sina.com.cn/wiki/">Weibo API</a>
 * @editor sinaWeibo
 */
/**
 * @author sinaWeibo
 *
 */

public class Weibo extends WeiboSupport implements java.io.Serializable {
	public static String CONSUMER_KEY = "1662238372";
	public static String CONSUMER_SECRET = "15fb5c9d5d68654928fe0d0998d583c9";
    private String baseURL = Configuration.getScheme() + "api.t.sina.com.cn/";
    private String searchBaseURL = Configuration.getScheme() + "api.t.sina.com.cn/";
    private static final long serialVersionUID = -1486360080128882436L;
    
    //----------------------------΢������ API------------------------------------
    /**
     * ����΢���û� (�������˺��������߿���)
     * @param query
     * @return a list of User
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Users/search">users/search </a>
     */
	public List<User> searchUser(Query query) throws WeiboException {
		return User.constructUsers(get(searchBaseURL + "users/search.json",
				query.getParameters(), false));
	}
    /**
     * ����΢������ (�������˺��������߿���)
     * @param query
     * @return
     * @throws WeiboException
     */
    
    public List<SearchResult> search(Query query) throws WeiboException {
        return SearchResult.constructResults(get(searchBaseURL + "search.json", query.getParameters(), true));
    }
    /**
     * ����΢��(���������) (���Ժ��������߿���)
     * @param query
     * @return a list of statuses
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/search">Statuses/search
     */
    public List<Status> statussearch(Query query) throws WeiboException{
    	return Status.constructStatuses(get(searchBaseURL + "statuses/search.json", query.getParameters(), true));
    }
  
    //----------------------------�ղؽӿ�----------------------------------------
 
    /**
     * ��ȡ��ǰ�û����ղ��б�
     * @param page the number of page
     * @return List<Status>
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites">favorites </a>
     * @since Weibo4J 1.2.0
     */
    public List<Status> getFavorites(int page) throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "favorites.json", "page", String.valueOf(page), true));
    }
    


    /**
     * �ղ�һ��΢����Ϣ
     * @param id the ID of the status to favorite
     * @return Status
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites/create">favorites/create </a>
     */
    public Status createFavorite(long id) throws WeiboException {
    	return new Status(http.post(getBaseURL() + "favorites/create/" + id + ".json", true));
    }

    /**
     * ɾ��΢���ղ�.ע�⣺ֻ��ɾ���Լ��ղص���Ϣ
     * @param id the ID of the status to un-favorite
     * @return Status
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites/destroy">favorites/destroy </a>
     */
    public Status destroyFavorite(long id) throws WeiboException {
    	return new Status(http.post(getBaseURL() + "favorites/destroy/" + id + ".json", true));
    }

    /**
     * ����ɾ��΢���ղ�
     * @param ids Ҫɾ����һ�����ղص�΢����ϢID���ð�Ƕ��Ÿ�����һ���ύ����ṩ20��ID
     * @return Status
     * @throws WeiboException
     * @Ricky
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Favorites/destroy_batch">favorites/destroy_batch</a>
     * @since Weibo4J 1.2.0
     */
    public List<Status> destroyFavorites(String ids)throws WeiboException{
    	return Status.constructStatuses(http.post(getBaseURL()+"favorites/destroy_batch.json",
    			new PostParameter[]{new PostParameter("ids",ids)},true));
    }

    public List<Status> destroyFavorites(String...ids)throws WeiboException{
    	 StringBuilder sb = new StringBuilder();
	  	 for(String id : ids) {
	  		 sb.append(id).append(',');
	  	 }
	  	 sb.deleteCharAt(sb.length() - 1);
    	return Status.constructStatuses(http.post(getBaseURL()+"favorites/destroy_batch.json",
    			new PostParameter[]{new PostParameter("ids",sb.toString())},true));
    }
    //----------------------------�˺Žӿ� ----------------------------------------
    

    /**
     * ��֤��ǰ�û�����Ƿ�Ϸ�
     * @return user
     * @since Weibo4J 1.2.0
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/verify_credentials">account/verify_credentials </a>
     */
    public User verifyCredentials() throws WeiboException {
        /*return new User(get(getBaseURL() + "account/verify_credentials.xml"
                , true), this);*/
    	return new User(get(getBaseURL() + "account/verify_credentials.json"
                , true).asJSONObject());
    }

    /**
     * ��������
     * @param name Optional. Maximum of 20 characters.
     * @param gender Optional. Must be m or f
     * @param url Optional. Maximum of 100 characters. Will be prepended with "http://" if not present.
     * @param location Optional. Maximum of 30 characters. The contents are not normalized or geocoded in any way.
     * @param description Optional. Maximum of 160 characters.
     * @return the updated user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/update_profile">account/update_profile </a>
     */
    public User updateProfile(String name, String gender, String url
            , String location, String description) throws WeiboException {
        List<PostParameter> profile = new ArrayList<PostParameter>(5);
        addParameterToList(profile, "name", name);
        addParameterToList(profile, "gender", gender);
        addParameterToList(profile, "url", url);
        addParameterToList(profile, "location", location);
        addParameterToList(profile, "description", description);
        return new User(http.post(getBaseURL() + "account/update_profile.json"
                , profile.toArray(new PostParameter[profile.size()]), true).asJSONObject());
    }
    /**
     * ��������
     * @param name Optional. Maximum of 20 characters.
     * @param gender Optional. Must be m or f
     * @param location Optional. Maximum of 30 characters. The contents are not normalized or geocoded in any way.
     * @param description Optional. Maximum of 160 characters.
     * @return the updated user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since pWeibo
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/update_profile">account/update_profile </a>
     */
    public User updateProfile(String name, String gender, String location, String description) throws WeiboException {
    	List<PostParameter> profile = new ArrayList<PostParameter>(5);
    	addParameterToList(profile, "name", name);
    	addParameterToList(profile, "gender", gender);
    	addParameterToList(profile, "location", location);
    	addParameterToList(profile, "description", description);
    	return new User(http.post(getBaseURL() + "account/update_profile.json"
    			, profile.toArray(new PostParameter[profile.size()]), true).asJSONObject());
    }
    /**
     * ��������
     * @param name Optional. Maximum of 20 characters.
     * @param gender Optional. Must be m or f
     * @param description Optional. Maximum of 160 characters.
     * @return the updated user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since pWeibo
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/update_profile">account/update_profile </a>
     */
    public User updateProfile(String name, String gender,String description) throws WeiboException {
    	List<PostParameter> profile = new ArrayList<PostParameter>(5);
    	addParameterToList(profile, "name", name);
    	addParameterToList(profile, "gender", gender);
    	addParameterToList(profile, "description", description);
    	return new User(http.post(getBaseURL() + "account/update_profile.json"
    			, profile.toArray(new PostParameter[profile.size()]), true).asJSONObject());
    }

    /**
     * ����ͷ��
     * @param image
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/update_profile_image">account/update_profile_image</a>
     */
    public User updateProfileImage(File image)throws WeiboException {
    	return new User(http.multPartURL("image",getBaseURL() + "account/update_profile_image.json",
    			new PostParameter[]{new PostParameter("source",CONSUMER_KEY)},image, true).asJSONObject());
    }

    /**
     *��ȡ��ǰ�û�API����Ƶ������<br>
     * @return the rate limit status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Account/rate_limit_status">account/rate_limit_status </a>
     */
    public RateLimitStatus rateLimitStatus() throws WeiboException {
    	 return new RateLimitStatus(http.get(getBaseURL() + "account/rate_limit_status.json" , true),this);
    }

    
    /**
     * ��ǰ�û��˳���¼
     * @return a user's object
     * @throws WeiboException when Weibo service or network is unavailable
     */
    public User endSession() throws WeiboException {
    	return new User(get(getBaseURL() + "account/end_session.json", true).asJSONObject());
    }
    //----------------------------Tags�ӿ� ----------------------------------------
    
    /**
     * �����û��ı�ǩ��Ϣ
     * @param user_id �û�id
     * @param paging ��ҳ����
     * @return tags
     * @throws WeiboException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags">Tags </a>
     */
    public List<Tag> getTags(String userId,Paging paging) throws WeiboException{
    	return Tag.constructTags(get(getBaseURL()+"tags.json",
    			new PostParameter[]{new PostParameter("user_id",userId)},
    			paging,true));
    }
    /**
     * Ϊ��ǰ��¼�û�����µ��û���ǩ
     * @param tags
     * @return tagid
     * @throws WeiboException
     * @throws JSONException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/create">Tags/create </a>
     */
     public List<Tag> createTags(String ...tags)throws WeiboException{
    	 StringBuffer sb= new StringBuffer();
    	 for(String tag:tags){
    		 sb.append(tag).append(",");
    	 }
    	 sb.deleteCharAt(sb.length()-1);
    	 return createTags(sb.toString());
       }
    
    /**
    * Ϊ��ǰ��¼�û�����µ��û���ǩ
    * @param tags Ҫ������һ���ǩ���ð�Ƕ��Ÿ�����
    * @return tagid
    * @throws WeiboException
    * @throws JSONException
    * @since Weibo4J 1.2.0
    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/create">Tags/create </a>
    */
    
    public List<Tag> createTags(String tags)throws WeiboException{
        return Tag.constructTags(http.post(getBaseURL()+"tags/create.json", 
        new PostParameter[]{new PostParameter("tags",tags)},true));
       
       }
    /**
     * �����û�����Ȥ�ı�ǩ
     * @return a list of tags
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/suggestions">Tags/suggestions </a>
     */
    
    public List<Tag> getSuggestionsTags()throws WeiboException{
    	return Tag.constructTags(get(getBaseURL()+"tags/suggestions.json",true));
    }
    /**
     * ɾ����ǩ
     * @param tag_id
     * @return 
     * @throws WeiboException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/destroy">Tags/destroy </a>
     */
    
    public  boolean destoryTag(String tag_id)throws WeiboException{
        try {
			return  http.post(getBaseURL()+"tags/destroy.json",
			new PostParameter[]{new PostParameter("tag_id",tag_id)},true).asJSONObject().getInt("result") ==0;
		} catch (JSONException e) {
			throw new WeiboException(e);
		}
       }
    /**
     * ɾ��һ���ǩ
     * @param ids
     * @return tagid
     * @throws WeiboException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Tags/destroy_batch">Tags/destroy_batch </a>
     */
    
    public List<Tag> destory_batchTags(String ids)throws WeiboException{
        return Tag.constructTags(http.post(getBaseURL()+"tags/destroy_batch.json",
        new PostParameter[]{new PostParameter("ids",ids)},true));
       }
    
  
    
    //----------------------------�������ӿ� ---------------------------------------

    /**
     * ��ĳ�û������¼�û��ĺ�����
     * @param weiboId �û�id
     * @return the blocked user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see<a href="http://open.t.sina.com.cn/wiki/index.php/Blocks/create">Blocks/create</a>
     */
    public User createBlock(String userid) throws WeiboException {
        return new User(http.post(getBaseURL() + "blocks/create.json",
    			new PostParameter[]{new PostParameter("user_id", userid)}, true).asJSONObject());
    }
    /**
     * ��ĳ�û������¼�û��ĺ�����
     * @param screenName �û��ǳ�
     * @return the blocked user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
      * @see<a href="http://open.t.sina.com.cn/wiki/index.php/Blocks/create">Blocks/create</a>
     */
    public User createBlockByScreenName(String screenName) throws WeiboException {
    	return new User(http.post(getBaseURL() + "blocks/create.json",
    			new PostParameter[]{new PostParameter("screen_name", screenName)}, true).asJSONObject());
    }


    /**
     * Un-blocks the user specified in the ID parameter as the authenticating user.  Returns the un-blocked user in the requested format when successful.
     * @param id the ID or screen_name of the user to block
     * @return the unblocked user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public User destroyBlock(String id) throws WeiboException {
    	return new User(http.post(getBaseURL() + "blocks/destroy.json",
    			new PostParameter[]{new PostParameter("id",id)}, true).asJSONObject());
    }


    /**
     * Tests if a friendship exists between two users.
     * @param id The ID or screen_name of the potentially blocked user.
     * @return  if the authenticating user is blocking a target user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public boolean existsBlock(String id) throws WeiboException {
        try{
            return -1 == get(getBaseURL() + "blocks/exists.json?user_id="+id, true).
                    asString().indexOf("<error>You are not blocking this user.</error>");
        }catch(WeiboException te){
            if(te.getStatusCode() == 404){
                return false;
            }
            throw te;
        }
    }

    /**
     * Returns a list of user objects that the authenticating user is blocking.
     * @return a list of user objects that the authenticating user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<User> getBlockingUsers() throws
            WeiboException {
        return User.constructUsers(get(getBaseURL() +
                "blocks/blocking.xml", true), this);
    }

    /**
     * Returns a list of user objects that the authenticating user is blocking.
     * @param page the number of page
     * @return a list of user objects that the authenticating user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<User> getBlockingUsers(int page) throws
            WeiboException {
        /*return User.constructUsers(get(getBaseURL() +
                "blocks/blocking.xml?page=" + page, true), this);*/
    	return User.constructUsers(get(getBaseURL() +
                "blocks/blocking.json?page=" + page, true));
    }

    /**
     * Returns an array of numeric user ids the authenticating user is blocking.
     * @return Returns an array of numeric user ids the authenticating user is blocking.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public IDs getBlockingUsersIDs() throws WeiboException {
//        return new IDs(get(getBaseURL() + "blocks/blocking/ids.xml", true));
    	return new IDs(get(getBaseURL() + "blocks/blocking/ids.json", true),this);
    }

    //----------------------------Social Graph�ӿ�-----------------------------------
    
    /**
     * �����û���ע��һ���û���ID�б�
     * @param userid �û�id
     * @param paging
     * @return
     * @throws WeiboException 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">Friends/ids</a>
     */
    public IDs getFriendsIDSByUserId(String userid,Paging paging) throws WeiboException{
    	return new IDs(get(baseURL+"friends/ids.json",
    			new PostParameter[]{new PostParameter("user_id", userid)},paging, true),this);
    }
    /**
     * �����û���ע��һ���û���ID�б�
     * @param userid �û��ǳ�
     * @param paging
     * @return
     * @throws WeiboException 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friends/ids">Friends/ids</a>
     */
    public IDs getFriendsIDSByScreenName(String screenName,Paging paging) throws WeiboException{
    	return new IDs(get(baseURL+"friends/ids.json",
    			new PostParameter[]{new PostParameter("screen_name", screenName)},paging, true),this);
    }
    /**
     * �����û��ķ�˿�û�ID�б�
     * @param userid �û�id
     * @param paging
     * @return
     * @throws WeiboException 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">Followers/ids</a>
     */
    public IDs getFollowersIDSByUserId(String userid,Paging paging) throws WeiboException{
    	return new IDs(get(baseURL+"followers/ids.json",
    			new PostParameter[]{new PostParameter("user_id", userid)},paging, true),this);
    }
    /**
     * �����û��ķ�˿�û�ID�б�
     * @param userid �û��ǳ�
     * @param paging
     * @return
     * @throws WeiboException 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Followers/ids">Followers/ids</a>
     */
    public IDs getFollowersIDSByScreenName(String screenName,Paging paging) throws WeiboException{
    	return new IDs(get(baseURL+"followers/ids.json",
    			new PostParameter[]{new PostParameter("screen_name", screenName)},paging, true),this);
    }
    
    
    
    //----------------------------����ӿ�-------------------------------------------
    /**
     * ��ȡ�û��Ļ��⡣
     * @return the result
     * @throws WeiboException when Weibo service or network is unavailable
     * @since  Weibo4J 1.2.0 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends">Trends </a>
     */
    public List<UserTrend> getTrends(String userid,Paging paging) throws WeiboException {
        return UserTrend.constructTrendList(get(baseURL + "trends.json",
        		new PostParameter[]{new PostParameter("user_id", userid)},paging,true));
    }
    /**
     * ��ȡĳһ�����µ�΢��
     * @param trendName ��������
     * @return list status
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/statuses">Trends/statuses </a>
     */
    public List<Status> getTrendStatus(String trendName,Paging paging) throws WeiboException {
    	return Status.constructStatuses(get(baseURL + "trends/statuses.json",
    			new PostParameter[]{new PostParameter("trend_name", trendName)},
    			paging,true));
    }
    /**��עĳһ������
     * @param treandName ��������
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/follow">trends/follow</a>
     */
    public UserTrend trendsFollow(String treandName) throws WeiboException {
			return  new UserTrend(http.post(baseURL+"trends/follow.json",new PostParameter[]{new PostParameter("trend_name", treandName)},true));
    }
    /**ȡ����ĳ����Ĺ�ע��
     * @param trendId ����id
     * @return result
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/destroy">Trends/destroy</a>
     */
    public boolean trendsDestroy(String trendId) throws WeiboException{
    	JSONObject obj= http.delete(baseURL+"trends/destroy.json?trend_id="+trendId+"&source="+Weibo.CONSUMER_KEY, true).asJSONObject();
    	try {
			return obj.getBoolean("result");
		} catch (JSONException e) {
			throw new WeiboException("e");
		}
    }
    /**
     * ��Сʱ�������Ż���
     * @param baseApp �Ƿ���ڵ�ǰӦ������ȡ���ݡ�1��ʾ���ڵ�ǰӦ������ȡ���ݡ�
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/hourly">Trends/hourly</a>
     */
    public List<Trends> getTrendsHourly(Integer baseApp)throws WeiboException{
    	if(baseApp==null)
    		baseApp=0;
    	return Trends.constructTrendsList(get(baseURL+"trends/hourly.json","base_app",baseApp.toString() ,true));
    }
    /**
     * �������һ���ڵ����Ż��⡣
     * @param baseApp �Ƿ���ڵ�ǰӦ������ȡ���ݡ�1��ʾ���ڵ�ǰӦ������ȡ���ݡ�
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/daily">Trends/daily</a>
     */
    public List<Trends> getTrendsDaily(Integer baseApp)throws WeiboException{
    	if(baseApp==null)
    		baseApp=0;
    	return Trends.constructTrendsList(get(baseURL+"trends/daily.json","base_app",baseApp.toString() ,true));
    }
    /**
     * �������һ���ڵ����Ż��⡣
     * @param baseApp �Ƿ���ڵ�ǰӦ������ȡ���ݡ�1��ʾ���ڵ�ǰӦ������ȡ���ݡ�
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Trends/weekly">Trends/weekly</a>
     */
    public List<Trends> getTrendsWeekly(Integer baseApp)throws WeiboException{
    	if(baseApp==null)
    		baseApp=0;
    	return Trends.constructTrendsList(get(baseURL+"trends/weekly.json","base_app",baseApp.toString() ,true));
    }
    
    
    private String toDateStr(Date date){
        if(null == date){
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

  
    //----------------------------��ע�ӿ�-------------------------------------------
    
    /**
     * ��עһ���û�����ע�ɹ��򷵻ع�ע�˵�����
     * @param id Ҫ��ע���û�ID ����΢���ǳ� 
     * @return the befriended user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/create">friendships/create </a>
     * @deprecated
     */
    //������룿ʹ�ñ�40307����Ӧ����post��
    public User createFriendship(String id) throws WeiboException {
    	 return new User(get(getBaseURL() + "friendships/create.json", "id",id, true).asJSONObject());
    }
    /**
     * ��עһ���û�����ע�ɹ��򷵻ع�ע�˵�����
     * @param weiboId ΢���ǳ� 
     * @return the befriended user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/create">friendships/create </a>
     */
    public User createFriendshipByScreenName(String screenName) throws WeiboException {
    	return new User(http.post(getBaseURL() + "friendships/create.json", 
    			new PostParameter[]{new PostParameter("screen_name", screenName)}
    			, true).asJSONObject());
    }
    /**
     * ��עһ���û�����ע�ɹ��򷵻ع�ע�˵�����
     * @param weiboId Ҫ��ע���û�ID 
     * @return the befriended user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/create">friendships/create </a>
     */
    public User createFriendshipByUserid(String userid) throws WeiboException {
    	return new User(http.post(getBaseURL() + "friendships/create.json",
    		new PostParameter[] {new PostParameter("user_id", userid)}
    		, true).asJSONObject());
    }
    /**
     * ȡ����ĳ�û��Ĺ�ע
     * @param id Ҫȡ����ע���û�ID ����΢���ǳ� 
     * @return User
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/destroy">friendships/destroy </a>
     */
    public User destroyFriendship(String id) throws WeiboException {
    	 return new User(http.post(getBaseURL() + "friendships/destroy.json", "id",id, true).asJSONObject());
    }
    /**
     * ȡ����ĳ�û��Ĺ�ע
     * @param weiboId Ҫȡ����ע���û�ID 
     * @return User
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/destroy">friendships/destroy </a>
     */
    public User destroyFriendshipByUserid(int userid) throws WeiboException {
    	return new User(http.post(getBaseURL() + "friendships/destroy.json", "user_id",""+userid, true).asJSONObject());
    }
    /**
     * ȡ����ĳ�û��Ĺ�ע
     * @param weiboId Ҫȡ����ע���û��ǳ�
     * @return User
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/destroy">friendships/destroy </a>
     */
    public User destroyFriendshipByScreenName(String screenName) throws WeiboException {
    	return new User(http.post(getBaseURL() + "friendships/destroy.json", "screen_name",screenName, true).asJSONObject());
    }
    
    //----------------------------�û��ӿ�-------------------------------------------
    /**
     * ���û�ID���ǳƷ����û������Լ��û������·�����һ��΢����Ϣ��
     * @param weiboId �û�ID�����ǳ�(string)
     * @return User
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Users/show">users/show </a>
     * @since Weibo4J 1.2.0
     */
    public User showUser(String user_id) throws WeiboException {
    	 return new User(get(getBaseURL() + "users/show.json",new PostParameter[]{new PostParameter("id", user_id)}
                 ,http.isAuthenticationEnabled()).asJSONObject());
    }
    

    /**
     * ��ȡ��ǰ�û���ע�б�ÿ����ע�û�������һ��΢�������ؽ������עʱ�䵹�����У����¹�ע���û�������ǰ�档
     * @return the list of friends
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
     * @since Weibo4J 1.2.0
     */
    public List<User> getFriendsStatuses() throws WeiboException {
    	return User.constructResult(get(getBaseURL() + "statuses/friends.json", true));
    }
    /**
     * ��ȡ��ǰ�û���ע�б�ÿ����ע�û�������һ��΢�������ؽ������עʱ�䵹�����У����¹�ע���û�������ǰ�档
     * @return the list of friends
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
     * @since Weibo4J 1.2.0
     */
    public List<User>getFriendsStatuses(int cursor) throws WeiboException {
    	return User.constructUser(get(getBaseURL()+"statuses/friends.json"
    			,new PostParameter[]{new PostParameter("cursor", cursor)},true));
    }
    /**
     * ��ȡָ���û���ע�б�ÿ����ע�û�������һ��΢�������ؽ������עʱ�䵹�����У����¹�ע���û�������ǰ�档
     * @param id �û�ID �����ǳ� 
     * @param paging ��ҳ����
     * @return the list of friends
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
     */
    public List<User> getFriendsStatuses(String id, Paging paging) throws WeiboException {
    	return User.constructUsers(get(getBaseURL() + "statuses/friends.json",
    			new PostParameter[]{new PostParameter("id", id)},paging,
                 true));
    }
    /**
     * ��ȡ��ǰ�û���ע�б�ÿ����ע�û�������һ��΢�������ؽ������עʱ�䵹�����У����¹�ע���û�������ǰ�档
     * @param paging controls pagination
     * @return the list of friends
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
     */
    public List<User> getFriendsStatuses(Paging paging) throws WeiboException {
        /*return User.constructUsers(get(getBaseURL() + "statuses/friends.xml", null,
                paging, true), this);*/
    	return User.constructUsers(get(getBaseURL() + "statuses/friends.json", null,
                paging, true));
    }


    /**
     * ��ȡָ���û���ע�б�ÿ����ע�û�������һ��΢�������ؽ������עʱ�䵹�����У����¹�ע���û�������ǰ�档
     * @param id id �û�ID �����ǳ� 
     * @return the list of friends
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends">statuses/friends </a>
     * @since Weibo4J 1.2.0
     */
    public List<User> getFriendsStatuses(String id) throws WeiboException {
        /*return User.constructUsers(get(getBaseURL() + "statuses/friends/" + id + ".xml"
                , false), this);*/
    	return User.constructUsers(get(getBaseURL() + "statuses/friends.json"
    			,new PostParameter[]{new PostParameter("id", id)}
                , false));
    }

    /**
     * ��ȡ��ǰ�û���˿�б���ÿ����˿�û�����һ��΢�������ؽ������עʱ�䵹�����У����¹�ע���û�������ǰ�档
     * @return List
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
     * @since Weibo4J 1.2.0
     */
    public List<User> getFollowersStatuses() throws WeiboException {
//        return User.constructUsers(get(getBaseURL() + "statuses/followers.xml", true), this);
    	return User.constructResult(get(getBaseURL() + "statuses/followers.json", true));
    }
 
    /**
     * ��ȡָ���û���˿�б���ÿ����˿�û�����һ��΢�������ؽ������עʱ�䵹�����У����¹�ע���û�������ǰ�档
     * @param id   �û�ID �����ǳ� 
     * @param paging controls pagination
     * @return List
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
     */
    public List<User> getFollowersStatuses(String id, Paging paging) throws WeiboException {
    	return User.constructUsers(get(getBaseURL() + "statuses/followers.json", 
    			new PostParameter[]{new PostParameter("id", id)},
    			paging, true));
    }

    /**
     * ��ȡ��ǰ�û���˿�б���ÿ����˿�û�����һ��΢�������ؽ������עʱ�䵹�����У����¹�ע���û�������ǰ�档
     * @param paging  ��ҳ����
     * @return List
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
     */
    public List<User> getFollowersStatuses(Paging paging) throws WeiboException {
    	return User.constructUsers(get(getBaseURL() + "statuses/followers.json", null
                , paging, true));
    }
    /**
     * ��ȡ��ǰ�û���˿�б���ÿ����˿�û�����һ��΢�������ؽ������עʱ�䵹�����У����¹�ע���û�������ǰ�档
     * @param cursor ��ҳ����
     * @return List
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
     */
    public List<User> getFollowersStatuses(int cursor) throws WeiboException {
    	return User.constructUser(get(getBaseURL()+"statuses/followers.json",
    			new PostParameter[]{new PostParameter("cursor", cursor)},true));
    }
   
    /**
     * ��ȡָ���û�ǰ20 ��˿�б���ÿ����˿�û�����һ��΢�������ؽ������עʱ�䵹�����У����¹�ע���û�������ǰ�档
     * @return List
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/followers">statuses/followers </a>
     */
    public List<User> getFollowersStatuses(String id) throws WeiboException {
    	 return User.constructUsers(get(getBaseURL() + "statuses/followers.json",new PostParameter[]{new PostParameter("id", id)}, true));
    }

    /**
     * ��ȡϵͳ�Ƽ��û�
     * @param category ����ĳһ�����Ƽ��û�  ����Ŀ¼�μ��ĵ�
     * @return User
     * @throws WeiboException
     * @see<a href="http://open.t.sina.com.cn/wiki/index.php/Users/hot">users/hot</a> 
     * @since Weibo4J 1.2.0
     */
    public List<User> getHotUsers(String category) throws WeiboException{
    	return User.constructUsers(get(getBaseURL()+"users/hot.json","category",  category, true));
    }
    
    /**
     * ���µ�ǰ��¼�û�����ע��ĳ�����ѵı�ע��Ϣ��
     * @param userid ��Ҫ�޸ı�ע��Ϣ���û�ID��
     * @param remark ��ע��Ϣ
     * @return
     * @throws WeiboException
     * @see<a href="http://open.t.sina.com.cn/wiki/index.php/User/friends/update_remark">friends/update_remark</a> 
     */
    public User updateRemark(String userid,String remark) throws WeiboException {
    	if(!URLEncodeUtils.isURLEncoded(remark))
    		remark=URLEncodeUtils.encodeURL(remark);
	return new User(http.post(getBaseURL()+"user/friends/update_remark.json",
    			new PostParameter[]{new PostParameter("user_id", userid),
    			new PostParameter("remark", remark)},
    			true).asJSONObject());
    }
    /**
     * ���µ�ǰ��¼�û�����ע��ĳ�����ѵı�ע��Ϣ��
     * @param userid ��Ҫ�޸ı�ע��Ϣ���û�ID��
     * @param remark ��ע��Ϣ
     * @return
     * @throws WeiboException
     * @see<a href="http://open.t.sina.com.cn/wiki/index.php/User/friends/update_remark">friends/update_remark</a> 
     */
    public User updateRemark(Long userid,String remark) throws WeiboException {
	return updateRemark(Long.toString(userid), remark);
    }
    
    /**
     * ���ص�ǰ�û����ܸ���Ȥ���û�
     * @return user list
     * @throws WeiboException
     * * @see<a href="http://open.t.sina.com.cn/wiki/index.php/Users/suggestions">friends/update_remark</a> 
     */
    public List<User> getSuggestionUsers() throws WeiboException {
    	return User.constructUsers(get(getBaseURL()+"users/suggestions.json","with_reason", "0", true));
    }
    
    
    //-----------------------��ȡ�������ݼ�(timeline)�ӿ�-----------------------------
    /**
     * �������µ�20������΢�������ؽ������ȫʵʱ����Ỻ��60��
     * @return list of statuses of the Public Timeline
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/public_timeline">statuses/public_timeline </a>
     */
    public List<Status> getPublicTimeline() throws
            WeiboException {
        return Status.constructStatuses(get(getBaseURL() +
                "statuses/public_timeline.json", true));
    }
    /**�������µĹ���΢��
     * @param count ÿ�η��صļ�¼��
     * @param baseApp �Ƿ����ȡ��ǰӦ�÷�������Ϣ��0Ϊ���У�1Ϊ����Ӧ�á�
     * @return
     * @throws WeiboException
     */
    public List<Status> getPublicTimeline(int count,int baseApp) throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() +
        "statuses/public_timeline.json", 
        new PostParameter[]{
    		new PostParameter("count", count),
    		new PostParameter("base_app", baseApp)
    	}
        , false));
    }
    /**
     * ��ȡ��ǰ��¼�û���������ע�û�������20��΢����Ϣ��<br/>
     * ���û���¼ http://t.sina.com.cn ���ڡ��ҵ���ҳ���п�����������ͬ��
     * <br>This method calls http://api.t.sina.com.cn/statuses/friends_timeline.format
     *
     * @return list of the Friends Timeline
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
     */
    public List<Status> getFriendsTimeline() throws
            WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/friends_timeline.json", true));
    }
    /**
     *��ȡ��ǰ��¼�û���������ע�û�������΢����Ϣ��<br/>
     * ���û���¼ http://t.sina.com.cn ���ڡ��ҵ���ҳ���п�����������ͬ��
     * @param paging ��ط�ҳ����
     * @return list of the Friends Timeline
     * @throws WeiboException when Weibo service or network is unavailable
     * @since  Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
     */
    public List<Status> getFriendsTimeline(Paging paging) throws
            WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/friends_timeline.json",null, paging, true));
    }
    /**
     * ��ȡ��ǰ��¼�û���������ע�û�����Ĳ�����ָ��΢��id��΢����Ϣ��<br/>
     * ������ָ��΢��id����ʱ�����΢����������id�����΢����
     * @param maxId ָ��΢��id
     * @return
     * @throws WeiboException when Weibo service or network is unavailable
     * @since pWeibo 09-12 00:01
     */
    public List<Status> getFriendsTimelineMax(String maxId) throws WeiboException{
    	return Status.constructStatuses(get(getBaseURL() + "statuses/friends_timeline.json",
    			new PostParameter[]{ new PostParameter( "max_id", maxId) }, true));
    }
    
    /**
     *��ȡ��ǰ��¼�û���������ע�û�������΢����Ϣ��<br/>
     * ���û���¼ http://t.sina.com.cn ���ڡ��ҵ���ҳ���п�����������ͬ��
     * @return list of the home Timeline
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
     * @since  Weibo4J 1.2.0
     */
    public List<Status> getHomeTimeline() throws
            WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/home_timeline.json", true));
    }


    /**
     *��ȡ��ǰ��¼�û���������ע�û�������΢����Ϣ��<br/>
     * ���û���¼ http://t.sina.com.cn ���ڡ��ҵ���ҳ���п�����������ͬ��
     * @param paging controls pagination
     * @return list of the home Timeline
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/friends_timeline"> statuses/friends_timeline </a>
     * @since  Weibo4J 1.2.0
     */
    public List<Status> getHomeTimeline(Paging paging) throws
            WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/home_timeline.json", null, paging, true));
    }
    /**
     * ����ָ���û����·����΢����Ϣ�б�
     * @param id   �����û�ID(int64)����΢���ǳ�(string)����ָ���û�������΢����Ϣ�б�
     * @param paging ��ҳ��Ϣ
     * @return list of the user Timeline
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
     */
    public List<Status> getUserTimeline(String id, Paging paging)
            throws WeiboException {
//        return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline/" + id + ".xml",
//                null, paging, http.isAuthenticationEnabled()), this);
        return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.json",
        		 new PostParameter[]{new PostParameter("id", id)}, paging, http.isAuthenticationEnabled()));
    }
    public List<Status> getUserTimeline(String id,Integer baseAPP,Integer feature, Paging paging)
    throws WeiboException {
    	Map<String,String> maps= new HashMap<String, String>();
    	if(id!=null){
    		maps.put("id", id);
    	}
    	if(baseAPP!=null){
    		maps.put("base_app", baseAPP.toString());
    	}
    	if(feature!=null){
    		maps.put("feature", feature.toString());
    	}
    	return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.json",
    			generateParameterArray(maps), paging, http.isAuthenticationEnabled()));
    }
    /**
     * ����ָ���û����·����΢����Ϣ�б�
     * @param id �����û�ID(int64)����΢���ǳ�(string)����ָ���û�������΢����Ϣ�б�
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
     */
    public List<Status> getUserTimeline(String id) throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.json",
    			new PostParameter[]{new PostParameter("id", id)},
    			http.isAuthenticationEnabled()));
    }
    /**
     *���ص�ǰ�û����·����΢����Ϣ�б�
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
     */
    public List<Status> getUserTimeline() throws
            WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.json"
                , true));
    }

    /**
     *���ص�ǰ�û����·����΢����Ϣ�б�
     * @param paging controls pagination
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
     * @since Weibo4J 1.2.0
     */
    public List<Status> getUserTimeline(Paging paging) throws
            WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/user_timeline.json"
                , null, paging, true));
    }
    /**
     *���ص�ǰ�û����·����΢����Ϣ�б�
     * @param paging controls pagination
     * @param base_app �Ƿ���ڵ�ǰӦ������ȡ���ݡ�1Ϊ���Ʊ�Ӧ��΢����0Ϊ�������ơ�
     * @param feature ΢�����ͣ�0ȫ����1ԭ����2ͼƬ��3��Ƶ��4����. ����ָ�����͵�΢����Ϣ���ݡ�
     * @return the 20 most recent statuses posted in the last 24 hours from the user
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/user_timeline">statuses/user_timeline</a>
     * @since Weibo4J 1.2.0
     */
    public List<Status> getUserTimeline(Integer baseAPP,Integer feature,Paging paging) throws
    WeiboException {
    	return getUserTimeline(null,baseAPP,feature,paging);
    }
    /**
     * ��������20���ᵽ��¼�û���΢����Ϣ��������@username��΢����Ϣ��
     * @return the 20 most recent replies
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/mentions">Statuses/mentions </a>
     */
    public List<Status> getMentions() throws WeiboException {
        return Status.constructStatuses(get(getBaseURL() + "statuses/mentions.json",
                null, true));
    }

    /**
     * ���������ᵽ��¼�û���΢����Ϣ��������@username��΢����Ϣ��
     * @param paging ��ҳ����
     * @return the 20 most recent replies
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/mentions">Statuses/mentions </a>
     */
    public List<Status> getMentions(Paging paging) throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/mentions.json",
                null, paging, true));
        /*return Status.constructStatuses(get(getBaseURL() + "statuses/mentions.xml",
                null, paging, true), this);*/
    }
 
    /**
     * ��������20�����ͼ��յ������ۡ�
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_timeline">Statuses/comments_timeline</a>
     */
    public List<Comment> getCommentsTimeline() throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_timeline.json", true));
    }
    /**
     * ��������n�����ͼ��յ������ۡ�
     * @param paging ��ҳ����
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_timeline">Statuses/comments_timeline</a>
     */
    public List<Comment> getCommentsTimeline(Paging paging) throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_timeline.json",null,paging, true));
    }
    
    /**
     * ��ȡ����20����ǰ�û�����������
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_by_me">Statuses/comments_by_me</a>
     */
    public List<Comment> getCommentsByMe() throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_by_me.json", true));
    }
    /**
     * ��ȡ��ǰ�û�����������
     *@param paging ��ҳ����
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_by_me">Statuses/comments_by_me</a>
     */
    public List<Comment> getCommentsByMe(Paging paging) throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_by_me.json",null,paging, true));
    }
    
    /**
     * ��������20����ǰ��¼�û��յ�������
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_to_me">Statuses/comments_to_me</a>
     */
    public List<Comment> getCommentsToMe() throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_to_me.json", true));
    }
    /**
     * ���ص�ǰ��¼�û��յ�������
     *@param paging ��ҳ����
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments_to_me">Statuses/comments_to_me</a>
     */
    public List<Comment> getCommentsToMe(Paging paging) throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments_to_me.json",null,paging, true));
    }


    
    /**
     * �����û�ת��������20��΢����Ϣ 
     * @param id specifies the id of user
     * @return statuses
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/repost_by_me">Statuses/repost_by_me</a>
     */
    public List<Status> getrepostbyme(String id)throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL()+"statuses/repost_by_me.json","id",id,true));
    }
    /**
     * �����û�ת��������n��΢����Ϣ 
     * @param id specifies the id of user
     * @return statuses
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0 
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/repost_by_me">Statuses/repost_by_me</a>
     */
    public List<Status> getrepostbyme(String id,Paging paging)throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL()+"statuses/repost_by_me.json",
    			new PostParameter[]{new PostParameter("id", id)},
    			paging,true));
    }
    /**
     * ����һ��ԭ��΢��������20��ת��΢����Ϣ���ӿ��޷��Է�ԭ��΢�����в�ѯ��
     * @param id specifies the id of original status.
     * @return a list of statuses object
     * @throws WeiboException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/repost_timeline">Statuses/repost_timeline</a>
     */
    public List<Status>getreposttimeline(String id)throws WeiboException{
    	return Status.constructStatuses(get(getBaseURL()+"statuses/repost_timeline.json",
    			"id", id,true));
    }
    /**
     * ����һ��ԭ��΢��������n��ת��΢����Ϣ���ӿ��޷��Է�ԭ��΢�����в�ѯ��
     * @param id specifies the id of original status.
     * @return a list of statuses object
     * @throws WeiboException
     * @since Weibo4J 1.2.0  
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/repost_timeline">Statuses/repost_timeline</a>
     */
    public List<Status>getreposttimeline(String id,Paging paging)throws WeiboException{
    	return Status.constructStatuses(get(getBaseURL()+"statuses/repost_timeline.json"
    			,new PostParameter[]{new PostParameter("id", id)},
    			paging,true));
    }
    /**
     * ����΢����ϢID����ĳ��΢����Ϣ��20�������б�
     * @param id specifies the ID of status
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0  
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments">Statuses/comments</a>
     */
    public List<Comment> getComments(String id) throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments.json","id",id, true));
    }
    /**
     * ����΢����ϢID����ĳ��΢����Ϣ��n�����б�
     * @param id specifies the ID of status
     * @return a list of comments objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comments">Statuses/comments</a>
     */
    public List<Comment> getComments(String id,Paging paging) throws WeiboException {
    	return Comment.constructComments(get(getBaseURL() + "statuses/comments.json",
    			new PostParameter[]{new PostParameter("id", id)},paging,
    			true));
    }
    /**
     * ������ȡn��΢����Ϣ����������ת������Ҫ��ȡ��������ת������΢����ϢID�б��ö��Ÿ���
     * һ�����������Ի�ȡ100��΢����Ϣ����������ת����
     * @param ids ids a string, separated by commas
     * @return a list of counts objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0  
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/counts">Statuses/counts</a>
     */
    public List<Count> getCounts(String ids) throws WeiboException{
    	return Count.constructCounts(get(getBaseURL() + "statuses/counts.json","ids",ids, true));
    }

    
    /**
     *��ȡ��ǰ�û�Web��վδ����Ϣ��
     * @return count objects
     * @throws WeiboException when Weibo service or network is unavailable
     * @throws JSONException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/unread">Statuses/unread</a>
     */
    public Count getUnread() throws WeiboException{
    	return new Count(get(getBaseURL() + "statuses/unread.json", true));
    }
    /**
     * ��ȡ��ǰ�û�Web��վδ����Ϣ��
     * @param withNewStatus 1��ʾ����а���new_status�ֶΣ�0��ʾ���������new_status�ֶΡ�new_status�ֶα�ʾ�Ƿ�����΢����Ϣ��1��ʾ�У�0��ʾû��
     * @param sinceId ����ֵΪ΢��id���ò��������with_new_status����ʹ�ã�����since_id֮���Ƿ�����΢����Ϣ����
     * @return
     * @throws WeiboException
     * @throws JSONException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/unread">Statuses/unread</a>
     */
    public Count getUnread(Integer withNewStatus,Long sinceId) throws WeiboException, JSONException {
    	Map<String, String> maps= new HashMap<String, String>();
    	if(withNewStatus!=null)
    		maps.put("with_new_status",Integer.toString( withNewStatus));
    	if(sinceId!=null)
    		maps.put("since_id",Long.toString( sinceId));
    	return new Count(get(getBaseURL() + "statuses/unread.json", generateParameterArray(maps),true).asJSONObject());
    }
    
    /**
     *����ǰ��¼�û���ĳ������Ϣ��δ����Ϊ0
     * @param type  1. ��������2.@ me����3. ˽������4. ��ע��
     * @return
     * @throws WeiboException
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/reset_count">statuses/reset_count</a> 
     */
    public Boolean resetCount(int type)throws WeiboException{
    	boolean res=false;
    	JSONObject json = null;
    	    try {
		 json=http.post(getBaseURL()+"statuses/reset_count.json",
			new PostParameter[]{new PostParameter("type",type)},true).asJSONObject();
		res=json.getBoolean("result");
    	    } catch (JSONException je) {
		throw new WeiboException(je.getMessage() + ":" + json,
			je);
	    }
	    return res;
    }

    /**
     * ��������΢���ٷ����б��顢ħ������������Ϣ����������������͡�������࣬�Ƿ����ŵȡ�
     * @param type �������"face":��ͨ���飬"ani"��ħ�����飬"cartoon"����������
     * @param language �������"cnname"���壬"twname"����
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Emotions">Emotions</a> 
     */
    public List<Emotion> getEmotions(String type,String language)throws WeiboException {
    	if(type==null)
    		type="face";
    	if(language==null)
    		language="cnname";
    	Map<String, String> maps= new HashMap<String, String>();
    	maps.put("type", type);
    	maps.put("language", language);
		return Emotion.constructEmotions(get(getBaseURL()+"emotions.json",generateParameterArray(maps),true));
    }
    /**
     * ��������΢���������ĵ���ͨ���顣
     * @return
     * @throws WeiboException
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Emotions">Emotions</a> 
     */
    public List<Emotion> getEmotions()throws WeiboException {
    	return getEmotions(null,null);
    }
    //--------------΢�����ʽӿ�----------
    
    /**
     * ����ID��ȡ����΢����Ϣ���Լ���΢����Ϣ��������Ϣ��
     * @param id Ҫ��ȡ��΢����ϢID
     * @return ΢����Ϣ
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/show">statuses/show </a>
     */
    public Status showStatus(String id) throws WeiboException {
    	return new Status(get(getBaseURL() + "statuses/show/" + id + ".json", true));
    }
    /**
     * ����ID��ȡ����΢����Ϣ���Լ���΢����Ϣ��������Ϣ��
     * @param id Ҫ��ȡ��΢����ϢID
     * @return ΢����Ϣ
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/show">statuses/show </a>
     */
    public Status showStatus(long id) throws WeiboException {
    	return showStatus(Long.toString(id));
    }

    /**
     * ����һ��΢����Ϣ
     * @param status Ҫ������΢����Ϣ�ı�����
     * @return the latest status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/update">statuses/update </a>
     */
    public Status updateStatus(String status) throws WeiboException{

    	return new Status(http.post(getBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status)}, true));
    }
    
    /**
     * ����һ��΢����Ϣ
     * @param status    Ҫ������΢����Ϣ�ı�����
     * @param latitude  γ�ȡ���Ч��Χ��-90.0��+90.0��+��ʾ��γ��
     * @param longitude ���ȡ���Ч��Χ��-180.0��+180.0��+��ʾ������
     * @return the latest status
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/update">statuses/update </a>
     * @since Weibo4J 1.2.0
     */
    public Status updateStatus(String status, double latitude, double longitude) throws WeiboException{
    	return new Status(http.post(getBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status),
                        new PostParameter("lat", latitude),
                        new PostParameter("long", longitude)}, true));
    }

    /**
     * ����һ��΢����Ϣ
     * @param status  Ҫ������΢����Ϣ�ı�����
     * @param inReplyToStatusId Ҫת����΢����ϢID
     * @return the latest status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/update">statuses/update </a>
     */
    public Status updateStatus(String status, long inReplyToStatusId) throws WeiboException {
    	 return new Status(http.post(getBaseURL() + "statuses/update.json",
                 new PostParameter[]{new PostParameter("status", status), new PostParameter("in_reply_to_status_id", String.valueOf(inReplyToStatusId)), new PostParameter("source", source)}, true));
    }

    /**
     *����һ��΢����Ϣ
     * @param status           Ҫ������΢����Ϣ�ı�����
     * @param inReplyToStatusId Ҫת����΢����ϢID
     * @param latitude  γ�ȡ���Ч��Χ��-90.0��+90.0��+��ʾ��γ��
     * @param longitude ���ȡ���Ч��Χ��-180.0��+180.0��+��ʾ������
     * @return the latest status
     * @throws WeiboException when Weibo service or network is unavailable
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/update">statuses/update </a>
     * @since Weibo4J 1.2.0
     */
    public Status updateStatus(String status, long inReplyToStatusId
            , double latitude, double longitude) throws WeiboException {
    	return new Status(http.post(getBaseURL() + "statuses/update.json",
                new PostParameter[]{new PostParameter("status", status),
                        new PostParameter("lat", latitude),
                        new PostParameter("long", longitude),
                        new PostParameter("in_reply_to_status_id",
                                String.valueOf(inReplyToStatusId)),
                        new PostParameter("source", source)}, true));
    }
    
    /**
     * ����ͼƬ΢����Ϣ��Ŀǰ�ϴ�ͼƬ��С����Ϊ<5M��
     * @param status Ҫ������΢����Ϣ�ı�����
     * @param item Ҫ�ϴ���ͼƬ
     * @return the latest status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/upload">statuses/upload </a>
     */
    public Status uploadStatus(String status,ImageItem item) throws WeiboException {
    	if(!URLEncodeUtils.isURLEncoded(status))
    		status=URLEncodeUtils.encodeURL(status);
    	return new Status(http.multPartURL(getBaseURL() + "statuses/upload.json",
                new PostParameter[]{new PostParameter("status", status), new PostParameter("source", source)},item, true));
    }
    /**
     * ����ͼƬ΢����Ϣ��Ŀǰ�ϴ�ͼƬ��С����Ϊ<5M��
     * @param status Ҫ������΢����Ϣ�ı�����
     * @param item Ҫ�ϴ���ͼƬ
     * @param latitude  γ�ȡ���Ч��Χ��-90.0��+90.0��+��ʾ��γ��
     * @param longitude ���ȡ���Ч��Χ��-180.0��+180.0��+��ʾ������
     * @return
     * @throws WeiboException
     */
    public Status uploadStatus(String status,ImageItem item, double latitude, double longitude) throws WeiboException {
    	if(!URLEncodeUtils.isURLEncoded(status))
    		status=URLEncodeUtils.encodeURL(status);
    	return new Status(http.multPartURL(getBaseURL() + "statuses/upload.json",
		new PostParameter[]{new PostParameter("status", status), 
	        new PostParameter("source", source),
		new PostParameter("lat", latitude),
                new PostParameter("long", longitude),	
	},item, true));
    }

    /**
     * ����ͼƬ΢����Ϣ��Ŀǰ�ϴ�ͼƬ��С����Ϊ<5M��
     * @param status Ҫ������΢����Ϣ�ı�����
     * @param file Ҫ�ϴ���ͼƬ
     * @return
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/upload">statuses/upload </a>
     */
    public Status uploadStatus(String status,File file) throws WeiboException {
    	if(!URLEncodeUtils.isURLEncoded(status))
    		status=URLEncodeUtils.encodeURL(status);
    	return new Status(http.multPartURL("pic",getBaseURL() + "statuses/upload.json",
                new PostParameter[]{new PostParameter("status", status), new PostParameter("source", source)},file, true));
    }
    /**
     * ����ͼƬ΢����Ϣ��Ŀǰ�ϴ�ͼƬ��С����Ϊ<5M��
     * @param status Ҫ������΢����Ϣ�ı�����
     * @param file Ҫ�ϴ���ͼƬ
     * @param latitude  γ�ȡ���Ч��Χ��-90.0��+90.0��+��ʾ��γ��
     * @param longitude ���ȡ���Ч��Χ��-180.0��+180.0��+��ʾ������
     * @return
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/upload">statuses/upload </a>
     */
    public Status uploadStatus(String status,File file, double latitude, double longitude) throws WeiboException {
    	if(!URLEncodeUtils.isURLEncoded(status))
    		status=URLEncodeUtils.encodeURL(status);
    	return new Status(http.multPartURL("pic",getBaseURL() + "statuses/upload.json",
		new PostParameter[]{new PostParameter("status", status), 
	    new PostParameter("source", source),
	    new PostParameter("lat", latitude),
            new PostParameter("long", longitude)},
            file, true));
    }
    

    /**
     * Destroys the status specified by the required ID parameter.  The authenticating user must be the author of the specified status.
     * <br>This method calls http://api.t.sina.com.cn/statuses/destroy/id.format
     *
     * @param statusId The ID of the status to destroy.
     * @return the deleted status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/destroy">statuses/destroy </a>
     */
    public Status destroyStatus(long statusId) throws WeiboException {
    	return destroyStatus(Long.toString(statusId));
    }
    public Status destroyStatus(String statusId) throws WeiboException {
	return new Status(http.post(getBaseURL() + "statuses/destroy/" + statusId + ".json",
		new PostParameter[0], true));
    }
    
    /**
     * ת��΢��
     * @param sid Ҫת����΢��ID
     * @param status ��ӵ�ת���ı���������URLEncode,��Ϣ���ݲ�����140�����֡��粻����Ĭ��Ϊ��ת��΢������
     * @return a single status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public Status repost(String sid,String status) throws WeiboException {
    	return repost(sid, status,0);
    }
    /**
     * ת��΢��
     * @param sid Ҫת����΢��ID
     * @param status ��ӵ�ת���ı���������URLEncode,��Ϣ���ݲ�����140�����֡��粻����Ĭ��Ϊ��ת��΢������
     * @param isComment �Ƿ���ת����ͬʱ�������ۡ�1��ʾ�������ۣ�0��ʾ������.
     * @return a single status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public Status repost(String sid,String status,int isComment) throws WeiboException {
	return new Status(http.post(getBaseURL() + "statuses/repost.json",
		new PostParameter[]{new PostParameter("id", sid),
	    new PostParameter("status", status),
	    new PostParameter("is_comment", isComment)}, true));
    }
    
    /**
    *��һ��΢����Ϣ��������
    * @param �������ݡ�������URLEncode,��Ϣ���ݲ�����140�����֡�
    * @param id Ҫ���۵�΢����ϢID
    * @param cid Ҫ�ظ�������ID,����Ϊnull.���id��cid�����ڣ�������400����
    * </br>����ṩ����ȷ��cid��������ýӿڵı���Ϊ�ظ�ָ�������ۡ�<br/>��ʱid�����������ԡ�<br/>��ʹcid������������۲�����id���������΢����Ϣ��ͨ���ýӿڷ����������Ϣֱ�ӻظ�cid��������ۡ�
    * @return the comment object
    * @throws WeiboException
    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comment">Statuses/comment</a>
    */
   public Comment updateComment(String comment, String id, String cid) throws WeiboException {
       PostParameter[] params = null;
       if (cid == null)
       	params = new PostParameter[] {
   			new PostParameter("comment", comment),
   			new PostParameter("id", id)
   		};
       else
       	params = new PostParameter[] {
   			new PostParameter("comment", comment),
   			new PostParameter("cid", cid),
   			new PostParameter("id", id)
   		};
       return new Comment(http.post(getBaseURL() + "statuses/comment.json", params, true));
   }
   /**
    * ɾ�����ۡ�ע�⣺ֻ��ɾ����¼�û��Լ����������ۣ�������ɾ�������˵����ۡ�
    * @param statusId ��ɾ��������ID
    * @return the deleted status
    * @throws WeiboException when Weibo service or network is unavailable
    * @since Weibo4J 1.2.0
    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comment_destroy">statuses/comment_destroy </a>
    */
	public Comment destroyComment(long commentId) throws WeiboException {
		return new Comment(http.delete(getBaseURL()
				+ "statuses/comment_destroy/" + commentId + ".json?source="
				+ CONSUMER_KEY, true));
	}

   /**
    * ����ɾ�����ۡ�ע�⣺ֻ��ɾ����¼�û��Լ����������ۣ�������ɾ�������˵����ۡ�
    * @Ricky
    * @param ids ��ɾ����һ������ID���ð�Ƕ��Ÿ��������20��
    * @return
    * @throws WeiboException
    * @since Weibo4J 1.2.0
    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/comment/destroy_batch">statuses/comment/destroy batch</a> 
    */
   public List<Comment> destroyComments(String ids)throws WeiboException{
   	return Comment.constructComments(http.post(getBaseURL()+"statuses/comment/destroy_batch.json",
   			new PostParameter[]{new PostParameter("ids",ids)},true));
   }
   public List<Comment> destroyComments(String[] ids)throws WeiboException{
   	StringBuilder sb = new StringBuilder();
	    for(String id : ids) {
		   sb.append(id).append(',');
	    }
	    sb.deleteCharAt(sb.length() - 1);
   	return Comment.constructComments(http.post(getBaseURL()+"statuses/comment/destroy_batch.json",
   			new PostParameter[]{new PostParameter("ids",sb.toString())},true));
   }

   /**
    * �ظ�����
    * @param sid Ҫ�ظ�������ID��
    * @param cid Ҫ���۵�΢����ϢID
    * @param comment Ҫ�ظ����������ݡ�������URLEncode,��Ϣ���ݲ�����140������
    * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Statuses/reply">Statuses/reply</a> 
    * @throws WeiboException when Weibo service or network is unavailable
    * @since Weibo4J 1.2.0
    */
	public Comment reply(String sid, String cid, String comment)
			throws WeiboException {
		return new Comment(http.post(getBaseURL() + "statuses/reply.json",
				new PostParameter[] { new PostParameter("id", sid),
						new PostParameter("cid", cid),
						new PostParameter("comment", comment) }, true));
	}
   
  //--------------auth method----------
    /**
     *
     * @param consumerKey OAuth consumer key
     * @param consumerSecret OAuth consumer secret
     * @since Weibo4J 1.2.0
     */
    public synchronized void setOAuthConsumer(String consumerKey, String consumerSecret){
        this.http.setOAuthConsumer(consumerKey, consumerSecret);
    }

    /**
     * Retrieves a request token
     * @return generated request token.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://oauth.net/core/1.0/#auth_step1">OAuth Core 1.0 - 6.1.  Obtaining an Unauthorized Request Token</a>
     */
    public RequestToken getOAuthRequestToken() throws WeiboException {
        return http.getOAuthRequestToken();
    }

    public RequestToken getOAuthRequestToken(String callback_url) throws WeiboException {
        return http.getOauthRequestToken(callback_url);
    }

    /**
     * Retrieves an access token assosiated with the supplied request token.
     * @param requestToken the request token
     * @return access token associsted with the supplied request token.
     * @throws WeiboException when Weibo service or network is unavailable, or the user has not authorized
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Oauth/access_token">Oauth/access token </a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since Weibo4J 1.2.0
     */
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken) throws WeiboException {
        return http.getOAuthAccessToken(requestToken);
    }

    /**
     * Retrieves an access token assosiated with the supplied request token and sets userId.
     * @param requestToken the request token
     * @param pin pin
     * @return access token associsted with the supplied request token.
     * @throws WeiboException when Weibo service or network is unavailable, or the user has not authorized
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Oauth/access_token">Oauth/access token </a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since  Weibo4J 1.2.0
     */
    public synchronized AccessToken getOAuthAccessToken(RequestToken requestToken, String pin) throws WeiboException {
        AccessToken accessToken = http.getOAuthAccessToken(requestToken, pin);
        return accessToken;
    }

    /**
     * Retrieves an access token assosiated with the supplied request token and sets userId.
     * @param token request token
     * @param tokenSecret request token secret
     * @return access token associsted with the supplied request token.
     * @throws WeiboException when Weibo service or network is unavailable, or the user has not authorized
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Oauth/access_token">Oauth/access token </a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since  Weibo4J 1.2.0
     */
    public synchronized AccessToken getOAuthAccessToken(String token, String tokenSecret) throws WeiboException {
        AccessToken accessToken = http.getOAuthAccessToken(token, tokenSecret);
        return accessToken;
    }

    /**
     * Retrieves an access token assosiated with the supplied request token.
     * @param token request token
     * @param tokenSecret request token secret
     * @param oauth_verifier oauth_verifier or pin
     * @return access token associsted with the supplied request token.
     * @throws WeiboException when Weibo service or network is unavailable, or the user has not authorized
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Oauth/access_token">Oauth/access token </a>
     * @see <a href="http://oauth.net/core/1.0/#auth_step2">OAuth Core 1.0 - 6.2.  Obtaining User Authorization</a>
     * @since  Weibo4J 1.2.0
     */
    public synchronized AccessToken getOAuthAccessToken(String token
            , String tokenSecret, String oauth_verifier) throws WeiboException {
        return http.getOAuthAccessToken(token, tokenSecret, oauth_verifier);
    }

    public synchronized AccessToken getXAuthAccessToken(String userId,String passWord,String mode) throws WeiboException {
    	return http.getXAuthAccessToken(userId, passWord, mode);
    }
    public synchronized AccessToken getXAuthAccessToken(String userid, String password) throws WeiboException {
	return getXAuthAccessToken(userid,password,Constants.X_AUTH_MODE);
	
    }
    /**
     * Sets the access token
     * @param accessToken accessToken
     * @since  Weibo4J 1.2.0
     */
    public void setOAuthAccessToken(AccessToken accessToken){
        this.http.setOAuthAccessToken(accessToken);
    }

    /**
     * Sets the access token
     * @param token token
     * @param tokenSecret token secret
     * @since  Weibo4J 1.2.0
     */
    public void setOAuthAccessToken(String token, String tokenSecret) {
        setOAuthAccessToken(new AccessToken(token, tokenSecret));
    }


 

    /* Status Methods */

 
    public RateLimitStatus getRateLimitStatus()throws
            WeiboException {
    	/*modify by sycheng edit with json */
        return new RateLimitStatus(get(getBaseURL() +
                "account/rate_limit_status.json", true),this);
    }

   

  

   
    /**
     * Returns the 20 most recent retweets posted by the authenticating user.
     *
     * @return the 20 most recent retweets posted by the authenticating user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<Status> getRetweetedByMe() throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_by_me.json",
                null, true));
        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_by_me.xml",
                null, true), this);*/
    }

    /**
     * Returns the 20 most recent retweets posted by the authenticating user.
     * @param paging controls pagination
     * @return the 20 most recent retweets posted by the authenticating user
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<Status> getRetweetedByMe(Paging paging) throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_by_me.json",
                null, true));
        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_by_me.xml",
                null, paging, true), this);*/
    }

    /**
     * Returns the 20 most recent retweets posted by the authenticating user's friends.
     * @return the 20 most recent retweets posted by the authenticating user's friends.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<Status> getRetweetedToMe() throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_to_me.json",
                null, true));
        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_to_me.xml",
                null, true), this);*/
    }

    /**
     * Returns the 20 most recent retweets posted by the authenticating user's friends.
     * @param paging controls pagination
     * @return the 20 most recent retweets posted by the authenticating user's friends.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<Status> getRetweetedToMe(Paging paging) throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_to_me.json",
                null, paging, true));
        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweeted_to_me.xml",
                null, paging, true), this);*/
    }

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<Status> getRetweetsOfMe() throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweets_of_me.json",
                null, true));
        /*return Status.constructStatuses(get(getBaseURL() + "statuses/retweets_of_me.xml",
                null, true), this);*/
    }

    /**
     * Returns the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @param paging controls pagination
     * @return the 20 most recent tweets of the authenticated user that have been retweeted by others.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<Status> getRetweetsOfMe(Paging paging) throws WeiboException {
    	return Status.constructStatuses(get(getBaseURL() + "statuses/retweets_of_me.json",
                null, paging, true));
    	/* return Status.constructStatuses(get(getBaseURL() + "statuses/retweets_of_me.xml",
                null, paging, true), this);*/
    }


    /**
     * Retweets a tweet. Requires the id parameter of the tweet you are retweeting. Returns the original tweet with retweet details embedded.
     * @param statusId The ID of the status to retweet.
     * @return the retweeted status
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public Status retweetStatus(long statusId) throws WeiboException {
        /*return new Status(http.post(getBaseURL() + "statuses/retweet/" + statusId + ".xml",
                new PostParameter[0], true), this);*/
    	return new Status(http.post(getBaseURL() + "statuses/retweet/" + statusId + ".json",
                new PostParameter[0], true));
    }

    /**
     * Returns up to 100 of the first retweets of a given tweet.
     * @param statusId The numerical ID of the tweet you want the retweets of.
     * @return the retweets of a given tweet
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<RetweetDetails> getRetweets(long statusId) throws WeiboException {
       /* return RetweetDetails.createRetweetDetails(get(getBaseURL()
                + "statuses/retweets/" + statusId + ".xml", true), this);*/
    	 return RetweetDetails.createRetweetDetails(get(getBaseURL()
                 + "statuses/retweets/" + statusId + ".json", true));
    }

    /**
     * Returns a list of the users currently featured on the site with their current statuses inline.
     *
     * @return List of User
     * @throws WeiboException when Weibo service or network is unavailable
     */
    public List<User> getFeatured() throws WeiboException {
//        return User.constructUsers(get(getBaseURL() + "statuses/featured.xml", true), this);
        return User.constructUsers(get(getBaseURL() + "statuses/featured.json", true));
    }

  

   

 

  

  

    /**
     * Tests if a friendship exists between two users.
     *
     * @param userA The ID or screen_name of the first user to test friendship for.
     * @param userB The ID or screen_name of the second user to test friendship for.
     * @return if a friendship exists between two users.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     * @see <a href="http://open.t.sina.com.cn/wiki/index.php/Friendships/exists">friendships/exists </a>
     */
    public boolean existsFriendship(String userA, String userB) throws WeiboException {
        return -1 != get(getBaseURL() + "friendships/exists.json", "user_a", userA, "user_b", userB, true).
                asString().indexOf("true");
    }

    private void addParameterToList(List<PostParameter> colors,
                                      String paramName, String color) {
        if(null != color){
            colors.add(new PostParameter(paramName,color));
        }
    }

   

    /**
     * Enables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * @param id String
     * @return User
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public User enableNotification(String id) throws WeiboException {
//        return new User(http.post(getBaseURL() + "notifications/follow/" + id + ".xml", true), this);
    	return new User(http.post(getBaseURL() + "notifications/follow/" + id + ".json", true).asJSONObject());
    }

    /**
     * Disables notifications for updates from the specified user to the authenticating user.  Returns the specified user when successful.
     * @param id String
     * @return User
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public User disableNotification(String id) throws WeiboException {
//        return new User(http.post(getBaseURL() + "notifications/leave/" + id + ".xml", true), this);
    	return new User(http.post(getBaseURL() + "notifications/leave/" + id + ".json", true).asJSONObject());
    }

   

    /* Saved Searches Methods */
    /**
     * Returns the authenticated user's saved search queries.
     * @return Returns an array of numeric user ids the authenticating user is blocking.
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public List<SavedSearch> getSavedSearches() throws WeiboException {
        return SavedSearch.constructSavedSearches(get(getBaseURL() + "saved_searches.json", true));
    }

    /**
     * Retrieve the data for a saved search owned by the authenticating user specified by the given id.
     * @param id The id of the saved search to be retrieved.
     * @return the data for a saved search
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public SavedSearch showSavedSearch(int id) throws WeiboException {
        return new SavedSearch(get(getBaseURL() + "saved_searches/show/" + id
                + ".json", true));
    }

    /**
     * Retrieve the data for a saved search owned by the authenticating user specified by the given id.
     * @return the data for a created saved search
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public SavedSearch createSavedSearch(String query) throws WeiboException {
        return new SavedSearch(http.post(getBaseURL() + "saved_searches/create.json"
                , new PostParameter[]{new PostParameter("query", query)}, true));
    }

    /**
     * Destroys a saved search for the authenticated user. The search specified by id must be owned by the authenticating user.
     * @param id The id of the saved search to be deleted.
     * @return the data for a destroyed saved search
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public SavedSearch destroySavedSearch(int id) throws WeiboException {
        return new SavedSearch(http.post(getBaseURL() + "saved_searches/destroy/" + id
                + ".json", true));
    }


	/**
	 * Obtain the ListObject feed list
	 * @param uid		User ID or screen_name
	 * @param listId	The ID or slug of ListObject
	 * @param auth		if true, the request will be sent with BASIC authentication header
	 * @return
	 * @throws WeiboException
	 */
	public List<Status> getListStatuses(String uid, String listId, boolean auth) throws WeiboException {
		StringBuilder sb = new StringBuilder();
		sb.append(getBaseURL()).append(uid).append("/lists/").append(listId).append("/statuses.xml").append("?source=").append(CONSUMER_KEY);
		String httpMethod = "GET";
		String url = sb.toString();
		//
		return Status.constructStatuses(http.httpRequest(url, null, auth, httpMethod), this);
	}


	/**
	 * Obtain ListObject member list
	 * @param uid		User ID or screen_name
	 * @param listId	The ID or slug of ListObject
	 * @param auth		if true, the request will be sent with BASIC authentication header
	 * @return
	 * @throws WeiboException
	 */
	public UserWapper getListMembers(String uid, String listId, boolean auth) throws WeiboException {
		StringBuilder sb = new StringBuilder();
		sb.append(getBaseURL()).append(uid).append("/").append(listId).append("/members.xml").append("?source=").append(CONSUMER_KEY);
		String httpMethod = "GET";
		String url = sb.toString();
		//
		return User.constructWapperUsers(http.httpRequest(url, null, auth, httpMethod), this);
	}

	/**
	 * Obtain ListObject subscribe user's list 
	 * @param uid		User ID or screen_name
	 * @param listId	The ID or slug of ListObject
	 * @param auth		if true, the request will be sent with BASIC authentication header
	 * @return
	 * @throws WeiboException
	 */
	public UserWapper getListSubscribers(String uid, String listId, boolean auth) throws WeiboException {
		StringBuilder sb = new StringBuilder();
		sb.append(getBaseURL()).append(uid).append("/").append(listId).append("/subscribers.xml").append("?source=").append(CONSUMER_KEY);
		String httpMethod = "GET";
		String url = sb.toString();
		//
		return User.constructWapperUsers(http.httpRequest(url, null, auth, httpMethod), this);
	}



	/**
	 * confirm list member
	 * @param uid		User ID or screen_name
	 * @param listId	The ID or slug of ListObject
	 * @param targetUid	Target user ID or screen_name
	 * @param auth		if true, the request will be sent with BASIC authentication header
	 * @return
	 * @throws WeiboException
	 */
	public boolean isListMember(String uid, String listId, String targetUid, boolean auth)
			throws WeiboException {
		StringBuilder sb = new StringBuilder();
		sb.append(getBaseURL()).append(uid).append("/").append(listId).append("/members/").append(targetUid)
				.append(".xml").append("?source=").append(CONSUMER_KEY);
		String url = sb.toString();
		//
		String httpMethod = "GET";
		//
		Document doc = http.httpRequest(url, null, auth, httpMethod).asDocument();
		Element root = doc.getDocumentElement();
		return "true".equals(root.getNodeValue());
	}

	/**
	 * confirm subscription list
	 * @param uid		User ID or screen_name
	 * @param listId	The ID or slug of ListObject
	 * @param targetUid	Target user ID or screen_name
	 * @param auth		if true, the request will be sent with BASIC authentication header
	 * @return
	 * @throws WeiboException
	 */
	public boolean isListSubscriber(String uid, String listId, String targetUid, boolean auth)
			throws WeiboException {
		StringBuilder sb = new StringBuilder();
		sb.append(getBaseURL()).append(uid).append("/").append(listId).append("/subscribers/").append(targetUid)
				.append(".xml").append("?source=").append(CONSUMER_KEY);
		String url = sb.toString();
		//
		String httpMethod = "GET";
		//
		Document doc = http.httpRequest(url, null, auth, httpMethod).asDocument();
		Element root = doc.getDocumentElement();
		return "true".equals(root.getNodeValue());
	}

    /* Help Methods */
    /**
     * Returns the string "ok" in the requested format with a 200 OK HTTP status code.
     * @return true if the API is working
     * @throws WeiboException when Weibo service or network is unavailable
     * @since Weibo4J 1.2.0
     */
    public boolean test() throws WeiboException {
        return -1 != get(getBaseURL() + "help/test.json", false).
                asString().indexOf("ok");
    }

    private SimpleDateFormat format = new SimpleDateFormat(
            "EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weibo weibo = (Weibo) o;

        if (!baseURL.equals(weibo.baseURL)) return false;
        if (!format.equals(weibo.format)) return false;
        if (!http.equals(weibo.http)) return false;
        if (!searchBaseURL.equals(weibo.searchBaseURL)) return false;
        if (!source.equals(weibo.source)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = http.hashCode();
        result = 31 * result + baseURL.hashCode();
        result = 31 * result + searchBaseURL.hashCode();
        result = 31 * result + source.hashCode();
        result = 31 * result + format.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Weibo{" +
                "http=" + http +
                ", baseURL='" + baseURL + '\'' +
                ", searchBaseURL='" + searchBaseURL + '\'' +
                ", source='" + source + '\'' +
                ", format=" + format +
                '}';
    }

 
  

   

    /**
     * Return your relationship with the details of a user
     * @param target_id id of the befriended user
     * @return jsonObject
     * @throws WeiboException when Weibo service or network is unavailable
     */
    public JSONObject showFriendships(String target_id) throws WeiboException {
    	return get(getBaseURL() + "friendships/show.json?target_id="+target_id, true).asJSONObject();
    }

    /**
     * Return the details of the relationship between two users
     * @param target_id id of the befriended user
     * @return jsonObject
     * @throws WeiboException when Weibo service or network is unavailable
     * @Ricky  Add source parameter&missing "="
     */
    public JSONObject showFriendships(String source_id,String target_id) throws WeiboException {
    	return get(getBaseURL() + "friendships/show.json?target_id="+target_id+"&source_id="+source_id+"&source="+CONSUMER_KEY, true).asJSONObject();
    }

  

    /**
     *  Return infomation of current user
     * @param ip a specified ip(Only open to invited partners)
     * @param args User Information args[2]:nickname,args[3]:gender,args[4],password,args[5]:email
     * @return jsonObject
     * @throws WeiboException when Weibo service or network is unavailable
     */
    public JSONObject register(String ip,String ...args) throws WeiboException {
    	return http.post(getBaseURL() + "account/register.json",
                new PostParameter[]{new PostParameter("nick", args[2]),
									new PostParameter("gender", args[3]),
									new PostParameter("password", args[4]),
									new PostParameter("email", args[5]),
									new PostParameter("ip", ip)}, true).asJSONObject();
    }
   
  


  //--------------base method----------
    public Weibo() {
        super();
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        http.setRequestTokenURL(Configuration.getScheme() + "api.t.sina.com.cn/oauth/request_token");
        http.setAuthorizationURL(Configuration.getScheme() + "api.t.sina.com.cn/oauth/authorize");
        http.setAccessTokenURL(Configuration.getScheme() + "api.t.sina.com.cn/oauth/access_token");
    }

    /**
     * Sets token information
     * @param token
     * @param tokenSecret
     */
    public void setToken(String token, String tokenSecret) {
        http.setToken(token, tokenSecret);
    }

    public Weibo(String baseURL) {
        this();
        this.baseURL = baseURL;
    }

    /**
     * Sets the base URL
     *
     * @param baseURL String the base URL
     */
    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    /**
     * Returns the base URL
     *
     * @return the base URL
     */
    public String getBaseURL() {
        return this.baseURL;
    }

    /**
     * Sets the search base URL
     *
     * @param searchBaseURL the search base URL
     * @since Weibo4J 1.2.0
     */
    public void setSearchBaseURL(String searchBaseURL) {
        this.searchBaseURL = searchBaseURL;
    }

    /**
     * Returns the search base url
     * @return search base url
     * @since Weibo4J 1.2.0
     */
    public String getSearchBaseURL(){
        return this.searchBaseURL;
    }
    
    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws WeiboException when Weibo service or network is unavailable
     */

    private Response get(String url, boolean authenticate) throws WeiboException {
        return get(url, null, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @param name1        the name of the first parameter
     * @param value1       the value of the first parameter
     * @return the response
     * @throws WeiboException when Weibo service or network is unavailable
     */

    protected Response get(String url, String name1, String value1, boolean authenticate) throws WeiboException {
        return get(url, new PostParameter[]{new PostParameter(name1, value1)}, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param name1        the name of the first parameter
     * @param value1       the value of the first parameter
     * @param name2        the name of the second parameter
     * @param value2       the value of the second parameter
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws WeiboException when Weibo service or network is unavailable
     */

    protected Response get(String url, String name1, String value1, String name2, String value2, boolean authenticate) throws WeiboException {
        return get(url, new PostParameter[]{new PostParameter(name1, value1), new PostParameter(name2, value2)}, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param params       the request parameters
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws WeiboException when Weibo service or network is unavailable
     */
    protected Response get(String url, PostParameter[] params, boolean authenticate) throws WeiboException {
		if (url.indexOf("?") == -1) {
			url += "?source=" + CONSUMER_KEY;
		} else if (url.indexOf("source") == -1) {
			url += "&source=" + CONSUMER_KEY;
		}
    	if (null != params && params.length > 0) {
			url += "&" + HttpClient.encodeParameters(params);
		}
        return http.get(url, authenticate);
    }

    /**
     * Issues an HTTP GET request.
     *
     * @param url          the request url
     * @param params       the request parameters
     * @param paging controls pagination
     * @param authenticate if true, the request will be sent with BASIC authentication header
     * @return the response
     * @throws WeiboException when Weibo service or network is unavailable
     */
    protected Response get(String url, PostParameter[] params, Paging paging, boolean authenticate) throws WeiboException {
        if (null != paging) {
            List<PostParameter> pagingParams = new ArrayList<PostParameter>(4);
            if (-1 != paging.getMaxId()) {
                pagingParams.add(new PostParameter("max_id", String.valueOf(paging.getMaxId())));
            }
            if (-1 != paging.getSinceId()) {
                pagingParams.add(new PostParameter("since_id", String.valueOf(paging.getSinceId())));
            }
            if (-1 != paging.getPage()) {
                pagingParams.add(new PostParameter("page", String.valueOf(paging.getPage())));
            }
            if (-1 != paging.getCount()) {
                if (-1 != url.indexOf("search")) {
                    // search api takes "rpp"
                    pagingParams.add(new PostParameter("rpp", String.valueOf(paging.getCount())));
                } else {
                    pagingParams.add(new PostParameter("count", String.valueOf(paging.getCount())));
                }
            }
            PostParameter[] newparams = null;
            PostParameter[] arrayPagingParams = pagingParams.toArray(new PostParameter[pagingParams.size()]);
            if (null != params) {
                newparams = new PostParameter[params.length + pagingParams.size()];
                System.arraycopy(params, 0, newparams, 0, params.length);
                System.arraycopy(arrayPagingParams, 0, newparams, params.length, pagingParams.size());
            } else {
                if (0 != arrayPagingParams.length) {
                    String encodedParams = HttpClient.encodeParameters(arrayPagingParams);
                    if (-1 != url.indexOf("?")) {
                        url += "&source=" + CONSUMER_KEY +
                        		"&" + encodedParams;
                    } else {
                        url += "?source=" + CONSUMER_KEY +
                        		"&" + encodedParams;
                    }
                }
            }
            return get(url, newparams, authenticate);
        } else {
            return get(url, params, authenticate);
        }
    }

	private PostParameter[] generateParameterArray(Map<String, String> parames)
			throws WeiboException {
		PostParameter[] array = new PostParameter[parames.size()];
		int i = 0;
		for (String key : parames.keySet()) {
			if (parames.get(key) != null) {
				array[i] = new PostParameter(key, parames.get(key));
				i++;
			}
		}
		return array;
}
	  public final static Device IM = new Device("im");
	    public final static Device SMS = new Device("sms");
	    public final static Device NONE = new Device("none");

	    static class Device {
	        final String DEVICE;

	        public Device(String device) {
	            DEVICE = device;
	        }
	    }
	//---------------@deprecated---------------------------------------

	public void setToken(AccessToken accessToken) {
	this.setToken(accessToken.getToken(), accessToken.getTokenSecret());
	
    }
	//----------------------------Tags�ӿ� ----------------------------------------

	
	
}