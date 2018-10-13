CREATE TABLE IF NOT EXISTS project (
  id SERIAL PRIMARY KEY,
  name VARCHAR (255) NOT NULL UNIQUE,
  description TEXT,
  logo_path VARCHAR (512),
  project_link VARCHAR (1024),
  technologies TEXT,
  features TEXT
);

INSERT INTO project("name", "description", "logo_path", "project_link", "technologies", "features")
VALUES
 ('OrmNext', 'OrmNext - это легковесная, быстрая библиотека orm. Имеет мощный fluent api для выполнения запросов. Весит всего 300 Кб, что дает возможность использовать ее в мобильных приложениях. Легковесность достигается тем, что не используется ни одна сторонняя библотека. Написана на чистом Java. Ленивая инициализация для связей 1:1 1:M реализована через собственную проксю генерируемую собственным генератором прокси.', 'ormnext.png', 'https://github.com/saidgadjiev/ormnext', 'Java', 'Весит 330Кб. Есть поддержка связей 1:1, 1:M, M:1, M:M. Есть ленивая инициализация сущностей.'),
 ('OrmNextCache', 'OrmNextCache - это модуль кеширования для OrmNext. Написан на чистом Java.', NULL, 'https://github.com/saidgadjiev/ormnext-cache', 'Java', NULL),
 ('OrmNextSupport', 'OrmNextSupport - это модуль поддержки для OrmNext. В билиотеке присутствуют реализации под разные СУБД и прочие интересные полезные вещи. Написан на чистом Java.', NULL, 'https://github.com/saidgadjiev/ormnext-support', 'Java', NULL),
 ('ProxyMaker', 'ProxyMaker - это модуль для генерирования bytecode для прокси в OrmNext. Написан на чистом Java.', NULL, 'https://github.com/saidgadjiev/ormnext/tree/master/proxy-maker', 'Java', NULL),
 ('Сайт портфорлио', 'Frontend написан с использованием фреймворка AngularJs. Backend написан с использованием фреймворка Spring.', NULL, 'https://github.com/saidgadjiev/ormnext/tree/master/proxy-maker', 'Spring Security, Spring AOP, Spring WebSockets with STOMP, OrmNext, AngularJS1.7.2, Boostrap4.1.2.', 'Real time likes, real time comments.'),
 ('Prototype', 'Prototype - библиотка для создания рестов.(Пока реализован не весь функционал)', NULL, 'https://github.com/saidgadjiev/Prototype', 'Java, Netty', NULL),
 ('MP3Player', 'MP3Player с использованием JNA и библиотки BASS.', NULL, 'https://github.com/saidgadjiev/MP3Player', 'Java, JNA, BASS.', NULL),
 ('SpaceHockey', 'Серверная часть браузерной онлайн игры.', NULL, 'https://github.com/saidgadjiev/SpaceHockeyServer', 'Java', NULL),
 ('Реализация алгоритмов и структур данных', 'Алгоритмы и структуры данных.', NULL, 'https://github.com/saidgadjiev/Algorithms_and_Data_Structures', 'C++', NULL),
 ('Archiver', 'Написал архиватор с использованием алгоритма Хаффмана.', NULL, 'https://github.com/saidgadjiev/Archiver', 'C++', NULL),
 ('Pacman', 'Компьтерная игра Pacman.', NULL, 'https://github.com/saidgadjiev/Pacman', 'C++, OpenGL', NULL),
 ('Highload HttpServer', 'Высоконагруженный http server', NULL, 'https://github.com/saidgadjiev/HighloadHttpServer', 'C++, libevent.', NULL),
 ('Blog', 'Блог вопрос ответ.', NULL, 'https://github.com/saidgadjiev/Blog', 'Python, Django', NULL);