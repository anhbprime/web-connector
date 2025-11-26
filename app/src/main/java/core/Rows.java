package core;

import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

@FunctionalInterface
interface RowMapper<T> { T map(ResultSet rs) throws Exception; }

public final class Rows {
    private Rows() {}

    public static <T> java.util.List<T> readAll(ResultSet rs, RowMapper<T> fn) throws Exception {
        java.util.ArrayList<T> out = new java.util.ArrayList<T>();
        while (rs.next()) out.add(fn.map(rs));
        return out;
    }

    public static JSONArray toJson(ResultSet rs, RowMapper<JSONObject> fn) throws Exception {
        JSONArray arr = new JSONArray();
        while (rs.next()) arr.put(fn.map(rs));
        return arr;
    }
}
