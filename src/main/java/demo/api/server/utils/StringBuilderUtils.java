package demo.api.server.utils;

public class StringBuilderUtils {
    private StringBuilder sb;

    public StringBuilderUtils() {
        sb = new StringBuilder();
    }

    public void append(String str) {
        sb.append(str != null? str : "");
    }

    public void appendLine(String str) {
        sb.append(str != null? str : "").append(System.getProperty("line.separator"));
    }

    public String toString() {
        return sb.toString();
    }
}
