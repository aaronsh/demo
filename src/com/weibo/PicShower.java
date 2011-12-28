package com.weibo;

import com.weibo.zoom.ImageZoomView;
import com.weibo.zoom.SimpleZoomListener;
import com.weibo.zoom.ZoomState;

import dibang.com.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ZoomControls;

public class PicShower extends Activity{

    /** Constant used as menu item id for setting zoom control type */
    private static final int MENU_ID_ZOOM = 0;

    /** Constant used as menu item id for setting pan control type */
    private static final int MENU_ID_PAN = 1;

    /** Constant used as menu item id for resetting zoom state */
    private static final int MENU_ID_RESET = 2;

    /** Image zoom view */
    private ImageZoomView mZoomView;

    /** Zoom state */
    private ZoomState mZoomState;

    /** Decoded bitmap image */
    private Bitmap mBitmap;

    /** On touch listener for zoom view */
    private SimpleZoomListener mZoomListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pic_shower);

        mZoomState = new ZoomState();

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.splash_background);

        mZoomListener = new SimpleZoomListener();
        mZoomListener.setZoomState(mZoomState);

        mZoomView = (ImageZoomView)findViewById(R.id.zoomview);
        mZoomView.setZoomState(mZoomState);
        mZoomView.setImage(mBitmap);
        mZoomView.setOnTouchListener(mZoomListener);

        resetZoomState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBitmap.recycle();
        mZoomView.setOnTouchListener(null);
        mZoomState.deleteObservers();
    }

    /**
     * Reset zoom state and notify observers
     */
    private void resetZoomState() {
        mZoomState.setPanX(0.5f);
        mZoomState.setPanY(0.5f);
        mZoomState.setZoom(1f);
        mZoomState.notifyObservers();
    }


}
