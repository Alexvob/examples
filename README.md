Клиент-серверное приложение, данные для вывода в интерфейс пользователя берутся из локальной базы данных, 
локальные данные в фоновых потоках синхронизируются с базой данных сервера. Синхронизация всех данных происходит при запуске приложения 
(первый запрос по https для проверки наличия изменений, если изменения есть, вторым запросом идет получение данных которые нужно обновить).
При обновлении данных на сервере по запросу одного клиента, со стороны сервера формируется push-уведомление другим клиентам в группе, 
по которому также синхронизируются данные. 
Также синхронизация необходимых данных в конкретном разделе происходит при переходах по разделам приложения, с задержкой, исключающей частые запросы. 
При возникновении событий касающихся конкретного пользователя (новая входящая задача) формируется всплывающее уведомление в панели уведомлений Андроид.
Компонент LiveData обеспечивает обновление данных пользовательского интерфейса при изменении локальных данных сервисом синхронизации. 
В связи с тем что возможна выгрузка приложения из памяти системой Андроид, есть включаемый режим работы приложения в режиме переднего плана, для повышения приоритета 
процесса, и формирования уведомлений. 
При создании экземпляра приложения, в singleton-объекте Application идет:
- получение данных о устройстве, 
- инициализация экземпляра RoomDatabase для работы с локальной БД,
- инициализация объектов для работы с сетью (Retrofit2), 
- сервисов синхронизации, 
- проверка наличия и получение данных приложения (данные о группе и пользователе), необходимых в контексте приложения, 
- подключение к Google-сервису для получения токена устройства, подписка на push-уведомления группы,
- 

Активти презентер репозитори.
crash logging
