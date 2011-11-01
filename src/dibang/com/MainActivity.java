package dibang.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	private ListView listviews;

    private static class EfficientAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Bitmap mIcon1;
        private Bitmap mIcon2;
        private Bitmap mFlag;

        public EfficientAdapter(Context context) {
            // Cache the LayoutInflate to avoid asking for a new one each time.
            mInflater = LayoutInflater.from(context);

            // Icons bound to the rows.
            mIcon1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon48x48_1);
            mIcon2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon48x48_2);
            mFlag = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_list_item_arrow);
        }

        /**
         * The number of items in the list is determined by the number of speeches
         * in our array.
         *
         * @see android.widget.ListAdapter#getCount()
         */
        public int getCount() {
            return DATA.length;
        }

        /**
         * Since the data comes from an array, just returning the index is
         * sufficent to get at the data. If we were using a more complex data
         * structure, we would return whatever object represents one row in the
         * list.
         *
         * @see android.widget.ListAdapter#getItem(int)
         */
        public Object getItem(int position) {
            return position;
        }

        /**
         * Use the array index as a unique id.
         *
         * @see android.widget.ListAdapter#getItemId(int)
         */
        public long getItemId(int position) {
            return position;
        }

        /**
         * Make a view to hold each row.
         *
         * @see android.widget.ListAdapter#getView(int, android.view.View,
         *      android.view.ViewGroup)
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            // A ViewHolder keeps references to children views to avoid unneccessary calls
            // to findViewById() on each row.
            ViewHolder holder;

            // When convertView is not null, we can reuse it directly, there is no need
            // to reinflate it. We only inflate a new View when the convertView supplied
            // by ListView is null.
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_icon_text, null);

                // Creates a ViewHolder and store references to the two children views
                // we want to bind data to.
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.text);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon);
                holder.flag = (ImageView) convertView.findViewById(R.id.flag);

                convertView.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (ViewHolder) convertView.getTag();
            }

            // Bind the data efficiently with the holder.
            holder.text.setText(DATA[position]);
            holder.icon.setImageBitmap((position & 1) == 1 ? mIcon1 : mIcon2);
            holder.flag.setImageBitmap(mFlag);

            return convertView;
        }

        static class ViewHolder {
            TextView text;
            ImageView icon;
            ImageView flag;
        }
    }


    private static final String[] DATA = {
            "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam",
            "Abondance", "Ackawi", "Acorn"};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		InitView();
	}

	/***
	 * 初始化布局属性
	 */
	private void InitView() {
		listviews = (ListView) findViewById(R.id.listview);
		SimpleAdapter adapter = new SimpleAdapter(this, getData(),
				R.layout.list_view, new String[] { "img", "username" },
				new int[] { R.id.img, R.id.username });

		listviews.setAdapter(new EfficientAdapter(this));
		
		listviews.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
//				if(arg2 == 6) 
				{
					Intent intent = new Intent(MainActivity.this,ContactOur.class);
					MainActivity.this.startActivity(intent);
				}
			}
		});
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("img", R.drawable.icon);
		map.put("username", "网站设计");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "三维动画");
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "效 果 图");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "戶 型 图");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "电 子 杂 知");
		list.add(map);
	
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "合 作 伙 伴");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("img", R.drawable.icon);
		map.put("username", "联 系 我 们");
		list.add(map);
		
		return list;
	}
}