package com.qa.ims.persistence.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.qa.ims.persistence.domain.Order;
import com.qa.ims.utils.DBUtils;

public class OrderDAO implements Dao<Order> {

	public static final Logger LOGGER = LogManager.getLogger();

	@Override
	public Order modelFromResultSet(ResultSet resultSet) throws SQLException {
		Long id = resultSet.getLong("order_id");
		Long itemId = resultSet.getLong("item_id");
		Long customerId = resultSet.getLong("customer_id");
		int quantity = resultSet.getInt("quantity");
		return new Order(id, customerId, itemId, quantity);
	}
	
	public Order modelFromResultSetOrder(ResultSet resultSet) throws SQLException {
		Long id = resultSet.getLong("order_id");
		Long itemId = resultSet.getLong("item_id");
		int quantity = resultSet.getInt("quantity");
		return new Order(id, itemId, quantity);
	}
	
	public Order modelFromResultSetItem(ResultSet resultSet) throws SQLException {
		Long id = resultSet.getLong("order_id");
		Long customerId = resultSet.getLong("customer_id");
		return new Order(id, customerId);		
	}
	

	@Override
	public List<Order> readAll() {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT o.order_id, o.customer_id, oi.item_id, oi.quantity "
						+ "FROM orders o "
						+ "JOIN orders_items oi ON o.order_id = oi.order_id;");) {
			List<Order> orders = new ArrayList<>();
			while (resultSet.next()) {
				orders.add(modelFromResultSet(resultSet));
			}
			return orders;
		} catch (SQLException e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return new ArrayList<>();
	}

	@Override
	public Order read(Long id) {
		try (Connection connection = DBUtils.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT * FROM orders_items WHERE order_id = ?");) {
			statement.setLong(1, id);
			try (ResultSet resultSet = statement.executeQuery();) {
				resultSet.next();
				return modelFromResultSetOrder(resultSet);
			}
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}
	
	public Order readLatest() {
		try (Connection connection = DBUtils.getInstance().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM orders ORDER BY order_id DESC LIMIT 1");) {
			resultSet.next();
			return modelFromResultSetItem(resultSet);
		} catch (Exception e) {
			LOGGER.debug(e);
			LOGGER.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Order create(Order t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order update(Order t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(long id) {
		// TODO Auto-generated method stub
		return 0;
	}

}
