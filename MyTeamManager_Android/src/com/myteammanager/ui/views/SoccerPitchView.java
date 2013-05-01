package com.myteammanager.ui.views;

import java.util.ArrayList;
import java.util.List;

import com.myteammanager.beans.PlayerBean;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import com.myteammanager.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class SoccerPitchView extends ImageView {
	


	public SoccerPitchView(Context context) {
		super(context);
	}

	public SoccerPitchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SoccerPitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		
	}

}
