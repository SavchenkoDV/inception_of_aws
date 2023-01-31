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

                    def ipDB = DataBase.split(';');
                    println ipDB[0]
                    println ipDB[1]
                    def ipWS = WebSite.split(';');
                    println ipWS[0]
                    println ipWS[1]
                    println "--------------"

                    def TEST = ipDB << ipDB
                    int i = 0;
                    println TEST.size()

                    prinln "---------------"
                    while (i < TEST.size()) {
                        println TEST[i];
                        i++;
                    }
                }
            }
        }
    }
}