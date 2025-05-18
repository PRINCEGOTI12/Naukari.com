# Job Portal Application

This is a full-featured Job Portal Application built using **Spring Boot**, **Spring MVC**, **Spring Data JPA**, **Spring Security**, **Hibernate**, **Thymeleaf**, and **MySQL**. The platform connects job seekers with employers by providing a secure and efficient interface for job applications, job postings, and resume management.

## Features

- **Role-Based Access** – Admin, Recruiter, and Candidate roles  
- **Secure Authentication** – Login/Logout with Spring Security  
- **Resume Upload/Download** – Candidates can upload, recruiters can view/download  
- **Job Management** – Recruiters can post, edit, and delete job listings  
- **Job Listings** – Candidates can view and search jobs  
- **Apply for Jobs** – Candidates can apply directly through the portal  
- **Dashboards** – Personalized dashboards for candidates and recruiters  
- **Account Management** – Manage profile, password, and contact information  
- **Admin Panel** – View user statistics and manage system data  

---

## Tech Stack

- **Backend**: Spring Boot, Spring MVC, Spring Security, Spring Data JPA, Hibernate  
- **Frontend**: Thymeleaf, HTML, CSS, Javascript  
- **Database**: MySQL  

---

## How to Run the Application

### Prerequisites

- Java 23 or above installed  
- Maven installed  
- MySQL running locally or remotely  
- IDE like IntelliJ IDEA or Eclipse (optional but helpful)  

### Steps to Run

1. **Clone the repository**  
   ```bash
   git clone https://github.com/PRINCEGOTI12/Naukari.com.git
   cd Naukari.com

2. **Configure MySQL Database**
   - Copy Script from `SQL_Script.sql` into MySQL
   - Create a MySQL database named `jobportal` 
   - Update `src/main/resources/application.properties` with your MySQL username and password:  
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/jobportal
     spring.datasource.username=jobportal
     spring.datasource.password=jobportal
     ```
     
4. **Build and Run the App**  
   ```bash
   mvn spring-boot:run

5. **Access the Application**
    - Open your browser and go to: `http://localhost:8080`

### Contact
Feel free to open an issue or submit a PR for contributions.
