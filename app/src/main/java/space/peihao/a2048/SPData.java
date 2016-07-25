package space.peihao.a2048;

import android.content.Context;
import android.content.SharedPreferences;

public class SPData {
    private static Context context = MyApp.getContext();
    private static final String CC_2048 = "2048";

    public static boolean saveBestScore(int bestScore){
        SharedPreferences preferences = context.getSharedPreferences(CC_2048,Context.MODE_PRIVATE);
        return preferences.edit().putInt("bestScore",bestScore).commit();
    }

    public static int getBestScore(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(CC_2048,Context.MODE_PRIVATE);
        return sharedPreferences.getInt("bestScore",0);
    }
}
