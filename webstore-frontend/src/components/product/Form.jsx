import React, { useState } from 'react';

const ProductForm = ( { url, method, product = null } ) => {

  console.log(url, method, product);

  const [formData, setFormData] = useState({
    name: '',
    price: '',
    description: '',
    quantity: '',
    category: ''
  });

  const [thumbnail, setThumbnail] = useState(null);
  const [gallery, setGallery] = useState([]);
  const [thumbnailPreview, setThumbnailPreview] = useState('');
  const [galleryPreviews, setGalleryPreviews] = useState([]);

  const handleSubmit = (e) => {
    e.preventDefault();
    
    const submitData = new FormData();
    
    Object.keys(formData).forEach(key => {
      submitData.append(key, formData[key]);
    });
    
    if (thumbnail) {
      submitData.append('thumbnail', thumbnail);
    }
    
    gallery.forEach((file, index) => {
      submitData.append(`gallery`, file);
    });
    
    console.log('Данные для создания товара:', submitData);
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleThumbnailChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setThumbnail(file);
      const reader = new FileReader();
      reader.onload = (e) => setThumbnailPreview(e.target.result);
      reader.readAsDataURL(file);
    }
  };

  const handleGalleryChange = (e) => {
    const files = Array.from(e.target.files);
    if (files.length > 0) {
      setGallery(prev => [...prev, ...files]);
      
      files.forEach(file => {
        const reader = new FileReader();
        reader.onload = (e) => {
          setGalleryPreviews(prev => [...prev, e.target.result]);
        };
        reader.readAsDataURL(file);
      });
    }
  };

  const removeGalleryImage = (index) => {
    setGallery(prev => prev.filter((_, i) => i !== index));
    setGalleryPreviews(prev => prev.filter((_, i) => i !== index));
  };

  return (
    <form onSubmit={handleSubmit} className="card p-4" encType="multipart/form-data">
        <div className="row">
            <div className="col-md-6 mb-3">
                <label className="form-label">Название товара *</label>
                <input
                    type="text"
                    className="form-control"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    required
                />
            </div>
            
            <div className="col-md-3 mb-3">
                <label className="form-label">Цена (₽) *</label>
                <input
                    type="number"
                    step="0.01"
                    className="form-control"
                    name="price"
                    value={formData.price}
                    onChange={handleChange}
                    required
                />
            </div>

            <div className="col-md-3 mb-3">
                <label className="form-label">Количество *</label>
                <input
                    type="number"
                    className="form-control"
                    name="quantity"
                    value={formData.quantity}
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
            <label className="form-label">Категория *</label>
            <select
            className="form-select"
            name="category"
            value={formData.category}
            onChange={handleChange}
            required
            >
            <option value="">Выберите категорию</option>
            <option value="1">Электроника</option>
            <option value="2">Одежда</option>
            <option value="3">Книги</option>
            </select>
        </div>

        <div className="mb-4">
            <label className="form-label">Обложка товара *</label>
            <input
                type="file"
                className="form-control"
                accept="image/*"
                onChange={handleThumbnailChange}
                required
            />
            {thumbnailPreview && (
                <div className="mt-2">
                    <img 
                    src={thumbnailPreview} 
                    alt="Превью обложки" 
                    className="img-thumbnail"
                    style={{ maxHeight: '200px' }}
                    />
                </div>
            )}
        </div>

        {/* Поле для галереи */}
        <div className="mb-4">
            <label className="form-label">Галерея изображений</label>
            
            {/* Кастомная кнопка для выбора файлов */}
            <div className="border rounded p-3 text-center bg-light">
            <input
                type="file"
                className="d-none"
                accept="image/*"
                multiple
                onChange={handleGalleryChange}
                id="gallery-input"
            />
            <label 
                htmlFor="gallery-input" 
                className="btn btn-outline-primary mb-2"
                style={{ cursor: 'pointer' }}
            >
                <i className="bi bi-plus-circle me-2"></i>
                Выбрать файлы
            </label>
            
            {/* Информация о выбранных файлах */}
            <div className="mt-2">
                <small className={gallery.length > 0 ? "text-success" : "text-muted"}>
                <i className={gallery.length > 0 ? "bi bi-check-circle-fill text-success me-1" : "bi bi-info-circle me-1"}></i>
                {gallery.length > 0 ? (
                    `Загружено файлов: ${gallery.length}`
                ) : (
                    'Файлы не выбраны'
                )}
                </small>
            </div>
            </div>

            {/* Превью изображений */}
            {galleryPreviews.length > 0 && (
            <div className="mt-3">
                <h6>Загруженные изображения:</h6>
                <div className="row">
                {galleryPreviews.map((preview, index) => (
                    <div key={index} className="col-2 mb-3 position-relative mx-3">
                        <img 
                            src={preview} 
                            alt={`Галерея ${index + 1}`}
                            className="img-thumbnail rounded"
                            style={{ 
                            height: '100px', 
                            width: '100%',
                            objectFit: 'cover' 
                            }}
                        />
                        <button
                            type="button"
                            className="btn btn-danger btn-sm position-absolute top-0 end-0"
                            onClick={() => removeGalleryImage(index)}
                            style={{ 
                            transform: 'translate(50%, -50%)',
                            borderRadius: '50%',
                            width: '24px',
                            height: '24px',
                            padding: '0',
                            fontSize: '12px'
                            }}
                        >
                            ×
                        </button>
                    </div>
                ))}
                </div>
            </div>
            )}
        </div>

        <div className="d-flex gap-2">
            <button type="submit" className="btn btn-primary">
            Сохранить
            </button>
            <button type="button" className="btn btn-secondary">
            Отмена
            </button>
        </div>
    </form>
  );
};

export default ProductForm;