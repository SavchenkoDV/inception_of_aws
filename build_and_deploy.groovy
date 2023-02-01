pipeline {
    agent any
    options {
        disableConcurrentBuilds()
    }
    environment {
        MDB_ROOT_PASSWORD = credentials('mdb_root_pass')
        MDB_USER_PASSWORD = credentials('mdb_user_pass')
    }
    parameters {
        string( name: 'WebSite', defaultValue: 'None', description: "Enter, separated by commas, the IP address(es) of the host(s) where you want to build and deploy the WEBSITE(S). Example: 13.39.107.210;13.38.121.165")
        string( name: 'DataBase', defaultValue: 'None', description: "Enter, separated by commas, the PRIVATE IP address(es) of the host(s) where you want to build and deploy the DATABASE(s). Or the PRIVATE IP address of the DATABASE to which you want to connect the applications you entered above. Example: 13.38.250.255")
    }
    stages {
        stage('Build and Deploy') {
            steps {
                script {
                    def ipDB = DataBase.split(';') as Set
                    def ipWS = WebSite.split(';') as Set

                    if (ipDB.size() > 1 && ipWS.size() > 0 && ipWS[0] != "None") {
                        error('When creating and deploying an application with a database at the same time, the number of databases can be only one.')
                    } else {
                        def IP_DB="define( 'DB_HOST', '${ipDB[0]}' );"
                    }

                    for (ip in ipDB) {
                        sshagent(credentials: ['websites']) {
                            sh """
                                echo "CREATE DATABASE DVSGroupDB; GRANT ALL PRIVILEGES ON DVSGroupDB.* TO \'dvs\'@\'%\'; FLUSH PRIVILEGES;" > CREATE_DB;
                                scp CREATE_DB ubuntu@${ip}:./
                                ssh ubuntu@${ip} '
                                    sudo docker stop mariadb || true;
                                    sudo docker rm mariadb || true;
                                    sudo apt install mariadb-client-core-10.6 -y;
                                    sudo docker run  --name mariadb --restart always -p 3306:3306 -v /mnt/mariadb-data:/var/lib/mysql --env MARIADB_USER=dvs --env MARIADB_PASSWORD=${MDB_USER_PASSWORD} --env MARIADB_ROOT_PASSWORD=${MDB_ROOT_PASSWORD} -d mariadb:latest;
                                    sleep 20;
                                    export HN=\$(hostname -i)
                                    mysql -u root -h \$HN --password=${MDB_ROOT_PASSWORD} < CREATE_DB;
                                '
                                
                            """
                        }
                    }
//TODO Приватный IP надо прокинуть в wordpress.conf
                    for (ip in ipWS) {
                        sshagent(credentials: ['websites']) {
                            sh """
                                ssh-keyscan -H github.com >> ~/.ssh/known_hosts
                                rm -Rf wpsite
                                git clone git@github.com:SavchenkoDV/wpsite.git
                                sed "2i\\${IP_DB}" wpsite/srcs/wordpress/test
                                scp -r ./wpsite ubuntu@${ip}:./
                                ssh ubuntu@${ip} '
                                    sudo mkdir /mnt/wordpress;
                                    sudo docker-compose -f ./wpsite/srcs/docker-compose.yml down;
                                    sudo docker-compose -f ./wpsite/srcs/docker-compose.yml up -d;
                                '
                            """
                        }
                    }
                }
            }
        }
    }
    post {
        success {
            script {
                emailext body: "Success!", subject: "createEnvironment(s)", to: "admin@admin.ru"
            }
        }
        failure {
            script {
                emailext body: "Failure!", subject: "createEnvironment(s)", to: "admin@admin.ru"
            }
        }
    }
}

