package com.zhqydot.framework.easyrouter.core.safr;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;

import io.reactivex.Observable;

public class RxActivityResult {
    private static final String TAG = "RxActivityResult";
    private RxActivityResultFragment mFragment;

    public RxActivityResult(Activity activity) {
        mFragment = getRxActivityResultFragment(activity);
    }

    public RxActivityResult(Fragment fragment) {
        this(fragment.getActivity());
    }

    private RxActivityResultFragment getRxActivityResultFragment(Activity activity) {
        RxActivityResultFragment rxActivityResultFragment = findRxActivityResultFragment(activity);
        if (rxActivityResultFragment == null) {
            rxActivityResultFragment = new RxActivityResultFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(rxActivityResultFragment, TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return rxActivityResultFragment;
    }

    private RxActivityResultFragment findRxActivityResultFragment(Activity activity) {
        return (RxActivityResultFragment) activity.getFragmentManager().findFragmentByTag(TAG);
    }

    public Observable<ActivityResultInfo> startForResult(Intent intent, int requestCode) {
        return mFragment.startForResult(intent, requestCode);
    }

    public Observable<ActivityResultInfo> startForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(mFragment.getActivity(), clazz);
        return startForResult(intent, requestCode);
    }

}
