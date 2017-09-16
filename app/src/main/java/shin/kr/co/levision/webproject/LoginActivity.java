package shin.kr.co.levision.webproject;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import shin.kr.co.levision.webproject.Parameters.LoginInfomation;
import shin.kr.co.levision.webproject.Parameters.WebUrlClass;

public class LoginActivity extends AppCompatActivity {

    public static final int STARTUP_DELAY = 300;
    public static final int ANIM_ITEM_DURATION = 1000;
    public static final int ITEM_DELAY = 300;

    EditText idEdit,passwordEdit;

    OkHttpClient client;

    private boolean animationStarted = false;

    Boolean RUN_LOGIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idEdit = (EditText)findViewById(R.id.Login_SchoolEditText);
        passwordEdit = (EditText)findViewById(R.id.Login_PassworkEditText);

        idEdit.setText("한국산업기술대학교");
        passwordEdit.setText("1");

        client = new OkHttpClient();

        RUN_LOGIN = false;

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (!hasFocus || animationStarted) {
            return;
        }

        animate();

        super.onWindowFocusChanged(hasFocus);
    }

    public void loginButtonClick(View v)
    {
        if(!RUN_LOGIN) threadLogin();
    }

    private void animate() {
        //ImageView logoImageView = (ImageView) findViewById(R.id.img_logo);
        ViewGroup container = (ViewGroup) findViewById(R.id.activity_login);

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
                        .setStartDelay((ITEM_DELAY * i) + 500)
                        .setDuration(500);
            }

            viewAnimator.setInterpolator(new DecelerateInterpolator()).start();
        }
    }




    private void threadLogin(){

        new Thread() {
            @Override
            public void run() {

                RUN_LOGIN = true;

                String requestUrl = WebUrlClass.loginUrl;
                String paramaterUrl = "?id=" + idEdit.getText().toString() + "&pw=" + passwordEdit.getText().toString();

                OkHttpClient copy = client.newBuilder()
                        .readTimeout(2000, TimeUnit.MILLISECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(requestUrl+paramaterUrl)
                        .build();

                try {
                    Response response = copy.newCall(request).execute();

                    //String rt;

                    //rt = response.body().string();
                    //JSONArray json = new JSONArray(response.body().string());

                    JSONObject obj = new JSONObject(response.body().string());

                   // for(int i = 0; i < json.length(); i++){
                   //     JSONObject obj = json.optJSONObject(i);

                        LoginInfomation.initInfomation();

                        LoginInfomation.info.setUid(obj.optString(LoginInfomation.PARAM_UID));
                        LoginInfomation.info.setName(obj.optString(LoginInfomation.PARAM_NAME));
                        LoginInfomation.info.setuType(obj.optString(LoginInfomation.PARAM_UTYPE));
                        LoginInfomation.info.setArea(obj.optString(LoginInfomation.PARAM_AREA));
                        LoginInfomation.info.setuScale(obj.optString(LoginInfomation.PARAM_USCALE));


                 //   }
                } catch (IOException e) {
                    e.printStackTrace();
                    makeToast.sendEmptyMessage(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                    makeToast.sendEmptyMessage(0);
                }

                if(LoginInfomation.info.getUid().equals("0")){
                    makeToast.sendEmptyMessage(0);
                }else{
                    nextActivity.sendEmptyMessage(0);
                }

                RUN_LOGIN = false;

            }
        }.start();
    }

    Handler makeToast = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(LoginActivity.this, "로그인 정보를 확인해주세요", Toast.LENGTH_SHORT).show();
        }
    };

    Handler nextActivity = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();

        }
    };

}
