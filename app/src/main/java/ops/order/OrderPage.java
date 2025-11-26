package main.java.ops.order;

import core.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class OrderPage {
    private OrderPage() {}

    public static final Op LIST_BY_USER = (ctx, p) -> {
        ResultSet rs = null;
        try {
            ParamValidator.of(p)
                .require("userId")
                .ifError();

            int userId = p.getInt("userId");
            int limit  = p.optInt("limit", 20);

            rs = ctx.sql().select(
                "SELECT id, total, status, created_at " +
                    "FROM orders WHERE user_id=? ORDER BY id DESC FETCH FIRST ? ROWS ONLY",
                stmt -> {
                    stmt.setInt(1, userId);
                    stmt.setInt(2, limit);
                }
            );

            JSONArray arr = new JSONArray();
            while (rs.next()) {
                arr.put(new JSONObject()
                    .put("id", rs.getInt("id"))
                    .put("total", rs.getBigDecimal("total"))
                    .put("status", rs.getString("status"))
                    .put("createdAt", rs.getTimestamp("created_at"))
                );
            }

            return new JSONObject()
                .put("rows", arr.length())
                .put("data", arr);

        } catch (SQLException se) {
            throw new OpException(ErrorDef.ORDER_SELECT_FAILED, "ORDER_SELECT_FAILED", se);
        } finally {
            QueryExecutor.closeQuietly(rs);
        }
    };
}
