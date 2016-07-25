package space.peihao.a2048;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    static Context context;
    static int column=4;
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
