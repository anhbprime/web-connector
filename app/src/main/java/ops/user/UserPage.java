package ops.user;

import core.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class UserPage {
    private UserPage() {}

    public static final Op GET_BY_EMAIL = (ctx, p) -> {
        ResultSet rs = null;
        try {
            ParamValidator.of(p)
                .require("email")
                .into("detail")
                    .isNull("data")
                .ifError();

            rs = ctx.sql().select(
                "SELECT id, name, email, status FROM users WHERE email=:email",
                stmt -> stmt.setStringAtName("email", p.getString("email"))
            );

            JSONArray data = new JSONArray();
            while (rs.next()) {
                data.put(new JSONObject()
                    .put("id", rs.getInt("id"))
                    .put("name", rs.getString("name"))
                    .put("email", rs.getString("email"))
                    .put("status", rs.getString("status"))
                );
            }

            if (data.length() == 0) {
                throw new OpException(
                    ErrorDef.USER_NOT_FOUND,
                    null,
                    "no user for email"
                );
            }

            return new JSONObject()
                .put("rows", data.length())
                .put("data", data);

        } catch (SQLException se) {
            throw new OpException(ErrorDef.USER_SELECT_FAILED, "USER_SELECT_FAILED", se);
        } finally {
            QueryExecutor.closeQuietly(rs);
        }
    };

    public static final Op ORDERS_BY_EMAIL = (ctx, p) -> {
        JSONObject user = GET_BY_EMAIL.handle(ctx, p);
        int userId = user.getJSONArray("data").getJSONObject(0).getInt("id");
        int limit  = p.optInt("limit", 5);

        JSONObject orders = ops.order.OrderPage.LIST_BY_USER.handle(
            ctx,
            new JSONObject().put("userId", userId).put("limit", limit)
        );

        return new JSONObject()
            .put("found", true)
            .put("userId", userId)
            .put("rows", orders.getInt("rows"))
            .put("orders", orders.getJSONArray("data"));
    };
}
