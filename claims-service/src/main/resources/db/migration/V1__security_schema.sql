-- Create users table
CREATE TABLE users (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP,
    updated_by VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

-- Create roles table
CREATE TABLE roles (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP,
    updated_by VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

-- Create user_roles table (Many-to-Many join table)
CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP,
    updated_by VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Insert roles with static UUIDs
INSERT INTO roles (id, name, created_by) VALUES 
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'ROLE_ANALYSIS', 'SYSTEM'), 
('cccccccc-cccc-cccc-cccc-cccccccccccc', 'ROLE_USER', 'SYSTEM');

-- Insert test users (Passwords: analyst123 and user123) with static UUIDs
INSERT INTO users (id, username, password, email, enabled, created_by) VALUES 
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'analyst', '$2a$10$asX8dEoGNSxm7RsdUqItOuHlXx/gODHdRvUTNbQ.QdtwQNUhR7qHG', 'analyst@pacifico.com.pe', TRUE, 'SYSTEM'),
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'user', '$2a$10$Xzg2/pb5YxkVPPmI2czP6up83adXfKI6lm0QZA7SO0knX7XG.lljO', 'user@pacifico.com.pe', TRUE, 'SYSTEM');

-- Map users to roles
INSERT INTO user_roles (user_id, role_id, created_by) VALUES 
('dddddddd-dddd-dddd-dddd-dddddddddddd', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'SYSTEM'), 
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'SYSTEM');
