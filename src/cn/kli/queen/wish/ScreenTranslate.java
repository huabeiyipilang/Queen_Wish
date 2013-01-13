package cn.kli.queen.wish;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public class ScreenTranslate {
	private static ScreenTranslate sTranslate;
	private Context mContext;
	private ViewGroup mContainer;
	private ViewGroup currentScreen;
	
	private WishListScreen mWishListScreen;
	private WishComposeScreen mWishComposeScreen;
	
    private Interpolator accelerator = new AccelerateInterpolator();
    private Interpolator decelerator = new DecelerateInterpolator();

	private ScreenTranslate(Context context, ViewGroup container){
		mContext = context;
		mContainer = container;
		mWishListScreen = new WishListScreen(context);
		mWishComposeScreen = new WishComposeScreen(context);
		mContainer.addView(mWishListScreen);
		mContainer.addView(mWishComposeScreen);
	}
	
	public static ScreenTranslate create(Context context, ViewGroup container){
		sTranslate = new ScreenTranslate(context, container);
		return sTranslate;
	}
	
	public static ScreenTranslate getInstance(){
		return sTranslate;
	}
	
	public boolean backToHome(){
		if(currentScreen != mWishListScreen){
			transToWishList();
			return true;
		}else{
			return false;
		}
	}
	
	public void transToWishList(){
		if(currentScreen == null){
			mWishListScreen.setVisibility(View.VISIBLE);
			currentScreen = mWishListScreen;
		}else{
			translate(mWishListScreen, null);
		}
	}
	
	public void transToWishCompose(Message msg){
		translate(mWishComposeScreen, msg);
	}
	
	public void translate(BaseScreen to, Message msg){
		to.start(msg);
		display(to);
//		flipit(to);
	}
	
	private void display(ViewGroup to){
		display(currentScreen, to);
	}
	
	private void display(ViewGroup from, ViewGroup to){
		from.setVisibility(View.GONE);
		to.setVisibility(View.VISIBLE);
	}
	/*
	private void flipit(ViewGroup to){
		flipit(currentScreen, to);
	}
	
    private void flipit(final ViewGroup from, final ViewGroup to) {
        ObjectAnimator visToInvis = ObjectAnimator.ofFloat(from, "rotationY", 0f, 90f);
        visToInvis.setDuration(500);
        visToInvis.setInterpolator(accelerator);
        
        final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(to, "rotationY",
                -90f, 0f);
        invisToVis.setDuration(500);
        invisToVis.setInterpolator(decelerator);
        
        visToInvis.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator anim) {
                from.setVisibility(View.GONE);
                invisToVis.start();
                to.setVisibility(View.VISIBLE);
                currentScreen = to;
            }
        });
        
        
        visToInvis.start();
    }
    */
}
