job('INFRABteam'){
steps {
    ansiblePlaybook('/home/ec2-user/roles/site.yml') {
     
        ansibleName('Ansible2.5.4')
        
        credentialsId('root')
        
    }
}
}
