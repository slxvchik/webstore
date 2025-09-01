import React from 'react';

const Orders = () => {
  const orders = [
    { id: 1001, customer: 'Иван Иванов', total: 29999, status: 'обработан' },
    { id: 1002, customer: 'Мария Петрова', total: 59999, status: 'доставляется' },
    { id: 1003, customer: 'Алексей Сидоров', total: 8999, status: 'новый' }
  ];

  return (
    <div className="container my-4">
      <h1 className="mb-4">Управление заказами</h1>
      
      <div className="table-responsive">
        <table className="table table-striped">
          <thead>
            <tr>
              <th>ID заказа</th>
              <th>Клиент</th>
              <th>Сумма</th>
              <th>Статус</th>
              <th>Действия</th>
            </tr>
          </thead>
          <tbody>
            {orders.map(order => (
              <tr key={order.id}>
                <td>#{order.id}</td>
                <td>{order.customer}</td>
                <td>{order.total} ₽</td>
                <td>
                  <span className={`badge ${
                    order.status === 'новый' ? 'bg-warning' :
                    order.status === 'обработан' ? 'bg-info' :
                    order.status === 'доставляется' ? 'bg-success' : 'bg-secondary'
                  }`}>
                    {order.status}
                  </span>
                </td>
                <td>
                  <button className="btn btn-sm btn-outline-primary">
                    Подробнее
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

export default Orders;