import React, { useState } from 'react';
import { useParams } from 'react-router-dom';

const ProductEdit = () => {
  const { id } = useParams();
  const [formData, setFormData] = useState({
    name: 'Смартфон',
    price: '29999',
    description: 'Мощный смартфон',
    category: 'electronics'
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Обновление товара:', id, formData);
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  return (
    <div className="container my-4">
      <h1 className="mb-4">Редактирование товара #{id}</h1>
      
      <form onSubmit={handleSubmit} className="card p-4">
        {/* Такая же форма как в создании */}
        <div className="row">
          <div className="col-md-6 mb-3">
            <label className="form-label">Название товара</label>
            <input
              type="text"
              className="form-control"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>
          
          <div className="col-md-6 mb-3">
            <label className="form-label">Цена (₽)</label>
            <input
              type="number"
              className="form-control"
              name="price"
              value={formData.price}
              onChange={handleChange}
              required
            />
          </div>
        </div>

        <div className="mb-3">
          <label className="form-label">Описание</label>
          <textarea
            className="form-control"
            name="description"
            rows="3"
            value={formData.description}
            onChange={handleChange}
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Категория</label>
          <select
            className="form-select"
            name="category"
            value={formData.category}
            onChange={handleChange}
          >
            <option value="electronics">Электроника</option>
            <option value="clothing">Одежда</option>
            <option value="books">Книги</option>
          </select>
        </div>

        <div className="d-flex gap-2">
          <button type="submit" className="btn btn-primary">
            Сохранить изменения
          </button>
          <button type="button" className="btn btn-secondary">
            Отмена
          </button>
        </div>
      </form>
    </div>
  );
};

export default ProductEdit;