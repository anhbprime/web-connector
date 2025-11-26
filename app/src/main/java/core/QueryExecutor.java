package core;

import java.sql.*;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OraclePreparedStatement;

public final class QueryExecutor {
    private final Connection conn;
    public QueryExecutor(Connection conn) { this.conn = conn; }

    @FunctionalInterface
    public interface Binder {
        void bind(OraclePreparedStatement stmt) throws Exception;
    }

    @FunctionalInterface
    public interface CSBinder {
        void bind(OracleCallableStatement stmt) throws Exception;
    }

    // SELECT: 호출자가 ResultSet/Statement 닫기
    public ResultSet select(String sql, Binder binder) throws Exception {
        OraclePreparedStatement stmt = (OraclePreparedStatement) conn.prepareStatement(sql);
        if (binder != null) binder.bind(stmt);
        return stmt.executeQuery();
    }

    // DML: 내부에서 Statement 닫음
    public int dml(String sql, Binder binder) throws Exception {
        OraclePreparedStatement stmt = null;
        try {
            stmt = (OraclePreparedStatement) conn.prepareStatement(sql);
            if (binder != null) binder.bind(stmt);
            return stmt.executeUpdate();
        } finally {
            if (stmt != null) try { stmt.close(); } catch (Exception ignore) {}
        }
    }

    // PROCEDURE/FUNCTION
    public OracleCallableStatement callProcedure(String callSql, CSBinder binder) throws Exception {
        OracleCallableStatement stmt = (OracleCallableStatement) conn.prepareCall(callSql);
        if (binder != null) binder.bind(stmt);
        stmt.execute();
        return stmt;
    }

    // ResultSet 안전 종료
    public static void closeQuietly(ResultSet rs) {
        if (rs != null) {
            Statement st = null;
            try { st = rs.getStatement(); } catch (Exception ignore) {}
            try { rs.close(); } catch (Exception ignore) {}
            if (st != null) try { st.close(); } catch (Exception ignore) {}
        }
    }
}
