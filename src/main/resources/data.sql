INSERT INTO brands (name) VALUES
  ('A'), ('B'), ('C'), ('D'), ('E'), ('F'), ('G'), ('H'), ('I');

-- Insert products for brand A
INSERT INTO products (brand_id, category, price) VALUES
                                                     (1, 'TOP', 11200),
                                                     (1, 'OUTER', 5500),
                                                     (1, 'PANTS', 4200),
                                                     (1, 'SNEAKERS', 9000),
                                                     (1, 'BAG', 2000),
                                                     (1, 'CAP', 1700),
                                                     (1, 'SOCKS', 1800),
                                                     (1, 'ACCESSORY', 2300);

-- Insert products for brand B
INSERT INTO products (brand_id, category, price) VALUES
                                                     (2, 'TOP', 10500),
                                                     (2, 'OUTER', 5900),
                                                     (2, 'PANTS', 3800),
                                                     (2, 'SNEAKERS', 9100),
                                                     (2, 'BAG', 2100),
                                                     (2, 'CAP', 2000),
                                                     (2, 'SOCKS', 2000),
                                                     (2, 'ACCESSORY', 2200);