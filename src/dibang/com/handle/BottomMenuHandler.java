package dibang.com.handle;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class BottomMenuHandler implements View.OnTouchListener,  View.OnClickListener
{
	public static final int HOME = 0;
	public static final int RECORD = 1;
	public static final int FOOD = 2;
	public static final int SPORTS = 3;
	public static final int ACCOUNT = 4;
	public static final int NONE = 255;
	private TextView accountTextView = null;
	private Activity activity = null;
	private Context context = null;
	private Drawable dwAccount = null;
	private Drawable dwFood = null;
	private Drawable dwHome = null;
	private Drawable dwRecord = null;
	private Drawable dwSports = null;
	private TextView foodTextView = null;
	private TextView homeTextView = null;
	private TextView recordTextView = null;
	private TextView sportsTextView = null;
	private TextView favoriteTextView = null;
	private TextView chartTextView = null;
	private TextView settingsTextView = null;
	
	private int pageType;
	private View view = null;

	public BottomMenuHandler(Activity owner, int type)
	{
		context = owner.getApplicationContext();
		activity = owner;
		pageType = type;
	}

	public BottomMenuHandler(Activity owner, View paramView, int type)
	{
		context = owner.getApplicationContext();
		activity = owner;
		pageType = type;
		view = paramView;
	}

	public void BindEvent(int type)
	{
		pageType = type;

		BindEvent();
	}
	public void BindEvent()
	{
/*		
		View v = activity.findViewById(R.layout.bottom_menu);
		if( v == null )
			return;
		BindHomeEvent(v);
*/		
	}

	public void BindHomeEvent()
	{
/*		
		homeTextView  = (TextView)view.findViewById(R.id.home);
		recordTextView  = (TextView)view.findViewById(R.id.record);
		foodTextView  = (TextView)view.findViewById(R.id.food);
		sportsTextView  = (TextView)view.findViewById(R.id.sports);
		accountTextView  = (TextView)view.findViewById(R.id.account);

		foodTextView.setVisibility(View.GONE);
		sportsTextView.setVisibility(View.GONE);
		accountTextView.setVisibility(View.GONE);
		
		favoriteTextView = (TextView)view.findViewById(R.id.common_bottom_fav);
		chartTextView = (TextView)view.findViewById(R.id.common_bottom_char);
		settingsTextView = (TextView)view.findViewById(R.id.common_bottom_settings);
*/		
		
		BindSetEvent();
	}

	public void BindHomeEvent(View v)
	{
		view = v;
		BindHomeEvent();
	}

	public void BindSetEvent()
	{
		homeTextView.setClickable(true);
		recordTextView.setClickable(true);
//		foodTextView.setClickable(true);
//		sportsTextView.setClickable(true);
//		accountTextView.setClickable(true);
		favoriteTextView.setClickable(true);
		chartTextView.setClickable(true);
		settingsTextView.setClickable(true);		

		BottomColorTouch localBottomColorTouch1 = new BottomColorTouch();
		homeTextView.setOnTouchListener(localBottomColorTouch1);

		BottomColorTouch localBottomColorTouch2 = new BottomColorTouch();
		recordTextView.setOnTouchListener(localBottomColorTouch2);

		BottomColorTouch localBottomColorTouch3 = new BottomColorTouch();
		foodTextView.setOnTouchListener(localBottomColorTouch3);

		BottomColorTouch localBottomColorTouch4 = new BottomColorTouch();
		sportsTextView.setOnTouchListener(localBottomColorTouch4);

		BottomColorTouch localBottomColorTouch5 = new BottomColorTouch();
		accountTextView.setOnTouchListener(localBottomColorTouch5);

		BottomHomeClick localBottomHomeClick = new BottomHomeClick();
		homeTextView.setOnClickListener(localBottomHomeClick);

		BottomRecordClick localBottomRecordClick = new BottomRecordClick();
		recordTextView.setOnClickListener(localBottomRecordClick);

		BottomFoodClick localBottomFoodClick = new BottomFoodClick();
		foodTextView.setOnClickListener(localBottomFoodClick);

		BottomSportsClick localBottomSportsClick = new BottomSportsClick();
		sportsTextView.setOnClickListener(localBottomSportsClick);

		BottomAccountClick localBottomAccountClick = new BottomAccountClick();
		accountTextView.setOnClickListener(localBottomAccountClick);

		favoriteTextView.setOnTouchListener(this);
		chartTextView.setOnTouchListener(this);
		settingsTextView.setOnTouchListener(this);			

		favoriteTextView.setOnClickListener(this);
		chartTextView.setOnClickListener(this);
		settingsTextView.setOnClickListener(this);		
/*		
		Drawable background = activity.getResources().getDrawable(R.drawable.black);
		dwHome = background;
		dwRecord = background;
		dwFood = background;
		dwSports = background;
		dwAccount = background;
		switch (this.pageType)
		{
		default:
		case HOME:
			homeTextView.setBackgroundDrawable(background);
			break;
		case RECORD:
			recordTextView.setBackgroundDrawable(background);
			break;
		case FOOD:
			foodTextView.setBackgroundDrawable(background);
			break;
		case SPORTS:
			sportsTextView.setBackgroundDrawable(background);
			break;
		case ACCOUNT:
			accountTextView.setBackgroundDrawable(background);
			break;
		}
*/
		return;
	}

	public void open(Activity paramActivity)
	{
		Intent localIntent1 = new Intent();
		Class localClass = paramActivity.getClass();
		localIntent1.setClass(activity, localClass);
		this.activity.startActivity(localIntent1);
	}

	public class BottomAccountClick
	implements View.OnClickListener
	{
		public BottomAccountClick()
		{
		}

		public void onClick(View paramView)
		{
/*
			if (BottomMenuHandler.this.pageType != 4)
			{
				if (!ApplicationState.checkLogin(BottomMenuHandler.this.context)){
					BottomMenuHandler localBottomMenuHandler2 = BottomMenuHandler.this;
					LoginActivity localLoginActivity = new LoginActivity();
					localBottomMenuHandler2.open(localLoginActivity);        	
				}
				BottomMenuHandler localBottomMenuHandler1 = BottomMenuHandler.this;
				AccountActivity localAccountActivity = new AccountActivity();
				localBottomMenuHandler1.open(localAccountActivity);
			}
			while (true)
			{
				Drawable localDrawable = BottomMenuHandler.this.dwAccount;
				paramView.setBackgroundDrawable(localDrawable);
				BottomMenuHandler localBottomMenuHandler2 = BottomMenuHandler.this;
				LoginActivity localLoginActivity = new LoginActivity();
				localBottomMenuHandler2.open(localLoginActivity);
				return;
			}
*/			
		}
	}

	public class BottomColorTouch
	implements View.OnTouchListener
	{
		public BottomColorTouch()
		{
		}

		public boolean onTouch(View view, MotionEvent event)
		{
			Drawable bg=null;
			switch (event.getAction())
			{
			default:
			case MotionEvent.ACTION_DOWN:
//				bg = BottomMenuHandler.this.context.getResources().getDrawable(R.drawable.orange);
				break;
			case MotionEvent.ACTION_UP:
			}
			view.setBackgroundDrawable(bg);
			return false;
		}
	}

	public class BottomFoodClick
	implements View.OnClickListener
	{
		public BottomFoodClick()
		{
		}

		public void onClick(View paramView)
		{
/*
			if (BottomMenuHandler.this.pageType != 2)
			{
				BottomMenuHandler localBottomMenuHandler = BottomMenuHandler.this;
				FoodClassList localFoodClassList = new FoodClassList();
				localBottomMenuHandler.open(localFoodClassList);
			}
			Drawable localDrawable = BottomMenuHandler.this.dwFood;
			paramView.setBackgroundDrawable(localDrawable);
*/			
		}
	}

	public class BottomHomeClick
	implements View.OnClickListener
	{
		public BottomHomeClick()
		{
		}

		public void onClick(View paramView)
		{
/*			
			if (!ApplicationState.clickok)
			{
				ApplicationState.clickok = true;
				return;
			}
			if (BottomMenuHandler.this.pageType != 0)
			{
				BottomMenuHandler localBottomMenuHandler = BottomMenuHandler.this;
				FitMissMain localFitMissMain = new FitMissMain();
				localBottomMenuHandler.open(localFitMissMain);
			}
			Drawable localDrawable = BottomMenuHandler.this.dwHome;
			paramView.setBackgroundDrawable(localDrawable);
*/			
		}
	}

	public class BottomRecordClick
	implements View.OnClickListener
	{
		public BottomRecordClick()
		{
		}

		public void onClick(View paramView)
		{
/*			
			if (!ApplicationState.clickok)
			{
				ApplicationState.clickok = true;
				return;
			}
			BottomMenuHandler localBottomMenuHandler = BottomMenuHandler.this;
			FoodRecordList localFoodRecordList = new FoodRecordList();
			localBottomMenuHandler.open(localFoodRecordList);
			Drawable localDrawable = BottomMenuHandler.this.dwRecord;
			paramView.setBackgroundDrawable(localDrawable);
*/			
		}
	}

	public class BottomSportsClick
	implements View.OnClickListener
	{
		public BottomSportsClick()
		{
		}

		public void onClick(View paramView)
		{
/*			
			if (!ApplicationState.clickok)
			{
				ApplicationState.clickok = true;
				return;
			}
			if (BottomMenuHandler.this.pageType != 3)
			{
				BottomMenuHandler localBottomMenuHandler = BottomMenuHandler.this;
				SportsClassList localSportsClassList = new SportsClassList();
				localBottomMenuHandler.open(localSportsClassList);
			}
			Drawable localDrawable = BottomMenuHandler.this.dwSports;
			paramView.setBackgroundDrawable(localDrawable);
*/			
		}
	}


	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
/*
		Drawable bg=null;
		switch (event.getAction())
		{
		default:
		case MotionEvent.ACTION_DOWN:
			bg = BottomMenuHandler.this.context.getResources().getDrawable(R.drawable.orange);
			break;
		case MotionEvent.ACTION_UP:
		}
		view.setBackgroundDrawable(bg);
*/		
		return false;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=null;
/*
		if( v == favoriteTextView ){
			intent = new Intent(activity, FoodRecordContent.class);
		}
		else if( v == chartTextView ){
			intent = new Intent(activity, AnaActivity.class);
		}
		else if( v == settingsTextView ){
			intent = new Intent(activity, SettingActivity.class);
		}
*/		
		if( intent != null )
			activity.startActivity(intent);
	}
}

/* Location:           D:\bangso\dex2jar-0.0.7.11-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     COM.Bangso.Handler.BottomMenuHandler
 * JD-Core Version:    0.6.0
 */