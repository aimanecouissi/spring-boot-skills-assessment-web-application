# SKILLS ASSESSMENT WEB APPLICATION 📝

A 4th-year capstone project built with Spring Boot and MySQL that lets users explore knowledge themes, take assessments, and download personalized certificates upon completion.

## FEATURES ✨

- **Theme Exploration** — Browse and select from a variety of knowledge themes.
- **Assessments** — Take interactive quizzes with up to two attempts per test.
- **Scoring System** — Get instant feedback with a detailed score after each test.
- **Certificate Generation** — Download a personalized PDF certificate upon scoring 80% or above.
- **Admin Dashboard** — Manage themes, tests, and questions from a dedicated backend interface.

## TECHNOLOGIES 🚀

- **Spring Boot** — Java-based framework for building production-ready web applications.
- **MySQL** — Relational database for data storage.
- **Thymeleaf** — Server-side template engine for rendering views.
- **Spring Security** — Authentication and authorization framework.
- **OAuth2** — Social login with Google and Facebook.

## DEMO 🎬

Watch the demo on [YouTube](https://youtu.be/mqycwiGWafY).

## INSTALLATION ⚙️

> This guide assumes you have JDK 21, MySQL, and IntelliJ IDEA installed.

1. Import the project into IntelliJ IDEA.
2. Open `src/main/resources/application.properties` and fill in the required values:
   - Database name, username, and password.
   - SMTP username and password from [Mailtrap](https://mailtrap.io/).
   - Client ID and client secret for Google and Facebook OAuth2 authentication.
3. Import `quizard.sql` into your MySQL database to load sample data.
4. Build and run the project from IntelliJ IDEA.
5. Default credentials are seeded automatically on first run and can be found in `src/main/java/com/aimanecouissi/quizard/seeder/DatabaseSeeder.java`.

## CONTRIBUTING 🤝

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature/my-new-feature`).
3. Make your changes.
4. Commit your changes (`git commit -am 'Add feature description'`).
5. Push to the branch (`git push origin feature/my-new-feature`).
6. Open a Pull Request.

## LICENSE 📄

This project is licensed under the [MIT License](LICENSE).

## CONTACT 📧

For any questions or feedback, reach out at [contact@aimanecouissi.com](mailto:contact@aimanecouissi.com).
