CREATE TABLE IF NOT EXISTS skill (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  aboutme_id INTEGER REFERENCES aboutme(id)
);

INSERT INTO skill("name", "aboutme_id")
VALUES
 ('Базы данных: MySQL, PosgreSQL, SQLite', 1),
 ('Системы контроля версий: Git', 1),
 ('Системы непрерывной интеграции: Hudson, Jenkins', 1),
 ('Системы отслеживания ошибок и задач: Jira', 1),
 ('DI Frameworks: Google Guice, Dagger2, Spring IoC', 1),
 ('Статический анализ кода: PMD, CheckStyle, FindBugs', 1),
 ('Работа с XML: jaxb', 1),
 ('Работа с сетью: сокеты Беркли', 1),
 ('Протоколы: HTTP, HTTPS, FTP, TCP/IP, UDP', 1),
 ('Контейнеры сервлетов: Tomcat, Jetty', 1),
 ('Библиотеки ОРМ: Hibernate, OrmLite, OrmNext', 1),
 ('Мобильная разработка: Android', 1),
 ('Языки программирования: Java, C/C++, JavaScript, Python', 1),
 ('Backend разработка: Vaadin, Spring Framework, Spring Security, Django, Netty, Prototype', 1),
 ('Frontend разработка: AngularJS 1.x, jQuery, Bootstrap', 1),
 ('Веб-сервисы: JAX-WS', 1),
 ('ООП: Design Patterns, SOLID Principles', 1),
 ('ОС: Linux, Mac OS, Windows', 1);