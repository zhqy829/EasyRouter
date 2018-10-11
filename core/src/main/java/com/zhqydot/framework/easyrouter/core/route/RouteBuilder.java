package com.zhqydot.framework.easyrouter.core.route;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import com.zhqydot.framework.easyrouter.core.arp.ActivityResultInfo;
import com.zhqydot.framework.easyrouter.core.common.EasyRouter;

import java.io.Serializable;

import io.reactivex.Observable;

/**
 * @author zhqy
 * @date 2018/9/16
 */

public class RouteBuilder {

    private Context mContext;
    private String mPath;
    private Bundle mExtras;

    public RouteBuilder(String path) {
        this(null, path);
    }

    public RouteBuilder(Context context, String path) {
        mContext = context;
        mPath = path;
    }

    public RouteBuilder withInt(String key, int value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putInt(key, value);
        return this;
    }

    public RouteBuilder withFloat(String key, float value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putFloat(key, value);
        return this;
    }

    public RouteBuilder withDouble(String key, double value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putDouble(key, value);
        return this;
    }

    public RouteBuilder withLong(String key, long value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putLong(key, value);
        return this;
    }

    public RouteBuilder withBoolean(String key, boolean value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putBoolean(key, value);
        return this;
    }

    public RouteBuilder withByte(String key, byte value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putByte(key, value);
        return this;
    }

    public RouteBuilder withShort(String key, short value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putShort(key, value);
        return this;
    }

    public RouteBuilder withCharSequence(String key, CharSequence value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putCharSequence(key, value);
        return this;
    }

    public RouteBuilder withString(String key, String value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putString(key, value);
        return this;
    }

    public RouteBuilder withSerializable(String key, Serializable value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putSerializable(key, value);
        return this;
    }

    public RouteBuilder withParcelable(String key, Parcelable value) {
        if (mExtras == null) {
            mExtras = new Bundle();
        }
        mExtras.putParcelable(key, value);
        return this;
    }

    public void navigation() {
        EasyRouter.navigation(this);
    }

    public Observable<ActivityResultInfo> navigationForResult(int requestCode) {
        return EasyRouter.navigationForResult(this, requestCode);
    }

    public Context getContext() {
        return mContext;
    }

    public String getPath() {
        return mPath;
    }

    public Bundle getExtras() {
        return mExtras;
    }
}
