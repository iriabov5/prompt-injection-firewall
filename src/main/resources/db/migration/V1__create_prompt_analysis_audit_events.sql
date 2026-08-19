CREATE TABLE prompt_analysis_audit_events (
    id BIGSERIAL PRIMARY KEY,
    prompt_hash VARCHAR(71) NOT NULL,
    source VARCHAR(64),
    score INTEGER NOT NULL,
    risk VARCHAR(16) NOT NULL,
    decision VARCHAR(16) NOT NULL,
    reasons TEXT NOT NULL,
    latency_ms BIGINT NOT NULL,
    ai_used BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_prompt_analysis_audit_events_created_at
    ON prompt_analysis_audit_events (created_at DESC);

CREATE INDEX idx_prompt_analysis_audit_events_decision
    ON prompt_analysis_audit_events (decision);
