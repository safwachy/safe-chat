CREATE ROLE dev_rw WITH LOGIN PASSWORD 'dev_database_passwd';

GRANT ALL PRIVILEGES ON DATABASE postgres TO dev_rw;