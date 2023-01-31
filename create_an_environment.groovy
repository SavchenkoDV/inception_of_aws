import groovy.transform.builder.InitializerStrategy

pipeline {
    agent any
    parameters {
        string( name: 'WebSite', defaultValue: 'None', description: "Enter, separated by a comma, the ip address(es) of the host(s) on which you want to prepare the environment for WEBSITE(s). Example: 13.39.107.210;13.38.121.165")
        string( name: 'DataBase', defaultValue: 'None', description: "Enter, separated by a comma, the ip address(es) of the host(s) on which you want to prepare the environment for DATA BASE(s). Example: 13.38.250.255")
    }
    stages {
        stage('CreateEnvironment') {
            steps {
                script {
                    def ipDB = DataBase.split(';') as Set
                    def ipWS = WebSite.split(';') as Set
                    def ipDocker = ipDB + ipWS

                    for (ip in ipDocker) {
                        println ip.getClass()
                        println ipWS.getClass()
                        println "DOCKER"
//                        sshagent(credentials: ['websites']) {
//                            sh '''
//                                ssh-keyscan -H ${Server} >> ~/.ssh/known_hosts
//                                ssh ubuntu@${Server} '
//                                    sudo apt update;
//                                    sudo apt install apt-transport-https ca-certificates curl software-properties-common -y;
//                                    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -;
//                                    sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu focal stable";
//                                    sudo apt update;
//                                    apt-cache policy docker-ce;
//                                    sudo apt install docker-ce -y;
//                                    sudo systemctl status docker;
//
//                                    sudo curl -L "https://github.com/docker/compose/releases/download/1.26.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose;
//                                    sudo chmod +x /usr/local/bin/docker-compose;
//                                    docker-compose --version;
//                                    sudo apt install mysql-client-core-8.0
//                                '
//                            '''
//                        if (ipWS.findAll(ip)) {
//                            println ip
//                            println "DOCKER COMPOSE"
//                        }

                    }
                }
            }
        }
    }
}