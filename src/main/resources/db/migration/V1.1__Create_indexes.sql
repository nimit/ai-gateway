-- API Requests performance indexes
CREATE INDEX idx_api_requests_user_id ON api_requests(user_id);
CREATE INDEX idx_api_requests_model_id ON api_requests(model_id);
CREATE INDEX idx_api_requests_model_user ON api_requests(model_id, user_id);
CREATE INDEX idx_api_requests_timestamp ON api_requests(request_timestamp);
CREATE INDEX idx_api_requests_metadata ON api_requests USING gin (metadata);

-- User lookup optimization
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_last_login ON users(last_login) WHERE is_active = true;

-- API key lookup
CREATE INDEX idx_api_keys_user_id ON api_keys(user_id);
CREATE INDEX idx_api_keys_provider ON api_keys(provider);