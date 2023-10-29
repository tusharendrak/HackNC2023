import React, { useState, useEffect } from 'react';
import { getDatabase, ref, onValue } from 'firebase/database';
import firebaseApp from './firebaseConfig'; // Import the Firebase configuration from your file
import DataCard from './DataCard';
import MapComponent from './MapComponent';

const db = getDatabase(firebaseApp);

const PRIMARY_COLOR = '#3498db';
const SECONDARY_COLOR = '#e74c3c';
const BACKGROUND_COLOR = '#f3f4f6';
const NAVBAR_COLOR = '#2c3e50';

function App() {
  const [data, setData] = useState(null);
  const [sortedData, setSortedData] = useState(null); // New state variable for sorted data
  const [activeTab, setActiveTab] = useState('reports');
  const [locations, setLocations] = useState([]); 



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
    const fetchLocations = () => {
      const uploadsRef = ref(db, 'uploads');
      onValue(uploadsRef, (snapshot) => {
        const data = snapshot.val();
        const locationList = Object.values(data).map(entry => ({
          lat: entry.latitude,
          lng: entry.longitude,
          description: entry.description
        }));
        setLocations(locationList);
      });
    };
  
    fetchLocations();
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
        (window.alert('Item deleted successfully.'))
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
    <div className="App container mt-4" style={{ backgroundColor: BACKGROUND_COLOR, borderRadius: '8px', boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)' }}>
     <a href="http://localhost:3000" style={{ textDecoration: 'none', color: 'inherit' }}>
      <h1>
      {/* <img src="crow.svg" alt="Logo" style={{ verticalAlign: 'middle' }} /> */}
        HackNC</h1>
    </a>
      <nav className="navbar navbar-expand-lg navbar-light bg-light" style={{ backgroundColor: NAVBAR_COLOR, borderRadius: '8px' }}>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav mr-auto">
            <li className={`nav-item ${activeTab === 'reports' ? 'active' : ''}`}>
              <a className="nav-link " href="#" style={{ color: PRIMARY_COLOR }} onClick={(e) => { e.preventDefault(); setActiveTab('reports'); }}>Reports</a>
            </li>
            <li className={`nav-item ${activeTab === 'maps' ? 'active' : ''}`}>
              <a className="nav-link" href="#" style={{ color: PRIMARY_COLOR }} onClick={(e) => { e.preventDefault(); setActiveTab('maps'); }}>Maps</a>
            </li>
          </ul>
          {activeTab === 'reports' && (
            <button onClick={handleSortByDate} type="button" className="btn btn-primary ml-auto" style={{ backgroundColor: SECONDARY_COLOR, borderColor: SECONDARY_COLOR }} >Sort by Date</button>
          )}
        </div>
      </nav>

      {activeTab === 'reports' && (
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
      )}

      {activeTab === 'maps' && (
        <div style={{ height: '100vh', width: '100%' }}>
          <MapComponent locations={locations} />
        </div>
      )}
    </div>
  );
}

const titleStyle = {
  fontFamily: 'Arial, sans-serif',
  color: 'white',
  textShadow: '2px 2px 4px rgba(0, 0, 0, 0.2)',
  textAlign: 'center',
  padding: '10px',
  backgroundColor: 'black',
  borderRadius: '8px',
  boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)'
};

export default App;