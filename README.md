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

### Milestone/Sprint 2 - In Progress

- Features
    - Complete integration of O-Auth system and associated user management systems
    - Frontend (FE)
        - User's cart view + Backend integration
        - Individual listing view + Backend integration
        - Integration with updated image management system in backend
    - Backend (BE)
        - Add checkout cart endpoint and order confirmation system.
        - Add order history tracking after checkout
        - Update listing API image handling system

### Milestone/Sprint 3 - Starting March 22nd

- Complete manual integration testing QA
- Features
    - Recommendation system
    - Investigate options for live deployment
