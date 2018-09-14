package com.zhqydot.framework.easyrouter.core.safr;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxActivityResultFragment extends Fragment {
    private Map<Integer, PublishSubject<ActivityResultInfo>> mSubjects = new HashMap<>();

    public RxActivityResultFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public Observable<ActivityResultInfo> startForResult(final Intent intent, final int requestCode) {
        PublishSubject<ActivityResultInfo> subject = PublishSubject.create();
        mSubjects.put(requestCode, subject);
        return subject.doOnSubscribe(disposable -> startActivityForResult(intent, requestCode));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PublishSubject<ActivityResultInfo> subject = mSubjects.remove(requestCode);
        if (subject != null) {
            subject.onNext(new ActivityResultInfo(requestCode, resultCode, data));
            subject.onComplete();
        }
    }
}
