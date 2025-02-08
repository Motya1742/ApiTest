  Инструкция по запуску проекта 
  https://github.com/Motya1742/ApiTest.git

  Установка и запуск
  
1. Клонировать репозиторий с GitHub:
   
git clone https://github.com/Motya1742/ApiTest.git

2. Установить IntelliJ IDEA с официального сайта GetBrains.
   
3. Скачать и установить Maven с официального сайта. Добавить в переменные среды.
   
4. Открыть проект в IntelliJ IDEA:
   
Выбрать "Open" и указать путь к склонированному репозиторию.

Дождаться загрузки зависимостей.

5. Настроить и запустить каждый микросервис в следующей последовательности:
   
Убедитесь, что в настройках проекта выбран JDK 21.

Запустить eurekaServer (порт 8761).

Запустить authenticationService (порт 8083).

Запустить bookStorageService (порт 8081).

Запустить bookTrackerService (порт 8082).

Запустить mainGateway (порт 8080).

Ожидать развертывание всех сервисов.

Проверить регистрацию сервисов на Eureka Server: http://localhost:8761

6. Для тестирования API использовать Postman, приложенный к проекту или Swagger.

Swagger UI для каждого сервиса:

bookStorageService: http://localhost:8081/swagger-ui/index.html#/

bookTrackerService: http://localhost:8082/swagger-ui/index.html#/

authenticationService: http://localhost:8083/swagger-ui/index.html#/

7. Описание основных запросов API:
   
7.1 bookStorageService
   
Get All Books – GET /api/book/all – Получение списка всех книг (без авторизации).

Get Book By Id – GET /api/book/id/{id} – Получение книги по ID (без авторизации).

Get Book By ISBN – GET /api/book/isbn/{isbn} – Получение книги по ISBN (без авторизации).

Add New Book – POST /api/book – Добавление новой книги (требуется авторизация).

Update Book – PUT /api/book/{id} – Обновление книги по ID (требуется авторизация).

Delete Book By Id – DELETE /api/book/{id} – Удаление книги по ID (требуется авторизация).

7.2 bookTrackerService

Get Available Books – GET /api/tracking/available – Получение списка доступных книг (без авторизации).

Add Tracking Book – POST /api/tracking/add/{id} – Добавление книги в отслеживание (требуется авторизация).

Update Tracking Book – PUT /api/tracking/update/{id} – Обновление информации об отслеживаемой книге (требуется авторизация).

Delete Tracking Book – DELETE /api/tracking/{id} – Удаление отслеживаемой книги (требуется авторизация).

7.3 authenticationService

Authenticate – POST /api/auth/login – Авторизация пользователя и получение JWT-токена (без авторизации).

Registration – POST /api/auth/register – Регистрация нового пользователя (без авторизации).
