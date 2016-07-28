package space.peihao.a2048;

import android.app.Activity;
import android.app.Application;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

public class MyApp extends Application {
    static Context context;
    static int column=4;

    public static final String version="1.1.0";

    private List<Activity> mList = new LinkedList<Activity>();
    private static MyApp instance;

    public synchronized static MyApp getInstance() {
        if (null == instance) {
            instance = new MyApp();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        mList.add(activity);
    }


    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static int getColumn() {
        return column;
    }

    public static void setColumn(int column) {
        MyApp.column = column;
    }

    public static Context getContext(){
        return context;
    }


}
