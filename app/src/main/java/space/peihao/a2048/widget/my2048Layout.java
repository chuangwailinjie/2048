package space.peihao.a2048.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import space.peihao.a2048.MyApp;

/**
 * Created by Administrator on 2016/7/24.
 */
public class my2048Layout extends RelativeLayout{

    private enum ACTION{
        LEFT,RIGHT,UP,DOWN
    }

    private int column ;
    private Item[] item2048s;
    private int margin = 10;
    private int padding;

    private GestureDetector gestureDetector;
    private boolean isMerged = true;
    private boolean isMoved = true;
    private boolean once=false;
    private int score;
    private On2048Listener on2048Listener;

    public my2048Layout(Context context) {
        this(context, null);
    }

    public my2048Layout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public my2048Layout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        column=MyApp.getColumn();
        margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                margin,
                getResources().getDisplayMetrics());
        padding = min(getPaddingLeft(), getPaddingRight(), getPaddingTop(), getPaddingBottom());
        gestureDetector = new GestureDetector(getContext(), new MyGestureDetector());
    }

    public interface On2048Listener{
        void onScoreChanged(int score);
        void onGameOver();
    }

    public void set2048Listener(On2048Listener on2048Listener){
        this.on2048Listener=on2048Listener;
    }

    private int min(int...params){
        int min=params[0];
        for(int param:params){
            if(min>param)
                min=param;
        }
        return min;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int game2048LayoutSize=min(getMeasuredHeight(),getMeasuredWidth());
        int item2048Size=(game2048LayoutSize-padding*2-(column-1)*margin)/column;

        if(!once){
            if(item2048s==null){
                item2048s=new Item[column*column];
            }

            for(int i=0;i<item2048s.length;i++){
                Item item=new Item(getContext());
                item2048s[i]=item;
                item.setId(i+1);
                RelativeLayout.LayoutParams  layoutParams=new LayoutParams(item2048Size,item2048Size);

                if((i+1)%column!=0)//如果不是最后一列
                    layoutParams.rightMargin=margin;
                if(i%column!=0) {//如果不是第一列 设置相对位置
                    layoutParams.addRule(RIGHT_OF,item2048s[i-1].getId());
                }

                if((i+1)>column){//如果Item不在第一行，则设置margin、相对位置
                    layoutParams.topMargin=margin;
                    layoutParams.addRule(BELOW,item2048s[i-column].getId());
                }
                addView(item,layoutParams);
            }
            generateNum();
        }
        once = true;
        setMeasuredDimension(game2048LayoutSize,game2048LayoutSize);//This method must be called by onMeasure(int, int) to store the measured width and measured height.
    }

    private void action(ACTION action){
        for(int i=0;i<column;i++){
            List<Item> row=new ArrayList<>();
            for(int j=0;j<column;j++){
                int index=getIndexByAction(action,i,j);
                Item item=item2048s[index];
                if(item.getNum()!=0)
                    row.add(item);
            }

            for(int j=0;j<column&&j<row.size();j++){
                int index = getIndexByAction(action, i, j);
                Item item = item2048s[index];
                if (item.getNum() != row.get(j).getNum())
                    isMoved = true;
            }

            mergeItem(row);

            for(int j=0;j<column;j++){
                int index=getIndexByAction(action,i,j);
                if(j<row.size())
                    item2048s[index].setNumber(row.get(j).getNum());
                else
                    item2048s[index].setNumber(0);
            }
        }
        generateNum();
    }

    public void restart(){
        for(Item item:item2048s){
            item.setNumber(0);
        }
        if(on2048Listener!=null)
            on2048Listener.onScoreChanged(0);
        isMoved = isMerged = true;
        generateNum();
    }

    private void mergeItem(List<Item> row){
        if(row.size()<2)
            return;
        for(int i=0;i<row.size()-1;i++) {
            Item item1 = row.get(i);
            Item item2 = row.get(i + 1);

            if (item1.getNum() == item2.getNum()){
                isMerged = true;
                int val = item1.getNum() + item2.getNum();
                item1.setNumber(val);

                score += val;
                if (on2048Listener != null)
                    on2048Listener.onScoreChanged(score);

                for (int x = i + 1; x < row.size() - 1; x++) {
                    row.get(x).setNumber(row.get(x + 1).getNum());
                }
                row.get(row.size() - 1).setNumber(0);
                return;
            }
        }
    }

    private int getIndexByAction(ACTION action,int x,int y){
        int index=-1;
        switch (action){//根据action的不同，将同一方向的item顺序序号改变，以最action方向的item为首
            case RIGHT:
                index=x*column+column-y-1;
                break;
            case LEFT:
                index=x*column+y;
                break;
            case DOWN:
                index=(column-y-1)*column+x;
                break;
            case UP:
                index=y*column+x;
                break;
        }
        return index;
    }

    private void generateNum(){
        if(checkOver()){
            if(on2048Listener!=null){
                on2048Listener.onGameOver();
            }
            return;
        }

        if(!isFull()){
            if(isMerged||isMoved){
                Random random=new Random();
                int index=random.nextInt(column*column);
                Item item=item2048s[index];

                while(item.getNum()!=0){
                    index=random.nextInt(column*column);
                    item=item2048s[index];
                }

                item.setNumber(Math.random()>0.75?4:2);
                item.scaleIn();
                isMoved=isMerged=false;
            }
        }
    }

    private boolean checkOver(){//检测是否所有都数字都不可移动 判定结束
        if(!isFull())
            return false;
        for(int i=0;i<column;i++)
            for(int j=0;j<column;j++){
                int index=i*column+j;
                Item item=item2048s[index];

                //检测右边
                if((index+1)%column!=0){//不是最后一列
                    Item right=item2048s[index+1];
                    if(right.getNum()==item.getNum())
                        return false;
                }

                //检测上边
                if((index+1)>column){
                    Item top=item2048s[index-column];
                    if(top.getNum()==item.getNum())
                        return false;
                }

                //检测左边
                if(index%column!=0){
                    Item left=item2048s[index-1];
                    if(left.getNum()==item.getNum())
                        return false;
                }

                //检测下面
                if(index<column*(column-1)){
                    Item bottom=item2048s[index+column];
                    if(bottom.getNum()==item.getNum())
                        return false;
                }
            }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private boolean isFull(){
        for(int i=0;i<item2048s.length;i++){
            if(item2048s[i].getNum()==0)
                return false;
        }
        return true;
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            //return super.onFling(e1, e2, velocityX, velocityY);
            float movex = e2.getX() - e1.getX();
            float movey = e2.getY() - e1.getY();
            if (movex > 100 && Math.abs(velocityX) > Math.abs(velocityY))
                action(ACTION.RIGHT);
            else if (movex < -100 && Math.abs(velocityX) > Math.abs(velocityY))
                action(ACTION.LEFT);
            else if (movey > 100 && Math.abs(velocityY) > Math.abs(velocityX))
                action(ACTION.DOWN);
            else if (movey < -100 && Math.abs(velocityY) > Math.abs(velocityX))
                action(ACTION.UP);
            return true;
        }
    }
}
