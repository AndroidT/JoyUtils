package com.joysoft.andutils.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by fengmiao on 2015/9/21.
 */
public class FragmentHelper {

    public static void addFragmentToStack(FragmentActivity context, int containerId, Fragment newFragment){
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
        String tag = newFragment.getClass().getSimpleName();
        ft.replace(containerId, newFragment,tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(tag);
        ft.commit();
    }

    public static void addFragmentToSTackAllowLoss(FragmentActivity context, int containerId, Fragment newFragment){
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
        String tag = newFragment.getClass().getSimpleName();
        ft.replace(containerId, newFragment,tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
    }

    public static void popBackStack(FragmentActivity mActivity){
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    public static void addFragmentToStack(Fragment context, int containerId, Fragment newFragment){
        FragmentManager fragmentManager = context.getFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
        String tag = newFragment.getClass().getSimpleName();
        ft.replace(containerId, newFragment,tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(tag);
        ft.commit();
    }

    public static void replaceFragment(FragmentActivity context, int containerId, Fragment newFragment){
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.setCustomAnimations(R.anim.ac_transition_fade_in, R.anim.ac_transition_fade_out);
        ft.replace(containerId, newFragment,newFragment.getClass().getSimpleName());
//		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    public static void replaceChildFrament(Fragment context, int containerId, Fragment newFragment){
        FragmentManager fragmentManager = context.getChildFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(containerId, newFragment,newFragment.getClass().getSimpleName()).commit();
    }

    public static void addChildFrament(Fragment context, int containerId, Fragment newFragment){
        addChildFragmentByTag(context, containerId, newFragment, newFragment.getClass().getSimpleName());
    }

    public static void addChildFragmentByTag(Fragment context, int containerId, Fragment newFragment, String tag){
        FragmentManager fragmentManager = context.getChildFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(containerId, newFragment,tag).commit();
    }

    public static void addFrament(FragmentActivity context, int containerId, Fragment newFragment){
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(containerId, newFragment,newFragment.getClass().getSimpleName()).commit();
    }

    public static void showFragment(FragmentActivity context, Fragment fragment){
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.show(fragment).commit();
    }

    public static void showChildFragment(Fragment context, Fragment fragment){
        FragmentManager fragmentManager = context.getChildFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.setCustomAnimations(R.anim.ac_transition_fade_in, R.anim.ac_transition_fade_out);
        ft.show(fragment).commit();
    }


    public static void hideFragment(FragmentActivity context, Fragment fragment){
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.hide(fragment).commit();
    }

    public static void hideChildFragment(Fragment context, Fragment fragment){
        FragmentManager fragmentManager = context.getChildFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
//        ft.setCustomAnimations(R.anim.ac_transition_fade_in, R.anim.ac_transition_fade_out);
        ft.hide(fragment).commit();
    }

    public static void removeFragment(FragmentActivity context, Fragment fragment){
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragment).commit();
    }




    public static Fragment findFragment(FragmentActivity context, Class<?> fragment){
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        return fragmentManager.findFragmentByTag(fragment.getSimpleName());
    }

    public static Fragment findFragment(Fragment context, Class<?> fragment){
        FragmentManager fragmentManager = context.getFragmentManager();
        return fragmentManager.findFragmentByTag(fragment.getSimpleName());
    }

    public static Fragment findChildFragment(Fragment context, Class<?> fragment){
        return findChildFragmentByTag(context, fragment, fragment.getSimpleName());
    }

    public static Fragment findChildFragmentByTag(Fragment context, Class<?> fragment, String tag){
        FragmentManager fragmentManager = context.getChildFragmentManager();
        return fragmentManager.findFragmentByTag(tag);
    }

}

