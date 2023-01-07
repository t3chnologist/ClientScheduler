# ClientScheduler | Java Advanced Concepts + JavaFX + MySQL database

## Overview
Client Scheduler is a GUI-based desktop application designed for a global consulting organization that operates in multiple languages and has offices located in Phoenix, 
Arizona; New York, New York; and London, England. The organization has provided us with a MySQL database that our application must retrieve data from. 
This database is utilized by other systems as well, so its structure cannot be altered.

### IDE: 
  IntelliJ IDEA 2022.1.3 (Community)
	Build #IU-221.5921.22, built on June 21, 2022
	Runtime version: 11.0.15+10-b2043.56 amd64
	VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
	Kotlin: 221-1.6.21-release-337-IJ5921.22

### JDK: 18.0.2

### JavaFX: javafx-sdk-18.0.2

### MySQL Connector: mysql-connector-java-8.0.30

## Directions for how to run the program:

	1. [File\Settings\Path Variable] "PATH_TO_FX" needs to point to "javafx-sdk-18.0.2\lib" folder;
	2. [File\Project Structure\Libraries] Add two Java project libraries: 
		 "javafx-sdk-18.0.2\lib" folder
		 "mysql-connector-java-8.0.30.jar"

	3. [Run\Edit Configurations...] Run/Debug Configurations -> Modify options -> Add VM Options. 
		Then add the following string as VM Options:  
			--module-path ${PATH_TO_FX} --add-modules javafx.fxml,javafx.controls,javafx.graphics

	4. Run the Main.java file under "\src\main".
