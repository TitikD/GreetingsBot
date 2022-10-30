CREATE TABLE users(
                     id SERIAL PRIMARY KEY,
                     telegram_user_id bigint UNIQUE NOT NULL,
                     state TEXT,
                     role TEXT,
                     last_message_id INTEGER
);
CREATE TABLE messages(
                         id SERIAL PRIMARY KEY,
                         telegram_user_id bigint NOT NULL,
                         text TEXT,
                         photo TEXT,
                         type TEXT
);
CREATE TABLE members(
                         id SERIAL PRIMARY KEY,
                         telegram_user_id bigint NOT NULL,
                         chat_id bigint
);