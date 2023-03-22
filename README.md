# SYSC4806-Project - Group 13

## Group Members

- Kevin Fox
- Jason Gao
- Harjap Gill
- Golan Hassin
- Millan Wang

## Running the App

1. Run the Backend
    - In IntelliJ
        - Open the `Backend` folder as a project in IntelliJ
        - Run Sysc4806ProjectApplication
    - With the .jar file
        - run `\Milestone 1\SYSC4806_Project-0.0.1-SNAPSHOT.jar`
2. Run the Frontend
    - Run the following commands from the root of the repository
        - `cd frontend`
        - `npm install`
        - `npm start`
    - This should open a new browser tab running the application
  
## Current State of Project

### Milestone/Sprint 1 - COMPLETE

- Management
    - Agreed on Amazin project
        - Decomposed problem domain into feature list and DB schema
    - Set up environment. Agreed on purely REST based BE with React FE using OAuth
    - Configured Github actions CI pipeline for FE and BE integration testing
- Features
    - Frontend (FE)
        - Showing listings in a grid
            - Visualization
            - Attached to backend
        - Creating listings form
            - Visualization
            - Attached to backend
        - Partial completion of O-Auth logged in context (PR in review)
    - Backend (BE)
        - Made exception handling and response system for API controllers
        - Made re-usable parameter validation systems for API controllers
        - Endpoints for listings (CRUD)
        - Endpoint for user carts (CRUD)
        - Partial completion of O-Auth integration and integration with users (PR in review)

### Milestone/Sprint 2 - Complete

- Features
       - Cart Feature
       - Checkout feature
       - O-Auth Spring Security
       - Editing Listings with Cover images
       - Filtering Listings
       
       # Important notices
     - Since OAuth has been implemented, you need a specific application.propertie's to be able to run the backend.
       Please ask Group 13 if you need them.
     - You need a gmail account.
       

### Milestone/Sprint 3 - Starting March 22nd

- Complete manual integration testing QA
- Features
    - Recommendation system
    - Investigate options for live deployment
