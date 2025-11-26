package core;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public final class ConnectionFactory {
    private ConnectionFactory() {}

    public static Connection fromJson(JSONObject db) throws Exception {
        if (db == null) throw new IllegalArgumentException("db config required");
        String host = db.getString("host");
        int port    = db.optInt("port", 1521);
        String user = db.getString("user");
        String pass = db.getString("password");
        String serviceName = db.optString("serviceName", null);
        String sid         = db.optString("sid", null);

        String url;
        if (serviceName != null && serviceName.length() > 0) {
            url = "jdbc:oracle:thin:@//" + host + ":" + port + "/" + serviceName;
        } else if (sid != null && sid.length() > 0) {
            url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
        } else {
            throw new IllegalArgumentException("oracle requires serviceName or sid");
        }

        Class.forName("oracle.jdbc.OracleDriver");
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", pass);
        return DriverManager.getConnection(url, props);
    }
}
