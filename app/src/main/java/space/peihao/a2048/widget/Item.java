package space.peihao.a2048.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import space.peihao.a2048.R;

/**
 * Created by Administrator on 2016/7/24.
 */
public class Item extends View{
    private int num;
    private String numStr;
    private Paint paint;
    private Rect square;

    private float textSize=25*getResources().getDisplayMetrics().density;
    public Item(Context context){
        super(context);
        paint=new Paint();
        //抗锯齿效果
        paint.setAntiAlias(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int bgColor;
        switch(num){
            case 0:
                bgColor= R.color.white;
                break;
            case 2:
                bgColor=R.color.grey;
                break;
            case 4:
                bgColor=R.color.cyan;
                break;
            case 8:
                bgColor=R.color.purple;
                break;
            case 16:
                bgColor=R.color.pink;
                break;
            case 32:
                bgColor=R.color.yellow;
                break;
            case 64:
                bgColor=R.color.orange;
                break;
            case 128:
                bgColor=R.color.cute_red;
                break;
            case 256:
                bgColor=R.color.light_shine;
                break;
            case 512:
                bgColor=R.color.light_pink;
                break;
            case 1024:
                bgColor=R.color.dark_grey;
                break;
            case 2048:
                bgColor=R.color.red;
                break;
            default:
                bgColor=R.color.white;
        }

        paint.setColor(getResources().getColor(bgColor));
        paint.setStyle(Paint.Style.FILL);

        RectF rectf=new RectF(0,0,getWidth(),getHeight());
        canvas.drawRoundRect(rectf,10,10,paint);
        if(num!=0){
            drawText(canvas);
        }
    }

    private void drawText(Canvas canvas){
        paint.setColor(Color.WHITE);
        float x=(getWidth()-square.width())/2;
        float y=(getHeight()+square.height())/2;
        canvas.drawText(numStr,x,y,paint);
    }

    public void setNumber(int number){
        this.num=number;
        this.numStr=number+"";
        paint.setTextSize(this.textSize);
        square=new Rect();
        paint.getTextBounds(numStr,0,numStr.length(),square);
        invalidate();
    }

    public int getNum() {
        return num;
    }

    public void scaleIn(){
        ObjectAnimator.ofFloat(getContext(),"scale_x",0f,1f).setDuration(200).start();
        ObjectAnimator.ofFloat(getContext(),"scale_y",0f,1f).setDuration(200).start();
    }
}
