package dibang.com.handle;



import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TopMenuHandler
{
	private Activity activity = null;
	private ImageView backImageView = null;
	private Context context = null;
	private ImageView searchImageView = null;
	private String topMenuTitle = null;
	private TextView topMenuTitleTextView = null;

	public TopMenuHandler(Activity paramActivity, String paramString)
	{
		Context localContext = paramActivity.getApplicationContext();
		this.context = localContext;
		this.activity = paramActivity;
		this.topMenuTitle = paramString;
		/*    
    TextView title = (TextView)paramActivity.findViewById(R.id.topMenuTitle);
    if( title != null)
    topMenuTitleTextView = title;
		 */    
	}

	public void BindEvent()
	{
		/*	  
	  backImageView = (ImageView)activity.findViewById(R.id.backBtn);
    backImageView.setClickable(true);
    BackTouch localBackTouch = new BackTouch();
    backImageView.setOnTouchListener(localBackTouch);

    searchImageView = (ImageView)activity.findViewById(R.id.searchBtn);
    searchImageView.setClickable(true);
    SearchBtnTouch localSearchBtnTouch = new SearchBtnTouch();
    searchImageView.setOnTouchListener(localSearchBtnTouch);

    if( topMenuTitleTextView == null)
    	topMenuTitleTextView = (TextView)activity.findViewById(R.id.topMenuTitle);

    topMenuTitleTextView.setText(topMenuTitle);
		 */    
	}

	public void BindEvent(String title)
	{
		/*	  
	  backImageView = (ImageView)activity.findViewById(R.id.backBtn);
    backImageView.setClickable(true);
    BackTouch localBackTouch = new BackTouch();
    backImageView.setOnTouchListener(localBackTouch);

    searchImageView = (ImageView)activity.findViewById(R.id.searchBtn);
    searchImageView.setClickable(true);
    SearchBtnTouch localSearchBtnTouch = new SearchBtnTouch();
    searchImageView.setOnTouchListener(localSearchBtnTouch);

    if( topMenuTitleTextView == null)
    	topMenuTitleTextView = (TextView)activity.findViewById(R.id.topMenuTitle);

    topMenuTitle = title;
    topMenuTitleTextView.setText(topMenuTitle);
		 */    
	}

	public class BackTouch
	implements View.OnTouchListener
	{
		public BackTouch()
		{
		}

		public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
		{
			switch (paramMotionEvent.getAction())
			{
			default:
			case 0:
			case 1:
			}
			/*
      while (true)
      {
        Drawable localDrawable1 = TopMenuHandler.this.context.getResources().getDrawable(R.drawable.jiantou_);
        paramView.setBackgroundDrawable(localDrawable1);
//4bc        continue;
        Drawable localDrawable2 = TopMenuHandler.this.context.getResources().getDrawable(R.drawable.jiantou);
        paramView.setBackgroundDrawable(localDrawable2);
        TopMenuHandler.this.activity.finish();
        return false;
      }
			 */
			return false;      
		}
	}

	public class SearchBtnTouch
	implements View.OnTouchListener
	{
		public SearchBtnTouch()
		{
		}

		public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
		{
			switch (paramMotionEvent.getAction())
			{
			default:
			case 0:
			case 1:
			}
			/*      
      while (true)
      {
        ImageView localImageView1 = TopMenuHandler.this.searchImageView;
        Drawable localDrawable1 = TopMenuHandler.this.context.getResources().getDrawable(R.drawable.shousuo_);
        localImageView1.setImageDrawable(localDrawable1);
//4bc        continue;
        boolean bool = TopMenuHandler.this.activity.onSearchRequested();
        ImageView localImageView2 = TopMenuHandler.this.searchImageView;
        Drawable localDrawable2 = TopMenuHandler.this.context.getResources().getDrawable(R.drawable.shousuo);
        localImageView2.setImageDrawable(localDrawable2);
        return false;
      }
			 */
			return false;      
		}
	}
}

/* Location:           E:\workspace\examples\apk-tools\dex2jar\classes_dex2jar.jar
 * Qualified Name:     COM.Bangso.Handler.TopMenuHandler
 * JD-Core Version:    0.6.0
 */