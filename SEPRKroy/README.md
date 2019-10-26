# Setup

Little tutorial on how to get setup IntelliJ IDEA and git with our project

## Step 1.
Download Git Bash here https://git-scm.com/downloads to use a CLI for git, 
or use software of your choice.

## Step 2.
Find a folder in your file system, then for Git Bash, use this command to clone the files to your machine:
`git clone "https://github.com/EMHodges/SEPR-game.git"` (you should be asked to sign in)

## Step 3.
Download IntelliJ here https://www.jetbrains.com/idea/download/ for your operating system, 
you can either download the community edition for free, or figure out how to get the Ultimate version,
however the community edition should be sufficient.

## Step 4.
Once you have download and setup eclipse, you should reach this page:  
![alt text](https://i.imgur.com/SCIFYuC.png "IntelliJ")  

Click on `Open` and navigate to where you cloned the project to. Select the `SEPRKroy`
folder (this is where the Java Project is and contains files such as `build.gradle` and 
`settings.gradle`).  
  
IDEA will now start downloading some files and may take some time.

## Step 5.
Once completed, open the project in the Project section on the left of the
screen. The majority of the project will take place in `SEPRKroy->core`, so take a look
in there.

## Step 6.
In order to run the application, start by opening this file:
`SEPRKroy->desktop->src->com.mozarellabytes.kroy.desktop->DesktopLauncher.java`.
Then right click in the editor and then on `Create 'DesktopLauncher.main()...'`  
A popup should appear:  
![alt-text](https://i.imgur.com/KiziFdr.png "Popup")  
Change `Working directory` to `SEPRKroy\core\assets`, click Apply then OK.

## Step 7.
To run the application, simply click on the Green Run button on the top bar:  
![alt-text](https://i.imgur.com/L8XJJmT.png "Run")

---

And that's it! Be sure to comment any questions if you get stuck at any point.
