CREATE TABLE IF NOT EXISTS aboutme (
  id INTEGER PRIMARY KEY,
  fio VARCHAR (255) NOT NULL,
  date_of_birth TIMESTAMP NOT NULL,
  place_of_residence VARCHAR(255) NOT NULL,
  education_level VARCHAR (255) NOT NULL,
  post VARCHAR (255) NOT NULL,
  additional_information TEXT
);

INSERT INTO aboutme("id", "fio", "date_of_birth", "place_of_residence", "education_level", "post")
  VALUES (1, 'Гаджиев Саид Алиевич', '1995-07-01', 'Россия, Москва', 'Бакалавр', 'Java разработчик');
