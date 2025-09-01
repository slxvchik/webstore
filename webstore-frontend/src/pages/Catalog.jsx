import React from 'react';
import { useParams, useLocation } from 'react-router-dom';

const Catalog = () => {
  const { '*': path } = useParams();
  const location = useLocation();

  // Разбиваем путь на части для хлебных крошек
  const pathParts = path ? path.split('/').filter(Boolean) : [];

  return (
    <div className="container my-4">
      {/* Хлебные крошки */}
      <nav aria-label="breadcrumb" className="mb-4">
        <ol className="breadcrumb">
          <li className="breadcrumb-item">
            <a href="/" className="text-decoration-none">Главная</a>
          </li>
          <li className="breadcrumb-item">
            <a href="/catalog" className="text-decoration-none">Каталог</a>
          </li>
          {pathParts.map((part, index) => {
            const currentPath = `/catalog/${pathParts.slice(0, index + 1).join('/')}`;
            const isLast = index === pathParts.length - 1;
            
            return (
              <li key={index} className={`breadcrumb-item ${isLast ? 'active' : ''}`}>
                {isLast ? (
                  part
                ) : (
                  <a href={currentPath} className="text-decoration-none">
                    {part}
                  </a>
                )}
              </li>
            );
          })}
        </ol>
      </nav>

      <h1>Каталог: {pathParts.join(' → ') || 'Все товары'}</h1>
      <p>Текущий путь: {path || 'корень каталога'}</p>
      
      {/* Здесь будет логика отображения товаров по категориям */}
      <div className="row">
        <div className="col-12">
          <p>Содержимое каталога для пути: {location.pathname}</p>
          {/* В будущем здесь будет компонент с товарами */}
        </div>
      </div>
    </div>
  );
};

export default Catalog;