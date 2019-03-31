package shake.android.mahmoud.learnshake;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsFragmentPagerAdapter extends FragmentPagerAdapter {

    public TabsFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Fragment getItem(int index) {
        // TODO Auto-generated method stub
        if(index == 0)
            return new HorzShake();
        if(index == 1)
            return new VerShake();
        if(index == 2)
            return new ForwShake();


        return null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }

}