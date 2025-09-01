import React from 'react';
import { Link } from 'react-router-dom';

const ProductCard = ({ product }) => {
  return (
    <Link to={`/product/${product.name}`} className="card h-100 text-decoration-none text-dark">
      <img 
        src={product.image} 
        className="card-img-top" 
        alt={product.name}
        style={{ height: '200px', objectFit: 'cover' }}
      />
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-center">
          <span className="h5 mb-0 text-primary">{product.price} ₽</span>
        </div>
        <h5 className="card-title mb-0">{product.name}</h5>
        {product.rating ? (
          <div className="mt-2 mb-2">
            <div className="small text-muted mt-1">
              <span className="text-warning">★</span> {product.rating} • ({product.reviewCnt})
            </div>
          </div>
        ) : (
          <div className="small text-muted mt-2 mb-2">
            Нет отзывов
          </div>
        )}
      </div>
    </Link>
  );
};

export default ProductCard;