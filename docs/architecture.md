# Project Architecture

```mermaid
flowchart TD
    U[User] --> M[Main]
    M --> UI[Menu]
    UI --> B[Book Management]
    UI --> S[Student Management]
    UI --> A[Admin Dashboard]
    UI --> I[Issue / Return]
    UI --> F[Fine Management]
    UI --> R[Reports]

    B --> BDAO[BookDAO]
    S --> SDAO[StudentDAO]
    A --> ADAO[AdminDAO]
    I --> IDAO[IssueBookDAO]
    F --> FDAO[FineDAO]
    R --> RDAO[ReportDAO]

    BDAO --> DB[(MySQL)]
    SDAO --> DB
    ADAO --> DB
    IDAO --> DB
    FDAO --> DB
    RDAO --> DB

    DB --> T1[books]
    DB --> T2[students]
    DB --> T3[admin]
    DB --> T4[issue_books]
    DB --> T5[fine]
```

## Notes
- `Menu` is the console entry point for all workflows.
- DAO classes isolate SQL from the UI and business flow.
- `DBConnection` centralizes MySQL access.
- `Validation` and `LoggerUtil` provide cross-cutting support.