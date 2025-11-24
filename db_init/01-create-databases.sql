-- CREATE DATABASE dev_gateway_db;
-- CREATE DATABASE dev_auth_db;
-- CREATE DATABASE dev_event_manager_db;
-- CREATE DATABASE dev_event_notificator_db;
SELECT 'CREATE DATABASE dev_gateway_db'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'dev_gateway_db')\gexec


SELECT 'CREATE DATABASE dev_auth_db'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'dev_auth_db')\gexec


SELECT 'CREATE DATABASE dev_event_manager_db'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'dev_event_manager_db')\gexec


SELECT 'CREATE DATABASE dev_event_notificator_db'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'dev_event_notificator_db')\gexec


SELECT 'CREATE DATABASE dev_profiles_db'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'dev_profiles_db')\gexec