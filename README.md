# Resolvify: Full-Stack Complaint Management System with AI Chatbot

A modern organizational complaint system powered by Spring Boot (Java 17), Aiven MySQL, and React (Vite). Includes advanced AI chat capabilities via the Gemini API.

## Project Structure

This project uses a decoupled frontend and backend logic.

*   **Backend Workspace**: `c:\Users\shrav\Documents\workspace-spring-tools-for-eclipse-5.0.0.RELEASE\complaint-backend`
*   **Frontend Workspace**: `e:\Complaint Managaement system\complaint_management_system`

## Prerequisites
- **Java 17** & **Maven**
- **Node.js** (v18+)
- **NPM**

## How to Run the Backend (Spring Boot)

1. Open your terminal and navigate to the backend workspace:
   `cd "c:\Users\shrav\Documents\workspace-spring-tools-for-eclipse-5.0.0.RELEASE\complaint-backend"`
2. Add your **Gemini API Key** to `src/main/resources/application.properties`:
   `gemini.api.key=YOUR_ACTUAL_KEY_HERE`
3. Run the application:
   `mvnw spring-boot:run`
4. The backend will start on `http://localhost:8080`.

## How to Run the Frontend (React + Vite)

1. Open a new terminal and navigate to the frontend workspace:
   `cd "e:\Complaint Managaement system\complaint_management_system"`
2. Install dependencies (if you haven't already):
   `npm install`
   *(Includes: react, react-dom, react-router-dom, axios, lucide-react)*
3. Start the Vite development server:
   `npm run dev`
4. Access the web app at `http://localhost:5173`.

## Features
- Complete CRUD API via Spring Boot connecting seamlessly to Aiven MySQL.
- Beautiful, fully-responsive SPA frontend resolving organizational issues natively.
- Integrated AI assistant located bottom right to assist employees instantly and intelligently map priorities. 

**Database Details**: The backend is configured to automatically connect to `mysql-2af612c1...aivencloud.com`. Data tables (`complaints`) are mapped and generated automatically by Hibernate.
