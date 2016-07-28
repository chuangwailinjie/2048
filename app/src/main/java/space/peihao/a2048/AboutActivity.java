package space.peihao.a2048;

import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {


    @BindView(R.id.main_content)
    CoordinatorLayout mainContent;

    @OnClick(R.id.fab_like)
    void likeClicked() {
        showAlertDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        MyApp.getInstance().addActivity(this);
        ButterKnife.bind(this);
    }

    private void showAlertDialog(){
        SquareImageView imageView = new SquareImageView(this);
        imageView.setImageResource(R.drawable.weixin);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("求打赏><");
        builder.setView(imageView);
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                try {
                    ShareUtils.saveWechatPay(AboutActivity.this);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Snackbar.make(mainContent,"图片以保存", Snackbar.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
