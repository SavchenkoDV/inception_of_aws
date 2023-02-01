pipeline {
    agent any
    options {
        disableConcurrentBuilds()
    }
    environment {
        MDB_ROOT_PASSWORD = credentials('mdb_root_pass')
        S3_ACCESS_KEY = credentials('s3_access_key')
        S3_SECRET_KEY = credentials('s3_secret_key')
    }
    parameters {
        string( name: 'DataBase', defaultValue: 'None', description: "Enter, separated by commas, the IP address, Private IP address DATABASE with whom you want to work. Example: 52.47.138.101;150.134.20.0;")
        string( name: 'Name', defaultValue: 'None', description: "Enter a name for the backup. Example: backupMariaDB")
        string (name: 'Action', defaultValue: 'SAVE',   description: 'What do you want to do with the database?Enter restore or save DATABASE; EXAMPLE:save')
    }
    stages {
        stage('Build and Deploy') {
            steps {
                script {
                    def ipDB = DataBase.split(';') as Set

                    if (ipDB[0] == "None")  {
                        error('Incorrect input. Example: IP;PRIVATE_IP')
                    }
                    if (Action != "save" && Action != "restore" )  {
                        error('Incorrect input. Example_1: restore, Example_2: save')
                    }

                    if (Action == "save") {
                        sshagent(credentials: ['websites']) {
                            sh """                            
                                scp CREATE_DB ubuntu@${ip}:./
                                ssh ubuntu@${ip[0]} '
                                    mysqldump -u root --password=${MDB_ROOT_PASSWORD} -h ${ip[1]} --all-databases  > ${Name};
                                    s3cmd put ${Name} s3://backupsmariadb
                                ' 
                            """
                        }
                    }
                    if (Action == "restore") {
                        sshagent(credentials: ['websites']) {
                            sh """                            
                                scp CREATE_DB ubuntu@${ip}:./
                                ssh ubuntu@${ip[0]} '
                                    s3cmd get s3://buscketname /${Name} ./ 
                                    mysql -u root --password=${MDB_ROOT_PASSWORD} -h ${ip[1]} < ${Name};
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
