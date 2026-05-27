-- Create customers table
CREATE TABLE customers (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP,
    updated_by VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

-- Create policies table
CREATE TABLE policies (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    customer_id UUID NOT NULL,
    policy_number VARCHAR(50) NOT NULL UNIQUE,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    coverage_amount DECIMAL(15, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP,
    updated_by VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Create claims table
CREATE TABLE claims (
    id UUID DEFAULT RANDOM_UUID() PRIMARY KEY,
    customer_id UUID NOT NULL,
    policy_id UUID NOT NULL,
    claim_type VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    claim_status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    updated_at TIMESTAMP,
    updated_by VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (policy_id) REFERENCES policies(id)
);

-- Insert test customers and policies with static UUIDs
INSERT INTO customers (id, name, email, phone, created_by) VALUES 
('a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d', 'Juan Perez', 'juan.perez@pacifico.com.pe', '999888777', 'SYSTEM'),
('f6e5d4c3-b2a1-0f9e-8d7c-6b5a4f3e2d1c', 'Maria Rodriguez', 'maria.rodriguez@pacifico.com.pe', '999666555', 'SYSTEM');

INSERT INTO policies (id, customer_id, policy_number, start_date, end_date, coverage_amount, created_by) VALUES 
('11111111-2222-3333-4444-555555555555', 'a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d', 'POL-100200', '2026-01-01 00:00:00', '2027-01-01 00:00:00', 50000.00, 'SYSTEM'),
('66666666-7777-8888-9999-000000000000', 'f6e5d4c3-b2a1-0f9e-8d7c-6b5a4f3e2d1c', 'POL-300400', '2025-06-01 00:00:00', '2026-06-01 00:00:00', 100000.00, 'SYSTEM');

INSERT INTO claims (id, customer_id, policy_id, claim_type, description, amount, claim_status, created_by) VALUES
('99999999-9999-9999-9999-999999999999', 'a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d', '11111111-2222-3333-4444-555555555555', 'HEALTH', 'Consulta médica general de prueba', 150.00, 'PENDING', 'SYSTEM');
