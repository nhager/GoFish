package com.example.nh612u.gofish;

import android.content.Context;
import android.content.Intent;

public class IntentHelper {
    public static Intent createNewIntent(Intent oldIntent, Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        if (oldIntent.getExtras() != null && oldIntent.getExtras().size() > 0) {
            intent.putExtras(oldIntent.getExtras());
        }
        return intent;
    }

    public static Intent createNewIntent(Context context, Class clazz, String[] keys,
                                         String[] vals) {
        Intent intent = new Intent(context, clazz);
        if (keys.length == vals.length) {
            for (int i = 0; i < keys.length; i++) {
                final String key = keys[i];
                final String val = vals[i];
                intent.putExtra(key, val);
            }
        }
        return intent;
    }
}
