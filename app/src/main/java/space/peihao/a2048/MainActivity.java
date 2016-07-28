package space.peihao.a2048;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import space.peihao.a2048.widget.my2048Layout;

public class MainActivity extends AppCompatActivity implements my2048Layout.On2048Listener{

    @BindView(R.id.best_score)
    TextView tv_bestscore;
    @BindView(R.id.current_score)
    TextView tv_currentscore;
    @BindView(R.id.game_view)
    my2048Layout gameLayout;
    @OnClick(R.id.tv_restart)
    void restart(){
        gameLayout.restart();
    }


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApp.getInstance().addActivity(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        gameLayout.set2048Listener(this);
        tv_bestscore.setText("BestScore: " + SPData.getBestScore());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.about:
                Intent it=new Intent();
                it.setClass(this,AboutActivity.class);
                startActivity(it);
                break;
            case R.id.share:
                try {
                    ShareUtils.share2048(this);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScoreChanged(int score) {
        if(SPData.getBestScore()<score){
            SPData.saveBestScore(score);
            tv_bestscore.setText("BestScore: "+score);
        }

        tv_currentscore.setText("CurrentScore: "+score);
    }

    @Override
    public void onGameOver() {
        showGameOver();
    }

    private void showGameOver(){

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("窗外临街");
        builder.setMessage("Small ZhaZha，GameOver! Try or Go Home?");
        builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameLayout.restart();
                //关闭对话框，dismiss直接释放对话框资源，hide则是隐藏
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyApp.getInstance().exit();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
