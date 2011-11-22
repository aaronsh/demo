/*
 * Copyright (C) 2009 The Android Open Source Project
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

import dibang.com.R;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Video;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;
import android.util.Log;

import android.media.AudioManager;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.app.Service;


public class MovieViewControl implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    @SuppressWarnings("unused")
    private static final String TAG = "MovieViewControl";

    private static final int ONE_MINUTE = 60 * 1000;
    private static final int TWO_MINUTES = 2 * ONE_MINUTE;
    private static final int FIVE_MINUTES = 5 * ONE_MINUTE;

    // Copied from MediaPlaybackService in the Music Player app. Should be
    // public, but isn't.
    //private static final String SERVICECMD =
    //        "com.android.music.musicservicecommand";
    //private static final String CMDNAME = "command";
    //private static final String CMDPAUSE = "pause";

	private AudioManager mAudioManager;
	private ComponentName mEvtrecv;
	public static final String SERVICECMD = "com.android.music.videoservicecommand";
    public static final String CMDNAME = "command";
    public static final String CMDTOGGLEPAUSE = "togglepause";
    public static final String CMDSTOP = "stop";
    public static final String CMDPAUSE = "pause";
    public static final String CMDPREVIOUS = "previous";
    public static final String CMDNEXT = "next";
	public static final String CMDRW = "rewind";
    public static final String CMDFF = "fastforward";

    private final VideoView mVideoView;
    private final View mProgressView;
    private final Uri mUri;
    private final ContentResolver mContentResolver;

    // State maintained for proper onPause/OnResume behaviour.
    private int mPositionWhenPaused = -1;
    private boolean mWasPlayingWhenPaused = false;
    private MediaController mMediaController;
    private int mWidthOnPaused = -1;
    private Context mContext;

    Handler mHandler = new Handler();

    Runnable mPlayingChecker = new Runnable() {
        public void run() {
            if (mVideoView.isPlaying()) {
                mProgressView.setVisibility(View.GONE);
            } else {
                mHandler.postDelayed(mPlayingChecker, 250);
            }
        }
    };

    Runnable mDelayedShowing = new Runnable() {
        public void run() {
            View anchorView = mVideoView.getParent() instanceof View ? (View)mVideoView.getParent() : mVideoView;
            Log.v(TAG, "Waiting..."
                     + " w=" + anchorView.getWidth()
                     + " h=" + anchorView.getHeight() + " pw=" + mWidthOnPaused);

            if (mWidthOnPaused != anchorView.getWidth()) {
                mHandler.postDelayed(mDelayedShowing, 250);
            } else {
                Log.v(TAG, "Show controller with right demensions");
                mMediaController = new MediaController(mContext);
                mVideoView.setMediaController(mMediaController);
                mMediaController.show(0);
           }
        }
    };
	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String cmd = intent.getStringExtra(CMDNAME);
            Log.d(TAG,"Video mIntentReceiver.onReceive " + action + " / " + cmd);
			if(!action.equals(SERVICECMD)){
				return;
			}
            if (CMDNEXT.equals(cmd)) {
                //next(true);
				Log.d(TAG,"mIntentReceiver.onReceive video next ");
            } else if (CMDPREVIOUS.equals(cmd)) {
                //prev();
                Log.d(TAG,"mIntentReceiver.onReceive video prev ");
            } else if (CMDTOGGLEPAUSE.equals(cmd)) {
                if (mVideoView.isPlaying()) {
                    mVideoView.suspend();
                } else {
                    mVideoView.resume();
                }
            } else if (CMDPAUSE.equals(cmd)) {
                mVideoView.pause();
            } else if (CMDSTOP.equals(cmd)) {
                mVideoView.pause();
                mVideoView.seekTo(0);

            }

        }
    };

    public MovieViewControl(View rootView, Context context, Uri videoUri) {
        mContentResolver = context.getContentResolver();
        mVideoView = (VideoView) rootView.findViewById(R.id.surface_view);
        mProgressView = rootView.findViewById(R.id.progress_indicator);

        mUri = videoUri;
        mContext = context;

        // For streams that we expect to be slow to start up, show a
        // progress spinner until playback starts.
        String scheme = mUri.getScheme();
        if ("http".equalsIgnoreCase(scheme)
                || "rtsp".equalsIgnoreCase(scheme)) {
            mHandler.postDelayed(mPlayingChecker, 250);
        } else {
            mProgressView.setVisibility(View.GONE);
        }

        mVideoView.setOnErrorListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setVideoURI(mUri);
        mMediaController = new MediaController(context);
        mVideoView.setMediaController(mMediaController);

        // make the video view handle keys for seeking and pausing
        mVideoView.requestFocus();

        //Intent i = new Intent(SERVICECMD);
        //i.putExtra(CMDNAME, CMDPAUSE);
        //context.sendBroadcast(i);
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
		mEvtrecv=new ComponentName(mContext.getPackageName(),MediaButtonIntentReceiver.class.getName());
        final Integer bookmark = getBookmark();
        if (bookmark != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.resume_playing_title);
            builder.setMessage(String.format(
                    context.getString(R.string.resume_playing_message),
                    MenuHelper.formatDuration(context, bookmark)));
            builder.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    onCompletion();
                }});
            builder.setPositiveButton(R.string.resume_playing_resume,
                    new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mVideoView.seekTo(bookmark);
                    mVideoView.start();
                }});
            builder.setNegativeButton(R.string.resume_playing_restart,
                    new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mVideoView.start();
                }});
            builder.show();
        } else {
            mVideoView.start();
        }
    }

    private static boolean uriSupportsBookmarks(Uri uri) {
        String scheme = uri.getScheme();
        String authority = uri.getAuthority();
        return ("content".equalsIgnoreCase(scheme)
                && MediaStore.AUTHORITY.equalsIgnoreCase(authority));
    }

    private Integer getBookmark() {
        if (!uriSupportsBookmarks(mUri)) {
            return null;
        }

        String[] projection = new String[] {
                Video.VideoColumns.DURATION,
                Video.VideoColumns.BOOKMARK};

        try {
            Cursor cursor = mContentResolver.query(
                    mUri, projection, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        int duration = getCursorInteger(cursor, 0);
                        int bookmark = getCursorInteger(cursor, 1);
                        if ((bookmark < TWO_MINUTES)
                                || (duration < FIVE_MINUTES)
                                || (bookmark > (duration - ONE_MINUTE))) {
                            return null;
                        }
                        return Integer.valueOf(bookmark);
                    }
                } finally {
                    cursor.close();
                }
            }
        } catch (SQLiteException e) {
            // ignore
        }

        return null;
    }

    private static int getCursorInteger(Cursor cursor, int index) {
        try {
            return cursor.getInt(index);
        } catch (SQLiteException e) {
            return 0;
        } catch (NumberFormatException e) {
            return 0;
        }

    }

    private void setBookmark(int bookmark) {
        if (!uriSupportsBookmarks(mUri)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(Video.VideoColumns.BOOKMARK, Integer.toString(bookmark));
        try {
            mContentResolver.update(mUri, values, null, null);
        } catch (SecurityException ex) {
            // Ignore, can happen if we try to set the bookmark on a read-only
            // resource such as a video attached to GMail.
        } catch (SQLiteException e) {
            // ignore. can happen if the content doesn't support a bookmark
            // column.
        } catch (UnsupportedOperationException e) {
            // ignore. can happen if the external volume is already detached.
        }
    }

    public void onPause() {
        View anchorView = mVideoView.getParent() instanceof View ? (View)mVideoView.getParent() : mVideoView;
        mWidthOnPaused = anchorView.getWidth();

        mHandler.removeCallbacksAndMessages(null);
        setBookmark(mVideoView.getCurrentPosition());

        mPositionWhenPaused = mVideoView.getCurrentPosition();
        mWasPlayingWhenPaused = mVideoView.isPlaying();
        mVideoView.stopPlayback();
		mContext.unregisterReceiver(mIntentReceiver);
		mAudioManager.unregisterMediaButtonEventReceiver(mEvtrecv);
    }

    public void onResume() {
        if (mPositionWhenPaused >= 0) {
            mVideoView.setVideoURI(mUri);
            mVideoView.setMediaController(null);
            mVideoView.seekTo(mPositionWhenPaused);
            mPositionWhenPaused = -1;
            mHandler.postDelayed(mDelayedShowing, 250);
        }
		IntentFilter commandFilter = new IntentFilter();
        commandFilter.addAction(SERVICECMD);
        mContext.registerReceiver(mIntentReceiver, commandFilter);
		mAudioManager.registerMediaButtonEventReceiver(mEvtrecv);
    }

    public boolean onError(MediaPlayer player, int arg1, int arg2) {
        mHandler.removeCallbacksAndMessages(null);
        mProgressView.setVisibility(View.GONE);
        return false;
    }

    public void onCompletion(MediaPlayer mp) {
        onCompletion();
    }

    public void onCompletion() {
    }
}
