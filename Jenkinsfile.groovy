node {
    properties([parameters([string(defaultValue: '127.0.01', description: 'Please give IP to host a website', name: 'DEVIP', trim: true)]), pipelineTriggers([pollSCM('* * * * * ')])])
    stage("Pull git"){
        git "git@github.com:farrukh90/website.git"
    }

    stage("Install apache"){
        sh "ssh ec2-user@${DEVIP}      sudo yum install httpd -y"
    }

    stage("Start Apache"){
        sh "ssh ec2-user@${DEVIP}      sudo systemctl start httpd"
    }

    stage("Copy files"){
        sh "rsync -aP --delete  index.html   ec2-user@${DEVIP}:/tmp"
    }
    
    stage("Move files"){
        sh "ssh ec2-user@${DEVIP}      sudo cp -f  /tmp/index.html  /var/www/html/index.html"
    }
}