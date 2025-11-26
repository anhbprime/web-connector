package core;

import org.json.JSONObject;

public final class Router {
    public JSONObject handle(String page, String op, ExecContext ctx, JSONObject params) throws Exception {
        switch (page) {
            case "user_mgmt":
                switch (op) {
                    case "getByEmail":
                        return ops.user.UserPage.GET_BY_EMAIL.handle(ctx, params);
                    case "ordersByEmail":
                        return ops.user.UserPage.ORDERS_BY_EMAIL.handle(ctx, params);
                }
                // op unknown
                throw new OpException(
                    ErrorDef.UNKNOWN_OPERATION,
                    null,
                    "unknown op: " + page + "." + op
                );

            case "order_mgmt":
                switch (op) {
                    case "listByUser":
                        return ops.order.OrderPage.LIST_BY_USER.handle(ctx, params);
                }
                throw new OpException(
                    ErrorDef.UNKNOWN_OPERATION,
                    null,
                    "unknown op: " + page + "." + op
                );

            default:
                throw new OpException(
                    ErrorDef.UNKNOWN_PAGE,
                    null,
                    "unknown page: " + page
                );
        }
    }
}
