package dibang.com;

import java.util.LinkedList;

public interface OnWebTaskFinish {
	void onWebTaskFinished(int id, LinkedList<Object> list, String errorText);
}
