import React from 'react';
import ProductCard from '../components/ProductCard';

const Home = () => {
    
  const products = [
    {
      id: 1,
      name: "Смартфон",
      description: "Мощный смартфон с отличной камерой",
      price: 29999,
      image: "https://via.placeholder.com/300x200?text=Смартфон",
      rating: 10,
      reviewCnt: 1
    },
    {
      id: 2,
      name: "Ноутбук",
      description: "Легкий и производительный ноутбук",
      price: 59999,
      image: "https://via.placeholder.com/300x200?text=Ноутбук"
    },
    {
      id: 3,
      name: "Наушники",
      description: "Беспроводные наушники с шумоподавлением",
      price: 8999,
      image: "https://via.placeholder.com/300x200?text=Наушники"
    },
    {
      id: 4,
      name: "Часы",
      description: "Умные часы с отслеживанием здоровья",
      price: 15999,
      image: "https://via.placeholder.com/300x200?text=Часы"
    },
    {
      id: 5,
      name: "Планшет",
      description: "Универсальный планшет для работы и развлечений",
      price: 24999,
      image: "https://via.placeholder.com/300x200?text=Планшет"
    },
    {
      id: 6,
      name: "Фотоаппарат",
      description: "Зеркальный фотоаппарат для профессиональной съемки",
      price: 45999,
      image: "https://via.placeholder.com/300x200?text=Фотоаппарат"
    }
  ];

  return (
    <div className="container my-4">
      <h2 className="text-center mb-4">Last added</h2>
      <div className="row">
        {products.map(product => (
          <div key={product.id} className="col-md-4 col-lg-3 mb-4">
            <ProductCard product={product} />
          </div>
        ))}
      </div>
    </div>
  );
};

export default Home;