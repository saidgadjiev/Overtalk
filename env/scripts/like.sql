CREATE TABLE IF NOT EXISTS "like" (
  id SERIAL PRIMARY KEY,
  post_id INTEGER,
  comment_id INTEGER,
  userprofile_username VARCHAR (255) NOT NULL REFERENCES userprofile(username)
);