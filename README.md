# Assignment Habit

Assignment Habit is an Android application that allows users to browse food items, add them to a cart, and proceed to checkout. The app is built using Kotlin and Jetpack Compose, and it supports both light and dark themes.

## Features

- Browse a list of food items
- Add food items to the cart
- View cart summary with total amount
- Remove items from the cart
- Adjust item quantities in the cart
- Proceed to checkout

## Screenshots


## Installation

1. Clone the repository:
    ```sh
    git clone <your-repo-url>
    ```
2. Open the project in Android Studio.
3. Build and run the project on an emulator or physical device.

## Usage

- **Home Screen**: Browse the list of available food items.
- **Cart Screen**: View the items added to the cart, adjust quantities, and proceed to checkout.

## Project Structure

- `app/src/main/java/com/example/assignment_habit/activity`: Contains the main activities (`MainActivity` and `CartActivity`).
- `app/src/main/java/com/example/assignment_habit/Data/model`: Contains the data model (`FoodItem`).
- `app/src/main/java/com/example/assignment_habit/Data/repository`: Contains the repository for managing food items.
- `app/src/main/java/com/example/assignment_habit/ui/theme`: Contains the theme definitions.
- `app/src/main/java/com/example/assignment_habit/view/components/screens`: Contains the composable screens (`HomeScreen` and `CartScreen`).

## Dependencies

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Coil](https://coil-kt.github.io/coil/) for image loading
- [Moshi](https://github.com/square/moshi) for JSON parsing
