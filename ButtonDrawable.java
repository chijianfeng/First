/**
 * 
 */
package com.baidu.lightgame.plugin.view.floatwindow;


import com.balysv.materialmenu.MaterialMenuDrawable.IconState;
import com.balysv.materialmenu.MaterialMenuDrawable.MaterialMenuState;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.animation.DecelerateInterpolator;

/**
 * @author chijianfeng
 *
 */
public class ButtonDrawable extends Drawable implements Animatable{
	
	/**define different icon state to show
	 * */
	public enum IconState {
       CROSS		/* + */,
       LEFTARROW	/* > */,
       RIGHTARROW	/* < */,
       UPARROW		/* ^ */,
       DOWNARROW	/* V */,
    }
	
	public enum AnimationState{
		CROSS_ANI,LEFT_ANI,RIGHT_ANI,UP_ANI,DOWN_ANI;
		
		public IconState getState(){
			switch(this){
				case CROSS_ANI:
					return IconState.CROSS;
				case LEFT_ANI:
					return IconState.LEFTARROW;
				case RIGHT_ANI:
					return IconState.RIGHTARROW;
				case UP_ANI:
					return IconState.UPARROW;
				case DOWN_ANI:
					return IconState.DOWNARROW;
				default:
					return null;
			}
		}
	}
	
	/**
	 * define stroke line width
	 * */
	public enum Stroke {
        /**
         * 3 dip
         */
        REGULAR(3),
        /**
         * 2 dip
         */
        THIN(2),
        /**
         * 1 dip
         */
        EXTRA_THIN(1);

        private final int strokeWidth;

        Stroke(int strokeWidth) {
            this.strokeWidth = strokeWidth;
        }

        protected static Stroke valueOf(int strokeWidth) {
            switch (strokeWidth) {
                case 3:
                    return REGULAR;
                case 2:
                    return THIN;
                case 1:
                    return EXTRA_THIN;
                default:
                    return THIN;
            }
        }
    }
	
	public static final int     DEFAULT_COLOR              = Color.WHITE;
    public static final int     DEFAULT_SCALE              = 1;
    public static final int     DEFAULT_TRANSFORM_DURATION = 800;
    public static final boolean DEFAULT_VISIBLE            = true;
    
    private static final float CROSS_FIRST_ANGLE = 90;
    private static final float CROSS_SECOND_ANGLE = 180;
    private static final float LEFT_FIRST_ANGLE = 135;
    private static final float LEFT_SECOND_ANGLE = -135;
    private static final float RIGHT_FIRST_ANGLE = 45;
    private static final float RIGHT_SECOND_ANGLE = -45;
    private static final float UP_FIRST_ANGLE = 225;
    private static final float UP_SECOND_ANGLE = -45;
    private static final float DOWN_FIRST_ANGLE = 135;
    private static final float DOWN_SECOND_ANGLR = 45;
	
    private static final float TRANSFORMATION_START = 0;
    private static final float TRANSFORMATION_MID   = 1.0f;
    private static final float TRANSFORMATION_END   = 2.0f;
    
    private static final int DEFAULT_CIRCLE_ALPHA = 200;
    
    private final Stroke stroke;

    private final Object lock = new Object();

    private final Paint iconPaint   = new Paint();
    private final Paint circlePaint = new Paint();
    private int iconColor = DEFAULT_COLOR;
    private int circleColor = Color.GRAY;
    private int sideColor = Color.BLUE;
    
    private float   transformationValue   = 0f;
    private boolean transformationRunning = false;
    
    private IconState      currentIconState = IconState.LEFTARROW;
    
    private IconState animatingIconState;
    private boolean   visible;
    private boolean   rtlEnabled;
    
    private final int   width;
    private final int   height;
    private final float strokeWidth;
    
    /*out side circle width*/
    private final float sidePadding;
    
    private ObjectAnimator   transformation;
    private AnimatorListener animatorListener;
    private ButtonConstantState buttonconstantState;
    
    public ButtonDrawable(Context context, int color, Stroke stroke) {
    	 this(context, color, stroke, DEFAULT_SCALE, DEFAULT_TRANSFORM_DURATION);
	}
    public ButtonDrawable(Context context, int color, Stroke stroke, int transformDuration) {
    	 this(context, color, stroke, DEFAULT_SCALE, transformDuration);
	}
    public ButtonDrawable(Context context, int color, Stroke stroke, int scale, int transformDuration) {
    	Resources resources = context.getResources();
    	
    	
    	this.stroke = stroke;
        this.visible = DEFAULT_VISIBLE;
        this.width = (int) (dpToPx(resources, FloatApplication.VButtonWidth) * scale);
        this.height = (int) (dpToPx(resources, FloatApplication.VButtonHeight) * scale);
        this.strokeWidth = dpToPx(resources, stroke.strokeWidth) * scale;
        this.sidePadding = (width * 0.85f) / 2;
        
        initPaint(color);
        initAnimations(transformDuration);

        buttonconstantState = new ButtonConstantState();
	}
    
    public ButtonDrawable(int color, Stroke stroke, long transformDuration,int width, int height,float strokeWidth){
    	this.stroke = stroke;
        this.visible = DEFAULT_VISIBLE;
        this.width = width;
        this.height = height;
        this.strokeWidth = strokeWidth;
        this.sidePadding = (width * 0.85f) / 2;
        
        initPaint(color);
        initAnimations((int)transformDuration);

        buttonconstantState = new ButtonConstantState();
    }
    
    public void setCircleColor(int color){
    	this.circleColor = color;
    	circlePaint.setColor(circleColor);
    }
    
    public void seticonColor(int color){
    	this.iconColor = color;
    	iconPaint.setColor(color);
    }
    
    private void initPaint(int color) {
        iconPaint.setAntiAlias(true);
        iconPaint.setStyle(Style.STROKE);
        iconPaint.setStrokeWidth(strokeWidth);
        iconPaint.setColor(color);

        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.FILL);
        circlePaint.setColor(circleColor);
        circlePaint.setAlpha(DEFAULT_CIRCLE_ALPHA);

        setBounds(0, 0, width, height);
    }
    
    private void initAnimations(int transformDuration) {
        transformation = ObjectAnimator.ofFloat(this, transformationProperty, 0);
        transformation.setInterpolator(new DecelerateInterpolator(3));
        transformation.setDuration(transformDuration);
        transformation.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator animation) {
                transformationRunning = false;
                setIconState(animatingIconState);
            }
        });
    }
    
	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		
	}
    
    
    
    
    
    
    
    
    
    private final class ButtonConstantState extends ConstantState{
    	private int changingConfigurations;
		@Override
		public int getChangingConfigurations() {
			return changingConfigurations;
		}

		@SuppressLint("NewApi")
		@Override
		public Drawable newDrawable() {
			ButtonDrawable drawable = new ButtonDrawable(circlePaint.getColor(), stroke, transformation.getDuration(),
	                width, height,strokeWidth);
			drawable.setIconState(animatingIconState != null ? animatingIconState : currentIconState);
            drawable.setVisible(visible);
            drawable.setRTLEnabled(rtlEnabled);
            return drawable;
		}
    }
    
	static float dpToPx(Resources resources, float dp) {
	    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
	}
}
