INSERT IGNORE INTO categories (id, name) VALUES
  (1, 'Électronique'),
  (2, 'Livres');

INSERT IGNORE INTO products (id, name, price, category_id) VALUES
  (1, 'Smartphone', 699.99, 1),
  (2, 'Laptop', 1199.00, 1),
  (3, 'Roman', 19.90, 2);
