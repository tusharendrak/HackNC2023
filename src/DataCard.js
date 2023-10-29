import React from 'react';

const cardStyle = {
  margin: '10px',
  borderRadius: '10px',
  boxShadow: '0 4px 12px rgba(0, 0, 0, 0.1)',
  backgroundColor: '#ffffff',
  transition: 'transform 0.2s', // smooth transition on hover
};

const imageStyle = {
  width: '100%', // This ensures the image scales based on the card's width
  maxHeight: '180px', // Adjusted image height
  objectFit: 'cover', // This ensures the image scales appropriately
  borderTopLeftRadius: '10px',
  borderTopRightRadius: '10px',
};

const footerStyle = {
  backgroundColor: '#f4f4f4',
};

function DataCard({ data, onDelete }) {
  const { date, description, imageUrl, lattitude, longitude, addressLine1 } = data;

  const handleDelete = () => {
    if (window.confirm('Are you sure you want to fix this cluster?')) {
      onDelete();
    }
  };

  return (
    <div className="col-md-4 col-sm-6 my-2">
      <div 
        className="card h-100 custom-card shadow" 
        style={cardStyle}
      >

        <img src={imageUrl} alt={description} className="card-img-top rounded" style={imageStyle} />
        <div className="card-body">
          <h5 className="card-title">Date : {date}</h5>
          <p className="card-text text-muted">Description : {description}</p>
          <p className="card-text text-muted">Latitude : {lattitude}</p>
          <p className="card-text text-muted">Longitude : {longitude}</p>
          <p className="card-text text-muted">Address : {addressLine1}</p>
        </div>
        <div className="card-footer" style={footerStyle}>
          <button onClick={handleDelete} className="btn btn-danger btn-block">Fix this cluster</button>
        </div>
      </div>
    </div>
  );
}

export default DataCard;
