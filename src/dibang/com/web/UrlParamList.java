package dibang.com.web;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class UrlParamList extends ArrayList<UrlParam>
{

	public void removeParam(String key)
	{
		for( UrlParam param: this){
			if( key.equals(param.Key) ){
				this.remove(param);
				break;
			}
		}
	}
	
	public void addParam(String key, String value){
		add(new UrlParam(key, value));
	}
}

