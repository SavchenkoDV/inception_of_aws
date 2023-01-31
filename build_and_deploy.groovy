pipeline {
    agent any
    options {
        disableConcurrentBuilds()
    }
    environment {
        MDB_ROOT_PASSWORD = credentials('mdb_root_pass')
    }
//    withCredentials([usernameColonPassword(credentialsId: 'mdb_root_pass', variable: 'MDB_ROOT_PASSWORD')]) {
//        // some block
//    }
    parameters {
        string( name: 'WebSite', defaultValue: 'None', description: "Enter, separated by commas, the IP address(es) of the host(s) where you want to build and deploy the WEBSITE(S). Example: 13.39.107.210;13.38.121.165")
        string( name: 'DataBase', defaultValue: 'None', description: "Enter, separated by commas, the IP address(es) of the host(s) where you want to build and deploy the DATA BASE(s). Example: 13.38.250.255")
    }
    stages {
        stage('Build and Deploy') {
            steps {
                script {
                    def ipDB = DataBase.split(';') as Set
                    def ipWS = WebSite.split(';') as Set

                    for (ip in ipDB) {
                        sshagent(credentials: ['websites']) {
                            sh """
                                ssh ubuntu@${ip} '
                                    sudo docker stop mariadb;
                                    sudo docker rm mariadb;
                                    export hname=\$(hostname -i);
                                    mysql -u root -h \$hname --password=cd3dw2c4ffdnjef -Bse "SHOW DATABASES";
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