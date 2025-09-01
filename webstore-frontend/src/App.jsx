import './App.css'
import { Routes, Route, Router } from "react-router-dom";
import Header from "./components/Header";
import Home from "./pages/Home";
import Product from "./pages/Product";
import Login from './pages/Login';
import Register from './pages/Register';
import Catalog from './pages/Catalog';
import AdminProducts from './pages/admin/products/Products';
import AdminProuctCreate from './pages/admin/products/ProductCreate';
import AdminProuctEdit from './pages/admin/products/ProductEdit';
import AdminOrders from './pages/admin/orders/Orders';
import AdminPayments from './pages/admin/payments/Payments';

function App() {

  return (
    <>
      <Header></Header>
      <main>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route path="/" element={<Home />} />
          <Route path="/product/:id" element={<Product />} />
          <Route path="/catalog/*" element={<Catalog />} />
          
          <Route path="/admin/products" element={<AdminProducts />} />
          <Route path="/admin/products/create" element={<AdminProuctCreate />} />
          <Route path="/admin/products/:id/edit" element={<AdminProuctEdit />} />

          <Route path="/admin/orders" element={<AdminOrders />} />
          <Route path="/admin/payments" element={<AdminPayments />} />

        </Routes>
      </main>
    </>
  )
}

export default App
