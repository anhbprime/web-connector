package core;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

public final class ParamValidator {

    private final JSONObject root;
    private JSONObject target;
    private final List<String> errors;

    private ErrorDef def = ErrorDef.BAD_REQUEST;

    private ParamValidator(JSONObject target, List<String> shared) {
        this.root = (target != null ? target : new JSONObject());
        this.target = this.root;
        this.errors = (shared != null ? shared : new ArrayList<String>());
    }

    public static ParamValidator of(JSONObject root) {
        return new ParamValidator(root, null);
    }

    public ParamValidator on(ErrorDef def) {
        this.def = def;
        return this;
    }

    public void addError(String msg) {
        errors.add(msg);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void ifError() {
        if (!errors.isEmpty()) {
            throw new OpException(def, null, String.join(", ", errors));
        }
    }

    public ParamValidator into(String... keys) {
        JSONObject obj = root;
        for (String key : keys) {
            if (obj == null) break;
            JSONObject child = obj.optJSONObject(key);
            if (child == null) {
                errors.add("missing object: " + key);
                target = null;
                return this;
            }
            obj = child;
        }
        this.target = obj;
        return this;
    }

    public ParamValidator out() {
        this.target = this.root;
        return this;
    }

    private boolean skip() {
        return target == null;
    }

    private String norm(String key) {
        if (skip()) return null;
        if (!target.has(key) || target.isNull(key)) return null;
        String v = target.optString(key, "").trim();
        return v.isEmpty() ? null : v;
    }

    public ParamValidator require(String key) {
        if (norm(key) == null) {
            errors.add("missing or empty: " + key);
        }
        return this;
    }

    public ParamValidator requireKeys(String... keys) {
        if (skip()) return this;
        for (String k : keys) {
            if (!target.has(k)) errors.add("missing key: " + k);
        }
        return this;
    }

    public ParamValidator regex(String key, Pattern p) {
        String v = norm(key);
        if (v == null) return this;
        if (!p.matcher(v).matches()) {
            errors.add("invalid format: " + key);
        }
        return this;
    }

    public ParamValidator intBetween(String key, int min, int max) {
        String v = norm(key); if (v == null) return this;
        try {
            int i = Integer.parseInt(v);
            if (i < min || i > max) {
                errors.add("out of range: " + key);
            }
        } catch (Exception e) {
            errors.add("invalid int: " + key);
        }
        return this;
    }

    public ParamValidator utcInstant(String key) {
        String v = norm(key); if (v == null) return this;
        try {
            Instant.parse(v);
        } catch (DateTimeParseException e) {
            errors.add("invalid utc: " + key);
        }
        return this;
    }

    public ParamValidator eachVal(String arrayKey, String elementKey,
                                  java.util.function.Consumer<ParamValidator> fn) {
        if (target == null) {
            errors.add("no target for array: " + arrayKey);
            return this;
        }

        JSONArray arr = target.optJSONArray(arrayKey);
        if (arr == null) {
            errors.add("not array: " + arrayKey);
            return this;
        }

        JSONObject originalTarget = this.target;

        for (int i = 0; i < arr.length(); i++) {
            Object raw = arr.isNull(i) ? null : arr.opt(i);
            JSONObject elem = new JSONObject();
            elem.put(elementKey, raw);
            this.target = elem;
            fn.accept(this);
        }

        this.target = originalTarget;
        return this;
    }

    public ParamValidator eachObj(String arrayKey,
                                  java.util.function.Consumer<ParamValidator> fn) {
        if (target == null) {
            errors.add("no target for array: " + arrayKey);
            return this;
        }

        JSONArray arr = target.optJSONArray(arrayKey);
        if (arr == null) {
            errors.add("not array: " + arrayKey);
            return this;
        }

        JSONObject originalTarget = this.target;

        for (int i = 0; i < arr.length(); i++) {
            JSONObject elem = arr.optJSONObject(i);
            if (elem == null) {
                errors.add("array element not object: " + arrayKey + "[" + i + "]");
                continue;
            }
            this.target = elem;
            fn.accept(this);
        }

        this.target = originalTarget;
        return this;
    }

    public JSONObject raw() {
        return target;
    }
}
