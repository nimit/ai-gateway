CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN DEFAULT true,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS api_keys (
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    provider VARCHAR(50)
);

-- All supported models will have the details here (configuration will be the default configuration of the model that can be changed per request if needed)
CREATE TABLE IF NOT EXISTS models (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    provider VARCHAR(50) NOT NULL,
    input_type VARCHAR(20) NOT NULL,
    api_endpoint VARCHAR(255) NOT NULL,
    model_version VARCHAR(50),
    max_input_tokens INTEGER,
    max_output_tokens INTEGER,
    supported_features JSONB,
    configuration JSONB,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(provider, name)
);

CREATE TABLE IF NOT EXISTS api_requests (
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    model_id INTEGER,
    request_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completion_timestamp TIMESTAMP,
    status_code INTEGER,
    request_body JSONB,
    response_body JSONB,
    metadata JSONB,
    latency INTEGER,
    error_message TEXT
);

CREATE TABLE IF NOT EXISTS usage_quotas (
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    model_id INTEGER,
    monthly_token_limit INTEGER,
    daily_request_limit INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, model_id)
);