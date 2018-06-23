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
