

#### Details of the activities performed for completing Ansible team activity

| Team Members |
| ------------ |
| Charukant    |
| Kamal        |
| Navdeep      |
| Shumail      |
| Tarun        |

##### Top level categorization of tasks performed for completing the assignment.

###### 1. Created organisation in github.

![github.com/Team-B-Ninja](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/TeamActivity-githubOrg.png)

###### 2. Ansible Installation in master.

   1. 

###### 3. Jenkins Installation in master using ansible role.

![ Setup User Permission ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/UserPermission.png)

   1. 

![ Manage & assignroles ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/Manageandassignroles.png)

   2. 

###### 4. Master Slave Setup.

![ Job for Master & Slave ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/Jobsandmasterslave.png)

   1. 

###### 5. Infra setup in slave using ansible roles.

![ Infrastructure setup using Ansible and Jenkins ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/INFRABteam.png)

Infrastructure Job DSL

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



   1. Tomcat Role
   2. Java Role
   3. Nginx Role

###### 6. Tag creator Job

![ Tag creator ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/tagcreatorBteam.png)

   1. Tag creator Job DSL

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

###### 7. Tag based build job

![ Jenkins Build job ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/BuildBteam.png)

   1. Build Job DSL

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

      

   2. 

###### 8. Tag based deployement.

   

![ Deployment ](https://github.com/Team-B-Ninja/Ansibleactivity/blob/master/Media/DeploymentBteam.png)

   1. Deployment DSL Job

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
