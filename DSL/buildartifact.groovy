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
