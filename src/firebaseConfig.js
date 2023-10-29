// firebaseConfig.js
import { initializeApp } from 'firebase/app';

// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyCncOaH6dw9Jt23ennh6K9C-0HQawGwlw8",
  authDomain: "hacknc2023.firebaseapp.com",
  databaseURL: "https://hacknc2023-default-rtdb.firebaseio.com",
  projectId: "hacknc2023",
  storageBucket: "hacknc2023.appspot.com",
  messagingSenderId: "682949610844",
  appId: "1:682949610844:web:b07753805443455713c599",
  measurementId: "G-MM7CBHW3PB"
};
  
  // Initialize Firebase

const firebaseApp = initializeApp(firebaseConfig);

export default firebaseApp;
