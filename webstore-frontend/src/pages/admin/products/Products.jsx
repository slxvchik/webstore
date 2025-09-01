import React from 'react';
import { Link } from 'react-router-dom';

const Products = () => {
  // Заглушка с данными
  const products = [
    { id: 1, name: 'Смартфон', price: 29999, stock: 15 },
    { id: 2, name: 'Ноутбук', price: 59999, stock: 8 },
    { id: 3, name: 'Наушники', price: 8999, stock: 25 }
  ];

  return (
    <div className="container my-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Управление товарами</h1>
        <Link to="/admin/products/create" className="btn btn-primary">
          + Добавить товар
        </Link>
      </div>

      <div className="table-responsive">
        <table className="table table-striped">
          <thead>
            <tr>
              <th>ID</th>
              <th>Название</th>
              <th>Цена</th>
              <th>Остаток</th>
              <th>Действия</th>
            </tr>
          </thead>
          <tbody>
            {products.map(product => (
              <tr key={product.id}>
                <td>{product.id}</td>
                <td>{product.name}</td>
                <td>{product.price} ₽</td>
                <td>{product.stock} шт.</td>
                <td>
                  <Link 
                    to={`/admin/products/edit/${product.id}`}
                    className="btn btn-sm btn-outline-primary me-2"
                  >
                    Редактировать
                  </Link>
                  <button className="btn btn-sm btn-outline-danger">
                    Удалить
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Products;