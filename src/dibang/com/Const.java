package dibang.com;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dibang.com.handle.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class Const extends BaseActivity {
	public static final int UI_TYPE_WEBSITE_DESIGN = 1;
	public static final int UI_TYPE_3D_ANIMATION = 2;
	public static final int UI_TYPE_EFFECT_SHOW = 3;
	public static final int UI_TYPE_HOUSE_SHOW = 4;
	public static final int UI_TYPE_EMAGZIN = 5;
	public static final int UI_TYPE_PARTNER = 6;
}