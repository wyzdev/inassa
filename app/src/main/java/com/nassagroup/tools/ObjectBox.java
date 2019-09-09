package com.nassagroup.tools;

import android.content.Context;
import android.util.Log;

import com.nassagroup.BuildConfig;
import com.nassagroup.core.MyObjectBox;


import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

/**
 * Created by Noel Emmanuel Roodly on 8/19/2019.
 */
public class ObjectBox {

    private static BoxStore boxStore;
//    private static Box<Card> cardBox;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder().androidContext(context.getApplicationContext()).build();
        if(BuildConfig.DEBUG){
            Log.d("DEBUG_DB", String.format("Using ObjectBox %s (%s)", BoxStore.getVersion(), BoxStore.getVersionNative()));
            new AndroidObjectBrowser(boxStore).start(context);
        }

//      cardBox = boxStore.boxFor(Card.class);
    }

    public static BoxStore get() { return boxStore; }
    public static BoxStore getBoxStore() { return boxStore; }
    public static void setBoxStore(BoxStore boxStore) { ObjectBox.boxStore = boxStore; }

}
