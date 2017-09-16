package shin.kr.co.levision.webproject;

import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import shin.kr.co.levision.webproject.Parameters.LoginInfomation;
import shin.kr.co.levision.webproject.Parameters.WebUrlClass;

public class MainActivity extends AppCompatActivity {

    final int REQ_BACK = 10011;

    public static final int STARTUP_DELAY = 300;
    public static final int ANIM_ITEM_DURATION = 1000;
    public static final int ITEM_DELAY = 300;

    private boolean animationStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 소프트키 활성화 디바이스 하단크기만큼 올려줘고 여백 사이즈 매꿔야됨
    }

    public void mainImageClick(View v)
    {
        Intent intent = null;
        switch (v.getId())
        {
            case R.id.Main_Layout_total :
                intent = new Intent(getApplicationContext(),ViewActivity.class);
                intent.putExtra("URL", WebUrlClass.allUrl+ LoginInfomation.info.makeParameterUrlForAll());
                intent.putExtra("TITLE",getString(R.string.STR_all));
                intent.putExtra("CODE",WebUrlClass.CODE_ALL);
                startActivityForResult(intent,REQ_BACK);
                break;
            case R.id.Main_Layout_check :
                intent = new Intent(getApplicationContext(),ViewActivity.class);
                intent.putExtra("URL", WebUrlClass.checkUrl+ LoginInfomation.info.makeParameterUrl());
                intent.putExtra("TITLE",getString(R.string.STR_check));
                intent.putExtra("CODE",WebUrlClass.CODE_CHECK);
                startActivityForResult(intent,REQ_BACK);
                break;
            case R.id.Main_Layout_variation :
                intent = new Intent(getApplicationContext(),ViewActivity.class);
                intent.putExtra("URL", WebUrlClass.variationUrl+ LoginInfomation.info.makeParameterUrl());
                intent.putExtra("TITLE",getString(R.string.STR_variation));
                intent.putExtra("CODE",WebUrlClass.CODE_VARIATION);
                startActivityForResult(intent,REQ_BACK);
                break;
            case R.id.Main_Layout_simulation :
                intent = new Intent(getApplicationContext(),ViewActivity.class);
                intent.putExtra("URL", WebUrlClass.simulationUrl+ LoginInfomation.info.makeParameterUrl());
                intent.putExtra("TITLE",getString(R.string.STR_simulation));
                intent.putExtra("CODE",WebUrlClass.CODE_SIMUL);
                startActivityForResult(intent,REQ_BACK);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== REQ_BACK)
        {
            if(resultCode==RESULT_OK) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (!hasFocus || animationStarted) {
            return;
        }

        animate();

        super.onWindowFocusChanged(hasFocus);
    }


    private void animate() {
        //ImageView logoImageView = (ImageView) findViewById(R.id.img_logo);
        ViewGroup container = (ViewGroup) findViewById(R.id.Main_view);

        /*
        ViewCompat.animate(logoImageView)
                .translationY(-250)
                .setStartDelay(STARTUP_DELAY)
                .setDuration(ANIM_ITEM_DURATION).setInterpolator(
                new DecelerateInterpolator(1.2f)).start();*/

        for (int i = 0; i < container.getChildCount(); i++) {
            View v = container.getChildAt(i);
            ViewPropertyAnimatorCompat viewAnimator;

            if (!(v instanceof Button)) {
                viewAnimator = ViewCompat.animate(v)
                        .translationY(1).alpha(1)
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(500);
            } else {
                viewAnimator = ViewCompat.animate(v)
                        .scaleY(1).scaleX(1).alpha(1)
                        .setStartDelay((ITEM_DELAY * i) + 800)
                        .setDuration(500);
            }

            viewAnimator.setInterpolator(new DecelerateInterpolator()).start();
        }
    }

}
