package com.shaweibo.biu.ui.common;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {

    protected boolean isFirstCreate=true;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isFirstCreate=false;
    }


}
