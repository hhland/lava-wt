package lava.wt.common;

public class WebCommon {

	public static String htmlWapper(String html) {
        html = html.replace("<", "&lt;");
        html = html.replace(">", "&gt;");
        return html;
    }
}
