CREATE TABLE IF NOT EXISTS post (
  id SERIAL PRIMARY KEY,
  title VARCHAR (255) NOT NULL,
  content TEXT NOT NULL,
  created_date TIMESTAMP NOT NULL,
  category_id INTEGER REFERENCES category (id)
);

