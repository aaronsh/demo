package dibang.com;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class EfficientAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Bitmap mFlag;
    private String[] DATA = {
    		"网站设计", "三维动画", "效果图",
            "户型图", "电子杂志", "合作伙伴"};
    private int[] ImgId = {
    		R.drawable.wangzhansheji,
    		R.drawable.sanweidonghua,
    		R.drawable.xiaogutu,
    		R.drawable.huxingtu,
    		R.drawable.dianzizazhi,
    		R.drawable.hezuohuoban
    };
    private Bitmap[] IMG = new Bitmap[6];

    public EfficientAdapter(Context context) {
        // Cache the LayoutInflate to avoid asking for a new one each time.
        mInflater = LayoutInflater.from(context);

        // Icons bound to the rows.
        Resources res = context.getResources();
        for(int i=0; i<6; i++){
        	IMG[i] = BitmapFactory.decodeResource(res, ImgId[i]);
        }

        mFlag = BitmapFactory.decodeResource(res, R.drawable.app_list_item_arrow);
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
    	ListItemViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_icon_text, null);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ListItemViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.flag = (ImageView) convertView.findViewById(R.id.flag);

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ListItemViewHolder) convertView.getTag();
        }

        // Bind the data efficiently with the holder.
        holder.text.setText(DATA[position]);
        holder.icon.setImageBitmap(IMG[position]);
        holder.flag.setImageBitmap(mFlag);

        return convertView;
    }


}

