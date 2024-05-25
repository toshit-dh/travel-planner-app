# Travel Planner App - README

## Introduction
Welcome to the Travel Planner App! This application helps users plan their trips with ease. From selecting destinations to managing itineraries and finding the best hotels and flights, the Travel Planner App covers all your travel needs. Built using Java for the frontend and Node.js for the backend, this app ensures a seamless and efficient travel planning experience.

## Features
- **Home Screen**: Browse various travel destinations.
- **Trip Fragment**: View and manage your trips.
- **Add Section**: Plan your itinerary, add new trips, and explore destinations on a map.
- **Social Section**: Connect with friends and create trips with them.
- **Explore Section**: Find flights, hotels, and activities. Includes a hotel price predictor using Amadeus API.

## Technologies Used
- **Frontend**: Java
- **Backend**: Node.js
- **APIs**: Amadeus API for flights, hotels, and activities

## Installation

### Prerequisites
- Java Development Kit (JDK) installed
- Node.js and npm installed

### Backend Setup
1. Clone the repository:
    ```sh
    git clone https://github.com/toshit-dh/travel-planner-app.git
    ```
2. Navigate to the backend directory:
    ```sh
    cd travel-planner-api
    ```
3. Install dependencies:
    ```sh
    npm install
    ```
4. Create a `.env` file in the backend directory and add your Amadeus API credentials:
    ```env
    AMADEUS_API_KEY=your_amadeus_api_key
    AMADEUS_API_SECRET=your_amadeus_api_secret
    ```
5. Start the backend server:
    ```sh
    npm start
    ```

### Frontend Setup
1. Open your Android Studio.
2. Import the project located in the `travel-planner-app` directory.
3. Ensure that all dependencies are properly configured in your IDE.
4. Run the project from your IDE.

## Usage

### Home Screen
- Browse through a list of popular travel destinations.
- Click on any destination to view more details.

### Trip Fragment
- View your current and past trips.
- Edit trip details or delete trips you no longer need.

### Add Section
- Plan new itineraries by selecting dates, destinations, and activities.
- Use the integrated map to view your trip route and locations.

### Social Section
- Add friends and view their travel plans.
- Create joint trips with friends and manage group itineraries.

### Explore Section
- Search for flights, hotels, and activities using the Amadeus API.
- Get recommendations based on your preferences.
- Use the hotel price predictor to find the best hotel deals.

## Contributing
We welcome contributions from the community! To contribute:
1. Fork the repository.
2. Create a new branch for your feature or bug fix:
    ```sh
    git checkout -b feature-name
    ```
3. Commit your changes:
    ```sh
    git commit -m "Add new feature"
    ```
4. Push to the branch:
    ```sh
    git push origin feature-name
    ```
5. Open a pull request and describe your changes.

[![Watch the demo video](https://img.youtube.com/vi/XrTq7cusLXI/0.jpg)](https://www.youtube.com/watch?v=XrTq7cusLXI)


