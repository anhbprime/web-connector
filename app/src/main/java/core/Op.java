public apackage core;

import org.json.JSONObject;

@FunctionalInterface
public interface Op {
    JSONObject handle(ExecContext ctx, JSONObject params) throws Exception;
}
 {
    
}
