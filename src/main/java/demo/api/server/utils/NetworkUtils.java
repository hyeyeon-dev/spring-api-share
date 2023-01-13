package demo.api.server.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtils {

    public static String getServerIp() {

        InetAddress local = null;
        try {
            local = InetAddress.getLocalHost();
        }
        catch ( UnknownHostException e ) {
            e.printStackTrace();
        }

        if( local == null ) {
            return "";
        }
        else {
            String ip = local.getHostAddress();
            return ip;
        }
    }
}
