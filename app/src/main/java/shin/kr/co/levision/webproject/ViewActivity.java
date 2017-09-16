package shin.kr.co.levision.webproject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.saeid.fabloading.LoadingView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import shin.kr.co.levision.webproject.Parameters.LoginInfomation;
import shin.kr.co.levision.webproject.Parameters.WebUrlClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkUIClient;
import org.xwalk.core.XWalkView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static android.view.KeyEvent.KEYCODE_BACK;

public class ViewActivity extends AppCompatActivity {

    XWalkView webView;
    String url;
    //String title;

    LoadingView loadingView;

    DrawerLayout drawerLayout;
    FrameLayout tabLayout;

    RelativeLayout actionBar;
    RelativeLayout subActionbar;
    RelativeLayout bottomBar;

    ImageView drawerButton;
    TextView ActionbarTitleTextview;
    TextView collegeNameTextview;

    Boolean LOADING = false;

    int CODE;

    OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Intent intent = getIntent();


        url = intent.getStringExtra("URL");
        CODE = intent.getIntExtra("CODE",WebUrlClass.CODE_ALL);
        //title = intent.getStringExtra("TITLE");

        loadingView = (LoadingView)findViewById(R.id.View_loading_view);
        loadingView.addAnimation(R.color.Color_Sub_all,R.drawable.rsz_all,LoadingView.FROM_TOP);
        loadingView.addAnimation(R.color.Color_Sub_check,R.drawable.rsz_check,LoadingView.FROM_RIGHT);
        loadingView.addAnimation(R.color.Color_Sub_simulation,R.drawable.rsz_simulation,LoadingView.FROM_LEFT);
        loadingView.addAnimation(R.color.Color_Sub_variation,R.drawable.rsz_variation,LoadingView.FROM_BOTTOM);
        loadingView.setDuration(400);

        loadingView.addListener(new LoadingView.LoadingListener() {
            @Override
            public void onAnimationStart(int currentItemPosition) {

            }

            @Override
            public void onAnimationRepeat(int nextItemPosition) {

            }

            @Override
            public void onAnimationEnd(int nextItemPosition) {
                if(LOADING){
                    loadingView.startAnimation();
                }else{
                    loadingView.setVisibility(View.INVISIBLE);
                }
            }
        });

        drawerLayout = (DrawerLayout)findViewById (R.id.activity_view);
        tabLayout = (FrameLayout)findViewById(R.id.View_TAB_LAYOUT);

        actionBar = (RelativeLayout)findViewById(R.id.View_ActionBar);
        subActionbar = (RelativeLayout)findViewById(R.id.View_SubActionBar);
        bottomBar = (RelativeLayout)findViewById(R.id.View_BottomBar);

        XWalkPreferences.setValue("enable-javascript",true);
        webView = (XWalkView)findViewById(R.id.View_webview);
        ActionbarTitleTextview = (TextView)findViewById(R.id.View_ActionBar_Textview);
        collegeNameTextview = (TextView)findViewById(R.id.View_SubActionBar_Textview);
        drawerButton = (ImageView)findViewById(R.id.View_ActionBar_Drawer);

        collegeNameTextview.setText(LoginInfomation.info.getName());

        //ActionbarTitleTextview.setText(title);

        /*
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed(); // SSL 에러가 발생해도 계속 진행!
            }
        });
        */

        /*
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.addJavascriptInterface(new AndroidBridge(), "HybridApp");
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadUrl(url);
        webView.addJavascriptInterface(new JavascriptWepInterface(this), "Android");
        */

        webView.setUIClient(new XWalkUIClient(webView)
        {
            @Override
            public void onPageLoadStarted(XWalkView view, String url) {
                super.onPageLoadStarted(view, url);
                LOADING = true;
                loadingView.setVisibility(View.VISIBLE);
                loadingView.startAnimation();
            }

            @Override
            public void onPageLoadStopped(XWalkView view, String url, LoadStatus status) {
                super.onPageLoadStopped(view, url, status);
                LOADING = false;
                //loadingView.setVisibility(View.INVISIBLE);
            }

        });

        //webView.load("javascript:uid=1057",null);
        webView.addJavascriptInterface(new AndroidBridge(),"android");

        webView.load(url,null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.pauseTimers();
            webView.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.resumeTimers();
            webView.onShow();
        }
        setActivityColor();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.clearCache(true);
            webView.onDestroy();
        }
    }

    public class JavascriptWepInterface {

        private Context mContext;

        public JavascriptWepInterface(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void closeAndLoad(){
            finish();
        }

        @JavascriptInterface
        public void closeWebview(String msg) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                        ViewActivity.this.finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        @JavascriptInterface
        public void closeAndLoad(String msg){
            finish();
        }

        @JavascriptInterface
        public void closeWebview() {
            finish();
        }
    }

    public void viewTabClick(View v)
    {
        webView.clearCache(true);

        switch (v.getId())
        {
            case R.id.View_Tab_Main_Image :
                finish();
                break;
            case R.id.View_Tab_Logout_Image:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.View_Tab_Check_Image:
                url = WebUrlClass.checkUrl + LoginInfomation.info.makeParameterUrl();
                CODE = WebUrlClass.CODE_CHECK;
             //   actionBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_check));
             //   bottomBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_check));
             //   subActionbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_Sub_check));
                ActionbarTitleTextview.setText(getString(R.string.STR_check));
                break;
            case R.id.View_Tab_Veriation_Image:
                url = WebUrlClass.variationUrl + LoginInfomation.info.makeParameterUrl();
                CODE = WebUrlClass.CODE_VARIATION;
              //  actionBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_variation));
              //  bottomBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_variation));
              //  subActionbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_Sub_variation));
                ActionbarTitleTextview.setText(getString(R.string.STR_variation));
                break;
            case R.id.View_Tab_Simulation_Image:
                url = WebUrlClass.simulationUrl + LoginInfomation.info.makeParameterUrl();
                CODE = WebUrlClass.CODE_SIMUL;
              //  actionBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_simulation));
             //   bottomBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_simulation));
             //   subActionbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_Sub_simulation));
                ActionbarTitleTextview.setText(getString(R.string.STR_simulation));
                break;
        }

        webView.load(url,null);

        setActivityColor();

        drawerLayout.closeDrawer(tabLayout);

    }

    public void setActivityColor(){

        if(CODE == WebUrlClass.CODE_CHECK /*url.equals(WebUrlClass.checkUrl)*/)
        {
            actionBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_check));
            bottomBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_check));
            subActionbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_Sub_check));

            ActionbarTitleTextview.setText(getString(R.string.STR_check));
        }else if (CODE == WebUrlClass.CODE_ALL /*url.equals(WebUrlClass.allUrl)*/){
            actionBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_all));
            bottomBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_all));
            subActionbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_Sub_all));
            ActionbarTitleTextview.setText(getString(R.string.STR_all));
        }else if (CODE == WebUrlClass.CODE_VARIATION /*url.equals(WebUrlClass.variationUrl)*/){
            actionBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_variation));
            bottomBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_variation));
            subActionbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_Sub_variation));
            ActionbarTitleTextview.setText(getString(R.string.STR_variation));
        }else if (CODE == WebUrlClass.CODE_SIMUL /*url.equals(WebUrlClass.simulationUrl)*/){
            actionBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_simulation));
            bottomBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_simulation));
            subActionbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Color_Sub_simulation));
            ActionbarTitleTextview.setText(getString(R.string.STR_simulation));
        }

    }

    public void actionbarDrawerClick(View v)
    {
        if(drawerLayout.isDrawerVisible(tabLayout))
        {
            drawerLayout.closeDrawer(tabLayout);
        }else{
            drawerLayout.openDrawer(tabLayout);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode)
        {
            case KEYCODE_BACK:
                if(drawerLayout.isDrawerVisible(tabLayout))
                {
                    drawerLayout.closeDrawer(tabLayout);
                }else{
                    finish();
                }
                break;
        }
        return true;
    }

    /*
    private void threadGetData(){

        new Thread() {
            @Override
            public void run() {


                String strRequest = WebUrlClass.uri;

                OkHttpClient copy = client.newBuilder()
                        .readTimeout(2000, TimeUnit.MILLISECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(strRequest)
                        .build();

                try {
                    Response response = copy.newCall(request).execute();

                    //String rt;

                    //rt = response.body().string();
                    JSONArray json = new JSONArray(response.body().string());

                    for(int i = 0; i < json.length(); i++){
                        JSONObject obj = json.optJSONObject(i);

                        Hivs hivs = new Hivs();

                        if(GlobalCheckInfo.info.getSelectedParameter() == GlobalParameter.SET_HIV){
                            hivs.setId(obj.optString("id"));
                            hivs.setTested_at(obj.optString("tested_at"));
                            hivs.setTester(obj.optString("tester"));
                            hivs.setHiv1(obj.optString("hiv1"));
                            hivs.setHiv2(obj.optString("hiv2"));
                            hivs.setMemo(obj.optString("memo"));
                            hivs.setImage(obj.optString("image"));              // 이미지 다운로드 구현하지 않음
                            hivs.setCreated_at(obj.optString("created_at"));
                            hivs.setUpdated_at(obj.optString("updated_at"));

                            hivsList.add(hivs);
                        }
                        else if(GlobalCheckInfo.info.getSelectedParameter() == GlobalParameter.SET_Malaria){
                            hivs.setId(obj.optString("id"));
                            hivs.setTested_at(obj.optString("tested_at"));
                            hivs.setTester(obj.optString("tester"));
                            hivs.setHiv1(obj.optString("pf"));
                            hivs.setHiv2(obj.optString("pv"));
                            hivs.setMemo(obj.optString("memo"));
                            hivs.setImage(obj.optString("image"));              // 이미지 다운로드 구현하지 않음
                            hivs.setCreated_at(obj.optString("created_at"));
                            hivs.setUpdated_at(obj.optString("updated_at"));

                            hivsList.add(hivs);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    makeToast.sendEmptyMessage(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
*/


    public class AndroidBridge{
        public void uid(){

        }
    }



}
