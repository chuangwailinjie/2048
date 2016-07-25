package space.peihao.a2048;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/7/25.
 */
public class ShareUtils {
    public static String get2048Dir(){
        return Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"2048";
    }

    public static Bitmap captureScreen(Activity activity){
        activity.getWindow().getDecorView().setDrawingCacheEnabled(true);
        Bitmap bitmap=activity.getWindow().getDecorView().getDrawingCache();
        return bitmap;
    }

    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static void share2048(Activity activity) throws FileNotFoundException{
        Intent it=new Intent(Intent.ACTION_SEND);
        File f=new File(save2048Capture(activity));
        if(f!=null&&f.exists()&&f.isFile()){
            it.setType("image/*");
            Uri u=Uri.fromFile(f);
            it.putExtra(Intent.EXTRA_STREAM,u);
        }
        it.putExtra(Intent.EXTRA_SUBJECT, "Share");
        it.putExtra(Intent.EXTRA_TEXT, "发现了一款颜值极高的2048App「Love2048」! 推荐~: http://peihao.space");
        //When using this flag, if a task is already running for the activity you are now starting, then a new activity will not be started
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(it,"窗外临街2048"));
    }

    public static String save2048Capture(Activity activity)throws  FileNotFoundException{
        if (!isSDCardEnable()){
            Toast.makeText(activity,"图片保存失败",Toast.LENGTH_SHORT).show();
            return "";
        }
        File my2048Dir=new File(get2048Dir());
        if(!my2048Dir.exists()){
            my2048Dir.mkdir();
        }
        File captureimg=new File(my2048Dir,"2048.jpg");
        try{
            captureimg.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fos=new FileOutputStream(captureimg);
        captureScreen(activity).compress(Bitmap.CompressFormat.JPEG,80,fos);
        try {
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return captureimg.getAbsolutePath();
    }

    public static String saveWechatPay(Activity activity)throws FileNotFoundException{
        if(!isSDCardEnable()){
            Toast.makeText(activity,"图片保存失败",Toast.LENGTH_SHORT).show();
            return "";
        }
        File my2048Dir=new File(get2048Dir());
        if(!my2048Dir.exists()){
            my2048Dir.mkdir();
        }
        File wximg=new File(my2048Dir,"wx.jpg");
        try {
            wximg.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap= BitmapFactory.decodeResource(activity.getResources(),R.drawable.weixin);
        FileOutputStream fos=new FileOutputStream(wximg);
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,fos);
        try {
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wximg.getAbsolutePath();
    }
}
