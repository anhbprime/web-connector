package core;

import org.json.JSONObject;

public final class ErrorResponse {
    private ErrorResponse() {}

    public static String toJson(String errorCode, String detailErrorCode, String msg) {
        JSONObject o = new JSONObject()
            .put("result", "error")
            .put("errorCode", errorCode)
            .put("msg", msg);
        if (detailErrorCode != null && !detailErrorCode.isEmpty()) {
            o.put("detailErrorCode", detailErrorCode);
        }
        return o.toString();
    }
}
