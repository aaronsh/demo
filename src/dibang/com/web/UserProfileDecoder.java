package dibang.com.web;

import java.util.LinkedList;


import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;



public class UserProfileDecoder extends WebBaseDecoder{
	@Override
	protected LinkedList<Object> decodeDocument(Document doc) throws Exception{
		Elements e = doc.select("item[id=_row]");
		test(e);
		e = e.first().children();

		LinkedList<Object> list = new LinkedList<Object>();

		return list;
	}
}
