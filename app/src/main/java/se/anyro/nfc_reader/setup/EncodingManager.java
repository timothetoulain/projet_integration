package se.anyro.nfc_reader.setup;

/**
 * replace all the special characters that could be receive in the server response after a query
 * replace all the accents by the letter without accent, necessary before a query
 */


public class EncodingManager {
    public static String convert(String sb){
        if(sb.contains(("\\u00e9"))) {
            sb=sb.replace("\\u00e9","é");
        }
        if(sb.contains(("\\u00e0"))) {
            sb=sb.replace("\\u00e0","à");
        }
        if(sb.contains(("\\u00e7"))) {
            sb=sb.replace("\\u00e7","ç");
        }
        if(sb.contains(("\\u00e8"))) {
            sb=sb.replace("\\u00e8","è");
        }
        if(sb.contains(("\\u00ea"))) {
            sb=sb.replace("\\u00ea","ê");
        }
        return sb;
    }
    public static String deleteAccent(String sb) {
        if(sb.contains(("é"))) {
            sb=sb.replace("é","e");
        }
        if(sb.contains(("ê"))) {
            sb=sb.replace("ê","e");
        }
        if(sb.contains(("è"))) {
            sb=sb.replace("è","e");
        }
        if(sb.contains(("à"))) {
            sb=sb.replace("à","a");
        }
        if(sb.contains(("ç"))) {
            sb=sb.replace("ç","c");
        }
        return sb;
    }
}
