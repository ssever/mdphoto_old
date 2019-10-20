package labs.com.mdfoto.gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import labs.com.mdfoto.Utils;


public class MySharedPrefs {
    private static SharedPreferences sharedPreferences;
    private static Gson gson;
    private static MySharedPrefs mySharedPrefs;

    public static MySharedPrefs getInstance(Context context) {
        if (mySharedPrefs == null) {
            mySharedPrefs = new MySharedPrefs(context);
        }
        return mySharedPrefs;
    }

    private MySharedPrefs(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        gson = new GsonBuilder().create();
    }

    private static void saveString(String key, String string) {
        sharedPreferences.edit().putString(key, string).apply();
    }

    public static void saveObject(String key, Object object) {
        String json = gson.toJson(object);
        MySharedPrefs.saveString(key, json);
    }

    public static <T> T loadObject(String key, Class<T> type) {
        String object = sharedPreferences.getString(key, null);
        if (object != null) {
            return gson.fromJson(object, type);
        } else {
            return null;
        }
    }

    public static void exportTo(Object o, String path){
        String json = gson.toJson(o);
        Utils.mCreateAndSaveFile(json,path);
    }
}