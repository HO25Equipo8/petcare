# 🐾 PetCare — All-in-One Platform for Pet Services

![Project Banner](./assets/banner-petcare.png) <!-- Replace with real image -->

**PetCare** is a web application that connects pet owners with trusted animal care professionals (walkers, veterinarians, sitters, and more).
It’s built to deliver a collaborative, visual, and emotionally engaging experience — with robust workflows, cross-layer validations, and a double-consent model for sensitive actions.

---

## 📑 Table of Contents

* [📌 Overview](#-overview)
* [🛠️ Tech Stack](#️-tech-stack)
* [🌍 Project Architecture](#-project-architecture)
* [🚀 Deployments](#-deployments)
* [📂 Project Structure](#-project-structure)
* [📖 Key Endpoints](#-key-endpoints)
* [⚡ Getting Started](#-getting-started)
* [📊 Database Setup](#-database-setup)
* [🌐 External Integrations](#-external-integrations)
* [🧠 Validations & Security](#-validations--security)
* [🧩 Entities & DTOs](#-entities--dtos)
* [🎨 Visual & UX Customization](#-visual--ux-customization)
* [📈 Metrics & Reputation](#-metrics--reputation)
* [🧪 Testing & Quality Assurance](#-testing--quality-assurance)
* [📌 Roadmap](#-roadmap)
* [👥 Authors](#-authors)

---

## 📌 Overview

Main features include:

* 📅 **Booking management** — create, confirm, cancel, and reschedule services.
* 🎥 **Live sessions via WebSockets** — real-time updates while the service is in progress.
* 🐶 **Pet profiles** — photos, service history, and medical details.
* 🌍 **Geolocation with Google API** — find nearby professionals and autocomplete addresses.
* ✉️ **Email notifications** — confirmations, reminders, and reschedule alerts.
* 🔑 **Secure authentication** — JWT-based login plus Google OAuth support.
* ⭐ **Reputation system** — public feedback and ratings for professionals.

---

## 🛠️ Tech Stack

| Layer         | Technology                                    |
| ------------- | --------------------------------------------- |
| Backend       | Java 21, Spring Boot 3, Hibernate             |
| Security      | Spring Security, JWT, Google OAuth            |
| Database      | MySQL 8                                       |
| Frontend      | React (JavaScript), Axios, Vite, Tailwind CSS |
| Real-time     | WebSocket                                     |
| External APIs | Google Maps & Places, Email API               |
| Documentation | Swagger UI                                    |

---

## 🌍 Project Architecture

![Architecture Diagram](./assets/arquitectura.png) <!-- Replace with real image -->

* **Frontend (React):** dynamic UI for managing pets, bookings, and sessions.
* **Backend (Spring Boot):** REST API, business logic, authentication, WebSocket services.
* **Database (MySQL):** structured data for users, pets, bookings, and plans.
* **WebSockets:** bidirectional communication for live sessions and tracking.
* **Email Service:** automatic transactional notifications.

---

## 🚀 Deployments

* 🖥️ [Frontend](https://petcare-zeta-kohl.vercel.app/home)
* 📘 [Backend Swagger UI](https://petcare-7yjq.onrender.com/swagger-ui/index.html)

---

## 📂 Project Structure

```bash
PetCare/
│── backend/                # REST API - Java + Spring Boot
│   ├── src/main/java
│   ├── src/main/resources
│   └── pom.xml
│
│── frontend/               # React + JavaScript + Axios
│   ├── src/
│   ├── public/
│   └── package.json
│
└── README.md
```

---

## 📖 Key Endpoints

### 👤 Users

```http
POST /login
POST /user/register
GET /user/me-profile
PUT /user/update-profile-frontend
GET /user/my-bookings
```

### 👩‍⚕️ Professionals (Sitters)

```http
POST /sitter/register/schedule
POST /sitter/register/offering
POST /sitter/register/discount/rule
PUT /sitter/booking/{id}/confirm
PUT /sitter/booking/{id}/cancel
```

### 🐶 Pet Owners

```http
POST /owner/register/pet
POST /owner/register/booking
PUT /owner/pet/{petId}
POST /owner/search/nearby-sitters
GET /owner/my-plan
```

### 📅 Sessions

```http
POST /sessions/{bookingId}/start
PUT /sessions/{id}/finish
GET /sessions/{bookingId}/join
POST /sessions/{sessionId}/updates/incident
```

### 📍 Real-time Tracking

```http
POST /tracking/{bookingId}
GET /tracking/{bookingId}/last
```

### ⚠️ Incidents

```http
POST /api
GET /api/{incidentId}
PUT /api/{incidentId}
POST /api/{incidentId}/images
```

### 🛠️ Admin

```http
POST /admin/register/plan
GET /admin/list/plan
GET /admin/public-profiles
POST /admin/schedules/expire-now
```

📌 Full API docs available in Swagger UI

---

## ⚡ Getting Started

### Requirements

* Java 21
* Maven
* MySQL 8
* Node.js 18+ (for React frontend)

### Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Runs at: `http://localhost:8080`

### Frontend

```bash
cd frontend
npm install
npm start
```

Runs at: `http://localhost:3000`

---

## 📊 Database Setup

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/petcare
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 🌐 External Integrations

* **Google API:** geolocation & address autocomplete
* **WebSockets:** real-time service sessions
* **Email Service:** transactional notifications
* **Google OAuth:** secure login via Google

---

## 🧠 Validations & Security

* Cross-layer input validations
* Double-consent for reschedules
* Role-based access control
* Profile verification before enabling features
* Audit logs for sensitive changes

---

## 🧩 Entities & DTOs

* **MapStruct** for entity–DTO mapping
* Flow-specific, clean DTOs
* Enums for encapsulation
* Refactored mappers for SaaS-oriented design

---

## 🎨 Visual & UX Customization

* Real pet photos
* Styling tailored by species & temperament
* Visual storytelling during live sessions
* Role-based UI identity

---

## 📈 Metrics & Reputation

* Reputation scored per completed service
* Secured average rating (`averageRating`)
* Service-level & professional-level metrics
* Incident history with resolution tracking

---

## 🧪 Testing & Quality Assurance

* Unit tests covering roles, ranges, and states
* Mocked authenticated users for testing flows
* Integration tests per feature
* Temporal logic validation (`Instant`, `Duration`)

---

## 📌 Roadmap

* 📱 Mobile app (React Native)
* 💳 Stripe / MercadoPago integration
* 🧠 AI-powered recommendations
* 🌍 Multi-language support
* 📦 PDF export for service history
* 🔔 Smart reminders & alerts
* 🧭 Real-time metrics dashboard
* 🐾 Pet profile customization by species/temperament

---

## 👥 Authors

Developed by **Team HO25Equipo8** at **Hackathon One**.

---

Would you like me to also **restructure it in a GitHub-optimized style** (with badges, quick-start at the top, and a shorter marketing-style description), or do you want to keep this **developer-focused documentation style**?



# PetCare 🐾

A comprehensive pet care management application developed for the **No Country ONE 2025 Hackathon**.

![PetCare Banner](https://drive.google.com/file/d/12Dtq7LIG3MWY6QgTcgaDY6dzhWtHb6J8/view)


PetCare is a web application designed to connect pet owners with animal care professionals (walkers, veterinarians, sitters, etc.). It offers a collaborative, visual, and emotional experience, with robust workflows, cross-layer validation, and double opt-in logic.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [Usage](#usage)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [Team](#team)
- [License](#license)

## 🎯 Overview

PetCare is a digital solution designed to help pet owners manage their pets' health, appointments, and daily care routines. Built during the No Country ONE 2025 Hackathon, this application aims to streamline pet care management and improve the relationship between pet owners, their pets, and veterinary professionals.

## ✨ Features

### Core Features
- **Pet Profile Management**: Create detailed profiles for each pet with photos, breed information, and medical history
- **Health Tracking**: Monitor vaccination schedules, medication reminders, and health records
- **Appointment Scheduling**: Book and manage veterinary appointments
- **Care Reminders**: Set up notifications for feeding, medication, and exercise routines
- **Emergency Contacts**: Quick access to emergency veterinary services
- **Growth Tracking**: Monitor pet growth and weight over time

### Advanced Features
- **Multi-pet Management**: Handle multiple pets in one account
- **Veterinarian Portal**: Dedicated interface for veterinary professionals
- **Medical Records**: Secure storage and sharing of medical documents
- **Community Features**: Connect with other pet owners in your area
- **GPS Pet Tracking**: Location services for pet safety (if applicable)

## 🛠 Tech Stack

### Frontend
- **Framework**: [React](https://react.dev/)
- **Styling**: [Tailwind CSS](https://tailwindcss.com/) + [Radix UI](https://www.radix-ui.com/)
- **State Management**: [Context API](https://react.dev/learn/passing-data-deeply-with-context)
- **Build Tool**: [Vite](https://vite.dev/)

### Backend
- **Runtime**: Java 21
- **Framework**: [SpringBoot]
- **Database**: [MySQL]
- **Authentication**: JWT + bcrypt

### DevOps & Tools
- **Version Control**: Git & GitHub
- **Deployment**: [Vercel/Render/Aiven]

## 🚀 Getting Started

The application is available at:
- **Frontend**: https://petcare-zeta-kohl.vercel.app/
- **Backend API**: https://petcare-7yjq.onrender.com/

## 💡 Usage

### For Pet Owners

1. **Registration & Login**
    - Create an account with email verification
    - Complete your profile setup

2. **Add Your Pet**
    - Click "Add New Pet"
    - Fill in pet details (name, breed, age, medical history)
    - Upload a profile photo

3. **Schedule Appointments**
    - Navigate to "Appointments"
    - Select your preferred veterinarian
    - Choose available time slots

4. **Set Reminders**
    - Go to "Care Schedule"
    - Add feeding, medication, and exercise reminders
    - Customize notification preferences

### For Veterinarians

1. **Professional Account Setup**
    - Register as a veterinary professional
    - Verify credentials
    - Set up clinic information

2. **Manage Appointments**
    - View and manage appointment requests
    - Access pet medical histories
    - Update treatment records

## 📚 API Documentation

### Authentication Endpoints

```http
POST /api/auth/register
POST /api/auth/login
POST /api/auth/logout
GET /api/auth/profile
```

### Pet Management Endpoints

```http
GET /api/pets              # Get all pets for authenticated user
POST /api/pets             # Create new pet profile
GET /api/pets/:id          # Get specific pet details
PUT /api/pets/:id          # Update pet information
DELETE /api/pets/:id       # Delete pet profile
```

### Appointment Endpoints

```http
GET /api/appointments      # Get user appointments
POST /api/appointments     # Create new appointment
PUT /api/appointments/:id  # Update appointment
DELETE /api/appointments/:id # Cancel appointment
```

For complete API documentation, visit `/api/docs` when running the server locally.

## 📁 Project Structure

```
petcare/
├── backend/
│   ├── controllers/
│   ├── middleware/
│   ├── models/
│   ├── routes/
│   ├── utils/
│   ├── config/
│   └── server.js
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── hooks/
│   │   ├── services/
│   │   ├── utils/
│   │   └── App.js
│   └── package.json
├── mobile/ (if applicable)
├── docs/
├── tests/
├── .gitignore
├── README.md
└── package.json
```

## 🤝 Contributing

We welcome contributions from the community! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. **Make your changes**
4. **Commit your changes**
   ```bash
   git commit -m 'Add amazing feature'
   ```
5. **Push to the branch**
   ```bash
   git push origin feature/amazing-feature
   ```
6. **Open a Pull Request**

### Code Style Guidelines
- Use ESLint and Prettier for code formatting
- Write meaningful commit messages
- Include tests for new features
- Update documentation as needed

## 👥 Team

**Equipo 8 - No Country ONE 2025**

| Role | Name | GitHub | LinkedIn |
|------|------|--------|----------|
| Frontend Developer | [Name] | [@username](https://github.com/username) | [Profile](https://linkedin.com/in/profile) |
| Backend Developer | [Name] | [@username](https://github.com/username) | [Profile](https://linkedin.com/in/profile) |
| UI/UX Designer | [Name] | [@username](https://github.com/username) | [Profile](https://linkedin.com/in/profile) |
| Project Manager | [Name] | [@username](https://github.com/username) | [Profile](https://linkedin.com/in/profile) |

## 🎉 Acknowledgments

- Thanks to **No Country** for organizing this amazing hackathon
- Special thanks to mentors and reviewers
- Icons and images from [Unsplash](https://unsplash.com) and [Feather Icons](https://feathericons.com)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

If you have any questions or need help getting started:

- **Email**: [team-email@example.com]
- **Discord**: [Your Discord Server]
- **Issues**: [Create an issue](https://github.com/HO25Equipo8/petcare/issues)

---

**Made with ❤️ during No Country ONE 2025 Hackathon**

![Footer](https://via.placeholder.com/800x100/2196F3/FFFFFF?text=Thank+you+for+checking+out+PetCare!)