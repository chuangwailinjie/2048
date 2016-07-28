package space.peihao.a2048;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import space.peihao.a2048.update.UpdateManager;

public class choose extends AppCompatActivity {

    @BindView(R.id.top_4)
    CircleButton bt_top;
    @BindView(R.id.bottom_5)
    CircleButton bt_bottom;
    @OnClick({R.id.top_4,R.id.bottom_5})
    public void onclick(View v){
        int id=v.getId();
        switch(id){
            case R.id.top_4:
                MyApp.setColumn(4);
                break;
            case R.id.bottom_5:
                MyApp.setColumn(5);
                break;
        }
        Intent it=new Intent();
        it.setClass(this,MainActivity.class);
        startActivity(it);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        MyApp.getInstance().addActivity(this);
        ButterKnife.bind(this);
        UpdateManager ud=new UpdateManager(this);
        ud.checkUpdate();
    }
}
