# 🐾 PetCare Platform - Safe walk, pure love
## All-in-One for Pet Services

![Project Banner](https://drive.google.com/file/d/12Dtq7LIG3MWY6QgTcgaDY6dzhWtHb6J8/view?usp=drive_link)

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

## 👥 Team

Developed by **Team 8** at **Hackathon One**.

| Role                                | Name            | GitHub                                           |                                                                                 |
|-------------------------------------|-----------------|--------------------------------------------------|---------------------------------------------------------------------------------|
| Frontend Developer                  | Julio Sosa      | [@juliopzsosa](https://github.com/juliopzsosa)   | [Julio Paz Sosa](https://www.linkedin.com/in/juliopzsosa/)                      |
| Backend Developer                   | Leo Amaya       | [@LEONISPE](https://github.com/LEONISPE)         | [Leo Moises Nisperuza Amaya](https://www.linkedin.com/in/leo-moises-nisperuza-amaya-19568434b/)                   |
| Backend Developer                   | Martin Di Peco  | [@martindipeco](https://github.com/martindipeco) | [Martín Di Peco](https://linkedin.com/in/martindipeco)                          |
| Backend Developer & Project Manager | Julia Rodriguez | [@JuliaDaniR](https://github.com/JuliaDaniR)     | [Julia Daniela Rodriguez](https://www.linkedin.com/in/julia-daniela-rodriguez/) |

## 🎉 Acknowledgments

- Thanks to **No Country** for organizing this amazing hackathon

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

If you have any questions or need help getting started:

- **Email**: [petcare.cuidadoseguro@gmail.com]
- **Issues**: [Create an issue](https://github.com/HO25Equipo8/petcare/issues)

---

**Made with ❤️ during No Country ONE 2025 Hackathon**
