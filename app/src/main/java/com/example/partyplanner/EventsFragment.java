package com.example.partyplanner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

public class EventsFragment extends Fragment {

    FrameLayout simpleLayout;
    TabLayout eventTabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_events, container, false);
        simpleLayout = viewGroup.findViewById(R.id.simpleLayout);
        eventTabLayout = (TabLayout) viewGroup.findViewById(R.id.eventTabLayout);
        TabLayout.Tab tab = eventTabLayout.getTabAt(0);
        tab.select();
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.simpleLayout, new SentFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
//        TabLayout.Tab tab = tabLayout.getTabAt(0);
//        tab.select();
//        TabLayout.Tab firstTab = tabLayout.newTab();
//        firstTab.setText("First"); // set the Text for the first Tab
//        //firstTab.setIcon(R.drawable.ic_launcher); // set an icon for the
//        // first tab
//        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout
//        // Create a new Tab named "Second"
//        TabLayout.Tab secondTab = tabLayout.newTab();
//        secondTab.setText("Second"); // set the Text for the second Tab
//        //secondTab.setIcon(R.drawable.ic_launcher); // set an icon for the second tab
//        tabLayout.addTab(secondTab); // add  the tab  in the TabLayout

        eventTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("49", "asdf");
                selectTab(tab);
            }

            private void selectTab(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new SentFragment();
                        break;
                    case 1:
                        fragment = new RecFragment();
                        break;
                }
                //ActivityManager am = getParentFragmentManager();
                FragmentManager fm = getParentFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.simpleLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("73", "asdf");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d("77", "asdf");
                if (tab.getPosition() == 0) {
                    selectTab(tab);

//                    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//
//                        @Override
//                        public void onTabSelected(TabLayout.Tab tab) {
//                            selectTab(tab);
//                        }
//                        @Override
//                        public void onTabReselected(TabLayout.Tab arg0) {
//                        }
//
//                        @Override
//                        public void onTabUnselected(TabLayout.Tab arg0) {
//                        }
//                    });

                }
            }
        });
        return viewGroup;

    }

}




