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
        string( name: 'WebSite', defaultValue: 'None', description: "Enter, separated by commas, the IP address(es) of the host(s) where you want to build and deploy the WEBSITE(S). Example: 13.39.107.210;15.237.107.47")
        string( name: 'DataBase', defaultValue: 'None', description: "Enter, separated by commas, the IP address(es) of the host(s) where you want to build and deploy the DATABASE(s). Example: 52.47.138.101")
        string( name: 'Config', defaultValue: 'None', description: "Enter the PRIVATE IP address of the DATABASE you want to connect your WEBSITE(S) to. Example: 172.31.150.184")
    }
    stages {
        stage('Build and Deploy') {
            steps {
                script {
                    def ipDB = DataBase.split(';') as Set
                    def ipWS = WebSite.split(';') as Set

                    if (ipDB[0] == "None")  { ipDB = [] }
                    if (ipWS[0] == "None")  { ipWS = [] }

                    if (Config == "None" && ipWS.size() > 0) {
                        error('When building and deploying the application, you must create a database connection. Enter the PRIVATE IP address.')
                    } else {
                        Config = "define( 'DB_HOST', '${Config}' );"
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

                    for (ip in ipWS) {
                        sshagent(credentials: ['websites']) {
                            sh """
                                ssh-keyscan -H github.com >> ~/.ssh/known_hosts                   
                                rm -Rf wpsite
                                git clone git@github.com:SavchenkoDV/wpsite.git
                                sed -i "32i\\${Config}" wpsite/srcs/wordpress/wp-config.php
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

