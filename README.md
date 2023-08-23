## Authors
* Name: Micha Briskman  Email: michabri@edu.hac.ac.il Id: 208674713
* Name: Shlomo Gulayev Email: shlomogu@edu.hac.ac.il Id: 318757382

## Description
Our program aims to build a program like Messenger, where logged users can chat with other registered users.<br>
First the user needs to sign up to the program. Then, he needs to login to the program with the password and username
he registered.<br>
Then, the user is redirected to a page called "chatRooms" where a table of all the registered users are displayed to him. <br>
The user can choose which other user he wants to chat with, and then is redirected to the chatPage with that user.
We added an admin role. Only the Admin user can remove other registered users. <br>
(The admin sees that possibility in chatRoom page). <br>
Removing the user also removes the messages the user wrote and received. <br>
The program has 3 controllers, one for the registered users, the second is for sending the messages and the third one
for rest api for the users. <br>
The program has 2 repositories: userRepository and messageRepository.

### General information
We used JPA - mySQL database to store all the registered users, and all the messages. <br>
We used bcrypt to encrypt the users passwords. <br>
We used thymeleaf view engine. <br>
We used bean configuration. <br>
We used an interceptor that handles the sessions and redirecting pages if a user is logged into the site or not.<br>
All of the logic is in server side (validations and extra), except the long pulling in chatPage. <br>
We used thread safe (synchronize) to store the data. <br>
The chatPage is SPA (not redirecting to another page when sending a message). 
### Functionality
<a href="./doc/index.html">Java-Doc</a>

## Installation
The tables are created in JPA - mySQL when running the program.

## Useful information
To login with an admin user enter the username "Admin" (capital letter 'A'), and any password.
There are no duplications in the userName.
Password must be at least 6 characters long <br>
Password must contain at least one letter and one number