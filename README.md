# PetCare 🐾

A comprehensive pet care management application developed for the **No Country ONE 2025 Hackathon**.

![PetCare Banner](https://via.placeholder.com/800x200/4CAF50/FFFFFF?text=PetCare+-+Your+Pet%27s+Digital+Companion)

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
- **Runtime**: Node.js
- **Framework**: [Express.js/Nest.js/Fastify]
- **Database**: [MongoDB/PostgreSQL/MySQL]
- **Authentication**: JWT + bcrypt
- **File Storage**: [AWS S3/Cloudinary]
- **Email Service**: [NodeMailer/SendGrid]

### Mobile (if applicable)
- **Framework**: React Native / Flutter
- **Push Notifications**: Firebase Cloud Messaging

### DevOps & Tools
- **Version Control**: Git & GitHub
- **Deployment**: [Vercel/Netlify/Heroku/AWS]
- **Testing**: [Jest/Mocha/Cypress]
- **Code Quality**: ESLint, Prettier

## 🚀 Getting Started

### Prerequisites

Before running this project, make sure you have the following installed:

- **Node.js** (v16 or higher)
- **npm** or **yarn**
- **Git**
- **Database** (MongoDB/PostgreSQL - specify your choice)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/HO25Equipo8/petcare.git
   cd petcare
   git checkout dev2
   ```

2. **Install dependencies**
   ```bash
   # Install backend dependencies
   cd backend
   npm install
   
   # Install frontend dependencies
   cd ../frontend
   npm install
   ```

3. **Environment Setup**

   Create `.env` files in both backend and frontend directories:

   **Backend `.env`:**
   ```env
   NODE_ENV=development
   PORT=5000
   DATABASE_URL=your_database_connection_string
   JWT_SECRET=your_jwt_secret
   EMAIL_SERVICE_API_KEY=your_email_service_key
   CLOUDINARY_CLOUD_NAME=your_cloudinary_name
   CLOUDINARY_API_KEY=your_cloudinary_key
   CLOUDINARY_API_SECRET=your_cloudinary_secret
   ```

   **Frontend `.env`:**
   ```env
   VITE_API_BASE_URL=your_api_base_url_here
   ```

4. **Database Setup**
   ```bash
   # Run database migrations (if applicable)
   cd backend
   npm run migrate
   
   # Seed database with sample data (optional)
   npm run seed
   ```

5. **Start the application**
   ```bash
   # Start backend server
   cd backend
   npm start

   # In a new terminal, start frontend
   cd ../frontend
   npm start
   ```

The application will be available at:
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:5000

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