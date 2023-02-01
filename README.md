# INCEPTION IN AWS

AWS
1. Создаем jenkins instant с ubuntu на AWS (EC2) :
   * Выбираем созданный ранее ключ *.pem
2. Уставливаем jenkins - https://www.jenkins.io/doc/book/installing/linux/ :
   * Обновляем пакеты - sudo apt-get update
   * Устанавливаем JAVA - sudo apt install openjdk-11-jre
   * Устанавливаем jenkins - команды по ссылке выше
   * Провереяем запуск jenkins - sudo service jenkins status
   * Устанавливает GIT - sudo apt install git-all
   * Открываем порт 8080: Security Groups > Edit inbound rules > Add rule = Custom TPC && Port range 8080
3. Создаем websites instants с ubuntu на AWS (EC2) в одной сети :
   * В первом instance cоздаем и сохраняем ключь *.pem
   * Во втром instance выбираем созданный ранее ключ *.pem
   * Создаем подсеть VPC ***.**.0.0/16 SUBNET IPv4 > CIDRs ***.**.150.0/24
   * Открываем порты 443
4. Создаем mariaDB instant с ubuntu на AWS (EC2) в одной сети :
   * Открываем порт 3306 для подсети: Security Groups > Edit inbound rules > Add rule = Subnet ID - IPv4 CIDR 
5. Настраиваем связь jenkins c instances и GIT :
   * Меняем права ключа *.pem - sudo chmod 600 *pem
   * Отправляем на jenkins instance -  scp -i ./*.pem ./*.pem user@ipaddres:/tmp/
   * Меняем владельца ключа *.pem - sudo chown user: ./*.pem
   * Настраиваем соеедниение с GIT

Jenkins pipeline
1. create_an_environment.groovy - pipeline подготавливающий окружающию среду для Deploy APP
2. build_and_deploy.groovy - pipeline собирающий и разворачивающий Mariadb, Nginx, Wordpress, myPhpAdmin




