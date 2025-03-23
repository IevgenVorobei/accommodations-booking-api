CREATE TABLE IF NOT EXISTS units (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    num_of_rooms INT,
    floor INT,
    accommodation_type VARCHAR(255),
    price_per_night NUMERIC,
    description TEXT
);

CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    unit_id UUID,
    payment_id UUID,
    check_in_date DATE,
    check_out_date DATE,
    order_status VARCHAR(255),
    total_price  NUMERIC,
    cancellation_reason VARCHAR(255),
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    status VARCHAR(255),
    amount  NUMERIC,
    currency_code VARCHAR(255),
    created_at TIMESTAMP
);
