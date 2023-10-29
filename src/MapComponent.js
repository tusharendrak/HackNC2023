// MapComponent.js
import React from 'react';
import GoogleMapReact from 'google-map-react';

const MapComponent = ({ locations }) => {
    return (
        <div style={{ height: '100vh', width: '100%' }}>
            <GoogleMapReact
                defaultCenter={{ lat: 35.9049, lng: -79.0469 }} // UNC Chapel Hill coordinates
                defaultZoom={12}
            >
                {locations.map((location, index) => (
                    <Marker
                        key={index}
                        lat={(location.lat)? location.lat: 35.9049}
                        lng={location.lng }
                        text={location.description}
                    />
                ))}
            </GoogleMapReact>
        </div>
    );
};

function Marker() {
    return (
      <div style={{
        width: '10px',
        height: '10px',
        backgroundColor: 'red',
        borderRadius: '50%'
      }}></div>
    );
  }

export default MapComponent;
