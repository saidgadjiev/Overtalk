CREATE TABLE IF NOT EXISTS comment (
  id SERIAL PRIMARY KEY,
  content TEXT NOT NULL,
  created_date TIMESTAMP NOT NULL,
  post_id INTEGER REFERENCES category (id),
  userprofile_username VARCHAR (255) NOT NULL REFERENCES userprofile(username)
);

