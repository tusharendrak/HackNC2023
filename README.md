# Road Guardian

## Description
Our application streamlines the process of reporting road issues. With just a single tap, users can take a picture of a pothole or damaged road, and the app will automatically log its location. This effortless process ensures that even the busiest individuals can contribute to the community's safety.

For administrators and officials, a real-time dashboard showcases these reports, allowing swift and efficient actions. Once the issues are addressed, they can be marked as 'fixed', updating the database and informing the users about the rectified problem.

**Unique Features**:
- **De-duplication System**: Our application has the ability to auto-remove duplicate reports or those within a 10m-100m radius, working on the assumption that nearby issues get resolved simultaneously.
  
- **Machine Learning Integration**: We've incorporated a machine learning model that meticulously scans every submission. This model filters out irrelevant or inappropriate submissions based on advanced image analysis, ensuring the quality and relevance of reports.

## Getting Started

To get the project up and running on your local machine, follow these steps:

### Prerequisites

Ensure you have [Node.js](https://nodejs.org/) and npm installed.

### Installation and Running

1. **Clone the Repository**

2. **Install Dependencies**
   npm install 

3. **Start the application**
  npm start

## Available Scripts

In the project directory, you can run:

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

The page will reload when you make changes.\
You may also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can't go back!**

If you aren't satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you're on your own.

You don't have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn't feel obligated to use this feature. However we understand that this tool wouldn't be useful if you couldn't customize it when you are ready for it.

## Learn More

You can learn more in the [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).

To learn React, check out the [React documentation](https://reactjs.org/).

### Code Splitting

This section has moved here: [https://facebook.github.io/create-react-app/docs/code-splitting](https://facebook.github.io/create-react-app/docs/code-splitting)

### Analyzing the Bundle Size

This section has moved here: [https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size](https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size)

### Making a Progressive Web App

This section has moved here: [https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app](https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app)

### Advanced Configuration

This section has moved here: [https://facebook.github.io/create-react-app/docs/advanced-configuration](https://facebook.github.io/create-react-app/docs/advanced-configuration)

### Deployment

This section has moved here: [https://facebook.github.io/create-react-app/docs/deployment](https://facebook.github.io/create-react-app/docs/deployment)

### `npm run build` fails to minify

This section has moved here: [https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify](https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify)
![image](https://github.com/tusharendrak/HackNC2023/assets/110748078/4bd704d9-a30a-4563-80bd-bdd0bb7ea926)
![image](https://github.com/tusharendrak/HackNC2023/assets/110748078/7d7d7c97-c45c-466e-bc12-889a46ebbbd9)
![image](https://github.com/tusharendrak/HackNC2023/assets/110748078/84fc4292-e249-4bc9-8a6d-23f9148757be)

screenshots of the android app. We also have made a web app dashboard for the admin to retrive and delete clusters, All of which happens in real time in the db and UI.
![image](https://github.com/tusharendrak/HackNC2023/assets/110748078/14b4fd6a-ec94-4593-8d44-f573c910b99a)

There's also a trained ML model which classifies the road images are true or false to help with fraud detection.
