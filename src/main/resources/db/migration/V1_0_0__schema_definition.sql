CREATE TABLE product (
	id CHAR(36) PRIMARY KEY,
  title VARCHAR(1024),
  description TEXT,
  brand VARCHAR(1024),
  price DECIMAL(15,2),
  currency CHAR(3),
  color VARCHAR(128)
);
