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

                    def ipDocker = (ipDB + ipWS) as set

                    println ipDocker.size()


                    while (i < ipDocker.size()) {
                        println result[i];
                        i++;
                    }


                }
            }
        }
    }
}