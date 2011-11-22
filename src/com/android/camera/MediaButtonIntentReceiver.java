/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.android.camera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.util.Log;

/**
 * 
 */
public class MediaButtonIntentReceiver extends BroadcastReceiver {
	private static final int MSG_LONGPRESS_TIMEOUT = 1;
    private static final int LONG_PRESS_DELAY = 1000;

    private static long mLastClickTime = 0;
    private static boolean mDown = false;
    private static boolean mLaunched = false;
	
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
		Log.d("----------------","intentAction:"+intentAction);
        if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            KeyEvent event = (KeyEvent)
                    intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            
            if (event == null) {
                return;
            }

            int keycode = event.getKeyCode();
            int action = event.getAction();
            long eventtime = event.getEventTime();
			Log.d("----------------","keycode:"+keycode+",action:"+action);
            // single quick press: pause/resume. 
            // double press: next track
            // long press: start auto-shuffle mode.
            
            String command = null;
            switch (keycode) {
                case KeyEvent.KEYCODE_MEDIA_STOP:
                    command = MovieViewControl.CMDSTOP;
                    break;
                case KeyEvent.KEYCODE_HEADSETHOOK:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                    command = MovieViewControl.CMDTOGGLEPAUSE;
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    command = MovieViewControl.CMDNEXT;
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    command = MovieViewControl.CMDPREVIOUS;
                    break;
/*
				case KeyEvent.KEYCODE_MEDIA_REWIND:
	                    	command = MovieViewControl.CMDRW;
	                    break;
			      case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
	                    	command = MovieViewControl.CMDFF;
	                    break;
*/
            }

            if (command != null) {
                if (action == KeyEvent.ACTION_DOWN) {
                   
                        // if this isn't a repeat event

                        // The service may or may not be running, but we need to send it
                        // a command.
                        Intent i = new Intent();
                        i.setAction(MovieViewControl.SERVICECMD);
                        if (keycode == KeyEvent.KEYCODE_HEADSETHOOK &&
                                eventtime - mLastClickTime < 300) {
                            i.putExtra(MovieViewControl.CMDNAME, MovieViewControl.CMDNEXT);
                            context.sendBroadcast(i,null);
                            mLastClickTime = 0;
                        } else {
                            i.putExtra(MovieViewControl.CMDNAME, command);
                            context.sendBroadcast(i,null);
                            mLastClickTime = eventtime;
                        }

                } else {
                    //mHandler.removeMessages(MSG_LONGPRESS_TIMEOUT);
                    //mDown = false;
                }
                if (isOrderedBroadcast()) {
                    abortBroadcast();
                }
            }
        }
    }
}
