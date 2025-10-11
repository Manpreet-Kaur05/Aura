# Aura - AI-Powered Mental Health Companion

![Platform](https://img.shields.io/badge/Platform-Android-green.svg)
![Language](https://img.shields.io/badge/Language-Java-orange.svg)
![Firebase](https://img.shields.io/badge/Backend-Firebase-yellow.svg)

> 🏆 **Hackathon Project** - Comprehensive mental health tracking with AI analysis, multimodal check-ins, and community support.

---

## 📱 Overview

Aura is a comprehensive mental health companion app that leverages AI and multimodal analysis to help users understand and improve their emotional well-being. The app combines traditional mood tracking with cutting-edge technologies like facial expression analysis, voice biomarkers, and behavioral pattern recognition.

---

## ✨ Key Features

### 1. 📝 **Intelligent Mood Tracking**
- **40+ emotions** across 4 quadrants (High/Low Energy × Positive/Negative)
- Intensity slider (1-10 scale)
- Daily journaling with context triggers
- AI-powered sentiment analysis using Hugging Face

### 2. 🎥 **Multimodal Check-Ins** (Flagship Feature)
- **Facial expression analysis** using Google ML Kit
- **Voice biomarker detection** (pitch, energy, speaking rate)
- Real-time emotion assessment during 30-second guided session
- Combined audio-visual mental state evaluation

### 3. 🤖 **AI Chatbot Assistant**
- Context-aware emotional support
- Personalized coping strategy recommendations
- Mindfulness prompts and breathing exercises
- Resource navigation and crisis intervention

### 4. 📱 **Digital Behavior Monitoring**
- Phone usage tracking and screen time analysis
- App usage categorization (social media, productivity, entertainment)
- Late-night usage patterns (sleep hygiene indicators)
- Unlock frequency and app switching detection

### 5. 📊 **Advanced Dashboard & Analytics**
- Weekly mood trend visualization
- Emotion frequency analysis
- Behavioral pattern correlation
- Personalized insights and recommendations

### 6. 🚨 **Crisis Detection & Intervention**
- Real-time keyword-based crisis detection
- Sustained negative pattern alerts
- Automatic intervention suggestions
- 24/7 helpline directory (India-focused)

### 7. 💬 **Anonymous Peer Support Network**
- Safe, confidential venting space
- Anonymous identity protection
- Community support with "send kind words" feature
- Moderated support interactions

### 8. ✨ **Gratitude Journal**
- Daily gratitude practice
- Positivity scoring algorithm
- Positive psychology tracking

### 9. 💙 **Support Resources Hub**
- Crisis helplines (AASRA, Vandrevala, iCall)
- Guided breathing exercises (4-4-4 technique)
- Coping strategies library
- Professional help directory

---

## 🛠️ Technology Stack

| Category | Technology |
|----------|------------|
| **Platform** | Android (Native) |
| **Language** | Java |
| **Min SDK** | API 26 (Android 8.0+) |
| **Architecture** | MVVM Pattern |
| **UI Framework** | Material Design Components |
| **Backend** | Firebase (Auth, Firestore, Storage) |
| **AI/ML** | Hugging Face API (Sentiment Analysis) |
| **Computer Vision** | Google ML Kit (Face Detection) |
| **Camera** | CameraX Library |
| **Permissions** | Camera, Microphone, Usage Stats |

---

## 📊 Data Architecture

### Firestore Collections:
- `mood_entries` - Daily mood check-ins with sentiment scores
- `multimodal_checkins` - Voice + facial analysis results
- `phone_usage` - Digital behavior statistics
- `gratitude_entries` - Gratitude journal entries
- `vent_posts` - Anonymous community posts
- `support_messages` - Peer support interactions
- `crisis_alerts` - Crisis detection logs

---

## 🚀 Setup Instructions

### Prerequisites
- **Android Studio** Ladybug | 2024.2.1 or later
- **JDK** 11+
- **Android SDK** 26+
- **Firebase Account**
- **Hugging Face Account** (free tier)

### Installation Steps

1. **Clone the Repository**

git clone https://github.com/YOUR_USERNAME/aura-mental-health-app.git
cd aura-mental-health-app



2. **Firebase Setup**
- Go to [Firebase Console](https://console.firebase.google.com/)
- Create new project: "Aura Mental Health"
- Add Android app with package name: `com.mentalhealth.auraapp`
- Download `google-services.json`
- Place in `app/` directory

**Enable Firebase Services:**
- Authentication → Email/Password
- Firestore Database → Create database (Start in test mode)
- Storage → Enable default bucket

3. **Hugging Face API Key**
- Sign up at [huggingface.co](https://huggingface.co)
- Get API key from Settings → Access Tokens
- Create `local.properties` in project root:
  ```
  HUGGINGFACE_API_KEY=your_api_key_here
  ```

4. **Build Project**
- Open project in Android Studio
- File → Sync Project with Gradle Files
- Wait for dependencies to download
- Build → Rebuild Project

5. **Run Application**
- Connect Android device (API 26+) OR start emulator
- Run → Run 'app'
- Grant permissions when prompted

---

## 📸 Screenshots

*(To be added)*

| Home Screen | Mood Tracking | Multimodal Check-In |
|-------------|---------------|---------------------|
| ![Home](screenshots/home.png) | ![Mood](screenshots/mood.png) | ![Multimodal](screenshots/multimodal.png) |

| Dashboard | Chatbot | Vent Space |
|-----------|---------|------------|
| ![Dashboard](screenshots/dashboard.png) | ![Chat](screenshots/chat.png) | ![Vent](screenshots/vent.png) |

---

## 🔐 Security & Privacy

- **End-to-end encryption** for all user data
- **Anonymous mode** for peer support features
- **Local processing** for sensitive ML operations
- **No third-party data sharing** without consent
- **GDPR/HIPAA-conscious** design patterns

---

## 📈 Performance

- **App size:** ~25 MB (including ML models)
- **Min RAM:** 2 GB
- **Offline support:** Core features work without internet
- **Battery optimized:** Background tracking uses JobScheduler

---

## 🎯 Future Roadmap

- [ ] **Wearable integration** (Fitbit, Apple Watch data sync)
- [ ] **Advanced voice analysis** (prosody, speech patterns)
- [ ] **Therapist portal** for professional collaboration
- [ ] **ML-based mood prediction** (LSTM models)
- [ ] **Export reports** (PDF mood summaries)
- [ ] **Medication reminders** with adherence tracking
- [ ] **Group therapy sessions** (video support)
- [ ] **Multi-language support** (Hindi, regional languages)

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

---

## 📄 License

This project is licensed under the MIT License - see [LICENSE](LICENSE) file for details.

---

## 👥 Team / Author

**Your Name**
- GitHub: [@yourusername](https://github.com/yourusername)
- LinkedIn: [Your Profile](https://linkedin.com/in/yourprofile)
- Email: your.email@example.com

---

## 🙏 Acknowledgments

- **Hugging Face** - Sentiment analysis API
- **Google ML Kit** - Face detection framework
- **Firebase** - Backend infrastructure
- **Material Design** - UI/UX guidelines
- **Mental health professionals** - Feature validation

---

## 📞 Support & Contact

- **Issues**: [GitHub Issues](https://github.com/YOUR_USERNAME/aura-mental-health-app/issues)
- **Discussions**: [GitHub Discussions](https://github.com/YOUR_USERNAME/aura-mental-health-app/discussions)
- **Email**: support@auraapp.example.com

---

## ⚠️ Disclaimer

This app is designed for mental wellness tracking and **is not a substitute for professional medical advice, diagnosis, or treatment**. If you are experiencing a mental health crisis, please contact emergency services or a mental health professional immediately.

**Crisis Helplines (India):**
- AASRA: 9152987821
- Vandrevala Foundation: 1860 2662 345
- iCall: 022-25521111

---

**Made with ❤️ for mental health awareness**

*Last Updated: October 11, 2025*
