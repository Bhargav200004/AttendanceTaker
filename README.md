# 📲 AttendanceTaker App

**AttendanceTaker** is a modern Android app designed to simplify attendance tracking for classrooms, meetings, and events. It features secure cloud syncing, real-time notifications, and a beautiful UI built with Jetpack Compose.

---

## ✨ Features

- **📚 Subject & Class Management**  
  Easily create and organize subjects or sessions.

- **👥 Student/Participant List**  
  Add and manage students or participants for each subject.

- **✅ Quick Attendance Marking**  
  Mark students as **present**, **absent**, or **late** with one tap.

- **📅 Calendar View**  
  Visualize attendance with a calendar to spot trends and gaps.

- **📊 Attendance Reports** *(Planned)*  
  Exportable reports to track individual and overall attendance.

- **🔔 Smart Notifications**  
  Notifications using **Android Services** to remind users to mark attendance.

- **🛡️ Supabase Authentication**  
  Secure sign-in with **Supabase Auth** (email/password or third-party).

- **☁️ Supabase Database**  
  Attendance data is synced and stored securely in **Supabase Postgres**.

- **🎨 Jetpack Compose UI**  
  Smooth, modern UI using the latest Android design practices.

---

## 🧪 Test-Credentials
- ``Email`` -> **_testteacher@gmail.com_**
- ``Password`` -> **_test@100_**

## 📷 Demo Video

https://github.com/user-attachments/assets/eb61c7b1-3bcd-414c-a7d8-d6ea07582408

---

## 🛠 Tech Stack

| Layer           | Tech Used                                         |
|----------------|---------------------------------------------------|
| **UI**         | Jetpack Compose                                   |
| **Architecture**| MVVM + Repository Pattern                        |
| **Backend**    | Supabase (Auth + Postgres via PostgREST)          |
| **Local Storage**| DataStore Preferences                          |
| **Notifications**| Android Services / WorkManager (for reminders) |
| **Async**      | Kotlin Coroutines                                 |
| **DI**         | Hilt                                              |
| **Navigation** | Jetpack Navigation Compose                        |
| **Logging**    | Timber                                            |
| **JSON**       | Kotlinx Serialization                             |

---

## 🗂️ Project Structure

- `MainActivity.kt` — Main launcher, sets up UI and permissions.
- `AppNavigation.kt` — App routes and screen navigation.
- `SnackBarController.kt` — For global message handling.
- `NotificationService.kt` — Service handling in-app reminders.
- `SupabaseService.kt` — Logic for Auth & DB sync using Supabase.
- `AttendanceTakerTheme.kt` — UI theming file.

---

## 🚀 Getting Started

1. **Clone the repo**
   ```bash
   git clone https://github.com/your-username/AttendanceTaker.git
   cd AttendanceTaker
