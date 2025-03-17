# INCEPTION IN AWS

![INCEPTION TO AWS](https://user-images.githubusercontent.com/78852244/216152568-0bd33ef1-7f8d-4b8a-99f4-f0e86cb24c03.jpg)


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
5. Создаем AWS S3 BUCKET.
6. Создаем IAM:
   * Создаем User
   * Создаем Access key and Secret access key
   * Создаем Policies > Create > JSON : { "Version": "2012-10-17", "Statement": [ { "Effect": "Allow", "Action": [ "s3:GetBucketLocation", "s3:ListAllMyBuckets"], "Resource": "arn:aws:s3:::*" }, { "Effect": "Allow", "Action": "s3:*", "Resource": [ "arn:aws:s3:::BUCKETNAME", "arn:aws:s3:::BUCKETNAME/*" ] } ] }
   * Создаем User groups
7. Настраиваем связь jenkins c instances и GIT :
   * Меняем права ключа *.pem - sudo chmod 600 *pem
   * Отправляем на jenkins instance -  scp -i ./*.pem ./*.pem user@ipaddres:/tmp/
   * Меняем владельца ключа *.pem - sudo chown user: ./*.pem
   * Настраиваем соеедниение с GIT

Jenkins pipeline
1. create_an_environment.groovy - pipeline подготавливающий окружающию среду для Deploy APP
2. build_and_deploy.groovy - pipeline собирающий и разворачивающий Mariadb, Nginx, Wordpress, myPhpAdmin
3. backup_database.groovy - pipeline создающий backup database и сохраняющие его на S3


Примечание:
1. Необходимо добавить Pipeline, который будет проверять состояние instances и APP :
   * В случае отказа одного из websites instances : уведомляет админа, разворачивает копию, подключает к базе данных.
   * В случае отказа базы данных : уведомляет админа, разворачиваеться DB с backup'ом, почередное подключение instances website к поднятной базе.

--------------------------------------------------------------------------------------------------------------------------------------------------

AWS
1. Create jenkins instant with ubuntu on AWS (EC2) :
    * Select the previously created *.pem key
2. Install jenkins - https://www.jenkins.io/doc/book/installing/linux/ :
    * Update packages - sudo apt-get update
    * Install JAVA - sudo apt install openjdk-11-jre
    * Install jenkins - commands from the link above
    * Check jenkins startup - sudo service jenkins status
    * Install GIT - sudo apt install git-all
    * Open port 8080: Security Groups > Edit inbound rules > Add rule = Custom TPC && Port range 8080
3. Create websites instants with ubuntu on AWS (EC2) on the same network:
    * In the first instance, create and save the *.pem key
    * In the second instance, select the previously created *.pem key
    * Create a VPC subnet ***.**.0.0/16 SUBNET IPv4 > CIDRs ***.**.150.0/24
    * Open ports 443
4. Create mariaDB instant with ubuntu on AWS (EC2) on the same network:
    * Open port 3306 for the subnet: Security Groups > Edit inbound rules > Add rule = Subnet ID - IPv4 CIDR
5. Create AWS S3 BUCKET.
6. Create IAM:
    * Create User
    * Create Access key and Secret access key
    * Create Policies > Create > JSON : { "Version": "2012-10-17", "Statement": [ { "Effect": "Allow", "Action": [ "s3:GetBucketLocation", "s3:ListAllMyBuckets "], "Resource": "arn:aws:s3:::*" }, { "Effect": "Allow", "Action": "s3:*", "Resource": [ "arn:aws:s3 :::BUCKETNAME", "arn:aws:s3:::BUCKETNAME/*" ] } ] }
    * Create User groups
7. Set up the connection of jenkins with instances and GIT :
    * Change key permissions *.pem - sudo chmod 600 *pem
    * Send to jenkins instance - scp -i ./*.pem ./*.pem user@ipaddres:/tmp/
    * Change the owner of the *.pem key - sudo chown user: ./*.pem
    * Set up a connection with GIT

Jenkins pipeline
1. create_an_environment.groovy - pipeline preparing environment for Deploy APP
2. build_and_deploy.groovy - pipeline that builds and deploys Mariadb, Nginx, Wordpress, myPhpAdmin
3. backup_database.groovy - pipeline creating a backup database and storing it on S3


Note:
1. You need to add a Pipeline that will check the status of instances and APP :
    * If one of the websites instances fails: notifies the admin, deploys a copy, connects to the database.
    * In case of database failure: notifies the admin, deploys DB with backup, connect instances website one by one to the raised database.
-----------------------------------------------------



# Jenkins Setup on AWS EC2 with MySQL and S3 Integration

This README provides instructions for setting up Jenkins on an Ubuntu EC2 instance on AWS, configuring MySQL and S3, and automating the process with Jenkins pipelines.

## Prerequisites

- AWS account with EC2, IAM, and S3 permissions
- SSH access to EC2 instances
- Jenkins installed on an Ubuntu server

---

## 1. Create Jenkins Instance on AWS EC2

1. **Create EC2 instance:**
   - Choose the appropriate Ubuntu image (e.g., Ubuntu 20.04).
   - Select an existing SSH key pair (*.pem) during the instance setup.

2. **Install Jenkins:**
   - Update the system:  
     `sudo apt-get update`
   - Install Java:  
     `sudo apt install openjdk-11-jre`
   - Install Jenkins:  
     `sudo apt install jenkins`
   - Install Git:  
     `sudo apt install git-all`
   - Check if Jenkins is running:  
     `sudo service jenkins status`

3. **Open necessary ports (8080):**
   - Go to **Security Groups** in AWS.
   - Edit inbound rules and add a rule for Custom TCP on port `8080`.

---

## 2. Create Website Instance on AWS EC2

1. **Create a new EC2 instance for the website:**
   - Use the previously created SSH key pair (*.pem).
   - Select the appropriate VPC and subnet.
   - Open port `443` (HTTPS) in the Security Group.

---

## 3. Create MySQL Instance on AWS EC2

1. **Launch an EC2 instance for MySQL:**
   - Select the same SSH key pair (*.pem) used in previous instances.
   - Open port `3306` for MySQL connections in the Security Group.

---

## 4. Create AWS S3 Bucket

1. **Create an S3 Bucket** for storing backups and data.

---

## 5. Create IAM Role

1. **Create IAM user and access keys:**
   - Create an Access Key and Secret Access Key.
   - Create IAM Policies with JSON:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetBucketLocation",
        "s3:ListAllMyBuckets"
      ],
      "Resource": "arn:aws:s3:::*"
    },
    {
      "Effect": "Allow",
      "Action": "s3:*",
      "Resource": "arn:aws:s3:::BUCKETNAME/*"
    }
  ]
}
```

2. **Create User Groups** and attach IAM policies.

---

## 6. Configure Jenkins and Git Integration

1. **Set permissions for the key pair:**
   - Change the permissions for the SSH key pair file:  
     `sudo chmod 600 *.pem`

2. **Send the Jenkins instance file:**
   - `scp -i ~/.pem /pem user@ipaddress:/tmp/`

3. **Change file owner:**
   - `sudo chown user:user *.pem`

4. **Set up Git connection in Jenkins.**

---

## 7. Jenkins Pipeline Setup

1. **Create `environment.groovy`:**
   - This pipeline will set up the environment for deploying the app.

2. **Create `build_and_deploy.groovy`:**
   - This pipeline compiles and deploys services such as MariaDB, Nginx, WordPress, and phpMyAdmin.

3. **Create `backup.database.groovy`:**
   - This pipeline creates a backup of the database and stores it in S3.

---

## Notes

1. **Add a pipeline to check the health of the instances in the APP:**
   - In case of failure, add monitoring to the website instances.
   - Perform database backups, restore data from backups, and migrate the data if needed.

2. **Monitor database health:**
   - Add steps to monitor the database's health, check backups, and restore as necessary.

---

Feel free to fork this repository or follow the instructions to set up your own Jenkins CI/CD pipeline on AWS.

