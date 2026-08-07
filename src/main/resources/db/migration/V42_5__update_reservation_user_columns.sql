DO $$ 
BEGIN 
    IF EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='reservation' AND column_name='user_id') 
       AND NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='reservation' AND column_name='client_id') THEN
        ALTER TABLE reservation RENAME COLUMN user_id TO client_id;
    END IF;
END $$;

ALTER TABLE reservation ADD COLUMN IF NOT EXISTS client_id UUID REFERENCES "app_user"(id);

ALTER TABLE reservation ADD COLUMN IF NOT EXISTS employee_id UUID REFERENCES "app_user"(id);

ALTER TABLE reservation DROP COLUMN IF EXISTS user_id;