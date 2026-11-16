ALTER TABLE paef.paef_t_documento
    ADD COLUMN IF NOT EXISTS content_type VARCHAR(255);
