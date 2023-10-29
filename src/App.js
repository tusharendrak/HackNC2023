import React, { useState, useEffect } from 'react';
import { getDatabase, ref, onValue } from 'firebase/database';
import firebaseApp from './firebaseConfig'; // Import the Firebase configuration from your file
import DataCard from './DataCard';


const db = getDatabase(firebaseApp);
// const dataRef = ref(db, '/'); // Replace 'your_data_path' with the actual path to your data

function App() {
  const [data, setData] = useState(null);
  const [sortedData, setSortedData] = useState(null); // New state variable for sorted data


  useEffect(() => {
    // Reference to the 'uploads' node
    const uploadsRef = ref(db, 'uploads');

    // Listen for changes in the 'uploads' node
    onValue(uploadsRef, (snapshot) => {
      const uploadsData = snapshot.val();
      setData(uploadsData);
    });
  }, []);

  useEffect(() => {
    if (data) {
      setSortedData(handleSortByDate(data));
    }
  }, [data]);

  const handleDelete = async function handleDelete(index) {
    try {
      // Replace with your API endpoint
      const response = await fetch('https://us-central1-hacknc2023.cloudfunctions.net/deleteNearbyData', {
        method: 'POST', // Or 'DELETE' based on your backend setup
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ mainRecordId: index })
      });
  
      if (response.ok) {
        // Handle successful response, maybe update the UI or provide feedback
        // console.log("Item deleted successfully.");
        (window.confirm('Item deleted successfully.'))
      } else {
        // Handle errors with the request
        const data = await response.json();
        console.error("Error deleting item:", data.message || "Unknown error");
      }
    } catch (error) {
      // Handle unexpected errors
      console.error("Unexpected error:", error);
    }
  }
  

  const handleSortByDate = () => {
    if (data) {
      const sortedData = Object.entries(data)
        .map(([elementKey, elementData]) => ({ elementKey, ...elementData }))
        .sort((a, b) => new Date(b.date) - new Date(a.date));
  
      setSortedData(sortedData);
    }
  };

  return (
    <div className="App">
      <h1 style={titleStyle}>MapSpotter</h1>
      <nav className="navbar navbar-expand-lg navbar-light bg-light">
    <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span className="navbar-toggler-icon"></span>
    </button>
    <div className="collapse navbar-collapse" id="navbarNav">
      <ul className="navbar-nav mr-auto">
        <li className="nav-item active">
          <a className="nav-link" href="#">Place</a>
        </li>
        <li className="nav-item">
          <a className="nav-link" href="#">Maps</a>
        </li>
      </ul>
      {/* This button will be displayed in the top right corner when "Place" is active */}
      <button onClick={handleSortByDate} type="button" className="btn btn-primary navbar-right-button">Sort by Date</button>
    </div>
  </nav>
      <div className="data-container">
        {sortedData ? (
          Object.values(sortedData).map((elementData, index) => (
            <DataCard
              key={index}
              data={elementData}
              onDelete={() => handleDelete(elementData.elementKey)}
            />
          ))
        ) : (
          <pre>{JSON.stringify(data, null, 2)}</pre>
        )}
      </div>
    </div>
  );
}

const titleStyle = {
  fontFamily: 'Arial, sans-serif',
  color: '#3498db',
  textShadow: '2px 2px 4px rgba(0, 0, 0, 0.2)',
};

export default App;