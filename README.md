
단일 JSON 프로토콜 Client<br>
Web UI 요청에 따라 로직 수행<br>
Page/Op 단위로 동작함<br>

```mermaid
flowchart TD
    Client[CLI JSON 입력] --> Main[app.Main]
    Main --> ConnectionFactory
    ConnectionFactory -->|Connection| ExecContext
    ExecContext --> Router
    Router --> UserPage[ops.user.UserPage]
    Router --> OrderPage[ops.order.OrderPage]
    UserPage --> ParamValidator
    OrderPage --> ParamValidator
    ParamValidator -->|검증 실패| OpException
    OpException --> ErrorResponse
    ExecContext --> QueryExecutor
    QueryExecutor --> DB[(Oracle DB)]
```
