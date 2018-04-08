package lava.wt.common;

public class TextCommon extends lava.rt.common.TextCommon {

	public static String htmlWapper(String html) {
        html = html.replace("<", "&lt;");
        html = html.replace(">", "&gt;");
        return html;
    }
}
