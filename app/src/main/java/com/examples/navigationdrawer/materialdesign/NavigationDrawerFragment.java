package com.examples.navigationdrawer.materialdesign;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

public class NavigationDrawerFragment extends Fragment
{
    public static final String PREFERENCE_FILE_NAME = "TEST_PREFERENCES";
    public static final String NAVIGATION_DRAWER_STATE_KEY = "isNavigationDrawerOpen";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private boolean mNavigationDrawerState;
    private boolean mNavigationDrawerStateFromSavedInstanceState;
    private View mNavigationDrawerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mNavigationDrawerState = Boolean.valueOf(readFromPreferences(getActivity(), NAVIGATION_DRAWER_STATE_KEY, "false"));

        if( savedInstanceState != null )
        {
            mNavigationDrawerStateFromSavedInstanceState = true;
        }
    }

    public void setUp(int mNavigationDrawerID, DrawerLayout drawerLayout, final Toolbar toolbar)
    {
        mNavigationDrawerView = getActivity().findViewById(mNavigationDrawerID);

        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);

                if( !mNavigationDrawerState )
                {
                    mNavigationDrawerState = true;

                    saveToPreferences(getActivity(), NAVIGATION_DRAWER_STATE_KEY, mNavigationDrawerState+"");
                }

                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);

                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset)
            {
                super.onDrawerSlide(drawerView, slideOffset);
                if( slideOffset <0.6 )
                {
                    if(Build.VERSION.SDK_INT > 11)
                    {
                        toolbar.setAlpha((1 - slideOffset));
                    }
                    else
                    {
                        setAlphaForView(toolbar,(1 - slideOffset));
                    }
                }
            }

        };

        if( !mNavigationDrawerState && !mNavigationDrawerStateFromSavedInstanceState )
        {
            mDrawerLayout.openDrawer( mNavigationDrawerView );
        }

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(preferenceName,preferenceValue);

        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String dafaultValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(preferenceName,dafaultValue);
    }

    private void setAlphaForView(View v, float alpha)
    {
        AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
        animation.setDuration(0);
        animation.setFillAfter(true);
        v.startAnimation(animation);
    }
}
