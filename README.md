# 🍽️ MealMatch

**MealMatch** is an intuitive meal planning and recipe discovery app designed to enhance your cooking experience. Explore a wide variety of dishes, save your favorites 💖, and generate grocery lists 🛒 with ease. Whether you're a foodie or just planning your meals, MealMatch is here to simplify your culinary journey!

---

## 👥 Contributors

This project was developed as part of **CS 3343 - Programming Project 3** under the guidance of **Professor Hend Al-Kittawi**.

A big thanks to the contributors who made **MealMatch** possible:

- **Gabriel Reyesfen** - [gabriel.reyesfen@my.utsa.edu](mailto:gabriel.reyesfen@my.utsa.edu)
- **Felix Nguyen** - [huuphuc.nguyen@my.utsa.edu](mailto:huuphuc.nguyen@my.utsa.edu)
- **Ian Rohan** - [ian.rohan@my.utsa.edu](mailto:ian.rohan@my.utsa.edu)
- **Issac Caldera** - [issac.caldera@my.utsa.edu](mailto:issac.caldera@my.utsa.edu)

---

## 📖 Instructions for Running the Application

To run the **MealMatch** application successfully, please follow these steps:

### Requirements:
1. **Internet Access**: Required for AI-powered features and fetching recipe data.
2. **Android Studio**: Ensure you have the **latest version (Ladybug)** installed to avoid compatibility issues.

### Steps:
1. Clone the repository:
   ```
   git clone https://github.com/UTSA-CS-3443/MealMatch.git
   ```
2. Open the project in Android Studio Ladybug.
3. Sync the project to download dependencies and configure Gradle.
4. Run the application on a device or emulator with internet access.

---

## ⚠️ Known Issues

**Gradle Version Incompatibility**:
    - The application was developed using the **Ladybug version** of Android Studio, which requires a newer Gradle version.
    - Attempting to build the project in the **Koala version** of Android Studio may result in errors due to Gradle version incompatibility.

   ### Suggested Fix:
    - Upgrade to the **Ladybug version** of Android Studio to ensure compatibility with the project's Gradle configuration.
    - After upgrading, sync the project with Gradle to resolve any dependency issues.

---

## 📱 Features

- 🍕 **Dynamic Recipe Browsing**: Scroll through beautifully presented dishes with the help of `RecyclerView`.
- ❤️ **Favorites Management**: Save and manage your favorite dishes with just a tap.
- 🛍️ **Smart Grocery Lists**: Automatically generate grocery lists based on your selected recipes.
- 🌄 **Smooth Image Loading**: Powered by `Glide`, ensuring fast and efficient image rendering.
- 🔍 **Real-time Search**: Quickly find your favorite recipes with a search bar.
- 🤖 **AI-Powered Instructions**: Generate detailed cooking steps using Groq API AI for precision and speed.
- 🖥️ **User-Friendly Interface**: Simple, sleek, and easy to navigate.
- ✨ **Interactive UI**: Tap, swipe, and manage meals effortlessly.

---

## 🛠️ Tech Stack

- **Android Studio**: Primary development environment.
- **Java**: Core programming language.
- **Groq AI**: For generating recipe instructions and insights.
- **Glide**: Image loading and caching.
- **RecyclerView**: Efficient list and grid display.

---

## 📽️ Demo Video

- Visit youtube link [here](https://www.youtube.com/watch?v=GvfcqAoC3kg&feature=youtu.be) for a full demo of our application.

---

## 🚀 Getting Started

1. Clone the repository:
   ```
   git clone https://github.com/UTSA-CS-3443/MealMatch.git
   ```
2. Open the project in Android Studio.
3. Sync the project and run the app on your device or emulator.

---

## 🔑 How to Get a Groq API Key

To use the AI-powered features in MealMatch, you’ll need a Groq API key. Follow these steps to obtain one:

1. **Sign Up for Groq API**:
   - Visit [Groq API](https://groq.com/).
   - Create an account or log in if you already have one.

2. **Generate an API Key**:
   - Once logged in, navigate to the **API Keys** section in your dashboard.
   - Click **Create New Key** and give it a name (e.g., "MealMatch").
   - Copy the generated API key.

3. **Add the API Key to Your Project**:
   - Store the key in your environment variables or securely in `gradle.properties`:
     ```properties
     GROQ_API_KEY=your_api_key_here
     ```

4. **Use the API Key in Your Code**:
   - Ensure your project reads this key during runtime for making authenticated API calls.

---

## 🤝 Contributing
Contributions are welcome! Feel free to submit issues or pull requests to enhance the app.

---

## 📧 Contact
For any inquiries, reach out to us at: 
- gabriel.reyesfen@my.utsa.edu
- huuphuc.nguyen@my.utsa.edu
- ian.rohan@my.utsa.edu
- issac.caldera@my.utsa.edu

---

## ⭐ Acknowledgements
- Icons from [FlatIcon](https://www.flaticon.com/) and Images from [Unplash](https://unsplash.com/).
- Special thanks to [Glide](https://github.com/bumptech/glide) for seamless image handling.
- Powered by [Groq API](https://groq.com/) for AI insights and [Kornkutan](https://github.com/kornkutan/groq4j) for an interface to interact with Groq API.






