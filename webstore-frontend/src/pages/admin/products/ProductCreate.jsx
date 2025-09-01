import React, { useState } from 'react';
import ProductForm from '../../../components/product/form';

const AdminCreateProduct = () => {

  return (
    <div className="container my-4">
      <h1 className="mb-4">Создание товара</h1>
      
      <ProductForm url="/api/v1/products" method="POST"></ProductForm>

    </div>
  );
};

export default AdminCreateProduct;