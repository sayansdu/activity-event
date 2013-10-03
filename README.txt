Скачать из репозитории последную версия bnd-event
Убедится, что у вас существует базе данных bnd_event и bnd_event2


1) Заходим в корневую директорию и делаем maven clean package or maven clean install:
	 bnd-event:
	 	---	bnd-event-adapter
	 	---	bnd-event-dal
	 	---	bnd-event-service
	 	---	bnd-event-view
	 	---	bnd-event-war
	 	--- pom.xml
	 	
	 1.1) Точнее заходим в bnd-event, она есть корневая директория
	 1.2) В bnd-event открываем терминал	
	 1.3) В терминале должно быть указано корневая директория bnd-event
	 1.4) В терминале пишем mvn install
	 	1.4.1) Чтобы mvn install выполнилось без ошибок, у вас в локальной репозиторий должен быть скачен bnd-core(version 1.0), bnd-eas-adapter(version 1.0.4) 
	 	
	 После в директории  bnd-event/bnd-event-war/target должен создатся event.war
	 
2) Теперь нам нужно за деплойд его:
	Если у вас установлен jboss-as server:
		1) Скопировать event.war в jboss-as-7.1.1.Final/standalone/deployments/
		2) Откройте терминал в jboss-as-7.1.1.Final/bin/
		3) И наберите ./standalone 
	
		Или запустите в eclipse
	
	Если нет, то в проекте по умолчанию идеть jetty server:
		1) После успешной команды mvn install
		2) Наберите в терминале cd bnd-event-war и попадаем в директорию /bnd-event/bnd-event-war/ 
		2) В терминале набираем mvn jetty:run  
	

В директорий /bnd-event/bnd-event-war/src/main/resources/spring/ нужно ввести правильные данные в файл config.properties
	pg_dump=Путь к PogstgreSQL файлу pg_dump (обычно он находится в postgresql/9.2/bin/)
	pg_restore=Путь к PogstgreSQL файлу pg_restore (обычно он находится в postgresql/9.2/bin/)
	db.username=PostgreSQL username
	db.password=PostgreSQL password
	db.url=PostgreSQL URL (example jdbc:postgresql://127.0.0.1:5432/bnd_event)
	hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
	entity.package.scanner=kz.jazzsoft.bnd.event
	liquibase.root=kz/jazzsoft/bnd/event/db/root.xml
	mail.host=krg-mail.nat.kz

PostgreSQL pgpass файл (с помощью этого файла вы можете заходить в PostgreSQL без пороли. Он хранить в себе данные о пользователе IP:PORT:DATABASE:POSTGRESQL-USERNAME:POSTGRESQL-PASSWORD):
	Если у вас Linux/Mac, то создайте файл .pgpass в папке /Users/username/. После напишите в нем информация в формате IP:PORT:DATABASE:POSTGRESQL-USERNAME:POSTGRESQL-PASSWORD (пример 127.0.0.1:5432:*:postgres:mypassword)

	Если у вас Windows, то создайте файл pgpass.conf в папке C:/Users/postgres/AppData/postgresql/. После напишите в нем информация в формате IP:PORT:DATABASE:POSTGRESQL-USERNAME:POSTGRESQL-PASSWORD (пример 127.0.0.1:5432:*:postgres:mypassword)

А на проверку прав пользователя пока я ввел данные статично.