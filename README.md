# Learning Assistance App
 Learning assistance application for Faculty of Computer Science Iasi.
 
## **The structure of Application :**
1. **Homepage**

It contains information about the application, detailing each functionality in the menu and specifying that this application represents my bachelor’s theme.

2. **Quizpage**

Contains hardcoded tests to verify knowledge and improve it. At the end, the time completed and the number of questions answered correctly will be displayed. Every user can share his score using the share button available at the end of the quiz.

3. **Coursepage**

It contains a list of all the courses that can be found in the application. Then, after selecting a subject, the courses will appear one by one, and the download button will appear. You can search for a certain one course using keywords. For example, if you search the word “abstract” or “virtual” it shows the course number 5 because in that course you can find details about that keyword.

4. **Chatpage**

Contains profile page, user page and chat page. Here we will find the details of the logged in account and the possibility to communicate with the online users. In this page we have a tab which contains the profile settings where the user can change his password, delete the account or update his profile photo.

## **Usage scenarios :**
1. **Registration**

•	In this image we can see what the interface of the application for recording looks like.

•	The new user will have to enter a username, an email that will be used to log in, the password and its repetition and the registration number.

•	All these fields must contain information otherwise the user will receive an error. Also, there are some restrictions for this data (password must be at least 6 characters, registration number must be CCCCCCCCCLLLCCCCCC ).

•	If the user already has an account, they can click on the text below the "Already have an account?" Button. and will be redirected to the login interface.

![registerdemo](https://user-images.githubusercontent.com/50926436/183112142-077574e7-24aa-436c-a55e-e2fadce10449.JPG)

2. **Login**

•	In this image we can see the user login interface.

•	You will need to enter the email and password used to register.

•	If the password has been forgotten by clicking on the text "Forgot password?" you will be redirected to a new page where you will have to enter your email address where you will receive an email with which you will be able to reset your password.

•	If the user clicks on the text “Don’t have an account? SignUp” will be redirected to the registration interface.

![logindemo](https://user-images.githubusercontent.com/50926436/183112716-107f9c1e-0c10-4f1a-8a02-7fa9538ff4a6.JPG)

3. **Profile settings**

•	In the next image we can see the profile interface of a user.

•	Here you will be able to see the email used for registration and the registration number.

•	Here he will be able to change the password, delete the user or upload his profile picture.

•	If you choose to change the password you will need to enter the actual password and the new password twice.

•	If you choose to delete the account, the user data from the realtime database will be deleted and you can not recover it.

•	If you choose to upload a profile photo, the profile photo will automatically update everywhere in the application.

![profildemo](https://user-images.githubusercontent.com/50926436/183112786-300f5406-1b21-4b42-9619-d6422057a8f9.JPG)

4. **Coursepage**

•	In the image on the left side we have the list of courses on OOP (Object-Oriented Programming).

•	Each course contains its title and a download button to save the document to the phone’s internal memory.

•	The search bar is functional and we can search for courses by keywords (Example: Course 2 is listed as a keyword in the „friend” database because it represents the course content and we can search for the course that contains details about the friend operator in the search bar).

•	On the right side we have course 2 open in pdf format and at the top we have the search bar with which we can search for different words in the file. After the search, a list will be made of all the appearances and the page number where it is found. A next button will appear at the bottom of the page through which we go through the list of appearances and we will be redirected to the page where we can find the searched word.

Courses            |  Course list
:-------------------------:|:-------------------------:
![cursuridemo](https://user-images.githubusercontent.com/50926436/183113012-1a42bd21-5b24-4343-9962-9c03ae23f4f1.JPG)   |  ![cursuridemo2](https://user-images.githubusercontent.com/50926436/183113072-74dec8ce-d7e8-49c9-9f69-edd9791dbf2a.JPG)


5. **Real-time chat**


•	In the image we can see the interface for the real-time chat with a user.

•	I also implemented th message seen / delivered part to know when the sent message was read or sent.

•	In the top bar we can see the name of the user we want to have a conversation with and his profile picture.

•	A notification will appear on the receiver device with the username of the sender and the message.

•	In this way, I increase the probability to get a response to a message by sending this notification.

![chatdemo](https://user-images.githubusercontent.com/50926436/183113419-e6e707b2-51c0-42be-82b0-94490048918f.JPG)

6. **Taking an quiz in app**

•	In the image on the left side we can see the general quiz page where we will select the course for which we want to take the quiz.

•	In the image on the right side we have a quiz.

•	After choosing the answer it will be colored green – correct answer or red – wrong answer.

•	At the end of the quiz we will be able to share the result obtained.

Quiz list              |  Taking an quiz
:-------------------------:|:-------------------------:
![quiz1](https://user-images.githubusercontent.com/50926436/183113492-5f739acc-5427-48b8-8947-282f5bd936df.JPG)  |  ![quiz2](https://user-images.githubusercontent.com/50926436/183113517-238bc422-ee90-437e-b05b-24c627c653b9.JPG)


