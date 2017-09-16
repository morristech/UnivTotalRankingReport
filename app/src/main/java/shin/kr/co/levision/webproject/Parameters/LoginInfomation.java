package shin.kr.co.levision.webproject.Parameters;

/**
 * Created by ROH on 2016-12-28.
 */

public class LoginInfomation {

    public static LoginInfomation info = null;

    public final static String PARAM_UID = "UID";
    public final static String PARAM_NAME = "Name";
    public final static String PARAM_UTYPE = "UType";
    public final static String PARAM_AREA = "Area";
    public final static String PARAM_USCALE = "UScale";

    String uid;
    String name;
    String uType;
    String area;
    String uScale;

    public static void initInfomation(){
        info = new LoginInfomation();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getuType() {
        return uType;
    }

    public void setuType(String uType) {
        this.uType = uType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getuScale() {
        return uScale;
    }

    public void setuScale(String uScale) {
        this.uScale = uScale;
    }

    public String makeParameterUrl()
    {
        return "?UID="+uid+"&UType="+uType+"&Area=전체&UScale=전체";
    }

    public String makeParameterUrlForAll()
    {
        return "?UID="+uid+"&UType="+uType+"&Area=전체&UScale=전체&NYear=2015";
    }

}
