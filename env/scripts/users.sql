CREATE TABLE IF NOT EXISTS userprofile (
  id SERIAL PRIMARY KEY,
  username VARCHAR (255) NOT NULL UNIQUE,
  nickname VARCHAR (255) NOT NULL UNIQUE ,
  password VARCHAR (255) NOT NULL
);

CREATE TABLE IF NOT EXISTS userprofile_role (
  id SERIAL PRIMARY KEY,
  userprofile_username VARCHAR (255) NOT NULL REFERENCES userprofile(username),
  role_name VARCHAR (255) NOT NULL REFERENCES role(name),
  UNIQUE (userprofile_username, role_name)
);

INSERT INTO userprofile("username", "nickname", "password") VALUES('admin', 'admin', '$2a$10$MItPOf2gYp7D5MOTw.Jl7O8.NTOtxvpQiR65apQ04QRonrMjQdTKe');
INSERT INTO userprofile_role("userprofile_username", "role_name") VALUES('admin', 'ROLE_ADMIN');

INSERT INTO userprofile("username", "nickname", "password") VALUES('test', 'test', '$2a$10$mXyMQeOzYCyBWdCwi42NGeVegfdl2H7uBIXRSFMkG02t5FEeGo8X6');
INSERT INTO userprofile_role("userprofile_username", "role_name") VALUES('test', 'ROLE_USER');

