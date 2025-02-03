-- Создание таблица пользователей
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(50) UNIQUE
);

-- Создание таблицы ролей
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Промежуточная таблица для связи пользователей и ролей (многие-ко-многим)
CREATE TABLE users_roles (
    user_id BIGINT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

-- Создание таблицы тем (топиков)
CREATE TABLE topics (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Создание таблица комментариев
CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    topic_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (topic_id) REFERENCES topics (id) ON DELETE CASCADE
);

-- Создание таблицы оценок (лайки/дизлайки)
CREATE TABLE likes (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    entity_id INT NOT NULL, -- ID сущности (тема или комментарий)
    entity_type VARCHAR(10) CHECK (entity_type IN ('TOPIC', 'COMMENT')) NOT NULL,
    is_like BOOLEAN NOT NULL, -- TRUE = лайк, FALSE = дизлайк
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (user_id, entity_id, entity_type),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Создание таблицы уведомлений
CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Заполнение таблицы ролей
INSERT INTO roles (name) VALUES
('ROLE_USER'),
('ROLE_ADMIN');

-- Добавление пользователей (пароль "password" захеширован через BCrypt)
INSERT INTO users (username, password, email) VALUES
('user',  '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'user@gmail.com'),
('admin', '$2a$04$Fx/SX9.BAvtPlMyIIqqFx.hLY2Xp8nnhpzvEEVINvVpwIPbA3v/.i', 'admin@gmail.com');

-- Назначение ролей пользователям
INSERT INTO users_roles (user_id, role_id) VALUES
(1, 1),  -- user -> ROLE_USER
(2, 2);  -- admin -> ROLE_ADMIN

-- Добавление тестовых тем
INSERT INTO topics (title, content, user_id) VALUES
('Первая тема', 'Это первый пост на форуме!', 1),
('Вторая тема', 'Обсуждение Spring Boot.', 2);

-- Добавление тестовых комментариев
INSERT INTO comments (content, user_id, topic_id) VALUES
('Отличный пост!', 2, 1),
('Spring Boot – мощный инструмент!', 1, 2);

-- Добавление тестовых лайков
INSERT INTO likes (user_id, entity_id, entity_type, is_like) VALUES
(1, 1, 'TOPIC', TRUE),
(2, 2, 'TOPIC', TRUE);

-- Добавление тестовых уведомлений
INSERT INTO notifications (user_id, message) VALUES
(1, 'У вас новый комментарий!'),
(2, 'Ваша тема получила лайк!');