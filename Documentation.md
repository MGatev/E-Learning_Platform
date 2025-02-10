# E-Learning Platform Documentation

## Table of Contents
1. [Introduction](#introduction)
2. [Project Overview](#project-overview)
3. [System Features](#system-features)
4. [User Roles](#user-roles)
5. [Use Cases](#use-cases)
6. [API Resources](#api-resources)
7. [System Architecture](#system-architecture)
8. [Database Schema](#database-schema)
9. [Future Enhancements](#future-enhancements)
10. [Conclusion](#conclusion)

---

## 1. Introduction

The **E-Learning Platform** is a comprehensive online education system designed to facilitate interactive learning experiences. It provides a platform for managing user accounts, course offerings, and learning progress. The system is built using the **Spring 6 Application Development Framework**, with a web-based front-end client implemented using **Thymeleaf** or modern JavaScript frameworks like **React.js**, **Vue.js**, or **Angular**. The backend consists of a **RESTful JSON API** to ensure smooth communication between the client and server.

---

## 2. Project Overview

### 2.1 Business Needs
The rise of online education has created a demand for platforms that allow learners to access knowledge and enhance their skills remotely. The E-Learning Platform addresses this need by providing:
- A centralized system for managing courses, lessons, and user accounts.
- Interactive learning experiences for students.
- Tools for instructors to create and manage courses.
- Administrative oversight for platform management.

### 2.2 Key Features
- **User Management**: Registration, login, and role-based access control.
- **Course Management**: Creation, editing, and deletion of courses and lessons.
- **Learning Progress Tracking**: Students can track their progress through courses and assignments.
- **Group Collaboration**: Students can join groups, create articles, and collaborate.
- **Analytics and Reporting**: Instructors and administrators can monitor student performance and platform activity.

---

## 3. System Features

### 3.1 User Roles
The system supports the following user roles:
1. **Unregistered User**:
   - Can view information pages and access sample content.
   - Cannot enroll in courses or access premium features.

2. **Student**:
   - Can enroll in courses, submit assignments, and take quizzes.
   - Can track learning progress through a personalized dashboard.
   - Can join groups, create articles, and view certificates.

3. **Instructor**:
   - Can create, edit, and manage courses and lessons.
   - Can view student performance analytics and provide feedback.
   - Can create quizzes and assignments.

4. **Administrator**:
   - Can manage user accounts, roles, and permissions.
   - Can oversee all courses and platform activity.
   - Can create news, events, and common questions.

---

## 4. Use Cases

### 4.1 Browse Courses and Lessons
- **Actors**: All users.
- **Description**: Users can browse available courses and lessons on the platform.

### 4.2 Register
- **Actors**: Unregistered User, Administrator.
- **Description**: Unregistered users can register by providing an email, name, and password. Administrators can register new users and assign roles.

### 4.3 Login
- **Actors**: Student, Instructor, Administrator.
- **Description**: Registered users can log in using their email and password.

### 4.4 Change User Data
- **Actors**: Registered User, Administrator.
- **Description**: Users can edit their personal data. Administrators can edit any user's data and assign roles.

### 4.5 Manage Users
- **Actors**: Administrator.
- **Description**: Administrators can browse, filter, and manage users. They can edit or delete user accounts.

### 4.6 Manage Student Groups
- **Actors**: Administrator, Instructor.
- **Description**: Users can create and manage student groups. Administrators can add or remove students from groups.

### 4.7 Manage Courses
- **Actors**: Instructor, Administrator.
- **Description**: Instructors can create, edit, and delete courses. Administrators can oversee all courses.

### 4.8 Add/Edit Course
- **Actors**: Instructor, Administrator.
- **Description**: Instructors and administrators can specify or edit course details, including lessons and resources.

### 4.9 Add/Edit Lesson
- **Actors**: Instructor, Administrator.
- **Description**: Instructors and administrators can create or edit lessons within a course.

### 4.10 Complete Lesson
- **Actors**: Student.
- **Description**: Students can mark lessons as completed to track their progress.

### 4.11 Complete Course
- **Actors**: Student, Instructor.
- **Description**: Students complete all lessons and a final quiz to mark a course as complete.

### 4.12 Monitor Course Progress
- **Actors**: Instructor, Administrator.
- **Description**: Instructors and administrators can monitor student progress through a dashboard.

### 4.13 Browse Course Results
- **Actors**: Student, Instructor.
- **Description**: Students can view their course results. Instructors can view results for their courses.

### 4.14 Manage Assignments
- **Actors**: Student, Instructor.
- **Description**: Students can complete assignments. Instructors can assign and grade assignments.

### 4.15 Create Ticket
- **Actors**: Student.
- **Description**: Students can create support tickets for specific lessons or courses.

### 4.16 Resolve Ticket
- **Actors**: Instructor.
- **Description**: Instructors can resolve support tickets by marking them as done.

### 4.17 View Certificates
- **Actors**: Student.
- **Description**: Students can view certificates obtained after completing courses.

### 4.18 Obtain Certificate
- **Actors**: Student.
- **Description**: Students receive certificates upon completing a course.

### 4.19 Create Group
- **Actors**: Student, Instructor, Administrator.
- **Description**: Users can create groups by providing a name, description, and members.

### 4.20 Manage Group
- **Actors**: Student, Instructor, Administrator.
- **Description**: Users can add members and articles to groups. Administrators can delete groups.

### 4.21 Manage Articles
- **Actors**: Student, Instructor, Administrator.
- **Description**: Users can create articles within groups. Administrators can delete inappropriate articles.

### 4.22 Create Quiz
- **Actors**: Instructor.
- **Description**: Instructors can create quizzes for courses.

### 4.23 Change Quiz
- **Actors**: Instructor.
- **Description**: Instructors can update quizzes by adding or modifying questions.

### 4.24 Complete Quiz
- **Actors**: Student.
- **Description**: Students can complete quizzes to pass courses.

### 4.25 View Announcements
- **Actors**: All users.
- **Description**: Users can view platform-wide announcements.

### 4.26 View Analytics
- **Actors**: Instructor.
- **Description**: Instructors can view course analytics, including student performance.

### 4.27 View Activity Logs
- **Actors**: Administrator.
- **Description**: Administrators can view all activity logs on the platform.

### 4.28 Create Category
- **Actors**: Instructor.
- **Description**: Instructors can create categories for courses.

### 4.29 Create Common Question
- **Actors**: Administrator.
- **Description**: Administrators can add common questions and answers.

### 4.30 View Common Questions
- **Actors**: Student, Instructor, Administrator.
- **Description**: Users can view common questions added by administrators.

### 4.31 View Rankings
- **Actors**: Student, Instructor, Administrator.
- **Description**: Users can view their assigned ranks.

### 4.32 Create Rank
- **Actors**: Administrator.
- **Description**: Administrators can create new ranks.

### 4.33 Create News
- **Actors**: Administrator.
- **Description**: Administrators can create news articles.

### 4.34 View News
- **Actors**: Student, Instructor, Administrator.
- **Description**: Users can view the latest news.

### 4.35 Create Event
- **Actors**: Instructor.
- **Description**: Instructors can add new events.

### 4.36 View Events
- **Actors**: Student, Instructor, Administrator.
- **Description**: Users can view upcoming events.

---

## 5. API Resources

The backend API provides the following resources:

| **Resource**            | **Description**                                                                 | **URI**                                   |
|--------------------------|---------------------------------------------------------------------------------|------------------------------------------|
| Users                   | GET all users, POST new user data.                                             | `/api/users`                             |
| User                    | GET, PUT, DELETE user data by `userId`.                                        | `/api/users/{userId}`                    |
| User Roles              | GET a list of user roles.                                                      | `/api/user-roles`                        |
| Login                   | POST user credentials to receive a security token.                             | `/api/login`                             |
| Logout                  | POST to end the active session.                                                | `/api/logout`                            |
| Register                | POST to register a new user.                                                   | `/api/register`                          |
| Courses                 | GET all courses, POST new course.                                              | `/api/courses`                           |
| Course                  | GET, PUT, DELETE course by `courseId`.                                         | `/api/courses/{courseId}`                |
| Lessons                 | GET all lessons in a course, POST new lesson.                                  | `/api/lessons`                           |
| Assignments             | GET all assignments in a lesson.                                               | `/api/users/{userId}/assignments`        |
| Quizzes                 | GET all quizzes in a course, POST new quiz.                                    | `/api/course/{courseId}/quizzes`         |
| Certificates            | GET certificates issued for a course.                                          | `/api/users/{userId}/certificates`       |
| Tickets                 | POST new ticket, GET all tickets.                                              | `/api/users/{userId}/tickets`            |
| Groups                  | POST new group, GET all groups.                                                | `/api/groups`                            |
| Articles                | POST articles to a group, GET all articles.                                    | `/groups/{groupId}/articles`             |
| Announcements           | GET platform-wide announcements, POST new announcement.                        | `/api/announcements`                     |
| Course Analytics        | GET, PUT analytics for a course.                                               | `/api/course/{courseId}/analytics`       |
| Activity Log            | GET, POST activity logs.                                                       | `/api/activity-log`                      |
| Ranks                   | GET, POST ranks.                                                               | `/api/rankings`                          |
| Questions               | GET common questions, POST new question.                                       | `/api/help`                              |
| News                    | GET news, POST news.                                                           | `/api/news`                              |
| Events                  | GET all events, POST event.                                                    | `/api/events`                            |

---

## 6. System Architecture

The system follows a **3-tier architecture**:
1. **Presentation Layer**:
   - Front-end built using **Thymeleaf**.
   - Handles user interaction and displays data.

2. **Application Layer**:
   - Backend built using **Spring 6** (Spring Boot, Spring MVC, Spring Data JPA).
   - Implements business logic, API endpoints, and data processing.

3. **Data Layer**:
   - **MySQL** database for storing user data, courses, lessons, and other entities.
   - Uses **Spring Data JPA** for database interactions.

---

## 7. Database Schema

The database schema includes the following tables:
- **Users**: Stores user information (email, password, role, etc.).
- **Courses**: Stores course details (name, description, category, etc.).
- **Lessons**: Stores lesson details (title, content, course ID, etc.).
- **Assignments**: Stores assignment details (title, description, lesson ID, etc.).
- **Quizzes**: Stores quiz details (questions, answers, course ID, etc.).
- **Groups**: Stores group details (name, description, members, etc.).
- **Articles**: Stores articles created within groups.
- **Certificates**: Stores certificates issued to students.
- **News**: Stores all added news by administrators.
- **Announcements**: Stores all added announements by administrators.
- **Events**: Stores all added announcements by administrators.
- **FAQ**: Stores all added common questions by administrators.
- **Media**: Stores all images in the platform.
- **Tickets**: Stores information about all tickets.
- **Questions**: Stores all questions used for the quizzes.
- **Student results**: Stores all results of the students for their courses.
- **Activity Logs**: Stores platform activity logs.

---

## 8. Future Enhancements

1. **Mobile Application**:
   - Develop a mobile app for iOS and Android to provide a seamless learning experience on mobile devices.

2. **Gamification**:
   - Add gamification features like badges, leaderboards, and rewards to increase user engagement.

3. **AI-Powered Recommendations**:
   - Implement AI algorithms to recommend courses and lessons based on user preferences and learning history.

4. **Live Classes**:
   - Integrate live class functionality using WebRTC for real-time interaction between instructors and students.

5. **Multilingual Support**:
   - Add support for multiple languages to make the platform accessible to a global audience.

---

## 9. Conclusion

The **E-Learning Platform** is a robust system designed to meet the needs of modern online education. It provides a comprehensive set of features for students, instructors, and administrators, ensuring a seamless learning experience. The system's modular architecture, powered by the Spring Framework and RESTful APIs, ensures scalability and flexibility for future enhancements.

---
