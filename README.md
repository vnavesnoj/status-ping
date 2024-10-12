# Status ping service

Приложение преднозначено для передачи информации другим пользователям о статусе соединений других пользователей.
Соединения с приложением осуществляется по WebSocket протоколу.

## Подготовка к работе:

* Для запуска приложения нужна установленная на машине Java 17+.
* Команда для запуска приложения: `/gradlew bootRun ./gradlew bootRun --args='--arg1=value --arg2=value'`

## Список основных аргументов:

* spring.datasource.url
* spring.datasource.username
* spring.datasource.password
* app.sql.findAllByNickname.path - путь основного sql скрипта
* app.sql.findAllByNickname.parameter - название параметра, использумого в sql скрипте
* app.websocket.endpoints - путь, по которому возможно подключение к приложению
* app.websocket.allowed-origins - источники, для которых разрешены cross-origin запросы из браузера.

Больше параметров можно найти в `status-ping-configuration/src/main/resources`

## Механизм взаимодействия

На эндпоинт приложения отправляется ws запрос на взаимодействие по
протоколу WebSocket. После WebSocket handshake и установленного подключения
клиент посылает JSON сообщение вида:

`{
"principal": "principal",
"status": "ONLINE"
}`

Приложение сохраняет WebSocket сессию и его principal (владелец, никнейм, id).
Затем посылает другим соединениям информацию об указаном статусе нового пользователя
(ONLINE, OFFLINE). В случае закрытия ws соединения, другим
подключениям придет сообщение о статусе OFFLINE
пользователя, соединение с которым было закрыто.

## Responses

Ответы пользователю присылаются в фомате JSON.
Возможные шаблоны ответов от приложения:

    Integer statusCode: 100;

    String user;

    String status;

    Instant instant;

___

    Integer statusCode: 200;

    String id;

    String principal;

    Instant instant;

___

    Integer statusCode: 4xx-5xx;

    String title;

    String detail;

    Instant instant;
