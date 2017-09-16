package shin.kr.co.levision.webproject;

import android.app.Application;
import android.graphics.Typeface;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ROH on 2016-11-10.
 */

public class FontApplication extends Application {
    @Override
    public void onCreate()
    {
        super.onCreate();

        Locale locale = this.getResources( ).getConfiguration( ).locale;
        locale.getLanguage( );

        Typeface font = null;

       // font = Typeface.createFromAsset(this.getResources().getAssets(),"NotoSansCJKkr-Regular.otf");

       // injectTypeface("noto_kr", font);
    }

    private boolean injectTypeface(String fontFamily, Typeface typeface)
    {
        try
        {
            Field field = Typeface.class.getDeclaredField("sSystemFontMap");
            field.setAccessible(true);
            Object fieldValue = field.get(null);
            Map<String, Typeface> map = (Map<String, Typeface>) fieldValue;
            map.put(fontFamily, typeface);
            return true;
        }
        catch (Exception e)
        {
            Log.e("Font-Injection", "Failed to inject typeface.", e);
        }
        return false;
    }

}
