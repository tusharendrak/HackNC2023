import React from 'react';

function DataCard({ data, onDelete }) {
  const { date, description, imageUrl, lattitude, longitude, addressLine1  } = data;

  const handleDelete = () => {
    if (window.confirm('Are you sure you want to fix this cluster?')) {
      onDelete();
    }
  };

  return (
    <div className="col-md-4 my-2">
      <div className="card h-100 custom-card">
        <img src={imageUrl} alt={description} className="card-img-top" />
        <div className="card-body">
          <h5 className="card-title">Date : {date}</h5>
          <p className="card-text">Description : {description}</p>
          <p className="card-text">Latitude : {lattitude}</p>
          <p className="card-text">Longitude : {longitude}</p>
          <p className="card-text">Address : {addressLine1}</p>
        </div>
        <div className="card-footer">
          <button onClick={handleDelete} className="btn btn-danger btn-block">Fix this cluster</button>
        </div>
      </div>
    </div>
  );
  
  
}

export default DataCard;