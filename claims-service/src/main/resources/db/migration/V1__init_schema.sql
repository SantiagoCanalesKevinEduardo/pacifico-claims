-- Crear tabla de clientes (customers)
CREATE TABLE customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20)
);

-- Crear tabla de pólizas (policies)
CREATE TABLE policies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    policy_number VARCHAR(50) NOT NULL UNIQUE,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    coverage_amount DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Crear tabla de siniestros (claims)
CREATE TABLE claims (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    policy_id BIGINT NOT NULL,
    claim_type VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (policy_id) REFERENCES policies(id)
);

-- Insertar algunos datos de prueba básicos para asociar pólizas
INSERT INTO customers (name, email, phone) VALUES 
('Juan Perez', 'juan.perez@pacifico.com.pe', '999888777'),
('Maria Rodriguez', 'maria.rodriguez@pacifico.com.pe', '999666555');

INSERT INTO policies (customer_id, policy_number, start_date, end_date, coverage_amount) VALUES 
(1, 'POL-100200', '2026-01-01 00:00:00', '2027-01-01 00:00:00', 50000.00),
(2, 'POL-300400', '2025-06-01 00:00:00', '2026-06-01 00:00:00', 100000.00);
