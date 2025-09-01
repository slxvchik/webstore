import React, { useState } from "react";
import { useParams } from "react-router-dom";

const Product = () => {
  const { id } = useParams();
  const [activeTab, setActiveTab] = useState('info');

  // Пример данных товара
  const product = {
    id: 1,
    name: "Смартфон Premium X",
    description: "Мощный смартфон с отличной камерой и длительным временем работы батареи",
    price: 29999,
    rating: 4.6,
    reviewCount: 24,
    images: [
      "https://www.apple.com/v/iphone-16/f/images/meta/iphone-16_overview__fcivqu9d5t6q_og.png?202508071452",
      "https://www.apple.com/v/iphone-16/f/images/meta/iphone-16_overview__fcivqu9d5t6q_og.png?202508071453",
      "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSHkToLvSt0MytfF283TROIyz6dkjDRySa3Lg&s",
      "https://myshamil.com/wp-content/uploads/2025/05/a-16-pink.jpg",
      "https://img.kleinanzeigen.de/api/v1/prod-ads/images/e0/e03910f1-8a67-4814-b6f1-53808e40145b?rule=$_59.AUTO"
    ],
    specifications: {
      "Экран": "6.7 дюймов, AMOLED",
      "Процессор": "Snapdragon 888",
      "Память": "128 ГБ",
      "Камера": "108 МП + 12 МП + 8 МП",
      "Батарея": "5000 мАч",
      "Батарея1": "5000 мАч",
      "Батарея2": "5000 мАч",
      "Батарея3": "5000 мАч",
      "Батарея4": "5000 мАч",
      "Батарея5": "5000 мАч",
      "Батарея6": "5000 мАч"
    },
    reviews: [
      { id: 1, user: "Иван", rating: 4, comment: "Отличный телефон!", date: "2024-01-15" },
      { id: 2, user: "Мария", rating: 5, comment: "Хорошая камера, но тяжеловат", date: "2024-01-10" }
    ]
  };

  const [selectedImage, setSelectedImage] = useState(product.images[0]);

  const renderRatingStars = (rating) => {
    const fullStars = Math.round(rating);
    const emptyStars = 5 - fullStars;

    return (
      <div className="d-flex">
        {[...Array(fullStars)].map((_, i) => (
          <span key={i} className="text-warning">★</span>
        ))}
        {[...Array(emptyStars)].map((_, i) => (
          <span key={`empty-${i}`} className="text-muted">☆</span>
        ))}
      </div>
    );
  };

  return (
    <div className="container my-4">

      <nav aria-label="breadcrumb" className="mb-4">
        <ol className="breadcrumb">
        <li className="breadcrumb-item">
            <a href="/" className="text-decoration-none">Главная</a>
        </li>
        <li className="breadcrumb-item">
            <a href="/catalog" className="text-decoration-none">Каталог</a>
        </li>
        <li className="breadcrumb-item">
            <a href="/catalog/electronics" className="text-decoration-none">Электроника</a>
        </li>
        <li className="breadcrumb-item">
            <a href="/catalog/electronics/smartphones" className="text-decoration-none">Смартфоны</a>
        </li>
        <li className="breadcrumb-item active" aria-current="page">
            {product.name}
        </li>
        </ol>
      </nav>

      <div className="row">
        <div className="col-md-6">
            <div className="position-relative mb-3 text-center" style={{ height: '600px' }}>
                <img 
                    src={selectedImage} 
                    alt={product.name}
                    className="img-fluid rounded"
                    style={{ 
                        width: '100%', 
                        height: '100%', 
                        objectFit: 'contain',
                        backgroundColor: '#f8f9fa'
                    }}
                />

                {product.images.length > 1 && (
                    <button
                        className="position-absolute top-50 start-0 translate-middle-y btn btn-dark btn-sm shadow mx-2"
                        style={{ zIndex: 10 }}
                        onClick={() => {
                            const currentIndex = product.images.indexOf(selectedImage);
                            const prevIndex = (currentIndex - 1 + product.images.length) % product.images.length;
                            setSelectedImage(product.images[prevIndex]);
                        }}
                    >
                    ‹
                    </button>
                )}
                
                {product.images.length > 1 && (
                    <button
                        className="position-absolute top-50 end-0 translate-middle-y btn btn-dark btn-sm shadow mx-2"
                        style={{ zIndex: 10 }}
                        onClick={() => {
                            const currentIndex = product.images.indexOf(selectedImage);
                            const nextIndex = (currentIndex + 1) % product.images.length;
                            setSelectedImage(product.images[nextIndex]);
                        }}
                    >
                    ›
                    </button>
                )}

            </div>

            <div className="d-flex overflow-auto pb-2" style={{ minHeight: '90px' }}>
                {product.images.map((image, index) => (
                    <div key={index} className="flex-shrink-0 me-2" style={{ width: '80px', height: '80px' }}>
                    <img 
                        src={image} 
                        alt={`${product.name} ${index + 1}`}
                        className={`img-fluid rounded cursor-pointer ${selectedImage === image ? 'border border-primary' : 'border'}`}
                        style={{ 
                        width: '100%', 
                        height: '100%', 
                        objectFit: 'contain',
                        backgroundColor: '#f8f9fa'
                        }}
                        onClick={() => setSelectedImage(image)}
                        role="button"
                    />
                    </div>
                ))}
            </div>

            {product.images.length > 1 && (
                <div className="text-center mt-2">
                    <small className="text-muted">
                        {product.images.indexOf(selectedImage) + 1} / {product.images.length}
                    </small>
                </div>
            )}

        </div>

        <div className="col-md-6">
          <h1 className="h2 mb-3">{product.name}</h1>
          
          <div className="d-flex align-items-center mb-3">
            {renderRatingStars(product.rating)}
            <span className="ms-2 text-muted">
              {product.rating}/5 • ({product.reviewCount} отзывов)
            </span>
          </div>

          <div className="mb-4">
            <h2 className="text-primary">{product.price.toLocaleString()} ₽</h2>
          </div>

          <p className="text-muted mb-4">{product.description}</p>

          <div className="d-grid gap-2 d-md-flex mb-4">
            <button className="btn btn-primary btn-lg flex-grow-1">
              <i className="bi bi-cart-plus me-2"></i>
              Добавить в корзину
            </button>
          </div>
        </div>

      </div>

      <div className="row mt-5">
        <div className="col-12">
          <ul className="nav nav-tabs">
            <li className="nav-item">
              <button 
                className={`nav-link ${activeTab === 'info' ? 'active' : ''}`}
                onClick={() => setActiveTab('info')}
              >
                Информация о товаре
              </button>
            </li>
            <li className="nav-item">
              <button 
                className={`nav-link ${activeTab === 'reviews' ? 'active' : ''}`}
                onClick={() => setActiveTab('reviews')}
              >
                Отзывы ({product.reviewCount})
              </button>
            </li>
          </ul>

          <div className="tab-content p-3 border border-top-0 rounded-bottom">
            {/* Вкладка с информацией */}
            {activeTab === 'info' && (
              <div className="tab-pane fade show active">
                <h4>Характеристики</h4>
                <div className="row">
                  {Object.entries(product.specifications).map(([key, value]) => (
                    <div key={key} className="col-md-6 mb-2">
                      <strong>{key}:</strong> {value}
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* Вкладка с отзывами */}
            {activeTab === 'reviews' && (
              <div className="tab-pane fade show active">
                <h4>Отзывы покупателей</h4>
                {product.reviews.length > 0 ? (
                  product.reviews.map(review => (
                    <div key={review.id} className="card mb-3">
                      <div className="card-body">
                        <div className="d-flex justify-content-between align-items-center mb-2">
                          <h5 className="card-title mb-0">{review.user}</h5>
                          <small className="text-muted">{review.date}</small>
                        </div>
                        {renderRatingStars(review.rating)}
                        <p className="card-text mt-2">{review.comment}</p>
                      </div>
                    </div>
                  ))
                ) : (
                  <p className="text-muted">Пока нет отзывов</p>
                )}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
    );
};

export default Product;