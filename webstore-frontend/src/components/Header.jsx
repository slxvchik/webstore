import React from 'react';
import { Link } from 'react-router-dom';

const Header = () => {
  return (
    <header className="bg-dark text-white p-3 sticky-top">
      <div className="container">
        <div className="d-flex justify-content-between align-items-center">
          <div className="d-flex align-items-center w-75 me-2">
            <Link to="/" className="h4 mb-0 me-3">Webstore</Link>
            <button className="btn btn-primary me-3">
              Catalog
            </button>
            <div className="w-100">
                <input 
                    type="text" 
                    className="form-control" 
                    placeholder="Search..." 
                />
            </div>
          </div>
          
          <div className="d-flex">
            <button className="btn btn-primary me-3">
              Login
            </button>
            <button className="btn btn-primary me-3">
                Register
            </button>
          </div>
        </div>
      </div>
    </header>
  );
};

export default Header;