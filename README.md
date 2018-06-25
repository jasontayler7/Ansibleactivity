

#### Details of the activities performed for completing Ansible team activity

| Team Members |
| ------------ |
| Charukant    |
| Kamal        |
| Navdeep      |
| Shumail      |
| Tarun        |

##### Top level categorization of tasks performed for completing the assignment.

###### 1. Created organisation in github added members.

![github.com/Team-B-Ninja](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/TeamActivity-githubOrg.png)

###### 2. Installed Ansible in control machine.
   ###### 2.1. We used an Ubuntu 16.04 server for this activity. Executed following commands to complete installation:
```sh
$ sudo apt-add-repository ppa:ansible/ansible
$ sudo apt-get update
$ sudo apt-get install ansible
```
###### 3. Installed Jenkins in control machine using ansible role and assigned permissions to users.

![ Setup User Permission ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/UserPermission.png)

   ###### 3.1. Manage & assign roles  

![ Manage & assign roles ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/Manageandassignroles.png)

###### 4. Master Slave Setup.

![ Job for Master & Slave ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/Jobsandmasterslave.png)

###### 5. Infra setup in slave using ansible roles.

###### 5.1. Infrastructure Job DSL

```groovy
job('INFRABteam'){
steps {
    ansiblePlaybook('/home/ec2-user/roles/site.yml') {
     
        ansibleName('Ansible2.5.4')
        
        credentialsId('root')
        
    }
}
}
```
Created following Ansible role for setting up infrastructure ( Java, Tomcat Nginx and Jenkins )

https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/roles/site.yml

![ Infrastructure setup using Ansible and Jenkins ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/INFRABteam.png)

###### 6. Tag creator Job

   ###### 6.1. Tag creator Job DSL

```groovy
  job('tagcreatorBteam')
  {
    parameters
    {
         stringParam('TAG_NAME', '')
      gitParam('SelectBranch'){
      type('BRANCH')
      }
     }
    scm 
    {
      git
       {
        remote
         {
           url('git@github.com:Team-B-Ninja/ContinuousIntegration.git')
          }
         branch('master')
        }
       }
   steps
    {
  	shell('''
  #!/bin/bash
  cd /var/lib/jenkins/workspace/tagcreatorBteam
  git checkout master
  git tag -a ${TAG_NAME} -m "This is NINJA Team B"
  git push -u origin master
  git push origin --tags
  git tag''')
    }
   
  }
```
![ Tag creator ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/tagcreatorBteam.png)

###### 7. Tag based build job

   ###### 7.1. Build Job DSL

```groovy
 mavenJob('BuildBteam') {
  label('Slave')
    
    parameters
    {
      gitParam('TAG_NAME'){
      type('TAG')
      }
     }
    scm 
    {
      git
       {
        remote
         {
           url('https://github.com/Team-B-Ninja/ContinuousIntegration.git')
          }
         branch('master')
        }
       }
       goals('install') 

             rootPOM("Spring3HibernateApp/pom.xml")

              mavenInstallation('Maven3.5.3')

       postBuildSteps

        {

      	shell('''

      #!/bin/bash

      echo $TAG_NAME

      mv /root/workspace/BuildBteam/Spring3HibernateApp/target/Spring3HibernateApp.war /artifacts/${TAG_NAME}.war''')

        }

      }
```
![ Jenkins Build job ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/BuildBteam.png)

###### 8. Tag based deployement.

###### 8.1. Deployment playbook

```yml
---
- name: "Healthcheck and Deployement"
  hosts: 52.66.13.193
  become: true
  become_user: root
  gather_facts: true
  tasks:
   - name: "copying war into tomcat webapps folder"
     command: cp /artifacts/${TAG_NAME}.war /var/lib/tomcat8/webapps/
          
   - name: "restarting server"
     service:
       name: tomcat8
       state: restarted

   - name: "wait for website to come up"
     uri:
       url: "http://52.66.13.193:8080/${TAG_NAME}"
       status_code: 200
     register: result
     until: result.status == 200
     retries: 5
     delay: 10
```
![ Deployment ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/DeploymentBteam.png)

###### 8.2. Deployment DSL Job

```groovy
  job('DeploymentBteam')
  {
    parameters
    {
       
      stringParam('TAG_NAME', '')
     }
    
   steps
    {
  	shell('''
  #!/bin/bash
  ansible-playbook -u root /home/ec2-user/roles/Deployement.yml -e TAG_NAME=$TAG_NAME''')
    }
   
  }
```

![ Tag based deployment ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/Tagbaseddeployment.png)
