# SKILLS ASSESSMENT WEB APPLICATION 📝

Welcome to the Skills Assessment Web Application repository! This project was developed as part of my 4th-year capstone project. It offers an engaging platform where users can explore various themes, take assessments, and receive personalized certificates upon completion. The application is built with Spring Boot and MySQL, delivering a robust backend and seamless performance.

## FEATURES 🌟

- **Theme Exploration**: 📚 Browse and select from a variety of knowledge themes.
- **Assessments**: 📝 Take interactive quizzes to test your knowledge.
- **Scoring System**: 📊 Get instant feedback with detailed scores after completing a test.
- **Certificate Generation**: 🏆 Download personalized certificates upon successful completion.
- **Admin Dashboard**: ⚙️ Manage themes, tests, and questions with ease.

## TECHNOLOGIES 💻

- **Spring Boot**: 🌐 Java-based framework for building production-ready web applications.
- **MySQL**: 🗄️ Relational database management system for data storage.
- **OAuth2**: 🔐 Secure authentication with Google and Facebook.
- **IntelliJ IDEA**: 🛠️ IDE used for development.

## DEMO 🎬

Explore the application in action by watching the video demonstration available on [YouTube](https://youtu.be/mqycwiGWafY).

## INSTALLATION ⚙️

> This guide assumes you have JDK, MySQL, and IntelliJ IDEA installed.

To run the application locally, follow these steps:

1. 📂 Import the project into IntelliJ IDEA.
2. ✏️ Locate the file `src/main/resources/application.properties` and fill in the required values:
   - Database name, username, and password.
   - SMTP username and password from [Mailtrap](https://mailtrap.io/).
   - Client ID and client secret for **Google** and **Facebook** OAuth2 authentication.
3. 📥 Import the `quizard.sql` file into your MySQL database to load sample data.
4. 🔎 The default **admin** and **candidate** credentials can be found in `src/.../seeder/DatabaseSeeder.java`
5. ▶️ Build and run the project from IntelliJ IDEA.

## CONTRIBUTING 🤝

Contributions are welcome! If you'd like to contribute to the project, please follow these steps:

1. 🍴 Fork the repository.
2. 🌿 Create a new branch (`git checkout -b feature/my-new-feature`).
3. ✨ Make your changes.
4. 📝 Commit your changes (`git commit -am 'Add some feature'`).
5. ⏫ Push to the branch (`git push origin feature/my-new-feature`).
6. 📬 Create a new Pull Request.

## LICENSE 📄

This project is licensed under the [MIT License](LICENSE).

## CONTACT 📧

For any inquiries or feedback, feel free to reach out to me at [contact@aimanecouissi.com](mailto:contact@aimanecouissi.com).
