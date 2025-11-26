package app;

import core.*;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(ErrorResponse.toJson("NO_INPUT", null, "no input"));
            System.exit(1);
        }

        JSONObject req = new JSONObject(args[0]);
        JSONObject db  = req.optJSONObject("db");
        if (db == null) {
            System.out.println(ErrorResponse.toJson("BAD_REQUEST", null, "db config required"));
            System.exit(1);
        }

        String page = req.optString("page", null);
        String op   = req.optString("op", null);
        JSONObject p = req.optJSONObject("params");
        if (p == null) p = new JSONObject();

        if (page == null || op == null) {
            System.out.println(ErrorResponse.toJson("BAD_REQUEST", null, "page/op required"));
            System.exit(1);
        }

        Connection conn = null;
        try {
            conn = ConnectionFactory.fromJson(db);
            ExecContext ctx = new ExecContext(conn);

            JSONObject body = new StaticRouter().handle(page, op, ctx, p);
            body.put("result", "ok");
            System.out.println(body.toString());

        } catch (OpException oe) {
            System.out.println(ErrorResponse.toJson(
                oe.getDef().name(),
                oe.getDetailCode(),
                oe.getMessage()
            ));
        } catch (SQLException se) {
            System.out.println(ErrorResponse.toJson(
                ErrorDef.DB_ERROR.name(),
                String.valueOf(se.getErrorCode()),
                se.getMessage()
            ));
        } catch (Exception e) {
            System.out.println(ErrorResponse.toJson(
                ErrorDef.SERVER_ERROR.name(),
                null,
                e.getMessage()
            ));
        } finally {
            if (conn != null) try { conn.close(); } catch (Exception ignore) {}
        }
    }
}
