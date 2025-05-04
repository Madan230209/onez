<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>Recent Orders</h1>
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Order ID</th>
          <th>Status</th>
          <th>Address</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>John Rai</td>
          <td>1001</td>
          <td>
            <select class="status-dropdown">
              <option value="pending">Pending</option>
              <option value="processing" selected>Processing</option>
              <option value="shipped">Shipped</option>
              <option value="delivered">Delivered</option>
            </select>
          </td>
          <td>Baluwatar,Kathmandu</td>
        </tr>
        <tr>
          <td>Dikshant Timsina</td>
          <td>1002</td>
          <td>
            <select class="status-dropdown">
              <option value="pending">Pending</option>
              <option value="processing" selected>Processing</option>
              <option value="shipped">Shipped</option>
              <option value="delivered">Delivered</option>
            </select>
          </td>
          <td>Jhapa</td>
        </tr>
        <tr>
          <td>Pratyush Badal</td>
          <td>1003</td>
          <td>
            <select class="status-dropdown">
              <option value="pending">Pending</option>
              <option value="processing" selected>Processing</option>
              <option value="shipped">Shipped</option>
              <option value="delivered">Delivered</option>
            </select>
          </td>
          <td>Kavre</td>
        </tr>
        <tr>
          <td>Rijan Buddhacharya</td>
          <td>1004</td>
          <td>
            <select class="status-dropdown">
              <option value="pending">Pending</option>
              <option value="processing" selected>Processing</option>
              <option value="shipped">Shipped</option>
              <option value="delivered">Delivered</option>
            </select>
          </td>
          <td>Pokhara</td>
        </tr>
      </tbody>
    </table>
</body>
</html>