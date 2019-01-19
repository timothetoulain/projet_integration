package se.anyro.nfc_reader.setup;

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
}
