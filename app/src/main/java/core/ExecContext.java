package core;

import java.sql.Connection;

public final class ExecContext {
    private final Connection conn;
    private final QueryExecutor executor;

    public ExecContext(Connection conn) {
        this.conn = conn;
        this.executor = new QueryExecutor(conn);
    }

    public Connection connection() { return conn; }
    public QueryExecutor sql() { return executor; }
}
