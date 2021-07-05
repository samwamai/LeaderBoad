    package com.gads.leaderboad;

    import android.app.Fragment;
    import android.app.FragmentManager;
    import android.util.Log;

    import androidx.annotation.NonNull;


    public class PagerAdapter extends FragmentStatePagerAdapter {
        private  String TAG="pager adapter";
       int mNumOfTabs;
       public PagerAdapter(FragmentManager fm, int mNumOfTabs){
           super(fm);
           this.mNumOfTabs = mNumOfTabs;
       }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            Log.e(TAG, "getItem: "+position );
           switch (position){
               case 1: return new LeadersSkillIQFragment();
               case 0: return new LeadersHoursFragment();
           }
            return null;
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
