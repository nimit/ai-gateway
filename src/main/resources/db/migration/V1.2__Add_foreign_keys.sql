-- API Keys foreign keys
ALTER TABLE api_keys
    ADD CONSTRAINT fk_user
    FOREIGN KEY (user_id) REFERENCES users(id);

-- Provider API Keys foreign keys
ALTER TABLE provider_api_keys
    DROP CONSTRAINT IF EXISTS api_keys_user_id_fkey,
    ADD CONSTRAINT fk_api_keys_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE;

-- API Requests foreign keys
ALTER TABLE api_requests
    DROP CONSTRAINT IF EXISTS api_requests_user_id_fkey,
    ADD CONSTRAINT fk_api_requests_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE SET NULL;

ALTER TABLE api_requests
    DROP CONSTRAINT IF EXISTS api_requests_model_id_fkey,
    ADD CONSTRAINT fk_api_requests_model
    FOREIGN KEY (model_id)
    REFERENCES models(id)
    ON DELETE SET NULL;

-- Usage Quotas foreign keys
ALTER TABLE usage_quotas
    DROP CONSTRAINT IF EXISTS usage_quotas_user_id_fkey,
    ADD CONSTRAINT fk_usage_quotas_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE;

ALTER TABLE usage_quotas
    DROP CONSTRAINT IF EXISTS usage_quotas_model_id_fkey,
    ADD CONSTRAINT fk_usage_quotas_model
    FOREIGN KEY (model_id)
    REFERENCES models(id)
    ON DELETE CASCADE;
