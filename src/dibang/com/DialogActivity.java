/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dibang.com;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;



/**
 * <h3>Dialog Activity</h3>
 * 
 * <p>This demonstrates the how to write an activity that looks like 
 * a pop-up dialog.</p>
 */
public class DialogActivity extends Activity implements OnClickListener {
	protected final static String KEY_CLICKED_BTN = "btn";
    /**
     * Initialization of the Activity after it is first created.  Must at least
     * call {@link android.app.Activity#setContentView setContentView()} to
     * describe what is to be displayed in the screen.
     */
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        // Be sure to call the super class.
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.quit_confirm_dlg_body);
        Button btn = (Button)findViewById(R.id.button_ok);
        btn.setOnClickListener(this);
        btn = (Button)findViewById(R.id.button_cancel);
        btn.setOnClickListener(this);
        
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		if( v.getId() == R.id.button_ok){
			intent = new Intent();
			intent.putExtra(KEY_CLICKED_BTN, R.id.button_ok);
			setResult(1, intent);
			finish();
		}
		else if( v.getId() == R.id.button_cancel){
			intent = new Intent();
			intent.putExtra(KEY_CLICKED_BTN, R.id.button_cancel);
			setResult(1, intent);
			finish();			
		}
	}
}
