# wpsite
1. Создаем websites instants с ubuntu на AWS (EC2) в одной сети :
   * В первом instance cоздаем и сохраняем ключь *.pem
   * Во втром instance выбираем созданный ранее ключ *.pem
2. Создаем jenkins instant с ubuntu на AWS (EC2) :
   * Выбираем созданный ранее ключ *.pem
3. Уставливаем jenkins - https://www.jenkins.io/doc/book/installing/linux/ :
   * Обновляем пакеты - sudo apt-get update
   * Устанавливаем JAVA - sudo apt install openjdk-11-jre
   * Устанавливаем jenkins - команды по ссылке выше
   * Провереяем запуск jenkins - sudo service jenkins status
   * Открываем порт 8080: Security Groups > Edit inbound rules > Add rule = Custom TPC && Port range 8080
4. Настраиваем связь jenkins c websites :
   * Меняем права ключа *.pem - sudo chmod 600 *pem
   * Отправляем на jenkins instance -  scp -i ./*.pem ./*.pem user@ipaddres:/tmp/
   * Меняем владельца ключа *.pem - sudo chown user: ./*.pem


5. Создаем jenkins Pipeline подготавливающий окружающию среду для Deploy websites на удаленых instances:

   6. Создаем mariaDB instant с ubuntu на AWS (EC2) в одной сети :
   Открываем порт 3306: Security Groups > Edit inbound rules > Add rule = Custom TPC && Subnet ID - IPv4 CIDR && Port range 3306

Добовляем jenkins authorized_keys в GIT HUB
Install git into jenkins instance
nc -vz 172.31.34.176 3306

W1   172.31.35.51 
W2   172.31.41.1
MDB  172.31.34.176




scp -i ./key.pem ubuntu@52.47.126.105:/tmp/

sudo chown jenkins: ./jenkins.pem
меняет владельца: default (группу) файл


Linux базовые команды