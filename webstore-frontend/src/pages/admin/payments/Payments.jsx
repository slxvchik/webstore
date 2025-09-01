import React from 'react';

const AdminPayments = () => {
  const payments = [
    { id: 5001, orderId: 1001, amount: 29999, method: 'карта', status: 'успешно' },
    { id: 5002, orderId: 1002, amount: 59999, method: 'карта', status: 'в обработке' },
    { id: 5003, orderId: 1003, amount: 8999, method: 'наличные', status: 'ожидание' }
  ];

  return (
    <div className="container my-4">
      <h1 className="mb-4">Управление оплатами</h1>
      
      <div className="table-responsive">
        <table className="table table-striped">
          <thead>
            <tr>
              <th>ID оплаты</th>
              <th>Заказ</th>
              <th>Сумма</th>
              <th>Способ</th>
              <th>Статус</th>
            </tr>
          </thead>
          <tbody>
            {payments.map(payment => (
              <tr key={payment.id}>
                <td>#{payment.id}</td>
                <td>Заказ #{payment.orderId}</td>
                <td>{payment.amount} ₽</td>
                <td>{payment.method}</td>
                <td>
                  <span className={`badge ${
                    payment.status === 'успешно' ? 'bg-success' :
                    payment.status === 'в обработке' ? 'bg-warning' :
                    payment.status === 'ожидание' ? 'bg-info' : 'bg-secondary'
                  }`}>
                    {payment.status}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default AdminPayments;