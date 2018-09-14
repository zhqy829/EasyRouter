package com.zhqydot.framework.easyrouter.core.safr;

import android.content.Intent;

public class ActivityResultInfo {
    public int requestCode;
    public int resultCode;
    public Intent data;

    public ActivityResultInfo(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        this.resultCode = resultCode;
        this.data = data;
    }
}
