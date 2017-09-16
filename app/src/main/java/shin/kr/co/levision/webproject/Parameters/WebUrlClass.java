package shin.kr.co.levision.webproject.Parameters;

/**
 * Created by ROH on 2016-12-05.
 */

public class WebUrlClass {

    public static int CODE_ALL = 10001;
    public static int CODE_CHECK = 10002;
    public static int CODE_VARIATION = 10003;
    public static int CODE_SIMUL = 10004;

    public static String publicUrl = "http://58.73.147.94:8888";

    public static String allUrl = publicUrl+"/testpage/radar.html";
    public static String checkUrl = publicUrl+"/testpage/manpower.html";
    public static String variationUrl = publicUrl+"/testpage/y_manpower.html";
    public static String simulationUrl = publicUrl+"/testpage/s_manpower.html";

    public static String uri = publicUrl+"/SampleRestServer/ManPower";

    public static String loginUrl = publicUrl+"/samplerestserver/login";


    public static String getAllUrl() {
        return allUrl;
    }

    public static String getCheckUrl() {
        return checkUrl;
    }

}
