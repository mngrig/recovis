# Recovis

**Recovis** is a system for recording and visualizing outpatient vital signs and monitoring data using smartphones, developed as part of my BSc-Integrated MSc [diploma thesis](https://polynoe.lib.uniwa.gr/xmlui/bitstream/handle/11400/7332/ice_18390071.pdf).
It consists of three main components: an Android app for patients, a web application for doctors, and a backend API to manage and store medical data.

---

## Project Structure

- `android/`  → Android application for patients (Java)
- `frontend/` → Web application for doctors (React.js)
- `backend/`  → SpringBoot Backend


---

## Components

### 1. Android App (Patient)
The Android application allows patients to record and manage their medical data. Key features include:

- **Daily measurements**: Record body temperature, blood pressure, medication doses, fluid intake/output, etc.  
- **Comments**: Add notes for specific days or periods to communicate with doctors indirectly.  
- **Annual measurements**: Quickly mark completion of annual exams with one click.


---

### 2. Web Application (Doctor)
The web application provides doctors with tools to monitor and manage their patients. Key features include:

- **Patient dashboard**: Overview of all patients with editing capabilities.  
- **Patient profile management**: Define required measurements, instructions, and optional/mandatory fields.  
- **Tabular view of measurements**: Detailed tables of patient data, filterable by date.  
- **Data visualization**: Graphs and charts to analyze trends and support treatment decisions, using Chart.js

---

### 3. SpringBoot Backend
The backend handles all data storage, retrieval, and integration between Android and web app.  
It exposes endpoints for managing patient exams, patient data etc.

---

## Installation & Setup

### Android
1. Open `android/`  
2. Build and run on an emulator or physical device.  

### Frontend
1. Open `frontend/`. 
2. Install dependencies:
```bash
npm install
```

### Backend (Spring Boot)
1. Open the `backend/` folder.  
2. Ensure **Java JDK 17+** is installed.  
3. Configure your database and environment variables if required
4. Run the Spring Boot application
